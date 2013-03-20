package me.boomer41.glizer.mute;

import java.util.TimerTask;

import org.bukkit.Bukkit;

import com.beecub.glizer.glizer;

public class MuteTimer extends TimerTask {

	private boolean alive = true;

	@Override
	public void run() {
			if (alive) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(glizer.plugin, new Runnable() {
					@Override
					public void run() {
						for (MuteTime mute : Mute.muted) {
							mute.tick();
					}
				}
			});
		}
	}

}
