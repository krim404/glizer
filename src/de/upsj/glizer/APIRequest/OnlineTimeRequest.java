package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;


public class OnlineTimeRequest extends APIRequest {
	Player p;
	String recipient;
	HashMap<String, String> url_items;
	JSONObject result;

	public OnlineTimeRequest(Player player, String recipient) {
		url_items = new HashMap<String, String>();
		url_items.put("exec", "onlinetime");
		url_items.put("account", player.getName());
		url_items.put("ip", bConnector.getPlayerIPAddress(player));
		url_items.put("username", recipient);
		this.p = player;
		this.recipient = recipient;
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
		try {
			int length = result.getInt("_size");
			if (length > 0) {
				bChat.sendMessage(p, "&6Onlinetimes of &e" + recipient);
				bChat.sendMessage(p,
						Language.GetTranslated("onlinetime.tablecaption", new String[]{}));
				for (int i = 0; i < length; i++) {
					JSONObject o = result.getJSONObject(String.valueOf(i));
					String time = "";
					String unit = "";
					int rawtime = o.getInt("gametime") * 5;
					if (rawtime < 60) {
						unit = "minutes";
						time = Integer.toString(rawtime);
						bChat.sendMessageToPlayer(
								p,
								"&6"
										+ o.getString("server")
										+ spaces(50 - o.getString("server")
												.length()) + "| " + time
										+ " " + unit);
						
					} else if (rawtime >= 60 && rawtime < 1440) {
						unit = "hours";
						time = Integer
								.toString((int) (((float) rawtime) / 60.f));
						bChat.sendMessageToPlayer(
								p,
								"&6"
										+ o.getString("server")
										+ spaces(50 - o.getString("server")
												.length()) + "| " + time
										+ " " + unit);
					} else {
						unit = "days";
						time = Integer
								.toString((int) (((float) rawtime) / 1440.f));
						float hours = ((float) (rawtime % 1440)) / 60;
						bChat.sendMessageToPlayer(
								p,
								"&6"
										+ o.getString("server")
										+ spaces(50 - o.getString("server")
												.length()) + "| " + time
										+ " " + unit + ";"
										+ Integer.toString((int) hours)
										+ " hours");
					}

				}

			} else
				throw new IllegalArgumentException();
		} catch (Exception e) {
			bChat.sendMessageToPlayer(p,
			Language.GetTranslated("other.noglizerregistered", new String[]{recipient}));
		}
	}

	private static String spaces(int i) {
		if (i <= 0)
			return "";
		String tmp = "";
		for (int x = 0; x < i; x++) {
			tmp += " ";
		}
		return tmp;
	}
}
