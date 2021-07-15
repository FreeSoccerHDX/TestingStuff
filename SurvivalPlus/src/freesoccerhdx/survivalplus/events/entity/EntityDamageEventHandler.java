package freesoccerhdx.survivalplus.events.entity;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import freesoccerhdx.survivalplus.haupt.Methoden;

public class EntityDamageEventHandler implements Listener {
	
	
	@EventHandler
	public void entitydmg(EntityDamageEvent e) {
		
		if(e.getEntityType() == EntityType.WITCH) {
			if(e.getCause() == DamageCause.POISON || e.getCause() == DamageCause.MAGIC || e.getCause() == DamageCause.BLOCK_EXPLOSION
					 || e.getCause() == DamageCause.ENTITY_EXPLOSION) {
				e.setCancelled(true);
			}
		
			Witch shooter = (Witch) e.getEntity();
			
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
						
						if(e.getEntity() instanceof Mob) {
							if(((Mob) e.getEntity()).getTarget() != null) {
								Entity target = ((Mob) e.getEntity()).getTarget();
								Vector v = target.getLocation().toVector().subtract(bat.getLocation().toVector()).multiply(0.2);
								bat.setVelocity(v);
							}
						}
						
						
						ProjectileLaunchEventHandler.detonate(bat, 20+rnd.nextInt(20));
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
		
		if(e.getEntityType() == EntityType.BAT) {
			if(e.getCause() == DamageCause.POISON || e.getCause() == DamageCause.MAGIC || e.getCause() == DamageCause.BLOCK_EXPLOSION
					 || e.getCause() == DamageCause.ENTITY_EXPLOSION) {
				e.setCancelled(true);
			}
		}
		
		
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			
			
			if(e.getCause() == DamageCause.FALL) {
				if(p.getInventory().getBoots() != null) {
					ItemStack is = p.getInventory().getBoots();
					int grav = Methoden.getLoreEnchLevel(is, "Sprungkraft");
					
					if(e.getDamage() <= 0.5*grav) {
						e.setCancelled(true);
					}else {
						e.setDamage(Math.max(0, e.getDamage()-0.5*grav));
					}
				}
			}else if(e.getCause() == DamageCause.THORNS) {
				int dornlvl = Methoden.getArmorEnchLevel(p, "Dornenschutz");
				e.setDamage(e.getDamage()*2);
				if(dornlvl >= 20) {
					e.setCancelled(true);
				}else if(dornlvl > 0){
					e.setDamage(e.getDamage() - e.getDamage()*(dornlvl/20));
				}
				
			}
		}
	}

}
