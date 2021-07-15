package freesoccerhdx.survivalplus.haupt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Sorter {

	
	public static List<ItemStack> sortItems(ItemStack[] invcontents){
		List<ItemStack> sort = new ArrayList<>();
		
		HashMap<Material, Integer> itemamount = new HashMap<>();
		
		
		for(ItemStack is : invcontents) {
			if(Methoden.isItemStackValid(is)) {
				Material mat = is.getType();
				int am = is.getAmount();
				
				if(!itemamount.containsKey(mat)) {
					itemamount.put(mat, 0);
				}
				itemamount.put(mat, itemamount.get(mat)+am);
				
			}
		}
		
		while(itemamount.size() > 0) {
			Material bestmat = null;
			int bestcount = -1;
			for(Material mat : itemamount.keySet()) {
				int count = itemamount.get(mat);
				
				if(bestcount == -1 || count > bestcount) {
					bestcount = count;
					bestmat = mat;
				}
			}
			
			for(ItemStack cis : invcontents) {
				if(Methoden.isItemStackValid(cis)) {
					if(cis.getType() == bestmat) {
						sort.add(cis.clone());
					}
				}
			}
			
			
			
			itemamount.remove(bestmat);
			
			
		
		}
		
		
		return sort;
	}
	
	private static int compare(String s1, String s2) {
		char c1 = s1.replace("§7", "").charAt(0);
    	char c2 = s2.replace("§7", "").charAt(0);
    	
    	if(c1 > c2) {
    		return 0;
    	}else {
    		return 1;
    	}
	}
	
	
	public static List<String> sortLoreEnchants(List<String> lore){
		List<String> newlore = new ArrayList<>();
		
		while(lore.size() > 0) {
			String bestlore = "";
			for(String s : lore) {
				
				if(!bestlore.equals("")) {
					int comp = compare(bestlore,s);
					if(comp == 1) {
						bestlore = s;
					}
				}else {
					bestlore = s;
				}
				
				
			} 
			
			newlore.add(bestlore);
			lore.remove(bestlore);
		}
		 
		return newlore;
		
		/*
		List<String> newlore = new ArrayList<>();
		
		
		
		while(lore.size() > 0) {
			String bestench = null;
			Char
			for(String s : lore) {
				
			}
			
			lore.remove(bestench);
		}
		
		
		
		
		return newlore;
		*/
	}
	
	
	
}
