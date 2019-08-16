package utils.serverclient;

import common.Logger;

public class LoggerImpl implements Logger {

    @Override
    public void info(Object message) {
        System.out.println("INFO: " + message);
    }

    @Override
    public void fatal(Object message, Throwable t) {
        System.out.println("FATAL: " + message + ", " + t.getMessage());
    }

    @Override
    public void warn(Object message) {
        System.out.println("WARN: " + message);
    }
    
    public void log(String message) {
        System.out.println("---> " + message);
    }

    @Override
    public void debug(Object message) {
        System.out.println("DEBUG: " + message);
    }

	@Override
	public void debug(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fatal(Object message) {
		// TODO Auto-generated method stub
		
	}

}
