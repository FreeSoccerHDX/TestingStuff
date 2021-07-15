package freesoccerhdx.survivalplus.events.player;

import java.io.File;





import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.core.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import freesoccerhdx.survivalplus.enchants.EnchantmentHandler;
import freesoccerhdx.survivalplus.haupt.DungeonChunkGenerator;
import freesoccerhdx.survivalplus.haupt.JSONMessage;
import freesoccerhdx.survivalplus.haupt.JSONMessage.ClickType;
import freesoccerhdx.survivalplus.haupt.JSONMessage.HoverType;
import freesoccerhdx.survivalplus.haupt.signgui.SignGUI;
import freesoccerhdx.survivalplus.haupt.signgui.SignGUIListener;
import freesoccerhdx.survivalplus.npc.NPCPlayer;
import freesoccerhdx.survivalplus.npc.NPCAction;
import freesoccerhdx.survivalplus.npc.NPCAnimation;
import freesoccerhdx.survivalplus.npc.NPCHandler;
import freesoccerhdx.survivalplus.npc.NPCMethods;
import freesoccerhdx.survivalplus.npc.NPCPlayer;
import freesoccerhdx.survivalplus.haupt.Lag;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.TicTacToe;
import freesoccerhdx.survivalplus.haupt.TimeData;
import freesoccerhdx.survivalplus.haupt.VierGewinnt;
import freesoccerhdx.survivalplus.haupt.WayPointManager;
import freesoccerhdx.survivalplus.haupt.main;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcher.Item;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityPose;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IInventory;
import net.minecraft.server.v1_16_R3.IRecipe;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.MobEffects;
import net.minecraft.server.v1_16_R3.PacketPlayInAbilities;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_16_R3.PacketPlayOutAbilities;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutRecipeUpdate;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.PacketPlayOutUpdateHealth;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.PathfinderGoalWrapped;
import net.minecraft.server.v1_16_R3.PlayerAbilities;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.ScoreboardServer.Action;
import net.minecraft.server.v1_16_R3.TileEntitySign;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.WorldServer;

import assets.minecraft.lang.*;



public class PlayerCommandPreprocessEventHandler implements Listener {
	
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
		
		
		Player p = e.getPlayer();
		String[] args = e.getMessage().split(" ");
		
		
		if(args[0].equalsIgnoreCase("/testmat")) {
			
			p.sendMessage(main.localquery.queryMaterial(p.getInventory().getItemInMainHand().getType()));
			e.setCancelled(true);
		}
		
		if(args[0].equalsIgnoreCase("/bypass")) {
			e.setCancelled(true);
			main.bypass = !main.bypass;
			p.sendMessage("bypass: " + main.bypass);
		}
		
		if(args[0].equalsIgnoreCase("/particletest")) {
		
			Location partloc = p.getLocation().add(0, 0, 1);
			e.setCancelled(true);
			Bukkit.getScheduler().runTaskTimer(main.m, ()->p.getWorld().spawnParticle(Particle.FLAME, partloc, 0), 10, 1);
			p.sendMessage("yes");
		}  
		
		if(args[0].equalsIgnoreCase("/npctest")) {
			Location spawnloc = p.getEyeLocation().add(p.getLocation().getDirection().multiply(3));
			NPCPlayer npc = new NPCPlayer("Jefferson","§aa §bb §cc §dd §ee §ff §00 §11 §22 §33 §44 §55 §66 §77 §88 §99 §ll §r§oo §r§mm §r§nn", spawnloc);
			NPCHandler.register(npc);
			String texture = "ewogICJ0aW1lc3RhbXAiIDogMTU5MDc2MjI4NTAwMCwKICAicHJvZmlsZUlkIiA6ICI2NTc3OGE5YWUzYTE0MTI5ODVlN2RjNTdhMzc3NTE1YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXJ0b3BoIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U5NWQ1NTk0ZWRjY2U4YWZhZGU2YmEyNGVlNTBkNmM3NGU4M2I5NjAwNTE0NjBlZGEyNWZlZTQ3NzRkNmNmYzMiCiAgICB9CiAgfQp9";
			String signature = "x4txELQZit88KubBG6BYhvAud0ewfGfKC1rZe5V42jKcbMXEfCO2QDA35xEPQxUbTLppPM4oOX9PsGAr2kYl+ZZLyFKDx3ibCE0Y3Ft/NnRtOOi9b5INao+KSEPYBbTnyCvyG3eF2evFRYZQ1X4OXCm4rHF7OKnTHMjNsryOAhbXZFh1tQnMMZfzegCVlOPqroiHdD7tx60SVqOrCD2U1qm4ZFLX5V0FXjrkMqDPw6FvLf4cnJiYZ0M/qbyD0EFr4uf06bOavYoB64c3JmR6mdGUSQzpCezFE+l6Mm0JAtlflAjpU5iDxrFihMvaRkVEtrtVKDjye53I/HCwLx87vg1mTr73IFGfKeu5KnqBmZpV/m4+m1FCrqLPeCiTo95U+CGLfwLJY7OyrDNjRVRbuHvh23dYUIL9Osr3STdrbkJUnhp1DozFXkBKHWrT702oB+/wvopEdeSq/GvNXeFBnmML/K8E9coahFxWd/eTV0Sc+V/8igWVoFILb9yeafKoKBIV9aUgcOWO/ghS6fBBstrANUdY9R75ombhaKbr8kj01nuWDj20WhB1eZr6XlFfzOSeb7Op4QcetrBZdhy2KA5iIQT2mO+bx3k4v9pSNsorHmIozSGQTbEe4FOY64L9zBQlswRyvCHR06nLZSRXU+bMBkX6abhF9ezMkYABWm8=";
//			EntityDeathEvent

			npc.addPlayer(p);
			npc.setKlonType(EntityType.SKELETON);
			npc.setSkin(texture, signature);
			npc.spawn(false);			
			npc.setEquipment(EnumItemSlot.HEAD, new ItemStack(Material.DIAMOND_HELMET));
			npc.setEquipment(EnumItemSlot.CHEST, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			npc.setEquipment(EnumItemSlot.LEGS, new ItemStack(Material.DIAMOND_LEGGINGS));
			npc.setEquipment(EnumItemSlot.FEET, new ItemStack(Material.DIAMOND_BOOTS));
			npc.setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Material.DIAMOND_SWORD));
			npc.setHealth(20.0);
			npc.startTickTimer();
			
