package olapGenerator;

public class Dimension {

	private String columnName;
	private String tableName;
	private String keyName;
	private boolean hasGroupBy;
	private String groupByClause;

	String getColumnName(){
		return this.columnName;
	}

	String getTableName(){
		return this.tableName;
	}

	String selectColumn(){
		return "";
	}

	String getKeyName(){
		return this.keyName;
	}

	boolean hasGroupByClause(){
		return false;
		
	}

	String groupByClause(){
		return this.groupByClause;
	}

}
