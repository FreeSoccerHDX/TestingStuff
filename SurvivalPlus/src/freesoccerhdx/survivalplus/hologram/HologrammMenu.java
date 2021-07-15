package freesoccerhdx.survivalplus.hologram;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import freesoccerhdx.survivalplus.haupt.Hologramm;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.main;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;

public class HologrammMenu {

	

	public static boolean interact(Player p) {
		if(getMenu(p) != null){
			HologrammMenu menu = HologrammMenu.getMenu(p);
			List<HHolo> lines = menu.getLines();
	
			Vector hit = menu.getHitSurfaceLocation();
			if(hit != null) {
				double ymax = lines.get(0).getLocation().getY() + 0.3 + 0.2; //0.3 ist der abstand vom namen bis zur "richtigen" location des Armorstands
				double ymin = lines.get(lines.size() - 1).getLocation().getY() + 0.3;
				
				if (hit.getY() <= ymax && hit.getY() >= ymin) {
					HHolo nearest = null;
		
					for (HHolo h : lines) {
						if (hit.getY() <= ymax && hit.getY() >= ymax - 0.2) {
							nearest = h;
							break;
						} else {
							ymax -= HologrammMenu.DIFF;
						}
					}
					if(nearest != null) {
						Double cdis = hit.clone().setY(0.0).distance(nearest.getLocation().clone().toVector().setY(0.0));
						if(cdis <= (1.0/7.0)*(Methoden.removeColorCodes(nearest.line).length()/2.0)) { // durch 2 weil 2 seiten aka. 2 richtungen von mittelpunkt
							if(nearest.getClickEvent() != null) {
								nearest.getClickEvent().click(p);
								return true;  //somit kann interact event abgebrochen werden -> blöcke können nicht kaputt gehen; kisten nicht geöffnet etc.
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	//returns the location where the player (could have) hit the hologram (surface)
	public Vector getHitSurfaceLocation(){
		if(lines.size() > 0) {
			Vector p = player.getLocation().getDirection(); //Richtungsvektor der Gerade (aka. Blickrichtung des Spielers)
			Vector p_ = p.setY(0); //Normalvektor der Ebene
			Vector z = lines.get(0).getLocation().toVector().setY(0); //Stützvektor der Ebene (aka. Oberfläche des Holograms)
			Vector l = player.getLocation().toVector().setY(0); //Stützvektor der Geraden (aka. Position des Spielers)
			Vector d = z.subtract(l); // Distanz zwischen den Stützvektoren
			
			Vector a = new Vector(p_.getZ(),0,-p_.getX()); //orthogonal zu p_
			
			//We solve: l + (alpha)*p_ = z + (beta)*a
			
			double[][] A = {
					{p_.getX(), -a.getX()},
					{p.getZ(),-a.getZ()}};
			
			double[] b = {d.getX(), d.getZ()};
			
			double[] results = LGS.solve(A, b);
			
			if(results[0] < 0.0) {
				return null;
			}
			
			return player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(results[0]));
		}else {
			return null;
		}
	}
	private static HashMap<Player, HologrammMenu> playermenus = new HashMap<>();
	
	public static HologrammMenu getMenu(Player p) {
		if(playermenus.containsKey(p)) {
			return playermenus.get(p);
		}
		return null;
	}
	public static void addHologrammMenu(Player p, HologrammMenu menu) {
		playermenus.put(p, menu);
	}
	public static void removeHologrammMenu(Player p) {
		if(playermenus.containsKey(p)) {
			playermenus.get(p).despawn();
			playermenus.remove(p);
		}
	}
	
	
	public static final double DIFF = 0.26;
	
	private Player player = null;
	private Double dis = 0.0;
	private Location center = null;
	private long lifeticks = -1L;
	private Double radius = 0.0;
	private HoloPosition position = null;
	private boolean hasspawned = false;
	private Entity centerentity = null;
	
	//private HashMap<Integer, ClickHologramm> lines = new HashMap<>();
	private List<HHolo> lines = new ArrayList<>();
	private BukkitTask task = null;
	
	public HologrammMenu(Player p, long lifeticks, Location center, Double distance){
		player = p;
		dis = distance;
		this.center = center;
		this.lifeticks = lifeticks;
		
		task = Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {

			@Override
			public void run() {
				tick();
			}
			
		},0, 1);
		
	}
	public void onEnable() {
		//aka spawn()
	}
	public void onDisable() {
		//aka despawn()
	}
	
	public void setRadius(Double radius, HoloPosition position) {
		this.radius = radius;
		this.position = position;
	}
	public Location getCenter() {
		return center;
	}
	public void addLine(String line, ClickHologramm click) {
		lines.add(new HHolo(line,click,lines.size()));
	}
	public void removeLine(int index) {
		lines.remove(index);
	}
	public void clearLines() {
		lines.clear();
	}
	public List<HHolo> getLines() {
		return lines;
	}
	public Player getPlayer() {
		return player;
	}
	public void move(Location tomove) {
		Location newloc = tomove.clone();
		
		for(HHolo h : lines) {
			newloc = newloc.add(0, -DIFF, 0);
			EntityArmorStand stand = h.hologramm.getArmorStand();
			stand.setLocation(newloc.getX(), newloc.getY(), newloc.getZ(), 0.0F, 0.0F);
			h.hologramm.setLocation(newloc.clone());
			h.loc = newloc.clone(); 

			((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(stand));
		}
	}
	public void teleport(Location center) {
		setCenter(center);
		move(center);
	}
	public void setCenter(Location center) {
		this.center = center;
	}
	public void setCenterEntity(Entity centerentity) {
		this.centerentity = centerentity;
	}
	public void spawn() {
		Location hloc = center.clone();
		spawn(hloc);
	}
	public void spawn(Location hololoc) {
		if(!hasspawned) {
			onEnable();
			hasspawned = true;
			Location hloc = hololoc.clone();
			for(HHolo h : lines) {
				Hologramm hologramm = new Hologramm(hloc.add(0, -DIFF, 0), h.line, 0, false, false);
				hologramm.addPlayer(player);
				h.hologramm = hologramm;
				hologramm.spawn();
				h.loc = hloc.clone();
			}
			if(playermenus.containsKey(player)) {
				removeHologrammMenu(player);
			}
			addHologrammMenu(player, this);
		}
	}
	private void despawn() {
		onDisable();
		for(HHolo h : lines) {
			h.hologramm.destroy();
		}
		lines.clear();
		task.cancel();
	}
	
	private void tick() {
		if((player.getWorld() != center.getWorld()) || (!player.isOnline()) || player.getLocation().distance(center) > dis) {
			removeHologrammMenu(player);
			return;
		}
		if(lifeticks > 0) {
			lifeticks--;
			if(lifeticks == 0) {
				removeHologrammMenu(player);
				return;
			}
		}
		if(centerentity != null) {
			if(centerentity.isValid()) {
				this.center = centerentity.getLocation().add(0, 2.0, 0);
			}else {
				removeHologrammMenu(player);
				return;
			}
		}
		
		
		if(position != null) {
			Vector pv = getPlayer().getLocation().toVector().setY(0.0);
			Vector hv = getCenter().toVector().setY(0.0);
			
			Vector lv = pv.clone().subtract(hv.clone()).normalize().multiply(radius);
			
			
			if(position == HoloPosition.NORMAL) {
				move(getCenter().clone());
			}else if(position == HoloPosition.FRONT) {
				move(getCenter().clone().add(lv));
			}else if(position == HoloPosition.BEHIND) {
				move(getCenter().clone().add(new Vector(-lv.getX(),0.0,-lv.getZ())));
			}else if(position == HoloPosition.RIGHT) {
				Location newpos = getCenter().clone().add(new Vector(lv.getZ(),0.0,-lv.getX())); 
				//Vector newpv = pv.clone().setY(0.0).subtract(newpos.clone().toVector().setY(0.0).clone()).normalize().multiply(newpos.toVector().distance(player.getEyeLocation().toVector())-3.0);
				Vector newpv = pv.clone().setY(0.0).subtract(newpos.clone().toVector().setY(0.0).clone()).normalize().multiply(1.6);
				Location finalloc = newpos.add(newpv);
				
				
				//newpv = newpv.multiply(this.radius/3);
				move(finalloc);
			}else if(position == HoloPosition.LEFT) {
				move(getCenter().clone().add(new Vector(-lv.getZ(),0.0,lv.getX())));
			}
			
		}
	}
	

	
	public enum HoloPosition{	
		NORMAL("normal"),
		FRONT("front"),
		RIGHT("right"),
		LEFT("left"),
		BEHIND("behind");
		
		
		private String type;
		HoloPosition(String type) {
			this.type = type;
		}
		public String getTypeName() {
			return type;
		}
		
	}
	
	public class HHolo{
		String line = "";
		ClickHologramm click = null;
		int zeile = -1;
		Location loc = null;
		Hologramm hologramm = null;
		
		public HHolo(String line, ClickHologramm click, int zeile) {
			this.line = line;
			this.click = click;
			this.zeile = zeile;
		}
		public Location getLocation() {
			return loc;
		}
		public ClickHologramm getClickEvent() {
			return click;
		}
		public String getName() {
			return line;
		}
	}



}