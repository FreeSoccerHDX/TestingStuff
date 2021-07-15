package freesoccerhdx.survivalplus.haupt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.attribute.Attribute;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import freesoccerhdx.survivalplus.enchants.EnchantLimits;
import freesoccerhdx.survivalplus.enchants.EnchantmentHandler;
import freesoccerhdx.survivalplus.events.blocks.BlockBreakEventHandler;
import freesoccerhdx.survivalplus.events.blocks.BlockDispenseEventHandler;
import freesoccerhdx.survivalplus.events.blocks.BlockPistonExtendEventHandler;
import freesoccerhdx.survivalplus.events.blocks.BlockPlaceEventHandler;
import freesoccerhdx.survivalplus.events.entity.EntityDamageByEntityEventHandler;
import freesoccerhdx.survivalplus.events.entity.EntityDamageEventHandler;
import freesoccerhdx.survivalplus.events.entity.EntityDeathEventHandler;
import freesoccerhdx.survivalplus.events.entity.EntityShootBowEventHandler;
import freesoccerhdx.survivalplus.events.entity.EntitySpawnEventHandler;
import freesoccerhdx.survivalplus.events.entity.ProjectileHitEventHandler;
import freesoccerhdx.survivalplus.events.entity.ProjectileLaunchEventHandler;
import freesoccerhdx.survivalplus.events.player.AsyncPlayerChatEventHandler;
import freesoccerhdx.survivalplus.events.player.CraftItemEventHandler;
import freesoccerhdx.survivalplus.events.player.EnchantItemEventHandler;
import freesoccerhdx.survivalplus.events.player.EntityToggleGlideEventHandler;
import freesoccerhdx.survivalplus.events.player.FoodLevelChangeEventHandler;
import freesoccerhdx.survivalplus.events.player.InventoryClickEventHandler;
import freesoccerhdx.survivalplus.events.player.InventoryCloseEventHandler;
import freesoccerhdx.survivalplus.events.player.InventoryInteractEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerBedEventsHandler;
import freesoccerhdx.survivalplus.events.player.PlayerCommandPreprocessEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerDeathEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerDropItemEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerFishEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerInteractEntityEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerInteractEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerJoinEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerMoveEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerPacketEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerPortalEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerQuitEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerStatisticIncrementEventHandler;
import freesoccerhdx.survivalplus.events.player.PlayerTeleportEventHandler;
import freesoccerhdx.survivalplus.events.player.PrepareAnvilEventHandler;
import freesoccerhdx.survivalplus.events.player.SignChangeEventHandler;
import freesoccerhdx.survivalplus.events.player.StructureGrowEventHandler;
import freesoccerhdx.survivalplus.npc.KlonEventHandler;
import freesoccerhdx.survivalplus.npc.NPCHandler;
import freesoccerhdx.survivalplus.turtle.TurtleManager;
import freesoccerhdx.survivalplus.warp.WarpManager;


public class main extends JavaPlugin implements Listener {
	
	public static boolean bypass = false;
	
	public static main m;
	public static LocaleQuery localquery = null;
	
	public long breakDelay = 1, decayDelay = 2;
	public final static Set<Block> scheduledBlocks = new HashSet<>();
	
