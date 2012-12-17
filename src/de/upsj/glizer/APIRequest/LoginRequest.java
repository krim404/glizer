package de.upsj.glizer.APIRequest;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.bBackupManager;
import com.beecub.util.bChat;
import com.beecub.util.bConfigManager;
import com.beecub.util.bConnector;
import com.beecub.util.bWhitelist;

public class LoginRequest extends APIRequest {
	HashMap<String, String> url_items;
	JSONObject result;
	Player target;
	String ip;
	boolean kick = false;
	boolean whitelist = false;
	boolean ipfail = false;

	public LoginRequest(Player player, String IP) {
		target = player;
		ip = IP;
		url_items = new HashMap<String, String>();
		url_items.put("exec", "login");
		url_items.put("ip", "1.1.1.1");
		url_items.put("account", "server");
		url_items.put("username", target.getName());
		if (!bConfigManager.shared_banborder.trim().equals("0"))
			url_items.put("relation", bConfigManager.shared_banborder);
	}

	@Override
	public void process() {
		result = bConnector.hdl_com(url_items);
		if (result != null)
		{
			if (result.optBoolean("banned", false))
			{
				kick = true;
			}
			
			if(result.optBoolean("iplock",false) && bConfigManager.bungiecord == false)
			{
				String tip = result.optString("locip","");
				if(!tip.equals(""))
				{
					if(!ip.startsWith(tip))
					{
						ipfail = true;
					}
				}
			}
			
			if (result.optInt("whitelisted", 0) == 1)
			{
				whitelist = true;
			}
			HashMap<String, String> url_items_ip = new HashMap<String, String>();
			url_items_ip.put("exec", "ip");
			url_items_ip.put("ip", "1.1.1.1");
			url_items_ip.put("account", "server");
			url_items_ip.put("username", target.getName());
			url_items_ip.put("userip", ip);
			if (kick == true)
			{
				url_items_ip.put("frombanned", "true");
			}
			bConnector.hdl_com(url_items_ip);
		}
	}

	@Override
	public void postProcess() {
		if (whitelist && bConfigManager.usewhitelist)
		{
			bWhitelist.addWhiteList(target.getName());
		}
		else
		{
			bWhitelist.removeWhiteList(target.getName());
		}
		if (kick)
		{
			if (target.isOnline())
			{
				target.kickPlayer(bConfigManager.ban_joinmessage);
				bChat.log.log(Level.INFO, "Player " + target.getName() + " is banned. Kick. (delayed)");
			}
			bBackupManager.addBanBackup(target.getName());
			return;
		}
		else if(ipfail)
		{
			if (target.isOnline())
			{
				target.kickPlayer(bConfigManager.ipcheck_joinmessage);
				bChat.log.log(Level.INFO, "Player " + target.getName() + " failed the IP-Check");
			}
		}
		else
		{
			bBackupManager.removeBanBackup(target.getName());
		}
		if (target.isOnline())
		{
			int reputation = result.optInt("local", 0);
			int globalreputation = result.optInt("global", 0);
			if (reputation > 0)
			{
				target.sendMessage(ChatColor.GOLD + "You are locally praised on this server " + ChatColor.YELLOW + "(" + reputation + ")");
			}
			else if (reputation < 0)
			{
				target.sendMessage(ChatColor.GOLD + "You are locally warned on this server " + ChatColor.YELLOW + "(" + reputation + ")");
			}
			
			if (globalreputation > 0)
			{
				target.sendMessage(ChatColor.GOLD + "You are globally praised on glizer " + ChatColor.YELLOW + "(" + globalreputation + ")");
			}
			else if (globalreputation < 0)
			{
				target.sendMessage(ChatColor.GOLD + "You are globally warned on glizer " + ChatColor.YELLOW + "(" + globalreputation + ")");
			}
			
			if (bConfigManager.messageReputation)
			{
				for (Player p : glizer.plugin.getServer().getOnlinePlayers())
				{
					if (p.equals(target))
						continue;
					if (bPermissions.scheckPermission(p, "messagereputation"))
					{
						if (reputation > 0)
						{
							bChat.sendMessage(p, "&ePlayer &6" + target.getName() + " &eis locally praised on this server (" + reputation + ")");
						}
						else if (reputation < 0)
						{
							bChat.sendMessage(p, "&ePlayer &6" + target.getName() + " &eis locally warned on this server (" + reputation + ")");
						}
						
						if (globalreputation > 0)
						{
							bChat.sendMessage(p, "&ePlayer &6" + target.getName() + " &eis globally praised on glizer (" + globalreputation + ")");
						}
						else if (globalreputation < 0)
						{
							bChat.sendMessage(p, "&ePlayer &6" + target.getName() + " &eis globally warned on glizer (" + globalreputation + ")");
						}
					}
				}
			}
		}
	}

}
