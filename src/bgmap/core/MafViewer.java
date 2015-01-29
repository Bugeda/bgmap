package bgmap.core;

import java.awt.*;

import javax.swing.*;

import bgmap.*;
import bgmap.core.controller.*;
import bgmap.core.model.Maf;
import bgmap.core.model.Map;
import static bgmap.core.model.Maf.*;

public class MafViewer extends JFrame {

	public static JDialog frame;	
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
    private static JLabel subjectNameLabel = new JLabel("<html><div align=center>" + SubjectNameField + "</div></html>", SwingConstants.CENTER);
    private static JLabel subjectAddressLabel = new JLabel("<html><div align=center>" + ObjectAddressField + "</div></html>", SwingConstants.CENTER);
    private static JLabel subjectRegNumLabel = new JLabel("<html><div align=center>" + SubjectRegNumField + "</div></html>", SwingConstants.CENTER);
    private static JLabel telephoneLabel = new JLabel("<html><div align=center>" + TelephoneField + "</div></html>", SwingConstants.CENTER);    
    private static JLabel siteLabel = new JLabel("<html><div align=center>" + SiteField + "</div></html>", SwingConstants.CENTER);
    private static JLabel purposeLabel = new JLabel("<html><div align=center>" + PurposeField + "</div></html>", SwingConstants.CENTER);
    private static JLabel objectAddressLabel = new JLabel("<html><div align=center>" + ObjectAddressField + "</div></html>", SwingConstants.CENTER);
    private static JLabel techCharacteristicsLabel = new JLabel("<html> <div align=center>" + TechCharacteristicsField + "</div></html>", SwingConstants.CENTER);
    private static JLabel passportLabel = new JLabel("<html><div align=center>" + PassportField + "</div></html>", SwingConstants.CENTER);
    private static JLabel personFullNameLabel = new JLabel("<html><div align=center>" + PersonFullNameField + "</div></html>", SwingConstants.CENTER);  
	private static Color color = new Color(238,238,238,100);
	private static Point mousePositionClick = null;

    private static boolean isOpen = false;    
    
	public static Point getMousePositionClick() {
		return MafViewer.mousePositionClick;
	}
       
    private static void initEditorFrame(){
        AppGUI.mainFrame.setEnabled(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.addWindowListener(Cancelistener);        
        frame.setSize(AppGUI.mapPanel.getSize().width/4, 
        		(int) (AppGUI.mainFrame.getOpacity() + AppGUI.mapPanel.getSize().height));
                
        Point p = new Point((int)(AppGUI.mainFrame.getX() +  AppGUI.mainFrame.getOpacity() + AppGUI.mainFrame.getInsets().left), 
        		(int)(AppGUI.mainFrame.getY() + AppGUI.mainFrame.getOpacity() + AppGUI.mainFrame.getInsets().top));
     
        if (AppGUI.mapPanel.getMousePosition().x < AppGUI.mapPanel.getSize().width/2)
        	p.x = (int)(AppGUI.mainFrame.getX() + AppGUI.mainFrame.getOpacity() + AppGUI.mapPanel.getSize().width - frame.getSize().width);
        
        frame.setLocation(p);
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        frame.setVisible(true);		
        isOpen=true;
        AppGUI.slider.setValue(100);
        Map.setScrollable(false);
    }
    
    private static void initViewerFrame(){  	
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);       
        frame.setUndecorated(true);
        frame.setBackground(color);
//        frame.setResizable(false);
        frame.addWindowListener(Cancelistener);	        
        frame.setSize(300, 250); 
        frame.setLocation(MouseInfo.getPointerInfo().getLocation());
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        frame.setVisible(true);		
        isOpen=true;        
        AppGUI.slider.setValue(100);
        Map.setScrollable(false);
    }
    public static void closeMafViewer(){
    	closeMafViewer("");
    } 
    public static void closeMafViewer(String param){
    	if (isOpen){    		
    		if (MafViewer.frame.getName().equals("insert")&&(param.equals("cancel"))){
    			AppGUI.eraseInsertedMark();
    		}
    		AppGUI.mainFrame.setEnabled(true);
			MafViewer.frame.dispose();   		
			isOpen=false;
			Map.setScrollable(true);
    	}
	}
    
