package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class ShowProfileRequest extends APIRequest {
	
	Player p;
	String recipient;
	JSONObject result;
	
	public ShowProfileRequest(Player p, String recipient)
	{
		this.p = p;
		this.recipient = recipient;
	}

	@Override
	public void process() {
		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "profile");
        url_items.put("do", "list");
        url_items.put("account", p.getName());
        url_items.put("ip", bConnector.getPlayerIPAddress(p));
        url_items.put("name", recipient);
		result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
		if (result == null)
		{// TODO Language
			bChat.sendMessage(p, "&6Not available at the moment");
			return;
		}
		if (!p.isOnline())
			return;
		if (result == null)
        {
        	bChat.sendMessage(p, "&6There is propably no glizer-registered player with the account name &e" + recipient);
        }
		
        bChat.sendMessage(p, "&6 --- Profile --- ");
        if (result.optInt("developer", 0) == 1)
        	bChat.sendMessage(p, "&3"+recipient+" is a member of the glizer team.");
        bChat.sendMessage(p, "&6Name: &e" + result.optString("name", "unknown"));
        bChat.sendMessage(p, "&6Realname: &e" + result.optString("realname", "unknown"));
        bChat.sendMessage(p, "&6Age: &e" + result.optString("age", "unknown"));
        bChat.sendMessage(p, "&6Last Server: &e" + result.optString("lastserverurl", "unknown"));
        bChat.sendMessage(p, "&6Status: &e" + result.optString("status", "unknown"));
	}

}
