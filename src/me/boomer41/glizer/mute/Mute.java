package me.boomer41.glizer.mute;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.beecub.command.bPermissions;


public class Mute {

	public static ArrayList<MuteTime> muted;
	public static MuteTimer mutetimer;
	
	static {
		muted = new ArrayList<MuteTime>();
	}
	
	public static boolean mute(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if (args.length >= 2) {
				String reason = "";
				String user = args[0];
				for(int i = 1; i < args.length; i++) {
					reason += args[i] + " ";
				}
				if (Bukkit.getPlayerExact(user) != null) {
					MuteTime mute = new MuteTime(user, sender.getName(), -1, reason);
					muted.add(mute);
				} else {
					
				}
			}
		}
		return true;
	}

	public static boolean isMuted(String name) {
		return getMute(name) == null ? false : true;
	}

	@SuppressWarnings("unchecked")
	public static MuteTime getMute(String name) {
		ArrayList<MuteTime> clone = (ArrayList<MuteTime>) muted.clone(); // Only that we dont get some errors when iterating
		MuteTime theMute = null;
		for (MuteTime mute : clone) {
			if (mute.isActive() && mute.getUser().equalsIgnoreCase(name)) {
				theMute = mute;
			}
		}
		clone = null;
		return theMute;
	}
	
}