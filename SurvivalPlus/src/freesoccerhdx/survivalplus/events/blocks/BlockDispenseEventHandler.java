package freesoccerhdx.survivalplus.events.blocks;

import java.util.HashMap;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

import freesoccerhdx.survivalplus.haupt.main;

public class BlockDispenseEventHandler implements Listener {

	
	@EventHandler
	public void onDispense(BlockDispenseEvent e) {
		ItemStack is = e.getItem();
		
		if(is != null) {
			if(is.hasItemMeta()) {
				if(is.hasItemMeta()) {
					if(is.getItemMeta().getDisplayName() != null) {
						if(is.getItemMeta().getDisplayName().equals("§6§lBomb §5§lin §4§lda §2§lSnow")) {
							e.setCancelled(true);
							Projectile proj = (Projectile) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation().add(e.getVelocity()).add(0.5, 0.5, 0.5), EntityType.SNOWBALL);
							
							proj.setVelocity(e.getVelocity());
							HashMap<String,Object> data = new HashMap<>();
							data.put("Abprallen", 16);
							data.put("Explosion", 3);
							data.put("Geschwindigkeit", 0.7);
							main.arrowdata.put(proj,data);
						}
					}
				}
			}
		}
	}
	
}
