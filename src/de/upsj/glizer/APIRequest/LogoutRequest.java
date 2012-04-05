package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.beecub.util.bConnector;

public class LogoutRequest extends APIRequest {
	HashMap<String, String> url_items;

	public LogoutRequest(Player player) {
		url_items = new HashMap<String, String>();
		url_items.put("exec", "logout");
		url_items.put("ip", "1.1.1.1");
		url_items.put("account", "server");
		url_items.put("username", player.getName());
	}

	@Override
	public void process() {
		bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {
	}

}
