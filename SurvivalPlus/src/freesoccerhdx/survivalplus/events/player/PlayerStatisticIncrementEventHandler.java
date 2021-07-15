package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;

import freesoccerhdx.survivalplus.haupt.Methoden;

public class PlayerStatisticIncrementEventHandler implements Listener {

	@EventHandler
	public static void statisticinc(PlayerStatisticIncrementEvent e) {
		if(e.getStatistic() == Statistic.JUMP) {
			Player p = e.getPlayer();
			//doublejump
			if(p.getInventory().getBoots() != null) {
				ItemStack is = p.getInventory().getBoots();
				int Doppelsprung = Methoden.getLoreEnchLevel(is, "Sprungkraft");
				
				if(Doppelsprung > 0) {
					p.setVelocity(p.getVelocity().multiply(1.0+(Doppelsprung/20)));
				}
				
			}
		}
	}
	
}
