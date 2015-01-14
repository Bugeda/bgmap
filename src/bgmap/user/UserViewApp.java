package bgmap.user;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import bgmap.core.AppConfig;
import bgmap.core.MapPanel;
import bgmap.core.MapMouseAdapter;
import bgmap.core.ThreadMapPart;
import bgmap.core.entity.*;

public class UserViewApp extends bgmap.core.AppGUI {
	   public static void main(String[] args) throws IOException{
			AppConfig.setConfig(true);			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	try {
							createAndShowGUI((byte)1,(byte)50,(byte)23);
						} catch (IOException e) {
							e.printStackTrace();
						}
		            }
		        });
		}   
	   
	private static void createFrame(){	
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		f.getContentPane().add(new JScrollPane(impanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
													 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));			  
         		 
		f.setSize(AppConfig.appWidth, AppConfig.appHeight); 
		f.setBackground(Color.gray);
		f.setResizable(false);
		f.setVisible(true);	
	}
	
	/**
	 * Create map from parts of map,
	 * Starts with scale, from (x, y) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI(byte scale, byte x, byte y) throws IOException{	
		impanel = new MapPanel(createMap(scale, x, y));	
		createFrame();		
	}
	/**
	 * Create map from parts of map,
	 * Starts with scale=1, from (1, 1) map parts
	 * @throws IOException
	 */
	public static void createAndShowGUI() throws IOException{			
		impanel = new MapPanel(createMap((byte)1,(byte)1,(byte)1));	
		createFrame();		
	}	
	
	/**
	 * Create map from the source,
	 * Starts with scale=1
	 * @throws IOException
	 */
	public static void createAndShowGUI(String source) throws IOException{		
		Map.setPngScale((byte) 1);
		impanel = new MapPanel(source);	
		createFrame();		
	}
}
