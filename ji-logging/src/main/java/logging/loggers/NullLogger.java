package logging.loggers;

import common.Logger;

public class NullLogger implements Logger {

	@Override
	public void debug(final Object message) {
		// not implemented		
	}

	@Override
	public void debug(final Object message, final Throwable t) {
		// not implemented		
	}

	@Override
	public void info(final Object message) {
		// not implemented		
	}

	@Override
	public void info(final Object message, final Throwable t) {
		// not implemented	
	}

	@Override
	public void warn(final Object message) {
		// not implemented		
	}

	@Override
	public void warn(final Object message, final Throwable t) {
		// not implemented	
	}

	@Override
	public void error(final Object message) {
		// not implemented		
	}

	@Override
	public void error(final Object message, final Throwable t) {
		// not implemented	
	}

	@Override
	public void fatal(final Object message) {
		// not implemented		
	}

	@Override
	public void fatal(final Object message, final Throwable t) {
		// not implemented
	}

}
