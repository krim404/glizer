package com.beecub.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.beecub.glizer.glizer;

public class bBackupManager {
    
    protected static glizer plugin;
    protected static YamlConfiguration backupban;
    protected static File backupFile;
    public static List<String> bannedPlayers = new ArrayList<String>();
        
    public bBackupManager(glizer glizer) {
        plugin = glizer;
        setupBackupBan();
    }
    
    public static void reload() {
        setupBackupBan();
    }
    
    private static void setupBackupBan() {
        backupFile = new File(plugin.getDataFolder() + "/banbackup/", "backupban.yml");
        backupban = new YamlConfiguration();
        if (backupFile.exists())
        {
        	try {
        		backupban.load(backupFile);
        	} catch (Exception e) {
        		e.printStackTrace();
        		bChat.log("Failed to load backupban.yml!", 3);
        	}
        }
        if (backupban.isList("ban"))
        	bannedPlayers = backupban.getStringList("ban");
        else
        	bannedPlayers = new LinkedList<String>();
    }
    
    public static boolean removeBanBackup(String name) {
        if(banListContains(name)) {
        	String lowercaseName = name.toLowerCase();
        	String entry = null;
        	String[] parts;
        	for (String s : bannedPlayers)
        	{
        		// Syntax: &unbanTimestamp&playerName
        		if (s.startsWith("&"))
        		{
        			parts = s.substring(1).split("&");
        			
        			if (parts.length != 2)
        			{
        				bChat.log("Glizer banbackup corrupt - Line: " + s, 3);
        			}
        			
        			if (parts[1].equals(lowercaseName))
        			{
        				entry = s;
        				break;
        			}
        		}
        		else
        		{
        			if (s.equals(lowercaseName))
        			{
        				entry = s;
        				break;
        			}
        		}
        	}
        	if (entry != null)
        	{
        		bannedPlayers.remove(entry);
            	
        		backupban.set("ban", bannedPlayers);
                save();
        	}
        }
        return false;
    }
    
    private static void save() {
    	if (!backupFile.exists())
    	{
        	try {
    			backupFile.createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
        		bChat.log("Failed to create empty backupban.yml!", 3);
    		}
    	}
    	try {
			backupban.save(backupFile);
		} catch (IOException e) {
			e.printStackTrace();
    		bChat.log("Failed to write to backupban.yml!", 3);
		}
	}

	public static boolean addBanBackup(String name) {
        if(!banListContains(name)) {
            bannedPlayers.add(name.toLowerCase());
            backupban.set("ban", bannedPlayers);
            save();
            return true;
        }
        return false;
    }
    
    public static boolean checkBanList(String name) {
        return banListContains(name);
    }
    
    public static boolean clearBanList() {
        bannedPlayers.clear();
        backupban.set("ban", bannedPlayers);
        save();
        return true;
    }
    
    private static boolean banListContains(String player)
    {
    	String p = player.toLowerCase();
    	String[] parts;
    	
    	long time = System.currentTimeMillis() / 1000;
    	long unbanTime;
    	
    	for(String s : bannedPlayers)
    	{
    		if (s.startsWith("&"))
    		{
    			// tempban
    			parts = s.substring(1).split("&");
    			if (parts.length != 2)
    			{
    				bChat.log.severe("Glizer banbackup corrupt - Line: " + s);
    			}
    			
    			unbanTime = Long.valueOf(parts[0]);
    			if (parts[1].equals(p))
    			{
        			if (unbanTime > time)
    				{
    	    			return true;
    				}
        			else
        			{
        				return false;
        			}
    			}
    		}
    		else
    		{
    			if (s.equals(p))
    			{
    				return true;
    			}
    		}
    	}
    	return false;
    }
}