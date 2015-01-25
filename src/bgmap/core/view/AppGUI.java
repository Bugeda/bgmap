package bgmap.core.view;


import static bgmap.core.model.Map.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bgmap.core.AppConfig;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;

public class AppGUI extends AppConfig{
	
	private static MafHashMap allMafs =  new MafHashMap(); 
	private static Maf clickedMaf;
	
	public static final byte MAX_scale = 127;
	public static final byte MIN_scale = 4;
	public static final byte START_scale = 100;	
	public static final JSlider slider = new JSlider(JSlider.VERTICAL, MIN_scale, MAX_scale, START_scale);;
	public static final JFrame mainFrame = new JFrame("bgmap");		
	public static MapPanel mapPanel = null;
	public static MafHashMap repaintMafs; 
	
	public static MafHashMap getAllMafs() {
		return allMafs;
	}
	
	public static Maf getClickedMaf() {
		return clickedMaf;
	}

	public static void setClickedMaf(Maf clickedMaf) {
		AppGUI.clickedMaf = clickedMaf;
	}
	
	/**
	 * create slidebar
	 */
	private static void createSlider() {                 
        slider.setMinorTickSpacing(12);  
        slider.setMajorTickSpacing(25);  
        slider.setPaintTicks(true);  
        slider.setPaintLabels(true);
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        slider.setDoubleBuffered(false);     
    }  
	
