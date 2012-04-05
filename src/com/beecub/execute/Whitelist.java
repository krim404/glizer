package com.beecub.execute;

import org.bukkit.command.CommandSender;
import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;

import de.upsj.glizer.APIRequest.WhiteListRequest;

public class Whitelist {

	public static boolean whitelistAdd(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if(args.length == 1) {
				String recipient = args[0];
				glizer.queue.add(new WhiteListRequest(sender, recipient, WhiteListRequest.Add));
				return true;
			}
			bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
			bChat.sendMessage(sender, "&6/note&e [playername]");
			return true;
		}
		return true;
	}

	public static boolean whitelistRemove(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if(args.length == 1) {
				String recipient = args[0];
				glizer.queue.add(new WhiteListRequest(sender, recipient, WhiteListRequest.Remove));
				return true;
			}
			bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
			bChat.sendMessage(sender, "&6/removewhitelist&e [playername]");
			return true;
		}
		return true;
	}
	
	public static void getPlayers()
	{
		glizer.queue.add(new WhiteListRequest(null, null, WhiteListRequest.Load));
	}

}
