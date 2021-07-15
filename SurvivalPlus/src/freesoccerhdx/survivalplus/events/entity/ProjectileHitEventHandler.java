package freesoccerhdx.survivalplus.events.entity;

import java.util.HashMap;



import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.material.TrapDoor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.main;
import net.minecraft.server.v1_16_R3.EntityLightning;

public class ProjectileHitEventHandler implements Listener {

	@EventHandler
	public void ProjectileHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		Location loc = proj.getLocation();
		
		
		
		
		if(main.arrowdata.containsKey(proj)) {
			HashMap<String,Object> data = main.arrowdata.get(proj);
			Object a1 = Methoden.getProjDataKey(data, "Explosion");
			Object a2 = Methoden.getProjDataKey(data, "Donnerblitz");
			Object a3 = Methoden.getProjDataKey(data, "Härte");
			Object a4 = Methoden.getProjDataKey(data, "Schwebe");
			Object a5 = Methoden.getProjDataKey(data, "Abprallen");
			Object a6 = Methoden.getProjDataKey(data, "Geschwindigkeit");
			Object a7 = Methoden.getProjDataKey(data, "Heranziehen");
			Object a8 = Methoden.getProjDataKey(data, "Wegschleudern");
			Object a9 = Methoden.getProjDataKey(data, "Spawnloc");
			Object a10 = Methoden.getProjDataKey(data, "Festhalten");
			
			
			Location spawnloc = null;
			if(a9 != null) spawnloc= (Location) a9;
			
			int explo = 0;
			if(a1 != null) explo = (int) a1;
			int blitz = 0;
			if(a2 != null) blitz = (int) a2;
			int härte = 0;
			if(a3 != null) härte = (int) a3;
			int schwebe = 0;
			if(a4 != null) schwebe = (int) a4;
			int bounce = 0;
			if(a5 != null) bounce = (int) a5;
			Double speed = 0.0;
			if(a6 != null) speed = (double) a6;
			int heranziehen = 0;
			if(a7 != null) heranziehen = (int) a7;
			int wegschleudern = 0;
			if(a8 != null) wegschleudern = (int) a8;
			
			int fest = 0;
			if(a10 != null) fest = (int) a10;
			
			if(bounce > 0) {
				if(e.getHitEntity() == null) {
					Projectile bounced = doBounce(proj, bounce, speed);
					
					if(bounced != null) {
						
						HashMap<String, Object> enchs = data;
						enchs.remove("Abprallen");
						enchs.put("Abprallen", bounce-1);
						
						main.arrowdata.put(bounced,enchs);
						
						main.arrowdata.remove(proj);
						proj.remove();
						
						return;
					
					}
				}
			}
			
			if(e.getHitEntity() != null) {
				if(e.getHitEntity() instanceof HumanEntity) {
					if(((HumanEntity) e.getHitEntity()).isBlocking()) {
						return;
					}
				}
			}
			
			if(explo > 0) {
				//loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), (float) (explo*0.3), false, false);
				
				loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
				loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
				
				for(Entity enear : loc.getWorld().getNearbyEntities(loc, 3.0, 3.0, 3.0)) {
					if(enear instanceof LivingEntity) {
						((LivingEntity) enear).damage(0.6*explo);
						Vector between = enear.getLocation().toVector().subtract(loc.toVector()).multiply(0.4);
						
						enear.setVelocity(between.setY(between.getY()*2));
					}
				}
			}
			if(blitz > 0) {
				if(proj.getShooter() != null) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						if(p.getWorld().equals(loc.getWorld())) {
							if(p.getLocation().add(0, -p.getLocation().getY(), 0).distance(loc.clone().add(0, -loc.getY(), 0)) < 64) {
								
								p.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
								p.getWorld().strikeLightningEffect(loc);
								/*EntityLightning is = new EntityLightning(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(),loc.getY(), loc.getZ(), false);
								new Entityligh
								
								 PacketPlayOutSpawnEntityWeather sp = new PacketPlayOutSpawnEntityWeather(is);
								((CraftPlayer)p).getHandle().playerConnection.sendPacket(sp);*/
							}
						}
					}
				}
				//loc.getWorld().strikeLightningEffect(loc);

