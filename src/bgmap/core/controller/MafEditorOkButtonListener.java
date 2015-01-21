package bgmap.core.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import bgmap.core.AppConfig;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;
import bgmap.core.view.AppGUI;
import bgmap.core.view.MafViewer;

public class MafEditorOkButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		MafViewer.isOpen=false;
		try{
			Maf maf;
			if (MafViewer.subjectName.getText().isEmpty())  {
				JOptionPane.showMessageDialog(MafViewer.frame,
					    "Поле `"+Maf.SubjectNameField+"` должно быть указано",
					    "fam editor error",
					    JOptionPane.ERROR_MESSAGE);
			}
			else{
				short x = (short) ((MafViewer.pos.x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
				short y = (short) ((MafViewer.pos.y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight);        		
				byte colNum = (byte) (Map.getStartCol() + (MafViewer.pos.x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
				byte rowNum = (byte) (Map.getStartRow() + (MafViewer.pos.y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
				System.out.println("xy="+x+","+y);
				
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
				System.out.println(e.getActionCommand());
				if (e.getActionCommand().equals("update")){
					DBManager.updateMaf(maf);
					System.out.println("update");
					if (maf.isFull())
						AppGUI.paintMaf(AppConfig.signFull,false,maf);
					else 
						AppGUI.paintMaf(AppConfig.sign,false,maf);					
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
		} catch (SQLException ex){
			JOptionPane.showMessageDialog(MafViewer.frame,
				    "Ошибка сохранения информации",
				    "fam editor error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

}