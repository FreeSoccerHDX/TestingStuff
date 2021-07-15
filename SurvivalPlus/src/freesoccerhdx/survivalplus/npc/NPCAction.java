package freesoccerhdx.survivalplus.npc;


public class NPCAction {

	private boolean on_fire, crouched, sprinting, invisible, glowing, flying_elytra;
	private byte result = 0;

	public NPCAction(boolean on_fire, boolean crouched, boolean sprinting, boolean invisible, boolean glowing,
			boolean flying_elytra) {
		this.on_fire = on_fire;
		this.crouched = crouched;
		this.sprinting = sprinting;
		this.invisible = invisible;
		this.glowing = glowing;
		this.flying_elytra = flying_elytra;
	}

	public NPCAction() {
	}

	public boolean isOn_fire() {
		return on_fire;
	}

	public NPCAction setOn_fire(boolean on_fire) {
		this.on_fire = on_fire;
		return this;
	}

	public boolean isCrouched() {
		return crouched;
	}

	public NPCAction setCrouched(boolean crouched) {
		this.crouched = crouched;
		return this;
	}

	public boolean isSprinting() {
		return sprinting;
	}

	public NPCAction setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
		return this;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public NPCAction setInvisible(boolean invisible) {
		this.invisible = invisible;
		return this;
	}

	public boolean isGlowing() {
		return glowing;
	}

	public NPCAction setGlowing(boolean glowing) {
		this.glowing = glowing;
		return this;
	}

	public boolean isFlying_elytra() {
		return flying_elytra;
	}

	public NPCAction setFlying_elytra(boolean flying_elytra) {
		this.flying_elytra = flying_elytra;
		return this;
	}

	public byte build() {
		result = 0;
		result = add(this.on_fire, (byte) 0x01);
		result = add(this.crouched, (byte) 0x02);
		result = add(this.sprinting, (byte) 0x08);
		result = add(this.invisible, (byte) 0x20);
		result = add(this.glowing, (byte) 0x40);
		result = add(this.flying_elytra, (byte) 0x80);
		return result;
	}

	private byte add(boolean condition, byte amount) {
		return (byte) (result += (condition ? amount : 0x00));
	}
}
	

