package utility;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;

import olapGenerator.Cube;
import olapGenerator.Fact;

public class BIToView extends Frame {

	private JList dimensionBox;
	private JList conceptBox;
	private JList filterBox;
	private Fact fact = new Fact("", "");
	private Cube cube = new Cube();
	private olapGenerator.CubeDimension d1 = new olapGenerator.CubeDimension("Product", "Brand", "ProductKey");

	public BIToView() {
		//cube.addDimension(d1);
		
		JFrame main = new JFrame("Simple Business Intelligence Tool");
		main.setLayout(new BorderLayout());
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		// output Table View
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.setMaximumSize(new Dimension(100, 100));
		JTextField text = new JTextField(50);
		text.setEditable(false);
		p1.add(text);
		main.add(p1, BorderLayout.WEST);

		JPanel p2 = new JPanel();
		
		// Dimensions
		JLabel dLabel = new JLabel("Dimensions:");
		List<olapGenerator.CubeDimension> dimensions = cube.getDimensions();
		String [] listData = new String [dimensions.size()];
		for(int i = 0; i < dimensions.size(); i++){
			listData[i] = dimensions.get(i).getTableName();
		}
		dimensionBox = new JList(listData);
		dimensionBox.setVisibleRowCount(5);
        Box box1 = Box.createVerticalBox();
        Box box5 = Box.createVerticalBox();
		JButton dimAdd = new JButton("+");
		JButton dimSub = new JButton("-");
		JButton addfact = new JButton("Add a Fact");
		JScrollPane scrollPane_1 = new JScrollPane(dimensionBox);
		dimensionBox.setVisibleRowCount(4);
		dimensionBox.setFixedCellHeight(20);
		dimensionBox.setFixedCellWidth(60);
		box5.add(dLabel);
		box5.add(scrollPane_1);
		//dimAdd.addActionListener();
		box1.add(dimAdd);
		box1.add(dimSub);
		p2.add(box5);
		p2.add(box1);
		p2.add(addfact);

		// concepts
		dLabel = new JLabel("Concept:");
        Box box2 = Box.createVerticalBox();
        Box box3 = Box.createVerticalBox();
        Box box6 = Box.createVerticalBox();
        conceptBox = new JList();
        conceptBox.setVisibleRowCount(5);
		JButton conAdd = new JButton("+");
		JButton conSub = new JButton("-");
		JButton rollUp = new JButton("Roll Up");
		JButton drillDown = new JButton("Drill Down");
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
		filterBox = new JList();
		filterBox.setVisibleRowCount(5);
        Box box4 = Box.createVerticalBox();
        Box box7 = Box.createVerticalBox();
		JButton filAdd = new JButton("+");
		JButton filSub = new JButton("-");
		JScrollPane scrollPane_3 = new JScrollPane(filterBox);
		filterBox.setVisibleRowCount(4);
		filterBox.setFixedCellHeight(20);
		filterBox.setFixedCellWidth(60);
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

		

		main.add(p2, BorderLayout.SOUTH);
		main.add(p3, BorderLayout.CENTER);

		main.pack();
		main.setVisible(true);
	}

	public static void main(String[] args) {
		BIToView mc = new BIToView();
	}

}