			npc.setSprinting(true);
			
		//	npc.setActionData(new NPCAction(true, true, true, false, true, false));
						
			npc.setVelocity(new Vector(0,1.1,1.2));
			
			
			//Bukkit.getScheduler().runTaskTimer(main.m, ()->npc.getEntityPlayer().getBukkitEntity().launchProjectile(Arrow.class), 10, 5);
			
			//npc.getEntityPlayer().
			
			
			//Bukkit.getScheduler().runTaskTimer(main.m, ()->Bukkit.broadcastMessage("loc: " + npc.getEntityPlayer().getBukkitEntity().getLocation()), 20,1);
		//	Bukkit.getScheduler().runTaskTimer(main.m, ()->npc.applyTick(), 0,1);
			
			
			
			
			//Bukkit.getScheduler().runTaskLater(main.m,()->p.setVelocity(new Vector(0,2,0)), 1*20);
			
			//npc.setSkin(texture, signature);
			
			/*
			Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {
				
				@Override
				public void run() {
					npc.move(spawnloc, spawnloc.clone().add(p.getEyeLocation().getDirection().multiply(0.28)), false);
					spawnloc.add(p.getEyeLocation().getDirection().multiply(0.28));
				}
			}, 20, 1);
			
			*/
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.setAnimation(NPCAnimation.TAKE_DAMAGE), 1*20);
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.setAnimation(NPCAnimation.CRITICAL_EFFECT), 2*20);
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.setAnimation(NPCAnimation.MAGIC_CRITICAL_EFFECT), 3*20);
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.setAnimation(NPCAnimation.SWING_MAIN_HAND), 4*20);
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.setAnimation(NPCAnimation.SWING_OFFHAND), 5*20);
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.setAnimation(NPCAnimation.LEAVE_BED), 6*20);

			
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.removePlayer(p), 3*20);
			//Bukkit.getScheduler().runTaskLater(main.m,()->npc.addPlayer(p), 8*20);
			
			/*for(int i = 0; i < 360; i++) {
				int wert = i;
				Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
					
					@Override
					public void run() {
						Bukkit.broadcastMessage(""+wert);
						spawnloc.setYaw(wert-45);
						npc.setLocation(spawnloc.clone());
						//npc.setHeadRotation((float)i);
					}
				}, i*2);
				
				
			}*/
			
		}
		
		if(args[0].equalsIgnoreCase("/swim")) {
			CraftPlayer player = (CraftPlayer) p;
			DataWatcher dw = player.getHandle().getDataWatcher(); //6 -> entitypose
			EntityPlayer eh = player.getHandle();
			
			
			dw.set(new DataWatcherObject<>(6, DataWatcherRegistry.s), EntityPose.CROUCHING);
			
			
			PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(eh.getId(), eh.getDataWatcher(), true);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoem);
			p.sendMessage("end");
			e.setCancelled(true);
		}
		
		
		if(args[0].equalsIgnoreCase("/levitate")) {
			PacketPlayOutEntityVelocity ppoev = new PacketPlayOutEntityVelocity(p.getEntityId(), new Vec3D(0, 2, 0));
			CraftPlayer cp = (CraftPlayer) p;
			cp.getHandle().playerConnection.sendPacket(ppoev);
		}
		
		if(args[0].equalsIgnoreCase("/roflss")) {
			SignGUI.openGUI(p, new String[] {"","","^^^^^^^^^^^^^^^^","§lInput Name"}, new SignGUIListener() {
				@Override
				protected void listen(Player p, BlockPosition bp, String[] lines) {
					p.sendMessage("finished");
					
				}
			});
			
			
		}
 
		if(args[0].equalsIgnoreCase("/getjson")) {
			File jsonfile = new File("playerjson.dat");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(jsonfile);
			
			cfg.set("abc", new NBTEntity(p).asNBTString());
			try {
				cfg.save(jsonfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if(args[0].equalsIgnoreCase("/humanpacket")) {
			MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer(); //NMS Server
	        WorldServer nmsWorld = ((CraftWorld) p.getWorld()).getHandle(); //NMS World Server
	        GameProfile gp = new GameProfile(UUID.randomUUID(), "§6Jeff"); //NMS Game Profile
	       
	        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTU5MDc2MjI4NTAwMCwKICAicHJvZmlsZUlkIiA6ICI2NTc3OGE5YWUzYTE0MTI5ODVlN2RjNTdhMzc3NTE1YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXJ0b3BoIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U5NWQ1NTk0ZWRjY2U4YWZhZGU2YmEyNGVlNTBkNmM3NGU4M2I5NjAwNTE0NjBlZGEyNWZlZTQ3NzRkNmNmYzMiCiAgICB9CiAgfQp9";
	        String signature = "x4txELQZit88KubBG6BYhvAud0ewfGfKC1rZe5V42jKcbMXEfCO2QDA35xEPQxUbTLppPM4oOX9PsGAr2kYl+ZZLyFKDx3ibCE0Y3Ft/NnRtOOi9b5INao+KSEPYBbTnyCvyG3eF2evFRYZQ1X4OXCm4rHF7OKnTHMjNsryOAhbXZFh1tQnMMZfzegCVlOPqroiHdD7tx60SVqOrCD2U1qm4ZFLX5V0FXjrkMqDPw6FvLf4cnJiYZ0M/qbyD0EFr4uf06bOavYoB64c3JmR6mdGUSQzpCezFE+l6Mm0JAtlflAjpU5iDxrFihMvaRkVEtrtVKDjye53I/HCwLx87vg1mTr73IFGfKeu5KnqBmZpV/m4+m1FCrqLPeCiTo95U+CGLfwLJY7OyrDNjRVRbuHvh23dYUIL9Osr3STdrbkJUnhp1DozFXkBKHWrT702oB+/wvopEdeSq/GvNXeFBnmML/K8E9coahFxWd/eTV0Sc+V/8igWVoFILb9yeafKoKBIV9aUgcOWO/ghS6fBBstrANUdY9R75ombhaKbr8kj01nuWDj20WhB1eZr6XlFfzOSeb7Op4QcetrBZdhy2KA5iIQT2mO+bx3k4v9pSNsorHmIozSGQTbEe4FOY64L9zBQlswRyvCHR06nLZSRXU+bMBkX6abhF9ezMkYABWm8=";
	        
	        gp.getProperties().get("textures").clear();
	        gp.getProperties().put("textures", new Property("textures", texture, signature));
	        
			EntityPlayer eh = new EntityPlayer(nmsServer, nmsWorld, gp, new PlayerInteractManager(nmsWorld));
			eh.setHealth(20);
			
		//	DataWatcherObject<EntityPose> POSE = DataWatcher.a(net.minecraft.server.v1_16_R3.Entity.class, DataWatcherRegistry.s);
		//	DataWatcher datawatcher = eh.getDataWatcher();
	//	datawatcher.set(POSE, EntityPose.SLEEPING);
			
			//datawatcher.register(POSE, EntityPose.STANDING);
			
			//dataWatcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
			
			//DataWatcherObject<EntityPose> POSE = DataWatcher.a(Entity.class, DataWatcherRegistry.s);
			//dataWatcher.set(POSE, EntityPose.SLEEPING);
			
			
			
			
			eh.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
			//eh.sleep(new BlockPosition(p.getLocation().getX(), p.getLocation().getY()-1, p.getLocation().getZ()),true);
			
			//eh.entitySleep(new BlockPosition(p.getLocation().getX(), p.getLocation().getY()-1, p.getLocation().getZ()));
			
			PacketPlayOutNamedEntitySpawn pposel = new PacketPlayOutNamedEntitySpawn(eh);
			PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(eh.getId(), eh.getDataWatcher(), true);
			
			PacketPlayOutUpdateHealth ppouh = new PacketPlayOutUpdateHealth(40, eh.getId(), 10);
			
			PacketPlayOutScoreboardScore pposc = new PacketPlayOutScoreboardScore(Action.CHANGE, "leben", "§6Jeff", 20);
			
			//PacketPlayOutEntity ppoe = new PacketPlayOutEntity(ep.getId());
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, eh));
					
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(pposel);
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoem);
					
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppouh);
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(pposc);
					//Bukkit.getScheduler().runTaskLater(main.m, ()->((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoem), 10);
					
					PacketPlayOutEntityStatus ppoes = new PacketPlayOutEntityStatus(eh, (byte)1);
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoes);
					
			Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {

				@Override
				public void run() {
				//	eh.sleep(new BlockPosition(p.getLocation().getX(), p.getLocation().getY()-1, p.getLocation().getZ()));
					//	((net.minecraft.server.v1_16_R3.EntityLiving) eh).entitySleep(new BlockPosition(p.getLocation().getX(), p.getLocation().getY()-1, p.getLocation().getZ()));
					DataWatcher dw = eh.getDataWatcher();
					p.sendMessage("§asdfg");
					for(Item<?> item : dw.c()) {
						if(item.b() instanceof EntityPose) {
							
							DataWatcherObject<EntityPose> pose = (DataWatcherObject<EntityPose>) item.a();
							dw.set(pose, EntityPose.SWIMMING); 
							
						}
						
						p.sendMessage(item.a() +" -> " + item.b());
					}
					
					//DataWatcherObject<EntityPose> POSE = DataWatcher.a(net.minecraft.server.v1_16_R3.Entity.class, DataWatcherRegistry.s);
					//p.sendMessage(eh.getDataWatcher().get(POSE).name());
					//((net.minecraft.server.v1_16_R3.Entity) eh).getDataWatcher().set(POSE, EntityPose.SLEEPING);
					
					
					
					
					PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(eh.getId(), eh.getDataWatcher(), true);
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoem);
				}
				
			}, 60);
					
			Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {

				@Override
				public void run() {
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, eh));
				}
				
			}, 10);
			Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {

				@Override
				public void run() {
					eh.setMot(new Vec3D(0, 2, -2));
					eh.getBukkitEntity().setVelocity(new Vector(0, 2, -2));
					PacketPlayOutEntityVelocity ppoev = new PacketPlayOutEntityVelocity(eh.getId(),new Vec3D(0, 2, -2));
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoev);
				}
				
			}, 30);
			
					
			
				//	((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoe);
				//	((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoem);
				//	((CraftPlayer)p).getHandle().playerConnection.sendPacket(pposel);
				
					
					
					e.setCancelled(true);
					p.sendMessage("jup");
				
				
			//new EntityPlayer(minecraftserver, nmsWorld, gameprofile, playerinteractmanager)
		}
		
		if(args[0].equalsIgnoreCase("/getrepaircost")) {
			e.setCancelled(true);
			ItemStack hand = p.getInventory().getItemInMainHand();
			NBTItem nbti = new NBTItem(hand);
			int pay = 0;
			
			if(nbti.hasNBTData()) {
				if(nbti.hasKey("RepairCost")) {
					pay = nbti.getInteger("RepairCost");
				}
			}
			p.sendMessage("§aDie Reperationskosten betragen " + pay + " Xp-Level.");
		}
		if(args[0].equalsIgnoreCase("/repaircost")) {
			e.setCancelled(true);
			ItemStack hand = p.getInventory().getItemInMainHand();
			NBTItem nbti = new NBTItem(hand);
			int pay = 0;
			
			if(nbti.hasNBTData()) {
				if(nbti.hasKey("RepairCost")) {
					pay = nbti.getInteger("RepairCost");
				}
			}
			if(pay > 30) {
				if(p.getLevel() >= pay) {
					p.setLevel(p.getLevel()-pay);
					nbti.setInteger("RepairCost", 0);
					p.getInventory().setItemInMainHand(nbti.getItem());
					p.sendMessage("§aDie Reperationskosten wurden zurückgesetzt!");
				}else {
					p.sendMessage("§cDu benötigst " + pay + " Xp-Level");
				}
			}else {
				p.sendMessage("§cDie Reperationskosten sind noch zu billig!");
			}
		}
		/* TODO: Breaktime-command that never worked
		if(args[0].equalsIgnoreCase("/breaktime")) {
			ItemStack tool = p.getInventory().getItemInMainHand();
			ItemStack block = p.getInventory().getItemInOffHand();
			
			net.minecraft.server.v1_16_R3.Block nmsBlock = org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers.getBlock(block.getType());
		    net.minecraft.server.v1_16_R3.IBlockData data = nmsBlock.getBlockData();
			
			
			Double time = (double) org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers.getItem(tool.getType()).getDestroySpeed(CraftItemStack.asNMSCopy(tool), data);
			p.sendMessage("->"+time+ " ->"+nmsBlock.getDurability() + ","+nmsBlock.strength);
		}
		*/
		
		if(args[0].equalsIgnoreCase("/restartdungeon")) {
			if(Bukkit.getWorld("DungeonWorld") != null) {
				Bukkit.unloadWorld("DungeonWorld", false);
				p.sendMessage("unload world");
			}
			
			File f = new File("DungeonWorld");
			
			
			Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> {
				try {
					org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils.deleteDirectory(f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}, 20*5);
			Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> p.sendMessage("delete fertig"), 20*5);
			
			WorldCreator wc = new WorldCreator("DungeonWorld");
			wc.environment(Environment.NORMAL);
			wc.type(WorldType.AMPLIFIED);
			wc.generator(new DungeonChunkGenerator());
			
			Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> wc.createWorld(), 20*10);
			Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> p.sendMessage("create fertig"), 20*10);
			
			e.setCancelled(true);
		}
		
		
		if(args[0].equalsIgnoreCase("/pops")) {
			p.sendMessage(p.getWorld().getPopulators().size()+"");
			p.getWorld().getPopulators().clear();
			p.sendMessage(p.getWorld().getPopulators().size()+"");
		}
		
		if(args[0].equalsIgnoreCase("/glow")) {
			e.setCancelled(true);
			p.setGlowing(!p.isGlowing());
		}
		
		if(args[0].equals("/roflskelet")) {

			Skeleton ent = (Skeleton) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON);
			ent.getEquipment().setItemInMainHand(new ItemStack(Material.CROSSBOW));
			
		}
		
		if(args[0].equalsIgnoreCase("/newstuff")) {
			p.openInventory(main.newstuffInventar);
			e.setCancelled(true);
		}
		
		if(args[0].equalsIgnoreCase("/xpfluider")) {
			e.setCancelled(true);
			
			if(p.getInventory().getItemInMainHand() != null) {
				ItemStack is = p.getInventory().getItemInMainHand();
				if(is.getType() == Material.EXPERIENCE_BOTTLE) {
					int amount = is.getAmount();
					p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
					
					ExperienceOrb orb = p.getWorld().spawn(p.getLocation(), ExperienceOrb.class);
					orb.setExperience(8*amount);
					p.sendMessage("§aDu hast deine Xp-Flaschen in Xp-Kugeln transferiert.");
				}else {
					p.sendMessage("§cDu musst Xp-Flaschen in deiner Hand halten!");
				}
			}else {
				p.sendMessage("§cDu musst Xp-Flaschen in deiner Hand halten!");
				
			}
			
		}
		
		if(args[0].equalsIgnoreCase("/xptransfer")) {
			e.setCancelled(true);
			float exp = p.getTotalExperience();
			if (exp >= 8.0F) {
				int bottle = (int)(exp / 8.0D);
				p.sendMessage("§2§lDu hast deine gesammten XP in Flaschen gebannt.");
				p.setTotalExperience(0);
				p.setLevel(0);
				while (bottle >= 64) {
					bottle -= 64;
					p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
		        } 
		        if (bottle > 0) {
		        	p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE, bottle)); 
		      	}	
		   	}  else {
	      		p.sendMessage("§cDu hast nicht genug XP.");
	      	}
		}
		
		
		if(args[0].equalsIgnoreCase("/wp") || args[0].equalsIgnoreCase("/waypoint") || args[0].equalsIgnoreCase("/wegpunkt")) {
			
			if(args.length == 1) {
				if(WayPointManager.hasWayPoints(p)) {
					WayPointManager.despawnWayPoints(p);
					p.sendMessage("§cDeine Wegpunkte werden jetzt nicht mehr angezeigt!");
				}else {
					boolean success = WayPointManager.spawnWayPoints(p);
					if(success) {
						p.sendMessage("§aDeine Wegpunkte werden jetzt angezeigt!");
					}else {
						p.sendMessage("§cDu hast noch keine Wegpunkte erstellt!");
					}
				}
			}else if(args.length >= 3 && args[1].equalsIgnoreCase("add")) {
				List<String> wpn = WayPointManager.getWayPointNames(p);
				String name = "";
				for(int i = 2; i < args.length; i ++) {					
					name += args[i] + " ";
				}
				name = name.substring(0, name.length()-1);
				if(!wpn.contains(name)) {
					boolean sucess = WayPointManager.addWayPoint(p,name);
					if(sucess) {
						p.sendMessage("§aDer Wegpunkt wurde erfolgreich erstellt!");
					}else {
						p.sendMessage("§cDer Wegpunkt konnte nicht erstellt werden!");
					}
				}else {
					p.sendMessage("§cDu hast einen Wegpunkt mit diesen Namen bereits erstellt!");
				}
				
			}else if(args.length == 3 && args[1].equalsIgnoreCase("remove")){
				List<String> wpn = WayPointManager.getWayPointNames(p);
				int index = -1;
				try {
					index = Integer.parseInt(args[2]);
				}catch(Exception ex) {
					p.sendMessage("§cDu musst die Zahl des Wegpunktes eingeben!");
					return;
				}
				if(index >= 1 && index <= wpn.size()) {
					String name = wpn.get(index-1);
					boolean sucess = WayPointManager.removeWayPoint(p,name);
					if(sucess) {
						p.sendMessage("§aDer Wegpunkt wurde entfernt!");
					}else{
						p.sendMessage("§cDer Wegpunkt konnte nicht entfernt werden!");
					}
				}else {
					p.sendMessage("§cDer Wegpunkt mit dieser ID exestiert nicht!");
				}
				
				
			}else if(args.length == 2 && args[1].equalsIgnoreCase("list")) {
				List<String> wpn = WayPointManager.getWayPointNames(p);
				p.sendMessage("§3Das sind deine Wegpunkte:");
				for(int i = 0; i < wpn.size(); i++) {
					p.sendMessage("§a#"+(i+1)+" §7- §2"+wpn.get(i));
				}
				
			}else {
				p.sendMessage("§3Wegpunkte:");
				p.sendMessage("§a/wp §7- toggle hide/show Wegpunkte");
				p.sendMessage("§a/wp add <name> §7- erstellt neuen Wegpunkt");
				p.sendMessage("§a/wp remove <id> §7- entfernt einen Wegpunkt");
				p.sendMessage("§a/wp list §7- zeigt alle Wegpunkte");
			}
			
			e.setCancelled(true);
		}
		
		if(args[0].equalsIgnoreCase("/maptest")) {
			MapView view = Bukkit.createMap(p.getWorld());
			for (MapRenderer ren : view.getRenderers()) {
				p.sendMessage(ren.toString());
			}
			view.getRenderers().clear();
			
			//view.addRenderer();
			
			
			ItemStack is = new ItemStack(Material.FILLED_MAP);
			MapMeta meta = (MapMeta) is.getItemMeta();
			meta.setMapView(view);
			meta.setScaling(true);
			is.setDurability((short) view.getId());
			is.setItemMeta(meta);
			
			p.getInventory().addItem(is);
		}
		
		if(args[0].equalsIgnoreCase("/extrembounce")) {
			int am = 50;
			
			if(args.length == 2) {
				am = Integer.parseInt(args[1]);
			}
			
			for(int i = 0; i < am; i++) {
				Projectile proj = p.launchProjectile(Snowball.class);
				HashMap<String,Object> data = new HashMap<>();
				data.put("Abprallen", 5);
				data.put("Explosion", 3);
				data.put("Geschwindigkeit", 0.7);
				main.arrowdata.put(proj,data);
			}
			p.sendMessage("§6They will die with " + am + " §6§lBombs §5§lin §4§lda §2§lSnow");
			
		}
		
		
		if(args[0].equalsIgnoreCase("/bounce")) {
			/*
			 * Arrow ar = (Arrow) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARROW);
			ar.setVelocity(p.getEyeLocation().getDirection().multiply(2));
			ar.setCustomName("§6bouncer");
			ar.setCustomNameVisible(true);
			ar.setBounce(true);
			ar.setMetadata("bouncing", new FixedMetadataValue(main.m, true));
			 * */
			
			ItemStack bomb = Methoden.item(Material.SNOWBALL, 1, 0, "§6§lBomb §5§lin §4§lda §2§lSnow", new String[]{});
			p.getInventory().addItem(bomb);
			
		}
		
		if(args[0].equalsIgnoreCase("/testwelt")) {
			e.setCancelled(true);
			if(args.length == 1) {
				p.teleport(Bukkit.getWorld("testwelt").getSpawnLocation());
			}else {
				p.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
			}
		}
		if(args[0].equalsIgnoreCase("/welt")) {
			e.setCancelled(true);
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
		}
		
		if(args[0].equalsIgnoreCase("/gc")){
			e.setCancelled(true);
			 double tps = Lag.getTPS();
			 double lagg = Math.round((1.0 - tps / 20.0D) * 100.0D);
			    ChatColor color;
			    if (tps >= 18.0D){
			      color = ChatColor.GREEN;
			    }else{
			      if(tps >= 15.0D){
			        color = ChatColor.YELLOW;
			      }else{
			        color = ChatColor.RED;
			      }
			    }
			    
			    Integer[] time = TimeData.getTime(System.currentTimeMillis()-ManagementFactory.getRuntimeMXBean().getStartTime());
			    
			    String timestring = "§c"+ (time[0] > 0 ? time[0]+" Jahre, " : "")
			    					 + (time[1] > 0 ? time[1] + " Monate, " : "")
			    					   + (time[2] > 0 ? time[2] + " Tage, " : "")
			    					   + (time[3] > 0 ? time[3]+ " Stunden, " : "")
			    					   + (time[4] > 0 ? time[4]+" Minuten, " : "")
			    					   + (time[5] > 0 ? time[5]+ " Sekunden " : "00 Sekunden");
			    p.sendMessage("§9§l[-------§e[§c§lServer-Checker§e]§9§l-------]");
			    p.sendMessage("§6Online seit: §b" +timestring);
			    p.sendMessage("§6Aktuelle TPS: " +color+ Methoden.round(tps) + " - " + lagg + "%");
			    p.sendMessage("§6Maximaler Speicher: §b" + (Runtime.getRuntime().maxMemory() / 1024L / 1024L) + " §cMB");
			    p.sendMessage("§6Zugewiesener Speicher: §b" + (Runtime.getRuntime().totalMemory() / 1024L / 1024L)  + " §cMB");
			    p.sendMessage("§6Freier Speicher: §b" + (Runtime.getRuntime().freeMemory() / 1024L / 1024L)  + " §cMB");
			    
			    List<World> worlds = Bukkit.getWorlds();
			    for(World w : worlds){
			    	String worldType = "Normal";
			    	switch (w.getEnvironment()){
			    		case NETHER: 
			    			worldType = "Nether";
			    			break;
			    		case THE_END: 
			    			worldType = "The End";
			    		default:
			    			worldType = "Normal";
			    	}
			    	int tileEntities = 0;
			    	try{
			    		for(Chunk chunk : w.getLoadedChunks()){
			    			tileEntities += chunk.getTileEntities().length;
			    		}
			    	}catch (ClassCastException ex){
			    	}
			    	p.sendMessage("§6Welt \"§c"+w.getName()+"§6\":§6 Type: \"§c"+worldType+"§6\",§c "+w.getLoadedChunks().length+"§6 chunks("+w.getForceLoadedChunks().size()+" ForceLoaded), §c" +w.getEntities().size() + "§6 entities, §c" +tileEntities+ "§6 tiles.");
			    	
			    }
			
		}
		
		
		if(args[0].equalsIgnoreCase("/ping")){
			e.setCancelled(true);
			int ping = ((CraftPlayer)p).getHandle().ping;
			p.sendMessage("§3§lDein Ping: §6" + ping + "ms");
		}
		
		if(args[0].equalsIgnoreCase("/showitem")){
			e.setCancelled(true);
			ItemStack hand = p.getInventory().getItemInMainHand();
			if(hand != null){
				if(hand.getType() != Material.AIR){
					ItemMeta meta = hand.getItemMeta();
					String itemname = (meta.getDisplayName()!=null && meta.getDisplayName().length() > 0 ? meta.getDisplayName() : ""+hand.getType());
					String hovertext = "§e§l"+itemname;
					
					if(hand.getType().getMaxDurability() != 0){
						hovertext += "\n§b§o"+Methoden.round(100.0*(1.0-((double) (((Damageable) hand.getItemMeta()).getDamage())/((double) (hand.getType().getMaxDurability())))))+"%";
					}
					
					if(meta.getLore() != null){
						for(String l : meta.getLore()){
							hovertext += "\n"+l;
						}
					}
					if(meta.getEnchants() != null){
						for(Enchantment ent : meta.getEnchants().keySet()){
							String name = Methoden.translateEnchantment(ent);
							hovertext += "\n§7"+ name + " " + Methoden.roemisch(meta.getEnchants().get(ent));
						}
					}
					if(meta instanceof EnchantmentStorageMeta) {
						for(Enchantment ent : ((EnchantmentStorageMeta) meta).getStoredEnchants().keySet()){
							String name = Methoden.translateEnchantment(ent);
							hovertext += "\n§7"+ name + " " + Methoden.roemisch(((EnchantmentStorageMeta) meta).getStoredEnchants().get(ent));
						}
					}
					
					JSONMessage msg = new JSONMessage("§6" +p.getName()+ " §ezeigt: ");
					msg.addExtra("§b"+hand.getAmount()+"x " + itemname, ClickType.SUGGEST_COMMAND, "/showitem",
							HoverType.SHOW_TEXT, hovertext);
					
					msg.sendToAllPlayers();
					
				}else{
					p.sendMessage("§cDu musst ein Item in der Hand haben!");
				}
			}else{
				p.sendMessage("§cDu musst ein Item in der Hand haben!");
			}
		}
		
		if(args[0].equalsIgnoreCase("/findslimechunk")) {
			e.setCancelled(true);
			int cx = p.getLocation().getChunk().getX();
			int cz = p.getLocation().getChunk().getZ();
			
			int found = 0;
			
			for(int x = -1; x <= 1; x++) {
				for(int z = -1; z <= 1; z++) {
					if(Methoden.isSlimeChunk(p, cx-x, cz-z)){
						found++;
						p.sendMessage("§aDer Chunk bei x: "+ ((cx-x)*16+8) + " und z: "+((cz-z)*16+8) + " ist ein Slimechunk");
					}
				}	
			}
			p.sendMessage("§cEs wurden " + found + " Slimechunks gefunden!");
			
			
			
		}
		
		if(args[0].equalsIgnoreCase("/speed")) {
			e.setCancelled(true);
			Double speed = (double) p.getWalkSpeed();
			p.sendMessage("§2Deine Laufgeschwindigkeit liegt bei: §6"+ Math.round((speed/0.2)*100)+ "%");
		}
		
		if(args[0].equalsIgnoreCase("/testb")) {
			e.setCancelled(true);
			
			
			
			p.sendMessage("key: " +Tag.ITEMS_FISHES.getKey().getKey());
			for(Material mat : Tag.ITEMS_FISHES.getValues()) {
				p.sendMessage("fishmat: " + mat.name());
			}
			
		}
		
		if(args[0].equalsIgnoreCase("/testc")) {
			e.setCancelled(true);
			
			for(int faktor = 1; faktor <= 3; faktor++) {
				int ench = 0;
				
				for(int i = 0; i <= 2000; i++) {
					ench += Methoden.getLoreEnchLvl((double) faktor, 1, 4);
				}
				p.sendMessage("§aFaktor:" + faktor + " ->  ["+ench+"]" + (ench/2000.0));
			}
		}
		
		
		if(args[0].equalsIgnoreCase("/testd")) {
			e.setCancelled(true);
			ItemStack is = p.getItemInHand();
			p.sendMessage("a: " + EnchantmentTarget.ALL.includes(is));
			p.sendMessage("b: " + EnchantmentTarget.ARMOR.includes(is));
			p.sendMessage("c: " + EnchantmentTarget.BREAKABLE.includes(is));
			p.sendMessage("d: " + EnchantmentTarget.WEARABLE.includes(is));
			p.sendMessage("e: " + EnchantmentTarget.TOOL.includes(is));
			p.sendMessage("f: " + EnchantmentTarget.WEAPON.includes(is));
		}
		
		if(args[0].equalsIgnoreCase("/sethome")) {
			e.setCancelled(true);
			
			new File("plugins/homes").mkdirs();
			File file = new File("plugins/homes",p.getUniqueId()+".yml");
			
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.set("homeloc", p.getLocation());
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			p.sendMessage("§aDu hast dein Zuhause gesetzt!");
		}
		
		if(args[0].equalsIgnoreCase("/home")) {
			e.setCancelled(true);
			new File("plugins/homes").mkdirs();
			File file = new File("plugins/homes",p.getUniqueId()+".yml");
			
			if(file.exists()) {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				Location loc = (Location) cfg.get("homeloc");
				p.teleport(loc);
				p.sendMessage("§aDu wurdest zu deinem Zuhause teleportiert!");
			}else {
				p.sendMessage("§cDu hast noch kein Zuhause gesetzt!");
			}
		}
		
		
		if(args[0].equalsIgnoreCase("/testnbt")){
			e.setCancelled(true);
			
			ItemStack item = Methoden.item(Material.CHEST, 1, 0, "§6§lSexy Backpack", new String[]{});
			NBTItem nbti = new NBTItem(item);
			nbti.setString("Kevin", "Kevin ist sein Name!");
			nbti.setInteger("Kevin2", 42);
			
			item = nbti.getItem();
			
			p.getInventory().addItem(item);
		}
		
		if(args[0].equalsIgnoreCase("/getnbt")) {
			e.setCancelled(true);
			NBTItem nbti = new NBTItem(p.getItemInHand());
			
			p.sendMessage(nbti.asNBTString());
			
	
		}
		
		
		if(args[0].equalsIgnoreCase("/tictactoe") || args[0].equalsIgnoreCase("/ttt")){
			e.setCancelled(true);
			try{
				Player x = Bukkit.getPlayer(args[1]);
				
				if(x != null){
					if(!x.getName().equals(p.getName())){
						
						if(TicTacToe.hasAnfrage(x, p)){
							if(TicTacToe.hasGame(x)){
								p.sendMessage("§cWarte bitte, §e" + x.getName() + " §cspielt bereits!");
							}else{
								new TicTacToe(x,p);
								TicTacToe.removeAnfrage(x,p);
							}
						}else{
							if(!TicTacToe.hasAnfrage(p, x)){
								p.sendMessage("§eErfolgreich §a"+x.getName()+" §ezum TicTacToe herrausgefordert!");
								x.sendMessage("§eDu wurdest von §d" + p.getName() + "§e in TicTacToe herrausgefordert! \n§eNoch: §c120 Sekunden bis zum Ablaufen der Anfrage!");
								JSONMessage click = new JSONMessage("");
								click.addExtra("§a/TicTacToe " + p.getName(), ClickType.RUN_COMMAND, "/TicTacToe " + p.getName(), HoverType.SHOW_TEXT, "§e*§dKlick§e*");
								Methoden.sendRawMessage(x, click.toString());
								
								TicTacToe.addAnfrage(p, x);
							}else{
								p.sendMessage("§cDu hast §e"+x.getName()+"§c schon herrausgefordert!");
							}
						}
					}else{
						p.sendMessage("§cDu darfst nicht gegen dich spielen!");
					}
				}else{
					p.sendMessage("§cDer Spieler §4" + args[1] + "§c ist nicht Online!");
				}
				
			}catch(Exception ex){
				p.sendMessage("§c/tictactoe [Spieler]");
			}
		}
		
		if(args[0].equalsIgnoreCase("/4gewinnt") || args[0].equalsIgnoreCase("/Viergewinnt") || args[0].equalsIgnoreCase("/4wins") || args[0].equalsIgnoreCase("/4win") ){
			e.setCancelled(true);
			try{
				Player x = Bukkit.getPlayer(args[1]);
				
				if(x != null){
					if(!x.getName().equals(p.getName())){
						if(VierGewinnt.hasAnfrage(x, p)){
							if(VierGewinnt.hasGame(x)){
								p.sendMessage("§cWarte bitte, §e" + x.getName() + " §cspielt bereits!");
							}else{
								new VierGewinnt(x,p);
								VierGewinnt.removeAnfrage(x,p);
							}
						}else{
							if(!VierGewinnt.hasAnfrage(p, x)){
								p.sendMessage("§eErfolgreich §a"+x.getName()+" §ezum 4Gewinnt herrausgefordert!");
								x.sendMessage("§eDu wurdest von §d" + p.getName() + "§e in 4Gewinnt herrausgefordert! \n§eNoch: §c120 Sekunden bis zum Ablaufen der Anfrage!");
								JSONMessage click = new JSONMessage("");
								click.addExtra("§a/4Gewinnt " + p.getName(), ClickType.RUN_COMMAND, "/4Gewinnt " + p.getName(), HoverType.SHOW_TEXT, "§e*§dKlick§e*");
								Methoden.sendRawMessage(x, click.toString());
								VierGewinnt.addAnfrage(p, x);
							}else{
								p.sendMessage("§cDu hast §e"+x.getName()+"§c schon herrausgefordert!");
							}
						}
					}else{
						p.sendMessage("§cDu darfst nicht gegen dich spielen!");
					}
				}else{
					p.sendMessage("§cDer Spieler §4" + args[1] + "§c ist nicht Online!");
				}
			}catch(Exception ex){
				
				p.sendMessage("§c/4Gewinnt [Spieler]");
				
			}
		}
	
		if(args[0].equalsIgnoreCase("/addlore")){
			e.setCancelled(true);
			if(p.isOp()){
				if(args.length > 1){
					if(p.getItemInHand() != null){
						ItemStack item = p.getItemInHand();
						if(item.getType() != null && item.getType() != Material.AIR){
							ItemMeta meta = item.getItemMeta();
							List<String> lore = meta.getLore();
							if(lore == null){
								lore = new ArrayList<>();
							}
							String add = e.getMessage().substring(9);
							add = Methoden.translateChatColorCodes(add);
							lore.add(add);
							meta.setLore(lore);
							item.setItemMeta(meta);
							p.sendMessage("§aDie Lore des Items wurde erfolgreich geändert.");
						}else{
							p.sendMessage("§cDu hast kein Item in deiner Hand.");
						}
					}else{
						p.sendMessage("§cDu hast kein Item in deiner Hand.");
					}
				}else{
					p.sendMessage("§c/addlore <Text>");
				}
			}
		}
		if(args[0].equalsIgnoreCase("/removeLore")){
			e.setCancelled(true);
			if(p.isOp()){
				if(e.getMessage().length() > 10){
					String remove = e.getMessage().substring(12).replaceAll( "&", "§");
					if(p.getItemInHand() != null){
						ItemStack item = p.getItemInHand();
						ItemMeta meta = item.getItemMeta();
						List<String> lore = meta.getLore();
						
						if(lore != null){
							boolean rem = lore.remove(remove);
							if(rem){
								meta.setLore(lore);;
								item.setItemMeta(meta);
								p.setItemInHand(item);
								p.sendMessage("§aErfolgreich diese Lore entfernt!");
							}else{
								p.sendMessage("§cDas Item hat diese Lore nicht!");
							}
						}else{
							p.sendMessage("§cDas Item hat keine Lore!");
						}
						
					}else{
						p.sendMessage("§cDu hast kein Item in der Hand!");
					}
				}else{
					p.sendMessage("§c/removelore <Text>");
				}
			}
		}
		
		

		
		
	}
	
}
