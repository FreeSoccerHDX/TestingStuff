package freesoccerhdx.survivalplus.npc;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.logging.log4j.core.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;

import freesoccerhdx.survivalplus.haupt.Holo;
import freesoccerhdx.survivalplus.haupt.main;
import freesoccerhdx.survivalplus.turtle.Hologramm;
import net.minecraft.server.v1_16_R3.*;
import net.minecraft.server.v1_16_R3.DataWatcher.Item;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_16_R3.ScoreboardServer.Action;

public class NPCPlayer{

	private int taskid = -1;
	
	private List<Player> cansee = new ArrayList<>();
	private String name = "";
	private String tablistname = "";
	private Location loc = null;
	private boolean spawned = false;
	private boolean isOnTablist = false;
	private boolean gravity = true;
	private byte actiondata = 0;
	private boolean sprint = false;
	private boolean fire = false;
	private boolean crouched = false;
	private boolean invisible = false;
	private boolean glowing = false;
	private UUID preuuid = null;
	
	private Vector velocity = new Vector(0,0,0);
	
	private EntityPlayer ep = null;
	private String texture = "";
	private String signature = "";
	private EntityPose entitypose = null;
	private HashMap<EnumItemSlot,net.minecraft.server.v1_16_R3.ItemStack> equipment = new HashMap<>();
	
	
	private org.bukkit.entity.LivingEntity klon = null;
	private EntityType klontype = null;
	
	private Double health = 20.0;
	private Double maxhealth = 20.0;
	
	private int respawnticks = -1;
	private int tickcounter = 0;
	private Holo respawntimer = null;
	
	public NPCPlayer(String npcname, String tablistname, Location loc) {
		this.name = npcname;
		this.tablistname = tablistname;
		this.loc = loc.clone();
	}
	
	private void calcPathfinder() {
		if(!hasKlon()) return;	
		EntityInsentient ei = (EntityInsentient) ((CraftEntity) getKlon()).getHandle();
		PathfinderGoalSelector goal = ei.goalSelector;
		PathfinderGoalSelector target = ei.targetSelector;
		
		/*
		Iterator<PathfinderGoalWrapped> c = goal.c().iterator(); 
		
		while(c.hasNext()) {
			PathfinderGoalWrapped pgw = c.next();
			
		}*/
		
		NPCMethods.setValue(goal, "c", new EnumMap<>(PathfinderGoal.Type.class));
		NPCMethods.setValue(target, "c", new EnumMap<>(PathfinderGoal.Type.class));
		
		NPCMethods.setValue(goal, "d", Sets.newLinkedHashSet());
		NPCMethods.setValue(target, "d", Sets.newLinkedHashSet());
		
		NPCMethods.setValue(goal, "f", EnumSet.noneOf(PathfinderGoal.Type.class));
		NPCMethods.setValue(target, "f", EnumSet.noneOf(PathfinderGoal.Type.class));

		
		EntityCreature ec = (EntityCreature) ((CraftEntity) getKlon()).getHandle();
	
		if(hasBow()) {
			PathfinderGoalBowShoot<EntitySkeletonAbstract> b = new PathfinderGoalBowShoot((EntityMonster) ec, 1.0D, 20, 15.0F);
			goal.a(4, b);
			
		}else {
			goal.a(1, new PathfinderGoalMeleeAttack(ec, 1.0D, true));
			goal.a(2, new PathfinderGoalMoveTowardsTarget(ec, 0.9D, 32.0F));
		}
	    goal.a(5, new PathfinderGoalRandomStrollLand(ec, 1.0D));
	    goal.a(6, new PathfinderGoalLookAtPlayer(ec, (Class)EntityHuman.class, 8.0F));
	    goal.a(6, new PathfinderGoalRandomLookaround(ec));
	    
	    target.a(1, new PathfinderGoalHurtByTarget(ec, new Class[0]));
	    target.a(2, new PathfinderGoalNearestAttackableTarget<>(ec, EntityInsentient.class, 5, false, false, entityliving -> 
	          (entityliving instanceof IMonster)));


	}
	public void setName(String name) {
		this.name = name;
		despawn(true);
		spawn(isOnTablist());
	}
	public String getName() {
		return name;
	}
	public void setSprinting(boolean sprint) {
		this.sprint = sprint;
		updateAction();
		ep.setSprinting(sprint);
		if(hasKlon()) {
			getKlon().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(sprint ? 0.4 : 0.2);
		}
	}
	public boolean isSprinting() {
		return sprint;
	}
	
