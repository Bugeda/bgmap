package bgmap.core.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bgmap.core.DecimalTextField;
import bgmap.core.RegexFormatter;
import bgmap.core.controller.MafViewerCancelListener;
import bgmap.core.controller.MafViewerOkButtonListener;
import bgmap.core.model.Maf;
import bgmap.core.model.Map;

public class MafViewer extends JFrame {

	public static JFrame frame;	
	public static JTextField subjectName;
	public static JTextField subjectAddress;
	public static DecimalTextField subjectRegNum;
	public static JTextField telephone;
	public static JFormattedTextField site;  
	public static JTextField purpose; 
	public static JTextField objectAddress;
	public static JTextField techCharacteristics; 
	public static JTextField passport;
	public static JTextField personFullName;
	
    private static MafViewerOkButtonListener OkButtonListener= new MafViewerOkButtonListener();      
    private static MafViewerCancelListener Cancelistener= new MafViewerCancelListener(); 		
    private static JLabel subjectNameLabel = new JLabel("<html><div align=center>" + Maf.SubjectNameField + "</div></html>", SwingConstants.CENTER);
    private static JLabel subjectAddressLabel = new JLabel("<html><div align=center>" + Maf.ObjectAddressField + "</div></html>", SwingConstants.CENTER);
    private static JLabel subjectRegNumLabel = new JLabel("<html><div align=center>" + Maf.SubjectRegNumField + "</div></html>", SwingConstants.CENTER);
    private static JLabel telephoneLabel = new JLabel("<html><div align=center>" + Maf.TelephoneField + "</div></html>", SwingConstants.CENTER);    
    private static JLabel siteLabel = new JLabel("<html><div align=center>" + Maf.SiteField + "</div></html>", SwingConstants.CENTER);
    private static JLabel purposeLabel = new JLabel("<html><div align=center>" + Maf.PurposeField + "</div></html>", SwingConstants.CENTER);
    private static JLabel objectAddressLabel = new JLabel("<html><div align=center>" + Maf.ObjectAddressField + "</div></html>", SwingConstants.CENTER);
    private static JLabel techCharacteristicsLabel = new JLabel("<html> <div align=center>" + Maf.TechCharacteristicsField + "</div></html>", SwingConstants.CENTER);
    private static JLabel passportLabel = new JLabel("<html><div align=center>" + Maf.PassportField + "</div></html>", SwingConstants.CENTER);
    private static JLabel personFullNameLabel = new JLabel("<html><div align=center>" + Maf.PersonFullNameField + "</div></html>", SwingConstants.CENTER);  
	private static Color color = new Color(238,238,238,100);
	
    private static boolean isOpen = false;
    private static Point pos = null;
    
	public static boolean isOpen() {
		return MafViewer.isOpen;
	}

	public static void setOpen(boolean isOpen) {
		MafViewer.isOpen = isOpen;
	}
	
	public static Point getPos() {
		return MafViewer.pos;
	}
	public static void setPos(Point pos) {
		MafViewer.pos = pos;
	}
       
    private static void initEditorFrame(){
        AppGUI.mainFrame.setEnabled(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);        
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.addWindowListener(Cancelistener);        
        frame.setSize(AppGUI.mapPanel.getSize().width/4, 
        		(int) (AppGUI.mainFrame.getOpacity() + AppGUI.mapPanel.getSize().height));
        
        int h = AppGUI.adminPanel !=null ? AppGUI.adminPanel.getHeight() : 0; 
        Point p = new Point((int)(AppGUI.mainFrame.getX() +  AppGUI.mainFrame.getOpacity() + AppGUI.mainFrame.getInsets().left), 
        		(int)(AppGUI.mainFrame.getY() + AppGUI.mainFrame.getOpacity() + AppGUI.mainFrame.getInsets().top + h));
     
        if (AppGUI.mapPanel.getMousePosition().x < AppGUI.mapPanel.getSize().width/2)
        	p.x = (int)(AppGUI.mainFrame.getX() + AppGUI.mainFrame.getOpacity() + AppGUI.mapPanel.getSize().width - frame.getSize().width);
        
        frame.setLocation(p);
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        frame.setVisible(true);		
        isOpen=true;
    }
    
