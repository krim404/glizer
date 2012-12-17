package com.beecub.glizer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import com.beecub.util.bBackupManager;
import com.beecub.util.bChat;
import com.beecub.util.bConfigManager;
import com.beecub.util.bConnector;
import com.beecub.util.bWhitelist;

import de.upsj.glizer.APIRequest.LoginRequest;
import de.upsj.glizer.APIRequest.LogoutRequest;

public class glizerPlayerListener implements Listener {
	Map<String, String> playerIPs = new HashMap<String, String>();
	
	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event)
	{
		playerIPs.put(event.getName().toLowerCase(), bConnector.getIPAddress(event.getAddress()));
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (event.getResult() == Result.ALLOWED)
		{
			Player player = event.getPlayer();
			
			if (bConfigManager.usewhitelist) {
				if (bWhitelist.checkWhiteList(player.getName()))
				{
					if (glizer.D)
						bChat.log("Player " + player.getName() + " is whitelisted");
				}
				else
				{
					event.disallow(Result.KICK_WHITELIST, bConfigManager.whitelist_joinmessage);
					if (glizer.D)
						bChat.log("Player " + player.getName() + " isn't whitelisted");
				}
			}
			
			if (event.getResult() == Result.ALLOWED && bBackupManager.checkBanList(player.getName()))
			{
				event.disallow(Result.KICK_BANNED, bConfigManager.ban_joinmessage);
				if (glizer.D)
					bChat.log("Player " + player.getName() + " is banned from this server. Kick", 2);
			}
		}
		glizer.queue.add(new LoginRequest(event.getPlayer(), playerIPs.remove(event.getPlayer().getName().toLowerCase())));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// TODO Language
		bChat.sendMessageToPlayer(event.getPlayer(), "&6This server is running &2glizer - the Minecraft Globalizer&6");
		bChat.sendMessageToPlayer(event.getPlayer(), "&6[GLIZER]: &eYour statistics are globally saved and public visible");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		glizer.queue.add(new LogoutRequest(event.getPlayer()));
	}
}
