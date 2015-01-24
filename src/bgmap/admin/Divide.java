package bgmap.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bgmap.core.*;
import bgmap.core.model.*;

public class Divide {	
	public static final String srcMapPath = "src/images/src/";
	
	public static void main(String[] args) throws IOException {
		
		//29120 x 43680   
		//320 x 480
		AppConfig.setConfig(false, true);
		//ViewMapTest ma = new ViewMapTest(srcMapPath+"p1.png");		
	
		 try {     
			 BufferedImage image = ImageIO.read(new File(srcMapPath+"p22.png"));
			 int column = image.getWidth(null)/Map.partMapWidth;			
	    	 int row = image.getHeight(null)/Map.partMapHeight; 
	         if (AppConfig.isDEBUG()){
	        	 AppConfig.lgTRACE.debug("column=" +column); 
	        	 AppConfig.lgTRACE.debug("row= "+row); 
	        	 }
	         int pos=36;
	         BufferedImage newImage = new BufferedImage(Map.partMapWidth, Map.partMapHeight, BufferedImage.TYPE_INT_RGB); 
	         for(byte y = 0 ; y < row; y++) {
	        	 
	        	 AppConfig.lgDEBUG.debug("create "+AppConfig.mapsPath+"1_"+pos+"_x.png");
	        	 for(byte x = 0; x < column; x++ ) {               
	        		                	   		
	        		 newImage.createGraphics().drawImage(image, -x * Map.partMapWidth, -y * Map.partMapHeight, null);            	             		
	        		 ImageIO.write(newImage, "png", new File(AppConfig.mapsPath+"1_"+pos+"_"+x+".png")); 
	        		 if (AppConfig.isDEBUG()) 
	        			 AppConfig.lgDEBUG.debug("create "+AppConfig.mapsPath+"1_"+pos+"_"+x+".png");
	        	 	}
	        	 pos++;
	        	 }
	         } catch (Exception e) {	        	 
	        	 AppConfig.lgTRACE.error(e+" "+srcMapPath+"f.png");
	        	 AppConfig.lgWARN.error(e+" "+srcMapPath+"f.png");
	        	 System.exit(1);	            
	        	 }
		 	AppConfig.lgTRACE.info("image has been cut");
		 	}		     
}
