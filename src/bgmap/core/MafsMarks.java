package bgmap.core;

import java.awt.Image;

import javax.swing.ImageIcon;

public enum MafsMarks {
  SIGN (new ImageIcon("src/images/src/sign.png").getImage()),	   	  
  SIGNFULL (new ImageIcon("src/images/src/signFull.png").getImage()),		  
  SIGNON (new ImageIcon("src/images/src/signOn.png").getImage()),	
  SIGNNEW (new ImageIcon("src/images/src/signNew.png").getImage());
  
  public final Image img;
  
  MafsMarks(Image img){
	  this.img = img;
  }
  
  public static MafsMarks getFullMark(boolean isFull){
	  if (isFull)
	  	return SIGNFULL;
	  else return SIGN; 
  }
	 
}

