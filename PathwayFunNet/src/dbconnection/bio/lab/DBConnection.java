package dbconnection.bio.lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import config.bio.lab.Configuration;

public class DBConnection {

	private Connection conn = null;
	private Statement stmt = null;
	public DBConnection() {

		initDBConn();
	}

	/**
	 * initialize the DB connection
	 */
	public void initDBConn() {
		Configuration conf = new Configuration("project.properties");
		String driver = conf.getValues("DBDriver");
		String host = conf.getValues("DBHost");
		String name = conf.getValues("DBName");
		String port = conf.getValues("DBPort");
		String user = conf.getValues("User");
		String passwd = conf.getValues("Passwd");
		String url = "jdbc:mysql://" + host + ":" + port + "/" + name;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, passwd);
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * execute sql query
	 * 
	 * @param sql
	 * @return
	 */
	public ResultSet execQuery(String sql) {
		ResultSet rs = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * execuate update(update insert delete) operations
	 * 
	 * @param sql
	 * @return
	 */
	public int execUpdate(String sql) {
		int result = 0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * execute batch sql sentences
	 * 
	 * @param pstmt
	 * @return
	 */
	public int[] execBatch() {
		int[] results = { 0 };
		try {
			results = stmt.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * add sql to batch cache
	 * 
	 * @param sql
	 */
	public void addBatch(String sql) {
		try {
			stmt.addBatch(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * close the connection and the statement
	 */
	public void close() {
		try {

			if (!conn.isClosed()) {
				conn.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// DBConnection conn = new DBConnection();

	}
}
