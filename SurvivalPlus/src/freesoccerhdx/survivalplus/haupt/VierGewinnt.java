package freesoccerhdx.survivalplus.haupt;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VierGewinnt {
	
	public static HashMap<Player, VierGewinnt> Games = new HashMap<>();
	public static HashMap<Player, HashMap<Player, Long>> anfragen = new HashMap<>();
	
	Player player1;
	Player player2;
	Inventory inv;
	String dran = "";
	int züge = 0;
	
	public static void removeAnfrage(Player p, Player target){
		if(hasAnfrage(p,target)){
			HashMap<Player, Long> PlayerAnfragen = anfragen.containsKey(p) ? anfragen.get(p) : new HashMap<>();
			PlayerAnfragen.remove(target);
			anfragen.put(p, PlayerAnfragen);
		}
	}
	
	public static void addAnfrage(Player p, Player target){
		HashMap<Player, Long> PlayerAnfragen = anfragen.containsKey(p) ? anfragen.get(p) : new HashMap<>();
		PlayerAnfragen.put(target, System.currentTimeMillis());
		anfragen.put(p, PlayerAnfragen);
	}
	
	public static boolean hasAnfrage(Player p, Player target){
		if(anfragen.containsKey(p)){
			HashMap<Player, Long> PlayerAnfragen = anfragen.get(p);
			if(PlayerAnfragen.containsKey(target)){
				Long time = PlayerAnfragen.get(target);
				Long jetzt = System.currentTimeMillis();
				if(jetzt-time <= 1000*120){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public VierGewinnt(Player p1, Player p2){
		this.player1 = p1;
		this.player2 = p2;
		
		this.dran = p1.getName();
		
		this.inv = Bukkit.createInventory(null, 54, "                  §a§lVier gewinnt");
		
		p1.openInventory(inv);
		p2.openInventory(inv);
		try{
		//	for(int x = 0; x < 6; x++){
			int x = 5;
				for(int i = 1+(9*x); i < 8+(9*x); i++){
					inv.setItem(i, Methoden.item(Material.LEGACY_WOOL, (x == 5 ? 1 : 0), (byte) (x == 5 ? 8 : 0), " ", new String[]{}));
				}
	//		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		inv.setItem(0, Methoden.kopf(player1.getName(), "§d" + player1.getName(),1, new String[]{}));
		inv.setItem(8, Methoden.kopf("MHF_Question", "§d" + player2.getName(),1, new String[]{}));
		
		inv.setItem(9, Methoden.item(Material.LEGACY_STAINED_GLASS_PANE, 1, (byte)13, "§2Dieser Spieler ist am Zug!", new String[]{}));	
		inv.setItem(17, Methoden.item(Material.BARRIER, 1, (byte)0, "§4Dieser Spieler ist nicht am Zug!", new String[]{}));	
		
		Games.put(p1, this);
		Games.put(p2, this);
	}
	public static VierGewinnt getGame(Player p){
		if(hasGame(p)){
			return Games.get(p);
		}else{
			return null;
		}
	}
	
	public void onClick(Player p, int slot){
		if(dran.equals(p.getName())){
			if(inv.getItem(slot).getAmount() == 1){
				if(inv.getItem(slot).getType() == Material.LEGACY_WOOL && inv.getItem(slot).getDurability() == 8){
					inv.setItem(slot, Methoden.item(Material.LEGACY_WOOL, 1, (byte) (p.getName().equals(player1.getName()) ? 11 : 14), "§e"+p.getName(), new String[]{}));
					if(slot-9 >= 0){
						inv.setItem(slot-9,  Methoden.item(Material.LEGACY_WOOL, 1, (byte) 8, "§e"+p.getName(), new String[]{}));
					}
					züge++;
						if(hasWon()){			
							if(p.getName().equals(player1.getName())){
								win(p,player2);
							}else{
								win(p,player1);
							}
							return;
						}else{
							if(züge < 42){
	
								if(p.getName().equals(player1.getName())){
								
									dran = player2.getName();
									inv.setItem(0, Methoden.kopf("MHF_Question", "§d" + player1.getName(),1, new String[]{}));
									inv.setItem(8, Methoden.kopf(player2.getName(), "§d" + player2.getName(),1, new String[]{}));
									
									inv.setItem(17, Methoden.item(Material.LEGACY_STAINED_GLASS_PANE, 1, (byte)13, "§2Dieser Spieler ist am Zug!", new String[]{}));	
									inv.setItem(9, Methoden.item(Material.BARRIER, 1, (byte)0, "§4Dieser Spieler ist nicht am Zug!", new String[]{}));	
									
								}else{
						
									dran = player1.getName();
									inv.setItem(0, Methoden.kopf(player1.getName(), "§d" + player1.getName(),1, new String[]{}));
									inv.setItem(8, Methoden.kopf("MHF_Question", "§d" + player2.getName(),1, new String[]{}));
									
									inv.setItem(9, Methoden.item(Material.LEGACY_STAINED_GLASS_PANE, 1, (byte)13, "§2Dieser Spieler ist am Zug!", new String[]{}));	
									inv.setItem(17, Methoden.item(Material.BARRIER, 1, (byte)0, "§4Dieser Spieler ist nicht am Zug!", new String[]{}));	
								}
							}else{
								
								player1.sendMessage("§cEs gab keinen Sieger!");
								player2.sendMessage("§cEs gab keinen Sieger!");
								
								Games.remove(player1);
								Games.remove(player2);
								
								breakInventory();
								return;
							}
							
						}
					
					player1.updateInventory();
					player2.updateInventory();
				}
			}
		}else{
			p.sendMessage("§cDu bist nicht am Zug!");
		}
	}
	public boolean checkCrossLeft(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 8;
		return check(slot, add, check);
	}
	public boolean checkCrossRight(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 10;
		return check(slot, add, check);
	}
	public boolean checkHorizontal(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 1;
		return check(slot, add, check);
	}
	public boolean checkVertical(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 9;
		return check(slot, add, check);
	}
	public boolean check(int slot, int add, ItemStack check){
		if(check.getDurability() != 0 && check.getDurability() != 8){
			for(int i = 1; i < 5; i++){
				try{
					if(inv.getItem(slot+(add*(i-1))) != null){
						if(!check.equals(inv.getItem(slot+(add*(i-1))))){
							return false;
						}
					}else{
						return false;
					}
				}catch(Exception ex){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean hasWon(){
		
		for(int x = 0; x < 6; x++){
			for(int i = 1+(9*x); i < 8+(9*x); i++){
				try{
					if(checkCrossRight(i)
					|| checkCrossLeft(i)
					|| checkHorizontal(i)
					|| checkVertical(i)){
						return true;
					}
				}catch(Exception ex){
				
				}
						
			}
		}
		
	
	
		return false;
	}
	
	public static boolean hasGame(Player p){
		return Games.containsKey(p);
	}
	
	public Player getGegner(Player p) {
		if(p.getName().equals(player1.getName())){
			return player2;
		}else if(p.getName().equals(player2.getName())){
			return player1;
		}
		return null;
	}
	public void win(Player winner, Player loser) {

		winner.sendMessage("§aDu hast gegen §c" + loser.getName() + " §aGewonnen!");
		loser.sendMessage("§cDu hast gegen §a" + winner.getName() + " §cVerloren!");

		Games.remove(player1);
		Games.remove(player2);
		
		breakInventory();
	}
	public void breakInventory(){
		Bukkit.getScheduler().runTaskLater(main.m, new Runnable(){

			@Override
			public void run() {
				Random r = new Random();
				
				int totalSlots = 0;
				
				for(int i = 0; i < inv.getSize();i ++){
					if(inv.getItem(i) != null){
						if(inv.getItem(i).getType() != Material.AIR){
							totalSlots++;
						}
					}
				}
				
				
				Bukkit.getScheduler().runTaskLater(main.m, new Runnable(){

					@Override
					public void run() {
						player1.closeInventory();
						player2.closeInventory();
					}
					
				}, totalSlots*2+10);
				
				for(int i = 0; i < totalSlots; i++){
					
					Bukkit.getScheduler().runTaskLater(main.m, new Runnable(){

						@Override
						public void run() {
							int slot = -10;
							
							while(slot == -10){
								int randSlot = r.nextInt(inv.getSize());
								ItemStack check = inv.getItem(randSlot);
								if(check != null){
									if(check.getType() != Material.AIR){
										slot = randSlot;
										break;
									}
								}
							}
							
							if(slot != -10){
								inv.setItem(slot, new ItemStack(Material.AIR));
								player1.playSound(player1.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.7);
								player2.playSound(player2.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.7);
							}
							
						}
						
					}, i*2);
				}
				
			}
			
		}, 20*3);
	}
	
}
