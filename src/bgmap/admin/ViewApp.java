package bgmap.admin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import bgmap.core.AppConfig;
import bgmap.core.ImagePanel;
import bgmap.core.MapMouseAdapter;
import bgmap.core.ThreadMapPart;
import bgmap.core.entity.*;

public class ViewApp {		
	public static final byte MAX_scale = 127;
	public static final byte MIN_scale = 4;
	public static final byte START_scale = 100;	
	public static final byte CAPTION_HEIGHT=25;
	public static ImagePanel impanel;
	public static JSlider slider;
    static Image imageArea;
    MapMouseAdapter movingAdapt = new MapMouseAdapter();
	
    public static void main(String[] args) throws IOException{
		AppConfig.setConfig(true);			
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	try {
						createAndShowGUI((byte)1,(byte)50,(byte)23);
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	        });
	}    
	/**
	 * @param y
	 * @param x
	 * @return url to png file with part of map
	 */
	public static String getPartMapUrl(byte y, byte x){
		return AppConfig.mapsPath+Map.getPngScale() + "_" + y + "_" + x + ".png";
	}
	/**
	 * Load map from parts with size (column,row)
	 * with coordinates from (startColumn, startRow)
	 * and scale.
	 * @return 
	 * @return Image
	 */
	private static void loadPartsMap(byte scale, byte startColumn, byte startRow, byte columnCount, byte rowCount, int drawX, int drawY){
				byte dx;
	    		byte dy=startRow;    		
	            for(byte y=0; y < rowCount; y++ ) {        
	            	dx=startColumn;
	            	for(byte x=0; x < columnCount; x ++ ) { 
	            		Runnable r = new ThreadMapPart(dx, dy, x, y, drawX, drawY);
	            		Thread t = new Thread(r);
	            		t.setPriority(t.MAX_PRIORITY);
	            		t.start();	      
	            		dx++;
	                }
	            	dy++;
	            }        	  	
	}
	
	/**
	 * Create map with size (column,row)
	 * with coordinates from (startColumn, startRow)
	 * and scale.
	 * Save link to the image in Map.class
	 * @return Image
	 */	
	private static Image createMap(byte scale, byte startColumn, byte startRow) {	
		startColumn-=Map.COL_COUNT;
		startRow-=Map.ROW_COUNT;
		Image im = new BufferedImage(Map.partMapWidth * Map.COL_COUNT,Map.partMapHeight * Map.ROW_COUNT, BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));
		try {        				 
	        	Image image = null;		        	
	    		byte dx;
	    		byte dy=startRow;    		
	            for(byte y=0; y < Map.ROW_COUNT; y++ ) {        
	            	dx=startColumn;
	            	for(byte x=0; x < Map.COL_COUNT; x ++ ) { 		            	
	            		image = new javax.swing.ImageIcon(ViewApp.getPartMapUrl(dy,dx)).getImage();            		
	                    g.drawImage(image, x * Map.partMapWidth, y * Map.partMapHeight, null);
	            		g.drawRect(x * Map.partMapWidth, y * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);	            	
	            		g.drawString(dy+" "+dx, x* Map.partMapWidth+150, y* Map.partMapHeight+200);
	            		image.flush();
	            		dx++;
	                }
	            	dy++;
	            }        	        
	            g.dispose();
	        	Map.setImage(im);     
	    		Map.setPngScale(scale);
	            Map.setStartCol(startColumn);
	            Map.setStartRow(startRow); 
	            
	        } catch (Exception e) {
	    		AppConfig.lgTRACE.error(e);
	            AppConfig.lgWARN.error(e);
	            System.exit(1);	 
	        } 
		return im; 
    }
	/**
	 * Redraw map and scroll it 
	 */
	public static void paintMap(){
		if ((impanel.offset.x!=0)||(impanel.offset.y!=0)){
			Image newImage = new BufferedImage(Map.getSize().width,Map.getSize().height, BufferedImage.TYPE_INT_RGB);			
			Image subImage = ((BufferedImage) Map.getImage()).getSubimage(0, 0, Map.getSize().width,Map.getSize().height);
			Graphics g = newImage.getGraphics();
			g.drawImage(subImage, (int)(impanel.offset.x), (int)(impanel.offset.y), null);
			Map.setImage(newImage);
			
			// absolute new coordinates for lefttop full cell 
			int x =  impanel.offset.x + Map.getMapOffset().x;	
			int y = impanel.offset.y + Map.getMapOffset().y;
			
			//  signX, signY use when we need calculate absolute left or right side from result position
			//  signMoveX, signMoveY use when we need calculate just mouse move
			byte signX = (byte) (x > 0 ? 1 : 0);	
			byte signMoveX = (byte) (impanel.offset.x > 0 ? 1 : 0);	
			byte signY = (byte) (y > 0 ? 1 : 0);	
			byte signMoveY = (byte) (impanel.offset.y > 0 ? 1 : 0);	
			
			/* Calculate coordinates and parts of one cell at axes
			 * right/down
			 * dx = 0, ax = 0 => Map.partMapWidth
			 * dx = -, ax = 0 => Map.partMapWidth - abs(dx)
			 * dx = +. ax = 1 => dx
			 * left/up
			 * dx = 0, ax = 0 => 0
			 * dx = -, ax = 0 => - abs(dx)
			 * dx = +. ax = 1 => dx - Map.partMapWidth
			 */								
			int dx = x % Map.partMapWidth;		
			int rightPartCellWidth =(Map.partMapWidth) * (1 - signX) + dx ;
			int leftPartCellWidth = rightPartCellWidth - Map.partMapWidth;
			
			int dy =  y % Map.partMapHeight;
			int downPartCellHeight = (Map.partMapHeight) * (1 - signY ) + dy;
			int upPartCellHeight = downPartCellHeight - Map.partMapHeight;
			
			//extra used for change picture when x/y < one cell
			byte extraCols = (byte) (dx > 0 ? 1 : dx < 0 ? -1 : 0);
			byte extraRows = (byte) (dy > 0 ? 1 : dy < 0 ? -1 : 0);
			
			// rightCol downRow used for correct col/row count on right/down sides
			byte rightCol = (byte) (signX - 1);
			byte downRow = (byte) (signY - 1);			
			
			byte addColCount = (byte) (x / Map.partMapWidth + extraCols + rightCol);
			byte startCol = (byte) (Map.getStartCol() - addColCount);
			int wCols = Math.abs(Map.partMapWidth * addColCount);			
			byte addRowCount = (byte) (y / Map.partMapHeight + extraRows + downRow); 
			byte startRow = (byte) (Map.getStartRow() - addRowCount);					
			int hRows = Math.abs(Map.partMapHeight * addRowCount);
			
			//not full left top corner
			byte leftTopCol = (byte) (startCol + extraCols - 1);
			byte topLeftRow = (byte) (startRow + extraRows - 1);
			
			/*if (AppConfig.isDEBUG()){
				System.out.println(impanel.offset);
	    		System.out.println("Map.getStart "+Map.getStartCol()+","+Map.getStartRow());
	    		System.out.println("Map.getMapoffset "+Map.getMapOffset()); 
	    		System.out.println("add "+addColCount+","+addRowCount); 	  	   
	    		System.out.println("start "+startCol+","+startRow);
	    		System.out.println("CellWidth=" +  leftPartCellWidth+ "," + rightPartCellWidth);
	    		System.out.println("cellHeight=" +  upPartCellHeight+ ", " + downPartCellHeight);	    		
	    		System.out.println("wn=" +  Map.getSize().width+ " hn" + Map.getSize().height);
	    		System.out.println("dx=" +  dx+ " dy=" + dy);	
	    		System.out.println("x=" +  x+ " y=" + y);
	    		System.out.println("wCols=" +  wCols+ " hRows=" + hRows);	
	    		System.out.println("signX=" +  signX+ " signY=" + signY);	
	    		System.out.println("signMoveX=" +  signMoveX+ " signMoveY=" + signMoveY);	
	    		System.out.println("extra=" +  extraCols+ "," + extraRows);
	    		System.out.println("rightCol=" +  rightCol+ " downRow=" + downRow);	
	    		System.out.println("COUNT" +  Map.COL_COUNT+ "," + Map.ROW_COUNT);
	    		System.out.println("lefttopCorner = " +  leftTopCol+ "," + topLeftRow);
			}*/
		//	Image pimage  = null;
			g.setColor(new Color(200,0,0,50));			
			//paint left side
			if (signMoveX > 0){		
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol), (byte) (topLeftRow + addRowCount*signMoveY), (byte) (Math.abs(addColCount)), (byte) (Map.ROW_COUNT), 
						leftPartCellWidth,upPartCellHeight+hRows*(signMoveY));				
			} 
			//paint right side
			else {										
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol + Map.COL_COUNT - (1  + addColCount)*rightCol), (byte) (topLeftRow  + addRowCount*signMoveY), (byte) (Math.abs(addColCount)), (byte) (Map.ROW_COUNT), 
						Map.getSize().width - wCols + rightPartCellWidth, upPartCellHeight + hRows * (signMoveY));
			}  
			//paint top side
			if (signMoveY > 0){					
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol), (byte) (topLeftRow),(byte) (Map.COL_COUNT + 1),  (byte) (Math.abs(addRowCount)), 
						leftPartCellWidth, upPartCellHeight);				
			}
			//paint down side
			else{		
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol), (byte) (topLeftRow + Map.ROW_COUNT - (1 + addRowCount)*downRow), (byte) (Map.COL_COUNT + 1), (byte) (Math.abs(addRowCount)), 
						leftPartCellWidth, Map.getSize().height - hRows + downPartCellHeight);										
			}
			//+1 becouse start col/row must be full cell
			Map.setStartCol((byte) (leftTopCol + 1));
			Map.setStartRow((byte) (topLeftRow + 1));
		/*	if (AppConfig.isDEBUG())
				AppConfig.lgTRACE.debug("Map.getStart after "+Map.getStartCol()+","+Map.getStartRow()); 	*/
			Map.setMapOffset(new Point(rightPartCellWidth,downPartCellHeight));
	
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
        slider.setDoubleBuffered(false);     
    }  
	
	
	private static void createFrame(){		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		f.getContentPane().add(new JScrollPane(impanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
													 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));	
		createSlider();   
		//f.getContentPane()
		f.getContentPane().add(slider, "East");		
		slider.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                int value = ((JSlider)e.getSource()).getValue();     
	                impanel.setScale(value/100.0);	                	               
	            }  
	        });         		 
		f.setSize(AppConfig.appWidth, AppConfig.appHeight); 
		f.setBackground(Color.gray);
		f.setResizable(false);
		f.setVisible(true);		
	}
		
	/**
	 * Create map from parts of map,
	 * Starts with scale, from (x, y) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI(byte scale, byte x, byte y) throws IOException{	
		impanel = new ImagePanel(createMap(scale, x, y));	
		createFrame();		
	}
	/**
	 * Create map from parts of map,
	 * Starts with scale=1, from (1, 1) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI() throws IOException{			
		impanel = new ImagePanel(createMap((byte)1,(byte)1,(byte)1));	
		createFrame();		
	}	
	
	/**
	 * Create map from the source,
	 * Starts with scale=1
	 * @throws IOException
	 */
	public static void createAndShowGUI(String source) throws IOException{		
		Map.setPngScale((byte) 1);
		impanel = new ImagePanel(source);	
		createFrame();		
	}

}
