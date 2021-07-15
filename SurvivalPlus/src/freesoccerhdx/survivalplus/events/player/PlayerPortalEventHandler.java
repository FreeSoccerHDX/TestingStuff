package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import freesoccerhdx.survivalplus.haupt.DungeonPortal;


public class PlayerPortalEventHandler implements Listener {

	@EventHandler
	public void PlayerPortal(PlayerPortalEvent e) {
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		
		
		if(e.getCause() == TeleportCause.END_PORTAL) {
			e.setCancelled(true);
			Block portalblock = from.getBlock();
			
			boolean suc = DungeonPortal.isDungeonPortal(portalblock);
			e.setCancelled(suc);
			
			if(Bukkit.getWorld("DungeonWorld") != null) {
				p.teleport(Bukkit.getWorld("DungeonWorld").getSpawnLocation());
			}
			
		}
	}
	

	
}















