package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public class CraftItemEventHandler implements Listener {

	@EventHandler
	public void craftitem(CraftItemEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			ItemStack item = e.getCurrentItem();
			
			if(item.getType() != Material.PLAYER_HEAD) return;
			if(item.getItemMeta() == null) return;

			NBTItem nbitem = new NBTItem(item);
			
			if(!nbitem.hasKey("dsu")) return;
			
			
			if(e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
				e.setCancelled(true);
				p.sendMessage("§cDu kannst dieses Item nicht mit Shift craften.");
			}else {
				nbitem.setString("special", Math.random() + "" +System.currentTimeMillis());
				e.setCurrentItem(nbitem.getItem());
			}
			
			/*
			if(item.getItemMeta() != null) {
				ItemMeta meta = item.getItemMeta();
				if(meta.hasDisplayName()) {
					String name = meta.getDisplayName();
					
					if(name.contains("§a§l") && name.contains(" Rucksack")) {
						NBTItem nbti = new NBTItem(item);
						
						nbti.setString("special", Math.random() + "" + Math.random() + "" + Math.random() + "" + Math.random() + "" + Math.random() + "" + System.currentTimeMillis());
						e.setCurrentItem(nbti.getItem());
						
					}
					
				}
			}
			*/
			
		}
	}
	
}
