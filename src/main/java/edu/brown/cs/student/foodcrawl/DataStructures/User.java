package edu.brown.cs.student.foodcrawl.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class User {

  private final String username;
  private final String password;
  private final List<String> following;
  private final List<String> followers;
  private String bio;

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
    this.bio = "";
  }

  /**
   * user constructor with all fields populated.
   * @param username
   * @param password
   * @param followers
   * @param following
   */
  public User(String username, String password, List<String> followers, List<String> following, String bio) {
    this.username = username;
    this.password = password;
    this.followers = followers;
    this.following = following;
    this.bio = bio;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {return password;}

  public List<String> getFollowing() {
    return following;
  }

  public List<String> getFollowers() {
    return followers;
  }

  public String getBio() {
    return bio;
  }
}
