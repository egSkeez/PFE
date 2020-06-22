package xtensus.beans.common.sauvgardeRestauration;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 
 * @author HB
 */
public class DB2Sql {

	private static Connection con;
    private static String DATA_BASE_NAME;
	private static String sauvegardePath;
	private static String dataBaseType;
    
	public static Connection getConnection() throws Exception {
		// Load the SQLServerDriver class, build the
		// connection string, and get a connection
		try {
			if (con == null || con.isClosed()) {
				Resource rsrc = new ClassPathResource("/paramDataBase.properties");
				String pathConfigFile = rsrc.getFile().getAbsolutePath();
				Properties props = new Properties();
				props.load(new FileInputStream(pathConfigFile));
				String dataBaseDriver = props.getProperty("DataBase.driver");
				Class.forName(dataBaseDriver); //.newInstance();
				String dataBaseUrl = props.getProperty("DataBase.url");
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
					dataBaseURL2.append("?user=").append(props.getProperty("DataBase.username"))
							.append("&password=").append(props.getProperty("DataBase.password"));
				}
				con = DriverManager.getConnection(dataBaseURL2.toString());
				sauvegardePath = props.getProperty("DataBase.sauvegardePath");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static synchronized ResultSet query(String sql) throws Exception {
		return getConnection().createStatement().executeQuery(sql);
	}

	public static void dumpDatabase(String Filename) {
		try {
			Connection conn = getConnection();
			if(dataBaseType.equals("sqlserver")){
				String query = "BACKUP DATABASE " + DATA_BASE_NAME +" TO DISK = "
					+ Filename;
				Statement st = conn.createStatement();
				st.executeUpdate(query);
			}
			if(dataBaseType.equals("mysql")){
				File parent = new File(sauvegardePath);
				if (!parent.mkdirs()) {
				   System.err.println("Could not create parent directories ");
				}
				BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(
					    new FileOutputStream(Filename), "UTF-8"));

				StringBuilder sb = new StringBuilder();
				sb.append("SET FOREIGN_KEY_CHECKS=0;");
				sb.append("\n");

				ResultSet rs = query("SHOW FULL TABLES WHERE Table_type != 'VIEW'");
				while (rs.next()) {
					String tbl = rs.getString(1);

					sb.append("\n");
					sb.append("-- ----------------------------\n")
							.append("-- Table structure for `").append(tbl)
							.append("`\n-- ----------------------------;\n");
					sb.append("DROP TABLE IF EXISTS `").append(tbl).append("`;\n");
					ResultSet rs2 = query("SHOW CREATE TABLE `" + tbl + "`");
					rs2.next();
					String crt = rs2.getString(2) + ";";
					sb.append(crt).append("\n");
					sb.append("\n");
					sb.append("-- ----------------------------\n")
							.append("-- Records for `").append(tbl)
							.append("`\n-- ----------------------------;\n");

					ResultSet rss = query("SELECT * FROM " + tbl);
					while (rss.next()) {
						int colCount = rss.getMetaData().getColumnCount();
						if (colCount > 0) {
							sb.append("INSERT INTO ").append(tbl)
									.append(" VALUES(");

							for (int j = 0; j < colCount; j++) {
								if (j > 0) {
									sb.append(",");
								}

								String s = "";
								try {
									s += "'";
									s += rss.getObject(j + 1).toString().replaceAll("'","''");;
									s += "'";
								} catch (Exception e) {
									s = "NULL";
								}
								sb.append(s);
							}
							sb.append(");\n");
							buff.append(sb.toString());
							sb = new StringBuilder();
						}
					}
				}

				ResultSet rs2 = query("SHOW FULL TABLES WHERE Table_type = 'VIEW'");
				while (rs2.next()) {
					String tbl = rs2.getString(1);

					sb.append("\n");
					sb.append("-- ----------------------------\n")
							.append("-- View structure for `").append(tbl)
							.append("`\n-- ----------------------------\n");
					sb.append("DROP VIEW IF EXISTS `").append(tbl).append("`;\n");
					ResultSet rs3 = query("SHOW CREATE VIEW `" + tbl + "`");
					rs3.next();
					String crt = rs3.getString(2) + ";";
					sb.append(crt).append("\n");
				}

				buff.flush();
				buff.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * public static void main(String[] args) { Date d = new Date();
	 * SimpleDateFormat dateFormat = new SimpleDateFormat(
	 * "yyyy-mm-dd HH:mm:ss"); String s = "_" + dateFormat.format(d); s =
	 * s.replaceAll(":", "-"); s = s.replaceAll("\\s", "_");
	 * System.out.println(s); String path="'C:/SQLServerBackups/GBO_BackUp_file"
	 * + s + ".Bak'"; dumpDatabase(path); }
	 */
}