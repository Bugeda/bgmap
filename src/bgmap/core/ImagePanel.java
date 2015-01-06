package bgmap.core;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import bgmap.core.entity.Map;

    public class ImagePanel extends JPanel {

    	private static Image image;  
 	    double scale; 	        
 	    public Point offset;
 		public Point startPoint;
 	    MapMouseAdapter movingAdapt = new MapMouseAdapter(); 	   

		public void setScale(double s) {  
	    	scale = s;  	 
	    	/*setSize((int) (Map.getSize().width*scale), (int) (Map.getSize().height*scale));
	    	 int x = (int) ((Map.getSize().width*scale)/2);  
	         int y = (int) ((Map.getSize().height*scale)/2);   
	    	setLocation(offset.x, offset.y);*/
	        repaint();  

	    }  
		
	    public void loadImage(String fileName) {          
	        try {
	        	if (image!=null) image.flush();
				image = ImageIO.read(new File(fileName));
				offset = new Point(0, 0);				
			} catch (IOException e) {								
					AppConfig.lgTRACE.error(e+" "+fileName);
		            AppConfig.lgWARN.error(e+" "+fileName);
		            System.exit(1);			
			}                                        
	    }
	    
		public void loadImage(Image newIm) {
			if (image!=null) image.flush();					
			image = newIm;
			offset = new Point(0, 0);
		}  
	    
		public void setMoveFrom(Point p){
			startPoint = p;
			startPoint.x -= offset.x;
			startPoint.y -= offset.y;		
		}
		
		public void setMoveTo(Point p){		   
			if (startPoint!=null) 
				offset = new Point(p.x - startPoint.x, p.y - startPoint.y);
	        repaint();
		}
		
		private void initPanel(){
			scale = 1.0;  			
			Map.setMapOffset(new Point(0,0));	
			addMouseMotionListener(movingAdapt);
			addMouseListener(movingAdapt);
			addMouseWheelListener(movingAdapt);
			setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); 
			//setBackground(Color.white);
		}
		
		public ImagePanel(String img) throws IOException {   
	        loadImage(img);  
	        initPanel();	    		              
	    }  
	   
	    public ImagePanel(Image img) throws IOException {  
	    	loadImage(img);  
	    	initPanel();	        
	    }  	 	    
	    
        @Override
        protected void paintComponent(Graphics g) {
        	
	      	super.paintComponent(g);
            if (image != null) {                  	
    	        int newImageWidth = (int) (image.getWidth(null)*scale);  
    	        int newImageHeight = (int) (image.getHeight(null)*scale);    	   	     
    	        int x = (getWidth() - newImageWidth)/2;  
    	        int y = (getHeight() - newImageHeight)/2;   
    	        //int x = 0;  
    	        //int y = 0;
    	        Graphics2D g2 = (Graphics2D)g;                	   
    	        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    	                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    			if (AppConfig.isWORKDEBUG()){
    	        	AppConfig.lgTRACE.debug("offset=" +offset+" and x.y="+x+"."+y+" scale="+scale);    
    	        }
    		    g2.drawImage(image, offset.x+x, offset.y+y, newImageWidth, newImageHeight,null);
    			//Image subImage = ((BufferedImage) image).getSubimage(0, 0, newImageWidth-offset.x, newImageHeight-offset.y);
    	    			
    			//g2.drawImage(subImage, offset.x+x, offset.y+y, newImageWidth-offset.x, newImageHeight-offset.y,null);    	         
   	            g2.dispose();         	
   	                	        
    	    }
             
        }

   }

