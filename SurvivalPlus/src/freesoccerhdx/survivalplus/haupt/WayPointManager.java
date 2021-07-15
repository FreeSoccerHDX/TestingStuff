package freesoccerhdx.survivalplus.haupt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WayPointManager {

	public static HashMap<UUID,ArrayList<WayPoint>> waypoints = new HashMap<>();
	public static HashMap<UUID,World> waypointworlds = new HashMap<>();
	
	public static boolean spawnWayPoints(Player p) {
		waypointworlds.put(p.getUniqueId(), p.getWorld());
		File dir = new File("plugins/WayPoints/");
		dir.mkdirs();
		
		File file = new File("plugins/WayPoints/"+p.getUniqueId().toString()+".cfg");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> wp = getWayPointNames(p);
		
		if(wp.size() > 0) {
			waypoints.put(p.getUniqueId(), new ArrayList<>());
			
			for(String name : wp) {
				Location loc = cfg.getLocation(name).clone().add(0, 1.5, 0);
				
				if(loc != null) {
					if(loc.getWorld().equals(p.getWorld())) {
						Location spawnloc = calculateWayPointPosition(p,loc.clone());	
						WayPoint pwp = new WayPoint(loc,spawnloc, name, p);
						Double distance = p.getEyeLocation().toVector().distance(pwp.origloc.toVector());
						pwp.update(null, pwp.origname+" §a- "+ Methoden.round(distance) + "m");
						
						waypoints.get(p.getUniqueId()).add(pwp);
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	public static Location calculateWayPointPosition(Player p, Location real) {
		
		Vector dif = p.getEyeLocation().toVector().subtract(real.clone().toVector()).normalize().multiply(9);
		Location place = p.getEyeLocation().subtract(dif).add(p.getVelocity().multiply(4));
		place.setWorld(real.getWorld());
		
		if(p.getEyeLocation().toVector().distance(real.clone().toVector()) < 10) {
			return real.clone();
		}
		
		

		return place.clone();
	}
	public static void despawnWayPoints(Player p) {
		
		if(waypoints.containsKey(p.getUniqueId())) {
			waypointworlds.remove(p.getUniqueId());
			List<WayPoint> pwp = waypoints.get(p.getUniqueId());
			for(WayPoint wp : pwp) {
				wp.despawn();
			}
			waypoints.remove(p.getUniqueId());
		}
	}
	
	public static void updateWayPoints(Player p) {
		if(waypoints.containsKey(p.getUniqueId())) {
			if(waypointworlds.get(p.getUniqueId()) != p.getWorld()) {
				despawnWayPoints(p);
				spawnWayPoints(p);
			}
			
			List<WayPoint> pwp = waypoints.get(p.getUniqueId());
			List<WayPoint> updated = new ArrayList<>();
			
			for(WayPoint wp : pwp) {
				
				Double distance = p.getEyeLocation().toVector().distance(wp.origloc.toVector());
				Location spawnloc = calculateWayPointPosition(p,wp.origloc); 
				
				for(WayPoint uwp : updated) {
					if(uwp.loc.getWorld().equals(wp.loc.getWorld())) {
						Vector a = uwp.loc.toVector();
						Vector b = spawnloc.toVector();
						
					//	p.sendMessage(Math.abs(a.getY()-b.getY()) + "");
						if(Math.abs(a.getY()-b.getY()) < 0.3) {
							
							if(b.getY() < a.getY()) {
								spawnloc = spawnloc.add(0, -0.25, 0);
							}else {
								spawnloc = spawnloc.add(0, 0.25, 0);
							}
								
						}
					}else {
						uwp.despawn();
					}
				}
				
				
				wp.update(spawnloc, wp.origname+" §a- "+ Methoden.round(distance) + "m");
				
				updated.add(wp);
				
			}
		}
	}
	public static boolean hasWayPoints(Player p) {
		return waypoints.containsKey(p.getUniqueId());
	}
	public static List<String> getWayPointNames(Player p) {
		File dir = new File("plugins/WayPoints/");
		dir.mkdirs();
		
		File file = new File("plugins/WayPoints/"+p.getUniqueId().toString()+".cfg");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> wp = (List<String>) cfg.getList("waypoints");
		
		if(wp != null) {
			return wp;
		}
		
		return new ArrayList<>();
	}
	public static boolean removeWayPoint(Player p, String name) {
		File dir = new File("plugins/WayPoints/");
		dir.mkdirs();
		
		File file = new File("plugins/WayPoints/"+p.getUniqueId().toString()+".cfg");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> wp = getWayPointNames(p);
		boolean sucess = wp.remove(name);
		
		cfg.set("waypoints", wp);
		cfg.set(name, null);
		
		try {
			cfg.save(file);
		}catch(Exception ex) {
		}
		if(hasWayPoints(p)) {
			despawnWayPoints(p);
			spawnWayPoints(p);
		}
		return sucess;
	}
	public static boolean addWayPoint(Player p, String name) {
		File dir = new File("plugins/WayPoints/");
		dir.mkdirs();
		
		File file = new File("plugins/WayPoints/"+p.getUniqueId().toString()+".cfg");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> wp = getWayPointNames(p);
		boolean sucess = wp.add(name);
		if(sucess) {

			cfg.set("waypoints", wp);
			cfg.set(name, p.getLocation());
			try {
				cfg.save(file);
			}catch(Exception ex) {
			}
			if(hasWayPoints(p)) {
				despawnWayPoints(p);
				spawnWayPoints(p);
			}
			return true;
		}
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
