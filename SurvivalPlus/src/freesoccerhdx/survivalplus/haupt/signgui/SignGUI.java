package freesoccerhdx.survivalplus.haupt.signgui;

import java.util.HashMap;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import freesoccerhdx.survivalplus.haupt.main;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_16_R3.TileEntitySign;

public class SignGUI {

	private static HashMap<UUID,BlockPosition> guipos = new HashMap<>();
	private static HashMap<UUID,SignGUIListener> guilistener = new HashMap<>();
	
	
	public static void openGUI(Player p, String[] lines,SignGUIListener guilist) {
		CraftPlayer cp = (CraftPlayer) p;
		Location l = p.getLocation();
		BlockPosition bp = new BlockPosition(l.getBlockX(), 1, l.getBlockZ());
		
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) p.getWorld()).getHandle(), bp);
		packet.block = CraftMagicNumbers.getBlock(Material.OAK_SIGN, (byte) 0);
		cp.getHandle().playerConnection.sendPacket(packet);
	
		
		IChatBaseComponent[] components = CraftSign.sanitizeLines(lines);
		TileEntitySign sign = new TileEntitySign();
		sign.setPosition(bp);
		System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);
		//sendPacket(sign.getUpdatePacket());
		
		cp.getHandle().playerConnection.sendPacket(sign.getUpdatePacket());
		
		
		PacketPlayOutOpenSignEditor ppose = new PacketPlayOutOpenSignEditor(bp);

		cp.getHandle().playerConnection.sendPacket(ppose);
		
		guilistener.put(p.getUniqueId(), guilist);
		guipos.put(p.getUniqueId(), bp);
		
	}
	
	
	public static void closeGUI(Player p, String[] lines) {
		SignGUIListener listener = guilistener.get(p.getUniqueId());
		CraftPlayer cp = (CraftPlayer) p;
		BlockPosition bp = guipos.get(p.getUniqueId());
		
		Block normalblock = p.getWorld().getBlockAt(bp.getX(), bp.getY(), bp.getZ());
		
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) p.getWorld()).getHandle(), bp);
		packet.block = CraftMagicNumbers.getBlock(normalblock.getType(), (byte) normalblock.getData());
		
		cp.getHandle().playerConnection.sendPacket(packet);
		listener.listen(cp, bp, lines);
		
		
		
		/*
		if(id == 0) {
			p.sendMessage(lines);
		}else if(id == 1) {
			SignChangeEvent sce = new SignChangeEvent(normalblock, p, lines);
			Bukkit.getScheduler().runTask(main.m, ()->Bukkit.getPluginManager().callEvent(sce));
		}
		*/
		
		guilistener.remove(p.getUniqueId());
		guipos.remove(p.getUniqueId());
	}
	
	
	public static boolean hasGUI(Player p) {
		return guipos.containsKey(p.getUniqueId());
	}
}
