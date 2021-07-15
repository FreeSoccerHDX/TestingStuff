package freesoccerhdx.survivalplus.haupt;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EntityHealthHandler {

	
	public static HashMap<UUID,Holo> targetholo = new HashMap<>();
	public static HashMap<UUID,LivingEntity> targetentity = new HashMap<>();
	public static HashMap<UUID,Long> targettime = new HashMap<>();
	
	
	public static void run() {
		Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {

			@Override
			public void run() {
				
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(hasTarget(p)) {
						LivingEntity le = targetentity.get(p.getUniqueId());
						long dif = System.currentTimeMillis()-targettime.get(p.getUniqueId());
						if(le.isValid() && dif < 1000*30) {
							if(p.getWorld().equals(le.getWorld())) {
								if(p.getEyeLocation().distance(le.getEyeLocation()) < 64) {
									Location spawn = le.getEyeLocation();
									Vector v = p.getEyeLocation().toVector().setY(0).subtract(spawn.toVector().setY(0)).normalize().multiply(1+le.getWidth());
									Vector av = new Vector(v.getZ(),v.getY(),-v.getX());
									spawn = spawn.add(av);
									targetholo.get(p.getUniqueId()).loc = spawn;
									targetholo.get(p.getUniqueId()).msg = "§6§l"+Math.round(le.getHealth()*10.0)/10.0 + " Leben";
									targetholo.get(p.getUniqueId())._update();
								}else {
									removeTarget(p);
								}
							}else {
								removeTarget(p);
							}
						}else {
							removeTarget(p);
						}
						
					}
				}
				
			}
			
		}, 20, 1);
	}
	
	
	public static boolean hasTarget(Player p) {
		return targetholo.containsKey(p.getUniqueId());
	}
	
	public static void removeTarget(Player p) {
		if(hasTarget(p)) {
			targetholo.get(p.getUniqueId()).despawn();
			targetholo.remove(p.getUniqueId());
			targetentity.remove(p.getUniqueId());
			targettime.remove(p.getUniqueId());
		}
	}
	
	public static void addTarget(Player p, LivingEntity target) {
		if(hasTarget(p)) {
			if(target.equals(targetentity.get(p.getUniqueId()))) {
				targettime.put(p.getUniqueId(), System.currentTimeMillis());
				return;
			}else {
				removeTarget(p);
			}
		}
		targetentity.put(p.getUniqueId(), target);
		targettime.put(p.getUniqueId(), System.currentTimeMillis());
		//target.getWidth()
		Location spawn = target.getEyeLocation();
		Vector v = p.getEyeLocation().toVector().setY(0).subtract(spawn.toVector().setY(0)).normalize().multiply(1+target.getWidth());
		Vector av = new Vector(v.getZ(),v.getY(),-v.getX());
		spawn = spawn.add(av);
		Holo h = new Holo(spawn, "§6§l"+Math.round(target.getHealth()*10.0)/10.0 +" Leben", -5, p);
		targetholo.put(p.getUniqueId(), h);
		
		
	}
	
	
	
	
}
