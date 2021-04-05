package edu.brown.cs.student.foodcrawl.DBCommands;

import edu.brown.cs.student.foodcrawl.DataStructures.User;

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
      "username varchar(255) NOT NULL," +
      "email varchar(255)," +
      "password varchar(255)," +
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

    PreparedStatement prep4 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS follower (" +
      "f_id varchar(255) NOT NULL," +
      "target_id varchar(255) NOT NULL");
    prep4.execute();
    prep4.close();

    PreparedStatement prep5 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS photos (" +
      "postID varchar(255) NOT NULL," +
      "photoPath varchar(255) NOT NULL," +
      "FOREIGN KEY postID REFERENCES posts(postID),");
    prep5.execute();
    prep5.close();
  }

  public void addUser(User u) throws SQLException{
    PreparedStatement adduser = conn.prepareStatement("INSERT INTO users (userID, username) VALUES (?, ?)");
    adduser.setString(1, u.getID());
    adduser.setString(2, u.getUsername());
    adduser.execute();
    adduser.close();
  }

  public void addFollow(User follower, User target) throws SQLException {
    PreparedStatement a = conn.prepareStatement("INSERT INTO friends (id1, id2) VALUES (?, ?)");
    a.setString(1, follower.getID());
    a.setString(2, target.getID());
    a.execute();
    a.close();
  }
}
