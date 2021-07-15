package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import de.tr7zw.nbtapi.NBTItem;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.main;
import freesoccerhdx.survivalplus.npc.NPCHandler;

public class InventoryCloseEventHandler implements Listener {

	
	
	@EventHandler
	public void invclose(InventoryCloseEvent e) {
		if(e.getPlayer() instanceof Player) {
			Inventory inv = e.getInventory();
			Player p = (Player) e.getPlayer();
			
			
			if(NPCHandler.hasEditInventory(p)) {
				if(p.getItemOnCursor() != null) {
					if(p.getItemOnCursor().getType() != Material.AIR) {
						p.getWorld().dropItem(p.getLocation(), p.getItemOnCursor());
					}
				}
				NPCHandler.closeEditInventory(p);
			}
			
			if(main.backpack.containsKey(p.getName())) {
				if(main.backpack.get(p.getName()) == inv) {
					
					NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
					nbti.setString("invcontent", Methoden.toBase64(inv));
					main.backpack.remove(p.getName());
					p.getInventory().setItemInMainHand(nbti.getItem());
					
				}
			}
		}
	}
	
}
