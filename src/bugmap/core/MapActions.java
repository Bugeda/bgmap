package bugmap.core;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
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
	static final int MAX_scale = 150;
	static final int MIN_scale = 4;
	static final int START_scale = 100;	
    private Point mouseClickPoint;   
    MouseListener movingAdapt = new MouseListener();
    
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
	
	private static void createSlider() {          
        slider = new JSlider(JSlider.VERTICAL, MIN_scale, MAX_scale, START_scale);
        slider.setInverted(true);
        slider.setMinorTickSpacing(12);  
        slider.setMajorTickSpacing(25);  
        slider.setPaintTicks(true);  
        slider.setPaintLabels(true);
        //slider.setLabelTable(null);         
    }  
	
	/*
	 *  private static void addMouseListeners(){
		  slider.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                int value = ((JSlider)e.getSource()).getValue();          
	                impanel.setScale(value/10.0);                
	            }  
	        });            
		  slider.addMouseWheelListener(new MouseWheelListener(){
	    		public void mouseWheelMoved(MouseWheelEvent e){
	    			slider.setValue(e.getWheelRotation()*10);;
	    			
	    		}    
	        });      
		  impanel.addMouseWheelListener(new MouseWheelListener(){
	    		public void mouseWheelMoved(MouseWheelEvent e){
	    			int wheel = e.getWheelRotation();
	    		      if (AppConfig.isDEBUG()){
			          	Log.getTRACE().debug("wheel=" +wheel); 
			          	Log.getTRACE().debug("slider.getValue()= "+slider.getValue());
			          	Log.getTRACE().debug("impanel.scale= "+impanel.scale); 
			          }
	    			if ((wheel>0)&&(slider.getValue()+wheel>MIN_scale)){
	    				slider.setValue(slider.getValue()+wheel);
	    			
	    			}
	      			if ((wheel<0)&&(slider.getValue()+wheel<MAX_scale)){
	    				slider.setValue(slider.getValue()+wheel);
	    			
	    			}
	    		}    
	        });
		  impanel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});			  		 
	}*/
	
	private void setFrameParam(){
	//	setBackground(Color.white); 	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                    
		//getContentPane().add(new JScrollPane(impanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
			//										 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		getContentPane().add(new JScrollPane(impanel));        

		createSlider();   
		impanel.setDoubleBuffered(true);
		getContentPane().add(slider, "East");
		 slider.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                int value = ((JSlider)e.getSource()).getValue();          
	                impanel.setScale(value/100.0);                
	            }  
	        });         
	//	impanel.addMouseMotionListener(movingAdapt);
	//	impanel.addMouseListener(movingAdapt);
		impanel.addMouseWheelListener(movingAdapt);   
		setSize(AppConfig.getAppWidth(), AppConfig.getAppHeight());  
		setLocation(1,1);          
		setVisible(true);		
	}
		
	
	public MapActions() throws IOException{		
		//ImagePanel impanel = new ImagePanel(createMap(AppConfig.getAppWidth()/AppConfig.mapWidth,AppConfig.getAppHeight()/AppConfig.mapHeight));		
		//impanel = new ImagePanel(createMap((int)Math.ceil((double)AppConfig.getAppWidth() / AppConfig.mapWidth),2));
		impanel = new ImagePanel(createMap(182,2));
		setFrameParam();		
	}
		
	public MapActions(String source) throws IOException{		
		impanel = new ImagePanel(source);	
		setFrameParam();		
	}
}
