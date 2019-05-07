package database.support;

import org.flywaydb.core.api.logging.Log;

import common.Logger;

public class FlywayLogger implements Log {
	
	private final Logger log;

	public FlywayLogger(final Logger log) {
		this.log = log;
	}
	
	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public void debug(String message) {
		log.debug(message);
	}

	@Override
	public void info(String message) {
		log.info(message);
	}

	@Override
	public void warn(String message) {
		log.warn(message);
	}

	@Override
	public void error(String message) {
		log.error(message);
	}

	@Override
	public void error(String message, Exception e) {
		log.error(message, e);
	}

}
