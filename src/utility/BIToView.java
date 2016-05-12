package utility;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import olapGenerator.Cube;
import olapGenerator.Cube.Filter;
import olapGenerator.Cube.InvalidOperatorException;
import olapGenerator.Fact;
import olapGenerator.CubeDimension;

public class BIToView extends Frame {

	private JList dimensionBox;
	private JList conceptBox;
	private JList filterBox;
	private JTextArea text;
	private Fact fact;
	private Cube cube = new Cube();
	private CubeDimension dim;
	public DefaultListModel dimlistModel;
	public DefaultListModel conlistModel;
	public DefaultListModel fillistModel;
	public BIToView() {
		//cube.addDimension(d1);
		JFrame main = new JFrame("Simple Business Intelligence Tool");
		main.setLayout(new BorderLayout());
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setMaximumSize(new Dimension(1000,2000));

		// output Table View
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.setMaximumSize(new Dimension(100, 100));
		text = new JTextArea(50,50);
		text.setEditable(false);
		p1.add(text);
		main.add(p1, BorderLayout.WEST);

		JPanel p2 = new JPanel();
		
		// Dimensions
		JLabel dLabel = new JLabel("Dimensions:");
		dimlistModel = new DefaultListModel();
		conlistModel = new DefaultListModel();
		fillistModel = new DefaultListModel();
		
		dimensionBox = new JList(dimlistModel);
		dimensionBox.setVisibleRowCount(5);
		
        Box box1 = Box.createVerticalBox();
        Box box5 = Box.createVerticalBox();
		JButton dimAdd = new JButton("+");
		dimAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dimWindow();
				
	         }
		});
		JButton dimSub = new JButton("-");
		dimSub.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dimlistModel.removeElement((String)dimensionBox.getSelectedValue());
				cube.removeDimension((String)dimensionBox.getSelectedValue());
				conlistModel.removeAllElements();
				refreshTextarea();
	         }
		});
		JButton addfact = new JButton("Add a Fact");
		addfact.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				factWindow();
	         }
		});
		JScrollPane scrollPane_1 = new JScrollPane(dimensionBox);
		dimensionBox.setVisibleRowCount(4);
		dimensionBox.setFixedCellHeight(20);
		dimensionBox.setFixedCellWidth(60);
		box5.add(dLabel);
		box5.add(scrollPane_1);
		box1.add(dimAdd);
		box1.add(dimSub);
		p2.add(box5);
		p2.add(box1);
		p2.add(addfact);
		dimensionBox.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				if(dimensionBox.getSelectedValue()!=null)
				{
				String select = (String)dimensionBox.getSelectedValue();
				Map<Integer, String> concept =  cube.getDimension(select).getConceptHierarchy();
				conlistModel.removeAllElements();
				for(Map.Entry<Integer, String> entry: concept.entrySet()){
					conlistModel.addElement(entry.getValue());
				}
				int index = cube.getDimension(select).getConceptList().indexOf(cube.getDimension(select).getCurrentConcept());
				conceptBox.setSelectedIndex(index);
				}
			}
		});
		
		// concepts
		dLabel = new JLabel("Concept:");
        Box box2 = Box.createVerticalBox();
        Box box3 = Box.createVerticalBox();
        Box box6 = Box.createVerticalBox();
        
        conceptBox = new JList(conlistModel);
        conceptBox.setVisibleRowCount(5);
		JButton conAdd = new JButton("+");
		conAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				conceptWindow((String)dimensionBox.getSelectedValue());
			
	         }
		});
		JButton conSub = new JButton("-");
		conSub.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String select1 = (String)conceptBox.getSelectedValue();
				conlistModel.removeElement(select1);
				cube.getDimension((String)dimensionBox.getSelectedValue())
				.getConceptHierarchy().values().remove(select1);
				
	         }
		});
		JButton rollUp = new JButton("Roll Up");
		rollUp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String table = (String)dimensionBox.getSelectedValue();
				
				cube.getDimension(table).rollUp();
				int index = cube.getDimension(table).getConceptList().indexOf(cube.getDimension(table).getCurrentConcept());
				conceptBox.setSelectedIndex(index);
				refreshTextarea();
				
	         }
		});
		JButton drillDown = new JButton("Drill Down");
		drillDown.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String table = (String)dimensionBox.getSelectedValue();
				cube.getDimension(table).drillDown();
				int index = cube.getDimension(table).getConceptList().indexOf(cube.getDimension(table).getCurrentConcept());
				conceptBox.setSelectedIndex(index);
				refreshTextarea();
	         }
		});
		JScrollPane scrollPane_2 = new JScrollPane(conceptBox);
		conceptBox.setVisibleRowCount(4);
		conceptBox.setFixedCellHeight(20);
		conceptBox.setFixedCellWidth(60);
		box6.add(dLabel);
		box6.add(scrollPane_2);
		box2.add(conAdd);
		box2.add(conSub);
		box3.add(rollUp);
		box3.add(drillDown);
		p2.add(box6);
		p2.add(box2);
		p2.add(box3);
		
		//filters
		dLabel = new JLabel("Filter:");
		
		filterBox = new JList(fillistModel);
		filterBox.setVisibleRowCount(5);
        Box box4 = Box.createVerticalBox();
        Box box7 = Box.createVerticalBox();
		JButton filAdd = new JButton("+");
		filAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				filterWindow();
			
	         }
		});
		JButton filSub = new JButton("-");
		filSub.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				int indextoRemove = filterBox.getSelectedIndex();
				fillistModel.remove(indextoRemove);
				cube.removeFilter(indextoRemove);
			
	         }
		});
		JScrollPane scrollPane_3 = new JScrollPane(filterBox);
		filterBox.setVisibleRowCount(4);
		filterBox.setFixedCellHeight(20);
		filterBox.setFixedCellWidth(250);
		box7.add(dLabel);
		box7.add(scrollPane_3);
		box4.add(filAdd);
		box4.add(filSub);
		p2.add(box7);
		p2.add(box4);
		
		//Execute Button
		JPanel p3 = new JPanel();
		JButton execute = new JButton("Execute");
		p3.add(execute);
		execute.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String q = cube.generateCubeSQLString();
				text.setText(q);
				TableFromMySqlDatabase query = new TableFromMySqlDatabase(q);
				query.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				query.pack();
				query.setVisible(true);
				
			}
		});
		

		main.add(p2, BorderLayout.SOUTH);
		main.add(p3, BorderLayout.CENTER);

		main.pack();
		main.setVisible(true);
	}
	public void dimWindow()
	{
		JFrame dframe = new JFrame("Dimensions");
	    int windowWidth = 400;           // Window width in pixels
	    int windowHeight = 150;          // Window height in pixels
	    dframe.setBounds(50, 100,       // Set position
	         windowWidth, windowHeight);  // and size
	    JButton button = new JButton("Submit");
	    dframe.setLayout(new GridLayout(4, 2));
	    
	    dframe.add(new JLabel("Table Name:"));
	    JTextField tname = new JTextField();
	    dframe.add(tname);
	    dframe.add(new JLabel("Initial Concept Column Name:"));
	    JTextField cname = new JTextField();
	    dframe.add(cname);
	    dframe.add(new JLabel("Key Name:"));
	    JTextField kname = new JTextField();
	    dframe.add(kname);
	    
	    button.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	CubeDimension dim = new CubeDimension(tname.getText(),cname.getText(),kname.getText());
	        	cube.addDimension(dim);
	        	dimlistModel.addElement(tname.getText());
	        	refreshTextarea();
	        	dframe.dispose();
	        	
	        	
	        }
	    });
	    
	    dframe.add(button);
	    dframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    dframe.setVisible(true);        // Display the window
	    
	   
	}
	public void conceptWindow(String table)
	{
		JFrame cframe = new JFrame("Concept");
	    int windowWidth = 400;           // Window width in pixels
	    int windowHeight = 150;          // Window height in pixels
	    cframe.setBounds(50, 100,       // Set position
	         windowWidth, windowHeight);  // and size
	    JTextField concept = new JTextField();
	    JTextField colname = new JTextField();
	    cframe.setLayout(new GridLayout(3, 2));
	    cframe.add(new JLabel("Concept Level:"));
	    cframe.add(concept);
	    cframe.add(new JLabel("Column Name:"));
	    cframe.add(colname);
	    JButton button = new JButton("Submit");
	    button.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {

	        	cube.getDimension(table).addConceptLevel(Integer.parseInt(concept.getText()), colname.getText());
	        	List<String> list = cube.getDimension(table).getConceptList();
	        	conlistModel.removeAllElements();
	        	for(String s:list){
	        		
	        			conlistModel.addElement(s);
	        	}
	        
	        	cframe.dispose();
	        }
	    });
	    cframe.add(button);
	    
	    
	    cframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    cframe.setVisible(true);        // Display the window
		
	}
	public void filterWindow()
	{
		JFrame fframe = new JFrame("Filter");
		int windowWidth = 400;           // Window width in pixels
	    int windowHeight = 150;          // Window height in pixels
	    fframe.setBounds(50, 100,       // Set position
	         windowWidth, windowHeight);  // and size
	    
	    
		String[] tables = { "Time", "Product", "Store" };
		String[] time = {"Year", "Month", "Week"};
 		String[] store = {"store_street_address","store_zip", "store_city","store_county","store_state"};
 		String[] product = {"department","category","subCategory"};
 		String[] operator = {"=", ">", "<", "<=", ">="};
 		final JComboBox column = new JComboBox();
 		JComboBox oper = new JComboBox(operator);
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
		JTextField operand = new JTextField();
		JButton button = new JButton("Submit");
	    button.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String select2 =(String)oper.getSelectedItem();
	            String table2 = (String)tlist.getSelectedItem();
	            String column2 = (String)column.getSelectedItem();
	            //System.out.println(select2);
	        	
	        	try {
					Filter f = cube.addFilter(table2,column2, operand.getText(),select2);
					
					fillistModel.addElement(f.getWhereClause());
				} catch (InvalidOperatorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	fframe.dispose();
	        }
	    });
		
		/*switch((String)tlist.getSelectedItem()){
		
		case "Time": column  = new JComboBox(time); break;
		case "Store": column  = new JComboBox(store); break;
		case "Product": column  = new JComboBox(product); break;
		
		}*/
		
		fframe.setLayout(new GridLayout(5,2));
		fframe.add(new JLabel("Table Name"));fframe.add(tlist);
		fframe.add(new JLabel("Column Name"));fframe.add(column);
		fframe.add(new JLabel("Operator"));fframe.add(oper);
		fframe.add(new JLabel("Operand"));fframe.add(operand);
		
		
		
	    
	    fframe.add(button);
	    
	    fframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    fframe.setVisible(true);        // Display the window
	    
	}

	public void factWindow(){
		
		JFrame factframe = new JFrame("Fact");
	    int windowWidth = 400;           // Window width in pixels
	    int windowHeight = 150;          // Window height in pixels
	    factframe.setBounds(50, 100,       // Set position
	         windowWidth, windowHeight);  // and size
	    
	    factframe.setLayout(new GridLayout(3, 2));
	    
	    factframe.add(new JLabel("Table Name:"));
	    JTextField tname = new JTextField();
	    factframe.add(tname);
	    factframe.add(new JLabel("column Name:"));
	    JTextField cname = new JTextField();
	    factframe.add(cname);
	    JButton button = new JButton("Submit");
	    button.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		fact = new Fact(tname.getText(),cname.getText());
	    		cube.addFact(fact);
	    		refreshTextarea();
	    		factframe.dispose();
	    	}
	    });
	    factframe.add(button);
	    
	    factframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    factframe.setVisible(true);        // Display the window
		
	}
	
	public void refreshTextarea(){
		text.setText(cube.generateCubeSQLString());
	}
	
	public static void main(String[] args) {
		BIToView mc = new BIToView();
	}

}