	public void damage(Double d) {
		setHealth(Math.max(0, health-d));
	}
	public void setHealth(Double d) {
		if(d > getMaxHealth()) return;
		this.health = d;
		PacketPlayOutScoreboardScore pposc = new PacketPlayOutScoreboardScore(Action.CHANGE, "leben", this.name, health.intValue());
		if(hasKlon()) {
			getKlon().setHealth(getHealth());
		}
		sendPacket(pposc);
	}
	public void setMaxHealth(Double d) {
		this.maxhealth = d;
		if(hasKlon()) {
			getKlon().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
		}
		if(getMaxHealth() > getHealth()) {
			setHealth(getMaxHealth());
		}
	}
	public Double getHealth() {
		return health;
	}
	public Double getMaxHealth() {
		return maxhealth;
	}
	
	public void setPreUUID(UUID uuid) {
		preuuid = uuid;
	}
	
	public boolean isTickTimerRunning() {
		return taskid != -1;
	}
	
	public void stopTickTimer() {
		if(taskid != -1) {
			Bukkit.getScheduler().cancelTask(taskid);
			taskid = -1;
		}
	}

	public void startTickTimer() {
		taskid = Bukkit.getScheduler().runTaskTimer(main.m, ()->applyTick(), 0,1).getTaskId();
	}
	
	public org.bukkit.inventory.ItemStack getBow(){
		org.bukkit.inventory.ItemStack bow = null;
		if(getEquipment().containsKey(EnumItemSlot.MAINHAND) || getEquipment().containsKey(EnumItemSlot.OFFHAND)){
			if(getEquipment().containsKey(EnumItemSlot.MAINHAND)) {
				
				if(NPCMethods.getBukkitItemStack(equipment.get(EnumItemSlot.MAINHAND)).getType() == Material.BOW) {
					bow = NPCMethods.getBukkitItemStack(equipment.get(EnumItemSlot.MAINHAND));
				}
			}else if(getEquipment().containsKey(EnumItemSlot.OFFHAND)) {
				if(NPCMethods.getBukkitItemStack(equipment.get(EnumItemSlot.OFFHAND)).getType() == Material.BOW) {
					bow = NPCMethods.getBukkitItemStack(equipment.get(EnumItemSlot.OFFHAND));
				}
			}
		}
		return bow;
	}
	public boolean hasBow() {
		return getBow() != null;
	}

	public void updateAction() {
		setActionData(new NPCAction(fire, crouched, sprint, invisible, glowing, false));
	}
	
	public void setOnFire(boolean fire) {
		this.fire = fire;
		updateAction();
	}
	public boolean isOnFire() {
		return this.fire;
	}
	
	private void applyTick() {
		tickcounter++;
		if(hasKlon()) {
			
			if(getKlon().getFireTicks() > 0) {
				setOnFire(true);
			}else {
				if(isOnFire()) {
					setOnFire(false);
				}
			}
			
			if(getLocation().getWorld().equals(getKlon().getWorld()) && getLocation().distance(getKlon().getLocation()) < 7.0) {
				this.move(getLocation(), getKlon().getLocation(), getKlon().isOnGround(), false);
			}else {
				this.setLocation(getKlon().getLocation());
			}
			this.setHeadRotation(getKlon().getEyeLocation());

			if(hasBow()) {
				this.sendDataWatcher(((CraftEntity) getKlon()).getHandle().getDataWatcher().get(new DataWatcherObject<>(7, DataWatcherRegistry.a)));
			}
		
			if(tickcounter%80 == 0) {
				if(getHealth() < getMaxHealth()) {
					setHealth(Math.min(getHealth()+1.0, getMaxHealth()));
				}
			}
		}
		if(respawnticks > 0) {
			respawnticks -= 1;
			if(respawntimer == null) {
				respawntimer = new Holo(loc, "Noch "+(respawnticks/20)+"s Tod", respawnticks, getPlayers());
			}else {
				List<Player> torem = new ArrayList<>();
				for(Player in : respawntimer.players) {
					if(!hasPlayer(in)) {
						torem.add(in);
					}
				}
				for(Player rem : torem) {
					respawntimer.remove(rem);
				}
				for(Player out : getPlayers()) {
					if(!respawntimer.players.contains(out)) {
						respawntimer.add(out);
					}
				}
				respawntimer.msg = "Noch "+(respawnticks/20)+"s Tod";
				respawntimer._update();
			}
		}else if(respawntimer != null){
			respawntimer.despawn();
			respawntimer = null;
		}
		if(tickcounter%(20*60) == 0) {
			tickcounter = 0;
		}
	}
	public void setKlonType(EntityType type) {
		klontype = type;
	}
	public EntityType getKlonType() {
		return klontype;
	}
	public boolean hasKlonType() {
		return getKlonType() != null;
	}
	
