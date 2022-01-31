package com.cloudera.pse.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class KnoxJDBCConnection
{
  public static String JDBC_URL = "";
  public static String JDBC_USER = "";
  public static String JDBC_PASSWORD = "";
  public static String JDBC_QUERY = "";
  static
  {
    try (InputStream input = new FileInputStream(".." + File.separator + "conf"
           + File.separator + "jdbc.properties"))
    {
      Properties prop = new Properties();
      prop.load(input);
      String protocol = prop.getProperty("PROTOCOL");
      String host = prop.getProperty("HOST");
      String trustStore = prop.getProperty("TRUST_STORE");
      String knoxProxy = prop.getProperty("KNOX_PROXY");

      JDBC_URL = protocol + host + ";" + trustStore + ";" + knoxProxy + ";";
      JDBC_USER = prop.getProperty("KNOX_USER");
      JDBC_PASSWORD = prop.getProperty("KNOX_PASS");
      JDBC_QUERY = prop.getProperty("QUERY");

      System.out.println("Executing Query: " + JDBC_QUERY);
      System.out.println("Using URL: " + JDBC_URL);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }
  
  public static void main(String[] args)
  {
    try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(JDBC_QUERY);)
    {    
      while (rs.next())
      {
        System.out.println(rs.getObject(1));
      } 
    } 
    catch (SQLException e)
    {
      e.printStackTrace();
    } 
  } 
} 


