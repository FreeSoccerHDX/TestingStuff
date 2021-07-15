package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryInteractEventHandler implements Listener {

	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
	
		if(!e.isCancelled()){
			HumanEntity ent = e.getWhoClicked();
			if(ent instanceof Player){
				Player p = (Player)ent;
				Inventory inv = e.getInventory();
				 
				if(inv instanceof AnvilInventory){
					InventoryView view = e.getView();
					int rawSlot = e.getRawSlot();
					if(rawSlot == view.convertSlot(rawSlot)){
						/*
						slot 0 = left item slot
						slot 1 = right item slot
						slot 2 = result item slot
						*/
						if(rawSlot == 2){
							ItemStack item = e.getCurrentItem();
							if(item != null){
								ItemMeta meta = item.getItemMeta();
								if(meta != null){
								
									if(meta.hasDisplayName()){
										if(inv.getItem(0) != null) {
											ItemStack is = inv.getItem(0);
											ItemMeta ismeta = is.getItemMeta();
											if(is.getType() == Material.CHEST) {
												if(ismeta != null) {
													if(ismeta.hasDisplayName() && ismeta.getDisplayName().contains("Rucksack") && ismeta.getDisplayName().contains("§a§l")) {
														e.setCancelled(true);
														p.sendMessage("§cWarum solltest du deinen Rucksack umbenennen ???");
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
		}
	}
	 
	
	
}
