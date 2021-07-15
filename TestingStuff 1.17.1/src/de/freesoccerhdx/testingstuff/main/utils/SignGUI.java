package de.freesoccerhdx.testingstuff.main.utils;

import java.util.HashMap;



import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.entity.TileEntitySign;

public class SignGUI {

	private static HashMap<UUID,BlockPosition> guipos = new HashMap<>();
	private static HashMap<UUID,SignGUIListener> guilistener = new HashMap<>();
	
	
	public static void openGUI(Player p, String[] lines, SignGUIListener guilist) {
		if(lines == null) {
			lines = new String[] {"","","",""};
		}
		
		CraftPlayer cp = (CraftPlayer) p;
		Location l = p.getLocation();
		BlockPosition bp = new BlockPosition(l.getBlockX(), 1, l.getBlockZ());
		WorldServer worldserver = ((CraftWorld) p.getWorld()).getHandle();
		
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(bp, CraftMagicNumbers.getBlock(Material.OAK_SIGN, (byte) 0));
		//packet.b = CraftMagicNumbers.getBlock(Material.OAK_SIGN, (byte) 0);
		
		cp.getHandle().b.sendPacket(packet);
	
		
		IChatBaseComponent[] components = CraftSign.sanitizeLines(lines);
		TileEntitySign sign = new TileEntitySign(bp, packet.b);
		//sign.setPosition(bp);
		System.arraycopy(components, 0, sign.d, 0, sign.d.length);
		//sendPacket(sign.getUpdatePacket());
		
		cp.getHandle().b.sendPacket(sign.getUpdatePacket());
		
		
		PacketPlayOutOpenSignEditor ppose = new PacketPlayOutOpenSignEditor(bp);

		cp.getHandle().b.sendPacket(ppose);
		
		guilistener.put(p.getUniqueId(), guilist);
		guipos.put(p.getUniqueId(), bp);
		
	}
	
	
	public static void closeGUI(Player p, String[] lines) {
		SignGUIListener listener = guilistener.get(p.getUniqueId());
		CraftPlayer cp = (CraftPlayer) p;
		BlockPosition bp = guipos.get(p.getUniqueId());
		
		Block normalblock = p.getWorld().getBlockAt(bp.getX(), bp.getY(), bp.getZ());
		
		
		
		
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(bp , CraftMagicNumbers.getBlock(normalblock.getType(), (byte) normalblock.getData()));		
		cp.getHandle().b.sendPacket(packet);
		listener.listen(cp, lines);
		
		
		guilistener.remove(p.getUniqueId());
		guipos.remove(p.getUniqueId());
	}
	
	
	public static boolean hasGUI(Player p) {
		return guipos.containsKey(p.getUniqueId());
	}
	
	public static abstract class SignGUIListener {

		protected abstract void listen(Player p, String[] lines);
		
		
	}
}
