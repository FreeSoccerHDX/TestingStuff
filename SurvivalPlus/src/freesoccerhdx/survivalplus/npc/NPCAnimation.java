package freesoccerhdx.survivalplus.npc;

public enum NPCAnimation {

	SWING_MAIN_HAND(0), 
	TAKE_DAMAGE(1), 
	LEAVE_BED(2), 
	SWING_OFFHAND(3), 
	CRITICAL_EFFECT(4), 
	MAGIC_CRITICAL_EFFECT(5);

	private int id;

	private NPCAnimation(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	



}
