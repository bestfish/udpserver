package com.fish.play.server.schedule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VarietyStats {
	private static final Log log = LogFactory.getLog(VarietyStats.class);
	private static final ConcurrentMap<String, AtomicLong> stats;
	static {
		stats = new ConcurrentHashMap<String, AtomicLong>();
		ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();
		schedule.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (!stats.isEmpty()) {
					log.info("real-time accumulated COUNT: " + stats);
				}
			}
		}, 0, 30000, TimeUnit.MILLISECONDS);
	}

	public static void incr(String key) {
		if (stats.containsKey(key)) {
			stats.get(key).incrementAndGet();
			return;
		}

		AtomicLong atomicLong = new AtomicLong();
		AtomicLong previous = stats.putIfAbsent(key, atomicLong);
		if (previous != null) {
			atomicLong = previous;
		}
		atomicLong.incrementAndGet();
	}

}