	public void setKlon(org.bukkit.entity.LivingEntity ent) {
		klon = ent;
	}
	public org.bukkit.entity.LivingEntity getKlon() {
		return klon;
	}
	public boolean hasKlon() {
		return getKlon() != null;
	}
	public Location getLocation() {
		return loc.clone();
	}
	
	public void setGravityAmount(double amount) {
		velocity = velocity.setY(amount);
	}
	public double getGravityAmount() {
		return velocity.getY();
	}
	
	public boolean hasGravity() {
		return gravity;
	}
	public void setGravity(boolean grav) {
		this.gravity = grav;
	}
	
	public boolean hasPlayer(Player p) {
		return this.cansee.contains(p);
	}
	public void addPlayer(Player p) {
		if(hasPlayer(p)) return;
		if(spawned()) {
		    
			PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
			PacketPlayOutNamedEntitySpawn pposel = new PacketPlayOutNamedEntitySpawn(ep);
			PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(ep.getId(), ep.getDataWatcher(), true);
			PacketPlayOutEntityHeadRotation ppoehr = new PacketPlayOutEntityHeadRotation(ep, (byte) ((loc.getYaw() * 256.0F) / 360.0F));
			
			NPCMethods.sendPacket(p,ppopi);
			NPCMethods.sendPacket(p,pposel);
			NPCMethods.sendPacket(p,ppoem);
			NPCMethods.sendPacket(p, ppoehr);
			
			for(EnumItemSlot slot : equipment.keySet()) {
				List<Pair<EnumItemSlot,net.minecraft.server.v1_16_R3.ItemStack>> itlist = new ArrayList<>();
				itlist.add(Pair.of(slot, equipment.get(slot)));
				
				PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(this.ep.getId(), itlist);
				NPCMethods.sendPacket(p,ppoee);
			}
			
			if(!isOnTablist()) {
				PacketPlayOutPlayerInfo ppopi2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
				Bukkit.getScheduler().runTaskLater(main.m, ()->NPCMethods.sendPacket(p, ppopi2), 10);
			}
			if(hasKlon()) {
				NPCMethods.destroyKlon(p, getKlon());
			}
			

		}
		cansee.add(p);
	}
	public void removePlayer(Player p) {
		if(hasPlayer(p)) {
			cansee.remove(p);
			if(spawned()) {
				PacketPlayOutEntityDestroy ppoed = new PacketPlayOutEntityDestroy(this.ep.getId());
				NPCMethods.sendPacket(p, ppoed);
				if(isOnTablist()) {
					PacketPlayOutPlayerInfo ppopi2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
					NPCMethods.sendPacket(p, ppopi2);
				}
			}
		}
	}
	public List<Player> getPlayers(){
		return this.cansee;
	}
	
	public boolean spawned() {
		return this.spawned;
	}
	
