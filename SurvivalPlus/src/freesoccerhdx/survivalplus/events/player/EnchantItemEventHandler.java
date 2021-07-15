package freesoccerhdx.survivalplus.events.player;

import java.lang.reflect.Field;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import freesoccerhdx.survivalplus.enchants.EnchantLimits;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.Sorter;
import net.minecraft.server.v1_16_R3.ContainerEnchantTable;
import net.minecraft.server.v1_16_R3.ContainerProperty;
import net.minecraft.server.v1_16_R3.CriterionTriggers;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.IInventory;
import net.minecraft.server.v1_16_R3.IRecipe;
import net.minecraft.server.v1_16_R3.StatisticList;

public class EnchantItemEventHandler implements Listener {

	@EventHandler
	public void prepareEnchItem(PrepareItemEnchantEvent e) {
		ItemStack is = e.getItem();

		if(Methoden.isItemStackValid(is)) {
			if(is.getType() == Material.ELYTRA) {
				e.getOffers()[0] = new EnchantmentOffer(Enchantment.DURABILITY, 1, 10);
				e.getOffers()[1] = new EnchantmentOffer(Enchantment.DURABILITY, 2, 20);
				e.getOffers()[2] = new EnchantmentOffer(Enchantment.DURABILITY, 3, 30);
			}
		}
		if(is.getItemMeta() == null) return;
		if(is.getItemMeta().getLore() == null) return;
		
		
		if(Methoden.hasCustomEnch(is)) {
			e.setCancelled(true);
			Methoden.sendActionMessage(e.getEnchanter(), "§cDas Item hat bereits verzauberungen!");
			
		}
	}

	
	@EventHandler
	public void EnchantItem(EnchantItemEvent e) {
		
		
		if(e.getInventory() instanceof EnchantingInventory) {
			
			Player p = e.getEnchanter();
			ItemStack is = e.getItem();
			ItemMeta me = is.getItemMeta();
			List<String> lore = new ArrayList<>();
			if(me.getLore() != null) {lore = me.getLore();}
			
			int cost = e.getExpLevelCost();
			double btn = e.whichButton()+1;
			double faktor = btn+(cost/8);
			

			if(Methoden.isItemStackValid(is)) {
				if(is.getType() == Material.ELYTRA) {
					e.getEnchantsToAdd().put(Enchantment.DURABILITY, (int) btn);
				}
			}
			
			String added = "";
			if(is.getType() != Material.BOOK) {
				btn*=2.0;
			}else {
				btn /=1.5;
			}
			//Alle Werkzeuge, Waffen, Rüstungen...
			if(is.getType() == Material.BOOK||EnchantmentTarget.BREAKABLE.includes(is.getType())) {
				
				//Mending
				if(Methoden.chance(3.0*btn)) {		
					me.addEnchant(Enchantment.MENDING, 1, true);
					added += "Mending ";
				}	
				//Ausbesserung
				if(Methoden.chance(3.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Ausbesserung")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Ausbesserung");
						lore.add("§7Ausbesserung " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Ausbesserung ";
					}
				}
				if(is.getType() == Material.BOOK || is.getType() == Material.ELYTRA) {
					if(Methoden.chance(6.0*btn)) {		
						int maxlvl = EnchantLimits.getEnchantLimit("Elytra-Boost");
						lore.add("§7Elytra-Boost " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Elytra-Boost ";
					}	
				}
			}
			//Angel
			if(is.getType() == Material.BOOK||EnchantmentTarget.FISHING_ROD.includes(is)) {
				//Doppelfischen
				if(Methoden.chance(12.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Doppelfischen")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Doppelfischen");
						lore.add("§7Doppelfischen " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Doppelfischen ";
					}
				}
				
			}
			
			//Werkzeug
			if(is.getType() == Material.BOOK||EnchantmentTarget.TOOL.includes(is)) {
				
				
				
				if(is.getType() == Material.BOOK||is.getType() == Material.NETHERITE_PICKAXE||is.getType() == Material.WOODEN_PICKAXE || is.getType() == Material.STONE_PICKAXE || is.getType() == Material.GOLDEN_PICKAXE || is.getType() == Material.IRON_PICKAXE || is.getType() == Material.DIAMOND_PICKAXE) {
					if(Methoden.chance(6.0*btn)) {
						if(!Methoden.hasLoreEnch(lore, "Auto-Smelt")) {
							int maxlvl = EnchantLimits.getEnchantLimit("Auto-Smelt");
							lore.add("§7Auto-Smelt " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
							added += "Auto-Smelt ";
						}
					}
					if(Methoden.chance(6.0*btn)) {
						if(!Methoden.hasLoreEnch(lore, "Veinminer") && !Methoden.hasLoreEnch(lore, "Hammer")) {
							int maxlvl = EnchantLimits.getEnchantLimit("Veinminer");
							lore.add("§7Veinminer " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
							added += "Veinminer ";
						}
					}
					
					if(Methoden.chance(6.0*btn)) {
						if(!Methoden.hasLoreEnch(lore, "Hammer") && !Methoden.hasLoreEnch(lore, "Veinminer")) {
							lore.add("§7Hammer I");
							added += "Hammer ";
						}
					}
				}
				
				if(is.getType() == Material.BOOK||is.getType() == Material.NETHERITE_AXE||is.getType() == Material.WOODEN_AXE || is.getType() == Material.STONE_AXE || is.getType() == Material.GOLDEN_AXE || is.getType() == Material.IRON_AXE || is.getType() == Material.DIAMOND_AXE) {			
					if(Methoden.chance(6.0*btn)) {
						if(!Methoden.hasLoreEnch(lore, "Timber")) {
							int maxlvl = EnchantLimits.getEnchantLimit("Timber");
							lore.add("§7Timber " + Methoden.roemisch(Methoden.getLoreEnchLvl(faktor, 1, maxlvl)));
							added += "Timber ";
						}
					}
				}
				
			}
			//Waffen + Bogen
			if(is.getType() == Material.BOOK||EnchantmentTarget.WEAPON.includes(is) || EnchantmentTarget.BOW.includes(is) || EnchantmentTarget.CROSSBOW.includes(is)) {
				//Heranziehen
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Heranziehen") && !Methoden.hasLoreEnch(lore, "Wegschleudern") && !Methoden.hasLoreEnch(lore, "Festhalten")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Heranziehen");
						lore.add("§7Heranziehen " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Heranziehen ";
					}
				}
				//Wegschleudern
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Wegschleudern") && !Methoden.hasLoreEnch(lore, "Heranziehen") && !Methoden.hasLoreEnch(lore, "Festhalten")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Wegschleudern");
						lore.add("§7Wegschleudern " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Wegschleudern ";
					}
				}
				//Festhalten
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Wegschleudern") && !Methoden.hasLoreEnch(lore, "Heranziehen") && !Methoden.hasLoreEnch(lore, "Festhalten")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Festhalten");
						lore.add("§7Festhalten " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Festhalten ";
					}
				}
			}
			
			//Waffen
			if(is.getType() == Material.BOOK||EnchantmentTarget.WEAPON.includes(is)) {
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Riesenschlag")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Riesenschlag");
						lore.add("§7Riesenschlag " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Riesenschlag ";
					}
				}
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Sturmschlag")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Sturmschlag");
						lore.add("§7Sturmschlag " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Sturmschlag ";
					}
				}
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Erstschlag")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Erstschlag");
						lore.add("§7Erstschlag " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Erstschlag ";
					}
				}
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Rüstungsdurchdringung")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Rüstungsdurchdringung");
						lore.add("§7Rüstungsdurchdringung " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Rüstungsdurchdringung ";
					}
				}
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Lebensraub")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Lebensraub");
						lore.add("§7Lebensraub " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Lebensraub ";
					}
				}
				if(Methoden.chance(2.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Verlangsamung")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Verlangsamung");
						lore.add("§7Verlangsamung " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Verlangsamung ";
					}
				}
				if(Methoden.chance(2.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Wither")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Wither");
						lore.add("§7Wither " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Wither ";
					}
				}
				//Poison
				if(Methoden.chance(2.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Vergiftung")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Vergiftung");
						lore.add("§7Vergiftung " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Vergiftung ";
					}
				}
				
			}
			//Bogen & Armbrüste
			if(is.getType() == Material.BOOK||EnchantmentTarget.BOW.includes(is) || EnchantmentTarget.CROSSBOW.includes(is)) {
				//Explosion
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Explosion")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Explosion");
						lore.add("§7Explosion "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Explosion ";
					}
				}
				//Donnerblitz
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Donnerblitz")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Donnerblitz");
						lore.add("§7Donnerblitz "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Donnerblitz ";
					}
				}
				//Härte
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Härte")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Härte");
						lore.add("§7Härte "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Härte ";
					}
				}
				//Schwebe
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Schwebe")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Schwebe");
						lore.add("§7Schwebe "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Schwebe ";
					}
				}
				//Abprallen
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Abprallen")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Abprallen");
						lore.add("§7Abprallen "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Abprallen ";
					}
				}
				//Zielsuchend
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Zielsuchend")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Zielsuchend");
						lore.add("§7Zielsuchend "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Zielsuchend ";
					}
				}
			}
			
			//Alle Rüstungs-Teile
			if(is.getType() == Material.BOOK||EnchantmentTarget.ARMOR.includes(is.getType())) {
				//Leben
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Leben")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Leben");
						lore.add("§7Leben "+Methoden.roemisch(Methoden.getLoreEnchLvl(faktor, 1, maxlvl)));
						added += "Leben ";
					}
				}
				
				//Dornschutz
				if(Methoden.chance(6.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Dornenschutz")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Dornenschutz");
						lore.add("§7Dornenschutz "+Methoden.roemisch(Methoden.getLoreEnchLvl(faktor, 1, maxlvl)));
						added += "Dornenschutz ";
					}
				}

				//Leichtigkeit
				if(Methoden.chance(4.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Leichtigkeit")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Leichtigkeit");
						lore.add("§7Leichtigkeit " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Leichtigkeit ";
					}
				}
			}
			
			//Helm
			if(is.getType() == Material.BOOK||EnchantmentTarget.ARMOR_HEAD.includes(is)) {
				//Sättigung
				if(Methoden.chance(3.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Sättigung")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Sättigung");
						lore.add("§7Sättigung " + Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Sättigung ";
					}
				}
			}
			
			//brust
			if(is.getType() == Material.BOOK||EnchantmentTarget.ARMOR_TORSO.includes(is.getType())) {
				//Doppelsprung
				if(Methoden.chance(3.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Gleiter")) {
						lore.add("§7Gleiter I");
						added += "Gleiter ";
					}
				}
			}
			
			//Hose
			if(is.getType() == Material.BOOK||EnchantmentTarget.ARMOR_LEGS.includes(is)) {
				//Schwimmboost
				if(Methoden.chance(3.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Schwimmboost")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Sättigung");
						lore.add("§7Schwimmboost "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Schwimmboost ";
					}
				}
			}
			
			//Schuhe
			if(is.getType() == Material.BOOK||EnchantmentTarget.ARMOR_FEET.includes(is.getType())) {
				//Doppelsprung
				if(Methoden.chance(3.0*btn)) {
					if(!Methoden.hasLoreEnch(lore, "Sprungkraft")) {
						int maxlvl = EnchantLimits.getEnchantLimit("Sättigung");
						lore.add("§7Sprungkraft "+Methoden.roemisch(Methoden.getLoreEnchLvl( faktor, 1, maxlvl)));
						added += "Sprungkraft ";
					}
				}
			}
			lore = Sorter.sortLoreEnchants(lore);
			me.setLore(lore);
			is.setItemMeta(me);
			
			
			if(is.getType() == Material.BOOK) {
				e.setCancelled(true);
				
				
				Map<Enchantment, Integer> toaddenchs = e.getEnchantsToAdd();
				ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
				for(Enchantment ench : toaddenchs.keySet()) {
					meta.addStoredEnchant(ench, toaddenchs.get(ench), true);
				}
				meta.setLore(lore);
				book.setItemMeta(meta);
				
				Inventory einv = e.getInventory();
				einv.setItem(0, book);
				
				if(p.getGameMode() != GameMode.CREATIVE) {
					ItemStack lapis = einv.getItem(1);
					lapis.setAmount(lapis.getAmount()-1-e.whichButton());
					if(lapis.getAmount() <= 0) {
						lapis.setType(Material.AIR);
					}
					einv.setItem(1, lapis);
					
					p.setLevel(p.getLevel()-e.whichButton()-1);
				}
				//Reset the player's enchantment seed
				try {
				    EntityHuman entityHuman = (EntityHuman) ((CraftPlayer)p).getHandle();
				    //entityHuman.enchantDone(null, 0);
				    entityHuman.enchantDone(CraftItemStack.asNMSCopy(new ItemStack(Material.BOOK)), 0);
				    entityHuman.a(StatisticList.ENCHANT_ITEM);
				    
				    
				    ContainerEnchantTable container = (ContainerEnchantTable) ((CraftInventoryView) e.getView()).getHandle();
				   
				    
				    //ContainerEnchantmentTable#enchantSlots#update()
				    Field enchantSlotsField = container.getClass().getDeclaredField("enchantSlots");
				    enchantSlotsField.setAccessible(true);
				    IInventory iinventory = ((IInventory) enchantSlotsField.get(container));
				    iinventory.update();
				    
				   
				    //ContainerEnchantmentTable#i#a(int)
				    Field containerProperty = container.getClass().getDeclaredField("i");
				    containerProperty.setAccessible(true);
				    //((ContainerProperty) containerProperty.get(container)).a(entityHuman.dN());
				    ((ContainerProperty) containerProperty.get(container)).set(entityHuman.eG());
				    
				    
				    
				    //Container#a(IInventory)
				    Method methodA = container.getClass().getMethod("a", IInventory.class);
				    methodA.setAccessible(true);
				    methodA.invoke(container, iinventory);
				} catch (Exception ex) {
				    ex.printStackTrace();
				}
			}
			
			//p.sendMessage(new NBTItem(is).asNBTString());
			
			if(added.length() > 0) {
				p.sendMessage("\n§6Folgende Verzauberungen wurden zusätzlich hinzugefügt: ");
				p.sendMessage("§a"+added);
			}
		}
	}
}
