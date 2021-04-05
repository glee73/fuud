package edu.brown.cs.student.foodcrawl.DBCommands;

import java.io.File;
import java.sql.*;

public class DBConnectionManager {
  /**
   * necessary SQL variables.
   */
  private Connection conn;
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
      setupTables();
    } catch (ClassNotFoundException | SQLException c) {
      System.out.println("ERROR: issue connecting to given db");
    }
  }

  private void setupTables() throws SQLException {
    PreparedStatement prep1 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS restaurants (" +
      "rest_id varchar(255) NOT NULL," +
      "latitude double NOT NULL," +
      "longitude double NOT NULL," +
      "name varchar(255))," +
    "PRIMARY KEY(rest_id)");
    prep1.execute();
    prep1.close();

    PreparedStatement prep2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users (" +
      "userID varchar(255) NOT NULL," +
      "name varchar(255) NOT NULL," +
      "email varchar(255) NOT NULL," +
      "PRIMARY KEY (userID) ");
    prep2.execute();
    prep2.close();

    PreparedStatement prep3 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS posts (" +
      "postID varchar(255) NOT NULL," +
      "stars int," +
      "restaurant_id varchar(255) NOT NULL," +
      "user_id varchar(255) NOT NULL," +
      "review_text varchar(1000))," +
      "PRIMARY KEY (postID)," +
    "FOREIGN KEY restaurant_id REFERENCES restaurants(rest_id)," +
      "FOREIGN KEY user_id REFERENCES users(userID)");
    prep3.execute();
    prep3.close();

    PreparedStatement prep4 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS friends (" +
      "id1 varchar(255) NOT NULL," +
      "id2 varchar(255) NOT NULL");
    prep4.execute();
    prep4.close();

    PreparedStatement prep5 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS photos (" +
      "postID varchar(255) NOT NULL," +
      "photoPath varchar(255) NOT NULL," +
      "FOREIGN KEY postID REFERENCES posts(postID),");
    prep5.execute();
    prep5.close();

  }




}
