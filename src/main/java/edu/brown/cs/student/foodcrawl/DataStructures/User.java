package edu.brown.cs.student.foodcrawl.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class User {

  private final String username;
  private final String password;
  private final List<String> following;
  private final List<String> followers;
  private final List<String> pinned;
  private String bio;
  private String pic;

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
    this.pinned = new ArrayList<>();
    this.bio = "";
  }

  /**
   * user constructor with all fields populated.
   * @param username
   * @param password
   * @param followers
   * @param following
   * @param bio
   */
  public User(String username, String password, List<String> followers, List<String> following, List<String> pinned, String bio, String pic) {
    this.username = username;
    this.password = password;
    this.followers = followers;
    this.following = following;
    this.pinned = pinned;
    this.bio = bio;
    this.pic = pic;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() { return password;}

  public List<String> getFollowing() {
    return following;
  }

  public List<String> getFollowers() {
    return followers;
  }

  public String getBio() {
    return bio;
  }

  public String getPic() {
    return pic;
  }

  public List<String> getPinned() {return pinned;}
}
