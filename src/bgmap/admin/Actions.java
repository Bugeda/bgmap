package bgmap.admin;
import java.awt.Point;
import java.io.IOException;

import bgmap.core.*;

public class Actions{
	public static void main(String[] args) throws IOException{
		AppConfig.setConfig(true);			
		//ViewMapTest ma = new ViewMapTest(AppConfig.mapsPath+"1_0_0.png");
		ViewMap.Run((byte)1,(byte)50,(byte)23);
		/*		ViewMap.impanel.setMoveFrom(new Point(0, 0));
		ViewMap.impanel.setMoveTo(new Point(182, 362));
		ViewMap.addPartsMap();
		ViewMap.impanel.setMoveTo(new Point(110, 50));
		ViewMap.addPartsMap();
		ViewMap.impanel.setMoveTo(new Point(89, -21));
		ViewMap.addPartsMap();
		ViewMap.impanel.setMoveTo(new Point(-189, -47));
		ViewMap.addPartsMap();*/
		ViewMap.impanel.startPoint = null;
	}
}
