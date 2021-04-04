package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.DataTypes.Star;

import java.util.Comparator;

/**
 * This class is a comparator that is used for the naive implementation of commands
 * (naive_radius and naive_neighbors). It sorts stars from closest to farthest distance
 * from a set target.
 */
public class SortByStar implements Comparator<Star> {
  private double x;
  private double y;
  private double z;
  private static final double CHANCE = 0.5;

  /**
   * Class constructor.
   */
  public SortByStar() {
  }

  /**
   * Star comparator that is used for the naive commands implementation
   * (naive_neighbors and naive_radius).
   *
   * @param o1 Star 1, whose distance from a target location will be compared with that of Star 2
   * @param o2 Star 2
   * @return an int (-1 or 1)
   * returns 1 if o1 distance from target is greater than o2 distance from target
   * returns -1 if o1 distance from target is less than o2 distance from target
   * returns -1 or 1 at random if o1 distance from target is equal to o2 distance from target
   */
  @Override
  public int compare(Star o1, Star o2) {
    double o1Dist = Math.pow(x - o1.getX(), 2) + Math.pow(y - o1.getY(), 2)
        + Math.pow(z - o1.getZ(), 2);
    double o2Dist = Math.pow(x - o2.getX(), 2) + Math.pow(y - o2.getY(), 2)
        + Math.pow(z - o2.getZ(), 2);
    //handling randomization if 2 stars are equal in distance
    if (o1Dist - o2Dist == 0) {
      double rand = Math.random(); //will get a random number between [0, 1)
      if (rand >= 0 && rand < CHANCE) {
        return -1;
      }
      if (rand >= CHANCE) {
        return 1;
      }
    } else {
      if (o1Dist - o2Dist > 0) {
        return 1;
      } else {
        return -1;
      }
    }
    return -1;
  }

  /**
   * Public setter method.
   *
   * @param xIn sets the x value of the target
   */
  public void setX(double xIn) {
    x = xIn;
  }

  /**
   * Public setter method.
   *
   * @param yIn sets the y value of the target
   */
  public void setY(double yIn) {
    y = yIn;
  }

  /**
   * Public setter method.
   *
   * @param zIn sets the z value of the target
   */
  public void setZ(double zIn) {
    z = zIn;
  }

}
