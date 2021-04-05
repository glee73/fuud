package edu.brown.cs.student.foodcrawl.DBCommands;

import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.Restaurant;
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
      "rest_id varchar(255) NOT NULL," +
      "user_id varchar(255) NOT NULL," +
      "review_text varchar(1000))," +
      "PRIMARY KEY (postID)," +
    "FOREIGN KEY rest_id REFERENCES restaurants(rest_id)," +
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

    PreparedStatement prep6 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tags (" +
      "restID varchar(255) NOT NULL," +
      "tag varchar(255)) NOT NULL," +
      "FOREIGN KEY restID REFERENCES restaurants(rest_id)");
    prep6.execute();
    prep6.close();
  }

  public void addUser(User u) throws SQLException{
    PreparedStatement adduser = conn.prepareStatement("INSERT INTO users (userID, username) VALUES (?, ?)");
    adduser.setString(1, u.getID());
    adduser.setString(2, u.getUsername());
    adduser.execute();
    adduser.close();
  }

  public void addFollow(User follower, User target) throws SQLException {
    PreparedStatement a = conn.prepareStatement("INSERT INTO follower (f_id, target_id) VALUES (?, ?)");
    a.setString(1, follower.getID());
    a.setString(2, target.getID());
    a.execute();
    a.close();
  }

  public void addPost(Post p) throws SQLException{
    PreparedStatement a = conn.prepareStatement("INSERT INTO posts " +
      "(postID, stars, rest_id, user_id, review_text) VALUES (?, ?, ?, ?, ?)");
    a.setString(1, p.getId());
    a.setInt(2, p.getReviewOutOfTen());
    a.setString(3, p.getRestaurantName());
    a.setString(4, p.getUser());
    a.setString(5, p.getDescription());
    a.execute();
    a.close();

    for (String photo : p.getPictures()) {
      PreparedStatement a2 = conn.prepareStatement("INSERT INTO photos" +
        "(postID, photoPath) VALUES (?, ?)");
      a2.setString(1, p.getId());
      a2.setString(2, photo);
      a2.execute();
      a2.close();
    }
  }

  public void addRestaurant(Restaurant r) throws SQLException{
    PreparedStatement p = conn.prepareStatement("INSERT INTO restaurants " +
      "(rest_id, latitude, longitude, name) VALUES (?, ?, ?, ?)");
    p.setString(1, r.getId());
    p.setDouble(2, r.getLatitude());
    p.setDouble(3, r.getLongitude());
    p.setString(4, r.getName());
    p.execute();
    p.close();

    for (String tag : r.getTags()) {
      PreparedStatement p2 = conn.prepareStatement("INSERT INTO tags" +
        "(restID, tag) VALUES (?, ?)");
      p2.setString(1, r.getId());
      p2.setString(2, tag);
      p2.execute();
      p2.close();
    }
  }

}
