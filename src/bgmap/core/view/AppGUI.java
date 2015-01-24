package bgmap.core.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.*;

import org.eclipse.swt.internal.win32.PAINTSTRUCT;

import bgmap.core.AdminPanelStatus;
import bgmap.core.AppConfig;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;

public class AppGUI {		
	
	public static final byte MAX_scale = 127;
	public static final byte MIN_scale = 4;
	public static final byte START_scale = 100;	
	public static final JSlider slider = new JSlider(JSlider.VERTICAL, MIN_scale, MAX_scale, START_scale);;
	public static final JFrame mainFrame = new JFrame("bgmap");	
	protected static JPanel adminPanel = null;	
	public static MapPanel mapPanel = null;
	private static MafHashMap mafs =  new MafHashMap(); 
	private static Maf clickedMaf;
	private static byte startColumn, startRow;
	private static byte columnCount,rowCount;
	private static int drawX, drawY;

	public static MafHashMap getMafs() {
		return mafs;
	}

	public static void setMafs(MafHashMap mafs) {
		AppGUI.mafs = mafs;
	} 
	
	public static Maf getClickedMaf() {
		return clickedMaf;
	}

	public static void setClickedMaf(Maf clickedMaf) {
		AppGUI.clickedMaf = clickedMaf;
	}
	
	/**
	 * return url to png file with part of map
	 */
	public static String getPartMapUrl(byte y, byte x){
		return AppConfig.mapsPath+Map.getPngScale() + "_" + y + "_" + x + ".png";
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
	    		Thread[] threads = null;
	    		int tNum = 0;
	            for(byte y=0; y < rowCount; y++ ) {        
	            	dx=startColumn;
	            	for(byte x=0; x < columnCount; x ++ ) { 
	            		Runnable r = new ThreadMapPart(dx, dy, x, y, drawX, drawY);
	            		Thread t = new Thread(r);	            		
	            		t.setPriority(Thread.MAX_PRIORITY);
	            	//	threads[tNum] = t;
	            		t.start();
	            		//tNum++;
	            		dx++;
	                }
	            	dy++;
	            } 
	          /*  for ( Thread t : threads )
					try {
						t.join();
					} catch (InterruptedException e) {
						AppConfig.lgTRACE.error(e);
			            AppConfig.lgWARN.error(e);
			            System.exit(0);	 
					}
	    		repaintMafs(scale, startColumn, startRow, columnCount, rowCount, drawX, drawY);
	    		*/  
	            
	}
	
