package de.upsj.glizer.APIRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.beecub.glizer.glizer;
import com.beecub.util.bChat;

public class BanQueue {
	private List<NoteAddRequest> notes;
	private File save;
	
	public BanQueue()
	{
		notes = new LinkedList<NoteAddRequest>();
		save = new File(glizer.plugin.getDataFolder(), "banqueue.json");
		load();
	}

	public void add(NoteAddRequest noteAddRequest) {
		notes.add(noteAddRequest);
		save();
	}
	
	public void flushQueue()
	{
		LinkedList<NoteAddRequest> newqueue = new LinkedList<NoteAddRequest>();
		for (NoteAddRequest r : notes)
		{
			r.process();
			r.silentPostProcess();
			
			if (r.status == NoteAddRequest.TryAgain)
			{
				newqueue.add(r);
				bChat.log("Saved ban of " + r.recipient + " failed: " + r.result);
			}
		}
		notes.clear();
		notes = newqueue;
		save();
	}

	private void load() {
		if (!save.exists())
			return;
		try {
			BufferedReader br = new BufferedReader(new FileReader(save));
			String content = "";
			String line;
			while((line = br.readLine()) != null)
			{
				content += line + "\n";
			}
			br.close();
			JSONArray bans = new JSONArray(content);
			JSONObject ban;
			for (int i = 0; i < bans.length(); i++)
			{
				ban = (JSONObject) bans.get(i);
				notes.add(
						new NoteAddRequest(
						ban.getString("by"),
						ban.getInt("type"),
						ban.getString("recipient"),
						ban.getString("reason"),
						ban.getInt("change"),
						ban.getInt("duration"),
						ban.getString("ip")
						)
				);
			}
		} catch (Exception e) {
			bChat.log("Failed to load banqueue!", 3);
			e.printStackTrace();
		}
		
	}

	private void save() {
		bChat.log("Saving banqueue");
		try {
			if (!save.exists())
				save.createNewFile();
			JSONArray bans = new JSONArray();
			for (NoteAddRequest r : notes)
			{
				JSONObject ban = new JSONObject();
				ban.put("by", r.senderName);
				ban.put("type", r.type);
				ban.put("recipient", r.recipient);
				ban.put("reason", r.reason);
				ban.put("change", r.change);
				ban.put("duration", r.duration);
				ban.put("ip", r.ip);
				bans.put(ban);
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(save, false));
			bw.write(bans.toString());
			bw.close();
		} catch (Exception e) {
			bChat.log("Failed to write banqueue!", 3);
			e.printStackTrace();
		}
	}
	
}
