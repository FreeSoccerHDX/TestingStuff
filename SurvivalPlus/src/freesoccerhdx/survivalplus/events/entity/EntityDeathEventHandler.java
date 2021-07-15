package freesoccerhdx.survivalplus.events.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import freesoccerhdx.survivalplus.haupt.Methoden;


public class EntityDeathEventHandler implements Listener {

	
	@EventHandler
	public void entitydeath(EntityDeathEvent e) {
		
		
		
		if(e.getEntityType() == EntityType.WITCH) {
			Entity witch = e.getEntity();
			Location loc = e.getEntity().getLocation();
			
			if(witch.getCustomName() != null) {
				Random r = new Random();
				int rnd = r.nextInt(100);
				
				if(rnd < 15) {
					loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.NETHER_STAR));
					
				}else if(rnd < 40) {
					ItemStack buch = new ItemStack(Material.ENCHANTED_BOOK);
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta) buch.getItemMeta();
					meta.addStoredEnchant(Enchantment.MENDING, 1, true);
				
					if(meta != null) {
						List<String> lore = new ArrayList<>();
						lore.add("§7Ausbesserung XXV");
						meta.setLore(lore);
					}
					buch.setItemMeta(meta);
					loc.getWorld().dropItemNaturally(loc, buch);
					
				}else if(rnd < 60) {
					ItemStack hand = new ItemStack(Material.CROSSBOW);
					ItemMeta m = hand.getItemMeta();
					m.setLore(Methoden.randomNewBowEnchant());
					hand.setItemMeta(m);
					hand.addUnsafeEnchantments(Methoden.randomOldBowEnchant(Material.CROSSBOW));
					loc.getWorld().dropItemNaturally(loc, hand);
				}else if(rnd < 80) {
					ItemStack hand = new ItemStack(Material.DIAMOND_AXE);
					ItemMeta m = hand.getItemMeta();
					m.setLore(Methoden.randomNewWeaponEnchant());
					hand.setItemMeta(m);
					hand.addUnsafeEnchantments(Methoden.randomOldWeaponEnchant(Material.DIAMOND_AXE));
					loc.getWorld().dropItemNaturally(loc, hand);
				}else if(rnd < 100) {
					ItemStack hand = new ItemStack(Material.BOW);
					ItemMeta m = hand.getItemMeta();
					m.setLore(Methoden.randomNewBowEnchant());
					hand.setItemMeta(m);
					hand.addUnsafeEnchantments(Methoden.randomOldBowEnchant(Material.BOW));
					loc.getWorld().dropItemNaturally(loc, hand);
				}
			}
		}
	}
	
	
}
