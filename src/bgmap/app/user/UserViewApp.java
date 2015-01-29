package bgmap.app.user;

import bgmap.AppConfig;
import bgmap.core.AppGUI;

public class UserViewApp extends AppGUI {
	   public static void main(String[] args){				
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
							createAndShowGUI((byte)1,(byte)50,(byte)50);
		            }
		        });
		}   	   
}
