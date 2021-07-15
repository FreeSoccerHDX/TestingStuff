package freesoccerhdx.survivalplus.events.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import freesoccerhdx.survivalplus.haupt.Methoden;

public class EntitySpawnEventHandler implements Listener {

	
	@EventHandler
	public void EntitySpawn(EntitySpawnEvent e) {
		Location loc = e.getLocation();
		if (!(e.getEntity() instanceof LivingEntity)) return;
		
		LivingEntity ent = (LivingEntity) e.getEntity();
	
		
		
		if(loc.getWorld().getEnvironment() == Environment.NETHER) {
			if(e.getEntityType() == EntityType.SKELETON) {
				e.setCancelled(true);
				WitherSkeleton skel = (WitherSkeleton) loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
				
				skel.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15);
				skel.setHealth(15);
				
			}
			if(e.getEntityType() == EntityType.ZOMBIE) {
				e.setCancelled(true);
				PigZombie skel = (PigZombie) loc.getWorld().spawnEntity(loc, EntityType.PIGLIN);
				skel.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15);
				skel.setHealth(15);
			}
		}
		
		if(e.getEntityType() == EntityType.WITCH) {
			ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200);
			ent.setHealth(200);
			ent.setCustomName("§6BitchBoss");
			ent.setCustomNameVisible(true);
		}
		if(e.getEntityType() == EntityType.EVOKER) {
		
			((Attributable) ent).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15);
			((Damageable) ent).setHealth(100);
			
		}
		
			
		if(e.getEntityType() == EntityType.PILLAGER) {
			EntityEquipment ee = ((LivingEntity) ent).getEquipment();
			ee.setItemInMainHandDropChance(0.25f);
			ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			if(Methoden.chance(50)) {
				ItemStack hand = new ItemStack(Material.CROSSBOW);
				ItemMeta m = hand.getItemMeta();
				m.setLore(Methoden.randomNewBowEnchant());
				hand.setItemMeta(m);
				hand.addUnsafeEnchantments(Methoden.randomOldBowEnchant(Material.CROSSBOW));
				ee.setItemInMainHand(hand);
			}else {
				ItemStack hand = new ItemStack(Material.IRON_AXE);
				ItemMeta m = hand.getItemMeta();
				m.setLore(Methoden.randomNewWeaponEnchant());
				hand.setItemMeta(m);
				hand.addUnsafeEnchantments(Methoden.randomOldWeaponEnchant(Material.IRON_AXE));
				ee.setItemInMainHand(hand);
			}
			
		
		}
			
		if((e.getEntityType() == EntityType.WITHER_SKELETON || e.getEntityType() == EntityType.ZOMBIE) && Methoden.chance(10)) {
			
			EntityEquipment ee = ((LivingEntity) ent).getEquipment();
			ItemStack hand = new ItemStack(Material.IRON_SWORD);
			
			ItemMeta m = hand.getItemMeta();
			m.setLore(Methoden.randomNewWeaponEnchant());
			hand.setItemMeta(m);
			hand.addUnsafeEnchantments(Methoden.randomOldWeaponEnchant(Material.IRON_SWORD));
			
			ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			
			ee.setItemInMainHand(hand);
			ee.setItemInMainHandDropChance(0.25f);
		}
		
		if(e.getEntityType() == EntityType.SKELETON && Methoden.chance(10)) {
			
			EntityEquipment ee = ((LivingEntity) ent).getEquipment();
			ItemStack hand = new ItemStack(Material.BOW);
			
			ItemMeta m = hand.getItemMeta();
			m.setLore(Methoden.randomNewBowEnchant());
			hand.setItemMeta(m);
			hand.addUnsafeEnchantments(Methoden.randomOldBowEnchant(Material.BOW));
			
			ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			
			ee.setItemInMainHand(hand);
			ee.setItemInMainHandDropChance(0.25f);
		}
		
		if(e.getEntityType() == EntityType.PIGLIN && Methoden.chance(10)) {
			
			EntityEquipment ee = ((LivingEntity) ent).getEquipment();
			ItemStack hand = new ItemStack(Material.GOLDEN_SWORD);
			
			ItemMeta m = hand.getItemMeta();
			m.setLore(Methoden.randomNewWeaponEnchant());
			hand.setItemMeta(m);
			hand.addUnsafeEnchantments(Methoden.randomOldWeaponEnchant(Material.GOLDEN_SWORD));
			
			ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			
			ee.setItemInMainHand(hand);
			ee.setItemInMainHandDropChance(0.5f);
		}
	}
	
}
