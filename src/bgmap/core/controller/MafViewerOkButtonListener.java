package bgmap.core.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import bgmap.core.AppConfig;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;
import bgmap.core.view.*;

public class MafViewerOkButtonListener implements ActionListener{
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		try{
			if (e.getActionCommand().equals("delete")){
				 if (JOptionPane.showConfirmDialog(AppGUI.mainFrame, 
			    		  "Вы действительно хотите удалить объект?", 
			    		  "Удаление объекта",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			    			   		  
			    			  short x = (short) ((MafViewer.getMousePositionClick().x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
			    			  short y = (short) ((MafViewer.getMousePositionClick().y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight);        		
			    			  byte colNum = (byte) (Map.getStartCol() + (MafViewer.getMousePositionClick().x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
			    			  byte rowNum = (byte) (Map.getStartRow() + (MafViewer.getMousePositionClick().y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
			    			  DBManager.deleteMaf(x, y, colNum, rowNum);
			    			  MafHashKey key = new MafHashKey(colNum, rowNum);
			    			  if (AppGUI.getAllMafs().containsKey(key)){
			    				  ArrayList<MafHashValue> list = AppGUI.getAllMafs().get(key);
			    				  if (list.size() == 1){
			    					  AppGUI.getAllMafs().remove(key);	      				
			    				  }
			    				  else {
			    					  MafHashValue coord = new MafHashValue(x, y, (byte) 2);
			    					  for (MafHashValue value:list)
			    						  if (coord.equals(value)){
			    							  list.remove(value);	      	  					
			    							  break;
			    						  }
			    				  }
			    				  if (AppGUI.getClickedMaf() != null)
			    					  AppGUI.setClickedMaf(null);
			    				  AppGUI.loadPartsMap(Map.getPngScale(), 
			    						  colNum, rowNum, (byte) 1, (byte) 1,
			    						  Map.getMapOffset().x + (colNum-Map.getStartCol())*Map.partMapWidth,
			    						  Map.getMapOffset().y + (rowNum-Map.getStartRow())*Map.partMapHeight);
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
					short x = (short) ((MafViewer.getMousePositionClick().x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
					short y = (short) ((MafViewer.getMousePositionClick().y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight);        		
					byte colNum = (byte) (Map.getStartCol() + (MafViewer.getMousePositionClick().x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
					byte rowNum = (byte) (Map.getStartRow() + (MafViewer.getMousePositionClick().y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
				
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
			      		if (AppGUI.getAllMafs().containsKey(key)){
			      			ArrayList<MafHashValue> list = AppGUI.getAllMafs().get(key);
			      			MafHashValue coord = new MafHashValue(x, y, maf.getMafMark());
			      	  			for (MafHashValue value:list)
			      	  				if (coord.equals(value)){
			      	  					value.setFull(maf.getMafMark());
			      	  					break;
			      	  				}
			      		}		      	 
			      	if (AppGUI.getClickedMaf() != null)
			      		AppGUI.setClickedMaf(maf);		      	
					}
					else{			
						DBManager.insertMaf(maf);
						AppGUI.setClickedMaf(maf);						
						AppGUI.paintClickedMaf(AppConfig.MafsMarks[maf.getMafMark()],true);
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