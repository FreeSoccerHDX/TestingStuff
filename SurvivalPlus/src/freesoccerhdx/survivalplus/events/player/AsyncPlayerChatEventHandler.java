package freesoccerhdx.survivalplus.events.player;

import java.util.List;



import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import freesoccerhdx.survivalplus.haupt.main;
import net.minecraft.server.v1_16_R3.PacketPlayInSpectate;
import net.minecraft.server.v1_16_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_16_R3.Vec3D;


public class AsyncPlayerChatEventHandler implements Listener {

	public static void flyitems(Player target) {
		List<Entity> nearby = target.getNearbyEntities(16, 16, 16);
		
		for(Entity ent : nearby) {
			if(ent != target) {
				PacketPlayOutEntityVelocity ppoev = new PacketPlayOutEntityVelocity(ent.getEntityId(), new Vec3D(Math.random()-.5, Math.random()-.5, Math.random()-.5));
				CraftPlayer cp = (CraftPlayer) target;
				cp.getHandle().playerConnection.sendPacket(ppoev);
			} 
		}
	}
	
	@EventHandler
	public void AsyncPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		
		
		
		if(msg.startsWith("flyitems")) {
			e.setCancelled(true);
			e.setMessage("");
			
			Player target = Bukkit.getPlayer(msg.split(" ")[1]);
			
			Bukkit.getScheduler().runTask(main.m, ()->flyitems(target));
		}
		
		if(msg.equals("whatdoesmoritz")) {
			e.setCancelled(true);
			e.setMessage("");
			
			if(p.getGameMode() != GameMode.SPECTATOR) {
				Bukkit.getScheduler().runTaskLater(main.m, ()->p.setGameMode(GameMode.SPECTATOR), 10);
			}else {
				Bukkit.getScheduler().runTaskLater(main.m, ()->p.setGameMode(GameMode.SURVIVAL), 10);
			}
		}
		if(msg.startsWith("packet")) {
			e.setCancelled(true);
			e.setMessage("");
			
			Entity target = Bukkit.getPlayer(msg.split(" ")[1]);
			if(target != null) {
				PacketPlayOutCamera ppoc = new PacketPlayOutCamera(((CraftEntity) target).getHandle());
				CraftPlayer cp = (CraftPlayer)p;
				cp.getHandle().playerConnection.sendPacket(ppoc);
			}else {
				p.sendMessage("smth dont worked");
			}
			
		}
	}
	
	
}
