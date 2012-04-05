package de.upsj.glizer.APIRequest;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.Language;
import com.beecub.util.bBackupManager;
import com.beecub.util.bChat;
import com.beecub.util.bConfigManager;
import com.beecub.util.bConnector;

public class NoteAddRequest extends APIRequest {
	public static final int GlobalBan = 0;
	public static final int LocalBan = 1;
	public static final int TempBan = 2;
	public static final int Warn = 3;
	public static final int TempWarn = 4;
	public static final int ForceBan = 8;
	public static final int Note = 5;
	public static final int Praise = 6;
	public static final int Unban = 7;
	public static final int Comment = 9;
	public static final int GWarn = 10;
	
	public static final int TryAgain = -1;
	public static final int NotAllowed = -2;
	public static final int Success = 0;
	int type;
	String recipient;
	CommandSender sender;
	String senderName;
	String reason;
	String ip;
	String result;
	int change;
	int duration;
	String durationText;
	boolean alreadyBanned;
	boolean kick;
	int attempt;
	int status;
	JSONObject resultJSON;
	
	public NoteAddRequest(String senderName, int type, String recipient, String reason, int change, int duration, String ip)
	{
		this.type = type;
		this.change = change;
		this.senderName = senderName;
		this.ip = ip;
		this.recipient = recipient;
		this.reason = reason;
		this.duration = duration;
		this.alreadyBanned = false;
		this.kick = false;
	}
	
	public NoteAddRequest(CommandSender sender, String recipient, String reason, int type, int ReputationChange, int duration, String durationText)
	{
		this.type = type;
		this.change = ReputationChange;
		this.sender = sender;
		this.senderName = sender instanceof Player ? sender.getName() : "server";
		this.ip = sender instanceof Player ? bConnector.getPlayerIPAddress((Player)sender) : "1.1.1.1";
		this.recipient = recipient;
		this.reason = reason;
		this.result = "";
		this.duration = duration;
		this.durationText = durationText;
		this.alreadyBanned = false;
		this.kick = false;
		attempt = 0;
	}

	@Override
	public void process() {
		if (type == GlobalBan)
		{
			if (CheckAlreadyBanned(recipient))
			{
				alreadyBanned = true;
				status = NotAllowed;
				return;
			}
			result = addNote(sender, recipient, "0", "1", "0", reason, "-100", "0", "0", false, false);
		}
		else if (type == LocalBan)
		{
			if (CheckAlreadyBanned(recipient))
			{
				alreadyBanned = true;
				status = NotAllowed;
				return;
			}
			result = addNote(sender, recipient, "0", "0", "0", reason, "-200", "0", "1", false, false);
		}
		else if (type == TempBan)
		{
			if (CheckAlreadyBanned(recipient))
			{
				alreadyBanned = true;
				status = NotAllowed;
				return;
			}
			result = addNote(sender, recipient, "0", "0", "0", reason, "-100", String.valueOf(duration), "0", false, false);
		}
		else if (type == ForceBan)
		{
			result = addNote(sender, recipient, "0", "0", "0", reason, "-200", "0", "1", true, false);
		}
		else if (type == Warn)
		{
			result = addNote(sender, recipient, "0", "0", "0", reason, String.valueOf(-Math.abs(change)), "0", "0", false, false);
			if (CheckAlreadyBanned(recipient))
			{
				kick = true;
			}
		}
		else if (type == TempWarn)
		{
			result = addNote(sender, recipient, "0", "0", "0", reason, String.valueOf(-Math.abs(change)), Double.toString(duration), "0", false, false);
			if (CheckAlreadyBanned(recipient))
			{
				kick = true;
			}
		}
		else if (type == GWarn)
		{
			result = addNote(sender, recipient, "0", "1", "0", reason, String.valueOf(-Math.abs(change)), Double.toString(duration), "0", false, false);
			if (CheckAlreadyBanned(recipient))
			{
				kick = true;
			}
		}
		else if (type == Note)
		{
			result = addNote(sender, recipient, "0", "1", "0", reason, String.valueOf(change), "0", "0", false, false);
		}
		else if (type == Praise)
		{
			result = addNote(sender, recipient, "0", "0", "0", reason, String.valueOf(change), "0", "0", false, false);
		}
		else if (type == Unban)
		{
			String rep = getRep(sender, recipient);
			if (rep.equalsIgnoreCase("not")) {
				alreadyBanned = true;
				status = NotAllowed;
				return;
			}
			result = addNote(sender, recipient, "0", "0", "0", reason, "100", "0", "0", false, true);				
		}
		else if (type == Comment)
		{
			result = addNote(sender, recipient, "0", "1", "1", reason, "0", "0", "0", false, false);
		}
		if (resultJSON == null)
		{
			status = TryAgain;
		}
		else
		{
			status = Success;
		}
	}

