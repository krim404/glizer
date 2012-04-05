package com.beecub.execute;

import com.beecub.glizer.glizer;

import de.upsj.glizer.APIRequest.BanBackupRequest;

public class Backup {
    
    public static void getPlayers() {
    	glizer.queue.add(new BanBackupRequest());
    }
}
