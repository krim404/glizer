package com.beecub.execute;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;

import de.upsj.glizer.APIRequest.NoteAddRequest;

public class Warning {

	public static boolean warn(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if(args.length >= 3) {
				String message = "";
				String recipient = args[0];
				int value = 0;
				try {
					value = Integer.valueOf(args[1]);
					if (value > 0 && (value > 80 || value < 1))
					{
						throw new IllegalArgumentException();
					}
					else if (value <= 0 && (value > -1 || value < -80))
					{
						throw new IllegalArgumentException();
					}
				}
				catch(NumberFormatException e) {
					bChat.sendMessage(sender, "&6This is not a Integer value: &e" + args[1]);
					return false;
				}
				catch (IllegalArgumentException e)
				{
					bChat.sendMessage(sender, "&6The Reputation has to be in a value from 1 to 80");
				}
				for(int i = 2; i < args.length; i++) {
					message += args[i] + " ";
				}
				if(message != null && message != "") {
					bChat.sendMessage(sender, "&eWarn enqueued");
					glizer.queue.add(new NoteAddRequest(sender, recipient, message, NoteAddRequest.Warn, value, 0, null));
				}
				else
				{
					bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
					bChat.sendMessage(sender, "&6/warn&e [playername] [reputation|-100 to 0] [message]");					
				}
				return true;
			}
			bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
			bChat.sendMessage(sender, "&6/warn&e [playername] [reputation|-100 to 0] [message]");
			return true;
		}
		return true;
	}

	public static boolean tempwarn(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if(args.length >= 5) 
			{
				String message = "";
				String recipient = args[0];
				int value = 0;
				try {
					value = Integer.valueOf(args[1]);
					if (value > 0 && (value > 80 || value < 1))
					{
						throw new IllegalArgumentException();
					}
					else if (value <= 0 && (value > -1 || value < -80))
					{
						throw new IllegalArgumentException();
					}
				}
				catch(NumberFormatException e) {
					bChat.sendMessage(sender, "&6This is not a Integer value: &e" + args[1]);
					return false;
				}
				catch (IllegalArgumentException e)
				{
					bChat.sendMessage(sender, "&6The Reputation has to be in a value from 1 to 80 or -1 to -80");
				}
				String stime  = args[2];
				String timetype = args[3];
				double dtime;
				try 
				{
					dtime = Double.valueOf(stime);
				}
				catch(Exception e)
				{
					bChat.sendMessage(sender, "&6Error, wrong time value");
					return true;
				}
				if(timetype.equalsIgnoreCase("minutes") || timetype.equalsIgnoreCase("m") || timetype.equals("minute") || timetype.equalsIgnoreCase("min")||timetype.equalsIgnoreCase("mins")) {
					dtime = dtime * 60;
				}
				else if(timetype.equalsIgnoreCase("hours") || timetype.equalsIgnoreCase("h") || timetype.equalsIgnoreCase("hrs")) {
					dtime = dtime * 60 * 60;
				}
				else if(timetype.equalsIgnoreCase("days") || timetype.equalsIgnoreCase("d") || timetype.equalsIgnoreCase("day")) {
					dtime = dtime * 60 * 60 * 24;
				}
				else 
				{
					bChat.sendMessage(sender, "&6Error, wrong time value");
					return true;
				}
				for(int i = 4; i < args.length; i++) {
					message += args[i] + " ";
				}

				if(message != null && message != "") {
					bChat.sendMessage(sender, "&eWarn enqueued");
					glizer.queue.add(new NoteAddRequest(sender, recipient, message, NoteAddRequest.TempWarn, value, (int) dtime, stime + timetype));
				}
				else
				{
					bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
					bChat.sendMessage(sender, "&6/tempwarn&e [playername] [NegativeReputation|0 to 100] [duration] [durationUnit | minutes, hours, days] [message]");					
				}
				return true;
			}
			bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
			bChat.sendMessage(sender, "&6/tempwarn&e [playername] [NegativeReputation|0 to 100] [duration] [durationUnit | minutes, hours, days] [message]");
			return true;
		}
		return true;
	}


	public static boolean warnings(String command, Player player, String[] args, boolean local) {
		if(bPermissions.checkPermission(player, command)) {
			Note.notes(command, player, args, local);
			/*
            if(bPermissions.checkPermission(player, command)) {
                if(args.length == 1) {
                    //String result = Ban.getNote(player, args[0], "0", "1", "1", "", "0", "0");
                    //bChat.sendMessageToPlayer(player, result);
                    return true;
                }
                bChat.sendMessageToPlayer(player, bMessageManager.messageWrongCommandUsage);
                return true;
            }
			 */
			return true;
		}
		return true;
	}

	public static boolean gwarn(String command, CommandSender sender, String[] args) {
		if(bPermissions.checkPermission(sender, command)) {
			if(args.length >= 3) {
				String message = "";
				String recipient = args[0];
				int value = 0;
				try {
					value = Integer.valueOf(args[1]);
					if (value > 0 && (value > 80 || value < 1))
					{
						throw new IllegalArgumentException();
					}
					else if (value <= 0 && (value > -1 || value < -80))
					{
						throw new IllegalArgumentException();
					}
				}
				catch(NumberFormatException e) {
					bChat.sendMessage(sender, "&6This is not a Integer value: &e" + args[1]);
					return false;
				}
				catch (IllegalArgumentException e)
				{
					bChat.sendMessage(sender, "&6The Reputation has to be in a value from 1 to 80");
				}
				for(int i = 2; i < args.length; i++) {
					message += args[i] + " ";
				}
				if(message != null && message != "") {
					bChat.sendMessage(sender, "&eGWarn enqueued");
					glizer.queue.add(new NoteAddRequest(sender, recipient, message, NoteAddRequest.GWarn, value, 0, null));
				}
				else
				{
					bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
					bChat.sendMessage(sender, "&6/gwarn&e [playername] [reputation|-100 to 0] [message]");
				}
				return true;
			}
			bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
			bChat.sendMessage(sender, "&6/gwarn&e [playername] [reputation|-100 to 0] [message]");
			return true;
		}
		return true;
		
	}
}
