package freesoccerhdx.survivalplus.haupt;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WayPoint {

	public Location origloc = null;
	public Location loc = null;
	public Holo holo = null;
	public String name = "";
	public String origname = "";
	public boolean spawned = false;
	public Player p = null;
	
	public WayPoint(Location origloc, Location spawnloc, String name, Player p) {
		this.loc = spawnloc;
		this.origloc = origloc;
		this.name = name;
		this.origname = name;
		
		Holo holo = new Holo(this.loc, this.name, -100, p);
		this.holo = holo;
		this.spawned = true;
		this.p = p;
		
	}
	public void update(Location loc, String name) {
		if(loc != null) {
			this.loc = loc;
			this.holo.loc = loc;
		}
		if(name != null && name != "") {
			this.name = name;
			this.holo.msg = name;
		}
		this.holo._update();
	}
	
	public void despawn() {
		this.holo.despawn();
		this.spawned = false;
	}
	public void respawn() {
		Holo holo = new Holo(this.loc, this.name, -100, this.p);
		this.holo = holo;
		this.spawned = true;
	}
	
}
