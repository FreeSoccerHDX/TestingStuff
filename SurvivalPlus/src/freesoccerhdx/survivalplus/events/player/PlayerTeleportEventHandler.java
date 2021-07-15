package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import freesoccerhdx.survivalplus.haupt.WayPointManager;
import freesoccerhdx.survivalplus.haupt.main;

public class PlayerTeleportEventHandler implements Listener {

	@EventHandler
	public void onTp(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		
		Location from = e.getFrom();
		Location to = e.getTo();
		
		if(WayPointManager.hasWayPoints(p)) {
			if(from.getWorld() == to.getWorld()) {
				if(from.distance(to) < 128) return;
				
				WayPointManager.despawnWayPoints(p);
				Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {

					@Override
					public void run() {
						if(p.isOnline()) {
							WayPointManager.spawnWayPoints(p);
						}
					}
					
				}, 10);
				
			}
		}
			
			
		
	}
	
	
}
