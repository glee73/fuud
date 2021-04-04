package edu.brown.cs.student.stars.KDTree;

import edu.brown.cs.student.stars.DataTypes.DataType;

import java.util.Comparator;

/**
 * Comparator used in KD tree. Does not handle randomizing values when
 * their distances are equal
 */
public class KDComparatorNoRandom implements Comparator<DataType> {

  private int dim;

  /**
   * Class constructor.
   */
  public KDComparatorNoRandom() { }


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
      return 0;
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
