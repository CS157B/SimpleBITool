package olapGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Cube {
	
	private List<Dimension> dimensions;
	private Fact fact;
	private List<Filter> filters;
	private final static List<String> validOperators = new ArrayList<String>(Arrays.asList("=", ">", "<", "<=", ">="));
	
	public Cube(Fact fact, List<Dimension> dimensions){
		this.dimensions = dimensions;
		this.fact = fact;
		filters = new ArrayList<>();
	}
	
	public List<Filter> getFilters(){
		return this.filters;
	}

	public List<String> getValidFilterOperators(){
		return Cube.validOperators;
	}
	
	public Fact getFact(){
		return this.fact;
	}
	
	public List<Dimension> getDimensions(){
		return this.dimensions;
	}
	
	public Dimension getDimension(String dimensionTableName){
		for(int i = 0; i < dimensions.size(); i++){
			if(dimensions.get(i).getTableName().equals(dimensionTableName)){
				return dimensions.get(i);
			}
		}
		return null;
	}
	
	public void addDimension(Dimension d){
		dimensions.add(d);
	}
	
	public boolean removeDimension(String dimensionTableName){
		for(int i = 0; i < dimensions.size(); i++){
			if(dimensions.get(i).getTableName().equals(dimensionTableName)){
				dimensions.remove(i);
				return true;
			}
		}
		return false;
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
				olapQueryString +=  f.getWhereClause() + " AND" + br; //(boolean) AND
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

	public void addFilter(Dimension operand1, String operand2, String operator) throws InvalidOperatorException{
			filters.add(new Filter(operand1, operand2, operator));
	}
	
	public boolean removeFilter(int i){
		return filters.remove(i) != null;
	}
	
	public class Filter {

		private Dimension operand1;
		private String operand2;
		private String operator;
		

		public Filter(Dimension operand1, String operand2, String operator) throws InvalidOperatorException {
			this.operand1 = operand1;
			this.operand2 = operand2;
			if(validOperators.contains(operator)){
				this.operator = operator;
			}else{
				throw new InvalidOperatorException(operator);
			}
		}

		public String getWhereClause(){
				return operand1.getTableName() + "." + operand1.getColumnName() + " " + operator + " \"" + operand2 + "\"";
		}

	}
	
	public class InvalidOperatorException extends Exception{
		private static final long serialVersionUID = -9105699008334621409L;
		private String operator;
		
		public InvalidOperatorException(String operator) {
			super();
			this.operator = operator;
		}
		
		@Override
		public String getMessage() {
			return "Invalid operator: " + operator;
		}
	}


}
