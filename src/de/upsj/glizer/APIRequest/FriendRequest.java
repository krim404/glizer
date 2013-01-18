package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class FriendRequest extends APIRequest {
	String action;
	String friend;
	Player player;
	JSONObject result;

	public FriendRequest(Player player, String string, String string2) {
		this.player = player;
		this.friend = string;
		this.action = string2;
	}

	@Override
	public void process() {
        
        String ip = bConnector.getPlayerIPAddress(player);
        
        HashMap<String, String> url_items = new HashMap<String, String>();
        url_items.put("exec", "friends");
        url_items.put("do", action);
        url_items.put("account", player.getName());
        url_items.put("ip", ip);
        url_items.put("name", friend);

        result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
		if (result == null)
		{
			bChat.sendMessage(player, Language.GetTranslated("other.not_available")/*"&6Not available at the moment"*/);
			return;
		}
		String check = bConnector.checkResult(result);
        
        if(check.equalsIgnoreCase("ok")) {
            if(glizer.D) bChat.log("Friend action done");
        }
        else if(check.equalsIgnoreCase("never connected")) {
            if(glizer.D) bChat.log("Friend action cant be done, player never connected to this server");
            bChat.sendMessage(player, "&6Error, Player &e" + friend + "&6 was never connected to this server");
            return;
        }
        else if(check.equalsIgnoreCase("not yourself")) {
            if(glizer.D) bChat.log("Friend action cant be done, not to command user himself");
            bChat.sendMessage(player, "&6Error, you cant do this to yourself");
            return;
        }
        if (action.equalsIgnoreCase("add"))
        {
            bChat.sendMessage(player, "&6Added Friend");
        }
        if (action.equalsIgnoreCase("remove"))
        {
            bChat.sendMessage(player, "&6Removed Friend");
        }
	}
}
