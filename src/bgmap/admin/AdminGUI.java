package bgmap.admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.OverlayLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bgmap.core.AppConfig;
import bgmap.core.model.Maf;
import bgmap.core.model.Map;
import bgmap.core.view.AppGUI;
import bgmap.core.view.MapPanel;

public class AdminGUI extends AppGUI {
	public static void main(String[] args){
		AppConfig.setConfig(true);			
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            		createAndShowGUI((byte)1,(byte)50,(byte)23);
	            }
	        });
	}    
	
	private static void createFrame(){		
	
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(AppConfig.appWidth, AppConfig.appHeight);
		mainFrame.setBackground(Color.gray);
		mainFrame.setResizable(false);
	
		createSlider();
		creadeAdminPanel();
		
		mainFrame.add(adminPanel, "North");
		mainFrame.add(slider, "East");		
		workPanel.add(AppGUI.mapPanel, 1, 0);	
		
	
		
	    mainFrame.add(new JScrollPane(workPanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
				 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));	
	 	
		slider.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                int value = ((JSlider)e.getSource()).getValue();     
	                mapPanel.setScale(value/100.0);	                	               
	            }  
	        });         		 
		mainFrame.setVisible(true);	
	
		int h = AppGUI.adminPanel !=null ? AppGUI.adminPanel.getHeight() : 0;		
		mapPanel.setSize(AppConfig.appWidth - AppGUI.slider.getWidth(), AppConfig.appHeight+h);
		mapPanel.setLocation((int)(-AppGUI.mainFrame.getInsets().left - AppGUI.mainFrame.getOpacity()),
				    (int)(-h-AppGUI.mainFrame.getInsets().top/2 + AppGUI.mainFrame.getOpacity() - 2));
		
     for (Maf maf:AppGUI.mafs){               
        // MafPanel mp = new MafPanel(maf);
    	   paintMaf(maf);
       }      	
	  
	}
		
	/**
	 * Create map from parts of map,
	 * Starts with scale, from (x, y) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI(byte scale, byte x, byte y){	
		mapPanel = new MapPanel(createMap(scale, x, y));	
		createFrame();		
	}
	/**
	 * Create map from parts of map,
	 * Starts with scale=1, from (1, 1) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI() {			
		mapPanel = new MapPanel(createMap((byte)1,(byte)1,(byte)1));	
		createFrame();		
	}	
	
	/**
	 * Create map from the source,
	 * Starts with scale=1
	 * @throws IOException
	 */
	public static void createAndShowGUI(String source) throws IOException{		
		Map.setPngScale((byte) 1);
		mapPanel = new MapPanel(source);	
		createFrame();		
	}
}
