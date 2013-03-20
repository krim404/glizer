package com.beecub.util;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.execute.Backup;
import com.beecub.execute.Whitelist;
import com.beecub.glizer.glizer;

public class bTimer extends Thread {
    
	int counter;
    
    public bTimer(){
        counter = 0;
    }
    
    public void run() {
    	while(true)
    	{
 //   		bChat.log("Heartbeat");
	    	Player [] players = glizer.plugin.getServer().getOnlinePlayers();
	        if(heartbeat(players, 5))
	        {
	            if(com.beecub.glizer.glizer.D) bChat.log("Heartbeat failed. Trying again in 1h");
	            try {
					sleep(360000);
				} catch (InterruptedException e) {
					if (com.beecub.glizer.glizer.D) e.printStackTrace();
				}
	        }
	        try {
				sleep(Math.max(0L, 300000));
			} catch (InterruptedException e) {
				if (com.beecub.glizer.glizer.D) e.printStackTrace();
			}
    	}
    }
    
    public boolean heartbeat(Player [] players, int count) {        
        if(count > 0) {
        	counter++;
     //       Whitelist.getPlayers();
        	glizer.banqueue.flushQueue();
        	HashMap<String, String> url_items = new HashMap<String, String>();        
            url_items.put("exec", "heartbeat");
            url_items.put("ip", "1.1.1.1");
            url_items.put("account", "server");
            if (TickCounter.ticks > 0)
            	url_items.put("ticks", String.valueOf(TickCounter.ticks));
            
            String users = "";
            //Player [] players = glizer.getServer().getOnlinePlayers();
            for(Player player : players) {
                users += player.getName() + ",";
                url_items.put("ip_" + player.getName(), bConnector.getPlayerIPAddress(player));
            }
            if(players.length > 0) users = users.substring(0, users.length() - 1);
            url_items.put("users", users);
            
            JSONObject result = bConnector.hdl_com(url_items);
            String check = bConnector.checkResult(result);
            
            if (count % 20 == 0)
            {
				Whitelist.getPlayers();
				Backup.getPlayers();
            }
            
            if(check.equalsIgnoreCase("ok")) {
                bChat.log("Heartbeat, Online Players: " + players.length);
                glizer.offline = false;
                return false;
            }
            else if(check.equalsIgnoreCase("to fast")) {
            	try {
					sleep(300000);
				} catch (InterruptedException e) {
					if (com.beecub.glizer.glizer.D) e.printStackTrace();
				}
            	return false;
            }
            else if(check.equalsIgnoreCase("sql error")) {
                bChat.log("Heartbeat failed, trying again", 2);
                try {
					sleep(15*1000);
				} catch (InterruptedException e) {
					if (com.beecub.glizer.glizer.D) e.printStackTrace();
					return true;
				}
                heartbeat(players, count - 1);
                return false;
            }
            else if(check.equalsIgnoreCase("null")) {
                bChat.log("Heartbeat failed, trying again", 2);
                try {
					sleep(15*1000);
				} catch (InterruptedException e) {
					if (com.beecub.glizer.glizer.D) e.printStackTrace();
					return true;
				}
                heartbeat(players, count - 1);
                return false;
            }
            else {

                bChat.log("Heartbeat: Status unkown");
                return true;
            }
            
            
            /*
            boolean ok = false;          
            
            try {
                // check response
                try {
                    if(result.getString("response").equalsIgnoreCase("ok")) {
                        ok = true;
                    }
                } catch (JSONException e) {
                    if(glizer.D) e.printStackTrace();
                    bChat.log("Unable to do heartbeat", 2);
                }
                // if response != ok, check errno
                if(!ok) {
                    try {
                        // "errno":1005
                        if(result.getInt("errno") == 1005) {
                            return false;
                        }
                    } catch(JSONException e) {
                        if(glizer.D) e.printStackTrace();
                    }
                }
            } catch(Exception e) {
                if(glizer.D) bChat.log("Heartbeat, critical error", 2);
                ok = false;
            }
            
            // print
            if(ok) {
                bChat.log("Heartbeat, Online Players: " + players.length);
            }
            else {
                bChat.log("Heartbeat failed", 2);
                heartbeat(players, count - 1);
            }*/
        }
        else if (count == 0)
        {
            glizer.offline = true;
            return true;
        }
        return true;
    }
}
