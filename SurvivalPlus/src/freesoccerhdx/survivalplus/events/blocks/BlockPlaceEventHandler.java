package freesoccerhdx.survivalplus.events.blocks;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public class BlockPlaceEventHandler implements Listener {

	
	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
	
		Block b = e.getBlock();
		ItemStack is = e.getItemInHand();
		
		NBTItem nbitem = new NBTItem(is);
		if(nbitem.hasKey("redstonetimer")) {
			File file = new File("plugins/redstonetimer/");
			file.mkdirs();
			String safestring = b.getX()+"+"+b.getY()+"+"+b.getZ()+".yml";
			
			File cfgfile = new File("plugins/redstonetimer/",safestring);
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(cfgfile);
			cfg.set("timer", 16);
			cfg.set("location", b.getLocation());
			
			try {
				cfg.save(cfgfile);
				e.getPlayer().sendMessage("§aDu hast erfolgreich ein Redstonetimer erstellt.");
			}catch(Exception ex) {
				e.getPlayer().sendMessage("§cEtwas ist fehlgeschlagen!");
			}
			
			
			
		}
			if(e.getBlock().getType() == Material.SPAWNER) {	
			if(is.getItemMeta() != null) {
				ItemMeta meta = is.getItemMeta();
				List<String> lore = meta.getLore();
				
				if(lore != null) {
					for(String s : lore) {
						if(s.startsWith("Monster: ")) {
							String da = s.split(": ")[1];
							CreatureSpawner spawner = (CreatureSpawner) b.getState();
							spawner.setSpawnedType(EntityType.valueOf(da));
						//	spawner.setMaxNearbyEntities(50);
							//spawner.setMinSpawnDelay(20*1);
						//	spawner.setMaxSpawnDelay(20*10);
							/*
							spawner.setDelay(0);
							spawner.setMinSpawnDelay(0);
							spawner.setMaxSpawnDelay(1);
							
							
							spawner.setRequiredPlayerRange(10);
							
							spawner.setSpawnRange(5);
							spawner.setSpawnCount(500);
							*/
							
							spawner.update();
						}
					}
				}
				
			}
		}
	}
	
}
