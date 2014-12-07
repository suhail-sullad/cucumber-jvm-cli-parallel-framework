package databaseinitilization;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DatabaseInit {
	private static String dbusername;
	private static String dbpassword;
	private static Connection dbconnection;

	public DatabaseInit(String dbusername, String dbpassword) {
		super();
		DatabaseInit.dbusername = dbusername;
		DatabaseInit.dbpassword = dbpassword;
	}

	public static Connection getmysqlconnection(String hostname, int port,
			String databasename) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		dbconnection = DriverManager.getConnection("jdbc:mysql://" + hostname
				+ ":" + port + "/" + databasename, dbusername, dbpassword);

		return DriverManager.getConnection("jdbc:mysql://" + hostname + ":"
				+ port + "/" + databasename, dbusername, dbpassword);

	}

	public static Connection getmssqlconnection(String hostname, int port,
			String databasename) throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dbconnection = DriverManager.getConnection("jdbc:sqlserver://"
				+ hostname + "\\SQLEXPRESS;databaseName=" + databasename,
				dbusername, dbpassword);
		return DriverManager.getConnection("jdbc:sqlserver://" + hostname
				+ "\\SQLEXPRESS;databaseName=" + databasename, dbusername,
				dbpassword);
	}

	public static Connection getmssqlconnectionwithwindowsauthentication(
			String hostname, int port, String databasename)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dbconnection = DriverManager.getConnection("jdbc:sqlserver://"
				+ hostname
				+ "\\SQLEXPRESS;integratedSecurity=true;databaseName="
				+ databasename);
		return DriverManager.getConnection("jdbc:sqlserver://" + hostname
				+ "\\SQLEXPRESS;integratedSecurity=true;databaseName="
				+ databasename);

	}

	public static Connection getsqliteconnection(File databasefilepath)
			throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		dbconnection = DriverManager.getConnection("jdbc:sqlite:"
				+ databasefilepath.getAbsolutePath());
		return DriverManager.getConnection("jdbc:sqlite:"
				+ databasefilepath.getAbsolutePath());

	}

	public static Connection getibmdb2connection(String hostname, int port,
			String databasename) throws ClassNotFoundException, SQLException {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		dbconnection = DriverManager.getConnection("jdbc:db2:" + hostname + ":"
				+ port + "/" + databasename, dbusername, dbpassword);
		return DriverManager.getConnection("jdbc:db2:" + hostname + ":" + port
				+ "/" + databasename, dbusername, dbpassword);
	}

	// for select and iterate over result set.
	public static ResultSet executequery(String query) throws SQLException {
		ResultSet rs = dbconnection.prepareStatement(query).executeQuery();
		return rs;
	}

	// for select and get list of rows.
	public static List<Map<String, String>> getresultList(String query)
			throws SQLException {
		ResultSet rs = dbconnection.prepareStatement(query).executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		List<Map<String, String>> elements = new ArrayList<>();
		while (rs.next()) {
			Map<String, String> rowitems = new HashMap<>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
				rowitems.put(rsmd.getColumnName(i), rs.getString(i));

			elements.add(rowitems);
		}

		return elements;
	}

	// for alter, update, delete, insert.
	public static void execute(String query) throws SQLException {
		dbconnection.prepareStatement(query).execute();
	}

	// queries can be of type insert into tablename(colum1,....,columnn)
	// values(?,......,?);
	// queries can be of type update tablename set column1 =?, column2=? where
	// columnn=?;
	public static int batchquery(String query, List<Map<String, String>> values)
			throws SQLException {
		int number_of_parameters = StringUtils.countMatches(query, "?");
		PreparedStatement ps = dbconnection.prepareStatement(query);
		for (Map<String, String> value : values) {
			for (int i = 0; i < number_of_parameters; i++)
				ps.setString(i + 1, (String) value.values().toArray()[i]);
			ps.addBatch();
		}

		return ps.executeBatch().length;

	}

	// missing driver
	/*
	 * public static Connection getoracleconnection(String hostname, int port,
	 * String databasename) throws ClassNotFoundException, SQLException {
	 * Class.forName("oracle.jdbc.driver.OracleDriver"); return
	 * DriverManager.getConnection("jdbc:oracle:thin:@" + hostname + ":" + port
	 * + ":" + databasename, dbusername, dbpassword);
	 * 
	 * }
	 */

	// wont work cause need ucanaccess
	/*
	 * public static Connection getaccessdbconnection(String databasefilepath)
	 * throws ClassNotFoundException, SQLException {
	 * Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); return
	 * DriverManager.getConnection("jdbc:ucanaccess://" + databasefilepath);
	 * 
	 * }
	 */
}
