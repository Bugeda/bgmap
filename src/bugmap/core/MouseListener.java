package bugmap.core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import bugmap.core.entity.Log;

public class MouseListener extends MouseAdapter {
	private int x;
    private int y;

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
		x=e.getX();
		y=e.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		/*System.out.println(x +" "+y);*/		
		int dx = e.getX() - x;
		int dy = e.getY() - y;
		/*System.out.println(dx +" "+dy);*/
		System.out.println(e.getX() +" "+e.getY());
		//MapActions.impanel.move(e.getX(), e.getY());
		//MapActions.impanel.setSize(MapActions.impanel.getWidth()-dx, MapActions.impanel.getHeight()-dy);
		MapActions.impanel.setBounds(dx, dy, MapActions.impanel.getWidth(), MapActions.impanel.getHeight());

		/*
		if (MapActions.impanel.getBounds().contains(x, y)) {

			
			dx++;
		    dy++;
		   
		    MapActions.impanel.paint(MapActions.impanel.getGraphics());
		 
		    /
		}*/
	}

}
