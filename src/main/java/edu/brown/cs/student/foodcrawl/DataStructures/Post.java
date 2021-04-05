package edu.brown.cs.student.foodcrawl.DataStructures;

import edu.brown.cs.student.foodcrawl.UtilityFunctions.GenerateHashID;

import java.util.List;

public class Post {
  private final String description;
  private final int reviewOutOfTen;
  private final List<String> pictures; //urls to pictures
  private final String restaurantName;
  private final String id;
  private final String user;

  public Post(String description, int reviewOutOfTen, List<String> pictures, String username,
              String restaurantName) {
    this.description = description;
    this.reviewOutOfTen = reviewOutOfTen;
    this.pictures = pictures;
    this.id = GenerateHashID.generateUUID();
    this.restaurantName = restaurantName;
    this.user = username;
  }


  public String getDescription() {
    return description;
  }

  public int getReviewOutOfTen() {
    return reviewOutOfTen;
  }

  public List<String> getPictures() {
    return pictures;
  }

  public String getId() {
    return id;
  }

  public String getRestaurantName() {
    return restaurantName;
  }

  public String getUser() {
    return user;
  }
}
