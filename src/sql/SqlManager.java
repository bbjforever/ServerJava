package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

public class SqlManager {
	/** 连接最大存在时间 */
	private static final int maxConnectTime = 3600;
	/** 连接最大使用次数 */
	private static final int maxUseTimes = 100;
	
	private static Queue<ConnectionObj> connectQueue = new LinkedList<ConnectionObj> ();
	
	static {
	    try {
	      Class.forName("com.mysql.jdbc.Driver");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	/* 内部类 */
	private static class ConnectionObj {
		private Connection connection;
		private int timeStamp = 0;
		private int useCount = 0;
		
		private ConnectionObj(Connection c) {
			this.connection = c;
			this.timeStamp = (int) Calendar.getInstance().getTimeInMillis();
		}
		
		private boolean isValid() throws SQLException {
			int now = (int) Calendar.getInstance().getTimeInMillis();
			if ((now - timeStamp) > maxConnectTime) {
				return false;
			}
			
			if (useCount >= maxUseTimes) {
				return false;
			}

			return !connection.isClosed();
		}
		
		/**
		 * 增加连接使用次数
		 */
		private void addTimes() {
			this.useCount ++;
		}
		
		private void closeConnection() throws SQLException {
			this.connection.close();
		}
	}
	
	public SqlManager() {
		
	}
	
	public static synchronized ConnectionObj getConnection() {
		if (connectQueue.isEmpty()) {
			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javatest", "javatest", "123456");
				
				ConnectionObj cObj = new ConnectionObj(c);
				connectQueue.offer(cObj);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ConnectionObj tarConn = connectQueue.poll();
		try {
			if (!tarConn.isValid()) {
				tarConn.closeConnection();
				tarConn = getConnection();
			}
			else {
				tarConn.addTimes();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return tarConn;
	}
	
	public static synchronized void putConnection(ConnectionObj cObj) {
		try {
			if (cObj.isValid()) {
				connectQueue.add(cObj);
			}
		}
		catch (IllegalStateException e) {
			System.out.println("线程池容量超出界限！");
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ConnectionObj cObj = SqlManager.getConnection();
		try {
			/* 增 */
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
			
			/* 删 */
//			String strSql_delete = "delete from test where pk=?";
//			PreparedStatement pst_delete = connection.prepareStatement(strSql_delete);
//			pst_delete.setString(1, "001000001");
//			
//			pst_delete.execute();
//			pst_delete.close();
			
			/* 改 */
//			String strUpdate = "update test set account=?,name=?,face=? where pk=?";
//			PreparedStatement pst_update = connection.prepareStatement(strUpdate);
//			pst_update.setString(1, "ttxatjavatest");
//			pst_update.setString(2, "ttx");
//			pst_update.setInt(3, 3);
//			pst_update.setString(4, "001000002");
//			
//			pst_update.execute();
//			pst_update.close();
			
			/* 查 */
			Statement statement = cObj.connection.createStatement();
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
			SqlManager.putConnection(cObj);
		}
	}
}
