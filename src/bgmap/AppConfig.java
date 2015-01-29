package bgmap;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import org.apache.log4j.*;

public class AppConfig {	

	public static final Logger lgTRACE = Logger.getRootLogger();
	public static final Logger lgWARN = Logger.getLogger("warnfile");
	public static final Logger lgDEBUG = Logger.getLogger("debugfile");
	public static final String mapsPath = "src/images/maps/";
		
	public static final Image[] MafsMarks =  {
			new ImageIcon("src/images/src/sign.png").getImage(),   	  
			new ImageIcon("src/images/src/signFull.png").getImage(),		  
			new ImageIcon("src/images/src/signOn.png").getImage(),
			new ImageIcon("src/images/src/signNew.png").getImage()
	};
	/*public static final Image sign = new ImageIcon("src/images/src/sign.png").getImage();
	public static final Image signFull = new ImageIcon("src/images/src/signFull.png").getImage();
	public static final Image signOn = new ImageIcon("src/images/src/signOn.png").getImage();
	public static final Image signTemp = new ImageIcon("src/images/src/signTemp.png").getImage();*/
	
	private static boolean DEBUG = false;
	private static boolean WORKDEBUG = false;
	private static boolean ADMIN = false;
	
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



	public static boolean isAdmin() {
		return ADMIN;
	}

	protected static void setAdmin(boolean ADMIN) {
		AppConfig.ADMIN = ADMIN;
	}
	
	public static boolean isDEBUG() {
		return DEBUG;
	}

	protected static void setDEBUG(boolean debug) {
		AppConfig.DEBUG = debug;
	}		

	public static boolean isWORKDEBUG() {
		return WORKDEBUG;
	}

	protected static void setWORKDEBUG(boolean wORKDEBUG) {
		WORKDEBUG = wORKDEBUG;
	}
	
	/**
	 * Set application configuration
	 * 
	 * @param debug - for logs release version's in file 
	 * @param wORKDEBUG  - for debugging at develop time
	 * @param isAdmin - if adminApp
	 */

	protected static void setConfig(boolean debug, boolean workDebug, boolean isAdmin){
		PropertyConfigurator.configure("src/log4j.properties");	
		AppConfig.DEBUG = debug;
		AppConfig.WORKDEBUG = workDebug;
		AppConfig.ADMIN = isAdmin;
	}
}

