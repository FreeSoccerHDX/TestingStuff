package freesoccerhdx.survivalplus.haupt.signgui;

import org.bukkit.entity.Player;


import net.minecraft.server.v1_16_R3.BlockPosition;

public abstract class SignGUIListener {


	protected abstract void listen(Player p, BlockPosition bp, String[] lines);
	
	
}
