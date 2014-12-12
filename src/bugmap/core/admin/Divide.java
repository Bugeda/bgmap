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
		//ViewMap ma = new ViewMap(srcMapPath+"p1.png");		
	
		 try {     
			 BufferedImage image = ImageIO.read(new File(srcMapPath+"p12.png"));
			 int column = image.getWidth(null)/AppConfig.partMapWidth;			
	    	 int row = image.getHeight(null)/AppConfig.partMapHeight; 
	         if (AppConfig.isDEBUG()){
	        	 Log.getTRACE().debug("column=" +column); 
	        	 Log.getTRACE().debug("row= "+row); 
	        	 }
	         int pos=0;
	         for(int y = 0 ; y < row; y++) {
	        	 pos++;
	        	 Log.getDEBUG().debug("create "+AppConfig.mapsPath+"1_"+pos+"_x.png");
	        	 for(int x = 0; x < column; x++ ) {               
	        		 BufferedImage newImage = new BufferedImage(AppConfig.partMapWidth, AppConfig.partMapHeight, BufferedImage.TYPE_INT_ARGB);                	   		
	        		 newImage.createGraphics().drawImage(image, x*(-AppConfig.partMapWidth), y*(-AppConfig.partMapHeight), null);            	             		
	        		 ImageIO.write(newImage, "png", new File(AppConfig.mapsPath+"1_"+pos+"_"+x+".png")); 
	        		 if (AppConfig.isDEBUG()) 
	        			 Log.getDEBUG().debug("create "+AppConfig.mapsPath+"1_"+pos+"_"+x+".png");
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
