package bugmap.core;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import bugmap.core.entity.*;


public class MapActions extends JFrame {		
	static ImagePanel impanel;
	static JSlider slider;
	static final int MAX_scale = 129;
	static final int MIN_scale = 4;
	static final int START_scale = 100;	
    MapMouseAdapter movingAdapt = new MapMouseAdapter();
    
	private static BufferedImage createMap(int size, int startposition, int column, int row) {      
	      if (AppConfig.isDEBUG()){
          	Log.getTRACE().debug("column=" +column); 
          	Log.getTRACE().debug("row= "+row); 
          }
		BufferedImage im = new BufferedImage(AppConfig.mapWidth*column,row*AppConfig.mapHeight, BufferedImage.TYPE_INT_ARGB);
		BufferedImage image = null;
		
        try {        	              	
            for(int y=0; y < row; y++ ) {        
            	startposition++;
            	for(int x=0; x < column; x ++ ) {          
            	    if (AppConfig.isDEBUG()){
       	            		Log.getTRACE().debug("add "+AppConfig.mapsPath+size+"_"+startposition+"_"+x+".png");        	            		
       	            	}
            	    
            	  //   java.net.URL imageurl = MapActions.class.getResource(AppConfig.mapsPath+"1_"+y+"_"+x+".png");
//            	     System.out.println(imageurl);
  //          	     Image imageX = new javax.swing.ImageIcon(imageurl).getImage();
           	    
            	    image = ImageIO.read(new File(AppConfig.mapsPath+size+"_"+startposition+"_"+x+".png"));
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
	private static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon)icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge =
              GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
	private static void createSlider() {          
        slider = new JSlider(JSlider.VERTICAL, MIN_scale, MAX_scale, START_scale);
        slider.setInverted(true);
        slider.setMinorTickSpacing(12);  
        slider.setMajorTickSpacing(25);  
        slider.setPaintTicks(true);  
        slider.setPaintLabels(true);
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //slider.setLabelTable(null);         
    }  
	
	
	private void setFrameParam(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                    
		getContentPane().add(new JScrollPane(impanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
													 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		//getContentPane().add(new JScrollPane(impanel));        

		createSlider();   
		impanel.setDoubleBuffered(false);
		getContentPane().add(slider, "East");
		 slider.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                int value = ((JSlider)e.getSource()).getValue();     
	                impanel.setScale(value/100.0);	                	               
	            }  
	        });         
		setSize(AppConfig.getAppWidth(), AppConfig.getAppHeight());  
		setLocation(1,1);          
		setVisible(true);			
	}
		
	
	public MapActions() throws IOException{		
		//ImagePanel impanel = new ImagePanel(createMap(AppConfig.getAppWidth()/AppConfig.mapWidth,AppConfig.getAppHeight()/AppConfig.mapHeight));		
		//impanel = new ImagePanel(createMap((int)Math.ceil((double)AppConfig.getAppWidth() / AppConfig.mapWidth),2));
		impanel = new ImagePanel(createMap(1,45,182,10));
		setFrameParam();		
	}
		
	public MapActions(String source) throws IOException{		
		impanel = new ImagePanel(source);	
		setFrameParam();		
	}
}
