package de.upsj.glizer.APIRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class EventDetailsRequest extends APIRequest {
	CommandSender sender;
	int ID;
	JSONObject result;
	
	public EventDetailsRequest(CommandSender sender, int ID) {
		this.sender = sender;
		this.ID = ID;
	}

	@Override
	public void process() {
		String ip = sender instanceof Player ? bConnector
				.getPlayerIPAddress((Player) sender) : "1.1.1.1";
		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "events");
		url_items.put("do", "detail");
		url_items.put("eventid", ID + "");
		url_items.put("account",
				(sender instanceof Player ? ((Player) sender).getName()
						: "server"));
		url_items.put("ip", ip);

		result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
		if (sender instanceof Player && !((Player) sender).isOnline())
			return;
		if (result == null)
		{
			bChat.sendMessage(sender, Language.GetTranslated("other.not_available")/*"&6Not available at the moment"*/);
			return;
		}
		String check = bConnector.checkResult(result);

		if (check.equals("no record")) {
			bChat.sendMessage(sender, Language.GetTranslated(
					"event.not_with_that_id", new String[] { ID + "" }));
			return;
		}

		try {
			String text = result.getString("text");
			String titel = result.getString("titel");
			String server = result.getString("server");
			Date beginnt = new Date(result.getLong("beginnt")), endet = new Date(
					result.getLong("endet"));
			String id = result.getString("id");

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

			bChat.sendMessage(sender, "&6" + titel);
			bChat.sendMessage(sender, "&6ID: &e" + id);
			bChat.sendMessage(sender, Language.GetTranslated(
					"event.starts_with",
					new String[] { sdf.format(beginnt) }));
			bChat.sendMessage(sender, Language.GetTranslated(
					"event.ends_with", new String[] { sdf.format(endet) }));
			bChat.sendMessage(sender, Language.GetTranslated(
					"event.takes_place_on", new String[] { server }));

			String[] strings = text.split("\\r\\n");
			for (int i = 0; i < strings.length; i++) {
				bChat.sendMessage(sender, "&6" + strings[i]);
			}

			return;

		} catch (Exception e) {
			bChat.sendMessage(sender, Language.GetTranslated("event.failed_to_retrieve"));
			return;
		}

	}

}
