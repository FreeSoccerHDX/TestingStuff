package freesoccerhdx.survivalplus.haupt;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TicTacToe {

	public static HashMap<Player, TicTacToe> Games = new HashMap<>();
	public static HashMap<Player, HashMap<Player, Long>> anfragen = new HashMap<>();

	Player player1;
	Player player2;
	Inventory inv;
	String dran = "";
	int züge = 0;
	int spielgröße = 3;
	
	
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
	
	public TicTacToe(Player p1, Player p2){
		this.player1 = p1;
		this.player2 = p2;
		
		this.dran = p1.getName();
		
		this.inv = Bukkit.createInventory(null, 27, "                  §a§lTic Tac Toe");
		Methoden.item(Material.DARK_OAK_DOOR, 0, 0, "", new String[]{});
		inv.setItem(3, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		inv.setItem(4, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		inv.setItem(5, Methoden.item(Material.WHITE_WOOL, 1,0, "", new String[]{}));
		inv.setItem(10, Methoden.kopf(player1.getName(), "§d" + player1.getName(), 1, new String[]{}));
		inv.setItem(12, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		inv.setItem(13, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		inv.setItem(14, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		inv.setItem(16, Methoden.kopf("MHF_Question", "§d" + player2.getName(), 1, new String[]{}));
		inv.setItem(21, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		inv.setItem(22, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		inv.setItem(23, Methoden.item(Material.WHITE_WOOL, 1, 0, "", new String[]{}));
		
		p1.openInventory(inv);
		p2.openInventory(inv);
		
		Games.put(p1, this);
		Games.put(p2, this);
	}
	public static TicTacToe getGame(Player p){
		if(hasGame(p)){
			return Games.get(p);
		}else{
			return null;
		}
	}
	
	public void onClick(Player p, int slot){
		if(dran.equals(p.getName())){
			if(inv.getItem(slot) != null){
				if(inv.getItem(slot).getAmount() == 1){
					if(inv.getItem(slot).getType() == Material.WHITE_WOOL){
						inv.setItem(slot, Methoden.item( (p.getName().equals(player1.getName()) ? Material.BLUE_WOOL : Material.RED_WOOL), 1, (byte)0, "§e"+p.getName(), new String[]{}));
						züge++;
						
							if(hasWon()){			
								if(p.getName().equals(player1.getName())){
									win(p,player2);
								}else{
									win(p,player1);
								}
								return;
							}else{
								if(züge < 9){
									if(p.getName().equals(player1.getName())){
										dran = player2.getName();
										inv.setItem(10, Methoden.kopf("MHF_Question", "§d" + player1.getName(),1, new String[]{}));
										inv.setItem(16, Methoden.kopf(player2.getName(), "§d" + player2.getName(),1, new String[]{}));
									}else{
										dran = player1.getName();
										inv.setItem(10, Methoden.kopf(player1.getName(), "§d" + player1.getName(),1, new String[]{}));
										inv.setItem(16, Methoden.kopf("MHF_Question", "§d" + player2.getName(),1, new String[]{}));
									}
								}else{
									
									player1.sendMessage("§cEs gab keinen Sieger!");
									player2.sendMessage("§cEs gab keinen Sieger!");
									
									Games.remove(player1);
									Games.remove(player2);
									
									player1.closeInventory();
									player2.closeInventory();
									return;
								}
								
							}
						
						player1.updateInventory();
						player2.updateInventory();
					}
				}
			}
		}else{
			p.sendMessage("§cDu bist nicht am Zug!");
		}
	}
	public boolean checkCrossLeft(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 8;
		int count = 0;
		if(check.getDurability() != 0){
			for(int i = 1; i < spielgröße; i++){
				count++;
				if(!check.equals(inv.getItem(slot+(add*count)))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public boolean checkCrossRight(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 10;
		int count = 0;
		if(check.getDurability() != 0){
			for(int i = 1; i < spielgröße; i++){
				count++;
				if(!check.equals(inv.getItem(slot+(add*count)))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public boolean checkHorizontal(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 1;
		int count = 0;
		if(check.getDurability() != 0){
			for(int i = 1; i < spielgröße; i++){
				count++;
				if(!check.equals(inv.getItem(slot+(add*count)))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public boolean checkVertical(int slot){
		ItemStack check = inv.getItem(slot);
		int add = 9;
		int count = 0;
		if(check.getDurability() != 0){
			for(int i = 1; i < spielgröße; i++){
				count++;
				if(!check.equals(inv.getItem(slot+(add*count)))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public boolean hasWon(){
		
		if(checkCrossRight(3)
		|| checkHorizontal(3)
		|| checkVertical(3)
		|| checkVertical(4)
		|| checkVertical(5)
		|| checkCrossLeft(5)
		|| checkHorizontal(12)
		|| checkHorizontal(21)
		){
			return true;
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
		
		player1.closeInventory();
		player2.closeInventory();
	}
	
	
}
