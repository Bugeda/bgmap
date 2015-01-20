package bgmap.core.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.apache.log4j.Layout;

import bgmap.core.DecimalTextField;
import bgmap.core.RegexFormatter;
import bgmap.core.controller.MafEditorCancelListener;
import bgmap.core.controller.MafEditorOkButtonListener;
import bgmap.core.model.Maf;
import bgmap.core.model.Map;


public class MafEditor extends JFrame {

	public static JFrame frame;	
    public static JTextField subjectName;
    private static JLabel subjectNameLabel = new JLabel(Maf.SubjectNameField,SwingConstants.CENTER);
    public static JTextField subjectAddress;
    private static JLabel subjectAddressLabel = new JLabel(Maf.ObjectAddressField);
    public static DecimalTextField subjectRegNum;
    private static JLabel subjectRegNumLabel = new JLabel(Maf.SubjectRegNumField);
    public static JTextField telephone;
    private static JLabel telephoneLabel = new JLabel(Maf.TelephoneField);
    public static JFormattedTextField site;    
    private static JLabel siteLabel = new JLabel(Maf.SiteField);
    public static JTextField purpose;
    private static JLabel purposeLabel = new JLabel(Maf.PurposeField);
    public static JTextField objectAddress;
    private static JLabel objectAddressLabel = new JLabel(Maf.ObjectAddressField);
    public static JTextField techCharacteristics; 
    private static JLabel techCharacteristicsLabel = new JLabel("<html> <div align=center>"+Maf.TechCharacteristicsField+"</div></html>");
    public static JTextField passport;
    private static JLabel passportLabel = new JLabel(Maf.PassportField);
    public static JTextField personFullName;
    private static JLabel personFullNameLabel = new JLabel(Maf.PersonFullNameField);
    static public Point pos = null;
    
    public static boolean isOpen = false;
    static MafEditorOkButtonListener OkButtonListener= new MafEditorOkButtonListener();
      
    static MafEditorCancelListener Cancelistener= new MafEditorCancelListener();
       
