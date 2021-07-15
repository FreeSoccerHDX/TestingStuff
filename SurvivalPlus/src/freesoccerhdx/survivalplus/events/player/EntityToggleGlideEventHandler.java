package freesoccerhdx.survivalplus.events.player;

import java.util.List;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;

import freesoccerhdx.survivalplus.haupt.Methoden;

public class EntityToggleGlideEventHandler implements Listener {

	@EventHandler
	public void EntityToggleGlide(EntityToggleGlideEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			
			if(e.isGliding()){
				if(p.getInventory().getChestplate() != null){
					ItemStack item = p.getInventory().getChestplate();
					
					if(item.getType() != null){
						if(item.getType() == Material.ELYTRA){
							if(item.getItemMeta() != null){
								if(item.getItemMeta().getLore() != null){
									List<String> lore = item.getItemMeta().getLore();
									
									for(String l : lore){
										if(l.startsWith("§7Elytra-Boost")){
											int wert = Methoden.arabisch(l.replace("§7Elytra-Boost ", ""));
										
											p.setVelocity(p.getLocation().getDirection().multiply((1 + (wert/20))));
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
	}
	
}
