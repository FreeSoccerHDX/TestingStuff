package freesoccerhdx.survivalplus.warp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.main;
import freesoccerhdx.survivalplus.npc.NPCMethods;

public class WarpManager implements Listener{

	public static List<String> istp = new ArrayList<>();
	
	public static void init() {
		ItemStack warp = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE,1);
		ItemMeta meta = warp.getItemMeta();
		meta.setDisplayName("§6§lWarpplatte");
		List<String> lore = new ArrayList<>();
		lore.add("§6Im Amboss umbenennen um den Warpname zu setzen!");
		lore.add("§cJeder Name ist nur einmal erlaubt!");
		meta.setLore(lore);
		warp.setItemMeta(meta);
		
		ShapedRecipe bb9r = new ShapedRecipe(NamespacedKey.minecraft("warpplatte"), warp).shape("ewe", "ede", "ggg").setIngredient('e', Material.EMERALD).
				setIngredient('w', Material.WITHER_SKELETON_SKULL).setIngredient('d', Material.DIAMOND_BLOCK).
				setIngredient('g', Material.GOLD_INGOT);
		Bukkit.getServer().addRecipe(bb9r);
		
		
		/*
		 Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {
			@Override
			public void run() {
				List<String> warps = Methoden.getWarpList(); 
				if(warps.size() > 0) {
					for(String w : warps) {
						Location loc = Methoden.getWarp(w);
						if(loc.getBlock().getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
							Methoden.removeWarp(w);
							for(Entity ent : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
								if(ent instanceof ArmorStand) {
									ent.remove();
								}
							}
						}
					}
				}
				
			}
		}, 1, 10);
		 */
	}
	public static void teleport(Player p, Location loc, Block start, int ticks) {
	
		if(p.getLocation().getBlock().getLocation().distance(start.getLocation()) < 1.0) {
			if(p.getFoodLevel() > 0) {
				if(Methoden.chance(50.0)){
					FoodLevelChangeEvent foodevent = new FoodLevelChangeEvent(p, p.getFoodLevel()-1);
					Bukkit.getServer().getPluginManager().callEvent(foodevent);
					if(!foodevent.isCancelled()) {
						p.setFoodLevel(p.getFoodLevel()-1);
					}
				}
			}
			if(ticks <= 0) {
				istp.remove(p.getName());
				p.sendMessage("§aDu wirst nun teleportiert...");
				p.teleport(loc);
			}else {
				
				Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
					
					@Override
					public void run() {
						
						teleport(p, loc, start, ticks-5);
						
						
					}
				}, 5);
			}
		}else {
			p.sendMessage("§cDu hast die Teleportation abgebrochen!");
		}
	}
	
	@EventHandler
	public void set(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		ItemStack is = e.getItemInHand();
		if(is.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
			 if(is.getItemMeta().getDisplayName() != null && is.getItemMeta().getDisplayName().length() > 0) {
				 String name = is.getItemMeta().getDisplayName();
				 
				 if(is.getItemMeta().getLore() != null) {
					 if(is.getItemMeta().getLore().get(0) != null) {
						 if(is.getItemMeta().getLore().get(0).equals("§6Im Amboss umbenennen um den Warpname zu setzen!")) {
							 if(name.equalsIgnoreCase("§6§lWarpplatte")) {
								 e.setCancelled(true);
								 p.sendMessage("§cDu musst die Warpplatte erst umbenennen.");
							 }else {
								 if(Methoden.getWarpList().contains(name)){
									 p.sendMessage("§cDieser Warp exestiert bereits!");
									 e.setCancelled(true);
								 }else {
									 if(Methoden.addWarp(name, e.getBlock().getLocation().add(0.5, 0, 0.5))) {
										 p.sendMessage("§aDu hast einen neuen Warp erstellt!");
										 
										 	ArmorStand a1 = p.getWorld().spawn(e.getBlock().getLocation().add(0.5, 0, 0.5), ArmorStand.class);
											a1.setCustomNameVisible(true);
											a1.setCustomName("§6§lWarp: §a§l'"+name+"'");
											a1.setGravity(false);
											a1.setVisible(false);
											a1.setInvulnerable(true);
											a1.setAI(false);
											a1.setCanPickupItems(false);
											a1.setCollidable(false);
											a1.setMarker(false);
										 
									 }else {
										 p.sendMessage("§cEs ist ein Fehler aufgetreten.");
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		}
	}
	
	@EventHandler
	public void remove(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		
		List<String> warps = Methoden.getWarpList(); 
		if(warps.size() > 0) {
			for(String w : warps) {
				Location loc = Methoden.getWarp(w);
				if(loc.getWorld() == b.getLocation().getWorld()) {
					if(loc.getBlock().getLocation().distance(b.getLocation()) <= 0.8) {
						Methoden.removeWarp(w);
						p.sendMessage("§cDu hast den Warp '"+w+"' zerstört!");
						for(Entity ent : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
							if(ent instanceof ArmorStand) {
								ent.remove();
							}
						}
						return;
					}
				}
			}
		}
	}
	
	public static void openWarpMenu(Player p, Location loc1) {
		if(p.getOpenInventory() != null) {
			if(p.getOpenInventory().getTitle() != null) {
				if(p.getOpenInventory().getTitle().equals("§6§lWarpmenu")) {
					return;
				}
			}
		}
		Location check = loc1.getBlock().getLocation().add(0.5, 0, 0.5);
		
		boolean gefunden = false;
		String warpname = "";
		
		List<String> warps = Methoden.getWarpList(); 
		if(warps.size() > 0) {
			for(String w : warps) {
				Location loc = Methoden.getWarp(w);
				if(loc.getBlock().getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
					Methoden.removeWarp(w);
					for(Entity ent : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
						if(ent instanceof ArmorStand) {
							ent.remove();
						}
					}
				}else {
					if(loc != null) {
						if(loc.getWorld() == check.getWorld()) {
							if(loc.distance(check) <= 0.5) {
								gefunden = true;
								warpname = w;
								break;
							}
						}
					}
				}
			}
		}
		if(gefunden) {
			p.sendMessage("§aDu befindest dich am Warp '"+warpname+"'.");
			int size = 0;
			for(int i = 0; i <= warps.size(); i++) {
				size += 9;
				i += 9;
			}
			Inventory warpmenu = Bukkit.createInventory(p, size, "§6§lWarpmenu");
			for(String w : warps) {
				if(!w.equals(warpname)) {
					ItemStack is = new ItemStack(Material.PAPER);
					ItemMeta me = is.getItemMeta();
					me.setDisplayName("§6"+w);
					is.setItemMeta(me);
					warpmenu.addItem(is);
				}
			}
			p.openInventory(warpmenu);
		}
	}
	
	@EventHandler
	public void sneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		
		if(p.isSneaking()) {
			openWarpMenu(p,p.getLocation());
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		

		if(p.isSneaking()) {
			
			openWarpMenu(p,e.getTo());
		}
		
	}
	
}



















