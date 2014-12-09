package bugmap.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import bugmap.core.entity.Log;
import bugmap.core.entity.Map;

public class MapActions {
	private static BufferedImage createMap(int column,int row) {      
	      if (AppConfig.isDEBUG()){
          	Log.getTRACE().debug("column=" +column); 
          	Log.getTRACE().debug("row= "+row); 
          }
		BufferedImage im = new BufferedImage(AppConfig.mapWidth*column,row*AppConfig.mapHeight, BufferedImage.TYPE_INT_ARGB);
		BufferedImage image = null;
        try {        	            
            for(int y=0; y < row; y++ ) {
            	for(int x=0; x < column; x ++ ) {          
            	    if (AppConfig.isDEBUG()){
       	            		Log.getTRACE().debug("add "+AppConfig.mapsPath+"1_"+y+"_"+x+".png");        	            		
       	            	}
            	    image = ImageIO.read(new File(AppConfig.mapsPath+"1_"+y+"_"+x+".png"));
                    im.getGraphics().drawImage(image, x*AppConfig.mapWidth, y*AppConfig.mapHeight, null);
                    
                }
            }     
        } catch (Exception e) {
    		Log.getTRACE().error(e);
            Log.getWARN().error(e);
            System.exit(1);	 
        }        
        return im;
    }
	
	
	public static void showMaps() throws IOException {	
		//ImagePanel impanel = new ImagePanel(createMap(AppConfig.getAppWidth()/AppConfig.mapWidth,AppConfig.getAppHeight()/AppConfig.mapHeight));
		;
		ImagePanel impanel = new ImagePanel(createMap((int)Math.ceil((double)AppConfig.getAppWidth() / AppConfig.mapWidth),2));
		ImageZoom imzoom = new ImageZoom(impanel);
		JFrame f = new JFrame();  
		
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.getContentPane().add(imzoom.getUIPanel(), "North"); 
        
        f.getContentPane().add(new JScrollPane(impanel));        
        f.setSize(AppConfig.getAppWidth(), AppConfig.getAppHeight());  
        f.setLocation(1,1);  
        f.setVisible(true); 
	}
	
	public static void showMap(String source) throws IOException {			
		ImagePanel impanel = new ImagePanel(source);	
		ImageZoom imzoom = new ImageZoom(impanel);
		JFrame f = new JFrame();  
		
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.getContentPane().add(imzoom.getUIPanel(), "North"); 
        
        f.getContentPane().add(new JScrollPane(impanel));              
        f.setSize(AppConfig.getAppWidth(), AppConfig.getAppHeight());  
        f.setLocation(1,1);  
        f.setVisible(true); 
	}
	
}
