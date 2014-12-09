package bugmap.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import bugmap.core.entity.*;

public class ImagePanel extends JPanel {
	    BufferedImage image;  
	    double scale; 	        
	    
	    public ImagePanel(String img) throws IOException {   
	        loadImage(img);  
	        scale = 1.0;  
	        //setBackground(Color.white); 	       
	    }  
	   
	    public ImagePanel(BufferedImage img) throws IOException {  
	    	image = img;  
	        scale = 1.0;  
	        //setBackground(Color.white); 	        
	    }  	   
	    
	    protected void paintComponent(Graphics g){  
	        super.paintComponent(g);  
	        Graphics2D g2 = (Graphics2D)g;  
	        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,  
	                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);  
	        int w = getWidth();  
	        int h = getHeight();  
	        int imageWidth = image.getWidth();  
	        int imageHeight = image.getHeight();  
	        double x = (w - scale * imageWidth)/2;  
	        double y = (h - scale * imageHeight)/2;  
	        if (AppConfig.isDEBUG()){
	        	Log.getTRACE().debug("new coordinates = ("+ x+","+y+")");
	        	Log.getTRACE().debug("scale to "+ scale);	
	        	Log.getTRACE().debug("new size = ("+ w+","+h+")");
	        }
	        AffineTransform at = AffineTransform.getTranslateInstance(x,y);  
	        at.scale(scale, scale);  
	        g2.drawRenderedImage(image, at);       
	    }  
	    
	    @Override
	    public void paint(Graphics graphics) {
	    	paintComponent(graphics);
	    }

	    /** 
	     * For the scroll pane. 
	     */  
	    public Dimension getPreferredSize() {  
	        int w = (int)(scale * image.getWidth());  
	        int h = (int)(scale * image.getHeight());  
	        return new Dimension(w, h);  
	    }  
	   
	    public void setScale(double s) {  
	    	scale = s;  	        
	        revalidate();      // update the scroll pane  
	        repaint();  
	    }  
	   
	    public void loadImage(String fileName) {          
	        try {
				image = ImageIO.read(new File(fileName));
			} catch (IOException e) {								
					Log.getTRACE().error(e+" "+fileName);
		            Log.getWARN().error(e+" "+fileName);
		            System.exit(1);			
			}                                        
	    }
}
