package olapGenerator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Cube {
	
	private List<Dimension> dimensions;
	private Fact fact;
	private List<Filter> filters;

	public Cube(Fact fact, List<Dimension> dimensions){
		this.dimensions = dimensions;
		this.fact = fact;
		this.filters = null;
	}
	
	public Cube(Fact fact, List<Dimension> dimensions, List<Filter> filters){
		this.dimensions = dimensions;
		this.fact = fact;
		this.filters = filters;
	}
	
	public String generateCubeSQLString(){
		String br = " \n";
		
		String olapQueryString = "SELECT" + br; //SELECT
		for(Dimension d : dimensions){
			olapQueryString += d.getTableName() + "." + d.getColumnName() + "," + br; //tableName.columnName,
		}
		olapQueryString += fact.getTableName() + "." + fact.getColumnName() + br; //tableName.columnName
		
		olapQueryString += "FROM"  + br; //FROM
		for(Dimension d : dimensions){
			olapQueryString += d.getTableName() + "," + br; //tableName,
		}
		olapQueryString += fact.getTableName() + br; //tableName
		
		olapQueryString += "WHERE" + br; //WHERE
		if(filters != null){
			for(Filter f : filters){
				olapQueryString +=  f.whereClause() + " AND" + br; //(boolean) AND
			}
		}
		for(Dimension d : dimensions){
			olapQueryString +=  d.getTableName() + "." + d.getKeyName() + " = " + fact.getTableName() + "." + d.getKeyName() + " AND" + br; //tableName.keyName = factTableName.dimensionKeyName
		}
		
		olapQueryString = olapQueryString.substring(0, olapQueryString.length() - (" AND" + br).length()) + br; //Trim off last AND
		
		if(!dimensions.stream().filter(new Predicate<Dimension>() {
			@Override
			public boolean test(Dimension d) {
				return d.isGrouped();
			}
		}).collect(Collectors.toList()).isEmpty()){ //If at least one dimension isGrouped
			olapQueryString += "GROUP BY" + br;
			for(Dimension d : dimensions){
				if(d.isGrouped()){
					olapQueryString += d.getTableName() + "." + d.getColumnName() + "," + br;
				}
			}
			olapQueryString = olapQueryString.substring(0, olapQueryString.length() - ("," + br).length());
		}
		
		olapQueryString += ";";

		return olapQueryString;
	}
}
