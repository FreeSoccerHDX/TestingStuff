package freesoccerhdx.survivalplus.haupt;

import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


	
public class EnderDragonBoostEventHandler implements Listener {
	
	  @EventHandler
	  public void onDragonDeath(EntityDeathEvent e) {
	    if (e.getEntity().getType() == EntityType.ENDER_DRAGON) {
	    Location eggspawn = new Location(Bukkit.getWorld("world_the_end"), 0.5D, 66.0D, 0.5D);
	    Location itemspawn = new Location(Bukkit.getWorld("world_the_end"), 0.5D, 70.0D, -10.5D);
	    
	    eggspawn.getBlock().setType(Material.DRAGON_EGG);
	    for (int i = 0; i < 20; i++) {
	      ExperienceOrb orb = (ExperienceOrb)eggspawn.getWorld().spawnEntity(eggspawn.clone().add(Math.random() * 10.0D - 5.0D, 60.0D, Math.random() * 10.0D - 5.0D), EntityType.EXPERIENCE_ORB);
	      orb.setExperience(500);
	    } 
	    Double r = Double.valueOf(Math.random());
	    if (r.doubleValue() <= 0.1D) {
	      Set<Material> disks = Tag.ITEMS_MUSIC_DISCS.getValues();
	      List<Material> targetList = new ArrayList<>(disks);
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(targetList.get((int)(Math.random() * targetList.size())), 1, 0, null, new String[] { "§4Ehrenlos §4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 0.2D) {
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(Material.TRIDENT, 1, 0, "Dreizack", new String[] { "§4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 0.3D) {
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(Material.DIAMOND, 42, 0, null, new String[] { "§4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 0.4D) {
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(Material.LAPIS_BLOCK, 16, 0, null, new String[] { "§4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 0.5D) {
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(Material.DIAMOND, 42, 0, null, new String[] { "§4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 0.7D) {
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(Material.DRAGON_HEAD, 1, 0, null, new String[] { "§4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 0.8D) {
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(Material.GHAST_TEAR, 3, 0, null, new String[] { "§4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 0.9D) {
	      itemspawn.getWorld().dropItem(itemspawn, Methoden.item(Material.GHAST_TEAR, 1, 0, null, new String[] { "§4Ehrenlos erhalten." }));
	    } else if (r.doubleValue() <= 1.0D) {
	      ItemStack is = new ItemStack(Material.BOW);
	      is.addUnsafeEnchantment(Enchantment.MENDING, 1);
	      is.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
	      itemspawn.getWorld().dropItem(itemspawn, is);
	    } 
	    File file = new File("plugins/mcextra.yml");
	    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	    Long start = cfg.getLong("lastdragonspawn");
	    Long dif = Long.valueOf(System.currentTimeMillis() - start.longValue());
	    
	    Bukkit.broadcastMessage(System.currentTimeMillis()+"");
	    Bukkit.broadcastMessage(start+"");
	    Bukkit.broadcastMessage(dif+"");
	    int h = 0;
	    int m = 0;
	    int s = 0;
	    while (dif.longValue() >= 3600000L) {
	      h++;
	      dif = Long.valueOf(dif.longValue() - 3600000L);
	    } 
	    while (dif.longValue() >= 60000L) {
	      m++;
	      dif = Long.valueOf(dif.longValue() - 60000L);
	    } 
	    while (dif.longValue() >= 1000L) {
	      s++;
	      dif = Long.valueOf(dif.longValue() - 1000L);
	    } 
	    
	    String zeit = "";
	    if (h > 0) zeit = String.valueOf(zeit) + h + "h "; 
	    
	    zeit = String.valueOf(zeit) + ((m < 10) ? "0" : "") + m + "min ";
	    zeit = String.valueOf(zeit) + ((s < 10) ? "0" : "") + s + "s ";
	    	Bukkit.broadcastMessage("§6§l----------------------------------");
	    	Bukkit.broadcastMessage(" ");
	    	Bukkit.broadcastMessage("§5    Der Enderdrache["+cfg.getInt("dragonspawns")+"] wurde besiegt!");
	    	Bukkit.broadcastMessage("§d    Dauer: §e"+ zeit);
	    	Bukkit.broadcastMessage(" ");
	    	Bukkit.broadcastMessage("§6§l----------------------------------");
	    } 
	  }
	  
	  @EventHandler
	  public void onDragonSpawn(EntitySpawnEvent e) {
	    if (e.getEntity().getType() == EntityType.ENDER_DRAGON) {
	  
	    File file = new File("plugins/mcextra.yml");
	    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	    cfg.set("lastdragonspawn", Long.valueOf(System.currentTimeMillis()));
	    if (!file.exists()) {
	      cfg.set("dragonspawns", Integer.valueOf(1));
	      try {
	        cfg.save(file);
	      } catch (IOException e1) {
	        e1.printStackTrace();
	      } 
	    } else {
	      cfg.set("dragonspawns", Integer.valueOf(cfg.getInt("dragonspawns") + 1));
	      try {
	        cfg.save(file);
	      } catch (IOException e1) {
	        e1.printStackTrace();
	      } 
	    } 
	    int drspawnas = cfg.getInt("dragonspawns");
	    LivingEntity ent = (LivingEntity)e.getEntity();
	    ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(ent.getHealth() * (1.0D + drspawnas / 20.0D));
	    ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	    } 
	  }
	  
	  @EventHandler
	  public void onDragonDmg(EntityDamageEvent e) {
	    if (e.getEntity().getType() == EntityType.ENDER_DRAGON) {
	    e.setDamage(e.getDamage() * 0.7D);
	    final Entity ent = e.getEntity();
	    if (Math.random() <= 0.4D)
	      Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
	            @Override
				public void run() {
	              if (!ent.isDead() && ent != null) {
	                Location strahl = ent.getLocation().add(ent.getLocation().getDirection().multiply(-8));
	                for (Player c : ent.getWorld().getPlayers()) {
	                  Vector drv = ent.getLocation().getDirection();
	                  Vector plv = strahl.toVector().subtract(c.getLocation().toVector());
	                  double vleng = drv.normalize().multiply(plv.normalize()).length();
	                  if (vleng > 0.0D && vleng < 0.5D) {
	                    float angle = drv.angle(plv);
	                    float degree = (float)(angle * 57.29577951308232D);
	                    if(degree < 60) {
		                    if (c.getLocation().distance(strahl) <= 80.0D && (
		                      (LivingEntity)ent).hasLineOfSight(c)) {
		                  Methoden.drawLaser(strahl, c.getLocation(), Double.valueOf(0.4D), 2.0F, Color.fromRGB(173, 90, 199));
		                  c.damage(6.0D, ent);
		                    } 
	                    }
	                  } 
	                } 
	              } 
	            }
	          },60L); 
	    } 
	  }
	}
