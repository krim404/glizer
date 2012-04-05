package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;


public class NoteRequest extends APIRequest {
	public static final int Notes = 1;
	public static final int LocalNotes = 2;
	public static final int Comments = 3;
	CommandSender sender;
	String recipient;
	int num;
	int type;
	JSONObject result;
	
	public NoteRequest(CommandSender sender, String recipient, int num, int type) {
		this.sender = sender;
		this.recipient = recipient;
		this.num = num;
		this.type = type;
	}

	@Override
	public void process() {
		String typeString = (type != Comments?"admin":"u2u");
		result = getNote(sender, recipient, typeString, (type == LocalNotes?"1":"0")/* 0 = alles */, String.valueOf(num), "5");
	}

	@Override
	public void postProcess() {
		if (result == null)
		{// TODO Language
			bChat.sendMessage(sender, "&6Not available at the moment");
			return;
		}
		if (type == Notes || type == LocalNotes)
		{
			try {
				if (result.has("error"))
				{
					bChat.sendMessage(sender, "&6Nothing found");
				}
				int length = result.getInt("_size");
	            bChat.sendMessage(sender, "&6 --- "+(type == LocalNotes?"Local":"")+" Notes --- ");
				for (int i = 0; i < length; i++) {
					JSONObject result2 = result.getJSONObject(String.valueOf(i));
					if (type == Notes)
					{
						// TODO Language
						bChat.sendMessage(sender, "&eServer: &6" + result2.optString("serverurl", "unknown") 
								+ "&e by &6" + result2.optString("fromuser", "unknown") 
								+ "&e: &6" + result2.optString("message", "- empty -") 
								+ " &e(" + result2.getString("reputation_change") 
								+ "/" + result2.getString("greputation_change") + ")");
					}
					else
					{
						// TODO Language
						bChat.sendMessage(sender, "&eBy &6" + result2.optString("fromuser", "unknown")
								+ "&e: &6" + result2.optString("message", "- empty -") 
								+ " &e(" + result2.getString("reputation_change") 
								+ "/" + result2.getString("greputation_change") + ")");
					}
				}
			} catch (Exception e3) {
				if (glizer.D)
					e3.printStackTrace();
			}
		}
		else
		{
			try {
				int length = result.optInt("_size", 0);
				// TODO Language
	            bChat.sendMessage(sender, "&6 --- Comments --- ");
	            if (length == 0)
	            {
	    			// TODO Language
	            	bChat.sendMessage(sender, "&6Nothing found");
	            	return;
	            }
				for (int i = 0; i < length; i++) {
					JSONObject result2 = result.getJSONObject(String.valueOf(i));
					String note = result2.getString("message");
					if (!note.equalsIgnoreCase("")) {
						bChat.sendMessage(sender, "&6" + result2.getString("message"));
					}
				}
			} catch (Exception e3) {
				if (glizer.D)
					e3.printStackTrace();
			}
		}
	}

	public static JSONObject getNote(CommandSender sender, String recipient,
			String type, String local, String start, String limit) {
		int page = Integer.valueOf(start);
		page = page * 5;
		start = String.valueOf(page);

		String ip = sender instanceof Player ? bConnector.getPlayerIPAddress((Player) sender) : "1.1.1.1";

		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "notes");
		url_items.put("do", "list");
		url_items.put("account", (sender instanceof Player ? ((Player) sender).getName() : "server"));
		url_items.put("ip", ip);
		url_items.put("username", recipient);
		url_items.put("type", type);
		url_items.put("local", local);
		url_items.put("start", start);
		url_items.put("limit", limit);
		return bConnector.hdl_com(url_items);
	}
}
