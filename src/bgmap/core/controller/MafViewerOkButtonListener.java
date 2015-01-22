package bgmap.core.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import bgmap.core.AppConfig;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;
import bgmap.core.view.AppGUI;
import bgmap.core.view.MafViewer;

public class MafViewerOkButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		MafViewer.isOpen=false;
		try{
			if (e.getActionCommand().equals("delete")){
				short x = (short) ((MafViewer.pos.x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
				short y = (short) ((MafViewer.pos.y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight);        		
				byte colNum = (byte) (Map.getStartCol() + (MafViewer.pos.x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
				byte rowNum = (byte) (Map.getStartRow() + (MafViewer.pos.y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
				DBManager.deleteMaf(x, y, colNum, rowNum);
				MafHashKey key = new MafHashKey(colNum, rowNum);
	      		if (AppGUI.mafs.containsKey(key)){
	      			ArrayList<MafHashValue> list = AppGUI.mafs.get(key);
	      			if (list.size() == 1){
	      				AppGUI.mafs.remove(key);	      				
	      			}
	      			else {
	      				MafHashValue coord = new MafHashValue(x, y, false);
	      	  			for (MafHashValue value:list)
	      	  				if (coord.equals(value)){
	      	  					list.remove(value);	      	  					
	      	  					break;
	      	  				}
	      		}
	      		if (AppGUI.mapPanel.movingAdapt.clickedMaf != null)
	    	    	AppGUI.mapPanel.movingAdapt.clickedMaf = null;
	      		AppGUI.loadPartsMap(Map.getPngScale(), 
					colNum, rowNum, (byte) 1, (byte) 1,
					Map.getMapOffset().x + (colNum-Map.getStartCol())*Map.partMapWidth,
					Map.getMapOffset().y + (rowNum-Map.getStartRow())*Map.partMapHeight);
	      		}			
	    		AppGUI.mainFrame.setEnabled(true);
				MafViewer.frame.dispose();   
			}else{
				Maf maf;
				if (MafViewer.subjectName.getText().isEmpty())  {
					JOptionPane.showMessageDialog(AppGUI.mainFrame,
						    "Поле `"+Maf.SubjectNameField+"` должно быть указано",
						    "fam editor error",
						    JOptionPane.ERROR_MESSAGE);
				}
				else{
					short x = (short) ((MafViewer.pos.x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
					short y = (short) ((MafViewer.pos.y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight);        		
					byte colNum = (byte) (Map.getStartCol() + (MafViewer.pos.x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
					byte rowNum = (byte) (Map.getStartRow() + (MafViewer.pos.y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
				
					maf = new Maf(x, y, colNum, rowNum, 
							MafViewer.subjectName.getText().trim(), 
							MafViewer.subjectAddress.getText().trim(), 
							MafViewer.subjectRegNum.getDecimal(), 
							MafViewer.telephone.getText().trim(), 
							MafViewer.site.getText().trim(), 
							MafViewer.purpose.getText().trim(), 
							MafViewer.objectAddress.getText().trim(), 
							MafViewer.techCharacteristics.getText().trim(),
							MafViewer.passport.getText().trim(), 
							MafViewer.personFullName.getText().trim());
					
					if (e.getActionCommand().equals("update")){				
						DBManager.updateMaf(maf);
						MafHashKey key = new MafHashKey(colNum, rowNum);
			      		if (AppGUI.mafs.containsKey(key)){
			      			ArrayList<MafHashValue> list = AppGUI.mafs.get(key);
			      			MafHashValue coord = new MafHashValue(x, y, maf.isFull());
			      	  			for (MafHashValue value:list)
			      	  				if (coord.equals(value)){
			      	  					value.setFull(maf.isFull());
			      	  					break;
			      	  				}
			      		}		      	 
			      	if (AppGUI.mapPanel.movingAdapt.clickedMaf != null)
			      		AppGUI.mapPanel.movingAdapt.clickedMaf = maf;		      	
					}
					else{			
						DBManager.insertMaf(maf);
						if (maf.isFull())
							AppGUI.paintMaf(AppConfig.signFull,true,maf);
						else 
							AppGUI.paintMaf(AppConfig.sign,true,maf);
					}
					AppGUI.mainFrame.setEnabled(true);
					MafViewer.frame.dispose();   
				}
			}
		} catch (SQLException ex){
			JOptionPane.showMessageDialog(AppGUI.mainFrame,
				    "Ошибка сохранения информации: "+ex.getCause().getMessage(),
				    "fam editor error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

}