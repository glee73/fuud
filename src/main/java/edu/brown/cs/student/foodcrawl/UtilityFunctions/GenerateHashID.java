package edu.brown.cs.student.foodcrawl.UtilityFunctions;

import java.util.UUID;

public class GenerateHashID {

  /**
   * generates a UUID string
   * @return a string representation of the unique id
   */
  public static String generateUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
