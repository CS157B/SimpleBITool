package src.utility;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Windows {
	
	public void Dimension()
	{
		JFrame dframe = new JFrame("Dimensions");
	    int windowWidth = 400;           // Window width in pixels
	    int windowHeight = 150;          // Window height in pixels
	    dframe.setBounds(50, 100,       // Set position
	         windowWidth, windowHeight);  // and size
	    JButton button = new JButton("Submit");
	    button.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	           
	        	dframe.dispose();
	        }
	    });
	    dframe.setLayout(new GridLayout(3, 2));
	    
	    dframe.add(new JLabel("Table Name:"));
	    dframe.add(new JTextField());
	    dframe.add(new JLabel("Initial Concept Column Name:"));
	    dframe.add(new JTextField());
	    dframe.add(button);
	    
	    dframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    dframe.setVisible(true);        // Display the window
	    
	    
	}
	
	public void Concept()
	{
		JFrame cframe = new JFrame("Concept");
	    int windowWidth = 400;           // Window width in pixels
	    int windowHeight = 150;          // Window height in pixels
	    cframe.setBounds(50, 100,       // Set position
	         windowWidth, windowHeight);  // and size
	    JButton button = new JButton("Submit");
	    button.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	           
	        	cframe.dispose();
	        }
	    });
	    cframe.setLayout(new GridLayout(3, 2));
	    cframe.add(new JLabel("Concept Level:"));
	    cframe.add(new JTextField());
	    cframe.add(new JLabel("Column Name:"));
	    cframe.add(new JTextField());
	    cframe.add(button);
	    
	    
	    cframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    cframe.setVisible(true);        // Display the window
		
	}
	
	public void Filter()
	{
		JFrame fframe = new JFrame("Filter");
		int windowWidth = 400;           // Window width in pixels
	    int windowHeight = 150;          // Window height in pixels
	    fframe.setBounds(50, 100,       // Set position
	         windowWidth, windowHeight);  // and size
	    
	    JButton button = new JButton("Submit");
	    button.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	           
	        	fframe.dispose();
	        }
	    });
		String[] tables = { "Time", "Product", "Store" };
		String[] time = {"Year", "Month", "Week"};
 		String[] store = {"store_street_address","store_zip", "store_city","store_county","store_state"};
 		String[] product = {"department","category","subCategory"};
 		String[] operator = {"=", ">", "<", "<=", ">="};
 		final JComboBox column = new JComboBox();

		//Create the combo box, select item at index 4.
		//Indices start at 0, so 4 specifies the pig.
		JComboBox tlist = new JComboBox(tables);
		tlist.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
		        //JComboBox cb = (JComboBox)e.getSource();
				column.removeAllItems();
		        String tablename = (String)tlist.getSelectedItem();
		        if(tablename.equals("Time")){
		        	for(int i = 0;i<time.length;i++){
		        		column.addItem(time[i]);
		        	}
		        }
		        if(tablename.equals("Store")){
		        	for(int i = 0;i<store.length;i++){
		        		column.addItem(store[i]);
		        	}
		        }
		        if(tablename.equals("Product")){
		        	for(int i = 0;i<product.length;i++){
		        		column.addItem(product[i]);
		        	}
		        }
		        
		        
		        
		    }
		});
		
		/*switch((String)tlist.getSelectedItem()){
		
		case "Time": column  = new JComboBox(time); break;
		case "Store": column  = new JComboBox(store); break;
		case "Product": column  = new JComboBox(product); break;
		
		}*/
		
		fframe.setLayout(new GridLayout(5,2));
		JComboBox oper = new JComboBox(operator);
		fframe.add(new JLabel("Table Name"));fframe.add(tlist);
		fframe.add(new JLabel("Column Name"));fframe.add(column);
		fframe.add(new JLabel("Operator"));fframe.add(oper);
		fframe.add(new JLabel("Operand"));fframe.add(new JTextField());
		
		
		
	    
	    fframe.add(button);
	    
	    fframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    fframe.setVisible(true);        // Display the window
	    
	}
	
	public static void main(String args[]){
		Windows win = new Windows();
		win.Dimension();
		win.Concept();
		win.Filter();
	}
	


}
