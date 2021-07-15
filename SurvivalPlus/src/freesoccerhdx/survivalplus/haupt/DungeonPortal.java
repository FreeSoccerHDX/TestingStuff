package freesoccerhdx.survivalplus.haupt;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class DungeonPortal {

	
	
	public static boolean isDungeonPortal(Block portalenter) {
		
		Block upperright = null;
		
		if(portalenter.getRelative(1, 0, -1).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(1, 0, -1);
		}
		if(portalenter.getRelative(2, 0, -1).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(2, 0, -1);
		}
		if(portalenter.getRelative(1, 0, -2).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(1, 0, -2); 
		}
		if(portalenter.getRelative(2, 0, -2).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(2, 0, -2); 
		}
		
		if(upperright != null) {
	
			// check beacons
			if(upperright.getType() != Material.BEACON) return false;
			if(upperright.getRelative(-3, 0, +3).getType() != Material.BEACON) return false;
			if(upperright.getRelative(0, 0, +3).getType() != Material.BEACON) return false;
			if(upperright.getRelative(-3, 0, 0).getType() != Material.BEACON) return false;
			
			// check stairs
			if(upperright.getRelative(0, 0, +1).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(0, 0, +2).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			if(upperright.getRelative(-1, 0, 0).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(-2, 0, 0).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			if(upperright.getRelative(-3, 0, 1).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(-3, 0, 2).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			if(upperright.getRelative(-1, 0, 3).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(-2, 0, 3).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			// check purpur_blocks
			if(upperright.getRelative(0, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-3, -1, +3).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(0, -1, +3).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-3, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(0, -1, +1).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(0, -1, +2).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(-1, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-2, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(-3, -1, 1).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-3, -1, 2).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(-1, -1, 3).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-2, -1, 3).getType() != Material.PURPUR_BLOCK) return false;
			
			// check black_glazed_terracotta
			if(upperright.getRelative(-1, -1, 1).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			if(upperright.getRelative(-2, -1, 1).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			if(upperright.getRelative(-1, -1, 2).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			if(upperright.getRelative(-2, -1, 2).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			
			
			
			return true;
		}
		
		
		return false;
	}
	
	public static boolean isDungeonCreatePortal(Block portalenter) {
		
		Block upperright = null;
		
		if(portalenter.getRelative(1, 0, -1).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(1, 0, -1);
		}
		if(portalenter.getRelative(2, 0, -1).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(2, 0, -1);
		}
		if(portalenter.getRelative(1, 0, -2).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(1, 0, -2); 
		}
		if(portalenter.getRelative(2, 0, -2).getType() == Material.BEACON) {
			upperright = portalenter.getRelative(2, 0, -2); 
		}
		
		if(upperright != null) {
	
			// check beacons
			if(upperright.getType() != Material.BEACON) return false;
			if(upperright.getRelative(-3, 0, +3).getType() != Material.BEACON) return false;
			if(upperright.getRelative(0, 0, +3).getType() != Material.BEACON) return false;
			if(upperright.getRelative(-3, 0, 0).getType() != Material.BEACON) return false;
			
			// check stairs
			if(upperright.getRelative(0, 0, +1).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(0, 0, +2).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			if(upperright.getRelative(-1, 0, 0).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(-2, 0, 0).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			if(upperright.getRelative(-3, 0, 1).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(-3, 0, 2).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			if(upperright.getRelative(-1, 0, 3).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			if(upperright.getRelative(-2, 0, 3).getType() != Material.RED_NETHER_BRICK_STAIRS) return false;
			
			// check purpur_blocks
			if(upperright.getRelative(0, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-3, -1, +3).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(0, -1, +3).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-3, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(0, -1, +1).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(0, -1, +2).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(-1, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-2, -1, 0).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(-3, -1, 1).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-3, -1, 2).getType() != Material.PURPUR_BLOCK) return false;
			
			if(upperright.getRelative(-1, -1, 3).getType() != Material.PURPUR_BLOCK) return false;
			if(upperright.getRelative(-2, -1, 3).getType() != Material.PURPUR_BLOCK) return false;
			
			// check black_glazed_terracotta
			if(upperright.getRelative(-1, -1, 1).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			if(upperright.getRelative(-2, -1, 1).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			if(upperright.getRelative(-1, -1, 2).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			if(upperright.getRelative(-2, -1, 2).getType() != Material.BLACK_GLAZED_TERRACOTTA) return false;
			
			if(upperright.getRelative(-2, 0, 2).getType() != Material.WATER) return false;
			if(upperright.getRelative(-2, 0, 1).getType() != Material.WATER) return false;
			if(upperright.getRelative(-1, 0, 2).getType() != Material.WATER) return false;
			if(upperright.getRelative(-1, 0, 1).getType() != Material.WATER) return false;
			
			return true;
		}
		
		
		return false;
	}
	
}
