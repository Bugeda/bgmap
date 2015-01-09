package bgmap.core;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import org.eclipse.swt.events.TypedEvent;

import bgmap.admin.ViewApp;
import bgmap.core.entity.Map;

public class MapMouseAdapter implements MouseWheelListener, MouseMotionListener, MouseInputListener {
		
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {		
    	int wheel = e.getWheelRotation();
		if ((wheel<0)&&(ViewApp.slider.getValue()-wheel>ViewApp.MIN_scale)){	
			ViewApp.slider.setValue(ViewApp.slider.getValue()-wheel);		
		}
		if ((wheel>0)&&(ViewApp.slider.getValue()-wheel<ViewApp.MAX_scale)){		
			ViewApp.slider.setValue(ViewApp.slider.getValue()-wheel);
		}		
    }
	 
    @Override
    public void mousePressed(MouseEvent e) {
    //    if (e.getButton()==MouseEvent.BUTTON1)
      //		ViewMapTest.slider.setValue(100);
     	ViewApp.impanel.setMoveFrom(e.getPoint());         
        ViewApp.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e) {        	
    	if (ViewApp.impanel.startPoint !=null){
    		ViewApp.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
             if (e.getButton()==MouseEvent.BUTTON1)      {  		
				ViewApp.paintMap();
            }
    		ViewApp.impanel.startPoint = null;
    		ViewApp.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	}    		     		
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	if (Map.isScrollable(e.getPoint()))
    		ViewApp.impanel.setMoveTo(e.getPoint());    	
    	else mouseReleased(e);    		
    }

	@Override
	public void mouseMoved(MouseEvent e) {	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
    	if (ViewApp.impanel.startPoint !=null){
    		ViewApp.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    		 if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {  		
				ViewApp.paintMap();			
            }
    		ViewApp.impanel.startPoint = null;
    		ViewApp.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	}    		     		
    }
		 
}