				for(Entity ent : loc.getWorld().getNearbyEntities(loc, 2,2,2)){
					
					if(ent instanceof Damageable) {
						((Damageable) ent).damage(blitz*0.8, (Entity) proj.getShooter());
					}
				}
			}
			if(e.getHitEntity() != null && e.getHitEntity() instanceof LivingEntity) {
				if(e.getHitEntity() instanceof Player) {
					if(fest > 0) {
						main.festhalten.put(e.getHitEntity().getUniqueId(),System.currentTimeMillis()+80*fest);
					}
				}
				if(spawnloc != null) {
					if(wegschleudern > 0) {
						Vector dir = spawnloc.toVector().subtract(e.getEntity().getLocation().toVector());
						if(dir.length() > 0.0) {
							e.getHitEntity().setVelocity(dir.normalize().multiply(-1).multiply(1+wegschleudern*0.30));
						}
					}
					if(heranziehen > 0) {
						Vector dir = spawnloc.toVector().subtract(e.getEntity().getLocation().toVector());
						if(dir.length() > 0.0) {
							e.getHitEntity().setVelocity(dir.normalize().multiply(1+heranziehen*0.30));
						}
					}
				}
				if(härte > 0) {
					((Damageable) e.getHitEntity()).damage(härte*0.4, (Entity) proj.getShooter());
					
				}
				if(schwebe > 0) {
					((LivingEntity) e.getHitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 5*schwebe, (int) (schwebe/4.0), false, false));
				}
			}
			
			
			main.arrowdata.remove(proj);
		}
		
	}
	
	
	private boolean isHollowUpDownType(Block block) {
		Material type = block.getType();
		return isStep(type) || Tag.CARPETS.isTagged(type) || type == Material.SNOW
				|| type == Material.COMPARATOR 
				|| type == Material.CAULDRON || Tag.BEDS.isTagged(type) || type == Material.DAYLIGHT_DETECTOR
				|| Tag.RAILS.isTagged(type) || type == Material.HEAVY_WEIGHTED_PRESSURE_PLATE || Tag.WOODEN_PRESSURE_PLATES.isTagged(type)
				|| type == Material.LIGHT_WEIGHTED_PRESSURE_PLATE 
				|| ((Tag.TRAPDOORS.isTagged(type)||Tag.WOODEN_TRAPDOORS.isTagged(type)) && !(new TrapDoor(type, block.getData()).isOpen()));
	}

	private boolean isStep(Material type) {
		return Tag.SLABS.isTagged(type) || Tag.WOODEN_SLABS.isTagged(type);
	}

	private boolean isStair(Material type) {
		return Tag.STAIRS.isTagged(type) || Tag.WOODEN_STAIRS.isTagged(type);
	}
	
	
	private Projectile doBounce(Projectile proj, int bounce, double speednerf) {
		ProjectileSource source = proj.getShooter();
		EntityType projectileType = proj.getType();

		Vector arrowVelocity = proj.getVelocity();
		double speed = arrowVelocity.length();
		
		if (speed < 0.3D || (projectileType == EntityType.ARROW && speed < 0.5D)) {
			return null;
		}
		
		Location arrowLocation = proj.getLocation();
		Block hitBlock = arrowLocation.getBlock();

		BlockFace blockFace = BlockFace.UP;

		// special cases:
		if (!isHollowUpDownType(hitBlock)) {
			BlockIterator blockIterator = new BlockIterator(arrowLocation.getWorld(), arrowLocation.toVector(), arrowVelocity, 0.0D, 3);

			Block previousBlock = hitBlock;
			Block nextBlock = blockIterator.next();

			// to make sure, that previousBlock and nextBlock are not the same block
			while (blockIterator.hasNext() && (nextBlock.getType() == Material.AIR || nextBlock.isLiquid() || nextBlock.equals(hitBlock))) {
				previousBlock = nextBlock;
				nextBlock = blockIterator.next();
			}

			// direction
			blockFace = nextBlock.getFace(previousBlock);

		}

		if (blockFace != null) {
			if (blockFace == BlockFace.SELF) {
				blockFace = BlockFace.UP;
			}

			

			Vector mirrorDirection = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
			double dotProduct = arrowVelocity.dot(mirrorDirection); //skalarprodukt
			mirrorDirection = mirrorDirection.multiply(dotProduct).multiply(2.0D);

			// reduce projectile speed
			speed *= speednerf;

			Projectile newProjectile;
			
			newProjectile = (Projectile) proj.getWorld().spawnEntity(arrowLocation, proj.getType());
			newProjectile.setVelocity(arrowVelocity.subtract(mirrorDirection).normalize().multiply(speed));
			

			newProjectile.setShooter(source);
			newProjectile.setFireTicks(proj.getFireTicks());
			//newProjectile.setMetadata("bouncing", new FixedMetadataValue(main.m, true));

			// remove old arrow
			proj.remove();
			return newProjectile;
		}
		
		return null;
		
	}
			
	
	
	
}
