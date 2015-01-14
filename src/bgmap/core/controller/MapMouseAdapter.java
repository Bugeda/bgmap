package bgmap.core.controller;

import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import bgmap.core.AdminPanelStatus;
import bgmap.core.model.Map;
import bgmap.core.view.AppGUI;
import bgmap.core.view.MafEditor;

public class MapMouseAdapter implements MouseWheelListener, MouseMotionListener, MouseInputListener {
		
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
        if (e.getButton()==MouseEvent.BUTTON1)
      		AppGUI.slider.setValue(100);
     	AppGUI.mapPanel.setMoveFrom(e.getPoint());         
        AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e) {        	
    	if (AppGUI.mapPanel.startPoint !=null){
    		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
             if (e.getButton()==MouseEvent.BUTTON1)      {  		
				AppGUI.paintMap();
            }
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
		System.out.println("click="+e.getPoint()+","+AppGUI.mapPanel.getMousePosition());
		if (AdminPanelStatus.isAddMaf()){
		     if (e.getButton()==MouseEvent.BUTTON1)
		      		AppGUI.slider.setValue(100);
			MafEditor.createEditor();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
    	if (AppGUI.mapPanel.startPoint !=null){
    		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    		 if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {  		
				AppGUI.paintMap();			
            }
    		AppGUI.mapPanel.startPoint = null;
    		AppGUI.mapPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	}    		     		
    }
		 
}
