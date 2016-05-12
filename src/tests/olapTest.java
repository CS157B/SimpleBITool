package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import olapGenerator.Cube;
import olapGenerator.CubeDimension;
import olapGenerator.Fact;

public class olapTest {

	@Test
	public void test() {
		Fact f = new Fact("sales_fact", "dollar_sales");
		List<CubeDimension> dimensions = new ArrayList<CubeDimension>();
		dimensions.add(new CubeDimension("store", "store_street_address", "store_key"));
		dimensions.add(new CubeDimension("product", "brand", "product_key"));
		dimensions.add(new CubeDimension("time", "day_of_week", "time_key"));
		Cube c = new Cube(f, dimensions);
		c.getDimensions().get(0).addConceptLevel(1, "test1");
		c.getDimensions().get(0).addConceptLevel(-1, "test2");
		c.getDimensions().get(0).getConceptList();
		System.out.println(c.generateCubeSQLString());
		
	}

}
