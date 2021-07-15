package freesoccerhdx.survivalplus.turtle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import freesoccerhdx.survivalplus.haupt.main;

public class TurtleManager implements Listener {

	public static List<Turtle> turtels = new ArrayList<>();
	public static HashMap<Material, Droptable> blocklist = new HashMap<>();
	public static HashMap<Material, Double> blockchance = new HashMap<>();
	
	public TurtleManager() {
		Bukkit.getPluginManager().registerEvents(this, main.m);
		
		blocklist.put(Material.OBSIDIAN, new Droptable(Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN, 1, 1,false ));
		blocklist.put(Material.SAND, new Droptable(Material.SAND, Material.SAND, Material.SAND, 1, 1,false));
		blocklist.put(Material.DIRT, new Droptable(Material.DIRT, Material.DIRT, Material.DIRT, 1, 1,false));
		blocklist.put(Material.GRAVEL, new Droptable(Material.GRAVEL, Material.GRAVEL, Material.GRAVEL, 1, 1,false));
		blocklist.put(Material.CLAY, new Droptable(Material.CLAY_BALL, Material.BRICK, Material.CLAY, 1, 3,true));
		blocklist.put(Material.DIORITE, new Droptable(Material.DIORITE, Material.DIORITE, Material.DIORITE, 1, 1,false));
		blocklist.put(Material.ANDESITE, new Droptable(Material.ANDESITE, Material.ANDESITE, Material.ANDESITE, 1, 1,false));
		blocklist.put(Material.GRANITE, new Droptable(Material.GRANITE, Material.GRANITE, Material.GRANITE, 1, 1,false));
		blocklist.put(Material.STONE, new Droptable(Material.COBBLESTONE, Material.STONE, Material.STONE, 1, 1,false));
		
		blocklist.put(Material.IRON_BLOCK, new Droptable(Material.IRON_ORE, Material.IRON_INGOT, Material.IRON_ORE, 1, 1,true));
		blocklist.put(Material.GOLD_BLOCK,  new Droptable(Material.GOLD_ORE, Material.GOLD_INGOT, Material.GOLD_ORE, 1, 1,true));
		blocklist.put(Material.EMERALD_BLOCK,  new Droptable(Material.EMERALD, Material.EMERALD, Material.EMERALD_ORE, 1, 1,true));
		blocklist.put(Material.LAPIS_BLOCK,  new Droptable(Material.LAPIS_LAZULI, Material.LAPIS_LAZULI, Material.LAPIS_ORE, 2, 4,true));
		blocklist.put(Material.DIAMOND_BLOCK,  new Droptable(Material.DIAMOND, Material.DIAMOND, Material.DIAMOND_ORE, 1, 1,true));
		blocklist.put(Material.REDSTONE_BLOCK,  new Droptable(Material.REDSTONE, Material.REDSTONE, Material.REDSTONE_ORE, 4, 5,true));
		blocklist.put(Material.QUARTZ_BLOCK,  new Droptable(Material.QUARTZ, Material.QUARTZ, Material.NETHER_QUARTZ_ORE, 1, 1,true));
		blocklist.put(Material.COAL_BLOCK,  new Droptable(Material.COAL, Material.COAL, Material.COAL_ORE, 1, 1,true));
		
		blockchance.put(Material.OBSIDIAN, 5.0);
		blockchance.put(Material.SAND, 25.0);
		blockchance.put(Material.DIRT, 25.0);
		blockchance.put(Material.GRAVEL, 25.0);
		blockchance.put(Material.CLAY, 12.5);
		
		blockchance.put(Material.DIORITE, 25.0);
		blockchance.put(Material.ANDESITE, 25.0);
		blockchance.put(Material.GRANITE, 25.0);
		blockchance.put(Material.STONE, 50.0);
		
		blockchance.put(Material.IRON_BLOCK, 0.25);
		blockchance.put(Material.GOLD_BLOCK, 0.12);
		blockchance.put(Material.EMERALD_BLOCK, 0.05);
		blockchance.put(Material.LAPIS_BLOCK, 0.2);
		blockchance.put(Material.DIAMOND_BLOCK, 0.1);
		blockchance.put(Material.REDSTONE_BLOCK, 0.5);
		blockchance.put(Material.QUARTZ_BLOCK, 3.0);
		blockchance.put(Material.COAL_BLOCK, 0.8);
		
		
		
		Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {
			@Override
			public void run() {
				tick();
				
			}
			
		}, 5, 1);
		
		
	}
	public static void recover() {
		List<ArmorStand> mains = new ArrayList<>();
		Collection<ArmorStand> armorstands = Bukkit.getWorld("world").getEntitiesByClass(ArmorStand.class);
		for(ArmorStand stand : armorstands) {
			if(stand.getCustomName() != null) {
				if(stand.getCustomName().startsWith("§6§lQuarry §6von §b")) {
					mains.add(stand);
				}
			}
		}
		
		if(mains.size() > 0) {
			for(ArmorStand stand : mains) {
				Location chest = stand.getLocation().add(0,0.7,0);
				Location dropper = null;
				if(chest.clone().add(1, 0, 0).getBlock().getType() == Material.DROPPER) {
					if(!main.turtlemanager.isTurtle(chest.clone().add(1, 0, 0))) {
						dropper = chest.clone().add(1, 0, 0);
					}
				}
				if(chest.clone().add(0, 0, 1).getBlock().getType() == Material.DROPPER) {
					if(!main.turtlemanager.isTurtle(chest.clone().add(0, 0, 1))) {
						dropper = chest.clone().add(0, 0, 1);
					}
				}
				if(chest.clone().add(-1, 0, 0).getBlock().getType() == Material.DROPPER) {
					if(!main.turtlemanager.isTurtle(chest.clone().add(-1, 0, 0))) {
						dropper = chest.clone().add(-1, 0, 0);
					}
				}
				if(chest.clone().add(0, 0, -1).getBlock().getType() == Material.DROPPER) {
					if(!main.turtlemanager.isTurtle(chest.clone().add(0, 0, -1))) {
						dropper = chest.clone().add(0, 0, -1);
					}
				}
				
				if(dropper != null) {
					Turtle tur = new Turtle(stand.getCustomName().split(" ")[2].replace("§b", ""),dropper,System.currentTimeMillis());
					turtels.add(tur);
				}
				stand.remove();	
			}
		}
		System.out.println("SurvivalPlus-QuarryManager: "+mains.size()+" (" +turtels.size()+ ") Quarry's gefunden!");
	}
	
	public void destory() {
		for(Turtle t : turtels) {
			t.destroyRec();
		}
	}
	
	public void tick() {
		if(turtels.size() > 0) {
			try {
				for(Turtle t : turtels) {
					t.run();
				}
			}catch (Exception e) {
			}
		}
	}
	public boolean isTurtle(Location loc) {
		if(turtels.size() > 0) {
			for(Turtle t : turtels) {
				if(t.mainblock.getBlock() != null && loc.getBlock() != null) {
					if(t.mainblock.getBlock().equals(loc.getBlock())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public Turtle getTurtle(Location loc) {
		if(turtels.size() > 0) {
			for(Turtle t : turtels) {
				if(t.mainblock.getBlock().equals(loc.getBlock())) {
					return t;
				}
			}
		}
		return null;
	}
	
	public boolean checkBreakBlock(Block bl) {
		
		if(turtels.size() > 0) {
			for(Turtle t : turtels) {
				if(bl.getWorld() == t.mainblock.getWorld()) {
					Double dis = t.mainblock.distance(bl.getLocation());
					if(dis <= 1) {
						return true;
					}
				}
				/*
				if(bl.getLocation() == t.mainblock.getBlock().getLocation()) { return true; }
				if(bl.getLocation() == t.mainblock.getBlock().getRelative(BlockFace.UP).getLocation()) { return true; }
				if(bl.getLocation() == t.mainblock.getBlock().getRelative(BlockFace.DOWN).getLocation()) { return true; }
				if(bl.getLocation() == t.mainblock.getBlock().getRelative(BlockFace.NORTH).getLocation()) { return true; }
				if(bl.getLocation() == t.mainblock.getBlock().getRelative(BlockFace.SOUTH).getLocation()) { return true; }
				if(bl.getLocation() == t.mainblock.getBlock().getRelative(BlockFace.WEST).getLocation()) { return true; }
				if(bl.getLocation() == t.mainblock.getBlock().getRelative(BlockFace.EAST).getLocation()) { return true; }
				*/
			}
		}
		return false;
	}
	@EventHandler
	public void breakblock(BlockBreakEvent e) {
		boolean check = checkBreakBlock(e.getBlock());
		e.setCancelled(check);
		if(check) {
			e.getPlayer().sendMessage("§cDu kannst die Quarry nicht abbauen, wenn sie noch Aktiviert ist!");
		}
	}
	
	@EventHandler
	public void rightclick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getHand() != EquipmentSlot.HAND) {return;}
			Block b = e.getClickedBlock();
			if(b.getType() == Material.DROPPER) {
				Player p = e.getPlayer();
				if(p.isSneaking()) {
					
					b.getFace(b);
					
					if(b.getRelative(BlockFace.UP).getType() == Material.CHEST) {
						if(blocklist.containsKey(b.getRelative(BlockFace.DOWN).getType())) {
							int passt = 0;
							if(blocklist.containsKey(b.getRelative(BlockFace.NORTH).getType())) {
								passt++;
							}
							if(blocklist.containsKey(b.getRelative(BlockFace.SOUTH).getType())) {
								passt++;
							}
							if(blocklist.containsKey(b.getRelative(BlockFace.EAST).getType())) {
								passt++;
							}
							if(blocklist.containsKey(b.getRelative(BlockFace.WEST).getType())) {
								passt++;
							}
							if(passt == 3) {
								if(this.isTurtle(b.getLocation())) {
									Turtle t = main.turtlemanager.getTurtle(b.getLocation());
									
									if(System.currentTimeMillis()-t.create > 1000) {
										t.destroy();
										turtels.remove(t);
										p.sendMessage("§cDu hast die Quarry erfolgreich gelöscht!");
									}
									e.setCancelled(true);
								}else {
									Turtle tur = new Turtle(p.getName(),b.getLocation(),System.currentTimeMillis());
									turtels.add(tur);
									p.sendMessage("§aDu hast erfolgreich eine Quarry erstellt!");
									e.setCancelled(true);
									
								}
							}else {
								p.sendMessage("§cAn den Seiten der Quarry müssen 3 passende Blöcke sein!");
							}
						}else {
							p.sendMessage("§cDer Block unter der Quarry passt nicht.");
						}
					}else {
						p.sendMessage("§cAuf der Quarry muss eine Kiste sein.");
					}
					
				}
			}
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
