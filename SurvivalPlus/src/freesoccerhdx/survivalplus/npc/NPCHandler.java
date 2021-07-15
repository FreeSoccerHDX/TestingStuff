package freesoccerhdx.survivalplus.npc;


import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import freesoccerhdx.survivalplus.haupt.main;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.ItemStack;

public class NPCHandler {

	private static final List<NPCPlayer> npcplayers = new ArrayList<>();
	
	public static void register(NPCPlayer npcplayer) {
		if(!isRegisterd(npcplayer)) {
			npcplayers.add(npcplayer);
		}
	}
	
	public static void unregister(NPCPlayer npcplayer) {
		if(isRegisterd(npcplayer)) {
			npcplayers.remove(npcplayer);
		}
	}
	
	public static boolean isRegisterd(NPCPlayer npcplayer) {
		return npcplayers.contains(npcplayer);
	}
	
	public static List<NPCPlayer> getRegistered(){
		return npcplayers;
	}
	
	
	public static void unregisterPlayer(Player p) {
		for(NPCPlayer npc : npcplayers) {
			if(npc.hasPlayer(p)) {
				npc.removePlayer(p);
			}
		}
	}
	
	private static int ticks = 0;
	public static void run() {
		Bukkit.getScheduler().runTaskTimer(main.m, new Runnable() {

			@Override
			public void run() {
				ticks ++;
				for(Player p : Bukkit.getOnlinePlayers()) {
					updatePlayer(p);
				}
				if(ticks%(20*60*10) == 0) {
					for(NPCPlayer npc : npcplayers) {
						npc.safeToFile();
					}
					ticks = 0;
				}
			}
			
		}, 10, 20);
	}
	public static void updatePlayer(Player p) {
		double maxdif = NPCValues.VIEW_RANGE;
		Location ploc = p.getLocation();
		
		if(npcplayers.size() > 0) {
			for(NPCPlayer npc : npcplayers) {
				Location nploc = npc.getLocation();
				
				if(nploc.getWorld().equals(ploc.getWorld())) {
					if(nploc.distance(ploc) < maxdif) {
						if(!npc.hasPlayer(p)) {
							npc.addPlayer(p);
						}
					}else if(npc.hasPlayer(p)) {
						npc.removePlayer(p);
					}
				}else if(npc.hasPlayer(p)) {
					npc.removePlayer(p);
				}
			}
		}
	}

	public static void onDisable() {
		for(NPCPlayer npc : getRegistered()) {
			npc.safeToFile();
			npc.despawn(false);
		}
	}

	public static void onEnable() {
		File dir = new File("plugins/npcdata");
		if(dir.exists()) {
			for(File file : dir.listFiles()) {
				if(file.getName().endsWith(".dat")) {
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					
					UUID uuid =  UUID.fromString(cfg.getString("UUID"));
					Location loc = cfg.getLocation("Location");
					String name = cfg.getString("Name");
					String tablistname = cfg.getString("Tablistname");
					boolean isontablist = cfg.getBoolean("Isontablist");
					EntityType klontype = EntityType.valueOf(cfg.getString("Klontype"));
					
					
					HashMap<EnumItemSlot,ItemStack> equipment = new HashMap<>();
					
					for(EnumItemSlot eis : EnumItemSlot.values()) {
						String obj = cfg.getString("equipment."+eis.toString());
						if(obj != null) {
							org.bukkit.inventory.ItemStack is = NPCMethods.fromBase64( obj);
							
							if(is != null) {
								equipment.put(eis, NPCMethods.getCraftItemStack(is));
							}
							
						}
					}
					
					
					
					String texture = cfg.getString("Texture");
					String signature = cfg.getString("Signature");
					byte actiondata = (byte) cfg.getInt("Actiondata");
					boolean gravity = cfg.getBoolean("Gravity");
					boolean sprint = cfg.getBoolean("sprinting");
					double maxhealth = cfg.getDouble("maxhealth");
					double health = cfg.getDouble("health");
					
					NPCPlayer npc = new NPCPlayer(name, tablistname, loc);
					npc.setPreUUID(uuid);
					npc.setKlonType(klontype);
					
					for(EnumItemSlot eis : equipment.keySet()) {
						npc.setEquipment(eis, NPCMethods.getBukkitItemStack(equipment.get(eis)));
					}
					npc.setSkin(texture, signature);
					npc.spawn(isontablist);
					npc.setActionData(actiondata);
					npc.setGravity(gravity);
					npc.setMaxHealth(maxhealth);
					npc.setHealth(health);
					npc.setSprinting(sprint);
					npc.startTickTimer();
					register(npc);
					
					
				}
			}
		}
	}
	/*
	 * 	
		cfg.set("UUID", ep.getUniqueID());
		cfg.set("Location", loc);
		cfg.set("Name", name);
		cfg.set("Tablistname", tablistname);
		cfg.set("Isontablist", isOnTablist);
		cfg.set("Klontype",klontype);
		cfg.set("Equipment", equipment);
		cfg.set("Texture", texture);
		cfg.set("Signature", signature);
		cfg.set("Actiondata", actiondata);
		cfg.set("Gravity", gravity);
		cfg.set("maxhealth", maxhealth);
		cfg.set("health", health);
	 */
	public static HashMap<UUID,Inventory> editinv = new HashMap<>();
	public static HashMap<UUID,NPCPlayer> editnpc = new HashMap<>();
	
	public static void setEditInventory(Player p, NPCPlayer npc, Inventory inv) {
		editinv.put(p.getUniqueId(), inv);
		editnpc.put(p.getUniqueId(), npc);
	}
	public static boolean hasEditInventory(Player p) {
		return editinv.containsKey(p.getUniqueId());
	}

	public static void closeEditInventory(Player p) {
		if(hasEditInventory(p)) {
			editinv.remove(p.getUniqueId());
			editnpc.remove(p.getUniqueId());
		}
		
	}
	
	
	
	
	
}
















