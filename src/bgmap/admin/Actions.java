package bgmap.admin;
import java.io.IOException;

import bgmap.core.*;

public class Actions{
	public static void main(String[] args) throws IOException{
		AppConfig.setConfig(true);			
		//ViewMap ma = new ViewMap(AppConfig.mapsPath+"1_0_0.png");
		ViewMap.Run((byte)1,(byte)50,(byte)23);
	}
}
