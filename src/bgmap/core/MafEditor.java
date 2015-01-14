package bgmap.core;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;





import bgmap.core.entity.Maf;
import bgmap.core.entity.Map;

public class MafEditor extends JFrame {

	MapMouseAdapter movingAdapt = new MapMouseAdapter();
	
	public static void createEditor() {
	
		final Point pos = AppGUI.impanel.getMousePosition();
		final JFrame frame = new JFrame("create object");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        AppGUI.mainFrame.setEnabled(false);
        frame.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent e){           	  
            	  AppGUI.mainFrame.setEnabled(true);                
            	  }
              });
        
        frame.setSize(500, 500);
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
      //  frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
       
     
        JLabel subjectNameLabel = new JLabel("полное наименование субъекта ведения хозяйства");
        final JTextField subjectName = new JTextField();
       
        JLabel subjectAddressLabel = new JLabel("адрес субъекта ведения хозяйства");
        final JTextField subjectAddress = new JTextField();
       
        JLabel subjectRegNumLabel = new JLabel("номер государственной регистрации субъекта ведения хозяйства");
        final JTextField subjectRegNum = new JTextField();
       
        JLabel telephoneLabel = new JLabel("телефон");
        final JTextField telephone = new JTextField();
 
        JLabel siteLabel = new JLabel("электронный адрес");
        MaskFormatter siteFormatter = null;
        try {
        	siteFormatter = new MaskFormatter("http:");
		} catch (ParseException e1) {
			e1.printStackTrace();
			AppGUI.mainFrame.setEnabled(true);
    		frame.dispose();   
		}     
        final JFormattedTextField site = new JFormattedTextField(new RegexFormatter("www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
		// JFormattedTextField email = new JFormattedTextField(emailFormatter);
 
        JLabel purposeLabel = new JLabel("назначение малой архитектурной формы");
        final JTextField purpose = new JTextField();
  
        JLabel objectAddressLabel = new JLabel("адрес или приблизительное желаемое место размещения");
        final JTextField objectAddress = new JTextField(); 
 
        JLabel techCharacteristicsLabel = new JLabel("<html>Технические характеристики малой архитектурной формы ( стационарная или передвижная, павильон, киоск, временное устройство для сезонной торговли, размеры, площадь, кв. метров)</html>");        
        final JTextField techCharacteristics = new JTextField(); 

        JLabel passportLabel = new JLabel("Дата и номер паспорта привязки");
        final JTextField passport = new JTextField();

        JLabel personFullNameLabel = new JLabel("Фамилия, имя и отчество уполномоченного лица");
        final JTextField personFullName = new JTextField(); 

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent ae) {
    			
        		try{

        		Maf maf;
				if (subjectName.getText().isEmpty() || subjectAddress.getText().isEmpty() || 
        				subjectRegNum.getText().isEmpty() || telephone.getText().isEmpty() ||  
        				site.getText().isEmpty() || purpose.getText().isEmpty() || 
						objectAddress.getText().isEmpty() || techCharacteristics.getText().isEmpty() ||
						passport.getText().isEmpty() || personFullName.getText().isEmpty() )  {
        			JOptionPane.showMessageDialog(frame,
        				    "all fields should be filled",
        				    "fam editor error",
        				    JOptionPane.ERROR_MESSAGE);
        		}
        		else{
         		pos.x -= 2;
        		pos.y -= 3;
        		System.out.println(pos);
           		int x = (pos.x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth; 
        		int y = (pos.y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight;        		
        		byte colNum = (byte) (Map.getStartCol() + (pos.x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
        		byte rowNum = (byte) (Map.getStartRow() + (pos.y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
        		
				maf = new Maf(x,y,colNum,rowNum,subjectName.getText(), subjectAddress.getText(), Integer.parseInt(subjectRegNum.getText()), 
						telephone.getText(), site.getText(), purpose.getText(), 
						objectAddress.getText(),techCharacteristics.getText(),
						passport.getText(),personFullName.getText());
				DBManager.insertMaf(maf);
				MafPanel.paintMaf(maf);
				//AppGUI.mafPanels.add(new MafPanel(maf));
        		AppGUI.mainFrame.setEnabled(true);
        		frame.dispose();   
        		}
        		} catch (NumberFormatException | SQLException ex){
        			JOptionPane.showMessageDialog(frame,
        				    "illegal symbols in subjectRegNum field",
        				    "fam editor error",
        				    JOptionPane.ERROR_MESSAGE);
        		}
			}
        });

        JButton cancelButton = new JButton("cancel");
        cancelButton.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent ae) {        	
        		AppGUI.mainFrame.setEnabled(true);
        		frame.dispose();        		
        	}
        });
        
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(subjectNameLabel);
        topPanel.add(subjectName);
        topPanel.add(subjectAddressLabel);
        topPanel.add(subjectAddress);
        topPanel.add(subjectRegNumLabel);
        topPanel.add(subjectRegNum);
        topPanel.add(telephoneLabel);
        topPanel.add(telephone);
        topPanel.add(siteLabel);
        topPanel.add(site);
        topPanel.add(purposeLabel);
        topPanel.add(purpose);
        topPanel.add(objectAddressLabel);
        topPanel.add(objectAddress);
        topPanel.add(techCharacteristicsLabel);
        topPanel.add(techCharacteristics);
        topPanel.add(passportLabel);
        topPanel.add(passport);
        topPanel.add(personFullNameLabel);
        topPanel.add(personFullName);   
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        bottomPanel.add(buttonPanel);
        bottomPanel.setSize(500, 100);
 
        frame.add(topPanel,"North");
        frame.add(bottomPanel,"South");   
        frame.setLocation(MouseInfo.getPointerInfo().getLocation());
        frame.setVisible(true);		
	}
}
