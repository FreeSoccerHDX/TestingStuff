package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import freesoccerhdx.survivalplus.haupt.main;

public class PlayerBedEventsHandler implements Listener {

	private static int sleepers = 0;
	
	@EventHandler
	public void  PlayerBedEnter(PlayerBedEnterEvent e) {
		
		if(e.getBedEnterResult() == BedEnterResult.OK) {
			Player p = e.getPlayer();
			sleepers ++;
			int wplayers = p.getWorld().getPlayers().size();
			int perc = (int) Math.round(((double)sleepers/(double)wplayers)*100.0);
			Bukkit.broadcastMessage("§6Es schlafen "+sleepers+"/"+wplayers+ " Spieler. ("+perc+"%)");
		
			if(perc >= 30) {
				Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
					
					@Override
					public void run() {
						if(sleepers != 0 && (int) Math.round(((double)sleepers/(double)wplayers)*100.0) >= 30) {
							p.getWorld().setTime(0);
							p.getWorld().setThundering(false);
							p.getWorld().setThunderDuration(0);
							
							p.getWorld().setWeatherDuration(0);
							
							Bukkit.broadcastMessage("§aDie Nacht wurde übersprungen.");
							sleepers = 0;
						}
						
					}
				}, 20*1);
				
			}
		}
	}
	
	
	@EventHandler
	public void  PlayerLeaveEnter(PlayerBedLeaveEvent e) {
		  if (sleepers > 0) {
		      sleepers--;
		      int wplayers = e.getPlayer().getWorld().getPlayers().size();
		      int perc = Math.round((sleepers / wplayers)) * 100;
		      
		      if (sleepers == 0) {
		        Bukkit.broadcastMessage("§7Keiner ist gerade müde und schläft.");
		      } else {
		        Bukkit.broadcastMessage("§6Es schlafen " + sleepers + "/" + wplayers + " Spieler. (" + perc + "%)");
		      } 
		    } 
	}
	
}
