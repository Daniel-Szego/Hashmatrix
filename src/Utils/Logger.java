package Utils;
import java.io.StringWriter;

import com.google.gson.Gson;

import ServiceBus.*;

// simplified logger implementation
// log to command line
public class Logger implements ServiceListener{
	
	public LogLevel log = LogLevel.ALL;

	public void Logg(Exception ex){
		Logger.Log(ex,Severity.ERROR);
	}
	// logging and exception 
	public void Logg(Exception ex, Severity sev){
		Logger.Log(ex,sev);		
	}
	
	// logging an information message
	public void Logg(String message){
		Logger.Log(message);

	}
	
	// logging an information message
	public void Logg(String message, Severity sev){
		Logger.Log(message,sev);
	}
	
	// logging a full object as Json
	public String LogObject(Object obj) {
		String objJson = "";
		Gson gson = new Gson();
		objJson =  gson.toJson(obj);
		System.out.println(objJson);
		return objJson;
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
		Logger.Log(ex,Severity.ERROR);
	}

	
	// logging an information message
	public static void Log(String message){
		Logger.Log(message, Severity.INFO);
	}
	
	// logging an information message
	public static void Log(String message, Severity sev){
		System.out.println(message);		
		if (sev.equals(Severity.CRITICAL))				
			throw new RuntimeException(new Exception(message));	
	}
		
	// service bus event implementation
	public void EventRaised (ServiceEvent event) {
		this.Logg(event.message, event.severity);		
	}				
}
