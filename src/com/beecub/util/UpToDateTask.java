package com.beecub.util;

import org.bukkit.entity.Player;

import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;

public class UpToDateTask implements Runnable {

	@Override
	public void run() {
		if (!glizer.upToDate)
		{
			Player[] pl = glizer.plugin.getServer().getOnlinePlayers();
			for (Player p : pl)
			{
				if (bPermissions.scheckPermission(p, "glizerupdate"))
				{
					bChat.sendMessageToPlayer(p, "&eA new version of glizer is available! You should update soon!");
				}
			}
		}
	}

}
