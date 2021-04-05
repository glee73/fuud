package edu.brown.cs.student.foodcrawl.DataStructures;

import edu.brown.cs.student.foodcrawl.UtilityFunctions.GenerateHashID;

import java.util.List;

public class Post {
  private final String description;
  private final int reviewOutOfTen;
  private final List<String> pictures; //urls to pictures
  private final String restaurantID;
  private final String id;
  private final String userID;

  public Post(String description, int reviewOutOfTen, List<String> pictures, String username,
              String restaurantName, String restaurantLocation) {
    this.description = description;
    this.reviewOutOfTen = reviewOutOfTen;
    this.pictures = pictures;
    this.id = GenerateHashID.generateUUID();
    this.restaurantID = " ";
    this.userID = " ";

    // TODO: query the DB and see if restaurant with same name + location already exists
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

  public String getRestaurantID() {
    return restaurantID;
  }

  public String getUserID() {
    return userID;
  }
}
