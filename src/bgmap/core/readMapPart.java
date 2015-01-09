package bgmap.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.Callable;

import bgmap.core.entity.Map;

public class readMapPart implements Runnable {	 
	 private byte dx, dy;	 
	 private byte x, y;	 
	 private int drawX, drawY;	 
	 
     public readMapPart(byte dx, byte dy, byte x, byte y, int drawX, int drawY) {
      this.dx=dx;
      this.dy=dy;
      this.x = x;
      this.y = y;
      this.drawX = drawX;
      this.drawY = drawY;
     }
     

	@Override
	public synchronized void run() {
		Image map = new javax.swing.ImageIcon(ViewMap.getPartMapUrl(dy,dx)).getImage();
		Graphics g = Map.getImage().getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));	
		g.drawImage(map, drawX+x * Map.partMapWidth, drawY+y * Map.partMapHeight, null);
		g.drawRect(drawX+x * Map.partMapWidth, drawY+y * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);	
		g.drawString(dy+" "+dx, drawX+x* Map.partMapWidth+150, drawY+y* Map.partMapHeight+200);				
		map.flush();
		ViewMap.impanel.loadImage(Map.getImage());
		g.dispose();
		ViewMap.impanel.repaint();
		
	}

}
