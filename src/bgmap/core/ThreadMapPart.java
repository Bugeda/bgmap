package bgmap.core;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import bgmap.AppConfig;
import bgmap.core.model.*;

public class ThreadMapPart implements Runnable {
	
	private static int mapPartCounts;
	private static int sumPartCount;	 
	
	private byte dx, dy;	 
	private byte columnCount, rowCount;	 
	private int drawX, drawY;	 
	 
	public static int getMapPartCounts() {
		return mapPartCounts;
	}
	public static int getSumPartCount() {
		return sumPartCount;
	}
	/**
	 * set sumPartCount = 0 to calculate how much parts has been loaded
	 * clear repaintsMaf to keep loaded cells
 	 * @param mapPartCounts - keep how much parts must be loaded
	 */
	public static void setMapPartCounts(int mapPartCounts) {
		ThreadMapPart.mapPartCounts = mapPartCounts;
		ThreadMapPart.sumPartCount = 0;
		AppGUI.repaintMafs = new MafHashMap();	
	}		


	
	/**
	 * create ThreadMapPart to load and paint cell
	 * @param dx - coll num of cell
	 * @param dy - row num of cell
	 * @param columnCount - count of column that must be loaded
	 * @param rowCount - count of row that should be loaded
	 * @param drawX - x coordinate of cell
	 * @param drawY - y coordinate of cell
	 */
    public ThreadMapPart(byte dx, byte dy, byte columnCount, byte rowCount, int drawX, int drawY) {
      this.dx=dx;
      this.dy=dy;
      this.columnCount = columnCount;
      this.rowCount = rowCount;
      this.drawX = drawX;
      this.drawY = drawY;
    }
     
    /**
     * loading and paint cell of map
     * save this [col,row] as maf from allMafs into repaintMafs
     * increments sumPartCount to know how many cell have been loaded
     */
    @Override
	public synchronized void run() {
		
		Image map = new javax.swing.ImageIcon(AppGUI.getPartMapUrl(dy,dx)).getImage();
		Graphics g = Map.getImage().getGraphics();
		g.drawImage(map, drawX + columnCount * Map.partMapWidth, drawY + rowCount * Map.partMapHeight, null);
		if (AppConfig.isWORKDEBUG()){
			g.setColor(Color.RED);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 60));
			g.drawRect(drawX+columnCount * Map.partMapWidth, drawY+rowCount * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);
			g.drawString(dy+" "+dx, drawX+columnCount* Map.partMapWidth+150, drawY+rowCount* Map.partMapHeight+200);
		}
		
		MafHashKey key = new MafHashKey(dx, dy);  
		AppGUI.repaintMafs.put(key, AppGUI.getAllMafs().get(key));
		
		map.flush();		
		AppGUI.mapPanel.loadImage(Map.getImage());
		g.dispose();
		
		ThreadMapPart.sumPartCount++;
	}
}
