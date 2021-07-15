package de.freesoccerhdx.testingstuff.main.utils;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;


public class ClickHologram{

	
	
	/**
	 * Determines the point of intersection between a plane defined by a point and a normal vector and a line defined by a point and a direction vector.
	 *
	 * @param planePoint    A point on the plane.
	 * @param planeNormal   The normal vector of the plane.
	 * @param linePoint     A point on the line.
	 * @param lineDirection The direction vector of the line.
	 * @return The point of intersection between the line and the plane, null if the line is parallel to the plane.
	 */
	public static Vector lineIntersection(Vector planePoint, Vector planeNormal, Vector linePoint, Vector lineDirection) {
	    if (planeNormal.dot(lineDirection.normalize()) == 0) {
	        return null;
	    }
	    

	    double t = (planeNormal.dot(planePoint) - planeNormal.dot(linePoint)) / planeNormal.dot(lineDirection.normalize());
	    
	    if(t <= 0) return null;
	    
	    return linePoint.add(lineDirection.normalize().multiply(t));
	}
	
	private static Vector getNormalVector(Vector a, Vector b) {
		
		double x = a.getY() * b.getZ() - a.getZ() * b.getY();
		double y = a.getZ() * b.getX() - a.getX() * b.getZ();
		double z = a.getX() * b.getY() - a.getY() * b.getX(); 
		
		return new Vector(x,y,z);
	}
	
	
	public static HashMap<String, List<ClickHologram>> player_clickholograms = new HashMap<>();
	
	private static void addPlayer(Player p, ClickHologram ch) {
		if(!player_clickholograms.containsKey(p.getName())) {
			player_clickholograms.put(p.getName(), new ArrayList<>());
		}
		
		player_clickholograms.get(p.getName()).add(ch);
		
	}
	
	public static void removeAllPlayerClickHolos(Player p) {
		if(player_clickholograms.containsKey(p.getName())) {
			player_clickholograms.remove(p.getName());
		}
	}
	private static void removePlayer(Player p, ClickHologram ch) {
		if(player_clickholograms.containsKey(p.getName())) {
			player_clickholograms.get(p.getName()).remove(ch);
		}
		
	}
	
	private static String removeColorCodes(String s) {
		s = s.replace("§0", "");
		s = s.replace("§1", "");
		s = s.replace("§2", "");
		s = s.replace("§3", "");
		s = s.replace("§4", "");
		s = s.replace("§5", "");
		s = s.replace("§6", "");
		s = s.replace("§7", "");
		s = s.replace("§8", "");
		s = s.replace("§9", "");
		s = s.replace("§a", "");
		s = s.replace("§b", "");
		s = s.replace("§c", "");
		s = s.replace("§d", "");
		s = s.replace("§e", "");
		s = s.replace("§f", "");
		s = s.replace("§l", "");
		s = s.replace("§o", "");
		s = s.replace("§m", "");
		s = s.replace("§n", "");
		
		return s;
	}
	
	public static void checkClick(Player p) {
		if(!player_clickholograms.containsKey(p.getName())) return;
		
		// TODO: Check all CLickHolograms for that Player
		for(ClickHologram ch : player_clickholograms.get(p.getName())) {
			
			Location loc = ch.getLocation();
			String name = ch.getArmorStandName();
			String nocolor_name = removeColorCodes(name);
			
			double namedistance = nocolor_name.length()*.145;
			if(name.contains("§l")) {
				namedistance = namedistance*1.1;
			}
			
			
			Location ploc = p.getEyeLocation();
			Vector pvec = p.getEyeLocation().getDirection();
			
			Vector normalvector_ebene = getNormalVector(new Vector(0,1,0), new Vector(-pvec.getZ(),0,pvec.getX()));
			
			Vector wherehit = lineIntersection(loc.toVector(), normalvector_ebene, ploc.toVector(), pvec);
			
			
			Vector totheleft = new Vector(pvec.getZ(), 0.45, -pvec.getX()).normalize().multiply(namedistance/2.0);
			totheleft = loc.clone().toVector().add(totheleft);
			
		//	p.getWorld().spawnParticle(Particle.ASH, new Location(p.getWorld(),totheleft.getX(),totheleft.getY(),totheleft.getZ()), 10);
			
			if(wherehit != null) {
				
				double distance = wherehit.clone().setY(0.0).distance(loc.toVector().clone().setY(0.0));
			//	Bukkit.broadcastMessage("distance: " + distance);
			//	Bukkit.broadcastMessage("namedis: " + namedistance);
				
				
				
				
				
				if(distance < namedistance/2.0) {
					Location where = new Location(p.getWorld(),wherehit.getX(),wherehit.getY(),wherehit.getZ());
					double totaldis = totheleft.clone().setY(0).distance(wherehit.clone().setY(0)); // distance from the left name
					double height_dis = where.getY()-loc.getY()-0.45;
				//	Bukkit.broadcastMessage("heightdis: " + height_dis);
					
				//	p.getWorld().spawnParticle(Particle.ASH, new Location(p.getWorld(),totheleft.getX(),totheleft.getY(),totheleft.getZ()), 10);
					
					if(height_dis >= 0.0 && height_dis <= 0.27) {
							
					//	Bukkit.broadcastMessage("totaldis: " + totaldis);
					//	Bukkit.broadcastMessage("max: " + namedistance);
						
						String[] planetext = nocolor_name.split(" ");
						
						double index = 0;
						double target_index = (nocolor_name.length() * (totaldis/namedistance));
						String target_word = "";
						
						for(String s : planetext) {
							double last = index;
							index += s.length();
							
							if(target_index >= last && target_index < index) {
								target_word = s;
								break;
							}
							
							index += 1;
							
						}
						
						
						
						
						for(HoloClickListener hcl : ch.getListener()) {
							hcl.click(p, where, loc.clone(), target_word, distance, totaldis/namedistance);
						}
					
					}
				}
			
			}
		}
	}
	
	
	
	private ArmorStand armorstand = null;
	private List<HoloClickListener> listener = new ArrayList<>();
	
	private Location custom_loc = null;
	private String custom_name = null;
	
	public ClickHologram(Location eyeloc, String name) {
		custom_loc = eyeloc;
		custom_name = name;
	}
	
	public ClickHologram(ArmorStand as) {
		armorstand = as;
	}
	
	public void addClickListener(HoloClickListener cl) {
		listener.add(cl);
	}
	public void addPlayer(Player p) {
		addPlayer(p, this);
	}
	public void removePlayer(Player p) {
		removePlayer(p,this);	
	}
	
	public Location getLocation() {
		if(armorstand == null) {
			return custom_loc;
		}
		return armorstand.getEyeLocation(); 
		//armorstand.loc
	}
	public String getArmorStandName() {
		if(armorstand == null) {
			return custom_name;
		}
		return armorstand.getName();
	}
	
	public List<HoloClickListener> getListener(){
		return listener;
	}
	
	
	public static abstract class HoloClickListener{
		
		public HoloClickListener() {
		}
		
		public abstract boolean click(Player p, Location where, Location eyeposArmorStand,String targetword, double distance_from_middle, double distance_from_left_perc);
		
	}
	
	
}
