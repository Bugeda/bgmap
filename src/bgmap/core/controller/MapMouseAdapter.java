package bgmap.core.controller;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.MouseInputListener;

import bgmap.core.AdminPanelStatus;
import bgmap.core.AppConfig;
import bgmap.core.model.Maf;
import bgmap.core.model.MafHashKey;
import bgmap.core.model.MafHashValue;
import bgmap.core.model.Map;
import bgmap.core.model.dao.DBManager;
import bgmap.core.view.AppGUI;
import bgmap.core.view.MafViewer;

public class MapMouseAdapter implements MouseWheelListener, MouseMotionListener, MouseInputListener {
		
	Maf clickedMaf;
	
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {		
    	int wheel = e.getWheelRotation();
		if ((wheel<0)&&(AppGUI.slider.getValue()-wheel>AppGUI.MIN_scale)){	
			AppGUI.slider.setValue(AppGUI.slider.getValue()-wheel);		
		}
		if ((wheel>0)&&(AppGUI.slider.getValue()-wheel<AppGUI.MAX_scale)){		
			AppGUI.slider.setValue(AppGUI.slider.getValue()-wheel);
		}		
    }
	 
    @Override
    public void mousePressed(MouseEvent e) {
    	if (MafViewer.isOpen) {
			MafViewer.frame.dispose();   		
			MafViewer.isOpen=false;
		}
      	AppGUI.slider.setValue(100);
      	AppGUI.mapPanel.setMoveFrom(e.getPoint());         
      	AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e) {        	
    	if (AppGUI.mapPanel.startPoint !=null){
          		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				AppGUI.repaintMap();
				AppGUI.mapPanel.startPoint = null;
		    	AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));                      
    	}    		     		
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	if (Map.isScrollable(e.getPoint()))
    		AppGUI.mapPanel.setMoveTo(e.getPoint());    	
    	else mouseReleased(e);    		
    }

	@Override
	public void mouseMoved(MouseEvent e) {	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
  		//repaint selected maf
		if (clickedMaf!=null){
			if (MafViewer.isOpen) {
				MafViewer.frame.dispose();   		
				MafViewer.isOpen=false;
			}
			
			if (clickedMaf.isFull())					
				 AppGUI.paintMaf(AppConfig.signFull, false, clickedMaf);								
			  else 				  					 	
				 AppGUI.paintMaf(AppConfig.sign, false, clickedMaf);
		}
		//check if exist maf
		if (e.getClickCount() == 1){
			clickedMaf = null;
    		short x = (short) ((e.getX() - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
    		short y = (short) ((e.getY() - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight); 
    		boolean isFull = false;
    		byte colNum = (byte) (Map.getStartCol() + (e.getX() - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
    		byte rowNum = (byte) (Map.getStartRow() + (e.getY() - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
      		MafHashKey cell = new MafHashKey(colNum, rowNum);
      		if (AppGUI.mafs.containsKey(cell)){
      			ArrayList<MafHashValue> list = AppGUI.mafs.get(cell);
      			MafHashValue coord = new MafHashValue(x, y, isFull);
      	  			for (MafHashValue value:list){
      	  				if (coord.equals(value)){
      	  					try {
      	  						clickedMaf = DBManager.readMaf((short)value.getX(),(short)value.getY(), colNum, rowNum);      	  					
    							break;							
    						} catch (SQLException e1) {
    							AppConfig.lgTRACE.error(e1);
    				            AppConfig.lgWARN.error(e1);
    				            System.exit(1);	
    						}  	  					
      	  				}
      	  			}
      	  		}
		}
		if (AdminPanelStatus.isEditMaf()){
			AppGUI.slider.setValue(100);
			if (e.getClickCount() == 2) 
				MafViewer.createEditor();
			else 		 		    	 
		      	if (clickedMaf!=null){		      		
		      		AppGUI.paintMaf(AppConfig.signOn, false, clickedMaf);				    
		      		MafViewer.editMaf(clickedMaf);		      		
		      	}
		}
		else {
			if (clickedMaf!=null) {
		  		AppGUI.paintMaf(AppConfig.signOn, false, clickedMaf);
	    		MafViewer.showMafInfo(clickedMaf);		
			}
		}		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
    	if (AppGUI.mapPanel.startPoint !=null){
    		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));    		  		
			AppGUI.repaintMap();			            
    		AppGUI.mapPanel.startPoint = null;
    		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	}    		     		
    }
		 
}
