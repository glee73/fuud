package edu.brown.cs.student.foodcrawl.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class User {

  String username;
  List<String> following;

  /**
   * invariant that the username for each user is unique!!
   * @param username
   */
  public User(String username) {
    this.username = username;
    this.following = new ArrayList<>();
  }
}