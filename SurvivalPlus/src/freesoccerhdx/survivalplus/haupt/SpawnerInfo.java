package freesoccerhdx.survivalplus.haupt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
public class SpawnerInfo {

	
	private static HashMap<UUID,Block> spawnerblock = new HashMap<>();
	private static HashMap<UUID,List<Holo>> spawnerholos = new HashMap<>();
	private static HashMap<UUID,Long> spawnerSpawned = new HashMap<>();
	
	
	public static boolean improveSpawner(Player p, Block clickedBlock) {
		if(spawnerSpawned.containsKey(p.getUniqueId())) {
			if(System.currentTimeMillis()-spawnerSpawned.get(p.getUniqueId()) < 500) {
				return false;
			}
		}
		spawnerSpawned.put(p.getUniqueId(), System.currentTimeMillis());
		
		CreatureSpawner spawner = (CreatureSpawner) clickedBlock.getState();
		spawner.setMinSpawnDelay(Math.max(spawner.getMinSpawnDelay()-20, 20));
		spawner.setMaxSpawnDelay(Math.max(spawner.getMinSpawnDelay(), Math.max(spawner.getMaxSpawnDelay()-60, 60)));
		spawner.setMaxNearbyEntities(Math.min(spawner.getMaxNearbyEntities()+2, 16));
		spawner.setRequiredPlayerRange(Math.min(spawner.getRequiredPlayerRange()+8, 64));
		
		spawner.update();
		printSpawnerInfo(p, clickedBlock, true);
		return true;
	}
	
	public static void printSpawnerInfo(Player p, Block clickedBlock, boolean forced) {
		boolean dontspawn = false;
		Long dif = -1L;
		if(spawnerSpawned.containsKey(p.getUniqueId()) && !forced) {
			dif = System.currentTimeMillis()-spawnerSpawned.get(p.getUniqueId());
			if(dif < 50 && dif < 1000*30) {
				return;
			}
		}
		if(hasSpawnerInfo(p)) {
			
			dontspawn = spawnerblock.get(p.getUniqueId()).equals(clickedBlock);
			removeSpawnerInfo(p);
		}
		if(dontspawn && dif < 1000*30 && !forced) return;
		
		
		CreatureSpawner spawner = (CreatureSpawner) clickedBlock.getState();

		List<String> lore = new ArrayList<>();
		lore.add("Monster: "+ spawner.getSpawnedType());
		lore.add("Spawndelay: "+ spawner.getMinSpawnDelay()/20 + "s - " + spawner.getMaxSpawnDelay()/20 + "s");
		lore.add("Spawnrange: "+ spawner.getSpawnRange());
		lore.add("Playerrange: "+ spawner.getRequiredPlayerRange());
		lore.add("Max. nearby Monsters: "+ spawner.getMaxNearbyEntities());
		
		List<Holo> h = new ArrayList<>();
		h.add(new Holo(clickedBlock.getLocation().add(0.5, 2*0.5+1.5, 0.5), "§2§nSpawner-Info:", 20*30, p));
		h.add(new Holo(clickedBlock.getLocation().add(0.5, 2*0.5+1.25, 0.5), "§eMonster: §b" +spawner.getSpawnedType(), 20*30, p));
		h.add(new Holo(clickedBlock.getLocation().add(0.5, 2*0.5+1.0, 0.5), "§eSpawndelay: §b"+ spawner.getMinSpawnDelay()/20 + "s - " + spawner.getMaxSpawnDelay()/20 + "s", 20*30, p));
		h.add(new Holo(clickedBlock.getLocation().add(0.5, 2*0.5+0.75, 0.5), "§eSpawnrange: §b"+ spawner.getSpawnRange(), 20*30, p));
		h.add(new Holo(clickedBlock.getLocation().add(0.5, 2*0.5+0.5, 0.5), "§ePlayerrange: §b"+ spawner.getRequiredPlayerRange(), 20*30, p));
		h.add(new Holo(clickedBlock.getLocation().add(0.5, 2*0.5+0.25, 0.5), "§eMax. nearby Monsters: §b"+ spawner.getMaxNearbyEntities(), 20*30, p));
		
		spawnerblock.put(p.getUniqueId(), clickedBlock);
		spawnerholos.put(p.getUniqueId(), h);
		spawnerSpawned.put(p.getUniqueId(), System.currentTimeMillis());
		
	}


	public static void removeSpawnerInfo(Player p) {
		if(hasSpawnerInfo(p)){
			List<Holo> pholo = spawnerholos.get(p.getUniqueId());
			spawnerblock.remove(p.getUniqueId());
			spawnerholos.remove(p.getUniqueId());
			//spawnerSpawned.remove(p.getUniqueId());
			spawnerSpawned.put(p.getUniqueId(), System.currentTimeMillis());
			for(Holo h : pholo) {
				h.despawn();
			}
			pholo.clear();
			
		}
		
	}


	public static boolean hasSpawnerInfo(Player p) {
		return spawnerblock.containsKey(p.getUniqueId());
	}

}
