package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class ListFriendsRequest extends APIRequest {
	Player player;
	JSONObject result;

	public ListFriendsRequest(Player player) {
		this.player = player;
	}

	@Override
	public void process() {
		String ip = bConnector.getPlayerIPAddress(player);        
        HashMap<String, String> url_items = new HashMap<String, String>();
        url_items.put("exec", "friends");
        url_items.put("do", "list");
        url_items.put("account", player.getName());
        url_items.put("ip", ip);
        url_items.put("name", player.getName());
        url_items.put("start", "0");
        url_items.put("limit", "20");        
        result = bConnector.hdl_com(url_items);
	}

	@Override
	public void postProcess() {

		if (result == null)
		{
			bChat.sendMessage(player, Language.GetTranslated("other.not_available")/*"&6Not available at the moment"*/);
			return;
		}
		try {
            int length = result.getInt("_size");
            String friends = ""; bChat.sendMessage(player, "&6Your Friends &e   (&2online&6:&enot online)");
            for(int i = 0; i < length; i++) {
                JSONObject result2 = result.getJSONObject(String.valueOf(i));
                friends += (result2.getInt("online")==1?"&2":"&e") +result2.getString("friend") + ", ";
            }
            friends = friends.substring(0,  friends.length() - 2);
            bChat.sendMessage(player, friends);
        } catch (Exception e) {
            if(glizer.D) e.printStackTrace();
            bChat.sendMessage(player, "&6You haven't added any friends yet.");
        }
	}

}
