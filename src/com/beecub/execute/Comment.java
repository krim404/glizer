package com.beecub.execute;

import org.bukkit.entity.Player;

import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;

import de.upsj.glizer.APIRequest.NoteAddRequest;
import de.upsj.glizer.APIRequest.NoteRequest;

public class Comment {

    public static boolean comment(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            if(args.length >= 2) {
                String message = "";
                String recipient = args[0];
                for(int i = 1; i < args.length; i++) {
                    message += args[i] + " ";
                }
                if(message != null && message != "") {
                    glizer.queue.add(new NoteAddRequest(player, recipient, message, NoteAddRequest.Comment, 0, 0, null));
                    return true;
                }
            }
            bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
            bChat.sendMessageToPlayer(player, "&6/comment&e [playername] [message]");
            return true;
        }
        return true;
    }
    
    public static boolean comments(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            if(args.length >= 1) {
                int page = 0;
                if(args.length == 2) {
                    try {
                        page = Integer.valueOf(args[1]);
                        page = page - 1;
                    }
                    catch(Exception e) {
                        bChat.sendMessageToPlayer(player, "&6This is not a Integer value: &e" + args[1]);
                        return false;
                    }
                }
                glizer.queue.add(new NoteRequest(player, args[0], page, NoteRequest.Comments));
                return true;
            }
            bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
            bChat.sendMessageToPlayer(player, "&6/comments&e [playername] [(page)]");
            return true;
        }
        return true;
    }
}