    private static void initFrame(){

        AppGUI.mainFrame.setEnabled(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.addWindowListener(Cancelistener);	        
        frame.setSize(500, 470); 
        frame.setLocation(MouseInfo.getPointerInfo().getLocation());
        frame.repaint();
        frame.setVisible(true);		
        isOpen=true;
    }
    
	public static void createEditor() {
		if (!isOpen){
			frame = new JFrame("Добавить МАФ");
			
			subjectName = new JTextField();
			subjectName.addActionListener(OkButtonListener);
			subjectName.addKeyListener(Cancelistener);	    
	          
			subjectAddress = new JTextField();
			subjectAddress.addActionListener(OkButtonListener);
			subjectAddress.addKeyListener(Cancelistener);
			
		    subjectRegNum = new DecimalTextField();
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
			        
			pos = AppGUI.mapPanel.getMousePosition();      
			
			JPanel topPanel = new JPanel();        
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
	        subjectNameLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(subjectNameLabel);
	        topPanel.add(subjectName);
	        subjectAddressLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(subjectAddressLabel);
	        topPanel.add(subjectAddress);
	        subjectRegNumLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(subjectRegNumLabel);
	        topPanel.add(subjectRegNum);
	        telephoneLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(telephoneLabel);
	        topPanel.add(telephone);
	        siteLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(siteLabel);
	        topPanel.add(site);
	        purposeLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(purposeLabel);
	        topPanel.add(purpose);
	        objectAddressLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(objectAddressLabel);
	        topPanel.add(objectAddress);
	        techCharacteristicsLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(techCharacteristicsLabel);
	        topPanel.add(techCharacteristics);
	        passportLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(passportLabel);
	        topPanel.add(passport);
	        personFullNameLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(personFullNameLabel);
	        topPanel.add(personFullName);   
	        
	        JButton okButton = new JButton("Ok");
		    okButton.setActionCommand("insert");
		    okButton.addActionListener(OkButtonListener);
		    okButton.addKeyListener(Cancelistener);		    

	        JButton cancelButton = new JButton("cancel");
	        cancelButton.addActionListener(Cancelistener);        
	        cancelButton.addKeyListener(Cancelistener);
	
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(okButton);
	        buttonPanel.add(cancelButton);
	        
	        JPanel bottomPanel = new JPanel(); 
	        bottomPanel.add(buttonPanel);	
	 
	        frame.add(topPanel,BorderLayout.NORTH);		        
		    frame.add(bottomPanel);   
	        initFrame();
		}		
	}
	
	public static void editMaf(Maf maf){
		if (!isOpen){
			frame = new JFrame("Изменить МАФ");
			
			subjectName = new JTextField(maf.getSubjectName());
			subjectName.addActionListener(OkButtonListener);
			subjectName.addKeyListener(Cancelistener);	
			subjectName.setActionCommand("update");
			
			subjectAddress = new JTextField(maf.getSubjectAddress());
			subjectAddress.addActionListener(OkButtonListener);
			subjectAddress.addKeyListener(Cancelistener);
			subjectAddress.setActionCommand("update");
	
			subjectRegNum = new DecimalTextField(maf.getSubjectRegNum());	    
		    subjectRegNum.addActionListener(OkButtonListener);
		    subjectRegNum.addKeyListener(Cancelistener);
		    subjectAddress.setActionCommand("update");
		    
		    telephone = new JTextField(maf.getTelephone());
		    telephone.addActionListener(OkButtonListener);
		    telephone.addKeyListener(Cancelistener);
		    telephone.setActionCommand("update");
	
		    site = new JFormattedTextField(new RegexFormatter("www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
		    site.setText(maf.getSite());
		    site.addActionListener(OkButtonListener);
		    site.addKeyListener(Cancelistener);
		    site.setActionCommand("update");

		    purpose = new JTextField(maf.getPurpose());
		    purpose.addActionListener(OkButtonListener);
		    purpose.addKeyListener(Cancelistener);
		    purpose.setActionCommand("update");

		    objectAddress = new JTextField(maf.getObjectAddress());
		    objectAddress.addKeyListener(Cancelistener);
		    objectAddress.addActionListener(OkButtonListener);
		    objectAddress.setActionCommand("update");

		    techCharacteristics = new JTextField(maf.getTechCharacteristics());
		    techCharacteristics.addActionListener(OkButtonListener);
		    techCharacteristics.addKeyListener(Cancelistener);
		    techCharacteristics.setActionCommand("update");
		        
		    passport = new JTextField(maf.getPassport());
		    passport.addActionListener(OkButtonListener);
		    passport.addKeyListener(Cancelistener);
		    passport.setActionCommand("update");
	
		    personFullName = new JTextField(maf.getPersonFullName());
		    personFullName.addActionListener(OkButtonListener);
		    personFullName.addKeyListener(Cancelistener);
		    personFullName.setActionCommand("update");			        
				    
			pos = new Point(maf.getX() + (maf.getColNum()-Map.getStartCol())*Map.partMapWidth + Map.getMapPos().x + Map.getMapOffset().x,
					maf.getY() + (maf.getRowNum()-Map.getStartRow())*Map.partMapHeight + Map.getMapPos().y + Map.getMapOffset().y);			

	        JPanel topPanel = new JPanel();        
	        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
	        subjectNameLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(subjectNameLabel);
	        topPanel.add(subjectName);
	        subjectAddressLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(subjectAddressLabel);
	        topPanel.add(subjectAddress);
	        subjectRegNumLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(subjectRegNumLabel);
	        topPanel.add(subjectRegNum);
	        telephoneLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(telephoneLabel);
	        topPanel.add(telephone);
	        siteLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(siteLabel);
	        topPanel.add(site);
	        purposeLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(purposeLabel);
	        topPanel.add(purpose);
	        objectAddressLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(objectAddressLabel);
	        topPanel.add(objectAddress);
	        techCharacteristicsLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(techCharacteristicsLabel);
	        topPanel.add(techCharacteristics);
	        passportLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(passportLabel);
	        topPanel.add(passport);
	        personFullNameLabel.setAlignmentX(CENTER_ALIGNMENT);
	        topPanel.add(personFullNameLabel);
	        topPanel.add(personFullName);   
	        
	        JButton okButton = new JButton("Ok");
		    okButton.setActionCommand("insert");
		    okButton.addActionListener(OkButtonListener);
		    okButton.addKeyListener(Cancelistener);		    

	        JButton cancelButton = new JButton("cancel");
	        cancelButton.addActionListener(Cancelistener);        
	        cancelButton.addKeyListener(Cancelistener);
	
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(okButton);
	        buttonPanel.add(cancelButton);
	        
	        JPanel bottomPanel = new JPanel(); 
	        bottomPanel.add(buttonPanel);	
	 
	        frame.add(topPanel,BorderLayout.NORTH);
	        frame.add(bottomPanel);   
	        initFrame();
		}	
	}
}