	@Override
	public void postProcess() {
		if (status == TryAgain)
		{
			if (attempt < 5)
			{
				glizer.queue.add(this);
				bChat.log("Retrying ban of " + recipient);
				attempt++;
				return;
			}
			else
			{
				glizer.banqueue.add(this);
				bChat.sendMessage(sender, Language.GetTranslated("ban.failed")/*"&eBan couldn't be delivered to glizer.de - will be done later"*/);
			}
		}
		if (result.equalsIgnoreCase("ok")) {
			if (glizer.D)
				bChat.log(Language.GetTranslated("note.add_action_done"));
		} else if (result.equalsIgnoreCase("nochange")) {
			if (glizer.D)
				bChat.log(Language.GetTranslated("note.nochange"));
			return;
		} else if (result.equalsIgnoreCase("never connected")) {
			Player p = glizer.plugin.getServer().getPlayerExact(recipient);
			if (p != null)
			{
				glizer.queue.add(new LoginRequest(p, bConnector.getPlayerIPAddress(p)));
				this.attempt = 0;
				glizer.queue.add(this);
				return;
			}
			if (glizer.plugin.getServer().getOfflinePlayer(recipient).hasPlayedBefore() && (type == GlobalBan || type == LocalBan || type == TempBan))
			{
				this.type = ForceBan;
				this.attempt = 0;
				bChat.sendMessage(sender, Language.GetTranslated("ban.never_connected_forceban", recipient)/*"&6" + recipient + " &eis not listed on glizer.de - Bantype changed to Forceban"*/);
				glizer.queue.add(this);
				return;
			}
			if (glizer.D)
				bChat.log("Note add action cant be done, player never connected to this server");
			bChat.sendMessage(sender, Language.GetTranslated(
					"note.never_connected_server", recipient));
			bChat.sendMessage(sender, Language.GetTranslated("ban.ban_anyways"));
			return;
		} else if (result.equalsIgnoreCase("not yourself")) {
			if (glizer.D)
				bChat.log("Note add action cant be done, not to command user himself");
			bChat.sendMessage(sender,
					Language.GetTranslated("note.not_yourself"));
			return;
		} else if (status == Success) {
			// TODO Language
			bChat.sendMessage(sender, "&eAction can't be done, Reason: " + result);
			return;
		}
		if (type == GlobalBan)
		{
			if (alreadyBanned)
			{
				// TODO Language
				bChat.sendMessage(sender, "&6This Player is already banned from this server.");
				return;
			}
			bBackupManager.addBanBackup(recipient);
			if (bConfigManager.broadcastBan) {
				String sname = ((sender instanceof Player) ? ((Player) sender).getName() : "Console");
				bChat.broadcastMessage(Language.GetTranslated("ban.globalban_1", sname, recipient, reason));
				bChat.broadcastMessage(Language.GetTranslated("ban.globalban_2", sname, recipient, reason));
			}
			Player result = null;
			result = glizer.plugin.getServer().getPlayer(recipient);
			if (result != null) {
				result.kickPlayer(bConfigManager.ban_kickmessage.replace("%1", reason));
			}
		}
		else if (type == LocalBan)
		{
			if (alreadyBanned)
			{
				// TODO Language
				bChat.sendMessage(sender, "&6This Player is already banned from this server.");
				return;
			}
			bBackupManager.addBanBackup(recipient);
			if (bConfigManager.broadcastBan) {
				String sname = ((sender instanceof Player) ? ((Player) sender)
						.getName() : "Console");
				bChat.broadcastMessage(Language.GetTranslated("ban.localban_1", sname, recipient, reason));
				bChat.broadcastMessage(Language.GetTranslated("ban.localban_2", sname, recipient, reason));
			}
			Player result = null;
			result = glizer.plugin.getServer().getPlayer(recipient);
			if (result != null) {
				result.kickPlayer(bConfigManager.ban_kickmessage.replace("%1", reason));
			}
		}
		else if (type == TempBan)
		{
			if (alreadyBanned)
			{
				// TODO Language
				bChat.sendMessage(sender, "&6This Player is already banned from this server.");
				return;
			}
			long unbanTime = (long)(System.currentTimeMillis() / 1000 + duration);
			bBackupManager.addBanBackup("&" + String.valueOf(unbanTime) + "&" + recipient);
			if (bConfigManager.broadcastBan) {
				// TODO Language for tempbans
				bChat.broadcastMessage("&6"
						+ (sender instanceof Player ? ((Player) sender)
								.getName() : "Console")
						+ " temporarily banned player: &e"
						+ recipient);
				bChat.broadcastMessage("&eDuration: &6" + durationText);
				bChat.broadcastMessage("&eReason: &6" + reason);
			}
			Player result = null;
			result = glizer.plugin.getServer().getPlayer(recipient);
			if (result != null) {
				result.kickPlayer(bConfigManager.tempban_kickmessage.replace("%1", reason));
			}
		}
		else if (type == ForceBan)
		{
			if (alreadyBanned)
			{
				// TODO Language
				bChat.sendMessage(sender, "&6This Player is already banned from this server.");
				return;
			}
			bBackupManager.addBanBackup(recipient);
			if (bConfigManager.broadcastBan) {
				String sname = ((sender instanceof Player) ? ((Player) sender).getName() : "Console");
				bChat.broadcastMessage(Language.GetTranslated("ban.localban_1", sname, recipient, reason));
				bChat.broadcastMessage(Language.GetTranslated("ban.localban_2", sname, recipient, reason));
			}
			Player result = null;
			result = glizer.plugin.getServer().getPlayer(recipient);
			if (result != null) {
				result.kickPlayer(bConfigManager.ban_kickmessage.replace("%1", reason));
			}
		}
		else if (type == Warn)
		{
			// TODO Language
			bChat.sendMessage(sender, "&6Warning added");
			if (bConfigManager.broadcastWarning)
			{
				// TODO put into language file
				bChat.broadcastMessage("&6" + (sender instanceof Player?((Player)sender).getName():"Console") + " &e warned Player &6" + recipient + "&e ("+String.valueOf(-Math.abs(change))+")");
				bChat.broadcastMessage("&eReason: &6" + reason);				
			}
			silentPostProcess();
		}
		else if (type == TempWarn)
		{
			// TODO Language
			bChat.sendMessage(sender, "&6Warning added");
			if (bConfigManager.broadcastWarning)
			{
				// TODO put into language file
				bChat.broadcastMessage("&6" + (sender instanceof Player?((Player)sender).getName():"Console") + " &e temporarily warned Player &6" + recipient + "&e ("+String.valueOf(-Math.abs(change))+")");
				bChat.broadcastMessage("&eDuration: &6" + durationText);
				bChat.broadcastMessage("&eReason: &6" + reason);
			}
			silentPostProcess();
		}
		else if (type == GWarn)
		{
			// TODO Language
			bChat.sendMessage(sender, "&6Warning added");
			if (bConfigManager.broadcastWarning)
			{
				// TODO put into language file
				bChat.broadcastMessage("&6" + (sender instanceof Player?((Player)sender).getName():"Console") + " &e globally warned Player &6" + recipient + "&e ("+String.valueOf(-Math.abs(change))+")");
				bChat.broadcastMessage("&eReason: &6" + reason);				
			}
			silentPostProcess();
		}
		else if (type == Note)
		{
			// TODO Language
            bChat.sendMessage(sender, "&6Note added");
		}
		else if (type == Praise)
		{
			// TODO Language
			bChat.sendMessage(sender, "&6Note added");
		}
		else if (type == Unban)
		{
			if (alreadyBanned)
			{
				// TODO Language
				bChat.sendMessage(sender, "&6This Player isn't banned from this server.");
				return;
			}
			bBackupManager.removeBanBackup(recipient);
			if (bConfigManager.broadcastBan)
				bChat.broadcastMessage(Language.GetTranslated("ban.unban_1", (sender instanceof Player ? ((Player) sender).getName() : "Console"), recipient));
		}
		else if (type == Comment)
		{
			bChat.sendMessage(sender, "&6Comment added");
		}
	}

