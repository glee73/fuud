package edu.brown.cs.student.foodcrawl.DataStructures;

import edu.brown.cs.student.foodcrawl.UtilityFunctions.GenerateHashID;

import java.util.ArrayList;
import java.util.List;

public class User {

  String username;
  List<String> following;
  String id;

  /**
   * invariant that the username for each user is unique!!
   * @param username username string
   */
  public User(String username) {
    this.username = username;
    this.following = new ArrayList<>();
    this.id = GenerateHashID.generateUUID();
  }
}