	/**
	 * Create map with size (column,row)
	 * with coordinates from (startColumn, startRow)
	 * and scale.
	 * Save link to the image in Map.class
	 * @return Image
	 */	
	protected static Image createMap(byte scale, byte startColumn, byte startRow) {			
		startColumn-=Map.COL_COUNT;
		startRow-=Map.ROW_COUNT;
		Image im = new BufferedImage(Map.partMapWidth * Map.COL_COUNT,Map.partMapHeight * Map.ROW_COUNT, BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		/*g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));	*/  
	    try {
			setMafs(DBManager.selectMafs(startColumn, (byte) (startColumn+Map.COL_COUNT), startRow, (byte)(startRow+Map.ROW_COUNT)));
		} catch (SQLException e) {
    		AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	 
		}			
	    Image image = null;		        	
		byte dx;
		byte dy=startRow;    		
        for(byte y=0; y < Map.ROW_COUNT; y++ ) {        
        	dx=startColumn;
        	for(byte x=0; x < Map.COL_COUNT; x ++ ) { 		            	
        		image = new ImageIcon(AppGUI.getPartMapUrl(dy,dx)).getImage();            		
                g.drawImage(image, x * Map.partMapWidth, y * Map.partMapHeight, null);
        		/*g.drawRect(x * Map.partMapWidth, y * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);	            	
        		g.drawString(dy+" "+dx, x* Map.partMapWidth+150, y* Map.partMapHeight+200);*/
        		image.flush();
        		dx++;
            }
        	dy++;
        }        
        //read all mafs and paint them 
        Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = getMafs().entrySet().iterator();
		while (it.hasNext()){
			  Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			  MafHashKey key = thisEntry.getKey();	
			  ArrayList<MafHashValue> value = thisEntry.getValue();
			  for (MafHashValue vl:value){							   	  
					  g.drawImage(vl.getMafsMarks().img, 
			    				Map.partMapWidth * (key.getCol() - startColumn) + vl.getX() - vl.getMafsMarks().img.getWidth(null)/2,
			    				Map.partMapHeight * (key.getRow() - startRow) + vl.getY() - vl.getMafsMarks().img.getHeight(null),
			    				vl.getMafsMarks().img.getWidth(null), vl.getMafsMarks().img.getHeight(null), null);								  				  
			  }
		}	            	         
		g.dispose();		
    	Map.setImage(im); 
		Map.setPngScale(scale);
        Map.setStartCol(startColumn);
        Map.setStartRow(startRow);	            
		return im; 
    }
	/**
	 * Read mafs from db and load them into MafHashMap
	 */
	public static void readHashMaf(byte col1, byte col2, byte row1, byte row2){
		try {	
			MafHashMap addMafs = DBManager.selectMafs((byte)col1, (byte)col2, (byte)row1, (byte)row2);
			Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = addMafs.entrySet().iterator();
			while (it.hasNext()){
				Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			
				if (!getMafs().containsKey(thisEntry.getKey()))
					getMafs().put(thisEntry.getKey(), thisEntry.getValue());
			}       					
		} catch (SQLException e) {
    		AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	 
		}
	}
	/**
	 * repaint map when drag&drop
	 */
	public static void repaintMap(){
		if ((mapPanel.getOffset().x!=0)||(mapPanel.getOffset().y!=0)){
			Image newImage = new BufferedImage(Map.getSize().width,Map.getSize().height, BufferedImage.TYPE_INT_RGB);			
			Image subImage = ((BufferedImage) Map.getImage()).getSubimage(0, 0, Map.getSize().width,Map.getSize().height);
			Graphics g = newImage.getGraphics();
			g.drawImage(subImage, (int)(mapPanel.getOffset().x), (int)(mapPanel.getOffset().y), null);
			Map.setImage(newImage);
			
			// absolute new coordinates for lefttop full cell 
			int x = mapPanel.getOffset().x + Map.getMapOffset().x;	
			int y = mapPanel.getOffset().y + Map.getMapOffset().y;
			
			//  signX, signY use when we need calculate absolute left or right side from result position
			//  signMoveX, signMoveY use when we need calculate just mouse move
			byte signX = (byte) (x > 0 ? 1 : 0);	
			byte signMoveX = (byte) (mapPanel.getOffset().x > 0 ? 1 : 0);	
			byte signY = (byte) (y > 0 ? 1 : 0);	
			byte signMoveY = (byte) (mapPanel.getOffset().y > 0 ? 1 : 0);	
			
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
			
			if (AppConfig.isDEBUG()){
				AppConfig.lgDEBUG.debug(mapPanel.getOffset());
				AppConfig.lgDEBUG.debug("Map.getStart "+Map.getStartCol()+","+Map.getStartRow());
				AppConfig.lgDEBUG.debug("Map.getMapoffset "+Map.getMapOffset()); 
				AppConfig.lgDEBUG.debug("add "+addColCount+","+addRowCount); 	  	   
				AppConfig.lgDEBUG.debug("start "+startCol+","+startRow);
				AppConfig.lgDEBUG.debug("CellWidth=" +  leftPartCellWidth+ "," + rightPartCellWidth);
				AppConfig.lgDEBUG.debug("cellHeight=" +  upPartCellHeight+ ", " + downPartCellHeight);	    		
				AppConfig.lgDEBUG.debug("wn=" +  Map.getSize().width+ " hn" + Map.getSize().height);
				AppConfig.lgDEBUG.debug("dx=" +  dx+ " dy=" + dy);	
				AppConfig.lgDEBUG.debug("x=" +  x+ " y=" + y);
				AppConfig.lgDEBUG.debug("wCols=" +  wCols+ " hRows=" + hRows);	
				AppConfig.lgDEBUG.debug("signX=" +  signX+ " signY=" + signY);	
				AppConfig.lgDEBUG.debug("signMoveX=" +  signMoveX+ " signMoveY=" + signMoveY);	
				AppConfig.lgDEBUG.debug("extra=" +  extraCols+ "," + extraRows);
				AppConfig.lgDEBUG.debug("rightCol=" +  rightCol+ " downRow=" + downRow);	
				AppConfig.lgDEBUG.debug("COUNT" +  Map.COL_COUNT+ "," + Map.ROW_COUNT);
				AppConfig.lgDEBUG.debug("lefttopCorner = " +  leftTopCol+ "," + topLeftRow);
			}
			ThreadMapPart.setMapPartCounts(Math.abs(addColCount)*Map.ROW_COUNT + (Map.COL_COUNT + 1)*Math.abs(addRowCount)) ;
	
			//paint left side			
			if (signMoveX > 0){		
				readHashMaf(
						leftTopCol, 
						(byte)(leftTopCol+Math.abs(addColCount) - 1), 
						(byte)(topLeftRow + addRowCount*signMoveY), 
						(byte)(topLeftRow + addRowCount*signMoveY+Map.ROW_COUNT - 1));
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol), 
						(byte) (topLeftRow + addRowCount*signMoveY), 
						(byte) (Math.abs(addColCount)), 
						(byte) (Map.ROW_COUNT), 
						leftPartCellWidth,upPartCellHeight+hRows*(signMoveY));
			} 
			//paint right side
			else {					
				readHashMaf(
						(byte)(leftTopCol + Map.COL_COUNT - (1  + addColCount)*rightCol), 
						(byte)(leftTopCol + Map.COL_COUNT - (1  + addColCount)*rightCol+Math.abs(addColCount) - 1), 
						(byte)(topLeftRow + addRowCount*signMoveY), 
						(byte)(topLeftRow + addRowCount*signMoveY+Map.ROW_COUNT - 1));
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol + Map.COL_COUNT - (1  + addColCount)*rightCol), 
						(byte) (topLeftRow + addRowCount*signMoveY), 
						(byte) (Math.abs(addColCount)), 
						(byte) (Map.ROW_COUNT), 
						Map.getSize().width - wCols + rightPartCellWidth, upPartCellHeight + hRows * (signMoveY));		
			}  
			//paint top side
			if (signMoveY > 0){		
				readHashMaf(
						(byte)(leftTopCol), 
						(byte)(leftTopCol + Map.COL_COUNT ), 
						(byte)(topLeftRow), 
						(byte)(topLeftRow + Math.abs(addRowCount) - 1));
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol), 
						(byte) (topLeftRow),
						(byte) (Map.COL_COUNT + 1),
						(byte) (Math.abs(addRowCount)), 
						leftPartCellWidth, upPartCellHeight);						
			}
			//paint down side
			else{	
				readHashMaf(
						(byte)(leftTopCol), 
						(byte)(leftTopCol + Map.COL_COUNT), 
						(byte)(topLeftRow + Map.ROW_COUNT - (1 + addRowCount)*downRow), 
						(byte)(topLeftRow + Map.ROW_COUNT - (1 + addRowCount)*downRow + Math.abs(addRowCount) - 1));
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol), 
						(byte) (topLeftRow + Map.ROW_COUNT - (1 + addRowCount)*downRow), 
						(byte) (Map.COL_COUNT + 1), 
						(byte) (Math.abs(addRowCount)), 
						leftPartCellWidth, Map.getSize().height - hRows + downPartCellHeight);				
			}			
			
			//remove excess mafs from mafs HashMap
			Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = getMafs().entrySet().iterator();
			while (it.hasNext()){
					  Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
					  MafHashKey key = thisEntry.getKey();	
					  if ((key.getCol() < leftTopCol) || 
							  (key.getCol() > leftTopCol + Map.COL_COUNT) || 
							  (key.getRow() < topLeftRow) || 
							  (key.getRow() > topLeftRow + Map.ROW_COUNT))					  
						  it.remove();						
				}	     							
			
			//+1 becouse start col/row must be full cell
			Map.setStartCol((byte) (leftTopCol + 1));
			Map.setStartRow((byte) (topLeftRow + 1));
			Map.setMapOffset(new Point(rightPartCellWidth,downPartCellHeight));
		}

	}	
	/**
	 * paint all HashMap mafs when map has been repaint
	 */
	public static void repaintMafs(byte scale, byte startColumn, byte startRow, byte columnCount, byte rowCount, int drawX, int drawY){		
	
	}
	
	/**
	 * paint ClickedMaf
	 */		
	public static void paintClickedMaf(Image sign, boolean isNew){	

		Graphics g = Map.getImage().getGraphics();  		
		g.drawImage(sign, 
				Map.getMapOffset().x+Map.partMapWidth*(clickedMaf.getColNum()-Map.getStartCol())+clickedMaf.getX() - sign.getWidth(null)/2,
				Map.getMapOffset().y+Map.partMapHeight*(clickedMaf.getRowNum()-Map.getStartRow())+clickedMaf.getY() - sign.getHeight(null),
				sign.getWidth(null), sign.getHeight(null), null);
		if (isNew){
			MafHashKey key = new MafHashKey(clickedMaf.getColNum(),clickedMaf.getRowNum());
			if (!getMafs().containsKey(key))
				getMafs().put(key, new ArrayList<MafHashValue>());
			ArrayList<MafHashValue> list = getMafs().get(key);
			list.add(new MafHashValue((short)clickedMaf.getX(), (short)clickedMaf.getY(), clickedMaf.getMafMark()));
			getMafs().setMafValue(list);
			}
		AppGUI.mapPanel.loadImage(Map.getImage());
		g.dispose();
		AppGUI.mapPanel.repaint();	
	}
	/**
	 * create slidebar
	 */
	public static void createSlider() {                 
        slider.setMinorTickSpacing(12);  
        slider.setMajorTickSpacing(25);  
        slider.setPaintTicks(true);  
        slider.setPaintLabels(true);
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        slider.setDoubleBuffered(false);     
    }  
	/**
	 * create adminpanel
	 */
	public static void creadeAdminPanel() {          
		adminPanel = new JPanel();  		
		JCheckBox addMAFButton = new JCheckBox("Режим редактирования");		
		addMAFButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				switch (e.getStateChange()) {
				case ItemEvent.SELECTED:AdminPanelStatus.setEditMaf(true);break;
				case ItemEvent.DESELECTED:AdminPanelStatus.setEditMaf(false);break;
			}
			}
		});
		adminPanel.add(addMAFButton,BorderLayout.CENTER);
    }
}
