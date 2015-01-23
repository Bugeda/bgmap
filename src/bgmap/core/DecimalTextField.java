package bgmap.core;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class DecimalTextField extends JTextField {

	 public DecimalTextField() {
	    super();
	  }

	  public DecimalTextField(int subjectRegNum) {
		  super();
		  setDecimal(subjectRegNum);
	}

	public int getDecimal() {
		int res = 0;
	    try{	    	
	    	res = Integer.valueOf(this.getText());	    	
	    	} catch(NumberFormatException e) {	    		
	    }
	    return res;
	  }

	  public void setDecimal(Integer value) {
	    this.setText(value == null ? "" : value.toString());
	  }
	  
	  @Override
	  protected Document createDefaultModel() {
	    return new DecimalTextDocument();
	  }
	  
	  protected class DecimalTextDocument extends PlainDocument {
		 
		@Override  
	    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException  {	
			String insertStr = str.trim();
			String currentText = getText(0, getLength());
			String beforeOffset = currentText.substring(0, offs);
		    String afterOffset = currentText.substring(offs, currentText.length());
		    String proposedResult = beforeOffset + insertStr + afterOffset;
	        if (proposedResult.length() == 0)
	        	 super.insertString(offs, insertStr, a);
	        else {
	        	 try {	        				    
	        		 Integer.valueOf(proposedResult);
	        		 super.insertString(offs, insertStr, a);
	        } 	catch(NumberFormatException e) {
	        }
		     
		      
	      }
	    }

		@Override
	    public void remove(int offs, int len) throws BadLocationException {
			String currentText = getText(0, getLength());
		      String beforeOffset = currentText.substring(0, offs);
		      String afterOffset = currentText.substring(len + offs, currentText.length());
		      String proposedResult = beforeOffset + afterOffset;

		      if (proposedResult.length() == 0)
		        super.remove(offs, len);
		      else {
		        try {	 			
		 			Integer.valueOf(proposedResult);
		            super.remove(offs, len);
		        }
		        catch(NumberFormatException e) {
		        }
		      }
		}	    
	  }
}
