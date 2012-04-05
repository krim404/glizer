package com.beecub.execute;

import org.bukkit.entity.Player;
import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;

import de.upsj.glizer.APIRequest.RateServerRequest;

public class Rating {
	public static boolean rateserver(String command, Player player, String[] args) {
		if(bPermissions.checkPermission(player, command)) {
			if(args.length >= 1) {
				try {
					int rating = Integer.valueOf(args[0]);
					if(rating >= 0) {
						if(rating <= 10) 
						{
							String message = "";
							if (args.length > 1)
							{
								for (int i = 1; i<args.length; i++)
								{
									message += args[i] + " ";
								}
							}
							glizer.queue.add(new RateServerRequest(player, rating, message));
							return true;
						}
					}
					bChat.sendMessageToPlayer(player, "&6Rating has to be between &e0&6 and &e10&6 (10 is best)");
					return true;
				}
				catch(Exception e) {
					bChat.sendMessageToPlayer(player, "&6This is not a Integer value: &e" + args[0]);
					return true;
				}
			}
			bChat.sendMessageToPlayer(player, Language.GetTranslated("other.wrong_command_usage"));
			bChat.sendMessageToPlayer(player, "&6/rateserver &e[value|0-10 (10 is best)]");
			return true;
		}
		return true;
	}
}
