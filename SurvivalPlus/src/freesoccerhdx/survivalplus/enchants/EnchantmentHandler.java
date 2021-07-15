package freesoccerhdx.survivalplus.enchants;

import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import freesoccerhdx.survivalplus.haupt.main;

public class EnchantmentHandler {


	public static boolean canEnchant(String newench, Material item) {
		
		if(newench.equals("Elytra-Boost")) {
			if(item == Material.ENCHANTED_BOOK 
					|| item == Material.ELYTRA) {
				return true;
			}
		}
		
		if(newench.equals("Ausbesserung")) {
			if(item == Material.ENCHANTED_BOOK 
					|| EnchantmentTarget.BREAKABLE.includes(item)) {
				return true;
			}
		}
		
		if(newench.equals("Doppelfischen")) {
			if(item == Material.ENCHANTED_BOOK 
					|| EnchantmentTarget.FISHING_ROD.includes(item)) {
				return true;
			}
		}
		
		if(item == Material.ENCHANTED_BOOK ||EnchantmentTarget.TOOL.includes(item)) {
			if(item == Material.ENCHANTED_BOOK 
					||item == Material.NETHERITE_PICKAXE
					||item == Material.WOODEN_PICKAXE 
					|| item == Material.STONE_PICKAXE 
					|| item == Material.GOLDEN_PICKAXE 
					|| item == Material.IRON_PICKAXE 
					|| item == Material.DIAMOND_PICKAXE) {
				if(newench.equals("Auto-Smelt")) {
					return true;
				}
				if(newench.equals("Veinminer")) {
					return true;
				}
				if(newench.equals("Hammer")) {
					return true;
				}
			}
			if(item == Material.ENCHANTED_BOOK 
					||item == Material.NETHERITE_AXE
					||item == Material.WOODEN_AXE 
					|| item == Material.STONE_AXE 
					|| item == Material.GOLDEN_AXE 
					|| item == Material.IRON_AXE 
					|| item == Material.DIAMOND_AXE) {
				if(newench.equals("Timber")) {
					return true;
				}
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.WEAPON.includes(item) 
				|| EnchantmentTarget.BOW.includes(item) 
				|| EnchantmentTarget.CROSSBOW.includes(item)) {
			if(newench.equals("Heranziehen")) {
				return true;
			}
			if(newench.equals("Wegschleudern")) {
				return true;
			}
			if(newench.equals("Festhalten")) {
				return true;
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.WEAPON.includes(item)) {
			if(newench.equals("Rüstungsdurchdringung")) {
				return true;
			}
			if(newench.equals("Lebensraub")) {
				return true;
			}
			if(newench.equals("Verlangsamung")) {
				return true;
			}
			if(newench.equals("Wither")) {
				return true;
			}
			if(newench.equals("Vergiftung")) {
				return true;
			}
			if(newench.equals("Erstschlag")) {
				return true;
			}
			if(newench.equals("Riesenschlag")) {
				return true;
			}
			if(newench.equals("Sturmschlag")) {
				return true;
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.BOW.includes(item) 
				|| EnchantmentTarget.CROSSBOW.includes(item)) {
			if(newench.equals("Explosion")) {
				return true;
			}
			if(newench.equals("Donnerblitz")) {
				return true;
			}
			if(newench.equals("Härte")) {
				return true;
			}
			if(newench.equals("Schwebe")) {
				return true;
			}
			if(newench.equals("Abprallen")) {
				return true;
			}
			if(newench.equals("Zielsuchend")) {
				return true;
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.ARMOR.includes(item)) {
			if(newench.equals("Leben")) {
				return true;
			}
			if(newench.equals("Dornenschutz")) {
				return true;
			}
			if(newench.equals("Leichtigkeit")) {
				return true;
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.ARMOR_HEAD.includes(item)) {
			if(newench.equals("Sättigung")) {
				return true;
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.ARMOR_TORSO.includes(item)) {
			if(newench.equals("Gleiter")) {
				return true;
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.ARMOR_LEGS.includes(item)) {
			if(newench.equals("Schwimmboost")) {
				return true;
			}
		}
		if(item == Material.ENCHANTED_BOOK 
				||EnchantmentTarget.ARMOR_FEET.includes(item)) {
			if(newench.equals("Sättigung")) {
				return true;
			}
		}
		
		return false;
	}
}























