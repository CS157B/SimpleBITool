package utility;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import olapGenerator.Cube;
import olapGenerator.Cube.Filter;
import olapGenerator.Cube.InvalidOperandTypeException;
import olapGenerator.Cube.InvalidOperatorException;
import olapGenerator.Fact;
import olapGenerator.CubeDimension;

public class BIToView extends Frame {

	private JList dimensionBox;
	private JList<String> conceptBox;
	private JList filterBox;
	private JTextArea text;
	private Fact fact;
	private Cube cube = new Cube();
	private CubeDimension dim;
	public DefaultListModel dimlistModel;
	public DefaultListModel conlistModel;
	public DefaultListModel fillistModel;

	public BIToView() {
		this(null, null);
	}

	public BIToView(List<CubeDimension> loadedDimensions, Fact loadedFact) {
		for (CubeDimension c : loadedDimensions) {
			cube.addDimension(c);
		}
		if (loadedFact != null) {
			cube.addFact(loadedFact);
		}
		
		

		// cube.addDimension(d1);
		JFrame main = new JFrame("Simple Business Intelligence Tool");
		main.setLayout(new BorderLayout());
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setMinimumSize(new Dimension(1000, 1000));
		main.setResizable(false);
		
		// output Table View
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.setMaximumSize(new Dimension(100, 100));
		text = new JTextArea(50, 50);
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
		dimAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dimWindow();

			}
		});
		JButton dimSub = new JButton("-");
		dimSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dimlistModel.removeElement((String) dimensionBox.getSelectedValue());
				cube.removeDimension((String) dimensionBox.getSelectedValue());
				conlistModel.removeAllElements();
				refreshDimensionList();
				refreshTextarea();
			}
		});
		JButton addfact = new JButton("Add a Fact");
		addfact.addActionListener(new ActionListener() {
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
		dimensionBox.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (dimensionBox.getSelectedValue() != null) {
					String select = (String) dimensionBox.getSelectedValue();
					conlistModel.removeAllElements();
					for (String s : cube.getDimension(select).getConceptList()) {
						conlistModel.addElement(s);
					}
					refreshConceptList();
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
		conAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conceptWindow((String) dimensionBox.getSelectedValue());
				refreshConceptList();
				refreshTextarea();
			}
		});

		conceptBox.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String select = (String) dimensionBox.getSelectedValue();
				int index = findIndexInListModel(conlistModel, cube.getDimension(select).getCurrentConcept());
				conceptBox.setSelectedIndex(index);
			}
		});
		JButton conSub = new JButton("-");
		conSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (conceptBox.getModel().getSize() > 1) {
					String select1 = (String) conceptBox.getSelectedValue();
					conlistModel.removeElement(select1);
					cube.getDimension((String) dimensionBox.getSelectedValue()).removeConcept(select1);
					refreshConceptList();
					refreshTextarea();
				}
			}
		});
		JButton rollUp = new JButton("Roll Up");
		rollUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String table = (String) dimensionBox.getSelectedValue();

				cube.getDimension(table).rollUp();
				int index = findIndexInListModel(conceptBox.getModel(), cube.getDimension(table).getCurrentConcept());
				conceptBox.setSelectedIndex(index);
				refreshConceptList();
				refreshTextarea();
			}
		});
		JButton drillDown = new JButton("Drill Down");
		drillDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String table = (String) dimensionBox.getSelectedValue();
				cube.getDimension(table).drillDown();
				int index = findIndexInListModel(conceptBox.getModel(), cube.getDimension(table).getCurrentConcept());
				conceptBox.setSelectedIndex(index);
				refreshConceptList();
				refreshTextarea();
			}
		});
		JScrollPane scrollPane_2 = new JScrollPane(conceptBox);
		conceptBox.setVisibleRowCount(4);
		conceptBox.setFixedCellHeight(20);
		conceptBox.setFixedCellWidth(150);
		box6.add(dLabel);
		box6.add(scrollPane_2);
		box2.add(conAdd);
		box2.add(conSub);
		box3.add(rollUp);
		box3.add(drillDown);
		p2.add(box6);
		p2.add(box2);
		p2.add(box3);

		// filters
		dLabel = new JLabel("Filter:");

		filterBox = new JList(fillistModel);
		filterBox.setVisibleRowCount(5);
		Box box4 = Box.createVerticalBox();
		Box box7 = Box.createVerticalBox();
		JButton filAdd = new JButton("+");
		filAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				filterWindow();
				refreshFilterList();
				refreshTextarea();
			}
		});
		JButton filSub = new JButton("-");
		filSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int indextoRemove = filterBox.getSelectedIndex();
				fillistModel.remove(indextoRemove);
				cube.removeFilter(indextoRemove);
				refreshFilterList();
				refreshTextarea();
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

		// Execute Button
		JPanel p3 = new JPanel();
		JButton execute = new JButton("Execute");
		p3.add(execute);
		execute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String q = cube.generateCubeSQLString();
				text.setText(q);
				TableFromMySqlDatabase query = new TableFromMySqlDatabase(q);
				query.pack();
				query.setVisible(true);
			}
		});

		main.add(p2, BorderLayout.SOUTH);
		main.add(p3, BorderLayout.CENTER);

		main.pack();
		main.setVisible(true);
		refreshDimensionList();
		refreshConceptList();
		refreshFilterList();
		refreshTextarea();
		
		if(dimensionBox.getModel().getSize() != 0){
			dimensionBox.setSelectedIndex(0);
		}
	}

	public BIToView(List<CubeDimension> dimensions) {
		this(dimensions, null);
	}

	public BIToView(Fact fact) {
		this(null, fact);
	}

	public void dimWindow() {
		JFrame dframe = new JFrame("Dimensions");
		int windowWidth = 400; // Window width in pixels
		int windowHeight = 150; // Window height in pixels
		dframe.setBounds(50, 100, // Set position
				windowWidth, windowHeight); // and size
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
				CubeDimension dim = new CubeDimension(tname.getText(), cname.getText(), kname.getText());
				cube.addDimension(dim);
				dimlistModel.addElement(tname.getText());
				dframe.dispose();
			}
		});

		dframe.add(button);
		dframe.setVisible(true); // Display the window

	}

	public void conceptWindow(String table) {
		JFrame cframe = new JFrame("Concept");
		int windowWidth = 400; // Window width in pixels
		int windowHeight = 150; // Window height in pixels
		cframe.setBounds(50, 100, // Set position
				windowWidth, windowHeight); // and size
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
				for (String s : list) {
					conlistModel.addElement(s);
				}
				cframe.dispose();
			}
		});
		cframe.add(button);

		cframe.setVisible(true); // Display the window

	}

	public void filterWindow() {
		if(cube.getDimensions().isEmpty()){
			return;
		}
		JFrame fframe = new JFrame("Filter");
		int windowWidth = 400; // Window width in pixels
		int windowHeight = 150; // Window height in pixels
		fframe.setBounds(50, 100, // Set position
				windowWidth, windowHeight); // and size

		
		
		
		
		
		String[] tables = new String[cube.getDimensions().size()], operator = new String[cube.getValidFilterOperators().size()];
		operator = cube.getValidFilterOperators().toArray(operator);
		tables = cube.getDimensions().stream().map(new Function<CubeDimension, String>() {

			@Override
			public String apply(CubeDimension t) {
				return t.getTableName();
			}
		}).collect(Collectors.toList()).toArray(tables);
		
		List<String> typeList = Cube.VALID_OPERAND_TYPES;
		String[] typeArr = new String[typeList.size()];
		typeArr = typeList.toArray(typeArr);
		JComboBox<String> typeComboBox = new JComboBox(typeArr);
		
		final JComboBox column = new JComboBox();
		JComboBox<String> oper = new JComboBox(operator);
		// Create the combo box, select item at index 4.
		// Indices start at 0, so 4 specifies the pig.
		JComboBox<String> tlist = new JComboBox(tables);
		tlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JComboBox cb = (JComboBox)e.getSource();
				column.removeAllItems();
				String tablename = (String) tlist.getSelectedItem();
				String[] conceptArr = new String[cube.getDimension(tablename).getConceptList().size()];
				conceptArr = cube.getDimension(tablename).getConceptList().toArray(conceptArr);
				column.setModel(new DefaultComboBoxModel<String>(conceptArr));
			}
		});
		tlist.setSelectedIndex(0);
		JTextField operand = new JTextField();
		JButton button = new JButton("Submit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String select2 = (String) oper.getSelectedItem();
				String table2 = (String) tlist.getSelectedItem();
				String column2 = (String) column.getSelectedItem();
				String operandType = (String) typeComboBox.getSelectedItem();
				// System.out.println(select2);

				try {
					Filter f = cube.addFilter(table2, column2, operand.getText(), select2, operandType);
					refreshFilterList();
					refreshTextarea();
				} catch (InvalidOperatorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidOperandTypeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				fframe.dispose();
			}
		});

		/*
		 * switch((String)tlist.getSelectedItem()){
		 * 
		 * case "Time": column = new JComboBox(time); break; case "Store":
		 * column = new JComboBox(store); break; case "Product": column = new
		 * JComboBox(product); break;
		 * 
		 * }
		 */

		fframe.setLayout(new GridLayout(6, 2));
		fframe.add(new JLabel("Table Name"));
		fframe.add(tlist);
		fframe.add(new JLabel("Column Name"));
		fframe.add(column);
		fframe.add(new JLabel("Operator"));
		fframe.add(oper);
		fframe.add(new JLabel("Operand"));
		fframe.add(operand);
		fframe.add(new JLabel("Operand Type"));
		fframe.add(typeComboBox);

		fframe.add(button);

		fframe.setVisible(true); // Display the window

	}

	public void factWindow() {

		JFrame factframe = new JFrame("Fact");
		int windowWidth = 400; // Window width in pixels
		int windowHeight = 150; // Window height in pixels
		factframe.setBounds(50, 100, // Set position
				windowWidth, windowHeight); // and size

		factframe.setLayout(new GridLayout(3, 2));

		factframe.add(new JLabel("Table Name:"));
		JTextField tname = new JTextField();
		factframe.add(tname);
		factframe.add(new JLabel("column Name:"));
		JTextField cname = new JTextField();
		factframe.add(cname);
		JButton button = new JButton("Submit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fact = new Fact(tname.getText(), cname.getText());
				cube.addFact(fact);
				refreshTextarea();
				factframe.dispose();
			}
		});
		factframe.add(button);

		factframe.setVisible(true); // Display the window

	}

	public void refreshTextarea() {
		text.setText(cube.generateCubeSQLString());
	}

	public void refreshDimensionList() {
		int selectedDimensionIndex = dimensionBox.getSelectedIndex();
		if (selectedDimensionIndex > dimlistModel.size()) {
			selectedDimensionIndex = 0;
		}
		dimlistModel.clear();
		if (!cube.getDimensions().isEmpty()) {
			for (int i = 0; i < cube.getDimensions().size(); i++) {
				dimlistModel.addElement(cube.getDimensions().get(i).getTableName());
			}
			dimensionBox.setModel(dimlistModel);
			dimensionBox.setSelectedIndex(selectedDimensionIndex);
		}

	}

	public void refreshConceptList() {
		String selectedConcept = conceptBox.getSelectedValue();
		conlistModel.clear();
		String selectedTable = (String) dimensionBox.getSelectedValue();
		if (selectedTable != null) {
			for (String s : cube.getDimension(selectedTable).getConceptList()) {
				conlistModel.addElement(s);
			}
			conceptBox.setModel(conlistModel);
			conceptBox.setSelectedIndex(findIndexInListModel(conlistModel, selectedConcept));
		} else {
			conceptBox.setModel(conlistModel);
		}
	}

	public void refreshFilterList() {
		fillistModel.clear();
		for (Filter f : cube.getFilters()) {
			fillistModel.addElement(f.getWhereClause());
		}
		filterBox.setModel(fillistModel);
	}

	public int findIndexInListModel(ListModel<String> model, String targetString) {
		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).equals(targetString)) {
				return i;
			}
		}
		return -1;

	}

	public static List<String> parseConcepts(String conceptsString) {
		Pattern stringPattern = Pattern.compile("([\\w\\d|_]+)");
		ArrayList<String> stringList = new ArrayList<>();
		Matcher m = stringPattern.matcher(conceptsString);
		while (m.find()) {
			stringList.add(m.group(0));
		}
		return stringList;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 1) {
			List<CubeDimension> dimensions = new ArrayList();
			Fact fact = null;
			Scanner in = new Scanner(new File(args[0]));
			Pattern dimensionPattern = Pattern.compile(
					"(?:Dimension:\\s*?table=\")([\\w\\d|_]*)(?:\",\\s*?)(?:concepts=\"(.*)\")(?:, key=\")([\\w\\d|_]+)(?:\";)");
			Pattern factPattern = Pattern
					.compile("(?:Fact:table=\")([\\w\\d|_]+)(?:\" fact_column=\")([\\w\\d|_]+)(?:\";)");
			Pattern elementPattern = Pattern.compile("(.*;)");
			Matcher m;
			String tableName = "";
			String factColumn = "";
			List<String> concepts = new ArrayList<>();
			String keyName = "";
			in.useDelimiter("\\z");
			Matcher elementMatcher = elementPattern.matcher(in.next());
			while (elementMatcher.find()) {
				String elementString = elementMatcher.group(0);
				;
				if ((m = dimensionPattern.matcher(elementString)).matches()) {
					// for(int i = 0; i <= m.groupCount(); i++){
					// System.out.println(i + " = " + m.group(i));
					// }
					tableName = m.group(1);
					concepts.addAll(parseConcepts(m.group(2)));
					keyName = m.group(3);
					dimensions.add(new CubeDimension(tableName, concepts, keyName));
					concepts = new ArrayList<>();
				} else if ((m = factPattern.matcher(elementString)).matches()) {
					tableName = m.group(1);
					factColumn = m.group(2);
					fact = new Fact(tableName, factColumn);
				} else {
					System.err.println("Invalid Input Element: " + elementString);
				}
			}
			if (dimensions != null && fact != null) {
				BIToView mc = new BIToView(dimensions, fact);
			} else if (dimensions != null) {
				BIToView mc = new BIToView(dimensions);
			} else if (fact != null) {
				BIToView mc = new BIToView(fact);
			}

		} else {
			BIToView mc = new BIToView();
		}

	}

}
