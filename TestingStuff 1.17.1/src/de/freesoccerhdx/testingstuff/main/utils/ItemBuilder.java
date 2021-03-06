package de.freesoccerhdx.testingstuff.main.utils;

import java.io.DataOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


public class ItemBuilder {
	   private ItemStack is;
	   /**
	    * Create a new ItemBuilder from scratch.
	    * @param m The material to create the ItemBuilder with.
	    */
	   public ItemBuilder(Material m){
	     this(m, 1);
	   }
	   /**
	    * Create a new ItemBuilder over an existing itemstack.
	    * @param is The itemstack to create the ItemBuilder over.
	    */
	   public ItemBuilder(ItemStack is){
	     this.is=is;
	   }
	   /**
	    * Create a new ItemBuilder from scratch.
	    * @param m The material of the item.
	    * @param amount The amount of the item.
	    */
	   public ItemBuilder(Material m, int amount){
	     is= new ItemStack(m, amount);
	   }
	   /**
	    * Create a new ItemBuilder from scratch.
	    * @param m The material of the item.
	    * @param amount The amount of the item.
	    * @param durability The durability of the item.
	    */
	   public ItemBuilder(Material m, int amount, byte durability){
	     is = new ItemStack(m, amount, durability);
	   }
	   /**
	    * Clone the ItemBuilder into a new one.
	    * @return The cloned instance.
	    */
	   public ItemBuilder clone(){
	     return new ItemBuilder(is);
	   }
	   
	   /**
	    * Change the durability of the item.
	    * @param s The String to search in the Lore
	    * @return true if the string was found
	    */
	   public boolean scanLore(String s){
		   
		   ItemMeta meta = is.getItemMeta();
		   
		   if(meta.hasLore()) {
			   for(String s2 : meta.getLore()) {
				   if(s.contains(s2)) {
					   return true;
				   }
			   }
		   }
		   
		   return false;
	   }
	   
