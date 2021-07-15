package freesoccerhdx.survivalplus.events.player;




import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import freesoccerhdx.survivalplus.haupt.PlayerPacketEvent;
import freesoccerhdx.survivalplus.haupt.main;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;




public class PlayerJoinEventHandler implements Listener {
	
	
	
	
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		removePlayer(p);
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		injectPlayer(p);
		
		for(String s : main.customrecipes) {
			p.discoverRecipe(NamespacedKey.minecraft(s));
		}
		
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
	        ChannelPipeline channelPipeline = ((CraftPlayer) p).getHandle().playerConnection.networkManager.channel.pipeline();
	        channelPipeline.addBefore("packet_handler", p.getName(), channelDuplexHandler);
	    }
	public static void removePlayer(Player player){
	        Channel channel =((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
	        channel.eventLoop().submit(() -> {
	            channel.pipeline().remove(player.getName());
	            return null;
	        });
	    }
}
