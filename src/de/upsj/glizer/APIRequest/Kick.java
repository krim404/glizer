package de.upsj.glizer.APIRequest;

import org.bukkit.entity.Player;

public class Kick extends APIRequest {
	String message;
	Player player;

	public Kick(String replace, Player player) {
		message = replace;
		this.player = player;
	}

	@Override
	public void process() {
	}

	@Override
	public void postProcess() {
		player.kickPlayer(message);
	}

}
