package edu.brown.cs.student.stars.KDTree;

import edu.brown.cs.student.stars.DataTypes.DataType;

/**
 * This class creates the KDNodes, which make up the KDTree.
 * It stores a DataType (like a Star) and the left and right children
 * (which are both also KDNodes).
 */
public class KDNode {
  private final DataType value;
  private KDNode left;
  private KDNode right;

  /**
   * Class constructor.
   * @param val the DataType to be stored at the KDNode.
   */
  public KDNode(DataType val) {
    value = val;
    left = null;
    right = null;

  }

  /**
   * Public accessor method.
   * @return a DataType (either Star or MockPerson)
   */
  public DataType getValue() {
    return value;
  }

  /**
   * Public accessor method.
   * @return left KDNode
   * will return null, if there is no left child
   */
  public KDNode getLeft() {
    return left;
  }

  /**
   * Public setter method.
   * @param node a KDNode to be set as the left child.
   */
  public void setLeft(KDNode node) {
    left = node;
  }

  /**
   * Public setter method.
   * @param node a KDNode to be set as the right child.
   */
  public void setRight(KDNode node) {
    right = node;
  }

  /**
   * Public accessor method.
   * @return right KDNode
   * will return null, if there is no right child
   */
  public KDNode getRight() {
    return right;
  }

}
