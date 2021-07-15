package freesoccerhdx.survivalplus.haupt;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

public class Zielsuchend {

	Projectile projectile = null;
	Entity target = null;
	int stufe = 0;
	
	public Zielsuchend(Projectile projectile, int zielsuchend, Entity target) {
		this.projectile = projectile;
		this.stufe = zielsuchend;
		this.target = target;
	}
	
	
	
	
	public Entity getZielsuchendEntity() {
		return this.target;
	}
	
}
