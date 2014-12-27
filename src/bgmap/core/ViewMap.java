package bgmap.core;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import bgmap.core.entity.*;

public class ViewMap {		
	
	static final byte MAX_scale = 127;
	static final byte MIN_scale = 4;
	static final byte START_scale = 100;	
	static final byte CAPTION_HEIGHT=25;
	public static ImagePanel impanel;
	public static JSlider slider;

    MapMouseAdapter movingAdapt = new MapMouseAdapter();
	
	/**
	 * @param y
	 * @param x
	 * @return url to png file with part of map
	 */
	private static String getPartMapUrl(byte y, byte x){
		return AppConfig.mapsPath+Map.getPngScale() + "_" + y + "_" + x + ".png";
	}
	/**
	 * Load map from parts with size (column,row)
	 * with coordinates from (startColumn, startRow)
	 * and scale.
	 * @return Image
	 */
	private static Image loadPartsMap(byte scale, byte startColumn, byte startRow, byte columnCount, byte rowCount){
		Image im = new BufferedImage(Map.partMapWidth * columnCount,Map.partMapHeight * rowCount, BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.setColor(Color.BLACK);

		try {        				 
	        	Image image = null;		        	
	    		byte dx;
	    		byte dy=startRow;    		
	            for(byte y=0; y < rowCount; y++ ) {        
	            	dx=startColumn;
	            	for(byte x=0; x < columnCount; x ++ ) { 	            		           		
	            		image = ImageIO.read(new File(getPartMapUrl(dy,dx)));	            		
	            		//image = new ImageIcon(getPartMapUrl(dy,dx)).getImage();
	            		//FileInputStream fis = new FileInputStream(new File(getPartMapUrl(dy,dx)));
	            		//image = ImageIO.read(fis);
	            	   // image = Toolkit.getDefaultToolkit().getImage(getPartMapUrl(dy,dx));
	                    g.drawImage(image, x * Map.partMapWidth, y * Map.partMapHeight, null);
	            		g.drawRect(x * Map.partMapWidth, y * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);
	            		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 25));
	            		g.drawString(dy+" "+dx, x* Map.partMapWidth+150, y* Map.partMapHeight+200);
	            		image.flush();
	            		dx++;
	                }
	            	dy++;
	            }        	        
	            g.dispose();
	        } catch (Exception e) {
	    		AppConfig.lgTRACE.error(e);
	            AppConfig.lgWARN.error(e);
	            System.exit(1);	 
	        }        
		 return im;
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
	    Image im = loadPartsMap(scale,startColumn,startRow,Map.COL_COUNT,Map.ROW_COUNT);
    	Map.setImage(im);     
		Map.setPngScale(scale);
        Map.setStartCol(startColumn);
        Map.setStartRow(startRow);                
        return im;
    }
	/**Redraw map and scroll it 
	 * @throws IOException 
	 * 
	 */
	static void addPartsMap(Point point){
		if ((impanel.offset.x!=0)||(impanel.offset.y!=0)){
			Image newImage = new BufferedImage(Map.getSize().width,Map.getSize().height, BufferedImage.TYPE_INT_RGB);
			Image subImage = ((BufferedImage) Map.getImage()).getSubimage(0, 0, Map.getSize().width,Map.getSize().height);
			newImage.getGraphics().drawImage(subImage, (int)(impanel.offset.x), (int)(impanel.offset.y), null);
			/* right/down
			 * dx = 0, ax = 0 => Map.partMapWidth
			 * dx = -, ax = 0 => Map.partMapWidth - abs(dx)
			 * dx = +. ax = -1 => dx
			 * left/up
			 * dx = 0, ax = 0 => 0
			 * dx = -, ax = 0 => - abs(dx)
			 * dx = +. ax = -1 => dx - Map.partMapWidth 
			 */			

			int x = impanel.offset.x + Map.getMapOffset().x;
			byte xAxis = (byte) (x > 0 ? -1 : 0);			
			int dx = x % Map.partMapWidth;		
			int rightPartCellWidth = (Map.partMapWidth)*(xAxis + 1) + dx;
			int leftPartCellWidth = rightPartCellWidth - Map.partMapWidth;			
			byte extraCols = (byte) (dx > 0 ? 1 : 0);
			byte addColCount = (byte) (x/Map.partMapWidth + extraCols);
			byte startCol = (byte) (Map.getStartCol() - addColCount);
			int wCols = Math.abs(Map.partMapWidth * addColCount);	
			
			int y = impanel.offset.y + Map.getMapOffset().y;	
			byte yAxis = (byte) (y > 0 ? -1 : 0);	
			int dy = y % Map.partMapHeight;
			int downPartCellHeight = (Map.partMapHeight) * (yAxis + 1) + dy;
			int upPartCellHeight = downPartCellHeight - Map.partMapHeight;
			byte extraRows = (byte) (dy > 0 ? 1 : 0);
			byte addRowCount = (byte) (y/Map.partMapHeight + extraRows); 
			byte startRow = (byte) (Map.getStartRow() - addRowCount);					
			int hRows = Math.abs(Map.partMapHeight * addRowCount);
			if (AppConfig.isDEBUG()){
				System.out.println(impanel.offset);
	    		System.out.println("Map.getStart "+Map.getStartCol()+","+Map.getStartRow());
	    		System.out.println("Map.getMapoffset "+Map.getMapOffset()); 
	    		System.out.println("add "+addColCount+","+addRowCount); 	  	   
	    		System.out.println("start "+startCol+","+startRow);
	    		System.out.println("CellWidth=" +  leftPartCellWidth+ "," + rightPartCellWidth);
	    		System.out.println("cellHeight=" +  upPartCellHeight+ ", " + downPartCellHeight);	    		
	    		System.out.println("wn=" +  Map.getSize().width+ " hn" + Map.getSize().height);
	    		System.out.println("dx=" +  dx+ " dy=" + dy);	
	    		System.out.println("wCols=" +  wCols+ " hRows=" + hRows);	
	    		System.out.println("xAxis=" +  xAxis+ " yAxis=" + yAxis);	
	    		System.out.println("extra=" +  extraCols+ "," + extraRows);
	    		System.out.println("COUNT" +  Map.COL_COUNT+ "," + Map.ROW_COUNT);
			}
			
			//paint left side
			if (xAxis == -1){		
				Image pimage = loadPartsMap(Map.getPngScale(), (byte) (startCol), (byte) (startRow-addRowCount*yAxis), (byte) (Math.abs(addColCount)+1), (byte) (Map.ROW_COUNT-Math.abs(addRowCount)+1));				
				newImage.getGraphics().drawImage(pimage, leftPartCellWidth, upPartCellHeight-(hRows)*(yAxis), null);
				pimage.flush();	
			} 
			//paint right side
			if (xAxis == 0){		
				Image pimage = loadPartsMap(Map.getPngScale(), (byte) (startCol+Map.COL_COUNT-Math.abs(addColCount)-1), (byte) (startRow-addRowCount*yAxis), (byte) (Math.abs(addColCount)+2), (byte) (Map.ROW_COUNT-Math.abs(addRowCount)+2));
				newImage.getGraphics().drawImage(pimage,Map.getSize().width-wCols-Map.partMapWidth + leftPartCellWidth, upPartCellHeight-hRows*(yAxis), null);
				pimage.flush();											
			}  
			//paint top side
			if (yAxis == -1){		
				Image pimage = loadPartsMap(Map.getPngScale(), (byte) (startCol), (byte) (startRow),(byte) (Map.COL_COUNT+1),  (byte) (Math.abs(addRowCount)+1) );				
				newImage.getGraphics().drawImage(pimage, leftPartCellWidth, upPartCellHeight, null);
				pimage.flush();	
			} 
			//paint down side
			if (yAxis==0){		
				Image pimage = loadPartsMap(Map.getPngScale(), (byte) (startCol), (byte) (startRow+Map.ROW_COUNT-Math.abs(addRowCount)-1), (byte) (Map.COL_COUNT+2), (byte) (Math.abs(addRowCount)+3));
				System.out.println(startRow+Map.ROW_COUNT-Math.abs(addRowCount)-1);
				newImage.getGraphics().drawImage(pimage, leftPartCellWidth, Map.getSize().height - hRows - Map.partMapHeight + upPartCellHeight, null);
				pimage.flush();											
			}
			//+1 becouse start col row must be full cell
			Map.setStartCol((byte) (startCol + 1));
			Map.setStartRow((byte) (startRow + 1));
			AppConfig.lgTRACE.debug("Map.getStart after "+Map.getStartCol()+","+Map.getStartRow()); 	
			Map.setMapOffset(new Point(rightPartCellWidth,downPartCellHeight));
			Map.setImage(newImage);		
			impanel.loadImage(newImage);
			impanel.repaint();
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
        //slider.setLabelTable(null);         
    }  
	
	
	private static void createFrame(){		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    	//impanel.setMoveFrom(new Point(0,0));
		//impanel.setMoveTo(new Point((-Math.abs(Map.getImage().getWidth(null))/2),-Math.abs((AppConfig.appHeight-Map.getImage().getHeight(null)+30)/2)));
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
		f.setSize(AppConfig.appWidth, AppConfig.appHeight); 
		f.setResizable(false);
		//setLocation(1,1);          
		f.setVisible(true);		
	}
		
	/**
	 * Create map from parts of map,
	 * Starts with scale, from (x, y) map parts
	 * @throws IOException
	 */
	public static void Run(byte scale, byte x, byte y) throws IOException{	
		impanel = new ImagePanel(createMap(scale, x, y));	
		//impanel = new ImagePanel(createMap(scale, x, y, (byte)20,(byte) 20));
		createFrame();		
	}
	/**
	 * Create map from parts of map,
	 * Starts with scale=1, from (1, 1) map parts
	 * @throws IOException
	 */
	public static void Run() throws IOException{				
		impanel = new ImagePanel(createMap((byte)1,(byte)1,(byte)1));		
		//impanel = new ImagePanel(createMap((byte)1,(byte)1,(byte)1, (byte)50,(byte)50));
		//impanel = new ImagePanel(createMap(1,1,8,3));
		createFrame();		
	}	
	
	/**
	 * Create map from the source,
	 * Starts with scale=1
	 * @throws IOException
	 */
	public static void Run(String source) throws IOException{		
		Map.setPngScale((byte) 1);
		impanel = new ImagePanel(source);	
		createFrame();		
	}

}
