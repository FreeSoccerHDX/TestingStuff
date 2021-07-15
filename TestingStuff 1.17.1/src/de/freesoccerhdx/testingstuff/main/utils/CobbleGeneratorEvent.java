package de.freesoccerhdx.testingstuff.main.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;

public class CobbleGeneratorEvent implements Listener {

	
	@EventHandler
	public void onFormTo(BlockFormEvent e){
		
		Block before = e.getBlock();
		BlockState newstate = e.getNewState();
		
		if(before.getType() == Material.LAVA) {
			if(newstate.getType() == Material.COBBLESTONE || newstate.getType() == Material.LEGACY_COBBLESTONE) {
				newstate.setType(Material.DIAMOND_BLOCK);
			}
		}
		
		
	}
	
	//@EventHandler
	public void onFromTo(BlockFromToEvent event){
	    Material type = event.getBlock().getType();
	    if (type == Material.WATER || type == Material.LAVA ){
	        Block b = event.getToBlock();
	        if (b.getType() == Material.AIR){
	            if (generatesCobble(type, b)){
	                /* DO WHATEVER YOU NEED WITH THE COBBLE */
	            //	Bukkit.broadcastMessage("cancel");
	             //   event.setCancelled(true);
	            	
	            //	b.setType(Material.DIAMOND_BLOCK);
	            	b.getWorld().getBlockAt(b.getLocation()).setType(Material.DIAMOND_BLOCK);
	            	
	            }
	        }
	    }
	}

	private final BlockFace[] faces = new BlockFace[]{
	        BlockFace.SELF,
	        BlockFace.UP,
	        BlockFace.DOWN,
	        BlockFace.NORTH,
	        BlockFace.EAST,
	        BlockFace.SOUTH,
	        BlockFace.WEST
	    };

	public boolean generatesCobble(Material type, Block b){
	    Material mirrorID1 = (type == Material.WATER ? Material.LAVA : Material.WATER);
	    for (BlockFace face : faces){
	        Block r = b.getRelative(face, 1);
	        if (r.getType() == mirrorID1){
	            return true;
	        }
	    }
	    return false;
	}
	
}
