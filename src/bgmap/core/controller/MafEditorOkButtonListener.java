package bgmap.core.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import bgmap.core.AppConfig;
import bgmap.core.model.*;
import bgmap.core.model.dao.DBManager;
import bgmap.core.view.AppGUI;
import bgmap.core.view.MafEditor;

public class MafEditorOkButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		try{

		Maf maf;
		if (MafEditor.subjectName.getText().isEmpty() || MafEditor.subjectAddress.getText().isEmpty() || 
				MafEditor.subjectRegNum.getText().isEmpty() || MafEditor.telephone.getText().isEmpty() ||  
				MafEditor.site.getText().isEmpty() || MafEditor.purpose.getText().isEmpty() || 
				MafEditor.objectAddress.getText().isEmpty() || MafEditor.techCharacteristics.getText().isEmpty() ||
				MafEditor.passport.getText().isEmpty() || MafEditor.personFullName.getText().isEmpty() )  {
			JOptionPane.showMessageDialog(MafEditor.frame,
				    "all fields should be filled",
				    "fam editor error",
				    JOptionPane.ERROR_MESSAGE);
		}
		else{
		short x = (short) ((MafEditor.pos.x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
		short y = (short) ((MafEditor.pos.y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight);        		
		byte colNum = (byte) (Map.getStartCol() + (MafEditor.pos.x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
		byte rowNum = (byte) (Map.getStartRow() + (MafEditor.pos.y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
		System.out.println("xy="+x+","+y);
		maf = new Maf(x,y,colNum,rowNum,MafEditor.subjectName.getText(), MafEditor.subjectAddress.getText(), Integer.parseInt(MafEditor.subjectRegNum.getText()), 
				MafEditor.telephone.getText(), MafEditor.site.getText(), MafEditor.purpose.getText(), 
				MafEditor.objectAddress.getText(),MafEditor.techCharacteristics.getText(),
				MafEditor.passport.getText(),MafEditor.personFullName.getText());
		if (e.getActionCommand().equals("update")){
			DBManager.updateMaf(maf);
			AppGUI.paintMaf(AppConfig.signOn,false,maf);
			}
		else{
			DBManager.insertMaf(maf);
			AppGUI.paintMaf(AppConfig.signFull,true,maf);
		}

		AppGUI.mainFrame.setEnabled(true);
		MafEditor.frame.dispose();   
		}
		} catch (NumberFormatException | SQLException ex){
			JOptionPane.showMessageDialog(MafEditor.frame,
				    "illegal symbols in subjectRegNum field",
				    "fam editor error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

}