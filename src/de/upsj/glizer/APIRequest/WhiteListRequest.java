package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;
import com.beecub.util.bWhitelist;

public class WhiteListRequest extends APIRequest {
	public static final int Add = 0;
	public static final int Remove = 1;
	public static final int Load = 2;
	String senderName;
	String ip;
	String recipient;
	CommandSender sender;
	boolean result;
	JSONObject json;
	int type;

	public WhiteListRequest(CommandSender sender, String recipient, int type) {
		this.sender = sender;
		this.senderName = sender instanceof Player ? sender.getName() : "server";
		this.ip = sender instanceof Player ? bConnector.getPlayerIPAddress((Player) sender) : "1.1.1.1";
		this.recipient = recipient;
		this.type = type;
	}

	@Override
	public void process() {
		if (type == Load) {
			HashMap<String, String> url_items = new HashMap<String, String>();
			url_items.put("exec", "export_whitelist");
			url_items.put("do", "list");
			url_items.put("account", "server");
			url_items.put("ip", "1.1.1.1");

			json = bConnector.hdl_com(url_items);
		} else {
			if (type != Add && type != Remove)
				return;
			result = whitelist(senderName, ip, recipient, type == Add ? "add" : "remove");
		}
	}

	@Override
	public void postProcess() {
		if (type == Add)
		{
			if (result)
			{
				bWhitelist.addWhiteList(recipient);
				bChat.sendMessage(sender, "&6Added to Whitelist");
			} 
			else
			{
				bChat.sendMessage(sender, "&6Cannot add this player to whitelist");
			}
		}
		
		if (type == Remove)
		{
			if (result) {
				bWhitelist.removeWhiteList(recipient);
				bChat.sendMessage(sender, "&6Removed from Whitelist");
			} else {
				bChat.sendMessage(sender, "&6Cannot remove this player from whitelist");
			}
		}
		
		if (type == Load)
		{
			try {
				int length = json.optInt("_size", 0);
				if(length > 0) {
					bWhitelist.clearWhitelist();
					for(int i = 0; i < length; i++) {
						String player = json.getString(String.valueOf(i));
						bWhitelist.addWhiteList(player, false);
					}
				}
				else
				{
		            bChat.log("Whitelist empty");
		            return;
				}
	            bChat.log("Whitelist loaded successfully");
			} catch (Exception e3) {
				if(glizer.D) e3.printStackTrace();
	            bChat.log("Failed to load Whitelist", 2);
			}
		}
	}

	private static boolean whitelist(String sender, String ip,
			String recipient, String action) {

		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "whitelist");
		url_items.put("do", action);
		url_items.put("account", sender);
		url_items.put("ip", ip);
		url_items.put("name", recipient);

		JSONObject result = bConnector.hdl_com(url_items);
		String ok = null;
		try {
			ok = result.getString("response");
		} catch (Exception e) {
			if (glizer.D)
				e.printStackTrace();
			return false;
		}
		if (ok.equalsIgnoreCase("ok")) {
			if (glizer.D)
				bChat.log("Whitelist action done.");
			return true;
		} else {
			if (glizer.D)
				bChat.log("Whitelist action cant be done", 2);
			return false;
		}
	}
}