	   /**
	    * Change the durability of the item.
	    * @param d The Percentage durability to set it to. (Range of 0.0-1.0)
	    */
	   public ItemBuilder setDurabilityPercentage(Double d){
		   Material mat = is.getType();
		   
		   short s = (short) (d*mat.getMaxDurability());
		   
		   return setDurability(s);
	   }
	   /**
	    * Change the durability of the item.
	    * @param dur The durability to set it to.
	    */
	   public ItemBuilder setDurability(short dur){
	     //is.setDurability(dur);
	     ItemMeta meta = is.getItemMeta();
	     
	     if(meta instanceof Damageable) {
	    	 ((Damageable) meta).setDamage(dur);
	    	 is.setItemMeta(meta);
	     }
	     
	     return this;
	   }
	   /**
	    * Set the displayname of the item.
	    * @param name The name to change it to.
	    */
	   public ItemBuilder setName(String name){
	     ItemMeta im = is.getItemMeta();
	     im.setDisplayName(name);
	     is.setItemMeta(im);
	     return this;
	   }
	   /**
	    * Add an unsafe enchantment.
	    * @param ench The enchantment to add.
	    * @param level The level to put the enchant on.
	    */
	   public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
	     is.addUnsafeEnchantment(ench, level);
	     return this;
	   }
	   /**
	    * Remove a certain enchant from the item.
	    * @param ench The enchantment to remove
	    */
	   public ItemBuilder removeEnchantment(Enchantment ench){
	     is.removeEnchantment(ench);
	     return this;
	   }
	   /**
	    * Set the skull owner for the item. Works on skulls only.
	    * @param owner The name of the skull's owner.
	    */
	   public ItemBuilder setSkullOwner(String owner){
	     try{
	       SkullMeta im = (SkullMeta)is.getItemMeta();
	       im.setOwner(owner);
	       is.setItemMeta(im);
	     }catch(ClassCastException expected){}
	     return this;
	   }
	   /**
	    * Add an enchant to the item.
	    * @param ench The enchant to add
	    * @param level The level
	    */
	   public ItemBuilder addEnchant(Enchantment ench, int level){
	     ItemMeta im = is.getItemMeta();
	     im.addEnchant(ench, level, true);
	     is.setItemMeta(im);
	     return this;
	   }
	   /**
	    * Add multiple enchants at once.
	    * @param enchantments The enchants to add.
	    */
	   public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments){
	     is.addEnchantments(enchantments);
	     return this;
	   }
	   /**
	    * Sets if the Item is Unbreakable
	    */
	   public ItemBuilder setUnbreakable(boolean b) {
		   ItemMeta meta = is.getItemMeta();
		   meta.setUnbreakable(b);
		   return this;
	   }
	   
	   /*
	    * Add a ItemFlag (Hiding Enchants, Lore ...)
	    */
	   public ItemBuilder addItemFlag(ItemFlag... flag) {
		   ItemMeta meta = is.getItemMeta();
		   meta.addItemFlags(flag);
		   is.setItemMeta(meta);
		   
		   return this;
	   }
	   
	   /*
	    * Remove a ItemFlag (Hiding Enchants, Lore ...)
	    */
	   public ItemBuilder removeItemFlag(ItemFlag... flag) {
		   ItemMeta meta = is.getItemMeta();
		   meta.removeItemFlags(flag);
		   is.setItemMeta(meta);
		   
		   return this;
	   }
	   
	   /**
	    * Re-sets the lore.
	    * @param lore The lore to set it to.
	    */
	   public ItemBuilder setLore(String... lore){
	     ItemMeta im = is.getItemMeta();
	     im.setLore(Arrays.asList(lore));
	     is.setItemMeta(im);
	     return this;
	   }
	   /**
	    * Re-sets the lore.
	    * @param lore The lore to set it to.
	    */
	   public ItemBuilder setLore(List<String> lore) {
	     ItemMeta im = is.getItemMeta();
	     im.setLore(lore);
	     is.setItemMeta(im);
	     return this;
	   }
	   /**
	    * Remove a lore line.
	    * @param lore The lore to remove.
	    */
	   public ItemBuilder removeLoreLine(String line){
	     ItemMeta im = is.getItemMeta();
	     List<String> lore = new ArrayList<>(im.getLore());
	     if(!lore.contains(line))return this;
	     lore.remove(line);
	     im.setLore(lore);
	     is.setItemMeta(im);
	     return this;
	   }
	   /**
	    * Remove a lore line.
	    * @param index The index of the lore line to remove.
	    */
	   public ItemBuilder removeLoreLine(int index){
	     ItemMeta im = is.getItemMeta();
	     List<String> lore = new ArrayList<>(im.getLore());
	     if(index<0||index>lore.size())return this;
	     lore.remove(index);
	     im.setLore(lore);
	     is.setItemMeta(im);
	     return this;
	   }
	   /**
	    * Add a lore line.
	    * @param line The lore line to add.
	    */
	   public ItemBuilder addLoreLine(String line){
	     ItemMeta im = is.getItemMeta();
	     List<String> lore = new ArrayList<>();
	     if(im.hasLore())lore = new ArrayList<>(im.getLore());
	     lore.add(line);
	     im.setLore(lore);
	     is.setItemMeta(im);
	     return this;
	   }
	   /**
	    * Add a lore line.
	    * @param line The lore line to add.
	    * @param pos The index of where to put it.
	    */
	   public ItemBuilder addLoreLine(String line, int pos){
	     ItemMeta im = is.getItemMeta();
	     List<String> lore = new ArrayList<>(im.getLore());
	     lore.set(pos, line);
	     im.setLore(lore);
	     is.setItemMeta(im);
	     return this;
	   }
	 
	  
	   /**
	    * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
	    * @param color The color to set it to.
	    */
	   public ItemBuilder setLeatherArmorColor(Color color){
	     try{
	       LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
	       im.setColor(color);
	       is.setItemMeta(im);
	     }catch(ClassCastException expected){}
	     return this;
	   }
	   /**
	    * Retrieves the itemstack from the ItemBuilder.
	    * @return The itemstack created/modified by the ItemBuilder instance.
	    */
	   public ItemStack toItemStack(){
		     return is;
	   }
	   /**
	    * Retrieves the itemstack from the ItemBuilder.
	    * @return The itemstack created/modified by the ItemBuilder instance.
	    */
	   public ItemStack build(){
		     return is;
	   }
	   
	   /**
	    * Adds a Custom NBT to the ItemStack
	    * @param key Key to store the value
	    * @param value Value to store
	    */
	   public ItemBuilder addCustomNBT(String key, String value) {
		   
		   net.minecraft.world.item.ItemStack nmsitemstack = CraftItemStack.asNMSCopy(is.clone());
		   NBTTagCompound tag = nmsitemstack.getOrCreateTag();
		   tag.setString(key, value);
		   
		   
		   nmsitemstack.setTag(tag);
		   
		   is = CraftItemStack.asBukkitCopy(nmsitemstack);
		   
		   return this;
	   }
	   /**
	    * Adds a Custom NBT to the ItemStack
	    * @param key Key to check if exists
	    * @return true if the CustomNBT has the key
	    */
	   public boolean hasCustomNBT(String key) {
	   
		   net.minecraft.world.item.ItemStack nmsitemstack = CraftItemStack.asNMSCopy(is.clone());
		   NBTTagCompound tag = nmsitemstack.getOrCreateTag();
		   return tag.hasKey(key);
	   }
	   
	   /**
	    * Adds a Custom NBT to the ItemStack
	    * @param key Key which was used to store the value
	    * @return Value that is stored under that key
	    */
	   public String getCustomNBT(String key) {
		   
		   if(hasCustomNBT(key)) {
			   net.minecraft.world.item.ItemStack nmsitemstack = CraftItemStack.asNMSCopy(is.clone());
			   NBTTagCompound tag = nmsitemstack.getOrCreateTag();
			   
			   tag.getString(key);
		   }
		   
		   return "";
	   }
	   
	   /**
	    * Sets the specific Texture of the Player_Head 
	    * @param uuid Can be a random UUID. For stackable heads use always the same UUID
	    * @param texture_value The texture_value to use for the head
	    */
	   public ItemBuilder setTexture(UUID uuid, String texture_value) {
		   NBTTagCompound head = new NBTTagCompound();
		  
		   //head.setString("Id", uuid);
		   head.a("Id", uuid);
		   
		   NBTTagCompound properties = new NBTTagCompound();
		   
		   NBTTagList textures = new NBTTagList();
		   
		   NBTTagCompound valuenbt = new NBTTagCompound();
		   valuenbt.setString("Value", texture_value);
			
		   textures.add(valuenbt);
		   
		   properties.set("textures", textures);
		   
		   head.set("Properties", properties);
		   
		   net.minecraft.world.item.ItemStack nmsitemstack = CraftItemStack.asNMSCopy(is.clone());
		   NBTTagCompound tag = nmsitemstack.getOrCreateTag();
		   tag.set("SkullOwner", head);
		   
		   
		   nmsitemstack.setTag(tag);
		   
		   is = CraftItemStack.asBukkitCopy(nmsitemstack);
		   
		   Bukkit.broadcastMessage("test-> " + tag.toString());
		   return this;
		   
	   }
	   
	   
	   
	   
	   
}
