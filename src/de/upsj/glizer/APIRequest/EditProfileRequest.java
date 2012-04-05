package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class EditProfileRequest extends APIRequest {
	String field;
	String value;
	String player;
	Player p;
	JSONObject result;
	
	public EditProfileRequest(String field, String value, Player p)
	{
		this.field = field;
		this.value = value;
		this.player = p.getName();
		this.p = p;
	}

	@Override
	public void process() {
		if (!p.isOnline())
			return;
		String ip = bConnector.getPlayerIPAddress(p);
        
        HashMap<String, String> url_items = new HashMap<String, String>();
        url_items.put("exec", "profile");
        url_items.put("do", "edit");
        url_items.put("account", player);
        url_items.put("ip", ip);
        url_items.put("name", player);
        url_items.put("field", field);
        url_items.put("value", value);
        
        result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
		if (result == null)
		{
			bChat.sendMessage(p, Language.GetTranslated("other.not_available")/*"&6Not available at the moment"*/);
			return;
		}
		if (!p.isOnline())
			return;
        String check = bConnector.checkResult(result);
        
        if(check.equalsIgnoreCase("ok")) {
            if(glizer.D) bChat.log("Profile edit action done");
            bChat.sendMessageToPlayer(p, "&6Done");
        }
        else if(check.equalsIgnoreCase("not allowed")) {
            if(glizer.D) bChat.log("Profile edit cant be done, its not allowed to edit this profile field");
            bChat.sendMessageToPlayer(p, "&6Error, its not allowed to edit this profile field");
        }
        else if(check.equalsIgnoreCase("wrong data type")) {
            if(glizer.D) bChat.log("Profile edit cant be done, wrong data type sent");
            bChat.sendMessageToPlayer(p, "&6Error, wrong data type");
        }
	}

}
