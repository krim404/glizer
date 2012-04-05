package com.beecub.execute;

import org.bukkit.entity.Player;

import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;

import de.upsj.glizer.APIRequest.OnlineTimeRequest;

public class OnlineTime {
	static public void showOnlineTime(String command, Player player,
			String[] args) {
		if (bPermissions.checkPermission(player, command)) {
			String recipient = "";
			if (args.length == 1) {
				recipient = args[0];
			} else {
				recipient = player.getName();
			}

			glizer.queue.add(new OnlineTimeRequest(player, recipient));
		}
	}
}
