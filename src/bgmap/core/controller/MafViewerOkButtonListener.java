package bgmap.core.controller;

import static bgmap.core.model.Map.COL_COUNT;
import static bgmap.core.model.Map.ROW_COUNT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import bgmap.AppConfig;
import bgmap.core.*;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;

public class MafViewerOkButtonListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		try{	
			if (e.getActionCommand().equals("delete")){
				 if (JOptionPane.showConfirmDialog(AppGUI.mainFrame, 
			    		  "Вы действительно хотите удалить объект?", 
			    		  "Удаление объекта",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			    			  short x = (short) AppGUI.getCoordinatesByMousePos(MafViewer.getMousePositionClick()).x; 
			    			  short y = (short) AppGUI.getCoordinatesByMousePos(MafViewer.getMousePositionClick()).y;        		
			    			  byte colNum = (byte) AppGUI.getCellByMousePos(MafViewer.getMousePositionClick()).x;
			    			  byte rowNum = (byte) AppGUI.getCellByMousePos(MafViewer.getMousePositionClick()).y;
			    			  DBManager.deleteMaf(x, y, colNum, rowNum);			    				 
			    			  
			    			  MafHashKey key = new MafHashKey(colNum, rowNum);
			    			  if (AppGUI.getAllMafs().containsKey(key)){
			    				  ArrayList<MafHashValue> list = AppGUI.getAllMafs().get(key);			    				  
			    				  if (list.size() == 1){
			    					  AppGUI.getAllMafs().remove(key);	      				
			    				  } else {			    					  
			    					  MafHashValue coord = new MafHashValue(x, y, (byte) 2);
			    					  for (MafHashValue value:list)
			    						  if (coord.equals(value)){
			    							  list.remove(value);	      	  					
			    							  break;
			    						  }
			    				  }
			    				  AppGUI.setClickedMaf(null);
			    				  AppGUI.eraseDeletedMaf(colNum, rowNum);			    				 
			    			  }			    		
			    			  MafViewer.closeMafViewer();
				 }			    		  							
			}else{
				Maf maf;
				if (MafViewer.subjectName.getText().isEmpty())  {
					JOptionPane.showMessageDialog(AppGUI.mainFrame,
						    "Поле `"+Maf.SubjectNameField+"` должно быть указано",
						    "fam editor error",
						    JOptionPane.ERROR_MESSAGE);
				}
				else{					
	    			short x = (short) AppGUI.getCoordinatesByMousePos(MafViewer.getMousePositionClick()).x; 
	    			short y = (short) AppGUI.getCoordinatesByMousePos(MafViewer.getMousePositionClick()).y;        		
	    			byte colNum = (byte) AppGUI.getCellByMousePos(MafViewer.getMousePositionClick()).x;
	    			byte rowNum = (byte) AppGUI.getCellByMousePos(MafViewer.getMousePositionClick()).y;
	    			  
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
					
					if (MafViewer.frame.getName().equals("update")){				
						DBManager.updateMaf(maf);						
						MafHashKey key = new MafHashKey(colNum, rowNum);
			      		if (AppGUI.getAllMafs().containsKey(key)){
			      			ArrayList<MafHashValue> list = AppGUI.getAllMafs().get(key);
			      			MafHashValue coord = new MafHashValue(x, y, maf.getMafMark());
			      	  			for (MafHashValue value:list)
			      	  				if (coord.equals(value)){
			      	  					value.setFull(maf.getMafMark());
			      	  					break;
			      	  				}
			      		}				
			    		AppGUI.setClickedMaf(maf);	
						AppGUI.paintClickedMaf(maf.getMafMark(),false);
					}
					else{									
						DBManager.insertMaf(maf);
						AppGUI.setClickedMaf(maf);	
						AppGUI.paintClickedMaf(maf.getMafMark(),true);
					}	
					MafViewer.closeMafViewer();
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