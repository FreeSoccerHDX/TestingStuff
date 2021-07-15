package freesoccerhdx.survivalplus.haupt;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_16_R3.EnumItemSlot;


public class BuildersWand {

	
	
	static HashMap<UUID,List<Block>> targetblocks = new HashMap<>();
	static HashMap<UUID,List<Holo>> targetholo = new HashMap<>();
	static HashMap<UUID, Block> targetb = new HashMap<>();
	
	public static void run() {
		Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {

			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(BuildersWand.isWand(p.getInventory().getItemInMainHand())) {
						
						 Block block = p.getTargetBlock((Set<Material>) null, 5);
						
			             if (block.getType() != Material.AIR && block.getType() != Material.VOID_AIR && block.getType() != Material.CAVE_AIR && (
			            		 p.getEyeLocation().getBlock().getType() == Material.VOID_AIR
			            		 ||p.getEyeLocation().getBlock().getType() == Material.CAVE_AIR
			            		||p.getEyeLocation().getBlock().getType() == Material.WATER
			            		||p.getEyeLocation().getBlock().getType() == Material.LAVA
			            		 ||p.getEyeLocation().getBlock().getType() == Material.AIR)) {  
			            	 
			            	 
			                 List<Block> lastBlocks = p.getLastTwoTargetBlocks((Set<Material>) null, 5);
			                 BlockFace blockFace = lastBlocks.get(1).getFace(lastBlocks.get(0));
			                 Block blockNext = block.getRelative(blockFace);
			                 
			                 if (blockNext != null) {
			                	 int maxblocks = BuildersWand.calcAmountInventory(p, block.getType()); 	 
			                	 
			                	 if(maxblocks > 0) {
				                	 List<Block> blocks = BuildersWand.getWandBlocks(p, block, blockFace, maxblocks);
				                	 
				                	 if(BuildersWand.hasChanged(p,blocks) || !BuildersWand.hasTargetBlocks(p)) {
				                		 if(BuildersWand.hasTargetBlocks(p)) {
				                			 BuildersWand.clearTargetBlocks(p);
				                		 }
				                		 BuildersWand.addTargetBlocks(p,blocks,block.getType()); 
				                	 }
			                	 }else {
			                		 if(BuildersWand.hasTargetBlocks(p)) {
			             				BuildersWand.clearTargetBlocks(p);
			             			}
			                		 Methoden.sendActionMessage(p, "§cDu hast keine Blöcke dieser Art im Inventar.");
			                	 }
			                	 
			                	 
			                	
			                 }else {
			            		 if(BuildersWand.hasTargetBlocks(p)) {
			         				BuildersWand.clearTargetBlocks(p);
			         			}
			            	 }
			             }else {
			            	 if(BuildersWand.hasTargetBlocks(p)) {
			     				BuildersWand.clearTargetBlocks(p);
			     			} 
			             }
					
					}else {
						if(BuildersWand.hasTargetBlocks(p)) {
							BuildersWand.clearTargetBlocks(p);
						}
					}
				}
			}
			
		}, 20, 10);
	}
	
	public static int calcAmountInventory(Player p, Material find) {
		int i = 0;
		if(p.getGameMode() == GameMode.CREATIVE) {
			return 160;
		}
		for(int a = 0; a < 41; a++) {
			ItemStack is = p.getInventory().getItem(a);
			
			if(is != null) {
				if(is.getType() == find) {
					i += is.getAmount();
				}
			}
		}
		return Math.min(i, 160);
	}
	
	public static List<Block> getWandBlocks(Player p, Block against, BlockFace bf, int max){
		List<Block> blocks = new ArrayList<>();
		
		List<BlockFace> tosearch = new ArrayList<>();
		if(bf == BlockFace.UP || bf == BlockFace.DOWN) {
			tosearch.add(BlockFace.EAST);
			tosearch.add(BlockFace.WEST);
			tosearch.add(BlockFace.SOUTH);
			tosearch.add(BlockFace.NORTH);
		}
		if(bf == BlockFace.NORTH || bf == BlockFace.SOUTH) {
			tosearch.add(BlockFace.UP);
			tosearch.add(BlockFace.DOWN);
			tosearch.add(BlockFace.EAST);
			tosearch.add(BlockFace.WEST);
		}
		if(bf == BlockFace.EAST || bf == BlockFace.WEST) {
			tosearch.add(BlockFace.UP);
			tosearch.add(BlockFace.DOWN);
			tosearch.add(BlockFace.SOUTH);
			tosearch.add(BlockFace.NORTH);
		}
		
		
		blocks.add(against.getRelative(bf));
		
		for(int i = 0; i < 50; i++) {
			List<Block> toadd = new ArrayList<>();
			for(Block b : blocks) {
				for(BlockFace bfs : tosearch) {
					Block newblock = b.getRelative(bfs);
					
					if(newblock.getType() == Material.AIR
							||newblock.getType()==Material.CAVE_AIR
							||newblock.getType()==Material.WATER
							||newblock.getType()==Material.LAVA
							||newblock.getType()==Material.VOID_AIR) {
						if(newblock.getRelative(bf.getOppositeFace()).getType() == against.getType()) {
							if(!blocks.contains(newblock) && !toadd.contains(newblock)) {
								if(toadd.size()+blocks.size() < max) {
									toadd.add(newblock);
								}else {
									i=100;
									break;
								}
							}
						}
					}
				}
			}
			blocks.addAll(toadd);
		}
		
		
		targetb.put(p.getUniqueId(), against);
		return blocks;
	}
	
	public static boolean hasTargetBlocks(Player p) {
		 return targetblocks.containsKey(p.getUniqueId());
	}
	
	public static void clearTargetBlocks(Player p) {
		if(hasTargetBlocks(p)) {
			targetblocks.get(p.getUniqueId()).clear();
			targetblocks.remove(p.getUniqueId());
			targetb.remove(p.getUniqueId());
			
			for(Holo h : targetholo.get(p.getUniqueId())) {
				h.despawn();
			}
			targetholo.remove(p.getUniqueId());
		}
	}
	
	public static void addTargetBlocks(Player p, List<Block> blocks, Material targetmat) {
		targetblocks.put(p.getUniqueId(),blocks);
		targetholo.put(p.getUniqueId(),new ArrayList<Holo>());
		
		
		for(Block b : blocks) {
			Holo h = new Holo(b.getLocation().add(0.5, -0.3, 0.5), "", -10, p);
			h.changeEquipment(EnumItemSlot.HEAD, new ItemStack(targetmat));
			targetholo.get(p.getUniqueId()).add(h);
		}
		
	}

	public static boolean hasChanged(Player p, List<Block> blocks) {
		if(hasTargetBlocks(p)) {
			List<Block> oldblocks = targetblocks.get(p.getUniqueId());
			if(oldblocks.size() != blocks.size()) {
				return true;
			}else {
				for(Block b : oldblocks) {
					if(!blocks.contains(b)) {
						return true;
					}
				}
				for(Block b : blocks) {
					if(!oldblocks.contains(b)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isWand(ItemStack iimh) {
		
		if(iimh != null) {
			if(iimh.getType() == Material.BLAZE_ROD) {
				if(iimh.getItemMeta() != null) {
					ItemMeta meta = iimh.getItemMeta();
					if(meta.getDisplayName().equals("§6§l§oBuildersWand")) {
						return true;
					}
				}
			}
		}
		
		
		return false;
	}

	public static void placeBlocks(Player p) {
		if(hasTargetBlocks(p)) {
			List<Block> blocks = targetblocks.get(p.getUniqueId());
			
			Block against = targetb.get(p.getUniqueId());
			if(against == null) return;
			
			Material mat = against.getType();
			int placed = 0;
			
			for(Block b : blocks) {
				if(p.getInventory().contains(mat, 1) || p.getGameMode() == GameMode.CREATIVE) {
					if(b.getType() == Material.AIR || b.getType() == Material.CAVE_AIR || b.getType() == Material.VOID_AIR) {
						if(p.getGameMode() != GameMode.CREATIVE) {
							removeSingleItem(p,mat);
						}
						b.setType(mat);
						b.setBlockData(against.getBlockData());
						placed ++;
					}
				}else {
					break;
				}
			}
			p.sendMessage("§a"+placed+" Blöcke platziert.");
			clearTargetBlocks(p);
		}
	}
	
	public static void removeSingleItem(Player p ,Material mat) {
		for(int a = 0; a < 41; a++) {
			ItemStack is = p.getInventory().getItem(a);
			
			if(is != null) {
				if(is.getType() == mat) {
					is.setAmount(is.getAmount()-1);
					if(is.getAmount() == 0) {
						is.setType(Material.AIR);
					}
					p.getInventory().setItem(a, is);
					break;
				}
			}
		}
	}
	
}
















