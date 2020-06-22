package xtensus.beans.common.sauvgardeRestauration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SQLtoDB {

	private static Connection con;
	private static String DATA_BASE_NAME;
	private static String dataBaseType;

	public static Connection getConnection() throws Exception {
		// Load the SQLServerDriver class, build the
		// connection string, and get a connection

		if (con == null || con.isClosed()) {
			Resource rsrc = new ClassPathResource("/paramDataBase.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));
			String dataBaseDriver = props.getProperty("DataBase.driver");
			Class.forName(dataBaseDriver); //.newInstance();
			String dataBaseUrl = props.getProperty("DataBase.url");
			//[AH] 2020-05-16
			String dataBaseUrlTest = props.getProperty("DataBase.urlTest");
			StringBuilder dataBaseURLTest2 = new StringBuilder(dataBaseUrlTest);
			//=======================================================
			int dataBaseNameBiginnigIndex = dataBaseUrl.lastIndexOf("=");
			DATA_BASE_NAME = dataBaseUrl.substring(dataBaseNameBiginnigIndex+1);
			StringBuilder dataBaseURL2 = new StringBuilder(dataBaseUrl);
			
			if (dataBaseDriver.toLowerCase().contains("sqlserver")) {
				dataBaseType = "sqlserver";
				dataBaseURL2.append(";user=").append(props.getProperty("DataBase.username"))
						.append(";password=").append(props.getProperty("DataBase.password"));
			}
			if (dataBaseDriver.toLowerCase().contains("mysql")) {
				dataBaseType = "mysql";
				//[AH] 2020-05-16
				dataBaseURLTest2.append("?user=").append(props.getProperty("DataBase.username"))
				.append("&password=").append(props.getProperty("DataBase.password"));
				dataBaseURL2.append("?user=").append(props.getProperty("DataBase.username"))
						.append("&password=").append(props.getProperty("DataBase.password"));
			}
			//con = DriverManager.getConnection(dataBaseURL2.toString());
			System.out.println(dataBaseURLTest2.toString());
			//[AH] 2020-05-16
			con = DriverManager.getConnection(dataBaseURLTest2.toString());
		}
		return con;
	}

	public static synchronized ResultSet query(String sql) throws Exception {
		return getConnection().createStatement().executeQuery(sql);
	}

	public static void resetDatabase(String fileName) throws SQLException {
		try {
			Connection conn = getConnection();
			if (dataBaseType.equals("sqlserver")) {
				String query = "Use master Alter Database " + DATA_BASE_NAME + " SET SINGLE_USER With ROLLBACK IMMEDIATE RESTORE DATABASE " + DATA_BASE_NAME + " FROM DISK ="
						+ fileName
						+ " WITH REPLACE ALTER DATABASE " + DATA_BASE_NAME + " SET MULTI_USER";
				Statement st = conn.createStatement();
				st.executeUpdate(query);
			}
			if (dataBaseType.equals("mysql")) {
				String s = new String();
				StringBuffer sb = new StringBuffer();

				BufferedReader br = new BufferedReader(new InputStreamReader(
					    new FileInputStream(fileName), "UTF-8"));

				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				br.close();

				String[] inst = sb.toString().split(";");

				Connection c = SQLtoDB.getConnection();
				Statement st = c.createStatement();

				for (int i = 0; i < inst.length; i++) {
					if (!inst[i].trim().equals("")) {
						st.executeUpdate(inst[i]);
						System.out.println(">>" + inst[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//
//		try {
//			String pathfile = "'C:/GBO/SQLServerBackups/GBO_BD_BackUp_file_2014-05-07_12-16-05.Bak'";
//			System.out.println(pathfile);
//			resetDatabase(pathfile);
//			System.out.println("*** succes");
//		} catch (SQLException e) {
//			System.err.println("*** Error");
//			e.printStackTrace();
//		}
//	}

}
