package freesoccerhdx.survivalplus.haupt;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.WorldServer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import com.mojang.datafixers.util.Pair;


public class Holo {

	public static List<Holo> alleHolos = new ArrayList<>();
	public Location loc;
	public String msg;
	public int tick;
	public EntityArmorStand stand;
	public List<Player> players = new ArrayList<>();
	public boolean despawned = false;
	
	
	public Holo(Location location, String text, int livingticks, List<Player> playerlist){
		this.loc = location;
		this.msg = text;
		this.tick = livingticks;
		for(Player p : playerlist) {
			this.players.add(p);
		}
		spawn();
	}
	
	public Holo(Location location, String text, int livingticks, Player... playerlist){
		this.loc = location;
		this.msg = text;
		this.tick = livingticks;
		for(Player p : playerlist) {
			this.players.add(p);
		}
		spawn(); 
	}
	
	public void changeEquipment(EnumItemSlot eis, ItemStack mat) {
		net.minecraft.server.v1_16_R3.ItemStack nmsIs = CraftItemStack.asNMSCopy(mat);
		//this.stand.setEquipment(eis, nmsIs);
		this.stand.setSlot(eis, nmsIs);
		
		
		List<Pair<EnumItemSlot,net.minecraft.server.v1_16_R3.ItemStack>> itlist = new ArrayList<>();
		itlist.add(Pair.of(eis, nmsIs));
		PacketPlayOutEntityEquipment ppoee = new PacketPlayOutEntityEquipment(this.stand.getId(), itlist);
		
		//new PacketPlayOutEntityEquipment
		for(Player p : this.players) {
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoee);
		}
	}
	
	public void spawn(){
		WorldServer world = ((CraftWorld)this.loc.getWorld()).getHandle();
		this.stand = new EntityArmorStand(world, this.loc.getX(), this.loc.getY(), this.loc.getZ());
		this.stand.setLocation(this.loc.getX(), this.loc.getY(), this.loc.getZ(), 0.0F, 0.0F);
		if(this.msg.length()!=0) {
			this.stand.setCustomNameVisible(true);
		}
		//IChatBaseComponent cbc = ChatSerializer.a("{\"text\":\""+ this.msg + "\"}");
		//#this.stand.setCustomName(cbc);
		this.stand.setCustomName(ChatSerializer.a("{\"text\":\""+ this.msg.replace("&", "§") + "\"}"));
		//Bukkit.broadcastMessage(this.msg);
		this.stand.setInvisible(true);
		this.stand.setNoGravity(true);
		this.stand.setMarker(true);
		this.stand.setSmall(true);
		
		
		
		PacketPlayOutSpawnEntityLiving pposel = new PacketPlayOutSpawnEntityLiving(this.stand);
	//	PacketPlayOutEntity ppoe = new PacketPlayOutEntity(this.stand.getId());
		PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(this.stand.getId(), this.stand.getDataWatcher(), true);
		
		for(Player p : this.players){
			if(p.isOnline()){
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(pposel);
			//	((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoe);
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoem);
				
			}
		}
		Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
			@Override
			public void run() {
				update();
			}
		}, 1);
		alleHolos.add(this);
		
	}
	
	public void _update() {
		this.stand.setLocation(this.loc.getX(), this.loc.getY(), this.loc.getZ(), 0.0F, 0.0F);
		this.stand.setCustomNameVisible(true);
		//IChatBaseComponent cbc = ChatSerializer.a("{\"text\":\""+ this.msg + "\"}");
		//#this.stand.setCustomName(cbc);
		this.stand.setCustomName(ChatSerializer.a("{\"text\":\""+ this.msg.replace("&", "§") + "\"}"));
		this.stand.setInvisible(true);
		this.stand.setNoGravity(true);
		this.stand.setMarker(true);
		
		//PacketPlayOutSpawnEntityLiving pposel = new PacketPlayOutSpawnEntityLiving(this.stand);
		//PacketPlayOutEntity ppoe = new PacketPlayOutEntity(this.stand.getId());
		PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(this.stand.getId(), this.stand.getDataWatcher(), true);
		
		for(Player p : this.players){
			if(p.isOnline()){
			//	((CraftPlayer)p).getHandle().playerConnection.sendPacket(pposel);
			//	((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoe);
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoem);
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(this.stand));
				
			}
		}
		
	}
	
	public void update() {
		this.tick = Math.max(-10, this.tick-1);
		//this._update();
		
		
		if((this.tick > 1 || this.tick <= -1) && !this.despawned) {
			Bukkit.getScheduler().runTaskLater(main.m, new Runnable() {
				@Override
				public void run() {
					update();
				}
			}, 1);
		}else {
			this.despawn();
			
			
		}
	}
	
	public void despawn() {
		for(Player p : this.players){
			if(p.isOnline()){
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { this.stand.getId() }));
			}
		}
		this.despawned = true;
		alleHolos.remove(this);
	}

	public void remove(Player rem) {
		if(players.contains(rem) && !this.despawned) {
			((CraftPlayer)rem).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { this.stand.getId() }));
		}
	}
	public void add(Player add) {
		if(players.contains(add)) {
			PacketPlayOutSpawnEntityLiving pposel = new PacketPlayOutSpawnEntityLiving(this.stand);
			PacketPlayOutEntityMetadata ppoem = new PacketPlayOutEntityMetadata(this.stand.getId(), this.stand.getDataWatcher(), true);
			((CraftPlayer)add).getHandle().playerConnection.sendPacket(pposel);
			((CraftPlayer)add).getHandle().playerConnection.sendPacket(ppoem);
		}
	}
	
	
}
