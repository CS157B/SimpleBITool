
public interface Dimension {

	String columnName();

	String tableName();

	String selectColumn();

	String keyName();

	boolean hasGroupByClause();

	String groupByClause();

	String factKeyName();

	

}
