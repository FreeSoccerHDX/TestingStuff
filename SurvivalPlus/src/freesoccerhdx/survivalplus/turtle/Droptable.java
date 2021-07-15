package freesoccerhdx.survivalplus.turtle;

import org.bukkit.Material;

public class Droptable {

	
	Material normal = null;
	Material smelt = null;
	Material silktouch = null;
	int mindrop = 0;
	int maxdrop = 0;
	boolean luckable;
	
	public Droptable(Material normal, Material smelt, Material silktouch, int mindrop, int maxdrop, boolean luckable) {
		this.normal = normal;
		this.smelt = smelt;
		this.silktouch = silktouch;

		this.mindrop = mindrop;
		this.maxdrop = maxdrop;
		this.luckable = luckable;
		
	}
	
	
}
