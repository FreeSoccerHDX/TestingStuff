package de.freesoccerhdx.testingstuff.main;

import java.awt.Color;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.core.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftFishHook;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftShulker;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.FileUtil;
import org.bukkit.util.Vector;
import org.spigotmc.AsyncCatcher;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;

import de.freesoccerhdx.testingstuff.customentity.utils.CustomEntity;
import de.freesoccerhdx.testingstuff.main.utils.AnvilGUI;
import de.freesoccerhdx.testingstuff.main.utils.ClickHologram;
import de.freesoccerhdx.testingstuff.main.utils.CobbleGeneratorEvent;
import de.freesoccerhdx.testingstuff.main.utils.ItemBuilder;
import de.freesoccerhdx.testingstuff.main.utils.ClickHologram.HoloClickListener;
import de.freesoccerhdx.testingstuff.main.utils.JSONMessage;
import de.freesoccerhdx.testingstuff.main.utils.NaviBossBar;
import de.freesoccerhdx.testingstuff.main.utils.JSONMessage.JSONClickType;
import de.freesoccerhdx.testingstuff.main.utils.JSONMessage.JSONHoverType;
import de.freesoccerhdx.testingstuff.main.utils.JSONMessage.JSONStyleElement;
import de.freesoccerhdx.testingstuff.main.utils.PacketListener;
import de.freesoccerhdx.testingstuff.main.utils.AnvilGUI.AnvilAction;
import de.freesoccerhdx.testingstuff.main.utils.AnvilGUI.AnvilSlot;
import de.freesoccerhdx.testingstuff.main.utils.InventoryHelper.ClickAction;
import de.freesoccerhdx.testingstuff.main.utils.SignGUI;
import de.freesoccerhdx.testingstuff.main.utils.WeightedRandom;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.IRegistry;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.entity.monster.EntityShulker;
import net.minecraft.world.item.enchantment.EnchantmentVanishing;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import net.minecraft.world.level.block.entity.TileEntityFurnaceFurnace;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import de.freesoccerhdx.testingstuff.main.utils.SignGUI.SignGUIListener;

