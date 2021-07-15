package freesoccerhdx.survivalplus.hologram;

import org.bukkit.entity.Player;

public class runCommand extends ClickHologramm {

	private String cmd = "";

	public runCommand(String cmd) {
		this.cmd = cmd;
	}

	@Override
	public void click(Player p) {
		p.chat(cmd);
	}

}
