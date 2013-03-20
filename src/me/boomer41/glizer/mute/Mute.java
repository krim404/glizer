package me.boomer41.glizer.mute;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.beecub.command.bPermissions;
import com.beecub.util.Language;
import com.beecub.util.bChat;


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
					bChat.sendMessage(sender, Language.GetTranslated("mute.playermuted_permanent", user, reason));
					bChat.sendMessage(Bukkit.getPlayerExact(user), Language.GetTranslated("mute.gotmuted_permanent", sender.getName(), reason));
					MuteTime mute = new MuteTime(user, sender.getName(), -1, reason);
					muted.add(mute);
				} else {
					bChat.sendMessage(sender, Language.GetTranslated("mute.player_not_online", user));
				}
			}
		}
		return true;
	}
	
	public static boolean tempmute(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if (args.length >= 2) {
				String reason = "";
				double time = Double.parseDouble(args[1]) * 60;
				if (time < 1) {
					
				} else {
					String user = args[0];
					for(int i = 2; i < args.length; i++) {
						reason += args[i] + " ";
					}
					if (Bukkit.getPlayerExact(user) != null) {
						bChat.sendMessage(sender, Language.GetTranslated("mute.playermuted_temporary", user, String.valueOf(time), reason));
						bChat.sendMessage(Bukkit.getPlayerExact(user), Language.GetTranslated("mute.gotmuted_temporary", sender.getName(), String.valueOf(time), reason));
						MuteTime mute = new MuteTime(user, sender.getName(), time, reason);
						muted.add(mute);
					} else {
						bChat.sendMessage(sender, Language.GetTranslated("mute.player_not_online", user));
					}
				}
			} else {
				bChat.sendMessage(sender, Language.GetTranslated("mute.time_negative"));
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