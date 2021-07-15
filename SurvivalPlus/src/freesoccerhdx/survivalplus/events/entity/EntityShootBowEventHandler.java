package freesoccerhdx.survivalplus.events.entity;


import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.Zielsuchend;
import freesoccerhdx.survivalplus.haupt.main;

public class EntityShootBowEventHandler implements Listener {

	@EventHandler
	public void EntityShootBow(EntityShootBowEvent e) {
		
		if(e.isCancelled()) return;
		
		if(e.getBow() != null) {
			if(e.getBow().getType() == Material.AIR) return;
			
			ItemStack bow = e.getBow();
			Entity arrow = e.getProjectile();
			
			int explo = Methoden.getLoreEnchLevel(bow, "Explosion");
			int light = Methoden.getLoreEnchLevel(bow, "Donnerblitz");
			int härte = Methoden.getLoreEnchLevel(bow, "Härte");
			int schwebe = Methoden.getLoreEnchLevel(bow, "Schwebe");
			int aprallen = Methoden.getLoreEnchLevel(bow, "Abprallen");
			int weg = Methoden.getLoreEnchLevel(bow, "Wegschleudern");
			int heran = Methoden.getLoreEnchLevel(bow, "Heranziehen");
			int ziel = Methoden.getLoreEnchLevel(bow, "Zielsuchend");
			int fest = Methoden.getLoreEnchLevel(bow, "Festhalten");
			
			HashMap<String, Object> enchs = new HashMap<>();
			
			if(e.getForce() > 0.8) {
				enchs.put("Explosion", explo);
				enchs.put("Donnerblitz", light);
			}
			enchs.put("Härte", härte);
			enchs.put("Schwebe", schwebe);
			enchs.put("Abprallen", aprallen);
			enchs.put("Wegschleudern", weg);
			enchs.put("Heranziehen", heran);
			enchs.put("Festhalten", fest);
			
			enchs.put("Spawnloc", e.getProjectile().getLocation().clone());
			enchs.put("Geschwindigkeit", 0.8); //needed for bounce: speednerf
			
			if(härte > 0) {
				arrow.setVelocity(arrow.getVelocity().multiply(1+härte*0.1));
			}
			
			
			Entity target = null;
			
			Location start = arrow.getLocation();
			for(int i = 0; i < 50; i++){
				start = start.add(e.getEntity().getLocation().getDirection());
				Double range = Math.min((ziel+i+0.0)/4.0,16);
				range = Math.min(range,ziel/2+12);
				
				for(Entity ent : start.getWorld().getNearbyEntities(start, range,range,range)){
					if(ent instanceof LivingEntity) {
						if(ent instanceof Damageable) {
							boolean skip = false;
							if(((Projectile) arrow).getShooter() != null) {
								Entity shooter = (Entity) ((Projectile) arrow).getShooter();
								//Bukkit.broadcastMessage(e.getUniqueId()+"->"+shooter.getUniqueId());
								if(ent.getUniqueId().equals(shooter.getUniqueId())) {
									skip = true;
								}
							}
							if(!skip) {
								if(e.getEntity().hasLineOfSight(ent)) {
									if(target == null || target.getLocation().distance(start) > ent.getLocation().distance(start)) {
										
										Location shot = e.getEntity().getLocation(); // the shooter
										Location newtarget = ent.getLocation();
										
										Vector targetV = shot.toVector().subtract(newtarget.toVector()).multiply(-1); 
										Double winkel = targetV.angle(shot.getDirection())*(180/Math.PI);
										
								//		Bukkit.broadcastMessage("i:"+i+" -> w:"+winkel);
										
										if(winkel < (30+i*1.0)) {
											
											target = ent;
											
										}
									}
								}
							}
						}
					
					}
				}
				if(target != null) {
					//break;
				}
							
			}
				

			
			enchs.put("Zielsuchend", new Zielsuchend((Projectile) e.getProjectile(), ziel, target));
			
			
			
			main.arrowdata.put(arrow, enchs);
			
			
			
			
			/*
			if(bow.getItemMeta() instanceof CrossbowMeta) {
				CrossbowMeta meta = (CrossbowMeta) bow.getItemMeta();
				
				List<ItemStack> itemstacks = new ArrayList<>();
				itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));
				itemstacks.add(new ItemStack(Material.ARROW,10));
				itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));
				itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));
				itemstacks.add(new ItemStack(Material.ARROW,10));
				itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));
				itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));
				itemstacks.add(new ItemStack(Material.ARROW,10));
				itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));

				meta.setChargedProjectiles(itemstacks);
				bow.setItemMeta(meta);
				
				Skeleton ent = (Skeleton) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SKELETON);
				e.getEntity().swingMainHand();
				
				Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {

					@Override
					public void run() {
						List<ItemStack> itemstacks = new ArrayList<>();
						itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));
						itemstacks.add(new ItemStack(Material.ARROW,10));
						itemstacks.add(new ItemStack(Material.FIREWORK_ROCKET,10));

						meta.setChargedProjectiles(itemstacks);
						bow.setItemMeta(meta);
					}
					
				}, 1); 
				
				
			}
			*/
			
		}
		
	}
	
}

















