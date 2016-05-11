package olapGenerator;

public class Fact {

	private String columnName;
	private String tableName;
	
	public Fact(String tableName, String columnName){
		this.columnName = columnName;
		this.tableName = tableName;
	}
	
	String getColumnName(){
		return this.columnName;
	}

	public String getTableName(){
		return this.tableName;
		
	}

}
