package edu.brown.cs.student.foodcrawl.UtilityFunctions;

import edu.brown.cs.student.foodcrawl.DataStructures.Post;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * a class to compare post by timestamp, in order to sort them
 */
public class TimestampComparator implements Comparator<Post> {
  @Override
  public int compare(Post p1, Post p2) {
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy, h:mm:ss a");
    try {
      Date date1 = format.parse(p1.getTimestamp());
      Date date2 = format.parse(p2.getTimestamp());
      if (date1.before(date2))  {
        return -1;
      } else if (date1.after(date2)) {
        return 1;
      } else {
        return 0;
      }
    } catch (Exception e) {
      System.out.println("sorting error");
      return 0;
    }
  }
}
