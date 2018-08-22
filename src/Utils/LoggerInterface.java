package Utils;

// interface for the logger
public interface LoggerInterface {

	// logging exception
	public void log(Exception ex);

	// logging and exception 
	public void log(Exception ex, Severity sev);

	// logging and exception 
	public void log(Exception ex, Severity sev, Object source);
	
	// logging an information message
	public void log(String message);
	
	// logging an information message
	public void log(String message, Severity sev);

	// logging an information message
	public void log(String message, Severity sev, Object source);
	
	// logging a full object as Json
	public void logObject(Object obj);

}

