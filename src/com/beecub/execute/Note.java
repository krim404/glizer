package com.beecub.execute;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;

import de.upsj.glizer.APIRequest.NoteAddRequest;
import de.upsj.glizer.APIRequest.NoteRequest;

public class Note {
    
    public static boolean note(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            if(args.length >= 3) {
                String message = "";
                String recipient = args[0];
                int value = 0;
                try {
                    value = Integer.valueOf(args[1]);
                }
                catch(Exception e) {
                    bChat.sendMessage(player, "&6This is not a Integer value: &e" + args[1]);
                    return false;
                }
                for(int i = 2; i < args.length; i++) {
                    message += args[i] + " ";
                }
                if(message != null && message != "") {
                	glizer.queue.add(new NoteAddRequest(player, recipient, message, NoteAddRequest.Note, value, 0, null));
                	return true;
                }
            }
            bChat.sendMessage(player, Language.GetTranslated("other.wrong_command_usage"));
            bChat.sendMessage(player, "&6/note&e [playername] [reputation|-100 to 100] [message]");
            return true;
        }
        return true;
    }
    
    public static boolean notes(String command, Player player, String[] args, boolean local) {
        if(bPermissions.checkPermission(player, command)) {
        	String recipient = player.getName();
        	if (args.length <= 2)
        	{
            if (args.length == 1)
            	recipient = args[0];
               int page = 0;
                if(args.length == 2) {
                    try {
                        page = Integer.valueOf(args[1]);
                        page = page - 1;
                    }
                    catch(Exception e) {
                        bChat.sendMessage(player, "&6This is not a Integer value: &e" + args[1]);
                        return false;
                    }
                }
                glizer.queue.add(new NoteRequest(player, recipient, page, (local?NoteRequest.LocalNotes:NoteRequest.Notes)));
                return true;
            }
            bChat.sendMessage(player, Language.GetTranslated("other.wrong_command_usage"));
            bChat.sendMessage(player, "&6/warnings&e [playername] [(page)]");
            return true;
        }
        return true;
    }

	public static boolean praise(String command, CommandSender sender, String[] args)
	{
		 if(bPermissions.checkPermission(sender, command)) {
	            if(args.length >= 3) {
	                String message = "";
	                String recipient = args[0];
	                int value = 0;
	                try {
	                    value = Integer.valueOf(args[1]);
	                }
	                catch(NumberFormatException e) {
	                    bChat.sendMessage(sender, "&6 The reputation has to be an Integer value. &e" + args[1] + "&6 isn't one.");
	                    return false;
	                }
	                catch(IllegalArgumentException e)
	                {
	                	bChat.sendMessage(sender, "&6 The reputation chnage has to be positive. &e" + args[1] + "&6 isn't.");
	                    return false;
	                }
	                for(int i = 2; i < args.length; i++) {
	                    message += args[i] + " ";
	                }
	                if(message != null && message != "") {
						bChat.sendMessage(sender, "&ePraise enqueued");
	                	glizer.queue.add(new NoteAddRequest(sender, recipient, message, NoteAddRequest.Praise, value, 0, null));
	                	return true;
	                }
	            }
	            bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
	            bChat.sendMessage(sender, "&6/note&e [playername] [reputation|-100 to 100] [message]");
	            return true;
	        }
	        return true;
		
	}
	
	public static boolean gpraise(String command, CommandSender sender, String[] args)
	{
		if(bPermissions.checkPermission(sender, command)) {
            if(args.length >= 3) {
                String message = "";
                String recipient = args[0];
                int value = 0;
                try {
                    value = Integer.valueOf(args[1]);
                }
                catch(NumberFormatException e) {
                    bChat.sendMessage(sender, "&6 The reputation has to be an Integer value. &e" + args[1] + "&6 isn't one.");
                    return false;
                }
                catch(IllegalArgumentException e)
                {
                	bChat.sendMessage(sender, "&6 The reputation chnage has to be positive. &e" + args[1] + "&6 isn't.");
                    return false;
                }
                for(int i = 2; i < args.length; i++) {
                    message += args[i] + " ";
                }
                if(message != null && message != "") {
					bChat.sendMessage(sender, "&ePraise enqueued");
                	glizer.queue.add(new NoteAddRequest(sender, recipient, message, NoteAddRequest.Note, value, 0, null));
                	return true;
                }
            }
            bChat.sendMessage(sender, Language.GetTranslated("other.wrong_command_usage"));
            bChat.sendMessage(sender, "&6/note&e [playername] [reputation|-100 to 100] [message]");
            return true;
        }
        return true;
	}
}
