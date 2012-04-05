package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class UnregisterFromEventRequest extends APIRequest {
	Player sender;
	String ID;
	JSONObject result;

	public UnregisterFromEventRequest(Player sender, String iD) {
		this.sender = sender;
		this.ID = iD;
	}

	@Override
	public void process() {
		String ip = sender instanceof Player ? bConnector
				.getPlayerIPAddress((Player) sender) : "1.1.1.1";
		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "events");
		url_items.put("do", "unregister");
		url_items.put("eventid", ID);
		url_items.put("account",
				(sender instanceof Player ? ((Player) sender).getName()
						: "server"));
		url_items.put("ip", ip);

		result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
		if (result == null)
		{// TODO Language
			bChat.sendMessage(sender, "&6Not available at the moment");
			return;
		}
		String check = bConnector.checkResult(result);

		if (check.equals("no record")) {
			bChat.sendMessage(sender, Language.GetTranslated(
					"event.not_with_that_id", new String[] { ID }));
			return;
		}

		bChat.sendMessage(sender,
				Language.GetTranslated("event.suc_unregistered"));
		return;
	}

}
