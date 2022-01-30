package cloudera.pse.demo.knox;

import java.sql.*;

public class KnoxJDBCConnection
{
  public static String JDBC_URL = "";
  static
  {
    String HOST = "ip-10-0-160-242.us-west-1.compute.internal:8443";
    String DB_URL = "jdbc:hive2://" + HOST + "/;ssl=true;";
    String TRUST_STORE = "sslTrustStore=/etc/pki/java/cacerts;trustStorePassword=changeit;";
    String KNOX_PROXY =  "transportMode=http;httpPath=gateway/cdp-proxy-api/hive;";
    JDBC_URL = DB_URL + TRUST_STORE + KNOX_PROXY;
  }
  static final String USER = "knox_user";
  static final String PASS = "password";
  static final String QUERY = "SHOW DATABASES";

  public static void main(String[] args)
  {
    try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(QUERY);)
    {
      while (rs.next())
      {
        System.out.printf("Databases: %s\n", rs.getString("database_name"));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}
