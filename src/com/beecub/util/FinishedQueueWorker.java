package com.beecub.util;

import com.beecub.glizer.glizer;

import de.upsj.glizer.APIRequest.APIRequest;


public class FinishedQueueWorker implements Runnable {
	glizer plugin;
	
	
	public FinishedQueueWorker(glizer _plugin) {
		plugin = _plugin;
	}

	@Override
	public void run() {
		APIRequest current;
		if (glizer.D && glizer.finished.size() > 0)
			bChat.log("FinishedQueue size: " + glizer.finished.size());
		while ((current = glizer.finished.poll()) != null)
		{
			try
			{
				current.postProcess();
			}
			catch(Exception e)
			{
				if (glizer.D)
					e.printStackTrace();
			}
		}
		if (glizer.D && glizer.finished.size() > 0)
			bChat.log("FinishedQueue size: " + glizer.finished.size());
	}
}
