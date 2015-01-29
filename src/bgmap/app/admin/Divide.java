package bgmap.app.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bgmap.*;
import bgmap.core.AppGUI;
import bgmap.core.model.*;

public class Divide extends AppGUI{	
	public static final String srcMapPath = "src/images/src/";
	
	public static void main(String[] args) throws IOException {
		
		//29120 x 43680   
		//320 x 480
		AppConfig.setConfig(false, true, true);
		//ViewMapTest ma = new ViewMapTest(srcMapPath+"p1.png");		
		 int pos=47;
		 String file=srcMapPath+"p31.png";
		 byte x = 0;
		 try {     
			 
			 BufferedImage image = ImageIO.read(new File(file));
			 int column = image.getWidth(null)/Map.partMapWidth;			
	    	 int row = image.getHeight(null)/Map.partMapHeight; 
	         if (AppConfig.isDEBUG()){
	        	 AppConfig.lgTRACE.debug("column=" +column); 
	        	 AppConfig.lgTRACE.debug("row= "+row); 
	        	 }
	        
	         BufferedImage newImage = new BufferedImage(Map.partMapWidth, Map.partMapHeight, BufferedImage.TYPE_INT_RGB); 
	         for(byte y = 0 ; y < row; y++) {
	        	 
	        	 AppConfig.lgDEBUG.debug("create "+AppConfig.mapsPath+"1_"+pos+"_x.png");
	        	 for(x = 0; x < column; x++ ) {               
	        		 file = AppConfig.mapsPath+"1_"+pos+"_"+x+".png";               	   		
	        		 newImage.createGraphics().drawImage(image, -x * Map.partMapWidth, -y * Map.partMapHeight, null);            	             		
	        		 ImageIO.write(newImage, "png", new File(file)); 
	        		 if (AppConfig.isDEBUG()) 
	        			 AppConfig.lgDEBUG.debug("create "+file);
	        	 	}
	        	 pos++;
	        	 }
	         } catch (Exception e) {	        	 
	        	 AppConfig.lgTRACE.error(e+" "+file);
	        	 AppConfig.lgWARN.error(e+" "+file);
	        	 System.exit(1);	            
	        	 }
		 	AppConfig.lgTRACE.info("image has been cut");
		 	}		     
}
