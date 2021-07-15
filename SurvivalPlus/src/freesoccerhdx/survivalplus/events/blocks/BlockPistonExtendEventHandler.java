package freesoccerhdx.survivalplus.events.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.util.Vector;

import freesoccerhdx.survivalplus.haupt.main;

public class BlockPistonExtendEventHandler implements Listener {

	@EventHandler
	public void onBlockPiston(BlockPistonExtendEvent e) {
		Block b = e.getBlock();
	//	b.setType(Material.DIAMOND_BLOCK);
		
		Sign sign = null;
		
		for(BlockFace bf : main.NEIGHBORS) {
			if(b.getRelative(bf).getState() instanceof Sign) {
				sign = (Sign) b.getRelative(bf).getState();
				break;
			}
		}
		if(sign != null) {
			String[] lines = sign.getLines();
			if(lines[1].equalsIgnoreCase("[Vector]")) {
				String[] vecstr = lines[2].split(",");
				double x = Double.parseDouble(vecstr[0]);
				double y = Double.parseDouble(vecstr[1]);
				double z = Double.parseDouble(vecstr[2]);
				
				Vector v = new Vector(x, y, z);
				
			
				BlockFace frontbf = e.getDirection();
				Block frontblock = b.getRelative(frontbf);
				
				for(Entity ent : b.getWorld().getNearbyEntities(frontblock.getLocation().add(.5, .5, .5), 1, 1, 1)) {
					if(ent instanceof LivingEntity) {
						ent.setVelocity(v);
					}
				}
			}
		}
		
	}
	
}