	public static void createEditor() {
		if (!isOpen){
			frame = new JDialog(AppGUI.mainFrame,"Добавить МАФ");
			
			frame.setName("insert");
			mousePositionClick = AppGUI.mapPanel.getMousePosition();   
									
			AppGUI.paintInsertMark();					
			
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
			        
		    mousePositionClick=AppGUI.mapPanel.getMousePosition();      
			
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
			frame = new JDialog(AppGUI.mainFrame,"Изменить МАФ");
			
			frame.setName("update");
			subjectName = new JTextField(AppGUI.getClickedMaf().getSubjectName());
			subjectName.addActionListener(OkButtonListener);
			subjectName.addKeyListener(Cancelistener);	
		
			subjectAddress = new JTextField(AppGUI.getClickedMaf().getSubjectAddress());
			subjectAddress.addActionListener(OkButtonListener);
			subjectAddress.addKeyListener(Cancelistener);
	
			subjectRegNum = new DecimalTextField(AppGUI.getClickedMaf().getSubjectRegNum());	    
		    subjectRegNum.addActionListener(OkButtonListener);
		    subjectRegNum.addKeyListener(Cancelistener);
		    
		    telephone = new JTextField(AppGUI.getClickedMaf().getTelephone());
		    telephone.addActionListener(OkButtonListener);
		    telephone.addKeyListener(Cancelistener);
	
		    site = new JFormattedTextField(new RegexFormatter("www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
		    site.setText(AppGUI.getClickedMaf().getSite());
		    site.addActionListener(OkButtonListener);
		    site.addKeyListener(Cancelistener);

		    purpose = new JTextField(AppGUI.getClickedMaf().getPurpose());
		    purpose.addActionListener(OkButtonListener);
		    purpose.addKeyListener(Cancelistener);

		    objectAddress = new JTextField(AppGUI.getClickedMaf().getObjectAddress());
		    objectAddress.addKeyListener(Cancelistener);
		    objectAddress.addActionListener(OkButtonListener);

		    techCharacteristics = new JTextField(AppGUI.getClickedMaf().getTechCharacteristics());
		    techCharacteristics.addActionListener(OkButtonListener);
		    techCharacteristics.addKeyListener(Cancelistener);
		        
		    passport = new JTextField(AppGUI.getClickedMaf().getPassport());
		    passport.addActionListener(OkButtonListener);
		    passport.addKeyListener(Cancelistener);
	
		    personFullName = new JTextField(AppGUI.getClickedMaf().getPersonFullName());
		    personFullName.addActionListener(OkButtonListener);
		    personFullName.addKeyListener(Cancelistener);
				    
		    mousePositionClick = new Point(AppGUI.getClickedMaf().getX() + (AppGUI.getClickedMaf().getColNum()-Map.getStartCol())*Map.partMapWidth + Map.getMapPos().x + Map.getMapOffset().x,
					AppGUI.getClickedMaf().getY() + (AppGUI.getClickedMaf().getRowNum()-Map.getStartRow())*Map.partMapHeight + Map.getMapPos().y + Map.getMapOffset().y);			

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
				frame =  new JDialog(AppGUI.mainFrame,"Информация о МАФе");
				
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
					    
			    mousePositionClick = new Point(AppGUI.getClickedMaf().getX() + (AppGUI.getClickedMaf().getColNum()-Map.getStartCol())*Map.partMapWidth + Map.getMapPos().x + Map.getMapOffset().x,
						AppGUI.getClickedMaf().getY() + (AppGUI.getClickedMaf().getRowNum()-Map.getStartRow())*Map.partMapHeight + Map.getMapPos().y + Map.getMapOffset().y);			

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
