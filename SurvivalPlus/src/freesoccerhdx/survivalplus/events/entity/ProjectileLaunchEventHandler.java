package freesoccerhdx.survivalplus.events.entity;

import java.util.HashMap;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.tr7zw.nbtapi.NBTEntity;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.main;

public class ProjectileLaunchEventHandler implements Listener {

	public static void detonate(Entity ent, int ticks){
		Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
			
			@Override
			public void run() {
				if(ent.isValid()) {
					Location loc = ent.getLocation();
					ent.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
					ent.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
					
					for(Entity enear : loc.getWorld().getNearbyEntities(loc, 3.0, 3.0, 3.0)) {
						if(enear instanceof LivingEntity) {
							if(enear.getType() != EntityType.BAT) {
								((LivingEntity) enear).damage(0.3*15);
								Vector between = enear.getLocation().toVector().subtract(loc.toVector()).multiply(0.4);
								
								enear.setVelocity(between.setY(between.getY()*2));
								ent.remove();
							}
						}
					}
				}
				
			}
		}, ticks);
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e){
		
		if(e.getEntity().getShooter() instanceof Witch){
			Witch shooter = (Witch) e.getEntity().getShooter();
			
			int counter = 0;
			
			if(shooter.getCustomName() != null) {
				if(shooter.getCustomName().split(" ").length != 2) {
					shooter.setCustomName("§6BitchBoss §c1");
				}else {
					counter = Integer.parseInt(shooter.getCustomName().split(" ")[1].replace("§c", ""))+1;
					shooter.setCustomName("§6BitchBoss §c"+counter);
				}
			}
			
			
			if(counter == 6) {
				shooter.setCustomName("§6BitchBoss");
				
				if(Methoden.chance(50)) {
					Random rnd = new Random();
					for(int i = 0; i < 3+rnd.nextInt(5); i++) {
						Location spawnloc = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection());
						spawnloc = spawnloc.add(Math.random()-0.5, Math.random()-0.5, Math.random()-0.5);
						
						Bat bat = (Bat) e.getEntity().getWorld().spawnEntity(spawnloc, EntityType.BAT);
						bat.setCustomName("§4Attacking Bird");
						bat.setCustomNameVisible(true);
						bat.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2);
						bat.setHealth(2);
						
						if(e.getEntity().getShooter() instanceof Mob) {
							if(((Mob) e.getEntity().getShooter()).getTarget() != null) {
								Entity target = ((Mob) e.getEntity().getShooter()).getTarget();
								Vector v = target.getLocation().toVector().subtract(bat.getLocation().toVector()).multiply(0.2);
								bat.setVelocity(v);
							}
						}
						
						
						detonate(bat, 20+rnd.nextInt(20));
					}
				}else {
					
					Location spawnloc = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection());
					spawnloc = spawnloc.add(Math.random()-0.5, Math.random()-0.5, Math.random()-0.5);
					Skeleton skel = (Skeleton) e.getEntity().getWorld().spawnEntity(spawnloc, EntityType.SKELETON);
					skel.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
					skel.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
					skel.setHealth(100);
					skel.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
					EntityEquipment ee = skel.getEquipment();
					ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
					skel.setCustomName("§4Attacking Turret");
					skel.setCustomNameVisible(true);
						
					
				}
			}
		}
		
		if(e.getEntity().getShooter() instanceof Player){
			Player p = (Player) e.getEntity().getShooter();
			Entity entity = e.getEntity();
			
			ItemStack bomb = null;
			
			if(entity.getType() == EntityType.SNOWBALL) {
				if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.SNOWBALL) {
					bomb = p.getInventory().getItemInMainHand();
				}else if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInMainHand().getType() == Material.SNOWBALL){
					bomb = p.getInventory().getItemInOffHand();
				}
				if(bomb == null) return;
				
				if(bomb.hasItemMeta()) {
					if(bomb.getItemMeta().getDisplayName() != null) {
						if(bomb.getItemMeta().getDisplayName().equals("§6§lBomb §5§lin §4§lda §2§lSnow")) {
							HashMap<String,Object> data = new HashMap<>();
							data.put("Abprallen", 5);
							data.put("Explosion", 3);
							data.put("Geschwindigkeit", 0.7);
							main.arrowdata.put(entity,data);
						}
					}
				}
			}
		
			
			
			
		}
	}	
	
}

