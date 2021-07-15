package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import freesoccerhdx.survivalplus.haupt.main;

public class PlayerInteractEntityEventHandler implements Listener {

	
	@EventHandler
	public void PlayerLeashEntityEvent(PlayerLeashEntityEvent e) {
		Player p = e.getPlayer();
		if(p.getInventory().getItemInOffHand() != null) {
			if(p.getInventory().getItemInOffHand().getType() == Material.LEAD) {
				e.setCancelled(true);
				p.updateInventory();
			}
		}
		
		
	}

	
	@EventHandler
	public void PlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(e.getHand() != EquipmentSlot.HAND) {return;}
		
		if(e.getRightClicked() instanceof LivingEntity) { 
			LivingEntity clicked = (LivingEntity) e.getRightClicked();
			if(p.getInventory().getItemInMainHand() != null) {
				ItemStack is = p.getInventory().getItemInMainHand();
				if(is.getType() == Material.LEAD) {
					if(is.getItemMeta() != null) {
						if(is.getItemMeta().getDisplayName() != null) {
							if(is.getItemMeta().getDisplayName().equals("§6§l§k#§f§4§lMobfänger§6§l§k#")) {
								e.setCancelled(true);
								if(!main.holdingentity.containsKey(p.getName())) {
									main.holdingentity.put(p.getName(), clicked);
									//clicked.setAI(false);
									clicked.setGravity(false);
									Double fakdis = 3-p.getLocation().distance(clicked.getEyeLocation());
									clicked.teleport(p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(fakdis)));
									
								}else {
									clicked.setGravity(true);
									main.holdingentity.remove(p.getName());
								}
								
							}
						}
					}
					
				}
			} 
			if(p.getInventory().getItemInOffHand() != null) {
				if(p.getInventory().getItemInOffHand().getType() == Material.LEAD) {
					e.setCancelled(true);
					
				}
			}
		}
		if(p.isSneaking()) {
			
			if(e.getRightClicked() instanceof Villager) {
				Villager vil = (Villager) e.getRightClicked();
				if(vil.isAdult()) {
					if(p.getInventory().getItemInMainHand() != null) {
						ItemStack is = p.getInventory().getItemInMainHand();
						if(is.getType() == Material.BREAD) {
							if(is.getAmount() >= 16) {
								is.setAmount(is.getAmount()-16);
								if(is.getAmount() == 0) {
									is.setType(Material.AIR);
									p.getInventory().setItemInMainHand(is);
								}
								Villager newvil = (Villager) vil.getWorld().spawnEntity(vil.getLocation(), EntityType.VILLAGER);
								newvil.setBaby();
								e.setCancelled(true);
																
							}
							
						}
					}
				}
			}
		}
	}
	
}
