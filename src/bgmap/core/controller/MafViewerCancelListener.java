package bgmap.core.controller;

import java.awt.event.*;

import bgmap.core.MafViewer;

public class MafViewerCancelListener implements ActionListener, WindowListener, KeyListener{
	
	@Override
	public void actionPerformed(ActionEvent arg0) {	
		MafViewer.closeMafViewer("cancel");
	}

	@Override
	public void windowActivated(WindowEvent e) {	
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		MafViewer.closeMafViewer("cancel");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {	
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
			MafViewer.closeMafViewer("cancel");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
