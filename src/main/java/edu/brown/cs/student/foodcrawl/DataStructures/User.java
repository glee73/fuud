package edu.brown.cs.student.foodcrawl.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class User {

  private final String username;
  private final String password;
  private final List<String> following;
  private final List<String> followers;

  /**
   * invariant that the username for each user is unique!!
   * @param username username string
   * @param password string
   */
  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.following = new ArrayList<>();
    this.followers = new ArrayList<>();
  }

  public String getUsername() {
    return username;
  }

  public List<String> getFollowing() {
    return following;
  }

  public List<String> getFollowers() {
    return followers;
  }
}
