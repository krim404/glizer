package com.beecub.util;

import com.beecub.glizer.glizer;

import de.upsj.glizer.APIRequest.APIRequest;

public class APIRequestThread extends Thread {
	
	@Override
	public void run() {
		glizer.log.info("glizer API request thread started");
		while (!this.isInterrupted())
		{
			APIRequest current = glizer.queue.poll();
			if (current == null)
			{
				try { Thread.sleep(100); } catch (InterruptedException e) { break; }
			}
			else
			{
				try
				{
					current.process();
				}
				catch (Exception e)
				{
					if (glizer.D)
						e.printStackTrace();
				}
				
				glizer.finished.add(current);
				try { Thread.sleep(50);	} catch (InterruptedException e1) {	}
			}
		}
	}
}
