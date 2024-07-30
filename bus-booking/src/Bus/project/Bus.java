package Bus.project;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DbConnection{
	static String url="jdbc:mysql://localhost:3306/busbooking";
	static String user ="root";
	static String password = "Mathi.51228";
	public static  Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url,user,password);
		
	}
	
}
class BusDao{
	public static void details() throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="select * from bus";
		ResultSet rst = st.executeQuery(q);
		while(rst.next()) {
			System.out.println(" BUS NO "+rst.getInt(1)+" CAPACITY "+rst.getInt(2)+" AC "+rst.getBoolean(3));
			
		}
;	}
}

public class Bus {
	public static void main (String args[]) throws SQLException {
		BusDao.details();
	}

}
