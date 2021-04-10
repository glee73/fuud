package edu.brown.cs.student.foodcrawl.DBCommands;

import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.Restaurant;
import edu.brown.cs.student.foodcrawl.DataStructures.User;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnectionManager {
  /**
   * necessary SQL variables.
   */
  private Connection conn;

  public DBConnectionManager() {
    try {
      Class.forName("org.mysql.Driver");
      String dbName = "theDatabase";
      String userName = "cs32";
      String password = "Ihatethisclass2!";
      String hostname = "foodcrawlv2.cuykllbkmivp.us-east-2.rds.amazonaws.com";
      String port = "3306";
      String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
      // for checking if the file exists
      this.conn = DriverManager.getConnection(jdbcUrl);
      System.out.println("ERROR: db file not found");
      setupTables();
    } catch (ClassNotFoundException | SQLException c) {
      System.out.println("ERROR: issue connecting to given db");
    }
  }

  private void setupTables() throws SQLException {
    PreparedStatement prep1 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS restaurants (" +
      "DISTINCT rest_id varchar(255) NOT NULL," +
      "address varchar(255) NOT NULL," +
      "name varchar(255)," +
    "PRIMARY KEY(rest_id))");
    prep1.execute();
    prep1.close();

    PreparedStatement prep2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users (" +
      "DISTINCT username varchar(255) NOT NULL," +
      "email varchar(255)," +
      "password varchar(255)," +
      "PRIMARY KEY (username)) ");
    prep2.execute();
    prep2.close();

    PreparedStatement prep3 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS posts (" +
      "DISTINCT postID varchar(255) NOT NULL," +
      "stars int," +
      "rest_id varchar(255) NOT NULL," +
      "username varchar(255) NOT NULL," +
      "timestamp int NOT NULL," +
      "review_text varchar(1000)," +
      "PRIMARY KEY (postID)," +
    "FOREIGN KEY (rest_id) REFERENCES restaurants(rest_id)," +
      "FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE)");
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
      "FOREIGN KEY (postID) REFERENCES posts(postID) ON DELETE CASCADE)");
    prep5.execute();
    prep5.close();

    PreparedStatement prep6 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tags (" +
      "restID varchar(255) NOT NULL," +
      "tag varchar(255) NOT NULL," +
      "FOREIGN KEY (restID) REFERENCES restaurants(rest_id) ON DELETE CASCADE)");
    prep6.execute();
    prep6.close();
  }

  public void addUser(User u) throws SQLException{
    PreparedStatement adduser = conn.prepareStatement("INSERT INTO users (username) VALUES (?)");
    adduser.setString(1, u.getUsername());
    adduser.execute();
    adduser.close();
  }

  public void addFollow(User follower, User target) throws SQLException {
    PreparedStatement a = conn.prepareStatement("INSERT INTO follower (f_id, target_id) VALUES (?, ?)");
    a.setString(1, follower.getUsername());
    a.setString(2, target.getUsername());
    a.execute();
    a.close();
  }

  public void addPost(Post p) throws SQLException{
    PreparedStatement a = conn.prepareStatement("INSERT INTO posts " +
      "(postID, stars, rest_id, user_id, review_text, timestamp) VALUES (?, ?, ?, ?, ?, ?)");
    a.setString(1, p.getId());
    a.setInt(2, p.getReviewOutOfTen());
    a.setString(3, p.getRestaurantID());
    a.setString(4, p.getUser());
    a.setString(5, p.getDescription());
    a.setString(6, p.getTimestamp());
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
      "(rest_id, address, name) VALUES (?, ?, ?)");
    p.setString(1, r.getId());
    p.setString(2, r.getAddress());
    p.setString(3, r.getName());
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

  public List<Restaurant> restaurantsMatchingTags(List<String> tags, boolean and) throws SQLException{
    StringBuilder statement = new StringBuilder();
    String conj = "OR";
    if (and) {
      conj = "AND";
    }
    int i = 0;
    for (String t : tags) {
      if (i == tags.size() - 1) {
        statement.append(t);
      } else {
        statement.append(t).append(" ").append(conj).append(" ");
      }
      i++;
    }
    String sql = "SELECT DISTINCT r.rest_id, r.address, r.name " +
      "FROM restaurants AS r INNER JOIN tags on r.rest_id = tags.restID " +
      "WHERE " + statement.toString();
    PreparedStatement p = conn.prepareStatement(sql);
    ResultSet rs = p.executeQuery();
    List<Restaurant> output = new ArrayList<>();
    p.close();
    while (rs.next()) {
      PreparedStatement p2 = conn.prepareStatement("SELECT tag FROM tags WHERE restID = ?");
      p2.setString(1, rs.getString("rest_id"));
      ResultSet rs2 = p2.executeQuery();
      List<String> taglist = new ArrayList<>();
      p.close();
      while (rs2.next()) {
        taglist.add(rs2.getString("tag"));
      }
      output.add(new Restaurant(rs.getString("name"), rs.getString("address"), taglist, rs.getString("rest_id")));
      rs2.close();
    }
    rs.close();
    return output;
  }

  public void deleteUserData(String username) throws SQLException{
    PreparedStatement p = conn.prepareStatement("DELETE FROM users WHERE username = ?");
    p.setString(1, username);
    p.execute();
    p.close();
  }

  public void check() throws SQLException {
    PreparedStatement p = conn.prepareStatement("SELECT * FROM users");
    ResultSet rs = p.executeQuery();
    rs.next();
    p.close();
    rs.close();
    System.out.println(rs.getString("username"));
  }

}
