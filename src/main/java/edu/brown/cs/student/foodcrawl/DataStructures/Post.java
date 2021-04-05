package edu.brown.cs.student.foodcrawl.DataStructures;

import java.util.List;

public class Post {
  String description;
  int reviewOutOfTen;
  List<String> pictures; //urls to pictures
  String restaurantID;
  String id;

  public Post(String description, int reviewOutOfTen, List<String> pictures,
              String restaurantName, String restaurantLocation) {
    this.description = description;
    this.reviewOutOfTen = reviewOutOfTen;
    this.pictures = pictures;

    // TODO: query the DB and see if restaurant with same name + location already exists
  }


}
