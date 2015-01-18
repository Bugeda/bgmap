package bgmap.core.view;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
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
import bgmap.core.model.Maf;
import bgmap.core.model.Map;


public class MafEditor extends JFrame {

	public static JFrame frame;	
    public static JTextField subjectName;
    private static JLabel subjectNameLabel = new JLabel("полное наименование субъекта ведения хозяйства");
    public static JTextField subjectAddress;
    private static JLabel subjectAddressLabel = new JLabel("адрес субъекта ведения хозяйства");
    public static JTextField subjectRegNum;
    private static JLabel subjectRegNumLabel = new JLabel("номер государственной регистрации субъекта ведения хозяйства");
    public static JTextField telephone;
    private static JLabel telephoneLabel = new JLabel("телефон");
    public static JFormattedTextField site;    
    private static JLabel siteLabel = new JLabel("электронный адрес");
    public static JTextField purpose;
    private static JLabel purposeLabel = new JLabel("назначение малой архитектурной формы");
    public static JTextField objectAddress;
    private static JLabel objectAddressLabel = new JLabel("адрес или приблизительное желаемое место размещения");
    public static JTextField techCharacteristics; 
    private static JLabel techCharacteristicsLabel = new JLabel("<html>Технические характеристики малой архитектурной формы ( стационарная или передвижная, павильон, киоск, временное устройство для сезонной торговли, размеры, площадь, кв. метров)</html>");
    public static JTextField passport;
    private static JLabel passportLabel = new JLabel("Дата и номер паспорта привязки");
    public static JTextField personFullName;
    private static JLabel personFullNameLabel = new JLabel("Фамилия, имя и отчество уполномоченного лица");
    static public Point pos = null;
    
    static MafEditorOkButtonListener OkButtonListener= new MafEditorOkButtonListener();
      
    static MafEditorCancelListener Cancelistener= new MafEditorCancelListener();
       
