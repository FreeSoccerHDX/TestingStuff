package freesoccerhdx.survivalplus.npc;

import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.EntityPose;
import net.minecraft.server.v1_16_R3.EnumItemSlot;


public class KlonEventHandler implements Listener {

	@EventHandler
	public void onShootBow(EntityShootBowEvent e) {
		Entity ent = e.getEntity();
		
		for(NPCPlayer npc : NPCHandler.getRegistered()) {
			if(npc.hasKlon()) {
				if(npc.getKlon().equals(ent)) {
					if(!npc.hasBow()) {
						e.setCancelled(true);
					}
					
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		Entity ent = e.getEntity();
		Entity tar = e.getTarget();
		boolean klonent = NPCMethods.isKlonOfNPC(ent);
		boolean klontar = NPCMethods.isKlonOfNPC(tar);
		
		if(klonent) {
			if(klontar) {
				e.setCancelled(true);
			}
		}
		if(klontar && ent.getType() == EntityType.IRON_GOLEM) {
			e.setCancelled(true);
		}
		
	}
	@EventHandler
	public void healthregain(EntityRegainHealthEvent e) {
		Entity ent = e.getEntity();
		for(NPCPlayer npc : NPCHandler.getRegistered()) {
			if(npc.hasKlon()) {
				if(npc.getKlon().equals(ent)) {
					npc.setHealth(Math.min(npc.getMaxHealth(), npc.getHealth()+e.getAmount()));
					break;
				}
			}
		}
	}
	@EventHandler
	public void ondeath(EntityDeathEvent e) {
		Entity ent = e.getEntity();
		
		for(NPCPlayer npc : NPCHandler.getRegistered()) {
			if(npc.hasKlon()) {
				if(npc.getKlon().equals(ent)) {
					e.setDroppedExp(0);
					e.getDrops().clear();
					npc.setLocation(npc.getKlon().getLocation());
					npc.setAnimation(NPCAnimation.TAKE_DAMAGE);
					npc.getLocation().getWorld().playSound(npc.getLocation(),Sound.ENTITY_PLAYER_DEATH, 1, 1);
					npc.getKlon().remove();
					npc.setKlon(null);
					npc.setPose(EntityPose.SLEEPING);
					npc.respawn(20*60);
	
					break;
				}
			}
		}
		
		
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		Entity ent = e.getEntity();
		
		
		for(NPCPlayer npc : NPCHandler.getRegistered()) {
			if(npc.hasKlon()) {
				if(npc.getKlon().equals(ent)) {
					npc.damage(e.getFinalDamage());
					e.setDamage(0);
					npc.setAnimation(NPCAnimation.TAKE_DAMAGE);
					
					if(e.getCause() == DamageCause.FALL) {
						ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_PLAYER_SMALL_FALL, 1, 1);
					}else {
						ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
					}
					break;
				}
			}
		}
		
		if(e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
			NPCPlayer npc = NPCMethods.getNPCOfKlon(ee.getDamager());
			if(npc != null) {
				npc.setAnimation(NPCAnimation.SWING_MAIN_HAND);
			}
		}
	}
	
	
	
}













