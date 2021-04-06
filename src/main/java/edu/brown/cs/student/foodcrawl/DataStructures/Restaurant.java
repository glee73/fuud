package edu.brown.cs.student.foodcrawl.DataStructures;

import edu.brown.cs.student.foodcrawl.UtilityFunctions.GenerateHashID;

import java.util.List;

public class Restaurant {
  private final String id;
  private final String name;
  private final Double latitude;
  private final Double longitude;
  private final List<String> tags;

  public Restaurant (String n, Double lat, Double longi, List<String> tags, String inputID) {
    this.name = n;
    if (inputID == null) {
      this.id = GenerateHashID.generateUUID();
    } else {
      this.id = inputID;
    }
    this.latitude = lat;
    this.longitude = longi;
    this.tags = tags;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public List<String> getTags() {
    return tags;
  }
}
