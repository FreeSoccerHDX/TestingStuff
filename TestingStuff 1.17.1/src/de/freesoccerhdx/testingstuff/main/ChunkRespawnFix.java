package de.freesoccerhdx.testingstuff.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkRespawnFix implements Listener{

	private static HashMap<String, HashMap<String, ArrayList<UUID>>> chunks;

	  // -- JavaPlugin methods --------------------------------------------------------------------------------------------

	public static void stop() {
	 
	    for (String worldName : chunks.keySet()) {
	      World world = Bukkit.getServer().getWorld(worldName);

	      for (String chunkKey : chunks.get(worldName).keySet()) {
	        Chunk chunk = getChunkByKey(world, chunkKey);
	        if (chunk != null) chunk.setForceLoaded(false);
	      }
	    }
	  }

	public static void init() {

	    chunks = new HashMap<String, HashMap<String, ArrayList<UUID>>>();

	    for (World world : Bukkit.getServer().getWorlds()) {
	      chunks.put(world.getName(), new HashMap<String, ArrayList<UUID>>());

	      for (Player player : world.getPlayers())
	        if (player.isDead()) {
	          lockChunk(player);
	        }
	    }

	  }

	  // -- Listener methods ----------------------------------------------------------------------------------------------

	  @EventHandler
	  private static void onPlayerDeath(PlayerDeathEvent event) {
	    Player player = event.getEntity();

	    lockChunk(player);
	  }

	  @EventHandler
	  private static void onPlayerJoin(PlayerJoinEvent event) {
	    Player player = event.getPlayer();
	    if (!player.isDead()) return;

	    lockChunk(player);
	  }

	  @EventHandler
	  private static void onPlayerQuit(PlayerQuitEvent event) {
	    Player player = event.getPlayer();

	    if (player.isDead()) releaseChunk(player);
	  }

	  @EventHandler
	  private static void onPlayerRespawn(PlayerRespawnEvent event) {
	    releaseChunk(event.getPlayer());
	  }

	  // -- Helper methods ------------------------------------------------------------------------------------------------

	  private static String chunkKey(Chunk chunk) {
	    return chunk.getX() + "_" + chunk.getZ();
	  }

	  private static Chunk getChunkByKey(World world, String chunkKey) {
	    Chunk    chunk = null;
	    String[] parts = chunkKey.split("_");

	    if (parts.length == 2) chunk = world.getChunkAt(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));

	    return chunk;
	  }


	  private static void lockChunk(Player player) {
	    Chunk  chunk = player.getLocation().getChunk();
	    String world = player.getWorld().getName(), chunkKey = chunkKey(chunk);

	    if (!chunks.get(world).containsKey(chunkKey))
	      chunks.get(world).put(chunkKey, new ArrayList<UUID>());

	    chunks.get(world).get(chunkKey).add(player.getUniqueId());
	    chunk.setForceLoaded(true);
	  }

	  private static void releaseChunk(Player player) {
	    UUID   uuid  = player.getUniqueId();
	    Chunk  chunk = player.getLocation().getChunk();
	    String world = player.getWorld().getName(), chunkKey = chunkKey(chunk);

	    if (!chunks.get(world).containsKey(chunkKey) || !chunks.get(world).get(chunkKey).contains(uuid)) return;

	    chunks.get(world).get(chunkKey).remove(uuid);
	    if (chunks.get(world).get(chunkKey).size() == 0) chunk.setForceLoaded(false);
	  }


	}
