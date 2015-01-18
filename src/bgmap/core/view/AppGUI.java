package bgmap.core.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.event.*;

import bgmap.core.AdminPanelStatus;
import bgmap.core.AppConfig;
import bgmap.core.controller.MapMouseAdapter;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;

public class AppGUI {		
	public static final byte MAX_scale = 127;
	public static final byte MIN_scale = 4;
	public static final byte START_scale = 100;	
	public static final byte CAPTION_HEIGHT=25;
	public static MapPanel mapPanel = null;
	public final static JSlider slider = new JSlider(JSlider.VERTICAL, MIN_scale, MAX_scale, START_scale);;
	public final static JFrame mainFrame = new JFrame("bgmap");	
	protected static JPanel adminPanel = null;	
	public static MafHashMap mafs =  new MafHashMap(); 
	
		
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
	private static void loadPartsMap(byte scale, byte startColumn, byte startRow, byte columnCount, byte rowCount, int drawX, int drawY){
				byte dx;
	    		byte dy=startRow;    		
	            for(byte y=0; y < rowCount; y++ ) {        
	            	dx=startColumn;
	            	for(byte x=0; x < columnCount; x ++ ) { 
	            		Runnable r = new ThreadMapPart(dx, dy, x, y, drawX, drawY);
	            		Thread t = new Thread(r);
	            		t.setPriority(Thread.MAX_PRIORITY);
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
	protected static Image createMap(byte scale, byte startColumn, byte startRow) {			
		startColumn-=Map.COL_COUNT;
		startRow-=Map.ROW_COUNT;
		Image im = new BufferedImage(Map.partMapWidth * Map.COL_COUNT,Map.partMapHeight * Map.ROW_COUNT, BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));	  
	    try {
			mafs = DBManager.selectMafs(startColumn, (byte) (startColumn+Map.COL_COUNT), startRow, (byte)(startRow+Map.ROW_COUNT));
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
        		g.drawRect(x * Map.partMapWidth, y * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);	            	
        		g.drawString(dy+" "+dx, x* Map.partMapWidth+150, y* Map.partMapHeight+200);
        		image.flush();
        		dx++;
            }
        	dy++;
        }        
        //read all mafs and paint them 
        Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = mafs.entrySet().iterator();
		while (it.hasNext()){
			  Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			  MafHashKey key = thisEntry.getKey();	
			  ArrayList<MafHashValue> value = thisEntry.getValue();
			  for (MafHashValue vl:value)
				  g.drawImage(AppConfig.signFull, 
	    				Map.partMapWidth*(key.getCol()-startColumn)+vl.getX() - AppConfig.signFull.getWidth(null)/2,
	    				Map.partMapHeight*(key.getRow()-startRow)+vl.getY() - AppConfig.signFull.getHeight(null),
	    				AppConfig.signFull.getWidth(null), AppConfig.signFull.getHeight(null), null);
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
	 * @return 
	 */
	public static void readHashMaf(byte col1, byte col2, byte row1, byte row2){
		try {	
			System.out.println("read");
			MafHashMap addMafs = DBManager.selectMafs((byte)col1, (byte)col2, (byte)row1, (byte)row2);
			System.out.println("sizedb= "+addMafs.size());
			Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = addMafs.entrySet().iterator();
			while (it.hasNext()){
				Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			
				if (!mafs.containsKey(thisEntry.getKey()))
					mafs.put(thisEntry.getKey(), thisEntry.getValue());
				/*else{					
					if (mafs.containsValue(thisEntry.getValue())){
						System.out.println(thisEntry.getValue().toArray().toString()+" exist");
					
					ArrayList<MafHashValue> oldValue = mafs.get(thisEntry.getKey());
					for (MafHashValue value:thisEntry.getValue()){
						if (!oldValue.contains(value)){
							oldValue.add(value);
						}
					}
				}*/
			}       					
	/*	System.out.println("sizemafs= "+mafs.size());	
		it = mafs.entrySet().iterator();
		while (it.hasNext()){
			System.out.println("hasnext");
			Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
			 MafHashKey key = thisEntry.getKey();
			  System.out.println(key.getCol()+","+key.getRow());	
			  ArrayList<MafHashValue> value = thisEntry.getValue();
			  for (MafHashValue vl:value)
				  System.out.println(vl.getX()+","+vl.getY());
			}*/
		
		
		} catch (SQLException e) {
    		AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	 
		}
	}
	
	public static void repaintMap(){
		if ((mapPanel.offset.x!=0)||(mapPanel.offset.y!=0)){
			Image newImage = new BufferedImage(Map.getSize().width,Map.getSize().height, BufferedImage.TYPE_INT_RGB);			
			Image subImage = ((BufferedImage) Map.getImage()).getSubimage(0, 0, Map.getSize().width,Map.getSize().height);
			Graphics g = newImage.getGraphics();
			g.drawImage(subImage, (int)(mapPanel.offset.x), (int)(mapPanel.offset.y), null);
			Map.setImage(newImage);
			
			// absolute new coordinates for lefttop full cell 
			int x = mapPanel.offset.x + Map.getMapOffset().x;	
			int y = mapPanel.offset.y + Map.getMapOffset().y;
			
			//  signX, signY use when we need calculate absolute left or right side from result position
			//  signMoveX, signMoveY use when we need calculate just mouse move
			byte signX = (byte) (x > 0 ? 1 : 0);	
			byte signMoveX = (byte) (mapPanel.offset.x > 0 ? 1 : 0);	
			byte signY = (byte) (y > 0 ? 1 : 0);	
			byte signMoveY = (byte) (mapPanel.offset.y > 0 ? 1 : 0);	
			
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
				System.out.println(mapPanel.offset);
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
			System.out.println(mafs.size());

			g.setColor(new Color(200,0,0,50));			
			//paint left side
			
			if (signMoveX > 0){		
				readHashMaf(
						leftTopCol, 
						(byte)(leftTopCol+Math.abs(addColCount) - 1), 
						(byte)(topLeftRow + addRowCount*signMoveY), 
						(byte)(topLeftRow + addRowCount*signMoveY+Map.ROW_COUNT - 1));
				loadPartsMap(Map.getPngScale(), 
						(byte) (leftTopCol), (byte) (topLeftRow + addRowCount*signMoveY), (byte) (Math.abs(addColCount)), (byte) (Map.ROW_COUNT), 
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
						(byte) (leftTopCol + Map.COL_COUNT - (1  + addColCount)*rightCol), (byte) (topLeftRow + addRowCount*signMoveY), (byte) (Math.abs(addColCount)), (byte) (Map.ROW_COUNT), 
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
						(byte) (leftTopCol), (byte) (topLeftRow),(byte) (Map.COL_COUNT + 1),  (byte) (Math.abs(addRowCount)), 
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
						(byte) (leftTopCol), (byte) (topLeftRow + Map.ROW_COUNT - (1 + addRowCount)*downRow), (byte) (Map.COL_COUNT + 1), (byte) (Math.abs(addRowCount)), 
						leftPartCellWidth, Map.getSize().height - hRows + downPartCellHeight);		
			}			
	
			System.out.println(mafs.size());	
			
			Iterator<Entry<MafHashKey, ArrayList<MafHashValue>>> it = mafs.entrySet().iterator();
			while (it.hasNext()){
					  Entry<MafHashKey, ArrayList<MafHashValue>> thisEntry = it.next();
					  MafHashKey key = thisEntry.getKey();	
					  if ((key.getCol() < leftTopCol) || 
							  (key.getCol() > leftTopCol + Map.COL_COUNT) || 
							  (key.getRow() < topLeftRow) || 
							  (key.getRow() > topLeftRow + Map.ROW_COUNT))					  
						  it.remove();						
				}	     	
			System.out.println(mafs.size());					
			
			//+1 becouse start col/row must be full cell
			Map.setStartCol((byte) (leftTopCol + 1));
			Map.setStartRow((byte) (topLeftRow + 1));
		/*	if (AppConfig.isDEBUG())
				AppConfig.lgTRACE.debug("Map.getStart after "+Map.getStartCol()+","+Map.getStartRow()); 	*/
			Map.setMapOffset(new Point(rightPartCellWidth,downPartCellHeight));
		}

	}	
	

	/**
	 * paint maf
	 */
	
	public static void paintMaf(Image sign, boolean isNew, Maf maf){	
		Graphics g = Map.getImage().getGraphics();  		
		g.setColor(Color.red);
		g.drawImage(sign, 
				Map.getMapOffset().x+Map.partMapWidth*(maf.getColNum()-Map.getStartCol())+maf.getX() - sign.getWidth(null)/2,
				Map.getMapOffset().y+Map.partMapHeight*(maf.getRowNum()-Map.getStartRow())+maf.getY() - sign.getHeight(null),
				sign.getWidth(null), sign.getHeight(null), null);
		if (isNew){
		MafHashKey key = new MafHashKey(maf.getColNum(),maf.getRowNum());
		if (!mafs.containsKey(key))
			mafs.put(key, new ArrayList<MafHashValue>());
		ArrayList<MafHashValue> list = mafs.get(key);
		list.add(new MafHashValue((short)maf.getX(), (short)maf.getY()));
		mafs.setMafValue(list);
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
				System.out.println(e.getStateChange());
				switch (e.getStateChange()) {
				case ItemEvent.SELECTED:AdminPanelStatus.setEditMaf(true);break;
				case ItemEvent.DESELECTED:AdminPanelStatus.setEditMaf(false);break;
			}
			}
		});
		adminPanel.add(addMAFButton,BorderLayout.CENTER);
    }  
}
