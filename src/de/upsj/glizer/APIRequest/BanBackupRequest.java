package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.bBackupManager;
import com.beecub.util.bChat;
import com.beecub.util.bConnector;

public class BanBackupRequest extends APIRequest {
	JSONObject result;
	@Override
	public void process() {
		HashMap<String, String> url_items = new HashMap<String, String>();
        url_items.put("exec", "export");
        url_items.put("do", "list");
        url_items.put("account", "server");
        url_items.put("ip", "1.1.1.1");
        url_items.put("withtemp", "true");
        
        result = bConnector.hdl_com(url_items);        
	}

	@Override
	public void postProcess() {
		try {
            int length = result.optInt("_size", 0);
            if(length > 0) {
                bBackupManager.clearBanList();
                for(int i = 0; i < length; i++) {
                    String player = result.getString(String.valueOf(i));
                    bBackupManager.addBanBackup(player);
                    if (glizer.D) bChat.log("Adding " + player + " to ban backup");
                }
            }
            else
            {
            	bChat.log("Banlist empty");
            	return;
            }
            bChat.log("Banlist loaded successfully");
        }
        catch (Exception e)
        {
            if(glizer.D) e.printStackTrace();
            bChat.log("Failed to load banlist", 2);
        }
	}
}
