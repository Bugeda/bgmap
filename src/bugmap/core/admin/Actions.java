package bugmap.core.admin;
import java.io.IOException;




import bugmap.core.*;
import bugmap.core.entity.*;

public class Actions{
	public static void main(String[] args) throws IOException{
		AppConfig.setConfig(false);			
		//ViewMap ma = new ViewMap(AppConfig.mapsPath+"1_0_0.png");
		ViewMap.Run(1,15,120);
	}
}
