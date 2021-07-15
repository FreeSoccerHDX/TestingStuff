package de.freesoccerhdx.testingstuff.customentity.utils;

import java.lang.reflect.Field;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;

import de.freesoccerhdx.testingstuff.main.TestingStuff;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.util.TimeRange;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.IEntityAngerable;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMoveTowardsTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalUniversalAngerReset;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.monster.IMonster;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.IntRange;


public class CustomEntity extends EntityVillager implements IEntityAngerable{

	/*
	public static final EntityTypes CUSTOMENTITY; 
	
	static {
		
		Builder entitytypes_a = Builder.a(CustomEntity::new, EnumCreatureType.CREATURE).a();

		CUSTOMENTITY = a("customentity",entitytypes_a);
		
	}
	private static <T extends Entity> EntityTypes a(String s, Builder<?> entitytypes_builder) {
		return (EntityTypes)IRegistry.<EntityTypes<?>>a(IRegistry.ENTITY_TYPE, s, entitytypes_builder.a(s));
	}
	*/
	
	public static void sendNewNpcPacket(Player p, EntityLiving el) {
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer(); //NMS Minecraft Server
        WorldServer nmsWorld = ((CraftWorld) p.getWorld()).getHandle(); //NMS World Server
        UUID gpuuid = el.getUniqueID();
        GameProfile gp = new GameProfile(gpuuid, "MyName"); //NMS Game Profile
		
        if(el instanceof EntityVillager) {
        	NPCSkins skin = NPCSkins.JEFFERSON;
        	
        	// https://mineskin.org/328491958
        	String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxNDcyMjI4Mzg2OSwKICAicHJvZmlsZUlkIiA6ICI0ZWQ4MjMzNzFhMmU0YmI3YTVlYWJmY2ZmZGE4NDk1NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaXJlYnlyZDg4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ2MmIxNzk3ZDc2N2M2MmRhMDQzZDAxNjIyNjcyODM5ZjE4YWYwNTUzM2M0YjNlMTU2ZjIzMTQ4YmFmMjQzODgiCiAgICB9CiAgfQp9";//skin.texture;
        	String signature = "coPz3nyuTT/5h+yVQjwCha9oQiE/Yj2ngAEVt4CIS4rsB9jEFgiVV7x83kW4twW3kpZ0y37/eha99DRiHWUYj+4pnM/1dhxbLI20rX3gh5J6gSZ7b/CNqoY7Qx3xWMkUgOF5Ezlx+hU8t4OFTMxudcByQFkF3uHPD6cTwGYeYZ+uq3mT1/W6Jayivh/EhvVJgcoa6Zxfa7vRYnMYnZ6z5OVPPTLXVkQwAbEKIZapS0P5V/pb6uIh057DbEk8y5w5naRp7FjhdCW06tDHmz3/vNLD7LPrRMjqwKnm0L+3asg3oLJytL1DZrI9497ec41A6F5i5cOWbsRmJ9YBsBXf11bf6wyDKoXLgzKMD6KrIqFlU0uZGP3tOm3nZ2mzVgaveAPDQvzUVArLPYgUYvET2odqsdHfY1/ZXonN61GVlP2hvfEqS3PvuvA+FzY8qVWFihmgQ5dSgQ61JCZB0dcMOwRN54sZ6Pgwqf/6qK4Ds2A7hcpSj+Zdted2PTBL5pFu453DJigGYkkDpbld+ZqWPbkaS/Mr3xwwE1C1bkX36HdsedJN66tcahapWrzqf7ojwog10Lnq/LZb79pyFM//kxm9NfUzV+vzw7LedwOc8K+4BtvlPW/pOTcYptjBukgtJuxCKJBM00sKrQLMXZGmryo1nS8tkMb/GcDj2QGUQ6E=";//skin.signature;
        	
        	gp.getProperties().get("textures").clear();
        	gp.getProperties().put("textures", new Property("textures", texture, signature));
        //	Bukkit.broadcastMessage("update textures");
        }
        //EntityPlayer npcep = new EntityPlayer(nmsServer, nmsWorld, gp, new PlayerInteractManager(nmsWorld));
        EntityPlayer npcep = new EntityPlayer(nmsServer, nmsWorld, gp);
        npcep.setLocation(el.locX(), el.locY(), el.locZ(), el.getYRot(), el.getXRot());
        npcep.e(el.getId()); // set new entity id to npc
        
        //PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npcep);
        PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.a, npcep);
        PacketPlayOutNamedEntitySpawn pposel = new PacketPlayOutNamedEntitySpawn(npcep);
        
        EntityPlayer ep = ((CraftPlayer)p).getHandle();
        
        ep.b.sendPacket(ppopi);
        ep.b.sendPacket(pposel);
        
