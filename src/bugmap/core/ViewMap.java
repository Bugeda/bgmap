package bugmap.core;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import bugmap.core.entity.*;


public class ViewMap {		
	static final short MAX_scale = 129;
	static final short MIN_scale = 4;
	static final short START_scale = 100;	
	static final short COL_COUNT=(short) (AppConfig.getAppWidth()/AppConfig.partMapWidth);
	static final short ROW_COUNT=(short) (AppConfig.getAppHeight()/AppConfig.partMapHeight);
	static final byte CAPTION_HEIGHT=25;
	static ImagePanel impanel;
	static JSlider slider;

    MapMouseAdapter movingAdapt = new MapMouseAdapter();
	
	/**
	 * @param y
	 * @param x
	 * @return url to png file with part of map
	 */
	private static String getPartMapUrl(short y, short x){
		return AppConfig.mapsPath+Map.getPngScale()+"_"+y+"_"+x+".png";
	}
	
	/**
	 * Create map with size (column,row)
	 * with coordinates from the Map class
	 * and save link to the image in Map class
	 */	
	private static BufferedImage createMap(short column, short row) {
		
	 //   if ((row*AppConfig.partMapHeight+CAPTION_HEIGHT)<AppConfig.getAppHeight()) row++;
	 //   if ((column*AppConfig.partMapWidth)<AppConfig.getAppWidth()) column++;
		BufferedImage im = new BufferedImage(AppConfig.partMapWidth*column,row*AppConfig.partMapHeight, BufferedImage.TYPE_INT_ARGB);	
    	Map.setImage(im);     
        
	      if (AppConfig.isDEBUG()){
	          	Log.getTRACE().debug("column=" +column); 
	          	Log.getTRACE().debug("row= "+row); 
	          }	      
        try {        	   
    		BufferedImage image = null;		
    		short dx;
    		short dy=Map.getStartRow();    		
            for(short y=0; y < row; y++ ) {        
            	dx=Map.getStartCol();
            	for(short x=0; x < column; x ++ ) { 
            		dx++;
            		if (AppConfig.isDEBUG()){
     	           		Log.getTRACE().debug("add "+getPartMapUrl(dy,dx));        	            		
     	            	}            	            
            	    image = ImageIO.read(new File(getPartMapUrl(dy,dx)));
                    im.getGraphics().drawImage(image, x*AppConfig.partMapWidth, y*AppConfig.partMapHeight, null);                    
                }
            	dy++;
            }        	
        } catch (Exception e) {
    		Log.getTRACE().error(e);
            Log.getWARN().error(e);
            System.exit(1);	 
        }         
        return im;
    }
	
	/**
	 * @param start
	 * @param end
	 * 
	 * redraw map and scroll it 
	 */
	
	public static void scrollMap(Point start, Point end){
		BufferedImage im = Map.getImage();
		short r_count = (short) ((end.x-start.x)/AppConfig.partMapWidth);
		short c_count = (short) ((end.y-start.y)/AppConfig.partMapHeight);
	
		  try {        	 
			  
			  if (r_count<0){		
				  for (short y = (short) (Map.getStartCol()-c_count); y < Map.getStartCol(); y++){
					for(short x = 0; x < r_count; x ++ ) { 	
						BufferedImage xim = ImageIO.read(new File(getPartMapUrl(Map.getStartRow(),x)));
					}
				  }
				//	impanel.setMoveTo(e.getPoint());
			  }				
	        } catch (Exception e) {
	    		Log.getTRACE().error(e);
	            Log.getWARN().error(e);
	            System.exit(1);	 
	        }        	
		  
	}
	/**
	 * create slidebar
	 */
	private static void createSlider() {          
        slider = new JSlider(JSlider.VERTICAL, MIN_scale, MAX_scale, START_scale);
        slider.setMinorTickSpacing(12);  
        slider.setMajorTickSpacing(25);  
        slider.setPaintTicks(true);  
        slider.setPaintLabels(true);
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        //slider.setLabelTable(null);         
    }  
	
	
	private static void createFrame(){		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
    	impanel.setMoveFrom(new Point(0,0));
		impanel.setMoveTo(new Point((-Math.abs(AppConfig.getAppWidth()-Map.getImage().getWidth())/2),-Math.abs((AppConfig.getAppHeight()-Map.getImage().getHeight()+CAPTION_HEIGHT)/2)));
		f.getContentPane().add(new JScrollPane(impanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
													 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));		
		createSlider();   
		f.getContentPane().add(slider, "East");
		 slider.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                int value = ((JSlider)e.getSource()).getValue();     
	                impanel.setScale(value/100.0);	                	               
	            }  
	        });         		 
		f.setSize(AppConfig.getAppWidth(), AppConfig.getAppHeight()); 
		f.setResizable(false);
		//setLocation(1,1);          
		f.setVisible(true);		
	}
		
	/**
	 * Create map from parts of map,
	 * Starts with scale, from (y, x) map parts
	 * @throws IOException
	 */
	public static void Run(int scale, int y, int x) throws IOException{	
		Map.setPngScale(scale);
        Map.setStartCol((short) x);
        Map.setStartRow((short) y); 
		impanel = new ImagePanel(createMap(COL_COUNT,ROW_COUNT));		
		//impanel = new ImagePanel(createMap(1,1,COL_COUNT,2));
		//impanel = new ImagePanel(createMap(1,1,8,3));
		createFrame();		
	}
	/**
	 * Create map from parts of map,
	 * Starts with scale=1, from (1, 1) map parts
	 * @throws IOException
	 */
	public static void Run() throws IOException{		
		Map.setPngScale(1);
        Map.setStartCol((short) 1);
        Map.setStartRow((short) 1); 
		impanel = new ImagePanel(createMap(COL_COUNT,ROW_COUNT));		
		//impanel = new ImagePanel(createMap(1,1,COL_COUNT,2));
		//impanel = new ImagePanel(createMap(1,1,8,3));
		createFrame();		
	}	
	
	/**
	 * Create map from the source,
	 * Starts with scale=1
	 * @throws IOException
	 */
	public static void Run(String source) throws IOException{		
		Map.setPngScale(1);
		impanel = new ImagePanel(source);	
		createFrame();		
	}
}
