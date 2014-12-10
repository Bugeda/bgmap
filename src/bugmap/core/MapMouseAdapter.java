package bugmap.core;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import bugmap.core.entity.Log;

public class MapMouseAdapter extends MouseAdapter {
	
	private Point startPoint;
	
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int wheel = e.getWheelRotation();
	      if (AppConfig.isDEBUG()){
        	Log.getTRACE().debug("wheel=" +wheel); 
        	Log.getTRACE().debug("slider.getValue()= "+MapActions.slider.getValue());
        	Log.getTRACE().debug("impanel.scale= "+MapActions.impanel.scale); 
        }
		if ((wheel>0)&&(MapActions.slider.getValue()+wheel>MapActions.MIN_scale)){
			MapActions.slider.setValue(MapActions.slider.getValue()+wheel);
		
		}
		if ((wheel<0)&&(MapActions.slider.getValue()+wheel<MapActions.MAX_scale)){
			MapActions.slider.setValue(MapActions.slider.getValue()+wheel);
		}
		
	}
	 
    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        startPoint.x -= MapActions.impanel.offset.x;
        startPoint.y -= MapActions.impanel.offset.y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        startPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        int x = p.x - startPoint.x;
        int y = p.y - startPoint.y;
        
        MapActions.impanel.offset = new Point(x, y);
        MapActions.impanel.repaint();
    }

}
