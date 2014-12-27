package bgmap.core;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import org.apache.log4j.*;

public class AppConfig {	

	public static final Logger lgTRACE = Logger.getRootLogger();
	public static final Logger lgWARN = Logger.getLogger("warnfile");
	public static final Logger lgDEBUG = Logger.getLogger("debugfile");
	
	private static boolean DEBUG = false;
	
	private static boolean WORKDEBUG = false;
	
	public static final String mapsPath = "src/images/maps/";
		
	public static final int appWidth = Toolkit.getDefaultToolkit().getScreenSize().width
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).left 
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).right;

	public static final int appHeight = Toolkit.getDefaultToolkit().getScreenSize().height
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).bottom 
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).top;

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean debug) {
		DEBUG = debug;
	}		

	public static void setConfig(boolean debug){
		PropertyConfigurator.configure("log4j.properties");	
		DEBUG = debug;
	}

	public static boolean isWORKDEBUG() {
		return WORKDEBUG;
	}

	public static void setWORKDEBUG(boolean wORKDEBUG) {
		WORKDEBUG = wORKDEBUG;
	}
}

