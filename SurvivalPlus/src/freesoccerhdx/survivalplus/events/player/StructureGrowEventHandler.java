package freesoccerhdx.survivalplus.events.player;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import freesoccerhdx.survivalplus.haupt.Methoden;

public class StructureGrowEventHandler implements Listener {

	Material[] mats = new Material[] {Material.DIAMOND_ORE,Material.COAL_ORE,Material.EMERALD_ORE,Material.GOLD_ORE,Material.IRON_ORE,Material.LAPIS_ORE,Material.REDSTONE_ORE,Material.NETHER_QUARTZ_ORE};
	
	@EventHandler
	public void StructureGrow(StructureGrowEvent e) {
		List<BlockState> blocks = e.getBlocks();
		Location loc = e.getLocation();
		TreeType tt = e.getSpecies();
		//Player p = e.getPlayer();
		
		if(tt.equals(TreeType.BROWN_MUSHROOM) || tt.equals(TreeType.RED_MUSHROOM)) {
			for(BlockState bs : blocks) {
				if(Methoden.chance(50)) {
					if(bs.getType().equals(Material.BROWN_MUSHROOM_BLOCK) || bs.getType().equals(Material.RED_MUSHROOM_BLOCK)) {
						bs.setType(mats[(int) (Math.random()*mats.length)]);		
					}
				}
			}
		}
		
	}
	
	
	
	
}
