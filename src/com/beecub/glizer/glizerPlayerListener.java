package com.beecub.glizer;

import java.util.HashMap;
import java.util.Map;

import me.boomer41.glizer.mute.Mute;
import me.boomer41.glizer.mute.MuteTime;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import com.beecub.util.Language;
import com.beecub.util.bBackupManager;
import com.beecub.util.bChat;
import com.beecub.util.bConfigManager;
import com.beecub.util.bConnector;
import com.beecub.util.bWhitelist;

import de.upsj.glizer.APIRequest.LoginRequest;
import de.upsj.glizer.APIRequest.LogoutRequest;

public class glizerPlayerListener implements Listener {
	Map<String, String> playerIPs = new HashMap<String, String>();
	glizer glz;
	public glizerPlayerListener(glizer glizer) 
	{
		this.glz = glizer;
	}

	@EventHandler
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event)
	{
		playerIPs.put(event.getName().toLowerCase(), bConnector.getIPAddress(event.getAddress()));
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
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
		
		String ip = null;
		ip = playerIPs.remove(event.getPlayer().getName().toLowerCase());
		if(ip == null)
			ip = bConnector.getIPAddress(event.getAddress());
		
		if(bConfigManager.noip == true)
		{
			if(!ip.equals("127.0.0.1") || event.getPlayer().getName().equalsIgnoreCase("player"))
			{
				event.disallow(Result.KICK_BANNED, bConfigManager.ipcheck_joinmessage);
				bChat.log(com.beecub.glizer.glizer.messagePluginName + " Bungee Cord error.", 2);
				glz.getServer().getPluginManager().disablePlugin(glz);
			}
		}
		glizer.queue.add(new LoginRequest(event.getPlayer(), ip));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// TODO Language
		bChat.sendMessageToPlayer(event.getPlayer(), "&6This server is running &2glizer - the Minecraft Globalizer&6");
		bChat.sendMessageToPlayer(event.getPlayer(), "&6[GLIZER]: &eYour statistics are globally saved and public visible");
		bChat.sendMessageToPlayer(event.getPlayer(), "&6[GLIZER]: &eFor more informations see www.glizer.de");

	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		glizer.queue.add(new LogoutRequest(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (Mute.isMuted(event.getPlayer().getName())) {
			MuteTime mute = Mute.getMute(event.getPlayer().getName());
			if (mute.isTemporary()) {
				bChat.sendMessage(event.getPlayer(), Language.GetTranslated("mute.muted_temporary")
														.replace("$1", mute.getMuter())
														.replace("$2", String.valueOf((int) (mute.getTimeLeft() / 60)))
														.replace("$3", mute.getReason()));
			} else {
				bChat.sendMessage(event.getPlayer(), Language.GetTranslated("mute.muted_permanent")
														.replace("$1", mute.getMuter())
														.replace("$2", mute.getReason()));
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		String[] command = event.getMessage().split(" ");
		if (bConfigManager.mute_commands.contains(command[0])) {
			if (Mute.isMuted(event.getPlayer().getName())) {
				MuteTime mute = Mute.getMute(event.getPlayer().getName());
				if (mute.isTemporary()) {
					bChat.sendMessage(event.getPlayer(), Language.GetTranslated("mute.muted_temporary",
															mute.getMuter(), 
															String.valueOf((int) (mute.getTimeLeft() / 60)),
															mute.getReason()));
				} else {
					bChat.sendMessage(event.getPlayer(), Language.GetTranslated("mute.muted_permanent",
															mute.getMuter(),
															mute.getReason()));
				}
				event.setCancelled(true);
			}
		}
	}
	
}
