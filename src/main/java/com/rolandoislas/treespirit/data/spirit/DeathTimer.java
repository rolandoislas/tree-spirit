package com.rolandoislas.treespirit.data.spirit;

import com.rolandoislas.treespirit.data.Config;

/**
 * Created by Rolando on 3/1/2017.
 */
public class DeathTimer {
	private final String playerUid;
	private long time = 0;
	private boolean startMessageSent;
	private boolean stopMessageSent;

	/**
	 * Death timer tied to player id
	 * @param playerUuid player id
	 */
	public DeathTimer(String playerUuid) {
		this.playerUid = playerUuid;
		update();
	}

	/**
	 * Constructor Gson serialization
	 * @param playerUuid player id
	 * @param time ticks elapsed
	 * @param startMessageSent has sent a start message
	 * @param stopMessageSent has sent a stop message
	 */
	public DeathTimer(String playerUuid, long time, boolean startMessageSent, boolean stopMessageSent) {
		this.playerUid = playerUuid;
		this.time = time;
		this.startMessageSent = startMessageSent;
		this.stopMessageSent = stopMessageSent;
	}

	/**
	 * Add one tick to the counter
	 */
	public void update() {
		this.time++;
	}

	public boolean shouldSendStartMessage() {
		if (!startMessageSent) {
			startMessageSent = true;
			stopMessageSent = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String)
			return obj.equals(playerUid);
		return obj instanceof DeathTimer && ((DeathTimer) obj).getPlayerId().equals(playerUid);
	}

	public String getPlayerId() {
		return playerUid;
	}

	/**
	 * Check if the player has been too far away from the core/allowed blocks for more than the allowed seconds
	 * @return dead
	 */
	public boolean isDead() {
		// Ticks instead of currentTimeMillis to allow for pausing the integrated server
		// PlayerTickEvent is called 40 times per seconds not 20
		return time >= Config.deathTime * 40;
	}

	public boolean shouldSendStopMessage() {
		if (!stopMessageSent) {
			stopMessageSent = true;
			startMessageSent = false;
			return true;
		}
		return false;
	}

	public void reset() {
		time = 0;
	}

	public long getTime() {
		return time;
	}
}
