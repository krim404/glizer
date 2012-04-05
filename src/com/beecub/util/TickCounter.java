package com.beecub.util;

import com.beecub.glizer.glizer;

public class TickCounter implements Runnable {
	static long lastTime = 0;
	static double ticks = -1;

	public TickCounter(glizer glizer) {
		lastTime = System.currentTimeMillis();
		bChat.log("Initialized tick counter");
	}

	@Override
	public void run() {
		ticks = 200.0 / ((System.currentTimeMillis() - lastTime) / 1000.0);
		if (glizer.D) bChat.log(ticks + " ticks");
		lastTime = System.currentTimeMillis();
	}

}
