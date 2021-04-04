package edu.brown.cs.student.stars.Commands;

import java.util.Comparator;

/**
 * A comparator class that is used for alphabetical sorting.
 */
public class AlphabeticalComparator implements Comparator<String> {


  /**
   * Class constructor.
   */
  public AlphabeticalComparator() { }


  /**
   * Comparator used in Ways command to sort alphabetically.
   * @param o1 String 1 to be compared
   * @param o2 String 2 to be compared
   * @return
   */
  @Override
  public int compare(String o1, String o2) {
    return o1.compareTo(o2);
  }

}
