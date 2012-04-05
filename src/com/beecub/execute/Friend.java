package com.beecub.execute;

import org.bukkit.entity.Player;
import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import de.upsj.glizer.APIRequest.FriendRequest;
import de.upsj.glizer.APIRequest.ListFriendsRequest;

public class Friend {

    public static boolean friends(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            if(args.length == 0) {
            	glizer.queue.add(new ListFriendsRequest(player));
                return true;
            }
            bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
            return true;
        }
        return true;
    }
    
    public static boolean addfriend(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            if(args.length == 1) {
            	glizer.queue.add(new FriendRequest(player, args[0], "add"));
            	return true;
            }
            bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
            return true;
        }
        return true;
    }
    
    public static boolean removefriend(String command, Player player, String[] args) {
        if(bPermissions.checkPermission(player, command)) {
            if(args.length == 1) {
                glizer.queue.add(new FriendRequest(player, args[0], "remove"));
                return true;
            }
            bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
            return true;
        }
        return true;
    }
}
