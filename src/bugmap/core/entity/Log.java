package bugmap.core.entity;
import org.apache.log4j.Logger;


public class Log {
	private static Logger TRACE;
	private static Logger WARN;
	private static Logger DEBUG;
	public static Logger getTRACE() {
		return TRACE;
	}
	public static void setTRACE(Logger trace) {
		TRACE = trace;
	}
	public static Logger getWARN() {
		return WARN;
	}
	public static void setWARN(Logger warn) {
		WARN = warn;
	}
	public static Logger getDEBUG() {
		return DEBUG;
	}
	public static void setDEBUG(Logger debug) {
		DEBUG = debug;
	}
	
	
}
