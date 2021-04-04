package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.DataTypes.DataType;
import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.KDTree.KDComparator;
import edu.brown.cs.student.stars.KDTree.KDNode;
import edu.brown.cs.student.stars.KDTree.KDTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class KDTreeTest {

  /**
   * A simple test of the KDComparator
   */
  @Test
  public void testKDComparatorSimple() {
    Star star0 = new Star(0, "star0", 0, 0, 0);
    Star star1 = new Star(1, "star1", 1, -1, 0);
    Star star2 = new Star(2, "star2", -1, 1, 0);
    List<Star> list = new ArrayList<>();
    list.add(star0);
    list.add(star1);
    list.add(star2);
    KDComparator comparator = new KDComparator();
    comparator.setCompareDim(1);
    list.sort(comparator);
    assertEquals(list.get(0), star2);
    assertEquals(list.get(1), star0);
    assertEquals(list.get(2), star1);
    comparator.setCompareDim(2);
    list.sort(comparator);
    assertEquals(list.get(0), star1);
    assertEquals(list.get(1), star0);
    assertEquals(list.get(2), star2);
  }

  /**
   * Testing the set up of a simple KDtree using 3 stars
   */
  @Test
  public void testSimpleTree() {
    Star star0 = new Star(0, "star0", 0, 0, 0);
    Star star1 = new Star(1, "star1", 1, -1, 0);
    Star star2 = new Star(2, "star2", -1, 1, 0);
    List<DataType> list = new ArrayList<>();
    list.add(star0);
    list.add(star1);
    list.add(star2);
    KDTree<DataType> tree = new KDTree<>(3, list, true);
    KDNode root = tree.getRoot();
    assertEquals(root.getValue(), star0);
    assertEquals(root.getLeft().getValue(), star2);
    assertEquals(root.getRight().getValue(), star1);
    assertEquals(tree.getNumDimensions(), 3);
  }

  /**
   * Tests that a tree with only one node can be created
   */
  @Test
  public void testOneNodeTree() {
    Star star0 = new Star(0, "star0", 0, 0, 0);
    List<DataType> list = new ArrayList<>();
    list.add(star0);
    KDTree<DataType> tree = new KDTree<>(3, list, true);
    KDNode root = tree.getRoot();
    assertEquals(root.getValue(), star0);
    assertNull(root.getLeft());
    assertNull(root.getRight());
  }

}
