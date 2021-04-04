package edu.brown.cs.student.stars.DataTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the parameters for a Star, which implements DataType.
 * It is used to store the appropriate information for each star.
 */
public class Star implements DataType {
  private double x;
  private double y;
  private double z;
  private String properName;
  private int starId;
  private Map<Integer, Double> dimensions;
  private String uniqueId;

  /**
   * Class constructor.
   * @param newStarId int representing the star ID
   * @param newProperName string of the star's propername
   * @param xIn double of the star's x location
   * @param yIn double of the star's y location
   * @param zIn double of the star's z location
   */
  public Star(int newStarId, String newProperName, double xIn, double yIn, double zIn) {
    x = xIn;
    y = yIn;
    z = zIn;
    properName = newProperName;
    starId = newStarId;
    dimensions = new HashMap<Integer, Double>();
    this.setDimensions();
  }

  /**
   * Public accessor method, that returns star's x value.
   * @return x
   */
  public double getX() {
    return this.x;
  }

  /**
   * Public accessor method, that returns star's y value.
   * @return y
   */
  public double getY() {
    return this.y;
  }

  /**
   * Public accessor method, that returns star's z value.
   * @return z
   */
  public double getZ() {
    return this.z;
  }

  /**
   * Public accessor method, that returns star's proper name.
   * @return properName
   */
  public String getProperName() {
    return this.properName;
  }

  /**
   * Public accessor method, that returns star's ID.
   * @return starId
   */
  public int getstarID() {
    return this.starId;
  }

  /**
   * Method implemented from DataType interface.
   * Allows the dimension of the star to be set, so that a star can act as a DataType,
   * and thus be used in KDNodes
   */
  @Override
  public void setDimensions() {
    dimensions.put(1, x);
    dimensions.put(2, y);
    dimensions.put(3, z);

  }

  /**
   * Method implemented from DataType interface.
   * retreives value of an inputted dimension
   * @param dim a dimension
   * @return dimension value of star
   */
  @Override
  public Double getDimensions(int dim) {
    return dimensions.get(dim);
  }

  /**
   * Method implemented from DataType interface.
   * Used to identify Stars for KDNodes
   * @return a unique ID value for each Star object
   */
  @Override
  public String getID() {
    return String.valueOf(getstarID());
  }

}
