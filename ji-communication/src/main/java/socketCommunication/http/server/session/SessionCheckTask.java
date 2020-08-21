package socketCommunication.http.server.session;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import common.Logger;

public class SessionCheckTask {

	private static ScheduledExecutorService  threadPool = Executors.newSingleThreadScheduledExecutor();
	private final SessionStorage storage;
	private final Logger logger;
	
	public SessionCheckTask(SessionStorage storage, Logger logger) {
		this.storage = storage;
		this.logger = logger;
	}
	
	public void startChecking() {
		threadPool.scheduleWithFixedDelay(checkSessins(), 0, 1, TimeUnit.DAYS);
	}
	
	private Runnable checkSessins() {
		return ()->{
			storage.forEach((session)->{
				if (new Date().getTime() > session.getExpirationTime()) {
					storage.removeSession(session.getSessionId());
				}
			});
		};
	}

	public void stopChecking() {
		threadPool.shutdownNow();
		try {
			threadPool.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("Problem with pool closing", e);
			threadPool.shutdownNow();
		}
	}
	
}
