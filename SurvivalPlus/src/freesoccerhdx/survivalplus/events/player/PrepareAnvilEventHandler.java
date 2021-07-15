package freesoccerhdx.survivalplus.events.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import freesoccerhdx.survivalplus.enchants.EnchantLimits;
import freesoccerhdx.survivalplus.enchants.EnchantmentHandler;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.Sorter;
import freesoccerhdx.survivalplus.haupt.main;

public class PrepareAnvilEventHandler implements Listener {


	
	@EventHandler
	public void PrepareAnvil(PrepareAnvilEvent e) {
		Inventory inv = e.getInventory();
		
		
		if(inv.getItem(0) != null && inv.getItem(1) != null) {
			ItemStack i1 = inv.getItem(0).clone();
			ItemStack i2 = inv.getItem(1).clone();
			
			if(i1.getType() != Material.AIR && i2.getType() != Material.AIR && i1.getType() != i2.getType()) {
				if(i2.getType() != Material.ENCHANTED_BOOK) {
					return;
				}
				
			}
			
			ItemStack result = e.getResult();
			
			List<String> lore1 = null, lore2 = null, resultlore = new ArrayList<>();
			
			if(Methoden.hasCustomLore(i1)) {
				lore1 = i1.getItemMeta().getLore(); 	
			}
			if(Methoden.hasCustomLore(i2)) {
				lore2 = i2.getItemMeta().getLore();
			}
			
			
			
			if(lore1 == null && lore2 != null) {
				resultlore.addAll(lore2);
			}else if(lore1 != null && lore2 == null) {
				resultlore.addAll(lore1);
			}else if(lore1 != null && lore2 != null){
				//need to be packed together
				List<String> hasench = new ArrayList<>();
				
				for(String s1 : lore1) {
					if(s1.startsWith("§7") && s1.split(" ").length == 2) {
						String enchname = s1.split(" ")[0].replace("§7", "");
						hasench.add(enchname);
						int thislvl = Methoden.arabisch(s1.split(" ")[1]);
						int otherlvl = Methoden.getLoreEnchLevel(lore2, enchname);
						
						if(thislvl == otherlvl) {
							resultlore.add("§7"+enchname+" "+Methoden.roemisch(Math.min(thislvl+1, EnchantLimits.getEnchantLimit(enchname))));
						}else {
							resultlore.add("§7"+enchname+" "+Methoden.roemisch(Math.max(thislvl, otherlvl)));
						}
						
					}else if(!hasench.contains(s1)){
						hasench.add(s1);
						resultlore.add(s1);
					}
				}
				for(String s2 : lore2) {
					if(s2.startsWith("§7") && s2.split(" ").length == 2) {
						String enchname = s2.split(" ")[0].replace("§7", "");
						if(!hasench.contains(enchname)) {
							resultlore.add(s2);
						}
					}else if(!hasench.contains(s2)){
						hasench.add(s2);
						resultlore.add(s2);
					}
				}
			}
			
			// handle custom enchant fix
			
			if(resultlore.size() > 0) {
				if(result == null || result.getType() == Material.AIR) {
					result = i1.clone();
				}
				ItemMeta rme = result.getItemMeta();
				if(rme != null) {
					
					int wegsch = Methoden.getLoreEnchLevel(resultlore, "Wegschleudern");
					int heran = Methoden.getLoreEnchLevel(resultlore, "Heranziehen");
					
					if(wegsch > heran) {
						resultlore.remove("§7Heranziehen "+ Methoden.roemisch(heran));
					}else if(heran > wegsch) {
						resultlore.remove("§7Wegschleudern "+ Methoden.roemisch(wegsch));
					}else if(wegsch == heran && wegsch != 0) {
						if(Methoden.chance(50)) {
							resultlore.remove("§7Wegschleudern "+ Methoden.roemisch(wegsch));
						}else {
							resultlore.remove("§7Heranziehen "+ Methoden.roemisch(heran));
						}
					}
					
					int ham = Methoden.getLoreEnchLevel(resultlore, "Hammer");
					int vein = Methoden.getLoreEnchLevel(resultlore, "Veinminer");
					
					if(ham > vein) {
						resultlore.remove("§7Hammer "+ Methoden.roemisch(vein));
					}else if(vein > ham) {
						resultlore.remove("§7Veinminer "+ Methoden.roemisch(ham));
					}else if(ham == vein && wegsch != 0) {
						if(Methoden.chance(50)) {
							resultlore.remove("§7Hammer "+ Methoden.roemisch(ham));
						}else {
							resultlore.remove("§7Veinminer "+ Methoden.roemisch(vein));
						}
					}

					wegsch = Methoden.getLoreEnchLevel(resultlore, "Wegschleudern");
					heran = Methoden.getLoreEnchLevel(resultlore, "Heranziehen");
					int fest = Methoden.getLoreEnchLevel(resultlore, "Festhalten");
					
					if(fest > wegsch) {
						resultlore.remove("§7Wegschleudern "+ Methoden.roemisch(wegsch));
					}else if(wegsch > fest) {
						resultlore.remove("§7Festhalten "+ Methoden.roemisch(fest));
					}else if(fest == wegsch && wegsch != 0) {
						if(Methoden.chance(50)) {
							resultlore.remove("§7Festhalten "+ Methoden.roemisch(fest));
						}else {
							resultlore.remove("§7Wegschleudern "+ Methoden.roemisch(wegsch));
						}
					}
					if(fest > heran) {
						resultlore.remove("§7Heranziehen "+ Methoden.roemisch(heran));
					}else if(heran > fest) {
						resultlore.remove("§7Festhalten "+ Methoden.roemisch(fest));
					}else if(fest == heran && heran != 0) {
						if(Methoden.chance(50)) {
							resultlore.remove("§7Festhalten "+ Methoden.roemisch(fest));
						}else {
							resultlore.remove("§7Heranziehen "+ Methoden.roemisch(heran));
						}
					}
					
					List<String> torem = new ArrayList<>();
					for(String s : resultlore) {
						String ename = s.split(" ")[0].replace("§7", "");
						if(s.split(" ").length == 2) {
							if(!EnchantmentHandler.canEnchant(ename, result.getType())) {							
								torem.add(s);	
							}
						}
					}
					if(torem.size() > 0) {
						for(String s : torem) {
							resultlore.remove(s);
						}
					}
					
				//	Bukkit.broadcastMessage(resultlore+" ->" + resultlore.size());
					resultlore = Sorter.sortLoreEnchants(resultlore);
					//Bukkit.broadcastMessage("§c"+resultlore+" ->" + resultlore.size());
					rme.setLore(resultlore);
					
					result.setItemMeta(rme);
					
					int oldsize = (lore1 == null ? 0 : lore1.size());
					int newsize = resultlore.size()*2;
					int cost = Math.max(5, e.getInventory().getRepairCost()+newsize-oldsize);
					
					Bukkit.getServer().getScheduler().runTask(main.m, () -> e.getInventory().setRepairCost(cost));
			
					
					e.setResult(result);	
					
				}
				
			}
			
			
		}
			
		
	}
	
}

















