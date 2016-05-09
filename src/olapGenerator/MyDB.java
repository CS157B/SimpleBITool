package olapGenerator;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyDB {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
	 
	public MyDB() 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//conn = DriverManager.getConnection(url, user_name, password);
			 conn = DriverManager.getConnection(
		            		           "jdbc:mysql://localhost:3306/testDB?" +
		                               "user=root"+"password=test");
			 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet runSql(String sql) throws SQLException 
	{
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}
	
}
