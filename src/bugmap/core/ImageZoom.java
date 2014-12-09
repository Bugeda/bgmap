package bugmap.core;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageZoom {
	  ImagePanel imagePanel;  
	   
	    public ImageZoom(ImagePanel ip) {  
	        imagePanel = ip;  
	    }  
	   
	    public JPanel getUIPanel() {  
	        SpinnerNumberModel model = new SpinnerNumberModel(1.0, 0.1, 1.4, .2);  
	        final JSpinner spinner = new JSpinner(model);  
	        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));  
	        spinner.addChangeListener(new ChangeListener() {  
	            public void stateChanged(ChangeEvent e) {  
	                float scale = ((Double)spinner.getValue()).floatValue();  
	                imagePanel.setScale(scale);  
	            }  
	        });  
	        JPanel panel = new JPanel();  
	        panel.add(new JLabel("scale"));  
	        panel.add(spinner);  
	        return panel;  
	    }  
}
