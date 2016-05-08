package olapGenerator;

import java.util.Map;
import java.util.TreeMap;

public class Dimension {

	private String columnName;
	private String tableName;
	private String keyName;
	private boolean isGrouped;
	private Map<Integer, String> conceptHierarchy;
	
	public Dimension(String tableName, String columnName, String keyName){
		this(tableName, columnName, keyName, false);
	}
	
	public Dimension(String tableName, String columnName, String keyName, boolean isGrouped){
		this.tableName = tableName;
		this.columnName = columnName;
		this.keyName = keyName;
		this.isGrouped = isGrouped;
		this.conceptHierarchy = new TreeMap<>();
	}
	
	public Map<Integer, String> getConceptHierarchy(){
		return this.conceptHierarchy;
	}
	
	public String getColumnName(){
		return this.columnName;
	}

	public String getTableName(){
		return this.tableName;
	}

	public String getKeyName(){
		return this.keyName;
	}

	public boolean isGrouped() {
		return this.isGrouped;
	}
	
	public void setGrouped(boolean isGrouped){
		this.isGrouped = isGrouped;
	}
}
