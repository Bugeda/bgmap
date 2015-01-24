package bgmap.core.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import bgmap.core.AppConfig;
import bgmap.core.model.MafHashKey;
import bgmap.core.model.MafHashValue;
import bgmap.core.model.Map;

public class ThreadMapPart implements Runnable {
	
	private static int mapPartCounts;
	private static int sumPartCount;	 
	
	private byte dx, dy;	 
	private byte x, y;	 
	private int drawX, drawY;	 
	 
	public static int getMapPartCounts() {
		return mapPartCounts;
	}

	public static void setMapPartCounts(int mapPartCounts) {
		ThreadMapPart.mapPartCounts = mapPartCounts;
	}		
			
    public ThreadMapPart(byte dx, byte dy, byte x, byte y, int drawX, int drawY) {
      this.dx=dx;
      this.dy=dy;
      this.x = x;
      this.y = y;
      this.drawX = drawX;
      this.drawY = drawY;
    }
     
	@Override
	public synchronized void run() {
		Image map = new javax.swing.ImageIcon(AppGUI.getPartMapUrl(dy,dx)).getImage();
		Graphics g = Map.getImage().getGraphics();
	/*	g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 60));*/	
		g.drawImage(map, drawX+x * Map.partMapWidth, drawY+y * Map.partMapHeight, null);
		/*g.drawRect(drawX+x * Map.partMapWidth, drawY+y * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);
		g.drawString(dy+" "+dx, drawX+x* Map.partMapWidth+150, drawY+y* Map.partMapHeight+200);	*/		
			  		
		map.flush();		
		AppGUI.mapPanel.loadImage(Map.getImage());
		g.dispose();
		
		if (mapPartCounts==++sumPartCount){
		//	AppGUI.repaintMafs();
			sumPartCount = 0;
		}
	
		AppGUI.mapPanel.repaint();	
		
	}


}
