package freesoccerhdx.survivalplus.events.blocks;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import freesoccerhdx.survivalplus.enchants.EnchantLimits;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.main;

public class BlockBreakEventHandler implements Listener {

	
	 
	 public static void breakLog(Player p, Block block) {
		  
	        if (!main.scheduledBlocks.remove(block)) return;
	        if (!Tag.LOGS.isTagged(block.getType())) return;
	       
	        BlockBreakEvent bbe = new BlockBreakEvent(block, p);
			Bukkit.getServer().getPluginManager().callEvent(bbe);
	        
			if(!bbe.isCancelled()) {
				block.breakNaturally();
			}
	    }

	 
	public static void breakVeinBlock(Player p,Material source,Block b) {
		if (!main.scheduledBlocks.remove(b)) return;
		if (source != b.getType()) return;
		
  		if(p.getFoodLevel() > 0) {
	        BlockBreakEvent bbe = new BlockBreakEvent(b, p);
			Bukkit.getServer().getPluginManager().callEvent(bbe);
	        
			if(!bbe.isCancelled()) {
				if(bbe.isDropItems()) {
					
					//b.breakNaturally(p.getInventory().getItemInMainHand());
					for(ItemStack drops : b.getDrops(p.getInventory().getItemInMainHand())) {
						p.getWorld().dropItemNaturally(p.getLocation(), drops);
					}
					
					b.setType(Material.AIR);
				}else {
					b.setType(Material.AIR);
				}
			}
  		}
	}
	
	private static HashMap<UUID,Long> lastnodurab = new HashMap<>();
	private static HashMap<UUID,Integer> blocksbreaked = new HashMap<>();
	 
	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		ItemStack item = p.getInventory().getItemInMainHand();
		Location loc = b.getLocation();
		
		
		if(b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD) {
			e.setDropItems(false);
		}
		
