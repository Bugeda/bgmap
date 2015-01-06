package bgmap.core;
import static org.junit.Assert.*;

import java.awt.Point;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import bgmap.core.*;
import bgmap.core.entity.Map;

public class ViewMapTest {

	
	@Test
	public void testRightDown() throws IOException {
		AppConfig.setConfig(true);	
		ViewMap.Run((byte)1,(byte)50,(byte)23);	
		ViewMap.impanel.setMoveFrom(new Point(2000, 500));
		ViewMap.impanel.setMoveTo(new Point(2100, 500));
		ViewMap.impanel.startPoint = null;
		
		int x = ViewMap.impanel.offset.x + Map.getMapOffset().x;
				
		byte xAxis = (byte) (x > 0 ? -1 : 0);			
		int dx = x % Map.partMapWidth;		
		int rightPartCellWidth = (Map.partMapWidth)*(xAxis + 1) + dx;
		int leftPartCellWidth = rightPartCellWidth - Map.partMapWidth;			
		byte extraCols = (byte) (dx != 0 ? 1 : dx < 0 ? -1 : 0);
		byte addColCount = (byte) (x/Map.partMapWidth + extraCols);
		byte startCol = (byte) (Map.getStartCol() - addColCount);
		int wCols = Math.abs(Map.partMapWidth * addColCount);	
		
		int y = ViewMap.impanel.offset.y + Map.getMapOffset().y;	
		byte yAxis = (byte) (y > 0 ? -1 : 0);	

		int dy = y % Map.partMapHeight;			
		int downPartCellHeight = (Map.partMapHeight) * (yAxis + 1) + dy;
		int upPartCellHeight = downPartCellHeight - Map.partMapHeight;
		
		byte extraRows = (byte) (dy != 0 ? 1 : 0);
		byte addRowCount = (byte) (y/Map.partMapHeight + extraRows); 
		byte startRow = (byte) (Map.getStartRow() - addRowCount);					
		int hRows = Math.abs(Map.partMapHeight * addRowCount);
		assertEquals(1, addRowCount);
		assertEquals(1, addColCount);
		assertEquals(1,extraCols);
		assertEquals(1,extraRows);
		assertEquals(16,startRow);
		assertEquals(35, startCol);
		assertEquals(-220,leftPartCellWidth);
		assertEquals(-380,upPartCellHeight);
		assertEquals(100, upPartCellHeight+hRows);	
		assertEquals(7, Map.ROW_COUNT+Math.abs(addRowCount));
		
		ViewMap.impanel.setMoveFrom(new Point(2500, 2000));
		ViewMap.impanel.setMoveTo(new Point(2700, 2100));
		ViewMap.impanel.startPoint = null;
		
		
		x = ViewMap.impanel.offset.x + Map.getMapOffset().x;		
		xAxis = (byte) (x > 0 ? -1 : 0);			
		dx = x % Map.partMapWidth;		
		rightPartCellWidth = (Map.partMapWidth)*(xAxis + 1) + dx;
		leftPartCellWidth = rightPartCellWidth - Map.partMapWidth;			
		extraCols = (byte) (dx != 0 ? 1 : dx < 0 ? -1 : 0);
		addColCount = (byte) (x/Map.partMapWidth + extraCols);
		startCol = (byte) (Map.getStartCol() - addColCount);
		wCols = Math.abs(Map.partMapWidth * addColCount);	
		
		y = ViewMap.impanel.offset.y + Map.getMapOffset().y;	
		yAxis = (byte) (y > 0 ? -1 : 0);	

		dy = y % Map.partMapHeight;			
		downPartCellHeight = (Map.partMapHeight) * (yAxis + 1) + dy;
		upPartCellHeight = downPartCellHeight - Map.partMapHeight;
		
		extraRows = (byte) (dy != 0 ? 1 : 0);
		addRowCount = (byte) (y/Map.partMapHeight + extraRows); 
		startRow = (byte) (Map.getStartRow() - addRowCount);					
		hRows = Math.abs(Map.partMapHeight * addRowCount);
		assertEquals(1,addRowCount);
		assertEquals(1, addColCount);
		assertEquals(1,extraCols);
		assertEquals(1,extraRows);
		assertEquals(16,startRow);
		assertEquals(35, startCol);
		assertEquals(-20,leftPartCellWidth);
		assertEquals(-280,upPartCellHeight);
		assertEquals(100, upPartCellHeight+hRows);	
		assertEquals(7, Map.ROW_COUNT+Math.abs(addRowCount));
	}



}
