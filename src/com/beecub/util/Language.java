package com.beecub.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.beecub.glizer.glizer;

public class Language {
	static String language;

	public static void Load() {
		language = bConfigManager.language;
		m_Language = new HashMap<String, HashMap<String, String>>();
		reloadFiles();
	}

	public static boolean reloadFiles() {
		m_Language.clear();
		File pDir = new File(glizer.plugin.getDataFolder() + "/lang/");
		if (!pDir.exists())
			pDir.mkdir();

		File[] Files = pDir.listFiles();

		if (Files == null) {
			bChat.log("Language-File-Directory pointer isn't pointing to a directory.");
			return false;
		}

		boolean en = false;
		for (int i = 0; i < Files.length; i++) {
			File pFile = Files[i];
			if (pFile.getName().equalsIgnoreCase("en.txt")) {
				en = true;
			}

			HashMap<String, String> thislangmap = new HashMap<String, String>();
			try {
				BufferedReader pReader = new BufferedReader(new FileReader(
						pFile));

				String line = pReader.readLine();
				while (line != null) {
					String[] tokens = line.split("#");
					if (tokens.length <= 1) {
						bChat.log("Found invalid language token: " + line
								+ " in file " + pFile.getName());
					} else {
						String tok = tokens[1];
						if (tokens.length > 2) {
							for (int y = 2; y < tokens.length; y++) {
								tok += tokens[y];
							}
						}

						thislangmap.put(tokens[0], tok);
					}
					line = pReader.readLine();
				}
				pReader.close();
			} catch (IOException e) {
				bChat.log("Reading from file '" + pFile.getName()
						+ "' failed. " + e.getCause());

			}
			String FN = pFile.getName();
			FN = FN.replace(".txt", "");
			m_Language.put(FN, thislangmap);
			bChat.log("Language " + FN + " loaded.");
		}
		
		HashMap<String, String> m_ENMap = new HashMap<String, String>();
		m_ENMap.put("onlinetime.tablecaption", "&eServer ------------------- Onlinetime ----------");
		m_ENMap.put("other.noglizerregistered", "&6There is no glizer-registered Player with the name &e");
		m_ENMap.put("event.not_with_that_id", "&6There is no event with the ID &e$1");
		m_ENMap.put("event.id_must_number", "&6The ID has to be a number");
		m_ENMap.put("event.sign_up",  "&6Successfully signed up.");
		m_ENMap.put("event.sign_up_to_following",  "&6You're signed up to the following events:");
		m_ENMap.put("event.myevents", "&e$1 &6start: &e$2 &6; ID: &e$3");
		m_ENMap.put("event.not_registered_for_any", "&6At the moment you aren't registered for any events.");
		m_ENMap.put("event.suc_unregistered", "&6Successfully unregistered.");
		m_ENMap.put("event.noevents", "At the moment there are no events.");
		m_ENMap.put("event.failed_to_retrieve", "&6Failed to retrieve data. Try again later!");
		m_ENMap.put("event.starts_with", "&6Starts (Day/Month): &e$1");
		m_ENMap.put("event.ends_with", "&6Ends (Day/Month): &e$1");
		m_ENMap.put("event.takes_place_on", "&6Takes place on: &e$1");
		m_ENMap.put("event.list_title", "&e$1 &6will take place on &e$2");
		m_ENMap.put("event.list_start", "                  &6It'll start: &e$1");
		m_ENMap.put("event.list_id", "                  &6It's ID is: &e$1");
		m_ENMap.put("other.not_available_now", "&6Not available in this version of glizer");
		m_ENMap.put("other.not_available", "&6Not available at the moment");
		m_ENMap.put("other.empty_result", "&6Nothing found");
		m_ENMap.put("other.wrong_command_usage", "&6Wrong command Usage!");
		m_ENMap.put("other.no_permissions", "&6You haven't got sufficient Permissions to do that.");
		m_ENMap.put("other.reload_glizer", "&6If you want to reload the glizer configuration please type: &e /glizer reload");
		m_ENMap.put("other.see_help_menu", "&6If you want to see the help menu please type: &e /glizer help");
		m_ENMap.put("other.not_a_glizer_command","&6Not a glizer command");
		m_ENMap.put("other.feature_disabled", "&6Feature disabled");
		m_ENMap.put("other.command_from_console", "&6Commands from console: Coming soon");
		m_ENMap.put("ban.ban", "&6Use /globalban, /tempban and /localban to ban players!");
		m_ENMap.put("ban.already_banned", "&6Player &e$1&6 is already banned from your server.");
		m_ENMap.put("ban.globalban_1", "&e$1 globally banned player: &6$2");
		m_ENMap.put("ban.globalban_2", "&eReason: &6$3");
		m_ENMap.put("ban.localban_1", "&e$1 locally banned player: &6$2");
		m_ENMap.put("ban.localban_2", "&eReason: &6$3");
		m_ENMap.put("ban.unban_1", "&6$1 &eunbanned player: &e$2");
		m_ENMap.put("note.add_action_done", "Note add action done");
		m_ENMap.put("note.nochange", "Note add action done, nochange reputation");
		m_ENMap.put("note.never_connected_server", "&6Error, Player &e$1 &6 was never connected to this server.");
		m_ENMap.put("ban.ban_anyways", "&6If you want to ban him anyways use /forceban.");
		m_ENMap.put("note.not_yourself", "&6Error, you cant do this to yourself");
		m_ENMap.put("ban.kick_not_online", "&6The player &e$1 &6isn't online.");
		m_ENMap.put("ban.kick_bc_1", "&6Player &e$1&6 was kicked $2: &e$3");
		m_ENMap.put("ban.kick_pm_1", "Kicked $1: $2");
		m_ENMap.put("ban.failed", "&eBan couldn't be delivered to glizer.de - will be done later");
		m_ENMap.put("ban.never_connected_forceban", "&6$1 &eis not listed on glizer.de - Bantype changed to Forceban");
		m_ENMap.put("mute.muted_permanent", "&6You are muted by &e$1&6: &e$2");
		m_ENMap.put("mute.muted_temporary", "&6You are muted by &e$1&6 for &e$2&6 minutes: &e$3");
		m_ENMap.put("mute.playermuted_permanent", "&6You muted &e$1&6: &e$2");
		m_ENMap.put("mute.playermuted_temporary", "&6You muted &e$1&6 for &e$2&6 minutes: &e$3");
		m_ENMap.put("mute.gotmuted_permanent", "&6You got muted by &e$1&6: &e$2");
		m_ENMap.put("mute.gotmuted_temporary", "&6You got muted by &e$1&6 for &e$2&6 minutes: &e$3");
		m_ENMap.put("mute.player_not_online", "&6The player &e$1&6 isn't online!");
		m_ENMap.put("mute.player_not_muted", "&6The player &e$1&6 isn't muted!");
		m_ENMap.put("mute.time_negative", "&6You can't enter a negative time!");
		m_ENMap.put("mute.gotunmuted", "&6You got unmuted!");
		m_ENMap.put("mute.player_unmuted", "&6You unmuted &e$1&6");
		m_Language.put("__en", m_ENMap);

		if (en == false) {
			bChat.log("No default language file exists. Attempting to create one.");
			File pFile = new File(glizer.plugin.getDataFolder()
					+ "/lang/en.txt");

			if (!pFile.exists())
				try {
					pFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

			try {
				BufferedWriter pWriter = new BufferedWriter(new FileWriter(
						pFile));

				Set<Entry<String, String>> entryset = m_ENMap.entrySet();
				Iterator<Entry<String, String>> It = entryset.iterator();

				while (It.hasNext()) {
					Entry<String, String> n = It.next();
					pWriter.write(n.getKey() + "#" + n.getValue()+"\n");
				}

				pWriter.flush();
				pWriter.close();

			} catch (IOException e) {
				bChat.log("Exception while writing to " + pFile.getName()
						+ ": " + e.getCause().getMessage());
			}

			reloadFiles();
		}
		return true;
	}

	private static String GetRaw(String tag, String lang) {
		try {
			HashMap<String, String> pTags = m_Language.get(lang);
			if (pTags == null) {
				bChat.log("Couldn't retrieve language tokens. (lang == " + lang
						+ ";tag==" + tag + ")");
				pTags = m_Language.get("__en");
				if (pTags == null) {
					throw new IllegalArgumentException(
							"Defaualt Language not found(tag==" + tag
									+ ";lang==" + lang + ")");
				}
			}

			String value = pTags.get(tag);
			if (value == null) {
				bChat.log("Couldn't retrieve language token. (lang == " + lang
						+ ";tag==" + tag + ")");
				pTags = m_Language.get("__en");
				if (pTags == null) {
					throw new IllegalArgumentException(
							"Defaualt Language not found(tag==" + tag
									+ ";lang==" + lang + ")");
				}
				value = pTags.get(tag);
				if (value == null) {
					throw new IllegalArgumentException(
							"Defaualt Language's tag not found(tag==" + tag
									+ ";lang==" + lang + ")");
				}
				
				return value;
			}
			return value;
		} catch (Exception e) {
			bChat.log(e.getMessage());
			return "";
		}
	
	}

	public static String GetTranslated(String tag, String[] args) {
		String text = "";
		try {
			text = GetRaw(tag, language);
		} catch (Exception e) {
			text = "";
		}
		if (args == null)
			return text;
		
		// Replace tokens
		for (int i = 1; i < args.length+1; i++) {
			text = text.replace("$" + i, args[i-1]);
		}
		text = text.replace("$$", "$");

		return text;

	}

	public static String GetTranslated(String tag)
	{
		return GetTranslated(tag, new String[]{});
	}
	public static String GetTranslated(String tag, String a)
	{
		return GetTranslated(tag, new String[]{a});
	}
	public static String GetTranslated(String tag, String a, String b)
	{
		return GetTranslated(tag, new String[]{a,b});
	}

	public static String GetTranslated(String tag, String a, String b, String c)
	{
		return GetTranslated(tag, new String[]{a,b,c});
	}

	public static String GetTranslated(String tag, String a, String b, String c, String d)
	{
		return GetTranslated(tag, new String[]{a,b,c,d});
	}

	public static String GetTranslated(String tag, String a, String b, String c, String d, String e)
	{
		return GetTranslated(tag, new String[]{a,b,c,d,e});
	}


	static private HashMap<String, HashMap<String, String>> m_Language;
}
