package edu.brown.cs.student.foodcrawl.DBCommands;

import java.io.File;
import java.sql.*;

public class DBConnectionManager {
  /**
   * necessary SQL variables.
   */
  private Connection conn;
  private PreparedStatement prep;
  private ResultSet rs;

  public DBConnectionManager(String filename) {
    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + filename;
      // for checking if the file exists
      File f = new File(filename);
      if (f.exists()) {
        this.conn = DriverManager.getConnection(urlToDB);
      } else {
        System.out.println("ERROR: db file not found");
      }
    } catch (ClassNotFoundException | SQLException c) {
      System.out.println("ERROR: issue connecting to given db");
    }
  }




}
