package de.freesoccerhdx.testingstuff.main.utils;

import java.io.IOException;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;

import de.freesoccerhdx.testingstuff.customentity.utils.CustomEntity;
import de.freesoccerhdx.testingstuff.main.TestingStuff;
import de.freesoccerhdx.testingstuff.main.utils.PacketListener.PlayerPacketEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInItemName;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;


public class PlayerPacketEventHandler {

	
	
	
	public static PacketDataSerializer serializer(Packet<?> packet) {
		ByteBuf bb = Unpooled.buffer();
		PacketDataSerializer pds = new PacketDataSerializer(bb);
		try {
			packet.a(pds);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return pds;
	}
	
	public static PlayerPacketEvent onPlayerPacket(PlayerPacketEvent e) {
		Player p = e.getPlayer();
		Object packet = e.getPacket();
		boolean playin = e.isPacketPlayIn();
		boolean playout = e.isPacketPlayOut();
		
		if(playin) {
			if(packet instanceof PacketPlayInUpdateSign) {
	    		PacketPlayInUpdateSign ppius = (PacketPlayInUpdateSign) packet;
	    		String[] lines = ppius.c();
	    		if(SignGUI.hasGUI(p)) {  			
	    			SignGUI.closeGUI(p, lines);
	    			e.setNewPacket(null);
	    		}
	    	}
			
			if(packet instanceof PacketPlayInItemName) {
				PacketPlayInItemName ppiin = (PacketPlayInItemName) packet;
				
				if(AnvilGUI.hasPlayer(p)) {
					AnvilGUI gui = AnvilGUI.getPlayerGUI(p);
					gui.onAnvilInput(p,ppiin.b());
				}
			}
			
			/*
			Bukkit.getScheduler().runTask(TestingStuff.main, new Runnable() {
				
				@Override
				public void run() {
					Bukkit.broadcastMessage("packet: " + packet.getClass().toString().replace("net.minecraft.server.v1_16_R3.", ""));
				}
				
			});
			*/
			
		}
		
		if(playout) {
			if(packet instanceof PacketPlayOutSpawnEntityLiving) {
				
				//Bukkit.getScheduler().runTask(TestingStuff.main, ()->Bukkit.broadcastMessage("packet: " + packet));
				
				boolean work = true;
				
				if(work) {
					PacketPlayOutSpawnEntityLiving ppoe = (PacketPlayOutSpawnEntityLiving) packet;
					
					PacketDataSerializer pds = serializer(ppoe);
					//int entityid = pds.i();
					int entityid = pds.j();
					
					WorldServer ws = ((CraftWorld)p.getWorld()).getHandle();
				
					
					
					//Entity ent = ws.getEntity(entityid);
					Entity ent = ws.G.d().a(entityid);
					
					
					if(ent != null) {
						
						if(ent.getClass().getName().contains("CustomEntity")) {
							e.setNewPacket(null);
							// Sending new Packets to Player
							Bukkit.getScheduler().runTask(TestingStuff.main, ()->CustomEntity.sendNewNpcPacket(p, (EntityLiving) ent));
						}
						
						/*
						NBTTagCompound ntc = new NBTTagCompound();		
						ent.save(ntc);
						
						if(ntc.hasKey("customnpc")) {
							CustomEntity ce = (CustomEntity) ent;
							e.setNewPacket(null);
							
							// Sending new Packets to Player
							Bukkit.getScheduler().runTask(TestingStuff.main, ()->CustomEntity.sendNewNpcPacket(p, (EntityLiving) ent));
							
						}
						
						
						if(ent.getClass().getName().endsWith("CustomEntity")) {
							e.setNewPacket(null);
							// Sending new Packets to Player
							Bukkit.getScheduler().runTask(TestingStuff.main, ()->CustomEntity.sendNewNpcPacket(p, (EntityLiving) ent));
						}
						*/
						
						/*
						
						Bukkit.getScheduler().runTask(TestingStuff.main, ()->Bukkit.broadcastMessage("entity: "+ent));
						
						if(ent instanceof CustomEntity) {
							e.setNewPacket(null);
							// Sending new Packets to Player
							Bukkit.getScheduler().runTask(TestingStuff.main, ()->CustomEntity.sendNewNpcPacket(p, (EntityLiving) ent));
						}
						
					//	Bukkit.getScheduler().runTask(TestingStuff.main, ()->Bukkit.broadcastMessage("entitytype: " + ent));
	
						NBTTagCompound ntc = new NBTTagCompound();		
						ent.save(ntc);
						
						if(ntc.hasKey("customnpc")) {
							e.setNewPacket(null);
							
							// Sending new Packets to Player
							Bukkit.getScheduler().runTask(TestingStuff.main, ()->CustomEntity.sendNewNpcPacket(p, (EntityLiving) ent));
							
						}
						*/					}
				}
				
				
			}
		}
		
		
		return e;
		
	}
	
}
