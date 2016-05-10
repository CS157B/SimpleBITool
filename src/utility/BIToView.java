package utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class BIToView extends Frame {

	private JList dimensionBox;

	public BIToView() {
		JFrame main = new JFrame("Simple Business Intelligence Tool");
		main.setLayout(new BorderLayout());

		// output Table View
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.setMaximumSize(new Dimension(100, 100));
		p1.add(new JTextField(40));
		main.add(p1, BorderLayout.WEST);

		JPanel p2 = new JPanel();
		// Dimensions
		JLabel dLabel = new JLabel("Select Dimensions: ");
		String listData[] = { "Product", "Category", "Store", "Time" };
		dimensionBox = new JList(listData);
		dimensionBox.setVisibleRowCount(5);
		JButton dimAdd = new JButton("+");
		JButton dimSub = new JButton("-");
		JScrollPane scrollPane_1 = new JScrollPane(dimensionBox);
		Dimension d = dimensionBox.getPreferredSize();
		d.width = 20;
		scrollPane_1.setPreferredSize(d);
		p2.add(dLabel);
		p2.add(dimensionBox);
		p2.add(dimAdd);
		p2.add(dimSub);

		// concepts
		dLabel = new JLabel("Select Concept: ");
		String cData[] = { "Item 1", "Item 2", "Item 3", "Item 4" };
		dimensionBox = new JList(cData);
		dimensionBox.setVisibleRowCount(5);
		JButton conAdd = new JButton("+");
		JButton conSub = new JButton("-");
		JButton rollUp = new JButton("Roll Up");
		JButton drillDown = new JButton("Drill Down");
		JScrollPane scrollPane_2 = new JScrollPane(dimensionBox);
		Dimension c = dimensionBox.getPreferredSize();
		c.width = 20;
		scrollPane_2.setPreferredSize(d);
		p2.add(dLabel);
		p2.add(dimensionBox);
		p2.add(conAdd);
		p2.add(conSub);
		p2.add(rollUp);
		p2.add(drillDown);
		
		//filters
		dLabel = new JLabel("Select Filter: ");
		String fData[] = { "Item 1", "Item 2", "Item 3", "Item 4" };
		dimensionBox = new JList(fData);
		dimensionBox.setVisibleRowCount(5);
		JButton filAdd = new JButton("+");
		JButton filSub = new JButton("-");
		JScrollPane scrollPane_3 = new JScrollPane(dimensionBox);
		Dimension f = dimensionBox.getPreferredSize();
		f.width = 20;
		scrollPane_3.setPreferredSize(d);
		p2.add(dLabel);
		p2.add(dimensionBox);
		p2.add(filAdd);
		p2.add(filSub);
		
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