	public static final BlockFace[] NEIGHBORS = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN};
	public static final BlockFace[] EXTENDNEIGHBORS = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST};
	
	public static Inventory newstuffInventar = null;
	public static Inventory newstuffInventar_enchanting = null;
	
	public static TurtleManager turtlemanager = null;
	
	public static List<String> customrecipes = new ArrayList<>();
	
	@Override
	public void onDisable(){
		
		NPCHandler.onDisable();
		
		if(turtlemanager != null) {
			turtlemanager.destory();
		}
		try {
			for(Player p : Bukkit.getOnlinePlayers()) {
				WayPointManager.despawnWayPoints(p);
			}
		}catch(Exception ex) {
		}
	}
	
	public static HashMap<Entity, HashMap<String, Object>> arrowdata = new HashMap<>();
	public static HashMap<String, Inventory> backpack = new HashMap<>();
	public static HashMap<String, LivingEntity> holdingentity = new HashMap<>();
	public static HashMap<String, BlockFace> blockface = new HashMap<>();
	public static HashMap<UUID, Long> festhalten = new HashMap<>();

	
	@Override
	public void onEnable() {
		m = this;
		localquery = new LocaleQuery();
		//turtlemanager = new TurtleManager();
		//TurtleManager.recover();
		
		Bukkit.getPluginManager().registerEvents(main.m, main.m);
		Bukkit.getPluginManager().registerEvents(new WarpManager(), main.m);
		
		Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new InventoryClickEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new ProjectileLaunchEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new EntityToggleGlideEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new BlockBreakEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new BlockPlaceEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerDropItemEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerBedEventsHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new InventoryCloseEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new InventoryInteractEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new EnchantItemEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerStatisticIncrementEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new EntityDamageEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerMoveEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new EntityShootBowEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new ProjectileHitEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new FoodLevelChangeEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerFishEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PrepareAnvilEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new EntitySpawnEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerTeleportEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new PlayerDeathEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new BlockDispenseEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new StructureGrowEventHandler(), m);
		
		Bukkit.getPluginManager().registerEvents(new EnderDragonBoostEventHandler(),m);
		Bukkit.getPluginManager().registerEvents(new PlayerPortalEventHandler(),m);
		
		Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatEventHandler(),m);
		Bukkit.getPluginManager().registerEvents(new EntityDeathEventHandler(),m);
		
		
		Bukkit.getPluginManager().registerEvents(new CraftItemEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new SignChangeEventHandler(), m);
		Bukkit.getPluginManager().registerEvents(new BlockPistonExtendEventHandler(), m);
		
		Bukkit.getPluginManager().registerEvents(new KlonEventHandler(), m);
		
		//
		
		
		//WarpManager.init();
		
		 
		
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 20L, 1L);
		createNewStuffInventar();
		addBackpackCrafting();
		AusbesserungsHandler();
		TickHandler();
		addNetherstarCrafting();
		addMagnetCrafting();
		addAppleCrafting();
		addMedusaCrafting();
		addRedstoneTimerCrafting();
		addNPCCrafting();
		//EnchantmentHandler.registerEnchantments();
		
		
		EntityHealthHandler.run();
		BuildersWand.run();
		RedstoneTimer.run();
		
		
		Bukkit.getScheduler().runTaskLater(main.m, ()->NPCHandler.onEnable(), 10);
		Bukkit.getScheduler().runTaskLater(main.m, ()->NPCHandler.run(), 10);
		
		
		
	
	//	Bukkit.getWorlds().get(0).getPopulators().addAll(new DungeonChunkGenerator().getDefaultPopulators(null));
		if(false) {
		 Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {

			@Override
			public void run() {
				
				WorldCreator wc = new WorldCreator("DungeonWorld");
				wc.environment(Environment.NORMAL);
				wc.type(WorldType.AMPLIFIED);
				wc.generator(new DungeonChunkGenerator());
				wc.createWorld();
			
			}
			 
		 }, 20);
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			Bukkit.getScheduler().runTaskLater(main.m, ()->PlayerJoinEventHandler.removePlayer(p), 10);
			Bukkit.getScheduler().runTaskLater(main.m, ()->PlayerJoinEventHandler.injectPlayer(p), 20);
		}
		
	/*
		if(true) {
			WorldCreator wc = new WorldCreator("testwelt2");
			wc.environment(Environment.NORMAL);
			wc.type(WorldType.CUSTOMIZED);
			wc.generator(new DungeonChunkGenerator());
			wc.createWorld();
		}
		if(true) {
			WorldCreator wc = new WorldCreator("3");
			wc.environment(Environment.NORMAL);
			wc.type(WorldType.CUSTOMIZED);
			wc.generator(new DungeonChunkGenerator());
			wc.createWorld();
		}
		
		if(true) {
			WorldCreator wc = new WorldCreator("world");
			wc.environment(Environment.NORMAL);
			wc.type(WorldType.CUSTOMIZED);
			wc.generator(new DungeonChunkGenerator());
			wc.createWorld();
		} 
		*/       	
	
		//new Location(world, x, y, z).getWorld().generateTree(arg0, arg1, BlockChange)
	}
	
	
	
	/*
	public static int k = 0;
	@EventHandler
	public void onDmgBlock(BlockDamageEvent e) {
		//e.setInstaBreak(true);
		k++;
		e.getPlayer().sendMessage(">" + k);
		
	
		
	}
	*/
	
	@EventHandler
	public void WorldLoad(WorldLoadEvent e) { //last
		if(e.getWorld().getName().equalsIgnoreCase("DungeonWorld")) {
			
		}
	}
	@EventHandler
	public void WorldInit(WorldInitEvent e) { //first
		if(e.getWorld().getName().equalsIgnoreCase("DungeonWorld")) {
			e.getWorld().getPopulators().add(new DungeonPopulator());
		}
	}
	
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
	    return new DungeonChunkGenerator();
	}
	
	private static void addNPCCrafting() {
		ItemStack deepstorageunit = Methoden.item(Material.VILLAGER_SPAWN_EGG, 1,0, "§6Spawn NPCPlayer", new String[] {});
		
		
		NBTItem nbitem = new NBTItem(deepstorageunit);
		
		nbitem.setBoolean("npcegg", true);
		deepstorageunit = nbitem.getItem();
		
		ShapedRecipe slr = new ShapedRecipe(NamespacedKey.minecraft("npcegg"), deepstorageunit).shape("xax", "ana", "xax").
				setIngredient('n', Material.NETHER_STAR).
				setIngredient('a', Material.WITHER_SKELETON_SKULL);
		Bukkit.getServer().addRecipe(slr);
		
		
		customrecipes.add("npcegg");
		
	}
	private static void addRedstoneTimerCrafting() {
		ItemStack deepstorageunit = Methoden.item(Material.PLAYER_HEAD, 1,0, "§6Redstone Timer", new String[] {});
	
		NBTItem nbitem = new NBTItem(deepstorageunit);
		nbitem.mergeCompound(new NBTContainer(""
				+ "{SkullOwner:"
					+ "{Id:\"ed198559-89c3-4882-9600-2a472a9b9875\","
					+ "Properties:"
						+ "{textures:"
							+ "[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiZjMwMjlkYWVkNDExYmYyYjAwMzI5MDc4MGVlOGEwYmNmNTllZjZkM2EyZTM4YWMzMjMwNmFhNWI0M2M1YyJ9fX0=\"}]}}}"));
		
		nbitem.setBoolean("redstonetimer", true);
		deepstorageunit = nbitem.getItem();

		ShapelessRecipe slr = new ShapelessRecipe(NamespacedKey.minecraft("redstonetimer"), deepstorageunit)
				.addIngredient(Material.CLOCK).
				addIngredient(Material.REPEATER).
				addIngredient(Material.REPEATER).
				addIngredient(Material.REPEATER).
				addIngredient(Material.REPEATER).
				addIngredient(Material.REPEATER).
				addIngredient(Material.REPEATER).
				addIngredient(Material.REPEATER).
				addIngredient(Material.REPEATER);
		Bukkit.getServer().addRecipe(slr);
		
		customrecipes.add("redstonetimer");
		
		
	}
	private static void addMedusaCrafting() {
		ItemStack deepstorageunit = Methoden.item(Material.PLAYER_HEAD, 1,0, "§6Medusa", new String[] {});
		
		
		NBTItem nbitem = new NBTItem(deepstorageunit);
		nbitem.mergeCompound(new NBTContainer("{SkullOwner:{Id:\"460628a0-1a25-42a3-a7a2-d87bfbc2d5c6\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk4ZjViYjdjMzYxMjA1NDY5YzliZDhlNzE1NDVhYjE4N2MwOGZjNDQ1ZTgzMzc4ZTc0MDQ2NDhlN2ZjMTM0ZSJ9fX0=\"}]}}}"));
		nbitem.setBoolean("medusa", true);
		deepstorageunit = nbitem.getItem();

	//	ShapelessRecipe slr = new ShapelessRecipe(NamespacedKey.minecraft("medusa"), deepstorageunit)
		//		.addIngredient(Material.DIAMOND_HELMET).addIngredient(Material.NETHER_STAR).addIngredient(Material.NETHER_STAR).addIngredient(Material.NETHER_STAR).addIngredient(Material.NETHER_STAR);
		//Bukkit.getServer().addRecipe(slr);
		
		ShapedRecipe slr = new ShapedRecipe(NamespacedKey.minecraft("medusa"), deepstorageunit).shape("bab", "aia", "bab").
				setIngredient('a', Material.NETHER_STAR).
				setIngredient('i', Material.DIAMOND_HELMET);	
		Bukkit.getServer().addRecipe(slr);
		
		
		customrecipes.add("medusa");
		
	}
	private static void addAppleCrafting() {
		ItemStack deepstorageunit = Methoden.item(Material.PLAYER_HEAD, 1,0, "§7Apple", new String[] {});
		
		
		NBTItem nbitem = new NBTItem(deepstorageunit);
		nbitem.mergeCompound(new NBTContainer("{SkullOwner:{Id:\"69bc868f-c804-4b98-9c11-86766e33331c\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTIxMTI3ZDIyNjc1YzVjNzY5YTBjMDhiNTcyMDE4ZDI5OWM4MzA0NmY4ZTFmOTQzZWQ5ZDUxYmUwM2IwNyJ9fX0=\"}]}}}"));
		
		deepstorageunit = nbitem.getItem();

		ShapelessRecipe slr = new ShapelessRecipe(NamespacedKey.minecraft("applehead"), deepstorageunit).addIngredient(Material.APPLE);
		
		
		Bukkit.getServer().addRecipe(slr);
		
		
		
		customrecipes.add("applehead");
	}
	
	private static void addMagnetCrafting() {
		ItemStack deepstorageunit = Methoden.item(Material.PLAYER_HEAD, 1,0, "§6Item Magnet", new String[] {});
		deepstorageunit.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		
		NBTItem nbitem = new NBTItem(deepstorageunit);
		nbitem.mergeCompound(new NBTContainer("{SkullOwner:{Id:\"a55b4c2b-6373-48c6-a299-974cebc87013\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ==\"}]}}}"));
		
		//Bukkit.getScheduler().runTaskLater(main.m, ()->Bukkit.broadcastMessage(nbitem.toString()), 40);
		nbitem.setBoolean("magnet", true);
		
		deepstorageunit = nbitem.getItem();
		ItemMeta metas = deepstorageunit.getItemMeta();
		metas.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		deepstorageunit.setItemMeta(metas);
	
		
		ShapedRecipe dsu = new ShapedRecipe(NamespacedKey.minecraft("magnet"), deepstorageunit).shape("bab", "aia", "bab")
				.setIngredient('b', Material.DIAMOND_BLOCK).
				setIngredient('a', Material.EMERALD_BLOCK).
				setIngredient('i', Material.NETHER_STAR);	
		Bukkit.getServer().addRecipe(dsu);
		
		
		customrecipes.add("magnet");
		
	}
	private static void addNetherstarCrafting() {

		ItemStack nethersword = Methoden.item(Material.DIAMOND_SWORD, 1, 0, "§6§l§k#§f§4§lNethersword§6§l§k#", new String[]{"§3Die unendliche Power","§3eines Netherstar."});
		ItemMeta meta = nethersword.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setUnbreakable(true);
		meta.addEnchant(Enchantment.DURABILITY, 10, true);
		nethersword.setItemMeta(meta);
		ShapelessRecipe nsr = new ShapelessRecipe(NamespacedKey.minecraft("nethersword"), nethersword).addIngredient(Material.DIAMOND_SWORD).addIngredient(Material.NETHER_STAR);
		Bukkit.getServer().addRecipe(nsr);
		customrecipes.add("nethersword");
		
		
		ItemStack mobfänger = Methoden.item(Material.LEAD, 1, 0, "§6§l§k#§f§4§lMobfänger§6§l§k#", new String[]{"§3Die unendliche Power","§3eines Netherstar."});
		ShapelessRecipe nnr = new ShapelessRecipe(NamespacedKey.minecraft("mobfaenger"), mobfänger).addIngredient(Material.LEAD).addIngredient(Material.NETHER_STAR);
		Bukkit.getServer().addRecipe(nnr);
		customrecipes.add("mobfaenger");
		
		
		ItemStack builderswand = Methoden.item(Material.BLAZE_ROD, 1, 0, "§6§l§oBuildersWand", new String[] {});
		builderswand.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		ShapelessRecipe bw = new ShapelessRecipe(NamespacedKey.minecraft("builderswand"), builderswand).addIngredient(Material.STICK).addIngredient(Material.NETHER_STAR);
		Bukkit.getServer().addRecipe(bw);
		customrecipes.add("builderswand");
		
		
		ItemStack deepstorageunit = Methoden.item(Material.PLAYER_HEAD, 1,0, "§6Deep Storage Backpack", new String[] {});
		deepstorageunit.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		
		NBTItem nbitem = new NBTItem(deepstorageunit);
		nbitem.mergeCompound(new NBTContainer("{SkullOwner:{Id:\"bfb95357-157e-4688-b466-f3dda7261e71\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEyM2Q4YmUwYTYyN2I1MTY3ZGJkOTk2MjkwNDIxY2RkNzk3M2ZkMDJmYzE4OGQ4MTgzYjFjMDYyZWNkZTcxNCJ9fX0=\"}]}}}"));
		
		//Bukkit.getScheduler().runTaskLater(main.m, ()->Bukkit.broadcastMessage(nbitem.toString()), 40);
		nbitem.setBoolean("dsu", true);
		nbitem.setString("material0", "null");
		nbitem.setInteger("counter0", 0);
		nbitem.setString("material1", "null");
		nbitem.setInteger("counter1", 0);
		nbitem.setString("material2", "null");
		nbitem.setInteger("counter2", 0);
		nbitem.setString("material3", "null");
		nbitem.setInteger("counter3", 0);
		nbitem.setString("material4", "null");
		nbitem.setInteger("counter4", 0);
		
		deepstorageunit = nbitem.getItem();
		ItemMeta metas = deepstorageunit.getItemMeta();
		metas.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		deepstorageunit.setItemMeta(metas);
	
		
		ShapedRecipe dsu = new ShapedRecipe(NamespacedKey.minecraft("deepstorageunit"), deepstorageunit).shape("bab", "aia", "bab")
				.setIngredient('b', Material.ENDER_EYE).
				setIngredient('a', Material.LEATHER).
				setIngredient('i', Material.NETHER_STAR);	
		Bukkit.getServer().addRecipe(dsu);
		customrecipes.add("deepstorageunit");
	}
	
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Methoden.onBlockRemove(event.getBlock(), breakDelay);
	}
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
	    Methoden.onBlockRemove(event.getBlock(), decayDelay);
	}

	
	private static HashMap<UUID,Double> medusaangle = new HashMap<>();
	
	private static void TickHandler() {
		Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					try {
						ItemStack helm = p.getInventory().getHelmet();
						if(helm != null) {
							if(helm.getType() == Material.PLAYER_HEAD) {
								NBTItem nbitem = new NBTItem(helm);
								if(nbitem.hasKey("medusa")) {
									if(!medusaangle.containsKey(p.getUniqueId())) {
										medusaangle.put(p.getUniqueId(), 0.0);
									}
									medusaangle.put(p.getUniqueId(), medusaangle.get(p.getUniqueId())+2.0/20);
									if(medusaangle.get(p.getUniqueId()) > 360) {
										medusaangle.put(p.getUniqueId(), 2.0/20);
									}
									double vx = Math.cos(medusaangle.get(p.getUniqueId()));
									double vz = Math.sin(medusaangle.get(p.getUniqueId()));
									Vector dv = new Vector(vx,0.0,vz);
									Location partloc = p.getEyeLocation().add(dv.multiply(5));
									p.getWorld().spawnParticle(Particle.FLAME, partloc, 0);
									
									for(Entity tar : p.getWorld().getNearbyEntities(partloc, 2.5, 2.5, 2.5)) {
										if(tar instanceof LivingEntity && tar instanceof Monster) {
											if(tar != p && p.hasLineOfSight(tar)) {
												
												((LivingEntity) tar).damage(20, tar);
												//p.attack(tar);
												
											}
										}
									}
									
								}
							}
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					try {
						ItemStack offhand = p.getInventory().getItemInOffHand();
						if(offhand != null) {
							if(offhand.getType() == Material.PLAYER_HEAD) {
								NBTItem nbitem = new NBTItem(offhand);
								if(nbitem.hasKey("magnet")) {
									for(Entity items : p.getWorld().getNearbyEntities(p.getLocation(), 10, 10, 10)) {
										if(items.getType() == EntityType.DROPPED_ITEM) {
											if(items.getLocation().distance(p.getLocation().add(0, 0.5, 0)) > 1.0){
												Vector v = items.getLocation().toVector().subtract(p.getLocation().add(0, 0.5, 0).toVector()).normalize();
												v = v.setY(v.getY()*2);
												items.setVelocity(v.multiply(-0.3));
											}
										}
									}
								}
							}
						}
								
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					try {
						if(holdingentity.containsKey(p.getName())) {
							LivingEntity ent = holdingentity.get(p.getName());
							boolean hasitem = false;
							if(p.getInventory().getItemInMainHand() != null) {
								ItemStack is = p.getInventory().getItemInMainHand();
								if(is.getType() == Material.LEAD) {
									if(is.getItemMeta() != null) {
										if(is.getItemMeta().getDisplayName() != null) {
											if(is.getItemMeta().getDisplayName().equals("§6§l§k#§f§4§lMobfänger§6§l§k#")) {
												hasitem = true;
											}
										}
									}
								}
							}
							if(hasitem) {
								Location loc = p.getLocation().add(0.0D, 1.0D, 0.0D).add(p.getLocation().getDirection().multiply(p.isSneaking() ? 1.2D : 3.0D));
				                loc.setYaw(ent.getLocation().getYaw());
				                loc.setPitch(ent.getLocation().getPitch());
				                
				                ent.teleport(loc);
				                
							}else {
								//ent.setAI(true);
								ent.setGravity(true);
								holdingentity.remove(p.getName());
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					
					int leben = (int) (Methoden.getArmorEnchLevel(p, "Leben")*0.5+20);
					
					if(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() != leben) {
						p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(leben);
					}
					
					int armorleichtigkeit = Methoden.getArmorEnchLevel(p, "Leichtigkeit");
					p.setWalkSpeed((float) (0.2 + 0.005*armorleichtigkeit));
					
					
					
					
					if(p.getInventory().getChestplate() != null) {
						ItemStack is = p.getInventory().getChestplate();
						int grav = Methoden.getLoreEnchLevel(is, "Gleiter");
						
						if(grav > 0 && EnchantmentTarget.BREAKABLE.includes(is) && !p.isOnGround() && p.getFallDistance() >= 1.5) {		
							if(!p.isSneaking()) {
								//p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, grav-1, false, false));
								p.setGliding(true);
								p.setFallDistance((float) 1.5);
							}else {
								p.setGliding(false);
								if(p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
									p.removePotionEffect(PotionEffectType.SLOW_FALLING);
								}
							}
						}
					}
				}
				
				//zielsuchend-Handler
				for(Entity proj : arrowdata.keySet()) {
					HashMap<String, Object> data = arrowdata.get(proj); 
					
					if(data.containsKey("Zielsuchend")) {
						if(data.get("Zielsuchend") != null) {
							Zielsuchend zs = (Zielsuchend) data.get("Zielsuchend");
							if(zs.stufe > 0) {
								Entity target = zs.getZielsuchendEntity();
								if(target != null && target.isValid()) {
						
									Entity ziel = zs.target;
									int level = zs.stufe;
									
									
									
									Vector newdir = ((LivingEntity) ziel).getEyeLocation().toVector().subtract(proj.getLocation().toVector()).normalize();
									Vector olddir = proj.getVelocity().clone();
									
									double faktor = 0.04; //semi gut aka. jeden tick 4% kurskorektur
									faktor += level*0.005; //level 10 -> + 0.05 
									faktor += ((LivingEntity) ziel).getEyeLocation().distance(proj.getLocation())/2000.0;  //50 Blöcke entfernung -> + 0.025 
									
									Vector result = newdir.clone().subtract(olddir.clone()).normalize().multiply(faktor);
									
									Vector newzieldir = olddir.clone().add(result.clone());
									//proj.teleport(proj.getLocation().setDirection(newzieldir));
									proj.setVelocity(newzieldir);
									
									
									if(target.getLocation().distance(proj.getLocation()) < newzieldir.length()) {
										zs.stufe = 0;
									}
									
									//if(newdir.angle(olddir))
									
									
									
									
									
								}
							}
						}
					}
					
					
				}
				
			}
		}, 10, 1);
	}
	
	
	private static void AusbesserungsHandler() {
		Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					
					for(ItemStack is : p.getInventory().getContents()) {
						if(is != null && is.getItemMeta() != null) {
							if(is.getItemMeta() instanceof Damageable) {
								int ausbesserung = Methoden.getLoreEnchLevel(is, "Ausbesserung");
								if(ausbesserung > 0 && EnchantmentTarget.BREAKABLE.includes(is) && Methoden.chance(4*ausbesserung)) {
									ItemMeta meta = is.getItemMeta();
									if(((Damageable) meta).hasDamage()){	
										((Damageable) meta).setDamage(Math.max(((Damageable) meta).getDamage()-5, 0));
										is.setItemMeta(meta);
									}
								}
							}
						}
					}
				}
			}
		}, 20, 20*10);
	}
	
	
	private static void addBackpackCrafting() {
		NBTItem nbti;
		
		ItemStack bb9 = Methoden.item(Material.SHULKER_BOX, 1, 0, "§a§lSehr kleiner Rucksack", new String[]{"","§7§lSlots: 9"});
		nbti = new NBTItem(bb9);
		nbti.setString("invcontent", Methoden.toBase64(Bukkit.createInventory(null, 9)));
		bb9 = nbti.getItem();		
		ShapedRecipe bb9r = new ShapedRecipe(NamespacedKey.minecraft("bb9"), bb9).shape("bbb", "bib", "bbb").setIngredient('b', Material.CHEST).
		setIngredient('i', Material.COBBLESTONE);
		Bukkit.getServer().addRecipe(bb9r);

		ItemStack bb18 = Methoden.item(Material.SHULKER_BOX, 1, 0, "§a§lKleiner Rucksack", new String[]{"","§7§lSlots: 18"});
		nbti = new NBTItem(bb18);
		nbti.setString("invcontent", Methoden.toBase64(Bukkit.createInventory(null, 18)));
		bb18 = nbti.getItem();
		
		ShapedRecipe bb18r = new ShapedRecipe(NamespacedKey.minecraft("bb18"),bb18).shape("bbb", "bib", "bbb").setIngredient('i', new RecipeChoice.ExactChoice(bb9)).
		setIngredient('b', Material.IRON_INGOT);
		Bukkit.getServer().addRecipe(bb18r);

		ItemStack bb27 = Methoden.item(Material.SHULKER_BOX, 1, 0, "§a§lMittlerer Rucksack", new String[]{"","§7§lSlots: 27"});
		nbti = new NBTItem(bb27);
		nbti.setString("invcontent", Methoden.toBase64(Bukkit.createInventory(null, 27)));
		bb27 = nbti.getItem();
		ShapedRecipe bb27r = new ShapedRecipe(NamespacedKey.minecraft("bb27"), bb27).shape("bbb", "bib", "bbb").setIngredient('i', new RecipeChoice.ExactChoice(bb18)).
		setIngredient('b', Material.GOLD_INGOT);
		Bukkit.getServer().addRecipe(bb27r);
		
		
		ItemStack bb36 = Methoden.item(Material.SHULKER_BOX, 1, 0, "§a§lGroßer Rucksack", new String[]{"","§7§lSlots: 36"});
		nbti = new NBTItem(bb36);
		nbti.setString("invcontent", Methoden.toBase64(Bukkit.createInventory(null, 36)));
		bb36 = nbti.getItem();
		ShapedRecipe bb36r = new ShapedRecipe(NamespacedKey.minecraft("bb36"),bb36).shape("bbb", "bib", "bbb").setIngredient('i', new RecipeChoice.ExactChoice(bb27)).
		setIngredient('b', Material.DIAMOND);
		Bukkit.getServer().addRecipe(bb36r);
		
		
		ItemStack bb45 = Methoden.item(Material.SHULKER_BOX, 1, 0, "§a§lGrößerer Rucksack", new String[]{"","§7§lSlots: 45"});
		nbti = new NBTItem(bb45);
		nbti.setString("invcontent", Methoden.toBase64(Bukkit.createInventory(null, 45)));
		bb45 = nbti.getItem();
		ShapedRecipe bb45r = new ShapedRecipe(NamespacedKey.minecraft("bb45"), bb45).shape("bbb", "bib", "bbb").setIngredient('i', new RecipeChoice.ExactChoice(bb36)).
		setIngredient('b', Material.EMERALD);
		Bukkit.getServer().addRecipe(bb45r);
		
		
		ItemStack bb54 = Methoden.item(Material.SHULKER_BOX, 1, 0, "§a§lSehr großer Rucksack", new String[]{"","§7§lSlots: 54"});
		nbti = new NBTItem(bb54);
		nbti.setString("invcontent", Methoden.toBase64(Bukkit.createInventory(null, 54)));
		bb54 = nbti.getItem();
		ShapedRecipe bb54r = new ShapedRecipe(NamespacedKey.minecraft("bb54"), bb54).shape("bbb", "bib", "bbb").setIngredient('i', new RecipeChoice.ExactChoice(bb45)).
		setIngredient('b', Material.EMERALD);
		Bukkit.getServer().addRecipe(bb54r);
		
	}
	
	
	
	
	private void createNewStuffInventar() {
		newstuffInventar = Bukkit.createInventory(null, 18, "§aVeränderungen");
		Inventory nsi = newstuffInventar;
		
		nsi.addItem(Methoden.item(Material.DRAGON_HEAD, 1, 0, "§6§lEnderdrachen-Veränderungen", 
				new String[]{"§b- Pro getöteten Enderdrachen: §a+5% Leben",
							"§b- §c-30% §berlittener Schaden",
							"§b- Zufällige Laserstrahlen auf Spieler",
							"§b- Beim Tod wird ein zufälliges Item gespawnt"
						}));
		nsi.addItem(Methoden.item(Material.SMITHING_TABLE, 1, 0, "§6§lSchmiedetisch-Veränderungen", 
				new String[]{"§b- Klicke den Tisch mit einem Verzauberten Item an,",
							"§b  um Verzauberungen wieder zu entfernen."
						}));
		
		nsi.addItem(Methoden.item(Material.LEAD, 1, 0, "§6§lLeinen-Veränderungen", 
				new String[]{"§b- Crafte eine Leine und ein NetherStar","§b  für eine bessere Leine zusammen."
						}));
		
		nsi.addItem(Methoden.item(Material.BLAZE_ROD, 1, 0, "§6§lBuildersWand-Veränderungen", 
				new String[]{"§b- Crafte ein Stick und ein NetherStar","§b  für ein BuildersWand zusammen."
						}));
		
		nsi.addItem(Methoden.item(Material.SHULKER_BOX, 1, 0, "§6§lRucksack-Veränderungen", 
				new String[]{"§b- Crafting & Upgrading",
						"§b 9 Slots > 1 Cobblestone mit 8 Kisten",
						"§b 18 Slots > 8 Eisen außenrum",
						"§b 27 Slots > 8 Gold außenrum",
						"§b 36 Slots > 8 Diamanten außenrum",
						"§b 45 Slots > 8 Emerald außenrum",
						"§b 54 Slots > 8 Emerald außenrum",
						"§7Upgrade: immer vorherigen Rucksack übernehmen"
						}));
		
		
		nsi.addItem(Methoden.item(Material.EXPERIENCE_BOTTLE, 1, 0, "§6§lXP-Veränderungen", 
				new String[]{"§b- Mit /xptransfer werden Xp in Flaschen gebannt",
							"§b- Mit /xpfluider werden Flaschen in Xp verwandelt"
						}));
		
		nsi.addItem(Methoden.item(Material.EXPERIENCE_BOTTLE, 1, 0, "§6§lXP-Veränderungen", 
				new String[]{"§b- Mit /repaircost werden Reperatur-","§b  Kosten des Items in der Hand", "§b  repariert",
							"§b- Mit /getrepaircost werden sie angezeigt"
						}));
		
		nsi.addItem(Methoden.item(Material.SPAWNER, 1, 0, "§6§lSpawner-Veränderungen", 
				new String[]{"§b- Mit Rechtsklick können Informationen über","§b  den Spawner angezeigt werden.",
							"§b- Mit einem Netherstar werden","§b  seine Stats verbessert."
						}));
		nsi.addItem(Methoden.item(Material.ZOMBIE_SPAWN_EGG, 1, 0, "§6§lMonster-Veränderungen", 
				new String[]{"§b- Zombies, Skelette, Pigmans & Pillager","§b  haben teilweise verbesserte Waffen^^"
						}));
		nsi.addItem(Methoden.item(Material.ENCHANTED_BOOK, 1, 0, "§6§lSilk Touch-Veränderungen", 
				new String[]{"§b- MobSpawner können abgebaut", "§b  werden und Droppen das Item."
						}));
		nsi.addItem(Methoden.item(Material.ENCHANTED_BOOK, 1, 0, "§6§lMending-Veränderungen", 
				new String[]{"§b- Kann jetzt beim Verzaubern erhalten werden"
						}));
		nsi.addItem(Methoden.item(Material.ENCHANTED_BOOK, 1, 0, "§6§lDornen-Veränderungen", 
				new String[]{"§b- Gegner erhält 2-fachen Dornenschaden"}));
		
		nsi.addItem(Methoden.item(Material.ENCHANTED_BOOK, 1, 0, "§6§lNeue Verzauberungen", 
				new String[]{"§e§l*Click*","§6§lNEU: ","§b- Auch Bücher werden verzaubert"}));
		
		
		newstuffInventar_enchanting = Bukkit.createInventory(null, 36, "§6§lNeue Verzauberungen");
		Inventory nsie = newstuffInventar_enchanting;
		
		nsie.addItem(Methoden.item(Material.ENCHANTED_BOOK, 1, 0, "§bAusbesserung", 
				new String[]{"§eFür: Rüstungen, Waffen & Werkzeuge",
						"§eRepariert alle 10s zu §a4% pro Level §edas Item",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Ausbesserung")
				}));
		
		nsie.addItem(Methoden.item(Material.FISHING_ROD, 1, 0, "§bDoppelfischen", 
				new String[]{"§eFür: Angeln",
						"§eExtra Fisch-Items: §a1 Item pro Level",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Doppelfischen")
				}));

		nsie.addItem(Methoden.item(Material.GOLDEN_PICKAXE, 1, 0, "§bAuto-Smelt", 
				new String[]{"§eFür: Spitzhacken",
						"§eSchmilzt zu §a20% pro Level §eden Block",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Auto-Smelt")
				}));

		nsie.addItem(Methoden.item(Material.IRON_AXE, 1, 0, "§bTimber", 
				new String[]{"§eFür: Äxte",
						"§eBaut zu §a10% pro Level §ealle","§eanliegenden Holzblöcke ab",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Timber")
				}));
		
		nsie.addItem(Methoden.item(Material.PISTON, 1, 0, "§bHeranziehen", 
				new String[]{"§eFür: Schwerter, Bögen & Armbrüste",
						"§eZieht den Gegner §a0,3 Blöcke pro Level §eheran",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Heranziehen")
				}));
		nsie.addItem(Methoden.item(Material.STICKY_PISTON, 1, 0, "§bWegschleudern", 
				new String[]{"§eFür: Schwerter, Bögen & Armbrüste",
						"§eStößt den Gegner §a0,3 Blöcke pro Level §eweg",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Wegschleudern")
				}));
		nsie.addItem(Methoden.item(Material.DIAMOND_SWORD, 1, 0, "§bRüstungsdurchdringung", 
				new String[]{"§eFür: Schwerter",
						"§eFügt Gegnern §a4% des Schaden pro Level §edirekt zu",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Rüstungsdurchdringung")
				}));
		nsie.addItem(Methoden.item(Material.POTION, 1, 0, "§bLebensraub", 
				new String[]{"§eFür: Schwerter",
						"§eHeilt §a0,25% pro Level §edes Schadens",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Lebensraub")
				}));
		nsie.addItem(Methoden.item(Material.WOODEN_SWORD, 1, 0, "§bVerlangsamung", 
				new String[]{"§eFür: Schwerter",
						"§eVerlangsamt den Gegner für §a0,5s pro Level",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Verlangsamung")
				}));
		nsie.addItem(Methoden.item(Material.GOLDEN_SWORD, 1, 0, "§bWither", 
				new String[]{"§eFür: Schwerter",
						"§eFügt den Gegner für §a0,5s pro Level","§eden Wither-Effekt zu",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Wither")
				}));
		nsie.addItem(Methoden.item(Material.IRON_SWORD, 1, 0, "§bVergiftung", 
				new String[]{"§eFür: Schwerter",
						"§eFügt den Gegner für §a0,5s pro Level","§eden Vergiftungs-Effekt zu",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Vergiftung")
				}));
		nsie.addItem(Methoden.item(Material.TNT, 1, 0, "§bExplosion", 
				new String[]{"§eFür: Bogen & Armbrüste",
						"§eProjektile erzeugen eine Explosion","§eder Stärke §a0,6 pro Level", "§7(3 Block radius)",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Explosion")
				}));
		nsie.addItem(Methoden.item(Material.TRIDENT, 1, 0, "§bDonnerblitz", 
				new String[]{"§eFür: Bogen & Armbrüste",
						"§eProjektile erzeugen einen Blitz","§eund machen §a0,8 Leben pro Level §eSchaden", "§7(2 Block radius)",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Donnerblitz")
				}));
		nsie.addItem(Methoden.item(Material.BOW, 1, 0, "§bHärte", 
				new String[]{"§eFür: Bogen & Armbrüste",
						"§eProjektile fliegen §a20% pro Level schneller","§eund machen §a0,4 Leben pro Level §eSchaden",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Härte")
				}));
		nsie.addItem(Methoden.item(Material.LINGERING_POTION, 1, 0, "§bSchwebe", 
				new String[]{"§eFür: Bogen & Armbrüste",
						"§eFügt den Gegner für §a0,25s pro Level","§eden Schwebe-Effekt zu",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Schwebe")
				}));
		nsie.addItem(Methoden.item(Material.SNOWBALL, 1, 0, "§bAbprallen", 
				new String[]{"§eFür: Bogen & Armbrüste",
						"§eProjektile prallen §a1 mal pro Level an Blöcken ab",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Abprallen")
				}));
		
		nsie.addItem(Methoden.item(Material.GOLDEN_CHESTPLATE, 1, 0, "§bLeben", 
				new String[]{"§eFür: Rüstungen",
						"§eDu erhälst §a+0,5 Leben pro Level §emehr",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Leben")
				}));
		nsie.addItem(Methoden.item(Material.GUARDIAN_SPAWN_EGG, 1, 0, "§bDornenschutz", 
				new String[]{"§eFür: Rüstungen",
						"§eDornen machen §a-5% pro Level §eSchaden",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Dornenschutz")
				}));
		nsie.addItem(Methoden.item(Material.SUGAR, 1, 0, "§bLeichtigkeit", 
				new String[]{"§eFür: Rüstungen",
						"§eDeine Geschwindigkeit wird","§eum §a+5% pro Level §eerhöht",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Leichtigkeit")
				}));
		nsie.addItem(Methoden.item(Material.BREAD, 1, 0, "§bSättigung", 
				new String[]{"§eFür: Helme",
						"§eDu verlierst zu §a+9% pro Level §ekein Hunger",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Sättigung")
				}));
		nsie.addItem(Methoden.item(Material.ELYTRA, 1, 0, "§bGleiter", 
				new String[]{"§eFür: Brustplatte",
						"§eDeine Brustplatte wird zur","§eautomatischen Elytra",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Gleiter")
				}));
		nsie.addItem(Methoden.item(Material.CHAINMAIL_LEGGINGS, 1, 0, "§bSchwimmboost", 
				new String[]{"§eFür: Hosen",
						"§eDu bist §a+10% pro Level §eschneller beim schwimmen",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Schwimmboost")
				}));
		nsie.addItem(Methoden.item(Material.RABBIT_HIDE, 1, 0, "§bSprungkraft", 
				new String[]{"§eFür: Schuhe",
						"§eDu springst §a+20% pro Level §ehöher",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Sprungkraft")
				}));
		nsie.addItem(Methoden.item(Material.ENDER_EYE, 1, 0, "§bZielsuchend", 
				new String[]{"§eFür: Bogen & Armbrüste",
						"§eProjektile suchen sich ein Ziel und","§efliegen §apro Level §emehr in deren Richtung",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Zielsuchend")
				}));
		nsie.addItem(Methoden.item(Material.COBBLESTONE, 64, 0, "§bVeinminer", 
				new String[]{"§eFür: Spitzhacken",
						"§eBaut pro Essensbalken ein Block ab", "§a-10% Hunger pro Level §7(Max. 99%)",
						"§7(Baut ohne Sneaken nur Blöcke ","§7auf Spielerhöhe und drüber ab)",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Veinminer")
				}));
		nsie.addItem(Methoden.item(Material.COBWEB, 1, 0, "§bFesthalten", 
				new String[]{"§eFür: Schwerter, Bögen & Armbrüste",
						"§eHält den Gegner für §a0.08s pro Level §efest", "§c(Gilt nur für Spieler)",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Festhalten")
				}));
		nsie.addItem(Methoden.item(Material.TNT, 1, 0, "§bHammer", 
				new String[]{"§eFür: Spitzhacken",
						"§eBaut wie ein Tinker's-Hamer ab",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Hammer")
				}));
		nsie.addItem(Methoden.item(Material.BLAZE_SPAWN_EGG, 1, 0, "§bRiesenschlag", 
				new String[]{"§eFür: Waffen",
						"§eBei voller Aufladung:","§eEntitys mit mehr als","§e25 Herzen kriegen §a0,5%","§apro Level §edes maximalen","§eLeben Schaden",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Riesenschlag")
				}));
		nsie.addItem(Methoden.item(Material.DIAMOND_SWORD, 1, 0, "§bErstschlag", 
				new String[]{"§eFür: Waffen",
						"§eMacht §a+10% pro Level", "§emehr Schaden beim ersten Treffer",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Erstschlag")
				}));
		nsie.addItem(Methoden.item(Material.DIAMOND_SWORD, 1, 0, "§bSturmschlag", 
				new String[]{"§eFür: Waffen",
						"§eGegner sind §a50ms pro Level", "§eweniger Lang unangreifbar",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Sturmschlag")
				}));
		nsie.addItem(Methoden.item(Material.DIAMOND_SWORD, 1, 0, "§bElytra-Boost", 
				new String[]{"§eFür: Elytra",
						"§eDu fliegst §a0,20 mal pro Level", "§eschneller mit der Elytra",
						"§eMax. Level: "+EnchantLimits.getEnchantLimit("Elytra-Boost")
				}));
		
	}
	
	
}











































