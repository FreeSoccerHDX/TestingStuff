package freesoccerhdx.survivalplus.events.player;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeEventHandler implements Listener {

	
	@EventHandler
	public void onSignChang(SignChangeEvent e) {
		String[] lines = e.getLines();
		
		lines[0] = lines[0].replace("&", "§");
		lines[1] = lines[1].replace("&", "§");
		lines[2] = lines[2].replace("&", "§");
		lines[3] = lines[3].replace("&", "§");	 
		
		Block bl = e.getBlock();
		Sign sign = (Sign) bl.getState();
		sign.setEditable(true);
		sign.setLine(0, lines[0]);
		sign.setLine(1, lines[1]);
		sign.setLine(2, lines[2]);
		sign.setLine(3, lines[3]);
		sign.update();

	}
	
}
