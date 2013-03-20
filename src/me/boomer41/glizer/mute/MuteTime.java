package me.boomer41.glizer.mute;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.beecub.util.Language;
import com.beecub.util.bChat;

public class MuteTime {
	
	private boolean isTemporary = false;
	private String user;
	private double time;
	private boolean isActive = true;
	private String reason;
	private String muter;
	
	public MuteTime(String user, String muter, double time, String reason) {
		this.user = user;
		if (time != -1) isTemporary = true;
		this.time = time;
		this.reason = reason;
		this.muter = muter;
	}
	
	public void tick() {
		if (isActive && isTemporary) {
			time--;
			if (time <= 0) {
				isActive = false;
				Player muted = Bukkit.getPlayerExact(user);
				if (muted != null) {
					bChat.sendMessage(muted, Language.GetTranslated("mute.gotunmuted", "server"));
				}
			}
		}
	}
	
	public String getUser() {
		return user;
	}
	
	public double getTimeLeft() {
		return time;
	}
	
	public boolean isTemporary() {
		return isTemporary;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public String getReason() {
		return reason;
	}

	public String getMuter() {
		return muter;
	}
	
	public void disable() {
		isActive = false;
	}
	
}