	public void silentPostProcess() {
		if (type == TempWarn)
		{
			if (kick)
			{
				glizer.finished.add(new Kick(bConfigManager.tempban_kickmessage.replace("%1", reason), glizer.plugin.getServer().getPlayerExact(recipient)));
			}
		}
		else if (type == Warn)
		{
			if (kick)
			{
				glizer.finished.add(new Kick(bConfigManager.ban_kickmessage.replace("%1", reason), glizer.plugin.getServer().getPlayerExact(recipient)));
			}
		}
	}
	
	public String addNote(CommandSender sender, String recipient,
			String fhide, String fglobal, String fprivate, String message,
			String reputation, String timelimit, String ban, boolean Force, boolean Unban) {

		String ip = sender instanceof Player ? bConnector
				.getPlayerIPAddress((Player) sender) : "1.1.1.1";

		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "notes");
		url_items.put("do", "add");
		url_items.put("account",
				(sender instanceof Player ? ((Player) sender).getName()
						: "server"));
		url_items.put("ip", ip);
		url_items.put("username", recipient);
		url_items.put("fhide", fhide);
		url_items.put("fglobal", fglobal);
		url_items.put("fprivate", fprivate);
		url_items.put("message", message);
		url_items.put("reputation", reputation);
		url_items.put("timelimit", timelimit);
		url_items.put("ban", ban);
		if (Force)
			url_items.put("force", "1");
		if (Unban)
			url_items.put("unban", "1");

