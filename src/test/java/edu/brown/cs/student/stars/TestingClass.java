package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.Commands.*;
import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.KDTree.KDNode;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Testing class for miscellaneous tests: repl, mock, Comparators
 */
public class TestingClass {


  /**
   * tests that mock command throws exception when filename is not valid
   * @throws FileNotFoundException when filename is not valid
   */
  @Test
  public void mockFakeFile() throws FileNotFoundException{
    Mock mock = new Mock();
    List<String> input = new ArrayList<>();
    input.add("mock");
    input.add("fakefile");
    mock.run(input);
  }


  /**
   * Simple test of the NonNaiveComparator, which is used for radius and neighbors command.
   */
  @Test
  public void testNonNaiveComparator() {
    NonNaiveComparator comp = new NonNaiveComparator();
    Double[] targetDims = {0.0, 0.0, 0.0};
    comp.setTargetDimensions(targetDims);
    Star star0 = new Star(0, "star0", 0, 0, 0);
    KDNode node0 = new KDNode(star0);
    Star star1 = new Star(1, "star1", 1, 1, 1);
    KDNode node1 = new KDNode(star1);
    Star star2 = new Star(2, "star2", 2, 2, 2);
    KDNode node2 = new KDNode(star2);
    List<KDNode> kdList = new ArrayList<>();
    kdList.add(node1);
    kdList.add(node2);
    kdList.add(node0);
    kdList.sort(comp);
    //should be returned farthest, to closest
    assertEquals(kdList.get(0), node2);
    assertEquals(kdList.get(1), node1);
    assertEquals(kdList.get(2), node0);
  }





}
