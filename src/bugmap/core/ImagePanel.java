package bugmap.core;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import bugmap.core.entity.Log;

    public class ImagePanel extends JPanel {
    	private BufferedImage image;  
 	    double scale; 	        
 	    Point offset = new Point(0, 0);
 	    MapMouseAdapter movingAdapt = new MapMouseAdapter();
 	       		
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
	    
		private void initPanel(){
			scale = 1.0;  			
			addMouseMotionListener(movingAdapt);
			addMouseListener(movingAdapt);
			addMouseWheelListener(movingAdapt);
			setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); 
			setBackground(Color.white); 	
			
		}
		
		public ImagePanel(String img) throws IOException {   
	        loadImage(img);  
	    	initPanel();	              
	    }  
	   
	    public ImagePanel(BufferedImage img) throws IOException {  
	    	image = img;  
	    	initPanel();	        
	    }  	 
 

        @Override
        public Dimension getPreferredSize() {
	        int w = (int)(scale * AppConfig.getAppWidth());  
	        int h = (int)(scale * AppConfig.getAppHeight());  
	        return new Dimension(w, h);  	        
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                if (offset == null) {
                    offset = new Point(0, 0);
                }
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
    	        g2.translate(offset.x,offset.y);
    	        g2.drawRenderedImage(image, at);    	        
    	        g2.dispose();
    	    }
        }          	  
    }

