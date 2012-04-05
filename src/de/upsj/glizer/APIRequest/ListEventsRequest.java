package de.upsj.glizer.APIRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class ListEventsRequest extends APIRequest {
	CommandSender sender;
	JSONObject result;

	public ListEventsRequest(CommandSender sender) {
		this.sender = sender;
	}

	@Override
	public void process() {
		String ip = sender instanceof Player ? bConnector
				.getPlayerIPAddress((Player) sender) : "1.1.1.1";
		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "events");
		url_items.put("do", "list");
		url_items.put("account", (sender instanceof Player ? ((Player) sender).getName() : "server"));
		url_items.put("ip", ip);

		result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
		if (result == null)
		{
			bChat.sendMessage(sender, Language.GetTranslated("other.not_available")/*"&6Not available at the moment"*/);
			return;
		}
		String check = bConnector.checkResult(result);

		if (check.equals("no record")) {
			bChat.sendMessage(sender,
					Language.GetTranslated("event.noevents"));
			return;
		}
		try {
			int length = result.getInt("_size");

			if (length > 0) {
				for (int i = 0; i < length; i++) {
					JSONObject o = result.getJSONObject(String.valueOf(i));
					long beginn = o.getLong("beginnt");
					Date d = new Date(beginn);
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
					sdf.setTimeZone(TimeZone.getDefault());

					bChat.sendMessage(sender, Language.GetTranslated(
							"event.list_title",
							new String[] { o.getString("titel"),
									o.getString("server") }));
					bChat.sendMessage(sender, Language.GetTranslated("event.list_start", 
							new String[]{sdf.format(d)}));
					bChat.sendMessage(sender, Language.GetTranslated("event.list_id", 
							new String[]{""+o.getInt("id")}));
				}
			}
		} catch (Exception e) {
			if(glizer.D) e.printStackTrace();
		}
	}

}
