package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

public class SqlManager {
	private static Queue<Connection> connectQueue = new LinkedList<Connection> ();
	
	static {
	    try {
	      Class.forName("com.mysql.jdbc.Driver");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public SqlManager() {
		
	}
	
	public static synchronized Connection getConnection() {
		if (connectQueue.isEmpty()) {
			try {
				connectQueue.offer(DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javatest", "javatest", "123456"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Connection connection = connectQueue.poll();
		try {
			if (connection.isClosed()) {
				connection = getConnection();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static synchronized void putConnection(Connection connection) {
		try {
			connectQueue.offer(connection);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection connection = SqlManager.getConnection();
		try {
			/* Ôö */
//			String strSql = "insert into test (pk,account,name,face,createdata) values (?,?,?,?,current_date())";
//			PreparedStatement pst = connection.prepareStatement(strSql);
//			try {
//				pst.setString(1, "001000001");
//				pst.setString(2, "bbjatjavatest");
//				pst.setString(3, "bbj");
//				pst.setInt(4, 1);
//				
//				pst.execute();
//			}
//			catch (Exception e) {
//				throw e;
//			}
//			finally {
//				pst.close();
//			}
			
			/* É¾ */
//			String strSql_delete = "delete from test where pk=?";
//			PreparedStatement pst_delete = connection.prepareStatement(strSql_delete);
//			pst_delete.setString(1, "001000001");
//			
//			pst_delete.execute();
//			pst_delete.close();
			
			/* ¸Ä */
//			String strUpdate = "update test set account=?,name=?,face=? where pk=?";
//			PreparedStatement pst_update = connection.prepareStatement(strUpdate);
//			pst_update.setString(1, "ttxatjavatest");
//			pst_update.setString(2, "ttx");
//			pst_update.setInt(3, 3);
//			pst_update.setString(4, "001000002");
//			
//			pst_update.execute();
//			pst_update.close();
			
			/* ²é */
			Statement statement = connection.createStatement();
			try {
				ResultSet res = statement.executeQuery("select * from test");
				while (res.next()) {
					System.out.println(res.getString("pk"));
					System.out.println(res.getString("account"));
					System.out.println(res.getString("name"));
					System.out.println(res.getInt("face"));
					System.out.println(res.getDate("createdata"));
				}
			}
			catch (Exception e) {
				throw e;
			}
			finally {
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			SqlManager.putConnection(connection);
		}
	}
}
