package com.beecub.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.beecub.glizer.glizer;

public class bWhitelist {
    
    protected static glizer plugin;
    public static List<String> whitelistPlayers = new LinkedList<String>();
    
    public bWhitelist(glizer glizer) {
        plugin = glizer;
        setupWhitelist();
        //load();
    }
    
    private static void load() {
        setupWhitelist();        
    }
    
    public static void reload() {
        load();
    }
    

    /* WHITELIST */
    
    private static void setupWhitelist() {
        try {
        	File pDir = new File(plugin.getDataFolder() + "/whitelistbackup/");
        	if (!pDir.exists())
        		pDir.mkdir();
        	File pBU = new File(plugin.getDataFolder() + "/whitelistbackup/" + "white-list.txt");
        	if (!pBU.exists())
        		pBU.createNewFile();
        	FileInputStream fstream  = new FileInputStream(pBU);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)      {
                whitelistPlayers.add(strLine);
            }
            in.close();
        } catch(Exception e) {
            if(glizer.D) e.printStackTrace();
        }
    }
    
    private static void writeWhitelist() {
        try {
            FileWriter fstream = new FileWriter(plugin.getDataFolder() + "/whitelistbackup/" + "white-list.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            for(String strLine : whitelistPlayers) {
//                bChat.log(strLine);
                out.write(strLine);
                out.newLine();
            }
            out.close();
        } catch(Exception e) {
            if(glizer.D) e.printStackTrace();
        }
    }
    
    public static boolean removeWhiteList(String name) {
        if(whitelistPlayers.contains(name.toLowerCase())) {
            whitelistPlayers.remove(whitelistPlayers.indexOf(name.toLowerCase()));
            writeWhitelist();
            return true;
        }
        return false;
    }
    
    public static boolean addWhiteList(String name)
    {
    	return addWhiteList(name, true);
    }
    public static boolean addWhiteList(String name, boolean log) {
        if(!whitelistPlayers.contains(name.toLowerCase())) {
            whitelistPlayers.add(name.toLowerCase());
            writeWhitelist();
            if (log)
            	bChat.log(whitelistPlayers.toString());
            return true;
        }
        return false;
    }
    
    public static boolean checkWhiteList(String name) {
        if(whitelistPlayers.contains(name.toLowerCase())) {
            return true;
        }
        return false;
    }

	public static void clearWhitelist() {
		whitelistPlayers.clear();
		writeWhitelist();
		
	}
}
