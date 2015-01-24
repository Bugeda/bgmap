package bgmap.core.controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.MouseInputListener;

import bgmap.core.AdminPanelStatus;
import bgmap.core.AppConfig;
import bgmap.core.MafsMarks;
import bgmap.core.model.Maf;
import bgmap.core.model.MafHashKey;
import bgmap.core.model.MafHashValue;
import bgmap.core.model.Map;
import bgmap.core.model.dao.DBManager;
import bgmap.core.view.AppGUI;
import bgmap.core.view.MafViewer;
import bgmap.core.view.MapPanel;

public class MapMouseAdapter implements MouseWheelListener, MouseMotionListener, MouseInputListener {

	private Point startMovePoint;
	
	private void setMoveFrom(Point p){
		startMovePoint = p;
		startMovePoint.x -= AppGUI.mapPanel.getOffset().x;
		startMovePoint.y -= AppGUI.mapPanel.getOffset().y;		
	}
	
	private void setMoveTo(Point p){		   
		if (startMovePoint!=null){
			AppGUI.mapPanel.setOffset(new Point(p.x - startMovePoint.x, p.y - startMovePoint.y));			
			AppGUI.mapPanel.repaint();
		}
	}
	
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
			AppGUI.paintClickedMaf(AppGUI.getClickedMaf().getMafMark().img, false);
		}
		//check if exist maf
		if (e.getClickCount() == 1){
			AppGUI.setClickedMaf(null);
    		short x = (short) ((e.getX() - MapPanel.getPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
    		short y = (short) ((e.getY() - MapPanel.getPos().y - Map.getMapOffset().y)  % Map.partMapHeight); 
    		byte colNum = (byte) (Map.getStartCol() + (e.getX() - MapPanel.getPos().x - Map.getMapOffset().x) / Map.partMapWidth );
    		byte rowNum = (byte) (Map.getStartRow() + (e.getY() - MapPanel.getPos().y - Map.getMapOffset().y) / Map.partMapHeight);
      		MafHashKey cell = new MafHashKey(colNum, rowNum);
      		if (AppGUI.getMafs().containsKey(cell)){
      			ArrayList<MafHashValue> list = AppGUI.getMafs().get(cell);
      			MafHashValue coord = new MafHashValue(x, y, MafsMarks.SIGNNEW);
      	  			for (MafHashValue value:list){
      	  				if (coord.equals(value)){
      	  					try {
      	  						AppGUI.setClickedMaf(DBManager.readMaf((short)value.getX(),(short)value.getY(), colNum, rowNum));      	  					
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
		      	if (AppGUI.getClickedMaf()!=null){		      		
		      		AppGUI.paintClickedMaf(MafsMarks.SIGNON.img, false);		    
		      		MafViewer.editClickedMaf();		      		
		      	}
		}
		else {
			if (AppGUI.getClickedMaf()!=null) {
				AppGUI.paintClickedMaf(MafsMarks.SIGNON.img, false);		  		
	    		MafViewer.showClickedMafInfo();		
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
