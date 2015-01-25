package bgmap.app.admin;

import bgmap.core.AppConfig;
import bgmap.core.view.AppGUI;


public class AdminGUI extends AppGUI {
	
	public static void main(String[] args){
		AppConfig.setConfig(false, true, true);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            		createAndShowGUI((byte)1,(byte)50,(byte)23);
	            }
	        });
	}    
}
