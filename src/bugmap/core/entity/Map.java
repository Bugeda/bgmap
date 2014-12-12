package bugmap.core.entity;

import java.awt.image.BufferedImage;

public class Map {	
	private static BufferedImage image;  	
	private static short startRow = 1;	
	private static short startCol = 1;    
	private static int pngScale = 1;
	
	public static BufferedImage getImage() {
		return image;
	}

	public static void setImage(BufferedImage image) {
		Map.image = image;
	}

	public Map() {	
	}
	
	public Map(BufferedImage image) {
		Map.image = image;
	}
	
	public static short getStartRow() {
		return startRow;
	}

	public static void setStartRow(short startRow) {
		Map.startRow = startRow;
	}

	public static short getStartCol() {
		return startCol;
	}

	public static void setStartCol(short startCol) {
		Map.startCol = startCol;
	}

	public static int getPngScale() {
		return pngScale;
	}

	public static void setPngScale(int pngScale) {
		Map.pngScale = pngScale;
	}


}
