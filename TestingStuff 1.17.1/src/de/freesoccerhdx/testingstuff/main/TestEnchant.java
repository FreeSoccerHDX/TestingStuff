package de.freesoccerhdx.testingstuff.main;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class TestEnchant extends Enchantment{
	
	private String name;
	
	public TestEnchant(String name) {
		super(NamespacedKey.minecraft(name));
		this.name = name;
	}

	public Enchantment getEnchantment() {
		return Enchantment.getByKey(getKey());
	}

	public int getMaxLevel() {
		return getEnchantment().getMaxLevel();
	}

	public int getStartLevel() {
		return getEnchantment().getStartLevel();
	}

	
	public EnchantmentTarget getItemTarget() {
		return getEnchantment().getItemTarget();
	}
	
	@Override
	public boolean canEnchantItem( ItemStack item) {
		return getEnchantment().canEnchantItem(item);
	}

	
	public String getName() {
		return name;
	}

	public boolean isTreasure() {
		return getEnchantment().isTreasure();
	}

	public boolean isCursed() {
		return getEnchantment().isCursed();
	}

	public boolean conflictsWith( Enchantment other) {
		return getEnchantment().conflictsWith(other);
	}

}