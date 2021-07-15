package freesoccerhdx.survivalplus.events.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.TicTacToe;
import freesoccerhdx.survivalplus.haupt.VierGewinnt;
import freesoccerhdx.survivalplus.haupt.main;
import freesoccerhdx.survivalplus.npc.NPCHandler;
import freesoccerhdx.survivalplus.warp.WarpManager;


public class InventoryClickEventHandler implements Listener {
	
	
	
	@EventHandler
	public void InventoryClick(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			if(e.getClickedInventory() != null){
				Player p = (Player) e.getWhoClicked();
				String openInvName = e.getView().getTitle();
				Inventory clickedinv = e.getClickedInventory();
				
				
				if(openInvName != null) {
					ItemStack cursor = e.getCursor().clone();
					ItemStack clickeditem = e.getCurrentItem() == null ? new ItemStack(Material.AIR) : e.getCurrentItem();
					if(openInvName.equals("NPC-Editor") && clickedinv.equals(e.getView().getTopInventory())) {
						e.setCancelled(true);
						if(!NPCHandler.hasEditInventory(p)) { p.closeInventory(); return;}
						
						int slot = e.getSlot();
						if(slot == 1 || slot == 10 || slot == 19 || slot == 28 || slot == 37 || slot == 46) { // equipment slot-names
							p.sendMessage("§cDieser Slot ist gesperrt!");
						}else if(slot == 0) { //helm
							
							if((cursor.getType() == Material.AIR || cursor == null) && clickeditem.getType() == Material.RED_STAINED_GLASS_PANE) {
								
							}else if(cursor == null && clickeditem.getType() != Material.RED_STAINED_GLASS_PANE) {
								e.getView().setCursor(clickeditem.clone());
								e.getClickedInventory().setItem(slot,  Methoden.item(Material.RED_STAINED_GLASS_PANE, 1, 0, "§cKein Item", new String[] {}));
							}else if(cursor != null && clickeditem.getType() == Material.RED_STAINED_GLASS_PANE) {
								e.getClickedInventory().setItem(slot, cursor.clone());
								//e.setCursor(new ItemStack(Material.AIR));
								e.getView().setCursor(new ItemStack(Material.AIR));
							}else if(cursor != null && clickeditem.getType() != Material.RED_STAINED_GLASS_PANE) {e.setCancelled(true);
								e.getClickedInventory().setItem(slot, cursor.clone());
								e.getView().setCursor(clickeditem.clone());
							}
						}
					}else if(openInvName.equals("NPC-Editor")) {
						e.setCancelled(true);
						//TODO: what if click on bottominv
					}
					
					if(openInvName.equals("§2Deep Storage Backpack")) {
						e.setCancelled(true);
						Inventory inv = e.getView().getTopInventory();	
						NBTItem nbitem =  new NBTItem(p.getInventory().getItemInMainHand());
						int rs = e.getRawSlot();
					
						if(rs < 5) {
							ItemStack clis = inv.getItem(rs);
							
							if(clis.getType() != Material.BEDROCK) {
								int clcount = nbitem.getInteger("counter"+rs);
								
								int count = clcount >= 64 ? 64 : clcount; 
								clcount -= count;
								nbitem.setInteger("counter"+rs, Math.max(0, clcount));
								if(clcount <= 0) {
									ItemStack bedrock = Methoden.item(Material.BEDROCK, 1, 0, "§cKein Item", new String[] {"§aAnzahl: 0"});
									inv.setItem(rs, bedrock);
									nbitem.setString("material"+rs, "null");
								}else {
									ItemMeta meta = clis.getItemMeta();
									List<String> lore = new ArrayList<>();
									lore.add("§aAnzahl: "+clcount );
									meta.setLore(lore);
									clis.setItemMeta(meta);
									inv.setItem(rs, clis);
								}
								ItemStack drop = new ItemStack(clis.getType(),count);
								Item dropped = p.getWorld().dropItem(p.getLocation(), drop);
								dropped.setPickupDelay(0);
								
							}
							
							
						}else if(rs > 4) {
							ItemStack clicked = e.getCurrentItem();
							
							if(clicked.getType() != Material.AIR && clicked.getType().getMaxDurability() == 0 && clicked.getMaxStackSize() > 1 && clicked.getEnchantments().size() == 0) {
								Material m = clicked.getType();
								int targetslot = -1;
								if(inv.getItem(0).getType() == m) targetslot = 0;
								if(inv.getItem(1).getType() == m) targetslot = 1;
								if(inv.getItem(2).getType() == m) targetslot = 2;
								if(inv.getItem(3).getType() == m) targetslot = 3;
								if(inv.getItem(4).getType() == m) targetslot = 4;
								
								if(targetslot != -1) {
									nbitem.setInteger("counter"+targetslot, nbitem.getInteger("counter"+targetslot)+clicked.getAmount());
									clicked.setType(Material.AIR);
									p.getInventory().setItem(e.getSlot(), clicked);
									
									ItemStack targetitem = inv.getItem(targetslot);
									ItemMeta meta = targetitem.getItemMeta();
									List<String> lore = new ArrayList<>();
									lore.add("§aAnzahl: "+ nbitem.getInteger("counter"+targetslot));
									meta.setLore(lore);
									targetitem.setItemMeta(meta);
									inv.setItem(targetslot, targetitem);
									
								}else {
									int freeslot = -1;
									if(inv.getItem(0).getType() == Material.BEDROCK) freeslot = 0;
									if(inv.getItem(1).getType() == Material.BEDROCK) freeslot = 1;
									if(inv.getItem(2).getType() == Material.BEDROCK) freeslot = 2;
									if(inv.getItem(3).getType() == Material.BEDROCK) freeslot = 3;
									if(inv.getItem(4).getType() == Material.BEDROCK) freeslot = 4;
									
									if(freeslot != -1) {
										nbitem.setString("material"+freeslot, ""+m);
										nbitem.setInteger("counter"+freeslot, clicked.getAmount());
										ItemStack targetitem = Methoden.item(clicked.getType(), 1, 0, null, new String[] {"§aAnzahl: "+clicked.getAmount()});
										inv.setItem(freeslot, targetitem);
										clicked.setType(Material.AIR);
										p.getInventory().setItem(e.getSlot(), clicked);
									}
								}
							}
							
							
						}
						p.getInventory().setItemInMainHand(nbitem.getItem());
					
						
						

					}
					
					if(openInvName.equals("§6§lNeue Verzauberungen")) {
						e.setCancelled(true);
					}
					
					if(openInvName.equals("§aVeränderungen")) {
						e.setCancelled(true);
						if(e.getCurrentItem() != null) {
							if(e.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
								if(e.getCurrentItem().getItemMeta().getDisplayName() != null) {
									if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6§lNeue Verzauberungen")) {
										p.openInventory(main.newstuffInventar_enchanting); 
									}
								}
							}
						}
					}
					
					if(openInvName.equals("§6§lWarpmenu")) {
						e.setCancelled(true);
						if(e.getCurrentItem() != null) {
							if(e.getCurrentItem().getType() == Material.PAPER) {
								if(e.getCurrentItem().getItemMeta().getDisplayName() != null) {
									String warp = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
									Location wloc = Methoden.getWarp(warp);
									p.closeInventory();
									WarpManager.istp.add(p.getName());
									WarpManager.teleport(p,wloc,p.getLocation().getBlock(),20*3);
									p.sendMessage("§aDeine Teleportation findet in 3 Sekunden statt.");
									
								}
							}
						}
					}
					
					if(openInvName.equals("§3§lDisenchanter")) {
						e.setCancelled(true);
						Inventory inv = p.getOpenInventory().getTopInventory();
						ItemStack target = inv.getItem(4);
						ItemMeta targetMeta = target.getItemMeta();
						
						
						if(e.getCurrentItem() != null) {
							ItemStack is = e.getCurrentItem();
							if(is.getType() == Material.ENCHANTED_BOOK && e.getClick() == ClickType.LEFT) {
								if(is.hasItemMeta()) {
									ItemMeta meta = is.getItemMeta();
									if(meta.hasDisplayName()) {
										String dname = meta.getDisplayName();
										
										//p.sendMessage(dname.replace("§", "&"));
										if(dname.startsWith("§e")) {
											List<String> lore = targetMeta.getLore();
											lore.remove(dname.replace("§e", "§7"));
											targetMeta.setLore(lore);
										}else {
											List<String> lore = meta.getLore();
											Enchantment ench = Enchantment.getByName(lore.get(0));
											
											targetMeta.removeEnchant(ench);
										}
										p.sendMessage("§aErfolgreich die Verzauberung entfernt!");
										ExperienceOrb orb = p.getWorld().spawn(p.getLocation(), ExperienceOrb.class);
										int xp = Integer.parseInt(meta.getLore().get(meta.getLore().size()-1).split(" ")[1]);
										orb.setExperience(xp);
										
									}
								}
							}
						}
						
						target.setItemMeta(targetMeta);
						inv.setItem(4, target);
						p.setItemInHand(target);
						Methoden.calcDisenchanter(p, inv);
						
					}
				}

				
				
				if(main.backpack.containsKey(p.getName())) {
					if(e.getAction() == InventoryAction.HOTBAR_SWAP) {
						
						if(p.getInventory().getItem(e.getHotbarButton()).equals(p.getItemInHand())) {
							e.setCancelled(true);
						}
					}
					if(e.getCurrentItem() != null) {
						if(e.getCurrentItem().equals(p.getItemInHand())) {
							e.setCancelled(true);
						}
					}
				}
				
				if(openInvName.equals("                  §a§lTic Tac Toe")){
					e.setCancelled(true);
					if(e.getClickedInventory() == p.getOpenInventory().getTopInventory()){
						if(TicTacToe.hasGame(p)){
							TicTacToe.getGame(p).onClick(p, e.getSlot());
						}
					}
				}
				if(openInvName.equals("                  §a§lVier gewinnt")){
					e.setCancelled(true);
					if(e.getClickedInventory() == p.getOpenInventory().getTopInventory()){
						if(VierGewinnt.hasGame(p)){
							VierGewinnt.getGame(p).onClick(p, e.getSlot());
						}
					}
				}
				
				
				
			}
		}
	}
}