    private static void initViewerFrame(){  	
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setBackground(color);
//        frame.setResizable(false);
        frame.addWindowListener(Cancelistener);	        
        frame.setSize(300, 250); 
        frame.setLocation(MouseInfo.getPointerInfo().getLocation());
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        frame.setVisible(true);		
        isOpen=true;        
    }
    
    public static void closeMafViewer(){
    	if (isOpen){
    		AppGUI.mainFrame.setEnabled(true);
			MafViewer.frame.dispose();   		
			isOpen=false;
    	}
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
			        
			pos=AppGUI.mapPanel.getMousePosition();      
			
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
		    initEditorFrame();
		}		
	}
	
	public static void editClickedMaf(){
		if (!isOpen){
			frame = new JFrame("Изменить МАФ");
			
			subjectName = new JTextField(AppGUI.getClickedMaf().getSubjectName());
			subjectName.addActionListener(OkButtonListener);
			subjectName.addKeyListener(Cancelistener);	
			subjectName.setActionCommand("update");
			
			subjectAddress = new JTextField(AppGUI.getClickedMaf().getSubjectAddress());
			subjectAddress.addActionListener(OkButtonListener);
			subjectAddress.addKeyListener(Cancelistener);
			subjectAddress.setActionCommand("update");
	
			subjectRegNum = new DecimalTextField(AppGUI.getClickedMaf().getSubjectRegNum());	    
		    subjectRegNum.addActionListener(OkButtonListener);
		    subjectRegNum.addKeyListener(Cancelistener);
		    subjectAddress.setActionCommand("update");
		    
		    telephone = new JTextField(AppGUI.getClickedMaf().getTelephone());
		    telephone.addActionListener(OkButtonListener);
		    telephone.addKeyListener(Cancelistener);
		    telephone.setActionCommand("update");
	
		    site = new JFormattedTextField(new RegexFormatter("www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
		    site.setText(AppGUI.getClickedMaf().getSite());
		    site.addActionListener(OkButtonListener);
		    site.addKeyListener(Cancelistener);
		    site.setActionCommand("update");

		    purpose = new JTextField(AppGUI.getClickedMaf().getPurpose());
		    purpose.addActionListener(OkButtonListener);
		    purpose.addKeyListener(Cancelistener);
		    purpose.setActionCommand("update");

		    objectAddress = new JTextField(AppGUI.getClickedMaf().getObjectAddress());
		    objectAddress.addKeyListener(Cancelistener);
		    objectAddress.addActionListener(OkButtonListener);
		    objectAddress.setActionCommand("update");

		    techCharacteristics = new JTextField(AppGUI.getClickedMaf().getTechCharacteristics());
		    techCharacteristics.addActionListener(OkButtonListener);
		    techCharacteristics.addKeyListener(Cancelistener);
		    techCharacteristics.setActionCommand("update");
		        
		    passport = new JTextField(AppGUI.getClickedMaf().getPassport());
		    passport.addActionListener(OkButtonListener);
		    passport.addKeyListener(Cancelistener);
		    passport.setActionCommand("update");
	
		    personFullName = new JTextField(AppGUI.getClickedMaf().getPersonFullName());
		    personFullName.addActionListener(OkButtonListener);
		    personFullName.addKeyListener(Cancelistener);
		    personFullName.setActionCommand("update");			        
				    
			pos = new Point(AppGUI.getClickedMaf().getX() + (AppGUI.getClickedMaf().getColNum()-Map.getStartCol())*Map.partMapWidth + MapPanel.getPos().x + Map.getMapOffset().x,
					AppGUI.getClickedMaf().getY() + (AppGUI.getClickedMaf().getRowNum()-Map.getStartRow())*Map.partMapHeight + MapPanel.getPos().y + Map.getMapOffset().y);			

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
		    okButton.setActionCommand("update");
		    okButton.addActionListener(OkButtonListener);
		    okButton.addKeyListener(Cancelistener);		    

	        JButton cancelButton = new JButton("cancel");
	        cancelButton.addActionListener(Cancelistener);        
	        cancelButton.addKeyListener(Cancelistener);
	
	        JButton deleteButton = new JButton("delete");
	        deleteButton.setActionCommand("delete");
	        deleteButton.addActionListener(OkButtonListener);
	        deleteButton.addKeyListener(Cancelistener);		
		    
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(okButton);
	        buttonPanel.add(cancelButton);
	        buttonPanel.add(deleteButton);
	        
	        JPanel bottomPanel = new JPanel(); 
	        bottomPanel.add(buttonPanel);	
	 
	        frame.add(topPanel,BorderLayout.NORTH);
	        frame.add(bottomPanel);   
	        initEditorFrame();
		}	
	}
		
		
	public static void showClickedMafInfo(){
			if (!isOpen){
				frame = new JFrame("Информация о МАФе");
				
				JLabel subjectName = new JLabel(AppGUI.getClickedMaf().getSubjectName());								
				JLabel subjectAddress = new JLabel(AppGUI.getClickedMaf().getSubjectAddress());		
				JLabel subjectRegNum = new JLabel(Integer.toString(AppGUI.getClickedMaf().getSubjectRegNum()));	    			   
				JLabel telephone = new JLabel(AppGUI.getClickedMaf().getTelephone());
			    JLabel site = new JLabel(AppGUI.getClickedMaf().getSite());
			    JLabel purpose = new JLabel(AppGUI.getClickedMaf().getPurpose());
			    JLabel objectAddress = new JLabel(AppGUI.getClickedMaf().getObjectAddress());
			    JLabel techCharacteristics = new JLabel(AppGUI.getClickedMaf().getTechCharacteristics());		        
			    JLabel passport = new JLabel(AppGUI.getClickedMaf().getPassport());
			    JLabel personFullName = new JLabel(AppGUI.getClickedMaf().getPersonFullName());	        
					    
				pos = new Point(AppGUI.getClickedMaf().getX() + (AppGUI.getClickedMaf().getColNum()-Map.getStartCol())*Map.partMapWidth + MapPanel.getPos().x + Map.getMapOffset().x,
						AppGUI.getClickedMaf().getY() + (AppGUI.getClickedMaf().getRowNum()-Map.getStartRow())*Map.partMapHeight + MapPanel.getPos().y + Map.getMapOffset().y);			

		        JPanel topPanel = new JPanel();   		       
		        topPanel.add(subjectName);
		        subjectName.setFont(new Font(subjectName.getFont().getFontName(), Font.PLAIN, 15));
		        topPanel.setSize(300, 50);	
		        
		        JPanel bottomPanel = new JPanel();
		        bottomPanel.setSize(300, 200);	
		        GridBagLayout gbl = new GridBagLayout();
		        bottomPanel.setLayout(gbl);		        
		        GridBagConstraints c =  new GridBagConstraints();
		        c.anchor = GridBagConstraints.NORTHWEST; 
		        c.fill   = GridBagConstraints.NONE;  
		        c.gridheight = 1;
		        c.gridwidth  = GridBagConstraints.REMAINDER; 
		        c.gridx = 0; 
		        c.gridy = GridBagConstraints.RELATIVE; 
		        c.insets = new Insets(0, 10, 0, 10);
		        c.ipadx = 0;
		        c.ipady = 0;
		        c.weightx = 1.0;
		        c.weighty = 0.0;
		        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		        
		        gbl.setConstraints(subjectAddress, c);
		        bottomPanel.add(subjectAddress);		        
			    gbl.setConstraints(subjectRegNum, c);
		        bottomPanel.add(subjectRegNum);
		        gbl.setConstraints(telephone, c);
		        bottomPanel.add(telephone);
		        gbl.setConstraints(site, c);	
		        bottomPanel.add(site);
		        gbl.setConstraints(purpose, c);
		        bottomPanel.add(purpose);	    
		        gbl.setConstraints(objectAddress, c);	
		        bottomPanel.add(objectAddress);
		        gbl.setConstraints(techCharacteristics, c);	
		        bottomPanel.add(techCharacteristics);
		        gbl.setConstraints(passport, c);
		        bottomPanel.add(passport);
		        gbl.setConstraints(personFullName, c);
		        bottomPanel.add(personFullName);   	
		        
		        bottomPanel.setBackground(color);
		      
		        frame.add(topPanel, BorderLayout.NORTH);		
		        frame.add(bottomPanel);	
		        initViewerFrame();
			}	
	}


}