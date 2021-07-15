package freesoccerhdx.survivalplus.events.player;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import freesoccerhdx.survivalplus.haupt.BuildersWand;
import freesoccerhdx.survivalplus.haupt.DungeonPortal;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.WayPointManager;
import freesoccerhdx.survivalplus.haupt.main;

public class PlayerMoveEventHandler implements Listener {
	
	@EventHandler
	public void playermove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		
		//p.sendMessage(from.distance(to)+"§b -> " + p.getVelocity());
		
		if(to.getBlock().getType() == Material.WATER) {
			if(to.getBlock().getRelative(0, -1, 0).getType() == Material.BLACK_GLAZED_TERRACOTTA) {
				
				if(DungeonPortal.isDungeonCreatePortal(to.getBlock())) {
					to.getBlock().setType(Material.END_PORTAL);
					for(BlockFace bf : main.NEIGHBORS) {
						if(bf != BlockFace.DOWN && bf != BlockFace.UP) {
							if(to.getBlock().getRelative(bf).getType() == Material.WATER) {
								to.getBlock().getRelative(bf).setType(Material.END_PORTAL);
							}
						}
					}
					for(BlockFace bf : main.EXTENDNEIGHBORS) {
						if(bf != BlockFace.DOWN && bf != BlockFace.UP) {
							if(to.getBlock().getRelative(bf).getType() == Material.WATER) {
								to.getBlock().getRelative(bf).setType(Material.END_PORTAL);
							}
						}
					}
				}
			}
		}
		
		if(main.festhalten.containsKey(p.getUniqueId())) {
			if(main.festhalten.get(p.getUniqueId())-System.currentTimeMillis() > 0) {
				if(!from.getBlock().equals(to.getBlock())) {
					e.setCancelled(true);
				}
			}
		}
		
		
		if(WayPointManager.hasWayPoints(p)) {
			if(from.getWorld() == to.getWorld()) {
				if(from.distance(to) > 128){
					WayPointManager.despawnWayPoints(p);
					WayPointManager.spawnWayPoints(p);
				}
			}
			
			WayPointManager.updateWayPoints(p);
			
		}
		
		if(p.isSwimming() && p.isSneaking()) {
			if(p.getInventory().getLeggings() != null) {
				int swimmbostlvl = Methoden.getLoreEnchLevel(p.getInventory().getLeggings(), "Schwimmboost");
				if(swimmbostlvl > 0) {
					p.setVelocity(p.getLocation().getDirection().multiply(1.0+swimmbostlvl/20));
				}
			}
		}
		
	}

}