		//veinminer
		if(p.getGameMode() == GameMode.SURVIVAL) {
			if(item != null) {	
				int vein = Methoden.getLoreEnchLevel(item, "Veinminer");
				if(vein == 0) {
					if(blocksbreaked.containsKey(p.getUniqueId())) {
						if(blocksbreaked.get(p.getUniqueId()) > 0) {
							int breaked = blocksbreaked.containsKey(p.getUniqueId()) ? blocksbreaked.get(p.getUniqueId()): 0;
							//p.sendMessage("§aVeinminer hat "+breaked+" Blöcke abgebaut!");
						//	Methoden.sendActionMessage(p, "§aVeinminer hat "+breaked+" Blöcke abgebaut!");
							blocksbreaked.put(p.getUniqueId(), 0);
						}
					}
				}
				if(vein > 0) {
					int maxblocks = vein*10; 
					
					
					if(p.getFoodLevel() > 0) {
						if(!Methoden.chance(Math.min(maxblocks, 99))) {
							if(!main.bypass) {
								p.setFoodLevel(p.getFoodLevel()-1);
							}
							if(p.getFoodLevel() == 0) {
								if(blocksbreaked.containsKey(p.getUniqueId())) {
									if(blocksbreaked.get(p.getUniqueId()) > 0) {
										int breaked = blocksbreaked.containsKey(p.getUniqueId()) ? blocksbreaked.get(p.getUniqueId()): 0;
										//p.sendMessage("§aVeinminer hat "+breaked+" Blöcke abgebaut!");
									//	Methoden.sendActionMessage(p, "§aVeinminer hat "+breaked+" Blöcke abgebaut!");
										blocksbreaked.put(p.getUniqueId(), 0);
									}
								}
							}
						}
						
						boolean nodurab = false;
						if(!main.bypass) {
							if(item.getItemMeta() instanceof Damageable) {
								int addon = item.getEnchantmentLevel(Enchantment.DURABILITY); 
								ItemMeta meta = item.getItemMeta();
								
								if(((Damageable) meta).getDamage() >= item.getType().getMaxDurability()-10) {
									nodurab = true;
								}
								if(Methoden.chance((10/(addon+1)))) {
		
									if(((Damageable) meta).getDamage() < item.getType().getMaxDurability()-10) {
										((Damageable) meta).setDamage(((Damageable) meta).getDamage()+1);
										item.setItemMeta(meta);
										p.getInventory().setItemInMainHand(item);
									}
								}
							}
						}
						if(nodurab) {
							if(!lastnodurab.containsKey(p.getUniqueId())) {
								lastnodurab.put(p.getUniqueId(), System.currentTimeMillis());
								p.sendMessage("§cDas Item hat zu wenig Haltbarkeit für Veinminer!");
								blocksbreaked.put(p.getUniqueId(), 0);
							}
							if(System.currentTimeMillis()-lastnodurab.get(p.getUniqueId()) > 1000) {
								p.sendMessage("§cDas Item hat zu wenig Haltbarkeit für Veinminer!");
								lastnodurab.put(p.getUniqueId(), System.currentTimeMillis());
								blocksbreaked.put(p.getUniqueId(), 0);
							}
							if(blocksbreaked.containsKey(p.getUniqueId())) {
								if(blocksbreaked.get(p.getUniqueId()) > 0) {
									int breaked = blocksbreaked.containsKey(p.getUniqueId()) ? blocksbreaked.get(p.getUniqueId()): 0;
									//p.sendMessage("§aVeinminer hat "+breaked+" Blöcke abgebaut!");
									//Methoden.sendActionMessage(p, "§aVeinminer hat "+breaked+" Blöcke abgebaut!");
									blocksbreaked.put(p.getUniqueId(), 0);
								}
							}
							
						}else {
							int breaked = blocksbreaked.containsKey(p.getUniqueId()) ? blocksbreaked.get(p.getUniqueId()): 0;
							blocksbreaked.put(p.getUniqueId(), breaked +1);
							Methoden.sendActionMessage(p, "§aVeinminer hat "+breaked+" Blöcke abgebaut!");
							int breaktime = b.getType() == Material.GRAVEL || b.getType() == Material.RED_SAND || b.getType()==Material.SAND ? 1 : 3;
							
							for (BlockFace neighborFace: main.NEIGHBORS) {
								Block block = b.getRelative(neighborFace);
								if(!main.scheduledBlocks.contains(block)) {
									Material oldmat = b.getType();
									if(block.getType() == oldmat) {
										if(block.getLocation().getBlockY() >= p.getLocation().getBlockY() || p.isSneaking()) {
											main.scheduledBlocks.add(block);
											Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakVeinBlock(p, oldmat, block), breaktime);
										}
									}
								}
							}
							for (BlockFace neighborFace: main.EXTENDNEIGHBORS) {
								Block block = b.getRelative(neighborFace);
								if(!main.scheduledBlocks.contains(block)) {
									Material oldmat = b.getType();
									if(block.getType() == oldmat) {
										if(block.getLocation().getBlockY() >= p.getLocation().getBlockY() || p.isSneaking()) {
											main.scheduledBlocks.add(block);
											Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakVeinBlock(p, oldmat, block), breaktime);
										}
									}
								}
							}
							for (BlockFace neighborFace: main.EXTENDNEIGHBORS) {
								Block block = b.getRelative(BlockFace.DOWN).getRelative(neighborFace);
								if(!main.scheduledBlocks.contains(block)) {
									Material oldmat = b.getType();
									if(block.getType() == oldmat) {
										if(block.getLocation().getBlockY() >= p.getLocation().getBlockY() || p.isSneaking()) {
											main.scheduledBlocks.add(block);
											Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakVeinBlock(p, oldmat, block), breaktime);
										}
									}
								}
							}
							for (BlockFace neighborFace: main.EXTENDNEIGHBORS) {
								Block block = b.getRelative(BlockFace.UP).getRelative(neighborFace);
								if(!main.scheduledBlocks.contains(block)) {
									Material oldmat = b.getType();
									if(block.getType() == oldmat) {
										if(block.getLocation().getBlockY() >= p.getLocation().getBlockY() || p.isSneaking()) {
											main.scheduledBlocks.add(block);
											Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakVeinBlock(p, oldmat, block), breaktime);
										}
									}
								}
							}
							
						}
						
					}
					
					
					
					
				}
			}
		}
		
		
		if(p.getGameMode() == GameMode.SURVIVAL && main.blockface.containsKey(p.getName())) {
			if(item != null) {	
				int hammer = Methoden.getLoreEnchLevel(item, "Hammer");
				
				if(hammer > 0) {
					BlockFace bf = main.blockface.get(p.getName());
					main.blockface.remove(p.getName());
					List<Block> tobreak = new ArrayList<>();
					
					if(bf == BlockFace.NORTH || bf == BlockFace.SOUTH) {
						tobreak.add(b.getRelative(0, 1, 0));
						tobreak.add(b.getRelative(0, -1, 0));
						
						tobreak.add(b.getRelative(1, 1, 0));
						tobreak.add(b.getRelative(1, 0, 0));
						tobreak.add(b.getRelative(1, -1, 0));
						
						tobreak.add(b.getRelative(-1, 1, 0));
						tobreak.add(b.getRelative(-1, 0, 0));
						tobreak.add(b.getRelative(-1, -1, 0));
					}
					if(bf == BlockFace.EAST || bf == BlockFace.WEST) {
						tobreak.add(b.getRelative(0, 1, 0));
						tobreak.add(b.getRelative(0, -1, 0));
						
						tobreak.add(b.getRelative(0, 1, 1));
						tobreak.add(b.getRelative(0, 0, 1));
						tobreak.add(b.getRelative(0, -1, 1));
						
						tobreak.add(b.getRelative(0, 1, -1));
						tobreak.add(b.getRelative(0, 0, -1));
						tobreak.add(b.getRelative(0, -1, -1));
					}
					
					if(bf == BlockFace.UP || bf == BlockFace.DOWN) {
						tobreak.add(b.getRelative(1, 0, 0));
						tobreak.add(b.getRelative(0, 0, 1));
						
						tobreak.add(b.getRelative(-1, 0, 0));
						tobreak.add(b.getRelative(0, 0, -1));
						
						tobreak.add(b.getRelative(1, 0, 1));		
						tobreak.add(b.getRelative(-1, 0, -1));
						tobreak.add(b.getRelative(1, 0, -1));
						tobreak.add(b.getRelative(-1, 0, 1));
					}
					
					for(Block tb : tobreak) {
						if(tb.getType() == b.getType() && (b.getType() == Material.NETHERRACK || Methoden.getHardness(b.getType()) >= 1.0) || Methoden.getHardness(b.getType())*1.1 >= Methoden.getHardness(tb.getType())) {
							BlockBreakEvent bbe = new BlockBreakEvent(tb, p);
							Bukkit.getPluginManager().callEvent(bbe);
							if(!bbe.isCancelled() && bbe.isDropItems()) {
								tb.breakNaturally(item);
							}else if(!bbe.isDropItems() && !bbe.isCancelled()) {
								tb.setType(Material.AIR);
							}
						}
					}
				}
			
			}
		
			
			
		
			
		}
		
		if(b.getType() == Material.PLAYER_HEAD) {
			File file = new File("plugins/grabstein",b.getX()+"#"+b.getY()+"#"+b.getZ()+".yml");
			if(file.exists()){
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				String invdata = cfg.getString("invdata");
				if(invdata != null) {
					for(ItemStack is : Methoden.fromBase64(invdata).getStorageContents()) {
						if(is != null) {
							b.getWorld().dropItemNaturally(loc, is);
						}
					}
					file.delete();
				}
			}
			
		}
		

		if(Tag.LOGS.isTagged(b.getType())) {
			if(item != null) {
				if(p.isSneaking()) {
					int cutter = Methoden.getLoreEnchLevel(item, "Timber");
					if(Methoden.chance(100*cutter/EnchantLimits.getEnchantLimit("Timber"))) {
						for (BlockFace neighborFace : main.NEIGHBORS) {
							Block block = b.getRelative(neighborFace);
							if(!main.scheduledBlocks.contains(block)) {
								if(Tag.LOGS.isTagged(block.getType())) {
									main.scheduledBlocks.add(block);
									Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakLog(p, block), 1);
								}
							}
						}
						for (BlockFace neighborFace : main.EXTENDNEIGHBORS) {
							Block block = b.getRelative(neighborFace);
							if(!main.scheduledBlocks.contains(block)) {
								if(Tag.LOGS.isTagged(block.getType())) {
									main.scheduledBlocks.add(block);
									Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakLog(p, block), 1);
								}
							}
						}
						for (BlockFace neighborFace : main.EXTENDNEIGHBORS) {
							Block block = b.getRelative(BlockFace.UP).getRelative(neighborFace);
							if(!main.scheduledBlocks.contains(block)) {
								if(Tag.LOGS.isTagged(block.getType())) {
									main.scheduledBlocks.add(block);
									Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakLog(p, block), 1);
								}
							}
						}
						for (BlockFace neighborFace : main.EXTENDNEIGHBORS) {
							Block block = b.getRelative(BlockFace.DOWN).getRelative(neighborFace);
							if(!main.scheduledBlocks.contains(block)) {
								if(Tag.LOGS.isTagged(block.getType())) {
									main.scheduledBlocks.add(block);
									Bukkit.getServer().getScheduler().runTaskLater(main.m, () -> breakLog(p, block), 1);
								}
							}
						}
				    }
					/*
					int amount = Methoden.cutWood(p, p.getItemInHand(), b.getLocation(), b.getLocation().clone());
					e.setDropItems(false);
					p.sendMessage("§aDu hast " + amount + " Holzblöcke mit einmal abgebaut!");
					*/
				}
			}
		}
		
		
		
		if(b.getType() == Material.STONE 
				|| b.getType() == Material.COBBLESTONE  
						|| b.getType() == Material.IRON_ORE
								|| b.getType() == Material.RED_SAND
								|| b.getType() == Material.SAND
								|| b.getType() == Material.SANDSTONE
				|| b.getType() == Material.GOLD_ORE) {
			if(item != null && p.getGameMode() == GameMode.SURVIVAL) {
				int modif = 1;
				int autosmelt = Methoden.getLoreEnchLevel(item, "Auto-Smelt");
				int vein = Methoden.getLoreEnchLevel(item, "Veinminer");
				
				if(Methoden.chance(autosmelt*20.0)) {
					if(item.getItemMeta() != null) {
						ItemMeta meta = item.getItemMeta();
						if(meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
							modif += meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
						}
					} 
					e.setDropItems(false);
					Location spawnloc = vein > 0 ? p.getLocation() : loc;
					
					if(b.getType() == Material.RED_SAND || b.getType() == Material.SAND) {
						b.getWorld().dropItemNaturally(spawnloc, new ItemStack(Material.GLASS,1));
					}
					if(b.getType() == Material.SANDSTONE) {
						b.getWorld().dropItemNaturally(spawnloc, new ItemStack(Material.SMOOTH_SANDSTONE,1));
					}
					if(b.getType() == Material.STONE || b.getType() == Material.COBBLESTONE) {
						b.getWorld().dropItemNaturally(spawnloc, new ItemStack(Material.STONE,1));
					}
					if(b.getType() == Material.IRON_ORE) {
						b.getWorld().dropItemNaturally(spawnloc, new ItemStack(Material.IRON_INGOT,(int) (1+Math.floor(Math.random()*modif))));
					}
					if(b.getType() == Material.GOLD_ORE) {
						b.getWorld().dropItemNaturally(spawnloc, new ItemStack(Material.GOLD_INGOT,(int) (1+Math.floor(Math.random()*modif))));
					}
					
					b.getWorld().spawnParticle(Particle.FLAME, b.getLocation().add(0.5, 0.5, 0.5), 0);
					ExperienceOrb orb = loc.getWorld().spawn(spawnloc, ExperienceOrb.class);
					orb.setExperience(1);
					
				}
				
			}
		}
		
		
		
		
		
		
		if(e.getBlock().getType() == Material.SPAWNER) {
		
			
			if(item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.IRON_PICKAXE) {
				if(item.getItemMeta() != null) {
					if(item.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)){
						CreatureSpawner spawner = (CreatureSpawner) b.getState();
						
						ItemStack is = new ItemStack(Material.SPAWNER,1);
						ItemMeta meta = is.getItemMeta();
						meta.setDisplayName(spawner.getSpawnedType() + " Spawner");
						
						List<String> lore = new ArrayList<>();
						lore.add("Monster: "+ spawner.getSpawnedType());
						lore.add("Spawndelay: "+ spawner.getMinSpawnDelay()/20 + "s - " + spawner.getMaxSpawnDelay()/20 + "s");
						lore.add("Spawnrange: "+ spawner.getSpawnRange());
						lore.add("Playerrange: "+ spawner.getRequiredPlayerRange());
						lore.add("Max. nearby Monsters: "+ spawner.getMaxNearbyEntities());
						
						
						meta.setLore(lore);
						is.setItemMeta(meta);
						
						loc.getWorld().dropItemNaturally(loc, is);
					}
				}				
			}
			
		}
		
	}

/*
BlockState state = <spawnerblock>.getState();
TileEntityMobSpawner tile = ((CraftCreatureSpawner) state).getTileEntity();
MobSpawnerAbstract spawner = tile.getSpawner();
NBTTagCompound tag = new NBTTagCompound();
NBTTagCompound properties = new NBTTagCompound();
NBTTagCompound item = new NBTTagCompound();
item.setString("id", "diamond");
item.setInt("Count", 10);
properties.set("Item", item);
tag.set("Properties", properties);
MobSpawnerAbstract.a data = new MobSpawnerAbstract.a(tag, "Item");
spawner.a(data);
state.update();
*/
}

