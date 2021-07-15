package freesoccerhdx.survivalplus.events.entity;



import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import freesoccerhdx.survivalplus.haupt.EntityHealthHandler;
import freesoccerhdx.survivalplus.haupt.Holo;
import freesoccerhdx.survivalplus.haupt.Methoden;
import freesoccerhdx.survivalplus.haupt.main;

public class EntityDamageByEntityEventHandler implements Listener {

	@EventHandler
	public void entitydmgbyent(EntityDamageByEntityEvent e) {
		
		
		if(e.getDamager() instanceof LivingEntity && e.getEntity() instanceof LivingEntity) {
			LivingEntity ent = (LivingEntity) e.getEntity();
			LivingEntity p = (LivingEntity) e.getDamager();

			
			
			
			if(main.holdingentity.containsKey(p.getName())) {
				if(main.holdingentity.get(p.getName()) == ent) {
					e.setCancelled(true);
				}
			}
			if(p.getEquipment().getItemInMainHand() != null) {
				if(e.getCause() != DamageCause.ENTITY_ATTACK) return;
				
				ItemStack is = p.getEquipment().getItemInMainHand();
				int raub = Methoden.getLoreEnchLevel(is, "Lebensraub");
				int wither = Methoden.getLoreEnchLevel(is, "Wither");
				int gift = Methoden.getLoreEnchLevel(is, "Vergiftung");
				int slow = Methoden.getLoreEnchLevel(is, "Verlangsamung");
				int truedmg = Methoden.getLoreEnchLevel(is, "Rüstungsdurchdringung");
				int wegsch = Methoden.getLoreEnchLevel(is, "Wegschleudern");
				int heran = Methoden.getLoreEnchLevel(is, "Heranziehen");
				int fest = Methoden.getLoreEnchLevel(is, "Festhalten");
				int riesen = Methoden.getLoreEnchLevel(is, "Riesenschlag");
				int erst = Methoden.getLoreEnchLevel(is, "Erstschlag");
				int sturm = Methoden.getLoreEnchLevel(is, "Sturmschlag");
				
				
				if(sturm > 0) {
					ent.setNoDamageTicks(Math.max(0, ent.getNoDamageTicks()-sturm));
					if(p instanceof Player) {
						Player he = (Player) p;
						
						int cd = he.getCooldown(((HumanEntity) p).getInventory().getItemInMainHand().getType());
						cd -= 1.0*cd*(sturm*1.0/20.0);
						he.setCooldown(((HumanEntity) p).getInventory().getItemInMainHand().getType(), cd);
						
					}
				}
				
				if(erst > 0) {
					if(ent.getScoreboardTags().contains("firsthit")) {
						if(ent.hasMetadata("lastdmg")) {
							Long lasthit = ent.getMetadata("lastdmg").get(0).asLong();
							if(System.currentTimeMillis()-lasthit > 1000*60) {
								ent.removeScoreboardTag("firsthit");
							}
						}else {
							ent.removeScoreboardTag("firsthit");
						}
						
					}
					
					if(!ent.getScoreboardTags().contains("firsthit")) {
						e.setDamage(e.getDamage()*(1.0+(erst*1.0/10)));
						ent.addScoreboardTag("firsthit");
						ent.setMetadata("lastdmg", new FixedMetadataValue(main.m, System.currentTimeMillis()));
						ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1f, 2f);
					}
					
				}
				double hitcd = 1.0;
				if(p instanceof Player) {
					try {
						Player he = (Player) p;
						//hitcd = he.getAttackCooldown();
						
					}catch(Exception ex) {
					}
					
				}
		
				if(riesen > 0 && hitcd==1.0) {
					double maxhp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(); 
					if(maxhp > 50) {
						e.setDamage(e.getDamage()+((maxhp*1.0/200.0)*(riesen)));
						ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1f, 0.1f);
					}
				}
				
				if(ent instanceof Player) {
					if(fest > 0) {
						main.festhalten.put(ent.getUniqueId(),System.currentTimeMillis()+50*fest);
					}
				}
				
				if(wegsch > 0) {
					ent.setVelocity(p.getLocation().getDirection().multiply(1+wegsch*0.30));
				}
				
				if(heran > 0) {
					ent.setVelocity(p.getLocation().getDirection().multiply(-(1+heran*0.30)));
				}
				if(truedmg > 0) {
					LivingEntity livent = (LivingEntity) ent;
					for(int i = 0; i < 10; i++) {
						livent.getWorld().playSound(livent.getLocation().add(Math.random()*1-.5, Math.random()*1-.5, Math.random()*1-.5), Sound.ITEM_SHIELD_BREAK, 0.2f, 2);
						livent.getWorld().spawnParticle(Particle.CRIT, livent.getLocation().add(Math.random()*1.5, Math.random()*1.5, Math.random()*1.5), 0);
					}
					double origdmg = e.getDamage();
					double fulldmg = origdmg*(truedmg*1.0/25.0);
					//p.sendMessage(origdmg + " ->" + fulldmg + " ->" + e.getDamage() + " -> " +e.getFinalDamage());
					
					
					//e.setDamage(e.getDamage()-fulldmg);
					EntityDamageEvent ede = new EntityDamageEvent(livent, DamageCause.CUSTOM, fulldmg);
					Bukkit.getPluginManager().callEvent(ede);
					if(!ede.isCancelled()) {
						fulldmg = ede.getDamage();
						if(fulldmg > 0) {
							if(livent.getHealth() > fulldmg) {
								livent.setHealth(livent.getHealth()-fulldmg);
							}else if(livent.getHealth() > 1) {
								livent.setHealth(1); 
							}	
						}
					}
				}
				
				Double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
				if(raub > 0 && Methoden.chance(10) && p.getHealth() != maxHealth) { 
					Double addh = e.getDamage()*(raub/40.0);
					
					
					if(p.getHealth()+addh > maxHealth) {
						p.setHealth(maxHealth);
						
						p.getWorld().playSound(ent.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1f, 0.3f);
						
					}else if(p.getHealth()+addh < maxHealth){
						p.setHealth(p.getHealth()+addh);
						p.getWorld().playSound(ent.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1f, 0.3f);
					}
				}
				
				if(wither > 0) {
					((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10*wither, 0, false, false, false));
				}
				if(gift > 0) {
					((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*gift, 0, false, false, false));
				}
				if(slow > 0) {
					((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*slow, 0, false, false, false));
				}
				
				
			}
			
			if(p instanceof Player) {
				EntityHealthHandler.addTarget((Player) p, ent);
				Location spawn = ent.getEyeLocation().add(0, 0.5, 0);
				
				new Holo(spawn, "§c§l"+Math.round(e.getFinalDamage()*10.0)/10.0+" §r§4☠", 20, (Player)p);
			}
		}
		
		
	}
	
	
}
