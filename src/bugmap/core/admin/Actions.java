package bugmap.core.admin;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bugmap.core.*;
import bugmap.core.entity.Log;

public class Actions{
	public static void main(String[] args) throws IOException{
		AppConfig.setConfig(false);	
		//MapActions.showMap(AppConfig.mapsPath+"1_0_0.png");
		MapActions.showMaps();
	}
}
