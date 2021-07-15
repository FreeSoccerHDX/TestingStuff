package freesoccerhdx.survivalplus.turtle;

import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dropper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import freesoccerhdx.survivalplus.haupt.Methoden;

public class Turtle {

	
	Location mainblock = null;
	Location output = null;
	String creator = null;
	
	ArmorStand a1 = null;
	ArmorStand a2 = null;
	ArmorStand a3 = null;
	
	int ticks = 0;
	Long create = 0L;
			
	public Turtle(String creator, Location mainblock,Long create) {
		this.mainblock = mainblock;
		this.creator = creator;
		this.create = create;
		
		Block b = this.mainblock.getBlock();
		if(!TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.NORTH).getType())) {
			output = b.getRelative(BlockFace.NORTH).getLocation();
		}else if(!TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.SOUTH).getType())) {
			output = b.getRelative(BlockFace.SOUTH).getLocation();
		}else if(!TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.EAST).getType())) {
			output = b.getRelative(BlockFace.EAST).getLocation();
		}else if(!TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.WEST).getType())) {
			output = b.getRelative(BlockFace.WEST).getLocation();
		}
		if(output != null) {
			output = output.add(0.5, -0.7, 0.5);
			
			a1 = output.getWorld().spawn(output, ArmorStand.class);
			a1.setCustomNameVisible(true);
			a1.setCustomName("§6§lQuarry §6von §b" + creator);
			a1.setGravity(false);
			a1.setVisible(false);
			a1.setInvulnerable(true);
			a1.setAI(false);
			a1.setCanPickupItems(false);
			a1.setCollidable(false);
			a1.setMarker(false);
			
			a2 = output.getWorld().spawn(output.clone().add(0, -0.25, 0), ArmorStand.class);
			a2.setCustomNameVisible(true);
			a2.setCustomName("§6");
			a2.setGravity(false);
			a2.setVisible(false);
			a2.setInvulnerable(true);
			a2.setAI(false);
			a2.setCanPickupItems(false);
			a2.setCollidable(false);
			a2.setMarker(false);
			
			a3 = output.getWorld().spawn(output.clone().add(0, -0.5, 0), ArmorStand.class);
			a3.setCustomNameVisible(true);
			a3.setCustomName("§6");
			a3.setGravity(false);
			a3.setVisible(false);
			a3.setInvulnerable(true);
			a3.setAI(false);
			a3.setCanPickupItems(false);
			a3.setCollidable(false);
			a3.setMarker(false);
		}
	}
	
	public void destroy() {
		if(a1 != null) { a1.remove(); }
		if(a2 != null) { a2.remove(); }
		if(a3 != null) { a3.remove(); }
		
	}
	public void destroyRec() {
		if(a2 != null) { a2.remove(); }
		if(a3 != null) { a3.remove(); }
		
	}
	
	
	public boolean checkBlocks() {
		Block b = this.mainblock.getBlock();
		if(b.getType() == Material.DROPPER) {
			if(b.getRelative(BlockFace.UP).getType() == Material.CHEST) {
				if(TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.DOWN).getType())) {
					int passt = 0;
					if(TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.NORTH).getType())) {
						passt++;
					}
					if(TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.SOUTH).getType())) {
						passt++;
					}
					if(TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.EAST).getType())) {
						passt++;
					}
					if(TurtleManager.blocklist.containsKey(b.getRelative(BlockFace.WEST).getType())) {
						passt++;
					}
					if(passt != 3) {
						TurtleManager.turtels.remove(this);
						destroy();
						return false;
					}
				}else {
					TurtleManager.turtels.remove(this);
					destroy();
					return false;
				}
			}else {
				TurtleManager.turtels.remove(this);
				destroy();
				return false;
			}
		}else {
			TurtleManager.turtels.remove(this);
			destroy();
			return false;
		}
		return true;
	}
	public boolean isPlayerNear() {
		if(Bukkit.getOnlinePlayers().size() > 0) {
			return true;
			/*
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.getLocation().getWorld() == this.mainblock.getWorld()) {
					if(p.getLocation().distance(this.mainblock) <= 32.0) {
						return true;
					}
				}
			}
			*/
		}
		return false;
	}
	
	public ItemStack getTool() {
		Chest inv = (Chest) this.mainblock.clone().add(0, 1, 0).getBlock().getState();
		ItemStack[] cont = inv.getInventory().getStorageContents();
		if(cont.length > 0) {
			for(ItemStack is : cont) {
				if(is != null) {
					if(EnchantmentTarget.TOOL.includes(is)) {
						if(is.getType() == Material.WOODEN_PICKAXE || is.getType() == Material.STONE_PICKAXE || is.getType() == Material.GOLDEN_PICKAXE || is.getType() == Material.IRON_PICKAXE || is.getType() == Material.DIAMOND_PICKAXE) {
							return is;
						}
					}
				}
			}
		}
		
		
		return null;
	}
	public int getLevel(ItemStack is, Enchantment ench) {
		if(is != null) {
			if(is.getItemMeta() != null) {
				if(is.getItemMeta().hasEnchant(ench)) {
					return is.getEnchantmentLevel(ench);
				}
			}
		}
		return 0;
	}
	
	public void run() {
		Double playermod = 0.25;
		if(!isPlayerNear()) { playermod = 1.0; /* a2.setCustomName("§4Kein Spieler in der nähe!"); return;*/ }
		if(!checkBlocks()) { return; }
		ItemStack is = getTool();
		if(is == null) { 
			a2.setCustomName("§4Keine Spitzhacke gefunden!");
			return; 
		}
		
		ticks++;
		int dura = getLevel(is, Enchantment.DURABILITY);
		int effi = getLevel(is, Enchantment.DIG_SPEED);
		int luck = getLevel(is, Enchantment.LOOT_BONUS_BLOCKS);
		boolean silktouch = getLevel(is, Enchantment.SILK_TOUCH) > 0 ? true : false;
		boolean autosmelt = Methoden.getLoreEnchLevel(is, "Auto-Smelt") > 0 ? true : false;
		
		Double matmod = 1.0;
		if(is.getType() == Material.DIAMOND_PICKAXE) {matmod = 0.5;}
		if(is.getType() == Material.IRON_PICKAXE) {matmod = 0.6;}
		if(is.getType() == Material.GOLDEN_PICKAXE) {matmod = 0.7;}
		if(is.getType() == Material.STONE_PICKAXE) {matmod = 0.8;}
		if(is.getType() == Material.WOODEN_PICKAXE) {matmod = 1.0;}
		
		a2.setCustomName(Methoden.balken(ticks/(3*playermod*matmod*20.0*(30.0-effi*5.0))*100));
		a3.setCustomName("§b§lDauer: "+ Methoden.round((3*playermod*matmod*1*(30-effi*5))) +"s");
		
		if(ticks >= 3*playermod*matmod*20*(30-effi*5)) {
			Material mat = getRandomMaterial();
			Double perc = calcPercent(mat);
			
			if(Methoden.chance(perc)) {
				Dropper drop = (Dropper) this.mainblock.getBlock().getState();
				Droptable table = TurtleManager.blocklist.get(mat);
				
				int dropam =  (int) (Math.random()*(table.maxdrop-table.mindrop)+table.mindrop);
				
				if(table.luckable && luck > 0) {
					if(mat != Material.GOLD_ORE && mat != Material.IRON_ORE) {
						dropam = 1+(int) (dropam* (Math.random()*luck));
					}
				}
				
				if(silktouch) {
					drop.getInventory().addItem(new ItemStack(table.silktouch,1));
					drop.drop();
				}else if(autosmelt) {
					drop.getInventory().addItem(new ItemStack(table.smelt, dropam));
					for(int i = 0; i <= dropam; i++) {
						drop.drop();
					}
				}else {
					drop.getInventory().addItem(new ItemStack(table.normal, dropam));
					for(int i = 0; i <= dropam; i++) {
						drop.drop();
					}
				}
				
				
				
				//Bukkit.broadcastMessage("§cdrop");
			}
			ticks = 0;
		}
	}
	
	public double calcPercent(Material mat) {
		double perc = TurtleManager.blockchance.get(mat);
		int faktor = 0;
		
		Block b = this.mainblock.getBlock();
		if(b.getRelative(BlockFace.DOWN).getType() == mat) {faktor++;}
		if(b.getRelative(BlockFace.NORTH).getType() == mat) {faktor++;}
		if(b.getRelative(BlockFace.SOUTH).getType() == mat) {faktor++;}
		if(b.getRelative(BlockFace.EAST).getType() == mat) {faktor++;}
		if(b.getRelative(BlockFace.WEST).getType() == mat) {faktor++;}
		
		
		return perc*faktor*TurtleManager.blocklist.keySet().size();
	}
	
	
	private static Material getRandomMaterial() {
		Object[] mats = TurtleManager.blocklist.keySet().toArray();
		
		return (Material) mats[(int) (Math.random()*mats.length)];
	}
	
	
}















