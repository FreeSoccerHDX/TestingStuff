package freesoccerhdx.survivalplus.turtle;


import java.util.ArrayList;



import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import freesoccerhdx.survivalplus.haupt.main;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.WorldServer;



public class Hologramm{
	private static boolean markerActive = true;
	public static List<Hologramm> alleHolos = new ArrayList();
	private Location loc;
	private String msg;
	private int tick;
	private EntityArmorStand stand;
	private boolean gravity;
	private boolean forAllPlayers;
	private double top = 1.0D;
	private double velocity = 0.0D;
	private double maxAcc = 9.81D;
	private double maxVel = 30.0D;
	private int repeatId = -1;
	private int runLaterId = -1;
	private boolean stop = false;
	private List<Player> players = new ArrayList();
  
	public Hologramm(Location location, String text, int ticks, boolean gravity, boolean forAllPlayers){
		this.loc = location;
		this.msg = text;
		this.tick = ticks;
		this.gravity = gravity;
		this.forAllPlayers = forAllPlayers;
		if(gravity){
			this.top = (location.getWorld().getHighestBlockYAt(location) - (markerActive ? -0.0D : 1.9D));
		}
	}
  
	public Hologramm(Location location, String text, int ticks, boolean gravity){
		this(location, text, ticks, gravity, false);
	}
  
	public boolean setTicks(int ticks){
		this.tick = ticks;
		return this.tick == ticks;
	}
  
	public int getTicks(){
		return this.tick;
	}
  
	public boolean clearPlayers(){
		this.players.clear();
		return this.players.size() == 0;
	}
  
	public List<Player> getPlayers(){
		return this.players;
	}
	  
	public boolean addPlayer(Player p){
		return this.players.add(p);
	}
	  
	public boolean removePlayer(Player p){
		return this.players.remove(p);
	}
	public boolean setLocation(Location location){
		return (this.loc = location) != null;
	}  
	public Location getLocation(){
		return this.loc;
	}  
	public String getText(){
		return this.msg;
	}
  
	public boolean setText(String text){
		return (this.msg = text) != null;
	}
	public void update(){
		if(this.stand != null){
			this.stand.setLocation(this.loc.getX(), this.loc.getY(), this.loc.getZ(), 0.0F, 0.0F);
			this.stand.setCustomNameVisible(true);
			this.stand.setCustomName(IChatBaseComponent.ChatSerializer.b(this.msg));
			this.stand.setInvisible(true);
			this.stand.setNoGravity(gravity);
			this.stand.setMarker(markerActive);
      
			boolean change = false;

			List<Player> toShow = new ArrayList<Player>();
			List<Player> toRem = new ArrayList<Player>();
			List<Player> toTeleport = new ArrayList<Player>();
			if(this.gravity){
				if(this.loc.getY() > this.top){
					double add = this.maxAcc / 200.0D;
					if(this.velocity + add < this.maxVel){
						this.velocity += add;
					}else{
						this.velocity = this.maxVel;
					}
				}else{
					this.loc.setY(this.top);
					this.velocity = 0.0D;
				}
				this.loc.setY(this.loc.getY() - this.velocity);
				if((this.velocity != 0.0D) || (!this.stop)){
					change = true;
				}
			}
			if((change) || (this.forAllPlayers)){
				this.repeatId = Bukkit.getScheduler().runTaskLater(main.m, new Runnable(){
					@Override
					public void run(){
						Hologramm.this.update();
					}
				}, 1L).getTaskId();
			}
			if(change){
				toTeleport = this.players;
				if(this.velocity == 0.0D){
					this.stop = true;
				}
			}else if(this.forAllPlayers){
				for(Player p : this.players){
					if(!p.isOnline()){
						toRem.add(p);
					}else if(!p.getLocation().getWorld().equals(this.loc.getWorld())){
						toRem.add(p);
					}else if(p.getLocation().distance(this.loc) > 64.0D){
						toRem.add(p);
					}
				}
				for(Player p : Bukkit.getOnlinePlayers()){
					if((!this.players.contains(p)) && (p.getLocation().getWorld().equals(this.loc.getWorld())) && (p.getLocation().distance(this.loc) <= 64.0D)){
						toShow.add(p);
					}
				}
			}
			destroy(toRem);
			for(Player p : toRem){
				this.players.remove(p);
			}
			for(Player p : toShow){
				if(p.isOnline()){
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(this.stand));
				}
				if(!this.players.contains(p)){
					this.players.add(p);
				}
			}
			for(Player p : toTeleport){
				if(p.isOnline()){
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(this.stand));
				}
			}
		}
	}
	public void spawn(){
		WorldServer world = ((CraftWorld)this.loc.getWorld()).getHandle();
		this.stand = new EntityArmorStand(world, this.loc.getX(), this.loc.getY(), this.loc.getZ());
		this.stand.setLocation(this.loc.getX(), this.loc.getY(), this.loc.getZ(), 0.0F, 0.0F);
		this.stand.setCustomNameVisible(true);
		this.stand.setCustomName(IChatBaseComponent.ChatSerializer.b(this.msg));
		this.stand.setInvisible(true);
		this.stand.setNoGravity(true);
		this.stand.setMarker(markerActive);
		for(Player p : this.players){
			if(p.isOnline()){
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(this.stand));
			}
		}
		if(this.tick > 0){
			this.runLaterId = Bukkit.getScheduler().runTaskLater(main.m, new Runnable(){
				@Override
				public void run(){
					for(Player p : Hologramm.this.players){
						if(p.isOnline()){
							((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { Hologramm.this.stand.getId() }));
						}
					}	
					if(Hologramm.this.repeatId != -1){
						Bukkit.getScheduler().cancelTask(Hologramm.this.repeatId);
					}
					Hologramm.alleHolos.remove(this);
				}
			}, this.tick).getTaskId();
		}	
		update();
	}
  
	public static void destroyAll(){
		for(Hologramm h : alleHolos){
			h.destroy(false);
		}
	}
  
	public void destroy(){
		destroy(true);
	}
  
	public void destroy(boolean del){
		for(Player p : this.players){
			if(p.isOnline()){
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { this.stand.getId() }));
			}
		}
		if(this.runLaterId != -1){
			Bukkit.getScheduler().cancelTask(this.runLaterId);
		}
		if(this.repeatId != -1){
			Bukkit.getScheduler().cancelTask(this.repeatId);
		}
		if(del){
			alleHolos.remove(this);
		}
	}
  
	public void destroy(List<Player> list){
		for(Player p : list){
			if(p.isOnline()){
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { this.stand.getId() }));
			}
		}
	}
	
	public static Hologramm spawnHologramm(Location location, String text, int ticks, boolean gravity, Player... players){
		if((players != null) && (players.length > 0)){
			Hologramm holo = new Hologramm(location, text, ticks, gravity);
			for(Player p : players){
				holo.addPlayer(p);
			}
			holo.spawn();
			alleHolos.add(holo);
			return holo;
		}
		Hologramm h = new Hologramm(location, text, ticks, gravity, true);
		h.spawn();
		alleHolos.add(h);
		return h;
	}

	public EntityArmorStand getArmorStand() {
		return this.stand;
	}
}
