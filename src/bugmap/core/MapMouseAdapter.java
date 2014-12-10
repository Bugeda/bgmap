package bugmap.core;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bugmap.core.entity.Log;

public class MapMouseAdapter extends MouseAdapter {
		
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
    	MapActions.impanel.setMoveFrom(e.getPoint());         
        MapActions.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        /*Toolkit toolkit = Toolkit.getDefaultToolkit();  
  Image image = toolkit.getImage("src/ico/hand_point_zoom.ico");          
  image.flush();
  MapActions.impanel.setCursor(toolkit.createCustomCursor(image , new Point(200,200), "closehand"));*/          
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    	MapActions.impanel.startPoint = null;       
        MapActions.impanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	MapActions.impanel.setMoveTo(e.getPoint());
    }

}
