package bgmap.core.view;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bgmap.core.RegexFormatter;
import bgmap.core.controller.MafEditorCancelListener;
import bgmap.core.controller.MafEditorOkButtonListener;


public class MafEditor extends JFrame {

	public static JFrame frame = new JFrame("create object");	
    public static JTextField subjectName;
    public static JTextField subjectAddress;
    public static JTextField subjectRegNum;
    public static JTextField telephone;
    public static JFormattedTextField site;    
    public static JTextField purpose;
    public static JTextField objectAddress;
    public static JTextField techCharacteristics; 
    public static JTextField passport;
    public static JTextField personFullName;
    static public Point pos = null;
    
    static MafEditorOkButtonListener OkButtonListener= new MafEditorOkButtonListener();
      
    static MafEditorCancelListener Cancelistener= new MafEditorCancelListener();
       
	public static void createEditor() {
		subjectName = new JTextField();
		JLabel subjectNameLabel = new JLabel("полное наименование субъекта ведения хозяйства");
		subjectAddress = new JTextField();
	    JLabel subjectAddressLabel = new JLabel("адрес субъекта ведения хозяйства");
	    subjectRegNum = new JTextField();
	    JLabel subjectRegNumLabel = new JLabel("номер государственной регистрации субъекта ведения хозяйства");
	    telephone = new JTextField();
	    JLabel telephoneLabel = new JLabel("телефон");
	    site = new JFormattedTextField(new RegexFormatter("www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
	    JLabel siteLabel = new JLabel("электронный адрес");
	    purpose = new JTextField();
	    JLabel purposeLabel = new JLabel("назначение малой архитектурной формы");
	    objectAddress = new JTextField();
	    JLabel objectAddressLabel = new JLabel("адрес или приблизительное желаемое место размещения");
	    techCharacteristics = new JTextField();
	    JLabel techCharacteristicsLabel = new JLabel("<html>Технические характеристики малой архитектурной формы ( стационарная или передвижная, павильон, киоск, временное устройство для сезонной торговли, размеры, площадь, кв. метров)</html>");        
	    passport = new JTextField();
	    JLabel passportLabel = new JLabel("Дата и номер паспорта привязки");
	    personFullName = new JTextField();
	    JLabel personFullNameLabel = new JLabel("Фамилия, имя и отчество уполномоченного лица");
		        
	    JButton okButton = new JButton("Ok");
	    okButton.addActionListener(OkButtonListener);
	
	    
		pos = AppGUI.mapPanel.getMousePosition();

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        AppGUI.mainFrame.setEnabled(false);
        frame.addWindowListener(Cancelistener);
        
        frame.setSize(500, 500);
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
      //  frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
       
 
     
      
        
        

        JButton cancelButton = new JButton("cancel");
        cancelButton.addActionListener(Cancelistener);        
        
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
        frame.repaint();
        frame.setVisible(true);		
	}
}
