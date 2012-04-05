package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class RateServerRequest extends APIRequest {
	HashMap<String, String> url_items;
	JSONObject result;
	Player p;
	
	public RateServerRequest(Player p, int rating, String comment)
	{
		this.p = p;
		String ip = bConnector.getPlayerIPAddress(p);
		url_items = new HashMap<String, String>();
		url_items.put("exec", "rate");
		url_items.put("do", "add");
		url_items.put("account", p.getName());
		url_items.put("ip", ip);
		url_items.put("rep", rating + "");
		url_items.put("text", comment);
	}

	@Override
	public void process() {
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
		String check = bConnector.checkResult(result);

		if(check.equalsIgnoreCase("ok")) {
			if(glizer.D) bChat.log("Server rate action done");
			bChat.sendMessageToPlayer(p, "&6Successfully added your Rating");
		}
		else if(check.equalsIgnoreCase("wrong data type")) {
			if(glizer.D) bChat.log("Server rate action cant be done, wrong data type sent");
			bChat.sendMessageToPlayer(p, "&6Error, wrong data type");
		}		
	}

}
