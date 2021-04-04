package edu.brown.cs.student.stars.Graph;

import java.util.Comparator;

/**
 * Comparator that is used for A* implemenation.
 *  * Uses extra calculation for weight in a Priority queue.
 * @param <V>
 * @param <E>
 */
public class AStarComparator<V extends Vertex<V, E>,
    E extends Edge<V, E>> implements Comparator<V> {

//  private static final double CHANCE = 0.5;
  private V endNode = null;

  /**
   * Class Constructor.
   */
  public AStarComparator() { }

  @Override
  public int compare(V o1, V o2) {
    if (endNode == null) {
      System.out.println("error: endNode never set for A* comparator");
      return 0;
    }
    double o1Dist = o1.getDistance() + o1.getDijkstraHeuristic(endNode);
    double o2Dist = o2.getDistance() + o2.getDijkstraHeuristic(endNode);
    return Double.compare(o1Dist, o2Dist);
  }

  /**
   * Method that sets the end node, which is used for the A* calculation.
   * @param end endNode
   */
  public void setEnd(V end) {
    endNode = end;
  }


}
