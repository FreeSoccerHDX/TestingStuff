package freesoccerhdx.survivalplus.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import freesoccerhdx.survivalplus.haupt.Methoden;

public class FoodLevelChangeEventHandler implements Listener {

	@EventHandler
	public void FoodLevelChange(FoodLevelChangeEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(p.getInventory().getHelmet() != null) {
				int feeder = Methoden.getLoreEnchLevel(p.getInventory().getHelmet(), "Sättigung");

				if(feeder > 0 && e.getFoodLevel() < p.getFoodLevel() && p.getFoodLevel() < 20) {
					if(Methoden.chance(9*feeder)) {
						e.setCancelled(true);
						p.setFoodLevel(Math.min(p.getFoodLevel()+1,19));
						p.setSaturation(2);
					}
				}
				
			}
		}
		
	}
	
}