		resultJSON = bConnector.hdl_com(url_items);
		return bConnector.checkResult(resultJSON);
	}	

	public static String getRep(CommandSender sender, String recipient) {

		String ip = sender instanceof Player ? bConnector
				.getPlayerIPAddress((Player) sender) : "1.1.1.1";

		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "notes");
		url_items.put("do", "rep");
		url_items.put("account",
				(sender instanceof Player ? ((Player) sender).getName()
						: "server"));
		url_items.put("ip", ip);
		url_items.put("username", recipient);

		JSONObject result = bConnector.hdl_com(url_items);
		boolean ok = true;
		try {
			ok = result.getBoolean("banned");
			int res;
			if (!ok) {
				return "not";
			} else {
				res = result.getInt("local");
				if (result.getInt("localglobal") <= -10) {
					return "global";
				}
				if (res == -100) {
					return "local";
				}
				return "global";
			}
		} catch (Exception e) {
			if (glizer.D)
				e.printStackTrace();
			return "not";
		}
	}

	private static boolean CheckAlreadyBanned(String Name) {
		HashMap<String, String> url_items = new HashMap<String, String>();
		url_items.put("exec", "notes");
		url_items.put("do", "rep");
		url_items.put("account", "server");
		url_items.put("ip", "1.1.1.1");
		url_items.put("username", Name);

		JSONObject result = bConnector.hdl_com(url_items);
		try {
			boolean banned = result.getBoolean("banned");
			if (banned)
				return true;
			else
				return false;

		} catch (Exception e) {
			if (glizer.D)
				e.printStackTrace();
			return false;
		}

	}

}
