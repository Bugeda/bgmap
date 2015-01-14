package bgmap.core;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import bgmap.core.entity.Maf;
import bgmap.core.entity.Map;

public class MafPanel {

	Maf maf;
	Point pos;
	
	final static private Image sign = new ImageIcon("src/images/src/home.png").getImage(); 
		
	public MafPanel(Maf maf){
		
		this.pos = new Point(Map.getMapOffset().x+Map.partMapWidth*(maf.getColNum()-Map.getStartCol())+maf.getX(),
				Map.getMapOffset().y+Map.partMapHeight*(maf.getRowNum()-Map.getStartRow())+maf.getY());  
		JButton b1 = new JButton(maf.getSubjectName());
	   
	    AppGUI.workPanel.add(b1, 2, 0);

	    Insets insets = AppGUI.workPanel.getInsets();
	    Dimension size = b1.getPreferredSize();
	    b1.setBounds(pos.x+Map.getMapPos().x  + Map.getMapOffset().x + insets.left, 5 + insets.top, size.width, size.height);
	    /*System.out.println(pos);
	    System.out.println(Map.getMapPos());
	    System.out.println(pos.x+Map.getMapPos().x);
	    b1.setBounds(pos.x+Map.getMapPos().x, pos.y + Map.getMapPos().y, size.width, size.height);*/
	    
		/*this.maf = maf;
		
		   JButton button = new JButton("Small");
		    button.setMaximumSize(new Dimension(25, 25));
		    button.setBackground(Color.white);
	
			button.setBounds(pos.x, pos.y, 25, 25);
			
			System.out.println(pos);
		    AppGUI.workPanel.add(button,2,0);
		    AppGUI.workPanel.repaint();
		    button.setLocation(pos);*/
		   // button.setBounds(pos.x, pos.y, 32, 37);
		//JPanel newMafPanel = new JPanel();
		
		//newMafPanel.setSize(32, 37);
		//newMafPanel.setMaximumSize(new Dimension(32, 37));
		//AppGUI.impanel.add(newMafPanel);	
		//paintMaf(maf);		
		    
	}
	
	public static void paintMaf(Maf maf){			
		Graphics g = Map.getImage().getGraphics();  		
		g.setColor(Color.red);
		g.fillOval(Map.getMapOffset().x+Map.partMapWidth*(maf.getColNum()-Map.getStartCol())+maf.getX(), Map.getMapOffset().y+Map.partMapHeight*(maf.getRowNum()-Map.getStartRow())+maf.getY(), 5, 5);
		AppGUI.impanel.loadImage(Map.getImage());
		g.dispose();
		AppGUI.impanel.repaint();	
	}
	
	/* @Override
     protected void paintComponent(Graphics g) {        	
	      	super.paintComponent(g);
         if (sign != null) {                  	
 	        int newImageWidth = (int) (sign.getWidth(null)*AppGUI.impanel.scale);  
 	        int newImageHeight = (int) (sign.getHeight(null)*AppGUI.impanel.scale);    	   	     
 	      
 	        Graphics2D g2 = (Graphics2D)g;                	   
 	        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
 	                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
 	
 		    g2.drawImage(sign, 0, 0, newImageWidth, newImageHeight,null);
 	        g2.dispose();         	        
 	    }    

     }*/
}
/// int x = (getWidth() - newImageWidth)/2;  
 // int y = (getHeight() - newImageHeight)/2;   	   
  //int x = 0;  
  //int y = 0;