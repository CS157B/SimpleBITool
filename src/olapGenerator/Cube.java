package olapGenerator;
import java.util.List;

public class Cube {
	
	private List<Dimension> dimensions;
	private FactGrain fact;
	private List<Filter> filters;

	public Cube(FactGrain fact, List<Dimension> dimensions, List<Filter> filters){
		this.dimensions = dimensions;
		this.fact = fact;
		this.filters = filters;
	}
	
	public String generateCubeSQLString(){
		String br = " \n";
		
		String olapQueryString = "SELECT" + br; //SELECT
		for(Dimension d : dimensions){
			olapQueryString += d.selectColumn() + "," + br; //tableName.columnName,
		}
		olapQueryString += fact.selectColumn() + br; //tableName.columnName
		
		olapQueryString += "FROM"  + br; //FROM
		for(Dimension d : dimensions){
			olapQueryString += d.getTableName() + "," + br; //tableName,
		}
		olapQueryString += fact.tableName() + br; //tableName
		
		olapQueryString += "WHERE" + br; //WHERE
		if(filters != null){
			for(Filter f : filters){
				olapQueryString +=  f.whereClause() + " AND" + br; //(boolean) AND
			}
		}
		for(Dimension d : dimensions){
			olapQueryString +=  d.getTableName() + "." + d.getKeyName() + " = " + fact.tableName() + "." + d.getKeyName() + " AND" + br; //tableName.keyName = factTableName.dimensionKeyName
		}
		
		olapQueryString = olapQueryString.substring(0, olapQueryString.length() - (" AND" + br).length()) + br; //Trim off last AND
		
		olapQueryString += "GROUP BY" + br;
		for(Dimension d : dimensions){
			if(d.hasGroupByClause()){
				olapQueryString += d.groupByClause() + "," + br;
			}
		}
		olapQueryString = olapQueryString.substring(0, olapQueryString.length() - ("," + br).length()) + ";";
		
		return olapQueryString;
	}
}