	/**
	 * create frame
	 */
	private static void createFrame(){				
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(appWidth, appHeight);
		mainFrame.setBackground(Color.gray);
		mainFrame.setResizable(false);
	  
		createSlider();
		
		mainFrame.add(slider, "East");			
	    mainFrame.add(new JScrollPane(mapPanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
				 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));	
	 	
		slider.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                int value = ((JSlider)e.getSource()).getValue();     
	                mapPanel.setScale(value/100.0);	                	               
	            }  
	        });         		 
		mainFrame.setVisible(true);	   	  
	}
		
	/**
	 * Create map from parts of map,
	 * Starts with scale, from (x, y) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI(byte scale, byte x, byte y){	
		mapPanel = new MapPanel(createMap(scale, x, y));	
		createFrame();		
	}
	/**
	 * Create map from parts of map,
	 * Starts with scale=1, from (1, 1) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI() {			
		mapPanel = new MapPanel(createMap((byte)1,(byte)1,(byte)1));	
		createFrame();		
	}	
	
	/**
	 * Create map from the source,
	 * Starts with scale=1
	 * @throws IOException
	 */
	public static void createAndShowGUI(String source) throws IOException{		
		setPngScale((byte) 1);
		mapPanel = new MapPanel(source);	
		createFrame();		
	}
	
	/**
	 * return url to png file with part of map
	 */
	public static String getPartMapUrl(byte y, byte x){
		return mapsPath+getPngScale() + "_" + y + "_" + x + ".png";
	}
	
	/**
	 * Load map from parts with size (column,row)
	 * with coordinates from (startColumn, startRow)
	 * and scale.
	 * 
	 */
	public static void loadPartsMap(byte scale, byte startColumn, byte startRow, byte columnCount, byte rowCount, int drawX, int drawY){
		byte dx;
		byte dy=startRow;   
		List<Thread> threads = new ArrayList<Thread>();
        for(byte y=0; y < rowCount; y++ ) {        
        	dx=startColumn;
        	for(byte x=0; x < columnCount; x ++ ) { 
        		Runnable r = new ThreadMapPart(dx, dy, x, y, drawX, drawY);
        		Thread t = new Thread(r);	            		
        		t.setPriority(Thread.MAX_PRIORITY);
        		threads.add(t);	            	            	
        		dx++;
            }
        	dy++;
        } 
      
        for (Thread thread: threads) {	            	
            thread.start();	                
        }
        for (Thread thread: threads) {
            try {
				thread.join();
			} catch (InterruptedException e) {
				AppConfig.lgTRACE.error(e);
	            AppConfig.lgWARN.error(e);
	            System.exit(1);	 
			}
        }
        if (ThreadMapPart.getMapPartCounts()==ThreadMapPart.getSumPartCount())
        	repaintMafs();	   				      	            
	}
	
	/**
	 * Create map with size (column,row)
	 * with coordinates from (startColumn, startRow)
	 * and scale.
	 * Save link to the image in class
	 * @return Image
	 */	
	protected static Image createMap(byte scale, byte startColumn, byte startRow) {			
		startColumn-=COL_COUNT;
		startRow-=ROW_COUNT;
		Image im = new BufferedImage(partMapWidth * COL_COUNT,partMapHeight * ROW_COUNT, BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));	  
	    try {
	    	allMafs = DBManager.selectMafs(startColumn, (byte) (startColumn+COL_COUNT), startRow, (byte)(startRow+ROW_COUNT));
		} catch (SQLException e) {
    		lgTRACE.error(e);
            lgWARN.error(e);
            System.exit(1);	 
		}			
	    Image image = null;		        	
		byte dx;
		byte dy=startRow;    		
        for(byte y=0; y < ROW_COUNT; y++ ) {        
        	dx=startColumn;
        	for(byte x=0; x < COL_COUNT; x ++ ) { 		            	
        		image = new ImageIcon(AppGUI.getPartMapUrl(dy,dx)).getImage();            		
                g.drawImage(image, x * partMapWidth, y * partMapHeight, null);
        		g.drawRect(x * partMapWidth, y * partMapHeight, partMapWidth,partMapHeight);	            	
        		g.drawString(dy+" "+dx, x* partMapWidth+150, y* partMapHeight+200);
        		image.flush();
        		dx++;
            }
        	dy++;
        }        
        //read all mafs and paint them 
        Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = getAllMafs().entrySet().iterator();
		while (it.hasNext()){
			  Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			  MafHashKey key = thisEntry.getKey();	
			  ArrayList<MafHashValue> value = thisEntry.getValue();
			  for (MafHashValue vl:value){							   	  
					  g.drawImage(MafsMarks[vl.getMafsMarks()], 
			    				partMapWidth * (key.getCol() - startColumn) + vl.getX() - MafsMarks[vl.getMafsMarks()].getWidth(null)/2,
			    				partMapHeight * (key.getRow() - startRow) + vl.getY() - MafsMarks[vl.getMafsMarks()].getHeight(null),
			    				MafsMarks[vl.getMafsMarks()].getWidth(null), MafsMarks[vl.getMafsMarks()].getHeight(null), null);								  				  
			  }
		}	            	         
		g.dispose();		
    	setImage(im); 
		setPngScale(scale);
        setStartCol(startColumn);
        setStartRow(startRow);	            
		return im; 
    }

	/**
	 * repaint map when drag&drop
	 */
	public static void repaintMap(){
		if ((mapPanel.getOffset().x!=0)||(mapPanel.getOffset().y!=0)){
			Image newImage = new BufferedImage(getSize().width,getSize().height, BufferedImage.TYPE_INT_RGB);			
			Image subImage = ((BufferedImage) getImage()).getSubimage(0, 0, getSize().width,getSize().height);
			Graphics g = newImage.getGraphics();
			g.drawImage(subImage, (int)(mapPanel.getOffset().x), (int)(mapPanel.getOffset().y), null);
			setImage(newImage);
			
			// absolute new coordinates for lefttop full cell 
			int x = mapPanel.getOffset().x + getMapOffset().x;	
			int y = mapPanel.getOffset().y + getMapOffset().y;
			
			//  signX, signY use when we need calculate absolute left or right side from result position
			//  signMoveX, signMoveY use when we need calculate just mouse move
			byte signX = (byte) (x > 0 ? 1 : 0);	
			byte signMoveX = (byte) (mapPanel.getOffset().x > 0 ? 1 : 0);	
			byte signY = (byte) (y > 0 ? 1 : 0);	
			byte signMoveY = (byte) (mapPanel.getOffset().y > 0 ? 1 : 0);	
			
			/* Calculate coordinates and parts of one cell at axes
			 * right/down
			 * dx = 0, ax = 0 => partMapWidth
			 * dx = -, ax = 0 => partMapWidth - abs(dx)
			 * dx = +. ax = 1 => dx
			 * left/up
			 * dx = 0, ax = 0 => 0
			 * dx = -, ax = 0 => - abs(dx)
			 * dx = +. ax = 1 => dx - partMapWidth
			 */								
			int dx = x % partMapWidth;		
			int rightPartCellWidth =(partMapWidth) * (1 - signX) + dx ;
			int leftPartCellWidth = rightPartCellWidth - partMapWidth;
			
			int dy =  y % partMapHeight;
			int downPartCellHeight = (partMapHeight) * (1 - signY ) + dy;
			int upPartCellHeight = downPartCellHeight - partMapHeight;
			
			//extra used for change picture when x/y < one cell
			byte extraCols = (byte) (dx > 0 ? 1 : dx < 0 ? -1 : 0);
			byte extraRows = (byte) (dy > 0 ? 1 : dy < 0 ? -1 : 0);
			
			// rightCol downRow used for correct col/row count on right/down sides
			byte rightCol = (byte) (signX - 1);
			byte downRow = (byte) (signY - 1);			
			
			byte addColCount = (byte) (x / partMapWidth + extraCols + rightCol);
			byte startCol = (byte) (getStartCol() - addColCount);
			int wCols = Math.abs(partMapWidth * addColCount);			
			byte addRowCount = (byte) (y / partMapHeight + extraRows + downRow); 
			byte startRow = (byte) (getStartRow() - addRowCount);					
			int hRows = Math.abs(partMapHeight * addRowCount);
			
			//not full left top corner
			byte leftTopCol = (byte) (startCol + extraCols - 1);
			byte topLeftRow = (byte) (startRow + extraRows - 1);
			
			if (isDEBUG()){
				lgDEBUG.debug(mapPanel.getOffset());
				lgDEBUG.debug("getStart "+getStartCol()+","+getStartRow());
				lgDEBUG.debug("getMapoffset "+getMapOffset()); 
				lgDEBUG.debug("add "+addColCount+","+addRowCount); 	  	   
				lgDEBUG.debug("start "+startCol+","+startRow);
				lgDEBUG.debug("CellWidth=" +  leftPartCellWidth+ "," + rightPartCellWidth);
				lgDEBUG.debug("cellHeight=" +  upPartCellHeight+ ", " + downPartCellHeight);	    		
				lgDEBUG.debug("wn=" +  getSize().width+ " hn" + getSize().height);
				lgDEBUG.debug("dx=" +  dx+ " dy=" + dy);	
				lgDEBUG.debug("x=" +  x+ " y=" + y);
				lgDEBUG.debug("wCols=" +  wCols+ " hRows=" + hRows);	
				lgDEBUG.debug("signX=" +  signX+ " signY=" + signY);	
				lgDEBUG.debug("signMoveX=" +  signMoveX+ " signMoveY=" + signMoveY);	
				lgDEBUG.debug("extra=" +  extraCols+ "," + extraRows);
				lgDEBUG.debug("rightCol=" +  rightCol+ " downRow=" + downRow);	
				lgDEBUG.debug("COUNT" +  COL_COUNT+ "," + ROW_COUNT);
				lgDEBUG.debug("lefttopCorner = " +  leftTopCol+ "," + topLeftRow);
			}
			//remove excess mafs from mafs HashMap
			Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = getAllMafs().entrySet().iterator();
			while (it.hasNext()){
					  Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
					  MafHashKey key = thisEntry.getKey();	
					  if ((key.getCol() < leftTopCol) || 
							  (key.getCol() > leftTopCol + COL_COUNT) || 
							  (key.getRow() < topLeftRow) || 
							  (key.getRow() > topLeftRow + ROW_COUNT))					  
						  it.remove();						
				}	     							
			//+1 becouse start col/row must be full cell
			setStartCol((byte) (leftTopCol + 1));
			setStartRow((byte) (topLeftRow + 1));
			setMapOffset(new Point(rightPartCellWidth,downPartCellHeight));
			ThreadMapPart.setMapPartCounts(Math.abs(addColCount) * ROW_COUNT + (COL_COUNT + 1) * Math.abs(addRowCount)) ;
			//paint left side			
			if (signMoveX > 0){		
				readHashMaf(
						leftTopCol, 
						(byte)(leftTopCol+Math.abs(addColCount) - 1), 
						(byte)(topLeftRow + addRowCount*signMoveY), 
						(byte)(topLeftRow + addRowCount*signMoveY+ROW_COUNT - 1));
				loadPartsMap(getPngScale(), 
						(byte) (leftTopCol), 
						(byte) (topLeftRow + addRowCount*signMoveY), 
						(byte) (Math.abs(addColCount)), 
						(byte) (ROW_COUNT), 
						leftPartCellWidth,upPartCellHeight+hRows*(signMoveY));
			} 
			//paint right side
			else {					
				readHashMaf(
						(byte)(leftTopCol + COL_COUNT - (1  + addColCount)*rightCol), 
						(byte)(leftTopCol + COL_COUNT - (1  + addColCount)*rightCol+Math.abs(addColCount) - 1), 
						(byte)(topLeftRow + addRowCount*signMoveY), 
						(byte)(topLeftRow + addRowCount*signMoveY+ROW_COUNT - 1));
				loadPartsMap(getPngScale(), 
						(byte) (leftTopCol + COL_COUNT - (1  + addColCount)*rightCol), 
						(byte) (topLeftRow + addRowCount*signMoveY), 
						(byte) (Math.abs(addColCount)), 
						(byte) (ROW_COUNT), 
						getSize().width - wCols + rightPartCellWidth, upPartCellHeight + hRows * (signMoveY));		
			}  
			//paint top side
			if (signMoveY > 0){		
				readHashMaf(
						(byte)(leftTopCol), 
						(byte)(leftTopCol + COL_COUNT ), 
						(byte)(topLeftRow), 
						(byte)(topLeftRow + Math.abs(addRowCount) - 1));
				loadPartsMap(getPngScale(), 
						(byte) (leftTopCol), 
						(byte) (topLeftRow),
						(byte) (COL_COUNT + 1),
						(byte) (Math.abs(addRowCount)), 
						leftPartCellWidth, upPartCellHeight);						
			}
			//paint down side
			else{	
				readHashMaf(
						(byte)(leftTopCol), 
						(byte)(leftTopCol + COL_COUNT), 
						(byte)(topLeftRow + ROW_COUNT - (1 + addRowCount)*downRow), 
						(byte)(topLeftRow + ROW_COUNT - (1 + addRowCount)*downRow + Math.abs(addRowCount) - 1));
				loadPartsMap(getPngScale(), 
						(byte) (leftTopCol), 
						(byte) (topLeftRow + ROW_COUNT - (1 + addRowCount)*downRow), 
						(byte) (COL_COUNT + 1), 
						(byte) (Math.abs(addRowCount)), 
						leftPartCellWidth, getSize().height - hRows + downPartCellHeight);				
			}			
		}
	}	

	/**
	 * Read mafs from db and load them into allMafs
	 */
	public static void readHashMaf(byte col1, byte col2, byte row1, byte row2){
		try {	
			MafHashMap addMafs = DBManager.selectMafs((byte)col1, (byte)col2, (byte)row1, (byte)row2);
			Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = addMafs.entrySet().iterator();
			while (it.hasNext()){
				Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			
				if (!getAllMafs().containsKey(thisEntry.getKey()))
					getAllMafs().put(thisEntry.getKey(), thisEntry.getValue());
			}       					
		} catch (SQLException e) {
    		AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	 
		}
	}
	/**
	 * paint repaintsMaf when map has been moved
	 */
	public static void repaintMafs(){	
		Graphics g = Map.getImage().getGraphics();
        Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = repaintMafs.entrySet().iterator();		
		while (it.hasNext()){			
			Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			MafHashKey key = thisEntry.getKey();
			ArrayList<MafHashValue> value = thisEntry.getValue();
			if (value != null)
				  for (MafHashValue vl:value)
					  g.drawImage(MafsMarks[vl.getMafsMarks()], 
			    				Map.getMapOffset().x + Map.partMapWidth * (key.getCol() - Map.getStartCol()) + vl.getX() - MafsMarks[vl.getMafsMarks()].getWidth(null)/2,
			    				Map.getMapOffset().y + Map.partMapHeight * (key.getRow() - Map.getStartRow()) + vl.getY() - MafsMarks[vl.getMafsMarks()].getHeight(null),
			    				MafsMarks[vl.getMafsMarks()].getWidth(null), MafsMarks[vl.getMafsMarks()].getHeight(null), null);
		}					
		AppGUI.mapPanel.loadImage(Map.getImage());
		g.dispose();		
		AppGUI.mapPanel.repaint();		
	}
	
	/**
	 * paint ClickedMaf
	 */		
	public static void paintClickedMaf(Image sign, boolean isNew){	

		Graphics g = getImage().getGraphics();  		
		g.drawImage(sign, 
				getMapOffset().x+partMapWidth*(clickedMaf.getColNum()-getStartCol())+clickedMaf.getX() - sign.getWidth(null)/2,
				getMapOffset().y+partMapHeight*(clickedMaf.getRowNum()-getStartRow())+clickedMaf.getY() - sign.getHeight(null),
				sign.getWidth(null), sign.getHeight(null), null);
		if (isNew){
			MafHashKey key = new MafHashKey(clickedMaf.getColNum(),clickedMaf.getRowNum());
			if (!getAllMafs().containsKey(key))
				getAllMafs().put(key, new ArrayList<MafHashValue>());
			ArrayList<MafHashValue> list = getAllMafs().get(key);			
			list.add(new MafHashValue((short)clickedMaf.getX(), (short)clickedMaf.getY(), clickedMaf.getMafMark()));
			getAllMafs().setMafValue(list);
			}
		AppGUI.mapPanel.loadImage(getImage());
		g.dispose();
		AppGUI.mapPanel.repaint();	
	}
	
}
