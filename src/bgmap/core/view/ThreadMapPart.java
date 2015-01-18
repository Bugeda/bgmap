package bgmap.core.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import bgmap.core.AppConfig;
import bgmap.core.model.Maf;
import bgmap.core.model.MafHashKey;
import bgmap.core.model.MafHashValue;
import bgmap.core.model.Map;
import bgmap.core.model.dao.DBManager;

public class ThreadMapPart implements Runnable {	 
	 private byte dx, dy;	 
	 private byte x, y;	 
	 private int drawX, drawY;	 
	 
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
		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));	
		g.drawImage(map, drawX+x * Map.partMapWidth, drawY+y * Map.partMapHeight, null);
		g.drawRect(drawX+x * Map.partMapWidth, drawY+y * Map.partMapHeight, Map.partMapWidth,Map.partMapHeight);
		g.drawString(dy+" "+dx, drawX+x* Map.partMapWidth+150, drawY+y* Map.partMapHeight+200);	
		MafHashKey key = new MafHashKey(dx,dy);
		ArrayList<MafHashValue> value = AppGUI.mafs.get(key);
		if (value != null)
		  for (MafHashValue vl:value)
			  g.drawImage(AppConfig.signFull, 
    				Map.getMapOffset().x+Map.partMapWidth*(key.getCol()-Map.getStartCol())+vl.getX() - AppConfig.signFull.getWidth(null)/2,
    				Map.getMapOffset().y+Map.partMapHeight*(key.getRow()-Map.getStartRow())+vl.getY() - AppConfig.signFull.getHeight(null),
    				AppConfig.signFull.getWidth(null), AppConfig.signFull.getHeight(null), null);		
		map.flush();
		AppGUI.mapPanel.loadImage(Map.getImage());
		g.dispose();
		AppGUI.mapPanel.repaint();	
	}
}
