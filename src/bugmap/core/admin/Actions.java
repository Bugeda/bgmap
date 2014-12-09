package bugmap.core.admin;
import java.io.IOException;

import bugmap.core.*;

public class Actions{
	public static void main(String[] args) throws IOException{
		AppConfig.setConfig(false);	
		//MapActions.showMap(AppConfig.mapsPath+"1_0_0.png");
		MapActions.showMaps();
	}
}
