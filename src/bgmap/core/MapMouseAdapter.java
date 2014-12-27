package bgmap.core;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import bgmap.core.entity.Map;

public class MapMouseAdapter implements MouseWheelListener, MouseMotionListener, MouseInputListener {
		
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {		
    	int wheel = e.getWheelRotation();
		if ((wheel<0)&&(ViewMap.slider.getValue()-wheel>ViewMap.MIN_scale)){	
			ViewMap.slider.setValue(ViewMap.slider.getValue()-wheel);		
		}
		if ((wheel>0)&&(ViewMap.slider.getValue()-wheel<ViewMap.MAX_scale)){		
			ViewMap.slider.setValue(ViewMap.slider.getValue()-wheel);
		}		
    }
	 
    @Override
    public void mousePressed(MouseEvent e) {
    //    if (e.getButton()==MouseEvent.BUTTON1)
      //		ViewMap.slider.setValue(100);
     	ViewMap.impanel.setMoveFrom(e.getPoint());         
        ViewMap.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e) {        	
    	if (ViewMap.impanel.startPoint !=null){
            ViewMap.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (e.getButton()==MouseEvent.BUTTON1)        		
				ViewMap.addPartsMap(e.getPoint());
    		ViewMap.impanel.startPoint = null;
    	}    		     		
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	if (Map.isScrollable(e.getPoint()))
    		ViewMap.impanel.setMoveTo(e.getPoint());    	
    	else mouseReleased(e);    		
    }

	@Override
	public void mouseMoved(MouseEvent arg0) {	
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

}
