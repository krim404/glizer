package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class LookupAccountsRequest extends APIRequest {
	CommandSender sender;
	String recipient;
	JSONObject result;

	public LookupAccountsRequest(CommandSender sender2, String recipient) {
		this.sender = sender2;
		this.recipient = recipient;
	}

	@Override
	public void process() {
		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "lookup");
		url_items.put("username", recipient);
		url_items.put("ip", sender instanceof Player ? bConnector.getPlayerIPAddress((Player) sender) : "1.1.1.1");
		url_items.put("account", (sender instanceof Player ? ((Player) sender).getName() : "server"));
		result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
		if (result == null)
		{
			bChat.sendMessage(sender, Language.GetTranslated("other.not_available")/*"&6Not available at the moment"*/);
			return;
		}
		try
		{
				if (result.has("error") || result.getInt("_size") == 0)
				{
					bChat.sendMessage(sender, Language.GetTranslated("other.empty_result")/*"&6Nothing found"*/);
					return;
				}
				int length = result.getInt("_size");
				for (int i = 0; i < length; i++)
				{
					JSONArray result2 = result.getJSONArray(i + "");
					bChat.sendMessage(sender, "&6" + result2.getString(0) + ": " + result2.getString(1));
				}
		}
		catch (JSONException e)
		{
			if (glizer.D) e.printStackTrace();
		}
	}

}
