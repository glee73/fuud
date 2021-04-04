package edu.brown.cs.student.stars.ThreadUserCheckin;

/**
 * class for passing user checkin data.
 */
public class UserCheckin {

  private int id;
  private String name;
  private double ts;
  private double lat;
  private double lon;

  /**
   * Class constructor.
   * @param userId integer representing user ID.
   * @param username String of user's name
   * @param timestamp timestamp of user's checkin
   * @param latitude latitude of checkin location
   * @param longitude longitude of checkin location
   */
  public UserCheckin(
      int userId,
      String username,
      double timestamp,
      double latitude,
      double longitude) {
    id = userId;
    name = username;
    ts = timestamp;
    lat = latitude;
    lon = longitude;
  }

  /**
   * Accessor method.
   * @return user id
   */
  public int getId() {
    return id;
  }

  /**
   * Accessor method.
   * @return Name of user
   */
  public String getName() {
    return name;
  }

  /**
   * Accessor method.
   * @return timestamp of checkin
   */
  public double getTimestamp() {
    return ts;
  }

  /**
   * Accessor method.
   * @return latitude of checkin location
   */
  public double getLat() {
    return lat;
  }

  /**
   * Accessor method.
   * @return longitude of checkin location
   */
  public double getLon() {
    return lon;
  }

}
