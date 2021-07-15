package freesoccerhdx.survivalplus.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import freesoccerhdx.survivalplus.haupt.main;

public class PlayerDropItemEventHandler implements Listener {

	@EventHandler
	public void dropitem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		
		if(main.backpack.containsKey(p.getName())) {
			if(e.getItemDrop().getItemStack().isSimilar(p.getInventory().getItemInMainHand())) {
				e.setCancelled(true);
			}
		}
	}
	
}
