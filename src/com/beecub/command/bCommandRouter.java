package com.beecub.command;

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

		if (command.equals("ban")) {
			if (bConfigManager.usebansystem) {
				Ban.ban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("kick")) {
			Ban.kick(command, sender, args, false);
			return true;
		} else if (command.equals("skick")) {
			Ban.kick(command, sender, args, true);
			return true;
		} else if (command.equals("globalban") || command.equals("gban")) {
			if (bConfigManager.usebansystem && bConfigManager.useglobalbans) {
				Ban.globalban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("localban") || command.equals("lban")) {
			if (bConfigManager.usebansystem) {
				Ban.localBan(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("forceban")) {
			if (bConfigManager.usebansystem) {
				Ban.forceBan(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("tempban") || command.equals("timeban")) {
			if (bConfigManager.usebansystem) {
				Ban.tempban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("unban")) {
			if (bConfigManager.usebansystem) {
				Ban.unban(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("tempwarn") || command.equals("timewarn")) {
			Warning.tempwarn(command, sender, args);
			return true;
		} else if (command.equals("warn")) {
			Warning.warn(command, sender, args);
			return true;
		} else if (command.equals("gwarn") || command.equals("globalwarn")) {
			Warning.gwarn(command, sender, args);
			return true;
		} else if (command.equals("praise")) {
			Note.praise(command, sender, args);
			return true;
		} else if (command.equals("gpraise") || command.equals("globalpraise")) {
			Note.gpraise(command, sender, args);
			return true;
		} else if (command.equals("glizer")) {
			Other.glizer(command, sender, args);
			return true;
		} else if (command.equals("glizerreload")) {
			Other.glizerreload(command, sender, args);
			return true;
		} else if (command.equals("glizerhelp")) {
			Other.glizerhelp(command, sender, args);
			return true;
		} else if (command.equals("addwhitelist")) {
			if (bConfigManager.usewhitelist) {
				Whitelist.whitelistAdd(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("removewhitelist")) {
			if (bConfigManager.usewhitelist) {
				Whitelist.whitelistRemove(command, sender, args);
				return true;
			}
			return false;
		} else if (command.equals("abuse")) {
			Other.abuse(command, sender, args);
			return true;
		} else if (command.equals("eventlist")) {
			if (bConfigManager.useevents) {
				gEvent.ListEvents(sender, command, args);
				return true;
			}
			return false;
		} else if (command.equals("eventdetail")) {
			if (bConfigManager.useevents) {
				gEvent.EventDetails(sender, command, args);
				return true;
			}
		} else if (command.equals("listaccounts")) {
			Other.lookup(command, sender, args);
			return true;
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
			else if (command.equals("onlinetime")) {
				OnlineTime.showOnlineTime(command, player, args);
				return true;
			} else if (command.equals("warnings")) {
				Warning.warnings(command, player, args, false);
				return true;
			} else if (command.equals("localwarnings"))	{
				Warning.warnings(command, player, args, true);
				return true;
			} else if (command.equals("comment")) {
				if (bConfigManager.usecomments) {
					Comment.comment(command, player, args);
					return true;
				}
			} else if (command.equals("comments")) {
				if (bConfigManager.usecomments) {
					Comment.comments(command, player, args);
					return true;
				}
			} else if (command.equals("rateserver")) {
				if (bConfigManager.useratings) {
					Rating.rateserver(command, player, args);
					return true;
				}
			} else if (command.equals("profile")) {
				if (bConfigManager.useprofiles) {
					Profile.profile(command, player, args);
					return true;
				}
			} else if (command.equals("editprofile")) {
				if (bConfigManager.useprofiles) {
					Profile.editprofile(command, player, args);
					return true;
				}
			} else if (command.equals("addfriend")) {
				if (bConfigManager.useprofiles) {
					Friend.addfriend(command, player, args);
					return true;
				}
			} else if (command.equals("removefriend")) {
				if (bConfigManager.useprofiles) {
					Friend.removefriend(command, player, args);
					return true;
				}
			} else if (command.equals("friends")) {
				if (bConfigManager.useprofiles) {
					Friend.friends(command, player, args);
					return true;
				}
			}

			else if (command.equals("registerevent")) {
				if (bConfigManager.useevents) {
					gEvent.RegisterForEvent(player, command, args);
					return true;
				}
				return false;
			} else if (command.equals("unregisterevent")) {
				if (bConfigManager.useevents) {
					gEvent.UnregisterFromEvent(player, command, args);
					return true;
				}
				return false;
			} else if (command.equals("myevents")) {
				if (bConfigManager.useevents) {
					gEvent.MyRegistered(player, command, args);
					return true;
				}
				return false;
			}

			else if (command.equals("theanswertolifetheuniverseandeverything")) {
				Other.theanswertolifetheuniverseandeverything(command, player,
						args);
				return true;
			} else {
				bChat.sendMessage(player, Language.GetTranslated("other.not_a_glizer_command"));
				return true;
			}
			if (ret.equalsIgnoreCase("disabled")) {
				bChat.sendMessage(player,Language.GetTranslated("other.feature_disabled"));
				return true;
			} else {
				return true;
			}

		} else {
			bChat.sendMessage(Language.GetTranslated("other.command_from_console"));
			return true;
		}
	}
}
