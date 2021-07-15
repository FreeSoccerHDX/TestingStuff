package freesoccerhdx.survivalplus.haupt;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

public class RedstoneTimer {

	public static void run() {
		
		Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {

			@Override
			public void run() {
				dostuff();
			}
			
		}, 20, 20);
	}
	
	private static HashMap<Location, Integer> timer = new HashMap<>();
	
	private static void dostuff() {
		File file = new File("plugins/redstonetimer/");
		file.mkdirs();
		
		for(File f : file.listFiles()) {
			if(f.getName().endsWith(".yml")) {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
				Location loc = cfg.getLocation("location");
				int sec = cfg.getInt("timer");
				
				if(loc.getChunk().isLoaded()) {
					
					if(loc.getBlock().getType() == Material.PLAYER_HEAD
							|| loc.getBlock().getType() == Material.PLAYER_WALL_HEAD) {
						
						if(!timer.containsKey(loc)) {
							timer.put(loc, 0);
						}
						timer.put(loc, timer.get(loc)+1);
						
						if(timer.get(loc) >= sec) {
							timer.put(loc, 0);
							for(BlockFace bf : main.NEIGHBORS) {
								Block rb = loc.getBlock().getRelative(bf);
								if(rb.getBlockData() instanceof RedstoneWire) {
									RedstoneWire rw = (RedstoneWire) rb.getBlockData();
									rw.setPower(15);
									
									rb.setBlockData(rw);
									Bukkit.getScheduler().runTaskLater(main.m, ()->setunpowered(rb), 10);
									
								}
							}
						}
						
					}else {
						f.delete();
					}
				}else {
					if(timer.containsKey(loc)) {
						timer.remove(loc);
					}
				}
				
			}
		}
		
	}
	
	private static void setunpowered(Block rb) {
		if(rb.getBlockData() instanceof RedstoneWire) {
			RedstoneWire rw = (RedstoneWire) rb.getBlockData();
			rw.setPower(0);
			
			rb.setBlockData(rw);
			
		}
	}
}










