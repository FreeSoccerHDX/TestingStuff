package freesoccerhdx.survivalplus.events.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.WayPointManager;
import freesoccerhdx.survivalplus.haupt.main;
import freesoccerhdx.survivalplus.npc.NPCHandler;

public class PlayerDeathEventHandler implements Listener {

	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		
		if(WayPointManager.hasWayPoints(p)) {
			Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {

				@Override
				public void run() {
					WayPointManager.despawnWayPoints(p);
					WayPointManager.spawnWayPoints(p);
				}
				
			}, 20);
			
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setKeepLevel(true);
		e.setDroppedExp(0);
		
		Player p = e.getEntity();
		Location loc = p.getLocation();
		
		NPCHandler.unregisterPlayer(p);
		//if(true) return;
		
		if(e.getDrops().size() > 0) {
			
			List<ItemStack> drops = new ArrayList<>();
			for(ItemStack iis : e.getDrops()) {
				drops.add(iis);
			}
			
			e.getDrops().clear();
			
			Block bl = loc.getBlock();
			
			
			while(bl.getType() != Material.AIR) {
				bl = bl.getRelative(BlockFace.UP);
			}
			
			
			
			bl.setType(Material.PLAYER_HEAD);
			
			Skull s = (Skull) bl.getState();
			s.setOwner(p.getName());
			s.update();
			
			
			String invdata = Methoden.toBase64(drops);
			
			File file = new File("plugins/grabstein",bl.getX()+"#"+bl.getY()+"#"+bl.getZ()+".yml");
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.set("invdata", invdata);
			try {
				cfg.save(file);
				String wname = "";
				if(bl.getWorld().getEnvironment() == Environment.NETHER) {
					wname = "Nether";
				}else if(bl.getWorld().getEnvironment() == Environment.NORMAL) {
					wname = "Ober";
				}else if(bl.getWorld().getEnvironment() == Environment.THE_END) {
					wname = "End";
				}
				
				p.sendMessage("§4Dein Grabstein steht bei x: "+ (bl.getX()+0.5)+ " y: " + bl.getY() + " z: " + (bl.getZ()+0.5) + " in der "+wname+"-Welt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
		
		
	}
	
}
