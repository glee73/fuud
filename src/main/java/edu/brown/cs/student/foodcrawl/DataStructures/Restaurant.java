package edu.brown.cs.student.foodcrawl.DataStructures;

import edu.brown.cs.student.foodcrawl.UtilityFunctions.GenerateHashID;

import java.util.List;

public class Restaurant {
  private final String id;
  private final String name;
  private final String address;
  private final List<String> tags;

  public Restaurant(String n, String address, List<String> tags, String inputID) {
    this.name = n;
    if (inputID == null) {
      this.id = GenerateHashID.generateUUID();
    } else {
      this.id = inputID;
    }
    this.address = address;
    this.tags = tags;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public List<String> getTags() {
    return tags;
  }
}
