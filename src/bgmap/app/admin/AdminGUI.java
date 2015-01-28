package bgmap.app.admin;

import bgmap.AppConfig;
import bgmap.core.AppGUI;


public class AdminGUI extends AppGUI {
	
	public static void main(String[] args){
		AppConfig.setConfig(false, false, true);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            		createAndShowGUI((byte)1,(byte)50,(byte)23);
	            }
	        });
	}    
}
