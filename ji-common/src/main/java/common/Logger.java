package common;

public interface Logger {

	public void trace(final Object message);
	
	public void trace(final Object message, final Throwable t);

	public void debug(final Object message);
	
	public void debug(final Object message, final Throwable t);
	
	public void info(final Object message);
	
	public void info(final Object message, final Throwable t);
	
	public void warn(final Object message);
	
	public void warn(final Object message, final Throwable t);
	
	public void error(final Object message);
	
	public void error(final Object message, final Throwable t);
	
	public void fatal(final Object message);
	
	public void fatal(final Object message, final Throwable t);
}
