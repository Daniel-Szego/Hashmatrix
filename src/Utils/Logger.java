package Utils;
import java.io.StringWriter;

import com.google.gson.Gson;

// simplified logger implementation
// log to command line
public class Logger {
	
	public static LogLevel log = LogLevel.ALL;

	public static void Log(Exception ex){
		Log(ex,Severity.ERROR);
	}
	// logging and exception 
	public static void Log(Exception ex, Severity sev){
		System.out.println("ERROR");	
		System.out.println(ex.getMessage());
		System.out.println(ex.getStackTrace());
		if (sev.equals(Severity.CRITICAL))				
			throw new RuntimeException(ex);	
	}
	
	// logging an information message
	public static void Log(String message){
		Log(message, Severity.INFO);
	}
	
	// logging an information message
	public static void Log(String message, Severity sev){
		System.out.println(message);		
		if (sev.equals(Severity.CRITICAL))				
			throw new RuntimeException(new Exception(message));	
	}
	
	// logging a full object as Json
	public static String LogObject(Object obj) {
		String objJson = "";
		Gson gson = new Gson();
		objJson =  gson.toJson(obj);
		System.out.println(objJson);
		return objJson;
	}
				
}
