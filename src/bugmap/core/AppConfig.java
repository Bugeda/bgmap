package bugmap.core;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import org.apache.log4j.*;

import bugmap.core.entity.*;

public class AppConfig {	

	private static boolean DEBUG = false;
	
	public static final String mapsPath = "src/images/maps/";
	
	// - slider.width
	private static int appWidth = Toolkit.getDefaultToolkit().getScreenSize().width
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).left 
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).right;
	//- taskbar
	private static int appHeight = Toolkit.getDefaultToolkit().getScreenSize().height
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).bottom 
			- Toolkit.getDefaultToolkit().getScreenInsets(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			).top;

	
	public static final int partMapWidth = 160;
	public static final int partMapHeight = 240;
	
	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean debug) {
		DEBUG = debug;
	}	
	
	public static int getAppWidth() {
		return appWidth;
	}

	public static void setAppWidth(int appWidth) {
		AppConfig.appWidth = appWidth;
	}

	public static int getAppHeight() {
		return appHeight;
	}

	public static void setAppHeight(int appHeight) {
		AppConfig.appHeight = appHeight;
	}

	public static void setLog4j() {		
		PropertyConfigurator.configure("log4j.properties");
		Log.setTRACE(Logger.getRootLogger());
		Log.setWARN(Logger.getLogger("warnfile"));
		Log.setDEBUG(Logger.getLogger("debugfile"));
	}		
	
	public static void setConfig(boolean debug){
		setLog4j();		
		DEBUG = debug;
	}
	
	public static void setApplicationSize(int appWidth, int appHeight){
		if (appWidth>0)
			AppConfig.appWidth = appWidth;
		if (appHeight>0)
			AppConfig.appHeight = appHeight;
	}
	
	public static void setConfig(boolean debug, int appWidth, int appHeight){
		setLog4j();		
		DEBUG = debug;
		setApplicationSize(appWidth,appHeight);
	}
	
}