public class TestingStuff extends JavaPlugin implements Listener {

	
	public static TestingStuff main = null;
	
	
	@Override
	public void onEnable() { 
		main = this;
		Bukkit.getPluginManager().registerEvents(main, main);
		Bukkit.getPluginManager().registerEvents(new PacketListener(), main);
		Bukkit.getPluginManager().registerEvents(new CobbleGeneratorEvent(), main);
		
		
		ChunkRespawnFix.init();
		Bukkit.getServer().getPluginManager().registerEvents(new ChunkRespawnFix(), this);
		
		
		registerEnchantment();
		
		
		
		//net.minecraft.server.v1_16_R3.ItemStack nsmitem = CraftItemStack.asNMSCopy(new ItemStack(Material.BLACKSTONE));
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			PacketListener.removePlayer(p);
			PacketListener.injectPlayer(p);
		}
		
		
		//AsyncCatcher.enabled = false;
	}
	
	 @EventHandler
	 public void onspawnentity(EntitySpawnEvent e) {
		 if(e.getEntity() instanceof FishHook) {
			 CraftFishHook cfh = (CraftFishHook) e.getEntity();
			 cfh.getHandle().minWaitTime = 0;
			 cfh.getHandle().maxWaitTime = 5;
		 }
		 /*
		 
		 if(e.getEntity() instanceof LivingEntity) {
			 e.getEntity().setGravity(false);
			 
			 
			 runMobTickVelocity((LivingEntity) e.getEntity());
			 
		 }
		 
		 */
	 }
	 
	 private static HashMap<LivingEntity, Integer> taskids = new HashMap<>();
	 private void runMobTickVelocity(LivingEntity le) {
		 int taskid = 0;
		 
		 taskid = Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
			CraftLivingEntity cle = (CraftLivingEntity) le;
			EntityLiving el = cle.getHandle();
			 
			@Override
			public void run() {
				if(le.isValid()) {
					el.setMot(el.getMot().add(0.0D, -0.08 / 4.0D, 0.0D));
				}else {
					cancelTask(taskids.get(le));
				}
			}
			 
		 }, 1, 1).getTaskId();
		 
		 taskids.put(le,taskid);
	 }
	 
	 private void cancelTask(int i) {
		 Bukkit.getScheduler().cancelTask(i);
	 }
	 
	 
	
	@Override
	public void onDisable() {
		ChunkRespawnFix.stop();
	}
	
	
	private void saveNBT(NBTTagCompound ntc, File file) {
        try {
            if(!file.exists()) {
            	FileUtils.makeParentDirs(file);
                file.createNewFile();
            }

            FileOutputStream stream = new FileOutputStream(file);

            NBTCompressedStreamTools.a(ntc, stream);
            stream.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	public NBTTagCompound readNBT(File file) {
		NBTTagCompound ntc = null;
        try {
            // if the file exists we read it
            if(file.exists()) {
                FileInputStream fileinputstream = new FileInputStream(file);
                ntc = NBTCompressedStreamTools.a(fileinputstream);
                fileinputstream.close();
               
            } else {
            	ntc = new NBTTagCompound();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ntc;
    }
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e) {
		Chunk chunk = e.getChunk();
		
		if(!e.isSaveChunk()) return;
		
		Entity[] entities = chunk.getEntities();
		
		List<UUID> apply = new ArrayList<>();
		
		NBTTagList ntl = new NBTTagList();
		boolean hasdata = false;
		for(Entity ent : entities) {
			CraftEntity ce = (CraftEntity) ent;
			if(!apply.contains(ce.getUniqueId())) {
				if(ce.getHandle().getClass().getName().endsWith("CustomEntity")) {
					
					//Bukkit.broadcastMessage(ce.getHandle().getClass().getName());
					apply.add(ce.getUniqueId());
					ntl.add(ce.getHandle().save(new NBTTagCompound()));
					hasdata = true;
					ent.remove();
				}
			}else {
				ent.remove();
			}
			
		}
		
	//	Bukkit.broadcastMessage(chunk + " -> " + entities.length + " -> " + apply.size());
		
		String dataname = chunk.getX() + " " + chunk.getZ() + ".dat";
		File file = new File(getDataFolder().getAbsolutePath()+"/"+chunk.getWorld().getName()+"/customentity",dataname);
		
		if(hasdata) {
			NBTTagCompound ntc = new NBTTagCompound();
			ntc.set("Entities", ntl);
			saveNBT(ntc, file);
		}else {
			if(file.exists()) {
				file.delete();
			}
		}
	}
	
	
	
	// 	nbttagcompound.set("Pos", a(new double[]{this.vehicle.locX(), locY(), this.vehicle.locZ()}));
	
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		Chunk chunk = e.getChunk();
		
		String dataname = chunk.getX() + " " + chunk.getZ() + ".dat";
		
		NBTTagCompound ntc = readNBT(new File(getDataFolder().getAbsolutePath()+"/"+chunk.getWorld().getName()+"/customentity",dataname));
		if(ntc.hasKey("Entities")) {
			NBTTagList ntl = ntc.getList("Entities",10);
			
			for(int i = 0; i < ntl.size(); i++) {
				NBTTagCompound nbtentity = (NBTTagCompound) ntl.get(i);
				NBTTagList poslist = nbtentity.getList("Pos", 6);
				
				CustomEntity ce = new CustomEntity(new Location(chunk.getWorld(),poslist.h(0),poslist.h(1),poslist.h(2)));
				ce.load(nbtentity);
				
			}
			
		}
		
	}
	
	
	
	//@EventHandler
	public void BlockSpreadEvent(BlockSpreadEvent e) {
		Bukkit.broadcastMessage("spread: " + e.getNewState().getType());
	}
	
	
	//@EventHandler
	public void onblockform(BlockFormEvent e) {
		Bukkit.broadcastMessage("Form: "+e.getBlock().getType().toString());
		
	}
	
	

	
	//@EventHandler
	public void onblockfromto(BlockFromToEvent e) {
		Bukkit.broadcastMessage("fromto: "+e.getBlock().getType().toString());
		Bukkit.broadcastMessage("to: "+e.getToBlock().getType().toString());
		
	}
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ClickHologram.checkClick(p);
		
		if(e.getClickedBlock() != null) {
			World w = p.getWorld();
			CraftWorld cw = (CraftWorld) w;
			
			
			
			Block b = e.getClickedBlock();
			CraftBlock cb = (CraftBlock) b;
			
			CraftBlockData cbd = (CraftBlockData) cb.getBlockData();
			if(cbd.getState().isTicking()) {
				Bukkit.broadcastMessage("ticking");
				for(int i = 0; i < 100; i ++) {
					cbd.getState().getBlock().tick(cbd.getState(), cw.getHandle(), new BlockPosition(b.getX(), b.getY(), b.getZ()), new Random());
				}
			}
			TileEntity te = cw.getHandle().getTileEntity(new BlockPosition(b.getX(),b.getY(),b.getZ()));
		//	Bukkit.broadcastMessage("Te: "+ te);
			if(te != null && te instanceof TileEntityFurnaceFurnace) {
				Bukkit.broadcastMessage("is furnace");
				TileEntityFurnace tef = (TileEntityFurnace) te;
				for(int i = 0; i< 1000; i++) {
					
					//tef.tick();
					TileEntityFurnace.a(tef.getWorld(), tef.getPosition(), tef.getBlock(), tef);
					
					
				}
				
			}
			
			
			if(p.isSneaking()) e.setCancelled(true);
		}
		
	}
	
	
	public static int shulkerpeek = 0;
	public static CraftShulker ca = null;
	
	public static void disguisedBlockWithShulker(Player p, Location loc, Material mat) {
		WorldServer nmsworld = ((CraftWorld) p.getWorld()).getHandle();
		
		//EntityShulker shulk = new EntityShulker(EntityTypes.SHULKER, nmsworld);
		EntityShulker shulk = new EntityShulker(EntityTypes.ay, nmsworld);
		shulk.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);

		shulk.setFlag(5, true); // invis
		shulk.setFlag(6, true); // glow

		EntityFallingBlock efb = new EntityFallingBlock(nmsworld, shulk.locX(), shulk.locY(), shulk.locZ(), CraftBlockData.newData(mat, null).getState());
		efb.setLocation(shulk.locX(), shulk.locY(), shulk.locZ(), 0, 0);
		efb.setNoGravity(true);
		efb.setInvisible(true);
	
		
		PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(efb.getId(), efb.getDataWatcher(), false);
		PacketPlayOutEntityMetadata ppoem2 = new PacketPlayOutEntityMetadata(shulk.getId(), shulk.getDataWatcher(), true);
		
		
		
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		ep.b.sendPacket(efb.getPacket());
		ep.b.sendPacket(shulk.getPacket());
		ep.b.sendPacket(ppoem);
		ep.b.sendPacket(ppoem2);
	}
	
	
	@EventHandler
	public <T> void onCmd(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		String cmd = args[0];
		Player p = e.getPlayer();
	
		if(cmd.equalsIgnoreCase("/copyshulkerbox")) {
			
			int x1 = Integer.parseInt(args[1]);
			int y1 = Integer.parseInt(args[2]);
			int z1 = Integer.parseInt(args[3]);
			
			int x2 = Integer.parseInt(args[4]);
			int y2 = Integer.parseInt(args[5]);
			int z2 = Integer.parseInt(args[6]);
			
			int minX = Math.min(x1, x2);
			int maxX = Math.max(x1, x2);
			
			int minY = Math.min(y1, y2);
			int maxY = Math.max(y1, y2);
			
			int minZ = Math.min(z1, z2);
			int maxZ = Math.max(z1, z2);
			
			HashMap<Vector,Material> blocks = new HashMap<>();
			Vector ref = new Vector(minX,minY,minZ); 
					
			for(int x = minX; x <= maxX; x++) {
				for(int y = minY; y <= maxY; y++) {
					for(int z = minZ; z <= maxZ; z++) {
						Block b = p.getWorld().getBlockAt(x, y, z);
						Vector dif = ref.clone().subtract(b.getLocation().toVector());
						
						blocks.put(dif, b.getType());
						
						
					}
				}
			}
			
			
			for(Vector v : blocks.keySet()) {
				disguisedBlockWithShulker(p, p.getLocation().clone().add(v), blocks.get(v));
			}
			
			Bukkit.broadcastMessage("§aTotal Blocks: " + blocks.size());
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/shulkerblock")) {
			//Shulker shulk = (Shulker) p.getWorld().spawnEntity(p.getLocation(), EntityType.SHULKER);
			//shulk.setColor(DyeColor.BLUE);
			//shulk.setGlowing(true);
			
			disguisedBlockWithShulker(p, p.getLocation(), Material.STONE);
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/shulkpeek")) {
			shulkerpeek = Integer.parseInt(args[1]);
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/shulkerpeek")) {
			ca = p.getWorld().spawn(p.getLocation(), CraftShulker.class); 
			
			
			//ca.getHandle().getDataWatcher().set(DataWatcher.a(EntityShulker.class, DataWatcherRegistry.n), EnumDirection.UP);
		//	ca.getHandle().getDataWatcher().set(DataWatcher.a(EntityShulker.class, DataWatcherRegistry.n), EnumDirection.b);
			
			Bukkit.getScheduler().runTaskTimer(main, new Runnable() {

				@Override
				public void run() {
					System.out.println("Peek: " + ca.getHandle().getPeek());
				}
				
			}, 0, 200);
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/arrowbody")) {
			
			p.setArrowsInBody(Integer.parseInt(args[1]));
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/gravity")) {
			p.setGravity(!p.hasGravity());
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/customentity")) {
			CustomEntity ce = new CustomEntity(p.getLocation());
			
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/fakeNPC")) {
			CraftPlayer cp = (CraftPlayer)p;
			EntityPlayer ep = cp.getHandle();
			
			
			Skeleton ent = (Skeleton) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON);
			ent.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false, false));
			CraftLivingEntity cle = (CraftLivingEntity) ent;
			EntityLiving el = cle.getHandle();
			int entid = el.getId();

			
			ep.b.sendPacket(new PacketPlayOutEntityDestroy(entid-1));
			
			MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer(); //NMS Minecraft Server
	        WorldServer nmsWorld = ((CraftWorld) p.getWorld()).getHandle(); //NMS World Server
	        UUID gpuuid = ent.getUniqueId();
	        GameProfile gp = new GameProfile(gpuuid, "MyName"); //NMS Game Profile
			
	        Location spawnloc = p.getLocation();
	        //EntityPlayer npcep = new EntityPlayer(nmsServer, nmsWorld, gp, new PlayerInteractManager(nmsWorld));
	        EntityPlayer npcep = new EntityPlayer(nmsServer, nmsWorld, gp);
	        npcep.setLocation(spawnloc.getX(), spawnloc.getY(), spawnloc.getZ(), spawnloc.getYaw(), spawnloc.getPitch());
	        npcep.e(entid); // set new entity id to npc
	        
	        //PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npcep);
	        PacketPlayOutPlayerInfo ppopi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.a, npcep);
	        PacketPlayOutNamedEntitySpawn pposel = new PacketPlayOutNamedEntitySpawn(npcep);
	        
	        ep.b.sendPacket(ppopi);
	        ep.b.sendPacket(pposel);
	        
	        
	    	ent.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
			ent.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
			
			ent.getEquipment().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
			
			List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>>  equip = new ArrayList<>();
			
			//equip.add(Pair.of(EnumItemSlot.MAINHAND, el.getEquipment(EnumItemSlot.MAINHAND)));
			equip.add(Pair.of(EnumItemSlot.a, el.getEquipment(EnumItemSlot.a)));
			
			
			PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(entid, equip);
			ep.b.sendPacket(ppoee);
			
			
			
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {

				@Override
				public void run() {
					ent.setGlowing(true);
				}
				
			}, 60);
			
			e.setCancelled(true);
		}
		
		
		if(cmd.equalsIgnoreCase("/testinvrename")) {
			Inventory inv = Bukkit.createInventory(null, 18, "Title Nr. 1");
			p.openInventory(inv);
			
			CraftPlayer cp = (CraftPlayer)p;
			EntityPlayer ep = cp.getHandle();
			
			
			Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
				int i = 1;
				
				@Override
				public void run() {
					if(p.getOpenInventory() != null) {
						//int windowid = ep.activeContainer.windowId;
						int windowid = ep.bV.j;
						
						//PacketPlayOutOpenWindow ppoow = new PacketPlayOutOpenWindow(windowid, ep.activeContainer.getType(), new ChatComponentText("Title Nr. " + (i++)));
						PacketPlayOutOpenWindow ppoow = new PacketPlayOutOpenWindow(windowid, ep.bV.getType(), new ChatComponentText("Title Nr. " + (i++)));
						ep.b.sendPacket(ppoow);
						
						//PacketPlayOutWindowItems ppowi = new PacketPlayOutWindowItems(windowid, ep.activeContainer.items);
						PacketPlayOutWindowItems ppowi = new PacketPlayOutWindowItems(windowid, ep.bU.incrementStateId(), ep.bV.n, CraftItemStack.asNMSCopy(p.getOpenInventory().getCursor()));
						ep.b.sendPacket(ppowi);
						
						if(p.getOpenInventory().getCursor() != null) {
							net.minecraft.world.item.ItemStack nmsitemstack = CraftItemStack.asNMSCopy(p.getOpenInventory().getCursor());
							PacketPlayOutSetSlot pposs = new PacketPlayOutSetSlot(-1, ep.bU.incrementStateId(), -1, nmsitemstack);
							ep.b.sendPacket(pposs);
						}
						
					}
				}
				
			}, 20,20);
			
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/testtick")) {
			World w = p.getWorld();
			CraftWorld cw = (CraftWorld) w;
			
			
			
			Block b = p.getLocation().getBlock();
			CraftBlock cb = (CraftBlock) b;
			
			CraftBlockData cbd = (CraftBlockData) cb.getBlockData();
			if(cbd.getState().isTicking()) {
				for(int i = 0; i < 100; i ++) {
					cbd.getState().getBlock().tick(cbd.getState(), cw.getHandle(), new BlockPosition(b.getX(), b.getY(), b.getZ()), new Random());
				}
			}
			
			e.setCancelled(true);
		}
		
		
		if(cmd.equalsIgnoreCase("/testweightedrandom")) {
			
			
			HashMap<Integer, Double> weightmap = new HashMap<>();
			weightmap.put(0, 1000.0);
			weightmap.put(1, 1000.0);
			weightmap.put(2, 100.0);
			weightmap.put(3, 10.0);
			weightmap.put(4, 1.0);
			
			
			
			int[] resultes = new int[] {0,0,0,0,0};
			
			for(int i = 0; i < 1000; i++) {
				int result = WeightedRandom.getWeightedRandom(weightmap);
				resultes[result] += 1;
			}
			
			for(int i = 0; i < resultes.length; i++) {
				Bukkit.broadcastMessage(i + " -> " + (resultes[i]) + " [" +resultes[i]/1000.0+ "]");
			}
			
			
			
			e.setCancelled(true);
			
		}
		
		
		if(cmd.equalsIgnoreCase("/testcopyspawner")) {
			Block b = p.getWorld().getBlockAt(742, 50, 175);
			
			World w = b.getWorld();
			CraftWorld cw = (CraftWorld) w;
			WorldServer ws = cw.getHandle();
			
			
			

			
			TileEntity tileent = ws.getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()));
			
			TileEntityMobSpawner tems = (TileEntityMobSpawner) tileent;
			NBTTagCompound mysave = new NBTTagCompound();
			tems.getSpawner().b(ws, tileent.getPosition(), mysave);
				
			Bukkit.broadcastMessage("nbtdata: " + mysave.asString());
			
			
			Block pblock = p.getLocation().getBlock();
			
			pblock.setType(Material.SPAWNER);
			
			
			TileEntity tileent2 = ws.getTileEntity(new BlockPosition(pblock.getX(), pblock.getY(), pblock.getZ()));
			
			TileEntityMobSpawner tems2 = (TileEntityMobSpawner) tileent2;
			try {
				tems2.getSpawner().a(ws, tileent2.getPosition(), MojangsonParser.parse(mysave.asString()));
			} catch (CommandSyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
			
		}
		
		// https://wiki.vg/Protocol#Change_Game_State
		if(cmd.equalsIgnoreCase("/teststate")) {
			e.setCancelled(true);
			
			int a = Integer.parseInt(args[1]);
			float b = Float.parseFloat(args[2]);
			
			int amount = 1;
			
			if(args.length > 3) {
				amount = Integer.parseInt(args[3]);
			}
			
			PacketPlayOutGameStateChange ppogsc = new PacketPlayOutGameStateChange(new PacketPlayOutGameStateChange.a(a), b);
			
			CraftPlayer cp = (CraftPlayer) p;
			EntityPlayer ep = cp.getHandle();
			
			while(amount > 1) {
				ep.b.sendPacket(ppogsc);
				amount --;
			}
			
		}
		
		if(cmd.equalsIgnoreCase("/testinvmenu")) {
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					
					CraftPlayer cp = (CraftPlayer) p;
					EntityPlayer ep = cp.getHandle();
					net.minecraft.world.item.ItemStack nmsitemstack = CraftItemStack.asNMSCopy(new ItemStack(Material.BLACKSTONE));
					
					PacketPlayOutSetSlot pposs = new PacketPlayOutSetSlot(0, ep.bU.incrementStateId(), 0, nmsitemstack);
					
					
					ep.b.sendPacket(pposs);
					
				}
			}, 20);
			
		
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/testnavi")) {
			
			NaviBossBar nbb = new NaviBossBar(p, p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(20)));
			nbb.getNaviMessage().default_nothing = "";
		//	nbb.addPlayer(p);
		//	nbb.show();
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/testitemstack")) {
			
			UUID uuid = UUID.fromString("ed198559-89c3-4882-9600-2a472a9b9875");
			uuid = UUID.randomUUID();
			
			ItemBuilder ib = new ItemBuilder(Material.PLAYER_HEAD).setTexture(uuid,"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiZjMwMjlkYWVkNDExYmYyYjAwMzI5MDc4MGVlOGEwYmNmNTllZjZkM2EyZTM4YWMzMjMwNmFhNWI0M2M1YyJ9fX0=");
			
			p.getInventory().addItem(ib.toItemStack());
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/testarmorstand")) {
			
			ArmorStand as = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
			as.setCustomName("my custom name");
			as.setCustomNameVisible(true);
			
			ClickHologram ch = new ClickHologram(as);
			ch.addPlayer(p);
			ch.addClickListener(new HoloClickListener() {
				
				@Override
				public boolean click(Player p, Location where, Location posArmorStand,String target_word, double distance, double perc_distance_left) {
					//Bukkit.broadcastMessage("Player hit! " + perc_distance_left + "%");
					Bukkit.broadcastMessage("Target: " + target_word);
					
					return false;
				}
			});
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/jsonmsg")) {
			
			JSONMessage msg = new JSONMessage("");
			msg.addExtra("Extra", JSONClickType.RUN_COMMAND, "Haha noob", JSONHoverType.SHOW_TEXT, "/gamemode Creative", new Color(123,123,54));
			
			JSONStyleElement[] jse = new JSONStyleElement[] {JSONStyleElement.BOLD, JSONStyleElement.ITALIC, JSONStyleElement.UNDERLINED};
			
			msg.addExtra(" Extra", JSONClickType.RUN_COMMAND, "Haha noob", JSONHoverType.SHOW_TEXT, "/gamemode Creative",jse,new Color(123,123,54));
			msg.sendToPlayer(p);
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/signgui")) {
			
			SignGUI.openGUI(p, new String[] {"","^^^^^^^^^^^^","Insert Custom","Name"}, new SignGUIListener() {
				
				@Override
				protected void listen(Player p, String[] lines) {
					Bukkit.broadcastMessage(p.getName());
					for(String s : lines) {
						Bukkit.broadcastMessage("Line: " + s);
					}
					
				}
			});
			
			e.setCancelled(true);
		}
		
		if(cmd.equalsIgnoreCase("/anvilgui")) {
			
			AnvilGUI gui = new AnvilGUI("§6§lInput Name:");
			
			gui.setAnvilAction(new AnvilAction() {
				
				@Override
				public void run(Player player, AnvilSlot anvilslot, String name) {
				//	Bukkit.broadcastMessage(player.getName() + " -> " + anvilslot + "-> " + name);
					
					if(anvilslot == AnvilSlot.OUTPUT) {
						p.sendMessage("§aDu hast §e" + name + "§a geschrieben!");
						p.closeInventory();
					}
					
					//
				}
			});
			
			gui.open(p);
			e.setCancelled(true);
		}
		
		/*
		if(cmd.equalsIgnoreCase("/testinv")) {
			
			Inventory inv = Bukkit.createInventory(null, 9, "Title");
			InventoryHelper ih = new InventoryHelper(inv);
			
			ItemStack testitem = new ItemStack(Material.STONE,40);
			ItemMeta meta = testitem.getItemMeta();
			meta.setDisplayName("test");
			testitem.setItemMeta(meta);
			
			inv.setItem(4, testitem);
			
			ih.setDefaultCancelClick(true);
			ih.setDefaultClickAction(new ClickAction() {

				@Override
				void run(Player p, InventoryView view, ClickType clicktype, int clickedslot, ItemStack clickeditem,
						ItemStack cursoritem) {
					Bukkit.broadcastMessage("default click");
					
				}
			});
			
			ih.addInvListener(new SlotMatch() {
				
				@Override
				boolean matches(Player p, InventoryView view, ClickType clicktype, int clickedslot, ItemStack clickeditem,
						ItemStack cursoritem) {
					
					return clickeditem.isSimilar(testitem);
				}
			}, new ClickAction() {
				

				@Override
				void run(Player p, InventoryView view, ClickType clicktype, int clickedslot, ItemStack clickeditem,
						ItemStack cursoritem) {
					Bukkit.broadcastMessage("specific item");
					
				}
			}, false);
			
			
			p.openInventory(inv);
			e.setCancelled(true);
		}
		*/
		if(cmd.equalsIgnoreCase("/testtest")) {
			
			Merchant mi = Bukkit.createMerchant("Trading Test");
			
			MerchantRecipe mr = new MerchantRecipe(new ItemStack(Material.BLACK_BANNER), 1);
			mr.addIngredient(new ItemStack(Material.DIAMOND_AXE));
			mr.addIngredient(new ItemStack(Material.DIAMOND_AXE));
			
			List<MerchantRecipe> list = new ArrayList<>();
			Random r = new Random();
			
			for (int i = 0; i < 200; i ++) {
				MerchantRecipe mr2 = new MerchantRecipe(new ItemStack(Material.values()[r.nextInt(Material.values().length)]), r.nextInt(64)+1);
				mr2.addIngredient(new ItemStack(Material.values()[r.nextInt(Material.values().length)],r.nextInt(64)+1));
				mr2.addIngredient(new ItemStack(Material.values()[r.nextInt(Material.values().length)],r.nextInt(64)+1));
				list.add(mr2);
				
			}
			list.add(mr);
			
			mi.setRecipes(list);
			CraftPlayer cp = (CraftPlayer) p;
			cp.openMerchant(mi, true);
		}	
	}
	
	//@EventHandler
	public void oninvss(InventoryOpenEvent e) {
		Bukkit.broadcastMessage(e.getInventory().getType().toString());
	}
	
	//@EventHandler
	public void onInv(InventoryClickEvent e) {
		Bukkit.broadcastMessage("bla: " + e.getInventory().getType().name() +" -> "+ e.getRawSlot());
		Bukkit.broadcastMessage(e.getView().getTopInventory().toString());
		Bukkit.broadcastMessage(e.getView().getBottomInventory().toString());
		Bukkit.broadcastMessage(e.getClickedInventory().toString());
	}
	
	@EventHandler
	public void onBlockBreak(BlockDamageEvent e) {
		e.setInstaBreak(true);
		e.setCancelled(true);
		Bukkit.broadcastMessage("eeee");
	}
	
	
	@EventHandler
	public void onPreEnchant(PrepareItemEnchantEvent e) {
		EnchantmentOffer[] offers = e.getOffers();
		offers[0] = new EnchantmentOffer(testenchant, 5, 60);
		offers[1] = new EnchantmentOffer(testenchant, 5, 80);
		offers[2] = new EnchantmentOffer(testenchant, 5, 99);
	
		e.setCancelled(false);
		Bukkit.broadcastMessage("eff");
	}
	
	
	
	static Enchantment testenchant = new TestEnchant("testenchant");
	//net.minecraft.world.item.enchantment.Enchantment VANISHING_CURSE = as("testenchant", new EnchantmentVanishing(net.minecraft.world.item.enchantment.Enchantment.Rarity.VERY_RARE, EnumItemSlot.values()));
	net.minecraft.world.item.enchantment.Enchantment VANISHING_CURSE = as("testenchant", new EnchantmentVanishing(net.minecraft.world.item.enchantment.Enchantment.Rarity.d, EnumItemSlot.values()));
	
	private static net.minecraft.world.item.enchantment.Enchantment as(String s, net.minecraft.world.item.enchantment.Enchantment enchantment) {
		//return IRegistry.<net.minecraft.world.item.enchantment.Enchantment>a(IRegistry.ENCHANTMENT, s, enchantment);
		return IRegistry.<net.minecraft.world.item.enchantment.Enchantment>a(IRegistry.X, s, enchantment);
	}
	
	
	
	private static void registerEnchantment(){
		Enchantment _testenchant = testenchant;
		
		
		
		System.out.println("Key:"+_testenchant.getKey());
		System.out.println("Name:"+_testenchant.getName());
		
		
		
	//	byKey.put(enchantment.key, enchantment);
	//    byName.put(enchantment.getName(), enchantment);
		

		try {
			
			Field f0 = Enchantment.class.getDeclaredField("byName");
			f0.setAccessible(true);
			
			Map<String, Enchantment> byName = (Map<String, Enchantment>) f0.get(Enchantment.class); 
			
			if(!byName.containsKey(testenchant.getName())) {
				
			//	acceptingNew
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(Enchantment.class, true);	
					
				Enchantment.registerEnchantment(testenchant);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
}




























