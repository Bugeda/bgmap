package bgmap.core;
import java.awt.*;
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
 		Point startPoint;
 	    MapMouseAdapter movingAdapt = new MapMouseAdapter(); 	   

		public void setScale(double s) {  
	    	scale = s;  	         
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
			setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); 
			setBackground(Color.white);
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
    	        Graphics2D g2 = (Graphics2D)g;                	   
    	        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    	                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    			if (AppConfig.isWORKDEBUG()){
    	        	AppConfig.lgTRACE.debug("offset=" +offset+" and x.y="+x+"."+y+" scale="+scale);    
    	        }
    	        g2.drawImage(image, offset.x+x, offset.y+y, newImageWidth, newImageHeight,null);    	         
   	            g2.dispose();         	
    	    }
        }

   }

