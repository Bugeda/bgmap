package bgmap.core.controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.MouseInputListener;

import static bgmap.AppConfig.*;
import bgmap.core.*;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;

public class MapMouseAdapter implements MouseWheelListener, MouseMotionListener, MouseInputListener {

	private static Point startMovePoint;
	private static boolean isClicked = false;
	
	private static void setMoveFrom(Point p){
		startMovePoint = p;
		startMovePoint.x -= AppGUI.mapPanel.getOffset().x;
		startMovePoint.y -= AppGUI.mapPanel.getOffset().y;		
	}
	
	private static void setMoveTo(Point p){		   
		if (startMovePoint!=null){
			MapPanel.setOffset(new Point(p.x - startMovePoint.x, p.y - startMovePoint.y));			
			AppGUI.mapPanel.repaint();
		}
	}
	
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
    	if (Map.isScrollable()){
    		int wheel = e.getWheelRotation();
    		if ((wheel<0)&&(AppGUI.slider.getValue()-wheel>AppGUI.MIN_scale)){	
    			AppGUI.slider.setValue(AppGUI.slider.getValue()-wheel);		
    		}
    		if ((wheel>0)&&(AppGUI.slider.getValue()-wheel<AppGUI.MAX_scale)){		
    			AppGUI.slider.setValue(AppGUI.slider.getValue()-wheel);
    		}
    	}
    }
	 
    @Override
    public void mousePressed(MouseEvent e) {
    	MafViewer.closeMafViewer();
      	AppGUI.slider.setValue(100);
      	setMoveFrom(e.getPoint());         
      	AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e) {        	
    	if (startMovePoint !=null){
          		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				AppGUI.repaintMap();
				startMovePoint = null;
		    	AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));                      
    	}    		     		
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	if (Map.isScrollable(e.getPoint()))
    		setMoveTo(e.getPoint());    	
    	else mouseReleased(e);    		
    }

	@Override
	public void mouseMoved(MouseEvent e) {	
	}

	@Override
	public void mouseClicked(MouseEvent e) {

  		//repaint selected maf
		if (AppGUI.getClickedMaf()!=null){
			MafViewer.closeMafViewer();
			AppGUI.paintClickedMaf(AppGUI.getClickedMaf().getMafMark(), false);
		}	
			if ((e.getModifiersEx()==InputEvent.SHIFT_DOWN_MASK)&&(bgmap.AppConfig.isAdmin())){	
				AppGUI.slider.setValue(100);			
				MafViewer.createEditor();
			}			
			
		else {		
			AppGUI.setClickedMaf(null);
	
    		short x = (short) AppGUI.getCoordinatesByMousePos(e.getPoint()).x; 
    		short y = (short) AppGUI.getCoordinatesByMousePos(e.getPoint()).y; 
    		byte colNum = (byte) AppGUI.getCellByMousePos(e.getPoint()).x;
    		byte rowNum = (byte) AppGUI.getCellByMousePos(e.getPoint()).y;
    		
      		MafHashKey cell = new MafHashKey(colNum, rowNum);
      		if (AppGUI.getAllMafs().containsKey(cell)){
      			ArrayList<MafHashValue> list = AppGUI.getAllMafs().get(cell);
      			MafHashValue coord = new MafHashValue(x, y, (byte) 3);	
      	  			for (MafHashValue value:list){
      	  				if (coord.equals(value)){
      	  					try {
      	  						AppGUI.setClickedMaf(DBManager.readMaf((short)value.getX(),(short)value.getY(), colNum, rowNum));      	  					
    							break;							
    						} catch (SQLException e1) {
    							lgTRACE.error(e1);
    				            lgWARN.error(e1);
    				            System.exit(1);	
    						}  	  					
      	  				}
      	  			}
      	  		}
      		
      		if (bgmap.AppConfig.isAdmin()){	 		    	 
		      	if (AppGUI.getClickedMaf()!=null){		
		      		AppGUI.paintClickedMaf((byte) 2, false);	
		      		MafViewer.editClickedMaf();		      		
		      	}
      		}
      		else {
      			if (AppGUI.getClickedMaf()!=null) {
      				AppGUI.paintClickedMaf((byte) 2, false);
      				MafViewer.showClickedMafInfo();		
      			}
      		}	
		} 
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
    	if (startMovePoint !=null){
    		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));    		  		
			AppGUI.repaintMap();			            
    		startMovePoint = null;
    		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	}    		     		
    }
		 
}
