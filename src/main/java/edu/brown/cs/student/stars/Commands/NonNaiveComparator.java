package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.KDTree.KDNode;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A comparator class that is used for the non-naive commands (Neighbors and Radius).
 * Returns KDNodes sorted from farthest to closest in comparison to a target location.
 */
public class NonNaiveComparator implements Comparator<KDNode> {

  private final HashMap<Integer, Double> targetDimensions = new HashMap<>();

  /**
   * Class comparator.
   */
  public NonNaiveComparator() { }

  /**
   * Comparator that is used for the non-naive commands implementation (neighbors and radius).
   * Sorts KDNodes from farthest to closest in comparison to a target location
   * @param o1 KDNode 1
   * @param o2 KDNode 2
   * @return an int (-1 or 1), 1 if o1 of certain dim is less than o2 of certain dim
   * -1 i o1 of certain dim is greater than o2 of certain dim
   * -1 or 1 at random if o1 of certain dim equals o2 o certain dim
   */
  @Override
  public int compare(KDNode o1, KDNode o2) {

    double o1Dist = 0;
    for (int i = 1; i < targetDimensions.size() + 1; i++) {
      o1Dist += Math.pow(o1.getValue().getDimensions(i) - targetDimensions.get(i), 2);
    }

    double o2Dist = 0;
    for (int j = 1; j < targetDimensions.size() + 1; j++) {
      o2Dist += Math.pow(o2.getValue().getDimensions(j) - targetDimensions.get(j), 2);
    }

    if (o1Dist - o2Dist > 0) {
      return -1;
    }
    if (o1Dist - o2Dist < 0) {
      return 1;
    } else {
      return 0;
    }
  }


  /**
   * This public setter method is used to set the target node's dimensions.
   * @param dims A list of the target node's (the node that will be used as reference
   * of comparison) dimension's values. For example, for a node that has a Star as
   * it's DataType, it will pass in a list where dims = {x, y, z}
   */
  public void setTargetDimensions(Double[] dims) {
    for (int i = 1; i < dims.length + 1; i++) {
      targetDimensions.put(i, dims[i - 1]);
    }
  }

}
