package Bus.project;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	public static int GetCapacity(int busno) throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="select capacity from bus where busno = ? ";
		PreparedStatement pst = con.prepareStatement(q);
		pst.setInt(1, busno);
		ResultSet rst =pst.executeQuery();	
		rst.next();
		return rst.getInt(1);
		
	}
	public static void SetCapacity(int busno,int capacity) throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q =" update  bus set capacity = ? where busno = ? ";
		PreparedStatement pst = con.prepareStatement(q);
		pst.setInt(1,capacity);
		pst.setInt(2,busno);

		int row = pst.executeUpdate();	
		System.out.println("NO OF ROW AFFECTED..."+row);
		
	}
	
	public static void details() throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="select * from bus";
		ResultSet rst = st.executeQuery(q);
		while(rst.next()) {
			System.out.println(" BUS NO "+rst.getInt(1)+" CAPACITY "+rst.getInt(2)+" AC "+rst.getBoolean(3));
			
		}
	}
}
class BookingDao{
	public static void deletebooking(String name) throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="delete from booking where name = ? ";
		PreparedStatement pst = con.prepareStatement(q);
		pst.setString(1, name);
		int row =pst.executeUpdate();	
		System.out.println(" no of row affected "+row);
		
	}
	public static int Getseats(String name) throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="select seats from booking where name = ? ";
		PreparedStatement pst = con.prepareStatement(q);
		pst.setString(1, name);
		ResultSet rst =pst.executeQuery();	
		rst.next();
		return rst.getInt(1);
		
	}
	public static int GetBusno(String name) throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="select busno from booking where name = ? ";
		PreparedStatement pst = con.prepareStatement(q);
		pst.setString(1, name);
		ResultSet rst =pst.executeQuery();	
		rst.next();
		return rst.getInt(1);
		
	}
	public static void insertValues(String name,int seats,int busno) throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="insert into booking values(?,?,?)";
		PreparedStatement pst = con.prepareStatement(q);
		pst.setString(1,name);
		pst.setInt(2,seats);
		pst.setInt(3,busno);


		int row = pst.executeUpdate();	
		System.out.println("NO OF ROW AFFECTED..."+row);
		
	}
	public static void details() throws SQLException {
		Connection con = DbConnection.getConnection();
		Statement st = con.createStatement();
		String q ="select * from booking";
		ResultSet rst = st.executeQuery(q);
		while(rst.next()) {
			System.out.println(" NAME "+rst.getString(1)+" SEATS "+rst.getInt(2)+" BUS NO "+rst.getInt(3)); 
			
		}
		
	}
}

class Booking{
	String name;
	int seats;
	int busno;
	Booking(){
		Scanner scan = new Scanner(System.in);
		System.out.println(" ENTER THE NAME !");
		name=scan.nextLine();
		System.out.println(" ENTER YOUR SEATS ");
		seats=scan.nextInt();
		System.out.println(" ENTER YOUR BUS NO ");
		busno=scan.nextInt();
		
	}
	public boolean toBooking() throws SQLException {
		int capacity = BusDao.GetCapacity(busno);
		if( capacity > seats) {
			BusDao.SetCapacity(busno,capacity-seats);
			BookingDao.insertValues(name, seats, busno);
			return true;
			
		}
		return false;
		
	}
	public static boolean tocancel(String name) throws SQLException {
		int seats = BookingDao.Getseats(name);
		if(seats > 0) {
		int busno = BookingDao.GetBusno(name);
		BusDao.SetCapacity(busno, BusDao.GetCapacity(busno)+seats);
		BookingDao.deletebooking(name);
		return true;

		
	}
		return false;

	}
}

public class Bus {
	public static void main (String args[]) throws SQLException {
		Scanner scan = new Scanner(System.in);
		while(true) {
			BusDao.details();

			System.out.println("1--->  BOOK  <---");
			System.out.println("2--->  CANCEL  <---");
			System.out.println("3--->  SHOW BOOKING DETAILS  <---");
			System.out.println(" ENTER YOUR  CHOICE ");
			int choice = scan.nextInt();
			switch(choice) {
			case 1:
				Booking b = new Booking();
				if(b.toBooking()) {
					System.out.println(" BOOKED ");
				}else {
					System.out.println(" NOT ENOUGH SEATS ");
				}
				break;
			case 2:
				scan.nextLine();
				System.out.println("ENTER THE NAME TO CANCEL ...");
				String name =scan.nextLine();
				if(Booking.tocancel(name)) {
					System.out.println(" CANCELED ");
				}else {
					System.out.println("INVALID BOOKING ");
				}
			case 3:
				BookingDao.details();
				break;
			}
			
		}
	
	}

}
