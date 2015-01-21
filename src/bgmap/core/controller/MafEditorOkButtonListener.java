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
		maf = new Maf(x,y,colNum,rowNum,MafViewer.subjectName.getText(), MafViewer.subjectAddress.getText(), MafViewer.subjectRegNum.getDecimal(), 
				MafViewer.telephone.getText(), MafViewer.site.getText(), MafViewer.purpose.getText(), 
				MafViewer.objectAddress.getText(),MafViewer.techCharacteristics.getText(),
				MafViewer.passport.getText(),MafViewer.personFullName.getText());
		
		if (e.getActionCommand().equals("update")){
			DBManager.updateMaf(maf);
			AppGUI.paintMaf(AppConfig.signOn,false,maf);
			}
		else{
			DBManager.insertMaf(maf);
			AppGUI.paintMaf(AppConfig.signFull,true,maf);
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