package bugmap.core.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bugmap.core.*;
import bugmap.core.entity.*;

public class Divide {	
	public static final String srcMapPath = "src/images/src/";
	
	public static void main(String[] args) throws IOException {
		
		//29120 x 43680   
		//320 x 480
		AppConfig.setConfig(false);		
		MapActions.showMap(srcMapPath+"f.png");		
	
		 try {     
			 BufferedImage image = ImageIO.read(new File(srcMapPath+"f.png"));
			 int column = image.getWidth(null)/AppConfig.mapWidth;			
	    	 int row = image.getHeight(null)/AppConfig.mapHeight; 
	         if (AppConfig.isDEBUG()){
	        	 Log.getTRACE().debug("column=" +column); 
	        	 Log.getTRACE().debug("row= "+row); 
	        	 }
	            
	         for(int y = 0 ; y < row; y++) {
	        	 for(int x = 0; x < column; x++ ) {               
	        		 BufferedImage newImage = new BufferedImage(AppConfig.mapWidth, AppConfig.mapHeight, BufferedImage.TYPE_INT_ARGB);                	   		
	        		 newImage.createGraphics().drawImage(image, x*(-AppConfig.mapWidth), y*(-AppConfig.mapHeight), null);            	             		
	        		 ImageIO.write(newImage, "png", new File(AppConfig.mapsPath+"1_"+y+"_"+x+".png")); 
	        		 if (AppConfig.isDEBUG()) 
	        			 Log.getDEBUG().debug("create "+AppConfig.mapsPath+"1_"+y+"_"+x+".png");
	        	 	}
	        	 }
	         } catch (Exception e) {	        	 
	        	 Log.getTRACE().error(e+" "+srcMapPath+"f.png");
	        	 Log.getWARN().error(e+" "+srcMapPath+"f.png");
	        	 System.exit(1);	            
	        	 }
		 	Log.getTRACE().info("image has cut");
		 	}		     
}
