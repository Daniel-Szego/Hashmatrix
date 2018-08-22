package Utils;
import java.io.StringWriter;

import com.google.gson.Gson;

import ServiceBus.*;

// simplified logger implementation
// log to command line
public class LoggerConsole implements ServiceListenerInterface, LoggerInterface{
	
	public LogLevel log = LogLevel.ALL;

	public void log(Exception ex){
		LoggerConsole.Log(ex,Severity.ERROR);
	}
	// logging and exception 
	public void log(Exception ex, Severity sev){
		LoggerConsole.Log(ex,sev);		
	}

	public void log(Exception ex, Severity sev, Object source) {
		LoggerConsole.Log(ex, sev, source);
	}

	// logging an information message
	public void log(String message){
		LoggerConsole.Log(message);

	}
	
	// logging an information message
	public void log(String message, Severity sev){
		LoggerConsole.Log(message,sev);
	}

	public void log(String message, Severity sev, Object source) {
		LoggerConsole.Log(message, sev, source);		
	}				

	
	// logging a full object as Json
	public void logObject(Object obj) {
		String objJson = "";
		Gson gson = new Gson();
		objJson =  gson.toJson(obj);
		System.out.println(objJson);
	}

	public static void Log(Exception ex, Severity sev, Object source) {
		System.out.println("ERROR");	
		System.out.println(ex.getMessage());
		System.out.println(ex.getStackTrace());
		System.out.println("source : " +  source);
		if (sev.equals(Severity.CRITICAL))				
			throw new RuntimeException(ex);			
	}

	
	// logging and exception 
	public static void Log(Exception ex, Severity sev){
		System.out.println("ERROR");	
		System.out.println(ex.getMessage());
		System.out.println(ex.getStackTrace());
		if (sev.equals(Severity.CRITICAL))				
			throw new RuntimeException(ex);	
	}
	
	public static void Log(Exception ex){
		LoggerConsole.Log(ex,Severity.ERROR);
	}

	
	// logging an information message
	public static void Log(String message){
		LoggerConsole.Log(message, Severity.INFO);
	}
	
	// logging an information message
	public static void Log(String message, Severity sev){
		System.out.println(message);		
		if (sev.equals(Severity.CRITICAL))				
			throw new RuntimeException(new Exception(message));	
	}


	public static void Log(String message, Severity sev, Object source) {
		System.out.println(message + " source : " + source.toString());		
		if (sev.equals(Severity.CRITICAL))				
			throw new RuntimeException(new Exception(message + " source : " + source.toString()));			
	}				


	// service bus event implementation
	public void EventRaised (ServiceEvent event) {
		this.log(event.message, event.severity);		
	}
	
}
