package edu.brown.cs.student.stars.Graph;

import java.util.Comparator;

/**
 * Comparator class that is used for comparing Vertices using the haversine distance.
 * @param <V>
 * @param <E>
 */
public class CompareHaversine<V extends Vertex<V, E>,
    E extends Edge<V, E>> implements Comparator<V> {

  private static final double CHANCE = 0.5;

  /**
   * Class Constructor.
   */
  public CompareHaversine() { }

  @Override
  public int compare(V o1, V o2) {
    if (o1.getDistance() > o2.getDistance()) {
      return 1;
    } else if (o1.getDistance() < o2.getDistance()) {
      return -1;
    } else {
      double rand = Math.random(); //will get a random number between [0, 1)
      if (rand >= 0 && rand < CHANCE) {
        return -1;
      } else if (rand >= CHANCE) {
        return 1;
      }
    }
    return 1;
  }

}
