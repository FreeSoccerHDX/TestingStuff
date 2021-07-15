package freesoccerhdx.survivalplus.haupt;

import org.bukkit.entity.Player;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPacketEvent extends Event{

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	
	private static final HandlerList handlers = new HandlerList();
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
