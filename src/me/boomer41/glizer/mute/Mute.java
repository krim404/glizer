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
			} else {
				bChat.sendMessage(sender,
						Language.GetTranslated("other.wrong_command_usage"));
				bChat.sendMessage(sender, "&6/mute&e [playername] [reason]");
			}
		}
		return true;
	}
	
	public static boolean tempmute(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if (args.length >= 3) {
				String reason = "";
				double time = Double.parseDouble(args[1]) * 60;
				if (time < 1) {
					bChat.sendMessage(sender, Language.GetTranslated("mute.time_negative"));
				} else {
					String user = args[0];
					for(int i = 2; i < args.length; i++) {
						reason += args[i] + " ";
					}
					if (Bukkit.getPlayerExact(user) != null) {
						bChat.sendMessage(sender, Language.GetTranslated("mute.playermuted_temporary", user, String.valueOf((int) (time / 60)), reason));
						bChat.sendMessage(Bukkit.getPlayerExact(user), Language.GetTranslated("mute.gotmuted_temporary", sender.getName(), String.valueOf((int) (time / 60)), reason));
						MuteTime mute = new MuteTime(user, sender.getName(), time, reason);
						muted.add(mute);
					} else {
						bChat.sendMessage(sender, Language.GetTranslated("mute.player_not_online", user));
					}
				}
			} else {
				bChat.sendMessage(sender,
						Language.GetTranslated("other.wrong_command_usage"));
				bChat.sendMessage(sender, "&6/tempmute&e [playername] [minutes] [reason]");
			}
		}
		return true;
	}
	
	public static boolean unmute(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if (args.length == 1) {
				String user = args[0];
				MuteTime mute = getMute(user);
				if (mute != null) {
					mute.disable();
					bChat.sendMessage(sender, Language.GetTranslated("mute.player_unmuted", user));
					bChat.sendMessage(Bukkit.getPlayerExact(user), Language.GetTranslated("mute.gotunmuted", sender.getName()));
				} else {
					bChat.sendMessage(sender, Language.GetTranslated("mute.player_not_muted", user));
				}
			} else {
				bChat.sendMessage(sender,
						Language.GetTranslated("other.wrong_command_usage"));
				bChat.sendMessage(sender, "&6/unmute&e [playername]");
			}
		}
		return true;
	}

	public static boolean mutes(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if (args.length == 0) {
				bChat.sendMessage(sender, "&6Current Mutes:");
				bChat.sendMessage(sender, "&6User | Temp? | Time left (min) | Muter");
				for (MuteTime m : muted) {
					if (m.isActive()) {
						bChat.sendMessage(sender, "&6" + m.getUser() + " | " + (m.isTemporary() ? "Yes" : "No") + " | " + (m.isTemporary() ? String.valueOf((int) (m.getTimeLeft() / 60)) : "-") + " | " + m.getMuter());
					}
				}
			} else {
				bChat.sendMessage(sender,
						Language.GetTranslated("other.wrong_command_usage"));
				bChat.sendMessage(sender, "&6/mutes");
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
				break;
			}
		}
		clone = null;
		return theMute;
	}
	
}