package freesoccerhdx.survivalplus.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import freesoccerhdx.survivalplus.haupt.TicTacToe;
import freesoccerhdx.survivalplus.haupt.VierGewinnt;
import freesoccerhdx.survivalplus.npc.NPCHandler;



public class PlayerQuitEventHandler implements Listener {

	
	
	
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		
		
		NPCHandler.unregisterPlayer(p);
		if(p.getOpenInventory() != null){
			String invname = p.getOpenInventory().getTitle();
			if(invname != null){
				if(invname.startsWith("                  §a§lTic Tac Toe")){
					if(TicTacToe.hasGame(p)){
						TicTacToe game = TicTacToe.getGame(p);
						game.win(game.getGegner(p), p);
					}
				}
				if(invname.startsWith("                  §a§lVier gewinnt")){
					if(VierGewinnt.hasGame(p)){
						VierGewinnt game = VierGewinnt.getGame(p);
						game.win(game.getGegner(p), p);
					}
				}
			}
		}
		
		
	}
	
}
