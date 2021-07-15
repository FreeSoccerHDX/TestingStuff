package freesoccerhdx.survivalplus.haupt;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if (random.nextBoolean()) {
			
			
		    int amount = random.nextInt(4)+1;  // Amount of trees
		    for (int i = 1; i < amount; i++) {
		        int x = random.nextInt(15)+chunk.getX()*16;
		        int z = random.nextInt(15)+chunk.getZ()*16;
		        int y = world.getHighestBlockYAt(x, z);
		       
		        
		        //world.getBlockAt(x, y+10, z).setType(Material.GLOWSTONE);
		        world.generateTree(new Location(world, x,y,z), TreeType.BIRCH);
						
		    }
		}
	}
	
}
