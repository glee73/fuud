package edu.brown.cs.student.stars.KDTree;
import edu.brown.cs.student.stars.DataTypes.DataType;

import java.util.Comparator;

/**
 * This comparator class is used in the KDTree class to sort KDNodes
 * and appropriately create the KDTree. It compares DataTypes along
 * a certain dimension.
 */
public class KDComparator implements Comparator<DataType> {

  private int dim;
  private static final double CHANCE = 0.5;

  /**
   * Class constructor.
   */
  public KDComparator() { }

  /**
   * Method used in non-naive implementations of commands (radius and neighbors).
   * Compares 2 DataTypes along a set Dimension
   * @param o1 DataType 1
   * @param o2 DataType 2
   * @return an int 1 or -1, where 1 means o1's dimension value is greater than o2's dimension value
   * and -1 means o1's dimension value is less than o2's dimension value
   * Will return -1 or 1 randomly if o1's dimension value equals o2's dimension value
   */
  @Override
  public int compare(DataType o1, DataType o2) {
    if (o1.getDimensions(dim).equals(o2.getDimensions(dim))) {
      double rand = Math.random(); //will get a random number between [0, 1)
      if (rand >= 0 && rand < CHANCE) {
        return -1;
      }
      if (rand >= CHANCE) {
        return 1;
      }
    } else {
      if (o1.getDimensions(dim) - o2.getDimensions(dim) > 0) {
        return 1;
      }
    }
    return -1;
  }
  /**
   * Public setter method, to determine which dimension/axis to compare DataTypes along.
   * @param n an int representing the dimension/axis
   */
  public void setCompareDim(int n) {
    dim = n;
  }

}