        //PacketPlayOutPlayerInfo ppopi2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npcep);
        PacketPlayOutPlayerInfo ppopi2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.e, npcep);
        
        Bukkit.getScheduler().runTaskLater(TestingStuff.main, ()->ep.b.sendPacket(ppopi2), 5);
        
        
        // update Armor
        List<Pair<EnumItemSlot, ItemStack>>  equip = new ArrayList<>();
        
        for(EnumItemSlot eis : EnumItemSlot.values()) {
        	if(el.getEquipment(eis) != null) {
        		equip.add(Pair.of(eis, el.getEquipment(eis)));
        	}
        }

		PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(el.getId(), equip);
		ep.b.sendPacket(ppoee);
        
        
	}
	
	private void registerGenericAttribute(Attribute attribute) {
		try {
			Field attributeField = AttributeMapBase.class.getDeclaredField("b");
	        attributeField.setAccessible(true);
			
	        AttributeMapBase attributeMapBase = this.getAttributeMap();
	        
	        Map<AttributeBase, AttributeModifiable> map = (Map<AttributeBase, AttributeModifiable>) attributeField.get(attributeMapBase);
	        
	        AttributeBase attributeBase = CraftAttributeMap.toMinecraft(attribute);
	        AttributeModifiable attributeModifiable = new AttributeModifiable(attributeBase, AttributeModifiable::getAttribute);
	        
	        map.put(attributeBase, attributeModifiable);
        
		}catch(Exception ex) {
			ex.printStackTrace();
		}
    }
	
	
	private static final UniformInt bT = TimeRange.a(20, 39);
	private int bp;
	private UUID bq;
	
	private boolean iscustomnpc = true;

	public CustomEntity(Location spawnloc) {
		//super(EntityTypes.VILLAGER, ((CraftWorld)spawnloc.getWorld()).getHandle());
		super(EntityTypes.aV, ((CraftWorld)spawnloc.getWorld()).getHandle());
		
		this.setLocation(spawnloc.getX(), spawnloc.getY(), spawnloc.getZ(),spawnloc.getYaw(), spawnloc.getPitch());
		// head
		this.setSlot(EnumItemSlot.f, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_HELMET)), true);
		// chest
		this.setSlot(EnumItemSlot.e, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_CHESTPLATE)), true);
		// legs
		this.setSlot(EnumItemSlot.d, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_LEGGINGS)), true);
		// mainhand
		this.setSlot(EnumItemSlot.a, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_AXE)), true);
		
	//	updateEquipment();
		
		this.persist = true;
		
	//	this.lootTableKey = null;
		
		NBTTagCompound ntc = new NBTTagCompound();		
		save(ntc);
		ntc.setBoolean("customnpc", true);
		load(ntc);

		
		WorldServer ws = ((CraftWorld)spawnloc.getWorld()).getHandle(); 
		ws.addEntity(this);
		
		registerGenericAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
		registerGenericAttribute(Attribute.GENERIC_ARMOR);
		
		//getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3);
		//getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(50);
		//getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(50);
		//getAttributeInstance(GenericAttributes.ARMOR).setValue(500.0d);
		
		getAttributeInstance(GenericAttributes.d).setValue(0.3);
		getAttributeInstance(GenericAttributes.a).setValue(50);
		getAttributeInstance(GenericAttributes.f).setValue(50);
		getAttributeInstance(GenericAttributes.i).setValue(500.0d);
		
		setVillagerData(getVillagerData().withProfession(CraftVillager.bukkitToNmsProfession(Profession.NITWIT)));
		
	}
	
	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		
		try {
			nbttagcompound.setBoolean("customnpc", iscustomnpc);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		super.save(nbttagcompound);
		
		return nbttagcompound;
	}
	
	@Override
	public void load(NBTTagCompound nbttagcompound) {
		
		try {
			if(nbttagcompound.hasKey("customnpc")) {
				iscustomnpc = nbttagcompound.getBoolean("customnpc");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		super.load(nbttagcompound);
	}
	
	
	/*
	@Override
	public void startDrownedConversion(int i) {
		// we dont want a drowned ;)
	}
	
	@Override
	public boolean isDrownConverting() {
		return false; // it will never convert to drowned ;)
	}
	
	@Override
	protected boolean eN() {
		return false; // dont try to convert to drowned
	}
	
	@Override
	protected boolean T_() {
		return false; // blocks sun dmg
	}
	*/
	
	
	@Override
	public void tick() {
		super.tick();
	}

	
	@Override
	public SoundEffect getSoundAmbient() {
		return null;
	}
	
	@Override
	public SoundEffect getSoundHurt(DamageSource damagesource) {
		return (damagesource == DamageSource.c)
				? SoundEffects.ow
				: ((damagesource == DamageSource.h)
						? SoundEffects.ou
						: ((damagesource == DamageSource.u)
								? SoundEffects.ox
								: ((damagesource == DamageSource.v) ? SoundEffects.ov : SoundEffects.ot)));
	}
	
	@Override
	public SoundEffect getSoundDeath() {
		return SoundEffects.os;
	}
	
	@Override
	public boolean isTypeNotPersistent(double var0) {
		return false;
	}


	private boolean checkEntitySelector(EntityLiving entityliving) {
		    return (entityliving instanceof net.minecraft.world.entity.monster.IMonster
					&& !(entityliving instanceof net.minecraft.world.entity.monster.EntityCreeper));
	}
	
	private boolean checkEntitySelector2(EntityLiving entityliving) {
		return entityliving instanceof IMonster;
	}
	
	@Override
	protected void initPathfinder() {
		
		
		
		
		this.bQ.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
		this.bQ.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
		this.bQ.a(7, new PathfinderGoalLookAtPlayer(this, (Class)EntityHuman.class, 6.0F));
		this.bQ.a(8, new PathfinderGoalRandomLookaround(this));
		
		
		this.bP.a(2, new PathfinderGoalHurtByTarget(this, new Class[0]));
		this.bP.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 10, true, false, this::checkEntitySelector));
		this.bP.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 5, false, false, entityliving->checkEntitySelector2(entityliving)));
		this.bP.a(4, new PathfinderGoalUniversalAngerReset<>(this, false));
		    
	 }

	
	// Golem Target Entity(Player) that hitted the player
	public void anger() {
		setAnger(bT.a(this.Q));
	}
	  
	public void setAnger(int i) {
		this.bp = i;
	}
	  
	public int getAnger() {
		return this.bp;
	}
	  
	public void setAngerTarget(UUID uuid) {
		this.bq = uuid;
	}
	  
	public UUID getAngerTarget() {
		return this.bq;
	}
	
	
	
	
}
