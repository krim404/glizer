package com.beecub.command;

import me.boomer41.glizer.mute.Mute;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.beecub.execute.Ban;
import com.beecub.execute.Comment;
import com.beecub.execute.Friend;
import com.beecub.execute.Note;
import com.beecub.execute.OnlineTime;
import com.beecub.execute.Other;
import com.beecub.execute.Profile;
import com.beecub.execute.Rating;
import com.beecub.execute.Warning;
import com.beecub.execute.Whitelist;
import com.beecub.execute.gEvent;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConfigManager;

public class bCommandRouter {
	public static boolean handleCommands(CommandSender sender, Command c,
			String commandLabel, String[] args) {

		String ret = "disabled";
		String command = c.getName().toLowerCase();
		if (glizer.D)
			bChat.log("Command: " + command);

		if (command.equalsIgnoreCase("ban")) {
			if (bConfigManager.usebansystem) {
				Ban.ban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("kick")) {
			Ban.kick(command, sender, args, false);
			return true;
		} else if (command.equalsIgnoreCase("skick")) {
			Ban.kick(command, sender, args, true);
			return true;
		} else if (command.equalsIgnoreCase("globalban") || command.equalsIgnoreCase("gban")) {
			if (bConfigManager.usebansystem && bConfigManager.useglobalbans) {
				Ban.globalban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("localban") || command.equalsIgnoreCase("lban")) {
			if (bConfigManager.usebansystem) {
				Ban.localBan(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("forceban")) {
			if (bConfigManager.usebansystem) {
				Ban.forceBan(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("tempban") || command.equalsIgnoreCase("timeban")) {
			if (bConfigManager.usebansystem) {
				Ban.tempban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("unban")) {
			if (bConfigManager.usebansystem) {
				Ban.unban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("tempwarn") || command.equalsIgnoreCase("timewarn")) {
			Warning.tempwarn(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("warn")) {
			Warning.warn(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("gwarn") || command.equalsIgnoreCase("globalwarn")) {
			Warning.gwarn(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("praise")) {
			Note.praise(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("gpraise") || command.equalsIgnoreCase("globalpraise")) {
			Note.gpraise(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("glizer")) {
			Other.glizer(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("glizerreload")) {
			Other.glizerreload(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("glizerhelp")) {
			Other.glizerhelp(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("addwhitelist")) {
			if (bConfigManager.usewhitelist) {
				Whitelist.whitelistAdd(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("removewhitelist")) {
			if (bConfigManager.usewhitelist) {
				Whitelist.whitelistRemove(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("abuse")) {
			Other.abuse(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("eventlist")) {
			if (bConfigManager.useevents) {
				gEvent.ListEvents(sender, command, args);
				return true;
			}
			return false;
		} else if (command.equalsIgnoreCase("eventdetail")) {
			if (bConfigManager.useevents) {
				gEvent.EventDetails(sender, command, args);
				return true;
			}
		} else if (command.equalsIgnoreCase("listaccounts")) {
			Other.lookup(command, sender, args);
			return true;
		} else if (command.equalsIgnoreCase("mute")) {
			Mute.mute(command, sender, args);
		} else if (command.equalsIgnoreCase("tempmute")) {
			Mute.tempmute(command, sender, args);
		} else if (command.equalsIgnoreCase("unmute")) {
			Mute.unmute(command, sender, args);
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (command.equals("")) {
				// not used
				return true;
			}
			/*
			 * else if(command.equals("note")) { Note.note(command, player,
			 * args); return true; } else if(command.equals("notes")) {
			 * Note.notes(command, player, args); return true; }
			 */
			else if (command.equalsIgnoreCase("onlinetime")) {
				OnlineTime.showOnlineTime(command, player, args);
				return true;
			} else if (command.equalsIgnoreCase("warnings")) {
				Warning.warnings(command, player, args, false);
				return true;
			} else if (command.equalsIgnoreCase("localwarnings"))	{
				Warning.warnings(command, player, args, true);
				return true;
			} else if (command.equalsIgnoreCase("comment")) {
				if (bConfigManager.usecomments) {
					Comment.comment(command, player, args);
					return true;
				}
			} else if (command.equalsIgnoreCase("comments")) {
				if (bConfigManager.usecomments) {
					Comment.comments(command, player, args);
					return true;
				}
			} else if (command.equalsIgnoreCase("rateserver")) {
				if (bConfigManager.useratings) {
					Rating.rateserver(command, player, args);
					return true;
				}
			} else if (command.equalsIgnoreCase("profile")) {
				if (bConfigManager.useprofiles) {
					Profile.profile(command, player, args);
					return true;
				}
			} else if (command.equalsIgnoreCase("editprofile")) {
				if (bConfigManager.useprofiles) {
					Profile.editprofile(command, player, args);
					return true;
				}
			} else if (command.equalsIgnoreCase("addfriend")) {
				if (bConfigManager.useprofiles) {
					Friend.addfriend(command, player, args);
					return true;
				}
			} else if (command.equalsIgnoreCase("removefriend")) {
				if (bConfigManager.useprofiles) {
					Friend.removefriend(command, player, args);
					return true;
				}
			} else if (command.equalsIgnoreCase("friends")) {
				if (bConfigManager.useprofiles) {
					Friend.friends(command, player, args);
					return true;
				}
			}

			else if (command.equalsIgnoreCase("registerevent")) {
				if (bConfigManager.useevents) {
					gEvent.RegisterForEvent(player, command, args);
					return true;
				}
				return false;
			} else if (command.equalsIgnoreCase("unregisterevent")) {
				if (bConfigManager.useevents) {
					gEvent.UnregisterFromEvent(player, command, args);
					return true;
				}
				return false;
			} else if (command.equalsIgnoreCase("myevents")) {
				if (bConfigManager.useevents) {
					gEvent.MyRegistered(player, command, args);
					return true;
				}
				return false;
			} else if (command.equalsIgnoreCase("theanswertolifetheuniverseandeverything")) {
				Other.theanswertolifetheuniverseandeverything(command, player,
						args);
				return true;
			} else {
				bChat.sendMessageToPlayer(player, Language.GetTranslated("other.not_a_glizer_command"));
				return true;
			}
			if (ret.equalsIgnoreCase("disabled")) {
				bChat.sendMessageToPlayer(player,Language.GetTranslated("other.feature_disabled"));
				return true;
			} else {
				return true;
			}

		} else {
			bChat.sendMessageToServer(Language.GetTranslated("other.command_from_console"));
			return true;
		}
	}
}