	public void setMobEffect(MobEffectList mobeffect, int ticks, int lvl){
		if(spawned()) {
			MobEffect me = new MobEffect(mobeffect, ticks, lvl, true, true, true);
			//ep.addEffect(me);
			PacketPlayOutEntityEffect ppoee = new PacketPlayOutEntityEffect(ep.getId(), me);
			sendPacket(ppoee);
		}
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	// dont work somehow
	public void setVelocity(Vector vec) {
		velocity = vec.clone();
		if(spawned()) {
			ep.getBukkitEntity().setVelocity(velocity);
			ep.setMot(vec.getX(), vec.getY(), vec.getZ());
			ep.velocityChanged = true;
			PacketPlayOutEntityVelocity ppoev = new PacketPlayOutEntityVelocity(ep.getId(), new Vec3D(vec.getX(), vec.getY(), vec.getZ()));
			sendPacket(ppoev);
			if(hasKlon()) {
				getKlon().setVelocity(velocity);
			}
		}
	}
	
	public void setAnimation(NPCAnimation npcanimation) {
		setAnimation((byte)npcanimation.getId());
	}
	
	public void setAnimation(byte animation) {
		PacketPlayOutAnimation ppoa = new PacketPlayOutAnimation(ep, animation);
		sendPacket(ppoa);
	}
	
	public void setActionData(NPCAction action) {
		setActionData(action.build());
	}
	
	public void setActionData(byte data) {
		this.actiondata = data;
		DataWatcher dw = ep.getDataWatcher();
		dw.set(new DataWatcherObject<>(0, DataWatcherRegistry.a), data);
		PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(ep.getId(), ep.getDataWatcher(), true);
		sendPacket(ppoem);
	}
	
	public void spawn(boolean tablist) {
		if(!spawned()) {
			MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer(); //NMS Server
	        WorldServer nmsWorld = ((CraftWorld) loc.getWorld()).getHandle(); //NMS World Server
	        GameProfile gp = new GameProfile(preuuid == null ? UUID.randomUUID() : preuuid, name); //NMS Game Profile
	        
	        gp.getProperties().get("textures").clear();
	        gp.getProperties().put("textures", new Property("textures", texture, signature));
	        
	        this.ep = new EntityPlayer(nmsServer, nmsWorld, gp, new PlayerInteractManager(nmsWorld));
	        //ep.getAttributeInstance(null).setValue(20);
	        //ep.setHealth(20);
	        ep.listName = ChatSerializer.a("{\"text\":\""+ tablistname + "\"}");
	        
	        ep.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	        DataWatcher dw = ep.getDataWatcher();
			dw.set(new DataWatcherObject<>(8, DataWatcherRegistry.c), 20.0f);
			
			PacketPlayOutNamedEntitySpawn pposel = new PacketPlayOutNamedEntitySpawn(ep);
			PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(ep.getId(), ep.getDataWatcher(), true);
			
			addTablist();
			sendPacket(pposel);
			sendPacket(ppoem);
			setHeadRotation(loc);
			
			if(entitypose != null) {
				setPose(entitypose);
			}
			if(equipment.size() > 0) {
				for(EnumItemSlot slot : equipment.keySet()) {
					List<Pair<EnumItemSlot,net.minecraft.server.v1_16_R3.ItemStack>> itlist = new ArrayList<>();
					itlist.add(Pair.of(slot, equipment.get(slot)));
					
					PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(this.ep.getId(), itlist);
					//PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(this.ep.getId(), slot, equipment.get(slot));
					sendPacket(ppoee);
				}
			}
			if(actiondata != 0) {
				setActionData(actiondata);
			}
			if(!tablist) {
				Bukkit.getScheduler().runTaskLater(main.m, ()->removeTablist(true), 10);
			}
			if(hasKlonType() && !hasKlon()) {
				setKlon((LivingEntity) loc.getWorld().spawnEntity(loc, getKlonType()));
				getKlon().setRemoveWhenFarAway(false);
				getKlon().setSilent(true);
				org.bukkit.inventory.ItemStack is = new org.bukkit.inventory.ItemStack(Material.GOLDEN_HELMET);
				ItemMeta meta = is.getItemMeta();
				meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
				meta.setUnbreakable(true);
				is.setItemMeta(meta);
				for(EnumItemSlot eis : equipment.keySet()) {
					if(eis == EnumItemSlot.HEAD) getKlon().getEquipment().setHelmet(NPCMethods.getBukkitItemStack(equipment.get(eis)));
					if(eis == EnumItemSlot.CHEST) getKlon().getEquipment().setChestplate(NPCMethods.getBukkitItemStack(equipment.get(eis)));
					if(eis == EnumItemSlot.LEGS) getKlon().getEquipment().setLeggings(NPCMethods.getBukkitItemStack(equipment.get(eis)));
					if(eis == EnumItemSlot.FEET) getKlon().getEquipment().setBoots(NPCMethods.getBukkitItemStack(equipment.get(eis)));
					
					if(eis == EnumItemSlot.MAINHAND) getKlon().getEquipment().setItemInMainHand(NPCMethods.getBukkitItemStack(equipment.get(eis)));
					if(eis == EnumItemSlot.OFFHAND) getKlon().getEquipment().setItemInOffHand(NPCMethods.getBukkitItemStack(equipment.get(eis)));
				}
				getKlon().getEquipment().setHelmet(is);
				getKlon().getEquipment().setHelmetDropChance(0);
			}
			if(hasKlon()) {
				calcPathfinder();
				for(Player p : getPlayers()) {
					NPCMethods.destroyKlon(p, getKlon());
				}
			}
			
			this.spawned = true;

			isOnTablist = tablist;
		}
	}
	
	
	public void move(Location from, Location to, boolean onground, boolean rotatehead) {
		this.loc = to.clone();
		this.ep.setLocation(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
		long deltaX = (((long) (loc.getX() * 32)) - ((long) (from.getX() * 32))) * 128;
		long deltaY = (((long) (loc.getY() * 32)) - ((long) (from.getY() * 32))) * 128;
		long deltaZ = (((long) (loc.getZ() * 32)) - ((long) (from.getZ() * 32))) * 128;
		//System.out.println("Delta: (X,Y,Z) = (" + deltaX + "," + deltaY + "," + deltaZ + ")");
		byte yaw = (byte) ((loc.getYaw() * 256.0F) / 360.0F);
		byte pitch = 0;//(byte) ((loc.getPitch() * 256.0F) / 360.0F);
		PacketPlayOutRelEntityMoveLook pporeml = new PacketPlayOutRelEntityMoveLook(ep.getId(), (short)deltaX, (short)deltaY, (short)deltaZ, yaw, pitch, onground);
		if(rotatehead) {
			this.setHeadRotation(yaw);
		}
		sendPacket(pporeml);
		
	}
	
	public void setSkin(String texture, String signature) {
		this.texture = texture;
		this.signature = signature;
		if(spawned()) {
			ep.getProfile().getProperties().get("textures").clear();
			ep.getProfile().getProperties().put("textures", new Property("textures", texture, signature));
			despawn(true);
			spawn(isOnTablist());
		}
		
	}
	
	public void setLocation(Location loc) {
		ep.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		PacketPlayOutEntityTeleport ppoet = new PacketPlayOutEntityTeleport(ep);
		sendPacket(ppoet);
		
	}
	public void setHeadRotation(Location loc) {
		setHeadRotation(loc.getYaw());
	}
	public void setHeadRotation(float yaw) {
		setHeadRotation((byte) ((yaw * 256.0F) / 360.0F));
	}
	public void setHeadRotation(byte yaw) {
		PacketPlayOutEntityHeadRotation ppoehr = new PacketPlayOutEntityHeadRotation(ep, yaw);
		sendPacket(ppoehr);
	}
	
	public void despawn(boolean keepklon) {
		if(spawned()) {
			PacketPlayOutEntityDestroy ppoed = new PacketPlayOutEntityDestroy(this.ep.getId());
			sendPacket(ppoed);
			this.spawned = false;
			if(isOnTablist()) {
				removeTablist(true);
			}
			if(hasKlon() && !keepklon) {
				getKlon().remove();
				setKlon(null);
			}
		}
	}

	public void setTablistName(String tablistname) {
		this.tablistname = tablistname;
		if(spawned()) {
			ep.listName = ChatSerializer.a("{\"text\":\""+ tablistname + "\"}");
			updateTablist();
		}
	}
	
	
	public boolean isOnTablist() {
		return this.isOnTablist;
	}
	public void updateTablist() {
		if(isOnTablist()) {

			PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ep);
			sendPacket(ppopi);
		}
	}
	public void addTablist() {
		if(!isOnTablist()) {
			PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
			sendPacket(ppopi);
		}
	}
	
	public void removeTablist(boolean force) {
		if(isOnTablist() || force) {
			PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
			sendPacket(ppopi);	
		}
	}
	
	public EntityPose getEntityPose() {
		return this.entitypose;
	}
	public void setPose(EntityPose pose) {
		this.entitypose = pose;
		DataWatcher dw = ep.getDataWatcher();
		dw.set(new DataWatcherObject<>(6, DataWatcherRegistry.s), pose);
		PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(ep.getId(), ep.getDataWatcher(), true);
		sendPacket(ppoem);
	}
	public void sendDataWatcher(int id) {
		DataWatcher dw = ep.getDataWatcher();
		dw.set(new DataWatcherObject<>(7, DataWatcherRegistry.a), (byte) id);
		PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(ep.getId(), dw, false);
		sendPacket(ppoem);
	}
	
	public void setEquipment(EnumItemSlot enumitemslot,org.bukkit.inventory.ItemStack itemstack) {
		if(itemstack == null) return;
		
		net.minecraft.server.v1_16_R3.ItemStack nmsitemstack = CraftItemStack.asNMSCopy(itemstack);
		//org.bukkit.inventory.ItemStack iasdf = CraftItemStack.asBukkitCopy(nmsitemstack);
		equipment.put(enumitemslot,nmsitemstack);
		
		if(spawned()) {
			this.ep.setSlot(enumitemslot, nmsitemstack);
			List<Pair<EnumItemSlot,net.minecraft.server.v1_16_R3.ItemStack>> itlist = new ArrayList<>();
			itlist.add(Pair.of(enumitemslot, nmsitemstack));
			
			PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(this.ep.getId(), itlist);
			//PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(this.ep.getId(), enumitemslot, nmsitemstack);
			sendPacket(ppoee);
			if(hasKlon()) {
				if(enumitemslot == EnumItemSlot.HEAD) getKlon().getEquipment().setHelmet(itemstack);
				if(enumitemslot == EnumItemSlot.CHEST) getKlon().getEquipment().setChestplate(itemstack);
				if(enumitemslot == EnumItemSlot.LEGS) getKlon().getEquipment().setLeggings(itemstack);
				if(enumitemslot == EnumItemSlot.FEET) getKlon().getEquipment().setBoots(itemstack);
				
				if(enumitemslot == EnumItemSlot.MAINHAND) getKlon().getEquipment().setItemInMainHand(itemstack);
				if(enumitemslot == EnumItemSlot.OFFHAND) getKlon().getEquipment().setItemInOffHand(itemstack);
				
			}
		}
	}
	
	public void sendPacket(Packet<?> packet) {
		for(Player p : this.getPlayers()) {
			NPCMethods.sendPacket(p, packet);
		}
	}
	public EntityPlayer getEntityPlayer() {
		return ep;
	}
	public void safeToFile() {
		File dir = new File("plugins/npcdata");
		try {
			FileUtils.mkdir(dir, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File("plugins/npcdata",ep.getUniqueID().toString()+".dat");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("UUID", ep.getUniqueID().toString());
		cfg.set("Location", loc);
		cfg.set("Name", name);
		cfg.set("Tablistname", tablistname);
		cfg.set("Isontablist", isOnTablist);
		cfg.set("Klontype",klontype.toString());
		for(EnumItemSlot eis : EnumItemSlot.values()) {
			net.minecraft.server.v1_16_R3.ItemStack is = null;
			if(equipment.containsKey(eis)) {
				is = equipment.get(eis);
				cfg.set("equipment."+eis.toString(), NPCMethods.toBase64(NPCMethods.getBukkitItemStack(is)));
			}else {
				cfg.set("equipment."+eis.toString(), null);
			}
		}
		//cfg.set("Equipment", equipment);
		cfg.set("Texture", texture);
		cfg.set("Signature", signature);
		cfg.set("Actiondata", actiondata);
		cfg.set("Gravity", gravity);
		cfg.set("maxhealth", maxhealth);
		cfg.set("health", health);
		cfg.set("sprinting", sprint);
		
		
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public HashMap<EnumItemSlot,net.minecraft.server.v1_16_R3.ItemStack> getEquipment() {
		return this.equipment;
	}

	public void respawn(int ticksuntilrespawn) {
		if(!isRespawning()) {
			boolean sprintcopy = this.sprint;
			setSprinting(false);
			respawnticks = ticksuntilrespawn;
			setOnFire(false);
			Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
	
				@Override
				public void run() {
					respawnticks = -1;
					setPose(EntityPose.STANDING);
					despawn(true);
					spawn(isOnTablist());
					setHealth(getMaxHealth());
					setSprinting(sprintcopy);
				}
				
			}, ticksuntilrespawn);
		}
	}

	public boolean isRespawning() {
		return respawnticks != -1;
	}

}
















