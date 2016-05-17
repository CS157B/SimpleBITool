package olapGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Cube {
	
	private List<CubeDimension> dimensions;
	private Fact fact;
	private List<Filter> filters;
	private final static List<String> validOperators = new ArrayList<String>(Arrays.asList("=", ">", "<", "<=", ">="));
	
	public Cube(){
		this(null, null);
	}
	
	public Cube(Fact fact, List<CubeDimension> dimensions){
		if(dimensions != null){
			this.dimensions = dimensions;
		}else{
			this.dimensions = new ArrayList();
		}
		
		this.fact = fact;
		filters = new ArrayList<>();
	}
	
	public String generateCubeSQLString(){
		if(dimensions == null || dimensions.isEmpty()){
			return "No dimension added";
		}else if(fact == null){
			return "No fact added";
		}
		
		String br = " \n";
		
		String olapQueryString = "SELECT" + br; //SELECT
		for(CubeDimension d : dimensions){
			olapQueryString +=  d.getTableName() + "." + d.getColumnName() + "," + br; //tableName.columnName,
		}
		olapQueryString += "sum(" + fact.getTableName() + "." + fact.getColumnName() + ")" + br; //tableName.columnName
		
		olapQueryString += "FROM"  + br; //FROM
		for(CubeDimension d : dimensions){
			olapQueryString += d.getTableName() + "," + br; //tableName,
		}
		olapQueryString += fact.getTableName() + br; //tableName
		
		olapQueryString += "WHERE" + br; //WHERE
		if(filters != null){
			for(Filter f : filters){
				olapQueryString +=  f.getWhereClause() + " AND" + br; //(boolean) AND
			}
		}
		for(CubeDimension d : dimensions){
			olapQueryString +=  d.getTableName() + "." + d.getKeyName() + " = " + fact.getTableName() + "." + d.getKeyName() + " AND" + br; //tableName.keyName = factTableName.dimensionKeyName
		}
		
		olapQueryString = olapQueryString.substring(0, olapQueryString.length() - (" AND" + br).length()) + br; //Trim off last AND
		
		if(!dimensions.stream().filter(new Predicate<CubeDimension>() {
			@Override
			public boolean test(CubeDimension d) {
				return d.isGrouped();
			}
		}).collect(Collectors.toList()).isEmpty()){ //If at least one dimension isGrouped
			olapQueryString += "GROUP BY" + br;
			for(CubeDimension d : dimensions){
				if(d.isGrouped()){
					olapQueryString += d.getTableName() + "." + d.getColumnName() + "," + br;
				}
			}
			olapQueryString = olapQueryString.substring(0, olapQueryString.length() - ("," + br).length());
		}
		
		olapQueryString += ";";

		return olapQueryString;
	}
	
	public void addFact(Fact f){
		fact = f;
	}
	
	
	
	public Fact getFact(){
		return this.fact;
	}
	
	public List<CubeDimension> getDimensions(){
		return this.dimensions;
	}
	
	public CubeDimension getDimension(String dimensionTableName){
		for(int i = 0; i < dimensions.size(); i++){
			if(dimensions.get(i).getTableName().equals(dimensionTableName)){
				return dimensions.get(i);
			}
		}
		return null;
	}
	
	public void addDimension(CubeDimension d){
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
	
	public static ArrayList<String> VALID_OPERAND_TYPES = new ArrayList<>();
	static{
		VALID_OPERAND_TYPES.add("INT");
		VALID_OPERAND_TYPES.add("STRING");
	}

	public Filter addFilter(String tableName, String columnName, String operand2, String operator, String operandType) throws InvalidOperatorException, InvalidOperandTypeException{
		if(!VALID_OPERAND_TYPES.contains(operandType)){
			throw new InvalidOperandTypeException();
		}
			Filter f = new Filter(tableName, columnName, operand2, operator, operandType);
		filters.add(f);
		return f;
	}
	
	public boolean removeFilter(int i){
		return filters.remove(i) != null;
	}
	
	public List<Filter> getFilters(){
		return this.filters;
	}

	public List<String> getValidFilterOperators(){
		return Cube.validOperators;
	}
	
	public class Filter {
		
		private String tableName;
		private String columnName;
		private String operand2;
		private String operator;
		private String operandType;
		

		public Filter(String tableName, String columnName, String operand2, String operator, String operandType) throws InvalidOperatorException {
			this.tableName = tableName;
			this.columnName = columnName;
			this.operand2 = operand2;
			if(validOperators.contains(operator)){
				this.operator = operator;
			}else{
				throw new InvalidOperatorException(operator);
			}
			this.operandType = operandType;
		}

		public String getWhereClause(){
			if(operandType.equals("INT")){
				return this.tableName + "." + this.columnName + " " + operator + " " + operand2 + " ";
			}else
				return this.tableName + "." + this.columnName + " " + operator + " \"" + operand2 + "\"";
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
	
	public class InvalidOperandTypeException extends Exception{
		private static final long serialVersionUID = 7863620656308722142L;
		
		@Override
		public String getMessage() {
			return "Invalid operand type";
		}
	}


}