	public static void createEditor() {
		frame = new JFrame("Добавить МАФ");
		
		subjectName = new JTextField();
		subjectName.addActionListener(OkButtonListener);
		subjectName.addKeyListener(Cancelistener);	    
	
		subjectAddress = new JTextField();
		subjectAddress.addActionListener(OkButtonListener);
		subjectAddress.addKeyListener(Cancelistener);
		
	    subjectRegNum = new JTextField();
	    subjectRegNum.addActionListener(OkButtonListener);
	    subjectRegNum.addKeyListener(Cancelistener);
	    
	    telephone = new JTextField();
	    telephone.addActionListener(OkButtonListener);
	    telephone.addKeyListener(Cancelistener);
	    
	    site = new JFormattedTextField(new RegexFormatter("www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
	    site.addActionListener(OkButtonListener);
	    site.addKeyListener(Cancelistener);
	    
	    purpose = new JTextField();
	    purpose.addActionListener(OkButtonListener);
	    purpose.addKeyListener(Cancelistener);
	    
	    objectAddress = new JTextField();
	    objectAddress.addKeyListener(Cancelistener);
	    objectAddress.addActionListener(OkButtonListener);
	    
	    techCharacteristics = new JTextField();
	    techCharacteristics.addActionListener(OkButtonListener);
	    techCharacteristics.addKeyListener(Cancelistener);
	            
	    passport = new JTextField();
	    passport.addActionListener(OkButtonListener);
	    passport.addKeyListener(Cancelistener);
	
	    personFullName = new JTextField();
	    personFullName.addActionListener(OkButtonListener);
	    personFullName.addKeyListener(Cancelistener);
	 
		        
	    JButton okButton = new JButton("Ok");
	    okButton.setActionCommand("insert");
	    okButton.addActionListener(OkButtonListener);
	    okButton.addKeyListener(Cancelistener);
	    
		pos = AppGUI.mapPanel.getMousePosition();

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        AppGUI.mainFrame.setEnabled(false);
        frame.addWindowListener(Cancelistener);
        
        frame.setSize(500, 500);
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel(); 

        JButton cancelButton = new JButton("cancel");
        cancelButton.addActionListener(Cancelistener);        
        cancelButton.addKeyListener(Cancelistener);
        
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
	
	public static void editMaf(Maf maf){
		frame = new JFrame("Изменить МАФ");
		
		subjectName = new JTextField(maf.getSubjectName());
		subjectName.addActionListener(OkButtonListener);
		subjectName.addKeyListener(Cancelistener);	
		subjectName.setActionCommand("update");
		JLabel subjectNameLabel = new JLabel("полное наименование субъекта ведения хозяйства");
		subjectAddress = new JTextField(maf.getSubjectAddress());
		subjectAddress.addActionListener(OkButtonListener);
		subjectAddress.addKeyListener(Cancelistener);
		subjectAddress.setActionCommand("update");
		JLabel subjectAddressLabel = new JLabel("адрес субъекта ведения хозяйства");
	    subjectRegNum = new JTextField(String.valueOf(maf.getSubjectRegNum()));	    
	    subjectRegNum.addActionListener(OkButtonListener);
	    subjectRegNum.addKeyListener(Cancelistener);
	    subjectAddress.setActionCommand("update");
	    JLabel subjectRegNumLabel = new JLabel("номер государственной регистрации субъекта ведения хозяйства");
	    telephone = new JTextField(maf.getTelephone());
	    telephone.addActionListener(OkButtonListener);
	    telephone.addKeyListener(Cancelistener);
	    telephone.setActionCommand("update");
	    JLabel telephoneLabel = new JLabel("телефон");
	    site = new JFormattedTextField(new RegexFormatter("www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
	    site.setText(maf.getSite());
	    site.addActionListener(OkButtonListener);
	    site.addKeyListener(Cancelistener);
	    site.setActionCommand("update");
	    JLabel siteLabel = new JLabel("электронный адрес");
	    purpose = new JTextField(maf.getPurpose());
	    purpose.addActionListener(OkButtonListener);
	    purpose.addKeyListener(Cancelistener);
	    purpose.setActionCommand("update");
	    JLabel purposeLabel = new JLabel("назначение малой архитектурной формы");
	    objectAddress = new JTextField(maf.getObjectAddress());
	    objectAddress.addKeyListener(Cancelistener);
	    objectAddress.addActionListener(OkButtonListener);
	    objectAddress.setActionCommand("update");
	    JLabel objectAddressLabel = new JLabel("адрес или приблизительное желаемое место размещения");
	    techCharacteristics = new JTextField(maf.getTechCharacteristics());
	    techCharacteristics.addActionListener(OkButtonListener);
	    techCharacteristics.addKeyListener(Cancelistener);
	    techCharacteristics.setActionCommand("update");
	    JLabel techCharacteristicsLabel = new JLabel("<html>Технические характеристики малой архитектурной формы ( стационарная или передвижная, павильон, киоск, временное устройство для сезонной торговли, размеры, площадь, кв. метров)</html>");        
	    passport = new JTextField(maf.getPassport());
	    passport.addActionListener(OkButtonListener);
	    passport.addKeyListener(Cancelistener);
	    passport.setActionCommand("update");
	    JLabel passportLabel = new JLabel("Дата и номер паспорта привязки");
	    personFullName = new JTextField(maf.getPersonFullName());
	    personFullName.addActionListener(OkButtonListener);
	    personFullName.addKeyListener(Cancelistener);
	    personFullName.setActionCommand("update");
	    JLabel personFullNameLabel = new JLabel("Фамилия, имя и отчество уполномоченного лица");
		        
	    JButton okButton = new JButton("Ok");
	    okButton.setActionCommand("update");
	    okButton.addActionListener(OkButtonListener);
	    okButton.addKeyListener(Cancelistener);
	 /*   short x = (short) ((MafEditor.pos.x - Map.getMapPos().x - Map.getMapOffset().x) % Map.partMapWidth); 
		short y = (short) ((MafEditor.pos.y - Map.getMapPos().y - Map.getMapOffset().y)  % Map.partMapHeight);        		
		byte colNum = (byte) (Map.getStartCol() + (MafEditor.pos.x - Map.getMapPos().x - Map.getMapOffset().x) / Map.partMapWidth );
		byte rowNum = (byte) (Map.getStartRow() + (MafEditor.pos.y - Map.getMapPos().y - Map.getMapOffset().y) / Map.partMapHeight);
		*/System.out.println(maf.getX()+","+maf.getY());
		pos = new Point(maf.getX() + (maf.getColNum()-Map.getStartCol())*Map.partMapWidth + Map.getMapPos().x + Map.getMapOffset().x,
				maf.getY() + (maf.getRowNum()-Map.getStartRow())*Map.partMapHeight + Map.getMapPos().y + Map.getMapOffset().y);
		System.out.println(pos);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        AppGUI.mainFrame.setEnabled(false);
        frame.addWindowListener(Cancelistener);
        
        frame.setSize(500, 500);
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel(); 

        JButton cancelButton = new JButton("cancel");
        cancelButton.addActionListener(Cancelistener);        
        cancelButton.addKeyListener(Cancelistener);
        
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
