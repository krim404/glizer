package com.beecub.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

import com.beecub.glizer.glizer;

public class bConnector {

	public static JSONObject hdl_com(HashMap<String, String> items) 
	{
		if (items == null) {
			if(glizer.D) bChat.log("Argument cant be null!", 2);
			return null;
		}
		String url_req = urlParse(items);
		if(glizer.D) glizer.log.info("url_req " + url_req);
		String json_text = APIRequest(url_req);
		JSONObject output = getData(json_text);

		return output;
	}

	@SuppressWarnings("rawtypes")
	public static String urlParse(HashMap<String, String> items) {
		if (items == null) {
			if(glizer.D) bChat.log("Argument cant be null!", 2);
			return "";
		}
		String data = "";
		try {
			for (Map.Entry entry : items.entrySet()) {
				String key = (String)entry.getKey();
				String val = (String)entry.getValue();
				//if(glizer.D) glizer.log.info(" ++++ KEY ++++ " + key);
				//if(glizer.D) glizer.log.info(" ++++ VAL ++++ " + val);
				if (data.equals(""))
					data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(val, "UTF-8");
				else
					data = data + "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(val, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			if(glizer.D) bChat.log("Error parsing", 2);
		}
		return data;
	}


	public static String APIRequest(String data) {
		try {
			URL url = new URL("http://api.glizer.de/" + bConfigManager.key + "/");

			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(3000); // 8000
			conn.setReadTimeout(6000); // 15000
			conn.setDoOutput(true);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			StringBuilder buf = new StringBuilder();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
			{
				buf.append(line);
			}

			String result = buf.toString();

			// DEBUG
			if(glizer.D) bChat.log(result);

			wr.close();
			rd.close();
			return result;
		} catch (Exception e) {
			if (glizer.D)
			{	
				e.printStackTrace();
				bChat.log("Error on request from API", 2);
			}
		}
		return "";
	}

	public static JSONObject getData(String json_text) {
		try {
			JSONObject json = new JSONObject(json_text);
			return json;
		} catch (JSONException e) {
			if(glizer.D)
			{
				bChat.log("Receiving data failed", 2);
				bChat.log(e.getMessage());
			}
		}
		return null;
	}

	public static String getPlayerIPAddress(Player player) {
		InetAddress addr = player.getAddress().getAddress();
		return getIPAddress(addr);
	}
	
	public static String getIPAddress(InetAddress addr)
	{
		if(bConfigManager.noip == true)
			return "127.0.0.1";
		
		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i=0; i<ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i]&0xFF;
		}
		return ipAddrStr;
	}

	public static String checkResult(JSONObject result) {
		String ret = "";               
		if(result != null) {
			try {
				if(result.getInt("error") == 1) {
					int errno = result.getInt("errno");
					switch(errno) {
					case 1000:
						ret = "wrong ip";
						break;
					case 1001:
						ret = "wrong ip";
						break;
					case 1002:
						ret = "no record";
						break;
					case 1003:
						ret = "sql error";
						break;
					case 1004:
						ret = "missing variable";
						break;
					case 1005:
						ret = "to fast";
						break;
					case 1006:
						ret = "never connected";
						break;
					case 1007:
						ret = "exec not found";
						break;
					case 1008:
						ret = "wrong data type";
						break;
					case 1009:
						ret = "server as user";
						break;
					case 1010:
						ret = "not allowed";
						break;
					case 1011:
						ret = "not from here";
						break;
					case 1012:
						ret = "not yourself";
						break;
					default:
						ret = "unknown error";
						break;
					}
				}
			} catch(JSONException e) {
				if(glizer.D) e.printStackTrace();
				try {
					ret = result.getString("response");
				} catch(JSONException e1) {
					if(glizer.D) e1.printStackTrace();
					return ret = "unkown";
				}
			}
		}
		else {
			ret = "Error - server connection failed, aborting actions";
		}
		if(glizer.D) bChat.log(ret);
		return ret;
	}
}
