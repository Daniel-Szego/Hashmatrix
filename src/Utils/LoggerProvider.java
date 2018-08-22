package Utils;

import java.util.ArrayList;

// provider for integrating different logger functionalities
public class LoggerProvider implements LoggerInterface {

	public ArrayList<LoggerInterface> loggers;
	
	// logging exception
	public void log(Exception ex) {
		loggers = new ArrayList<LoggerInterface>();
	}

	public void addLogger(LoggerInterface logger){
		loggers.add(logger);
	}
	
	// logging and exception 
	public void log(Exception ex, Severity sev) {
		for(LoggerInterface logger: loggers) {
			logger.log(ex,sev);
		}
	}

	// logging and exception 
	public void log(Exception ex, Severity sev, Object source) {
		for(LoggerInterface logger: loggers) {
			logger.log(ex,sev, source);
		}		
	}
	
	// logging an information message
	public void log(String message) {
		for(LoggerInterface logger: loggers) {
			logger.log(message);
		}
	}	
	
	// logging an information message
	public void log(String message, Severity sev) {
		for(LoggerInterface logger: loggers) {
			logger.log(message, sev);
		}
	}

	// logging an information message
	public void log(String message, Severity sev, Object source) {
		for(LoggerInterface logger: loggers) {
			logger.log(message, sev, source);
		}
	}
	
	// logging a full object as Json
	public void logObject(Object obj) {
		for(LoggerInterface logger: loggers) {
			logger.logObject(obj);
		}			
	}	
}
