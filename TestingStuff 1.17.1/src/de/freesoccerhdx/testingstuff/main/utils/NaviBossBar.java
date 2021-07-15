package de.freesoccerhdx.testingstuff.main.utils;

import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_17_R1.boss.CraftBossBar;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.freesoccerhdx.testingstuff.main.TestingStuff;

public class NaviBossBar extends CraftBossBar{
	
	private Location target = null;
	private Player player = null;
	private int task = -1;
	
	private double barscale = 1000;
	
	private double reachrange = 8.0;
	
	private NaviMessage navimessage = new NaviMessage() {

		
		@Override
		public String getMessage(double distance, double reachrange, double barscale, Location target) {
			return "Distance: " + (int) distance + "m";
		}

		@Override
		public double getBarProgress(double distance, double reachrange, double barscale, Location target) {
			double fortschritt = barscale / (distance + barscale);	
			return fortschritt;
		}

		@Override
		public boolean showDirectionArrows(double distance, double reachrange, double barscale, Location target) {
			return true;
		}

		@Override
		public boolean showHeightArrows(double distance, double reachrange, double barscale, Location target) {
			return true;
		}
	};
	
	
	public NaviBossBar(Player p, Location target) {
		this(p, target, BarColor.WHITE, BarStyle.SOLID, new BarFlag[]{});
	}

	public NaviBossBar(Player p, Location target, BarColor color, BarStyle style, BarFlag[] flags) {
		super("", color, style, flags);
		this.target = target;
		this.player = p;
		super.addPlayer(p);
		this.show();
		task = Bukkit.getScheduler().runTaskTimer(TestingStuff.main, ()->run(), 5, 1).getTaskId();
		
	}
	
	public void setNaviMessage(NaviMessage msg) {
		navimessage = msg;
	}
	public NaviMessage getNaviMessage() {
		return navimessage;
	}
	
	@Override
	public void addPlayer(Player player) {
		// Function is not provided because only handel NaviBossBar per player duo the changing name
	}
	
	public void setBarScale(double dist) {
		barscale = dist;
	}
	public double getBarScale() {
		return barscale;
	}
	
	public void setTargetLocation(Location l) {
		target = l.clone();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean stop() {
		if(isRunning()) {
			Bukkit.getScheduler().cancelTask(task);
			task = -1;
			this.removeAll();
			return true;
		}
		return false;
	}
	
	public boolean isRunning() {
		return task != -1;
	}
	
	
	private void run() {
	
		
		if(!player.isValid() || player == null || target == null || navimessage == null) {
			stop();
			return;
		}
		
		Location ploc = player.getEyeLocation().clone();
		Location tloc = target.clone();
		
		Vector dif = tloc.toVector().clone().subtract(ploc.toVector().clone());
		
		double dis = dif.length();
		double real_dis = dis;
		
		if(dis < reachrange){
			dis = 0;
		}

		
		int richtung = -2; //-1 Links, 0 Mitte Rechts, 1 Rechts, 2 Hinten

		
		Vector recht = new Vector(player.getLocation().getDirection().getZ(),0,-player.getLocation().getDirection().getX());
		
		if (player.getLocation().toVector().subtract( target.clone().toVector()).setY(0.0).angle(recht)/Math.PI*180 < 90)
			richtung = 1;
		else
			richtung = -1;
		
		double angle = (player.getLocation().toVector().subtract( target.clone().toVector()).setY(0.0).angle(player.getLocation().getDirection().setY(0.0).multiply(-1))/Math.PI*180);
		
		if (angle<20+((int) (25.0*(1.0-((2048.0)/(dis+2048.0))))))
			richtung = 0;
		
		if (angle>160-((int) (25.0*(1.0-((2048.0)/(dis+2048.0))))))
			richtung = 2;
		
		if (dis==0 || tloc.toVector().clone().setY(0).subtract(ploc.toVector().clone().setY(0)).length() < reachrange)
			richtung = 3;
		

		
		
		String titel = "§f"+navimessage.getMessage(real_dis, reachrange, barscale, target);
		
		if(navimessage.showDirectionArrows(real_dis, reachrange, barscale, target)) {
			String l = "";
			
			if (angle <= 10 || dis <= reachrange || tloc.toVector().clone().setY(0).subtract(ploc.toVector().clone().setY(0)).length() < reachrange)
				l = "§2";
			else if (angle<=30)
				l = "§a";
			else if (angle<=60)
				l = "§e";
			else if (angle<=90)
				l = "§6";
			else if (angle<=120)
				l = "§c";
			else if (angle<=180)
				l = "§4";
			
			String al = navimessage.default_arrow_left;
			String ar = navimessage.default_arrow_right;
			String ad = navimessage.default_nothing;
			
			if (richtung==-1){
				titel = l+al+" "+titel+l+" "+al;
			}else if (richtung == 1){
				titel = l+ar+" "+titel+l+" "+ar;
			}else if (richtung == 0)
				titel = l+ar+" "+titel+l+" "+al;
			else if (richtung == 2)
				titel = l+al+" "+titel+l+" "+ar;
			else if (richtung == 3)
				titel = l+ad+" "+titel+l+" "+ad;
			
		}
		
		if(navimessage.showDirectionArrows(real_dis, reachrange, barscale, target)) {
			int heightdif = target.getBlockY() - ploc.getBlockY();
			int h = Math.abs(heightdif);
			
			String zeich = navimessage.default_arrow_down;
			if(heightdif > 0) { // player needs to go up ^^^
				zeich = navimessage.default_arrow_up;
			}
			if(h < reachrange) {
				zeich = navimessage.default_nothing;
			}
			
			
			String l = "";
			
			if (h <= 20 || dis <= reachrange)
				l = "§2";
			else if (h<=40)
				l = "§a";
			else if (h<=70)
				l = "§e";
			else if (h<=90)
				l = "§6";
			else if (h<=120)
				l = "§c";
			else
				l = "§4";
			
			
			titel = l + zeich +" "+ titel +" "+ l + zeich;
		}
		

		setProgress(navimessage.getBarProgress(real_dis, reachrange, barscale, target));
		setTitle(titel);
		
	}
	
	

	public static abstract class NaviMessage {
		
		
		public String default_arrow_up = "^^^";
		public String default_arrow_down = "vvv";
		public String default_arrow_left = "<<<";
		public String default_arrow_right = ">>>";
		
		public String default_nothing = "---";
		
		
		
		
		public NaviMessage() {
		}
		
		
		public abstract String getMessage(double distance, double reachrange, double barscale, Location target);
		public abstract double getBarProgress(double distance, double reachrange, double barscale, Location target);
		public abstract boolean showDirectionArrows(double distance, double reachrange, double barscale, Location target);
		public abstract boolean showHeightArrows(double distance, double reachrange, double barscale, Location target);
		
	}
	
	
}














