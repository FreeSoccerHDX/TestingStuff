package de.freesoccerhdx.testingstuff.main.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

public class PacketListener  implements Listener {
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		removePlayer(p);
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		injectPlayer(p);
	}
	
	public static void injectPlayer(Player p){
	        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
	            @Override
	            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
	            	
	            	PlayerPacketEvent ppe = new PlayerPacketEvent(p, packet, true);
	            	try {
		            	ppe = PlayerPacketEventHandler.onPlayerPacket(ppe);
		            	packet = ppe.getPacket();
	            	}catch(Exception ex) {
	            		ex.printStackTrace();
	            	}
	            	
	            	/*
	            	if(packet instanceof PacketPlayInUpdateSign) {
	            		PacketPlayInUpdateSign ppius = (PacketPlayInUpdateSign) packet;
	            		String[] lines = ppius.c();
	            		if(SignGUI.hasGUI(p)) {
	            			
	            			SignGUI.closeGUI(p, lines);
	            		}
	            	}
	            	*/
	            	if(packet != null) {
	            		super.channelRead(channelHandlerContext, packet);
	            	}
	            }
	            @Override
	            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
	            
	            	PlayerPacketEvent ppe = new PlayerPacketEvent(p, packet, false);
	            	try {
		            	ppe = PlayerPacketEventHandler.onPlayerPacket(ppe);
		            	packet = ppe.getPacket();
	            	}catch(Exception ex) {
	            		ex.printStackTrace();
	            	}
	            	if(packet != null) {
	            		super.write(channelHandlerContext, packet, channelPromise);
	            	}
	            }
	        };
	        ChannelPipeline channelPipeline = ((CraftPlayer) p).getHandle().b.a.k.pipeline();
	        try {
	        	channelPipeline.addBefore("packet_handler", p.getName(), channelDuplexHandler);
			} catch (Exception e) {
				
				new Thread() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							channelPipeline.addBefore("packet_handler", p.getName(), channelDuplexHandler);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}.run();
			}
	        
	    }
	public static void removePlayer(Player player){
		Channel channel =((CraftPlayer) player).getHandle().b.a.k;
		channel.eventLoop().submit(() -> {
		    channel.pipeline().remove(player.getName());
	    return null;
		});
	}
	
	

	public static class PlayerPacketEvent extends Event{

		public HandlerList getHandlerList() {
			return handlers;
		}
		
		
		private final HandlerList handlers = new HandlerList();
		private Player player = null;
		private Object packet = null;
		private boolean playin = false;
		
		
		public PlayerPacketEvent(Player player, Object packet, boolean playin) {
			this.player = player;
			this.packet = packet;
			this.playin = playin;
		}
		
		public HandlerList getHandlers() {
			return handlers;
		}
		
		
		
		
		
		public Player getPlayer() {
			return this.player;
		}
		public Object getPacket() {
			return this.packet;
		}
		public boolean isPacketPlayIn() {
			return playin;
		}
		public boolean isPacketPlayOut() {
			return !playin;
		}
		
		public void setNewPacket(Object newpacket) {
			this.packet = newpacket;
		}
		
	}
	
	
	
	
	
	
}
