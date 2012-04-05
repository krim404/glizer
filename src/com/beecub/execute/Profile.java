package com.beecub.execute;

import org.bukkit.entity.Player;
import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;

import de.upsj.glizer.APIRequest.EditProfileRequest;
import de.upsj.glizer.APIRequest.ShowProfileRequest;

public class Profile {

    public static boolean profile(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            String recipient = player.getName();
            if (args.length == 1)
            	recipient = args[0];
            if (args.length <= 1) {
            	glizer.queue.add(new ShowProfileRequest(player, recipient));
                return true;
            }
            bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
            bChat.sendMessageToPlayer(player, "&6/profile&e [playername]");
            return true;
        }
        return true;
    }
    
    public static boolean editprofile(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            if(args.length >= 2) {
                String field = args[0];
                String message = "";
                for(int i = 1; i < args.length; i++) {
                    message += args[i] + " ";
                }
                glizer.queue.add(new EditProfileRequest(message, field, player));
                return true;
                
            }
            bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
            bChat.sendMessageToPlayer(player, "&6/editprofile&e [field] [value]");
            return true;
        }
        return true;
    }
}
