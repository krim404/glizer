package com.beecub.execute;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.beecub.command.bPermissions;
import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bChat;
import de.upsj.glizer.APIRequest.EventDetailsRequest;
import de.upsj.glizer.APIRequest.InitEventRequest;
import de.upsj.glizer.APIRequest.ListEventsRequest;
import de.upsj.glizer.APIRequest.MyEventsRequest;
import de.upsj.glizer.APIRequest.RegisterForEventRequest;
import de.upsj.glizer.APIRequest.UnregisterFromEventRequest;

public class gEvent {
	public static void RegisterForEvent(Player sender, String command,
			String[] args) {
		if (args.length == 1) {
			if (!bPermissions.checkPermission(sender, command))
				return;

			String ID = "";
			try {
				int id = Integer.parseInt(args[0]);
				ID = Integer.toString(id);
			} catch (Exception e) {
				bChat.sendMessage(sender, Language.GetTranslated(
						"event.id_must_number", new String[] {}));
				return;
			}

			glizer.queue.add(new RegisterForEventRequest(sender, Integer.parseInt(ID)));
			return;
		}

		bChat.sendMessage(sender, "&6/registerevent &e[eventid]");
	}

	public static void EventDetails(CommandSender sender, String command,
			String[] args) {
		if (args.length == 1) {
			if (!bPermissions.checkPermission(sender, command))
				return;

			String ID = "";
			try {
				int id = Integer.parseInt(args[0]);
				ID = Integer.toString(id);
			} catch (Exception e) {
				bChat.sendMessage(sender, Language.GetTranslated(
						"event.id_must_number", new String[] {}));
				return;
			}

			glizer.queue.add(new EventDetailsRequest(sender, Integer.parseInt(ID)));
			return;
		}

		bChat.sendMessage(sender, "&6/eventdetail &e[eventid]");
		return;
	}

	public static void ListEvents(CommandSender sender, String command,
			String[] args) {
		if (!bPermissions.checkPermission(sender, command))
			return;
		
		glizer.queue.add(new ListEventsRequest(sender));
	}

	public static void MyRegistered(Player sender, String command, String[] args) {
		if (!bPermissions.checkPermission(sender, command))
			return;

		glizer.queue.add(new MyEventsRequest(sender));
	}

	public static void UnregisterFromEvent(Player sender, String command,
			String[] args) {
		if (args.length == 1) {
			if (!bPermissions.checkPermission(sender, command))
				return;

			String ID = "";
			try {
				int id = Integer.parseInt(args[0]);
				ID = Integer.toString(id);
			} catch (Exception e) {
				bChat.sendMessage(sender, Language.GetTranslated(
						"event.id_must_number", new String[] {}));
				return;
			}
			
			glizer.queue.add(new UnregisterFromEventRequest(sender, ID));
			return;
		}

		bChat.sendMessage(sender, "&6/unregisterevent &e[eventid]");
	}
	public static void Initevent(Player sender, String command,
			String[] args) {
		if (args.length == 1) {
			if (!bPermissions.checkPermission(sender, command))
				return;
			
			String ID = "";
			try {
				int id = Integer.parseInt(args[0]);
				ID = Integer.toString(id);
			} catch (Exception e) {
				bChat.sendMessage(sender, Language.GetTranslated(
						"event.id_must_number", new String[] {}));
				return;
			}
			
			glizer.queue.add(new InitEventRequest(sender, ID));
			return;
		}

		bChat.sendMessage(sender, "&6/initevent &e[eventid]");
	}

}
