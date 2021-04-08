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
  private final int timestamp;

  public Post(String description, int reviewOutOfTen, List<String> pictures, String username,
              String restaurantName, String inputID, int ts) {
    this.description = description;
    this.reviewOutOfTen = reviewOutOfTen;
    this.pictures = pictures;
    if (inputID == null) {
      this.id = GenerateHashID.generateUUID();
    } else {
      this.id = inputID;
    }
    this.restaurantName = restaurantName;
    this.user = username;
    this.timestamp = ts;
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

  public int getTimestamp() {
    return timestamp;
  }
}
