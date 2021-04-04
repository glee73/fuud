package edu.brown.cs.student.stars.KDTree;

import edu.brown.cs.student.stars.DataTypes.DataType;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class creates a KDTree of any dimension, and that can hold any DataType.
 * It creates KDNodes, and sets their left and right child accordingly.
 * @param <E> a DataType to be stored in the KDNodes.
 */
public class KDTree<E extends DataType> {

  private final int numDimensions;
  private final KDNode root;
  private final KDComparator comparatorRandom;
  private final KDComparatorNoRandom comp;
  private static final double SQRT = 0.5;
  private PriorityQueue<KDNode> pq;
  private final boolean randomization;

  /**
   * Class constructor.
   * @param n integer representing dimension value (K)
   * @param list list of dataTypes to be made into KDNodes adn stored in KDTree
   * @param random boolean whether or not to account for randomizing when two distances are equal
   */
  public KDTree(int n, List<E> list, Boolean random) {
    numDimensions = n;
    comparatorRandom = new KDComparator();
    comp = new KDComparatorNoRandom();
    root = this.create(list, 1);
    randomization = random;
  }

  /**
   * Method that creates the KDTree by setting KDNodes' left and right accordingly.
   * On initial run, set dim equal to 1
   * @param list a list of DataTypes
   * @param dim an int representing the current tree's dimension level
   * @return a KDNode that is the root of the tree
   */
  public KDNode create(List<E> list, int dim) {
    //take in a list, sort by Dim, find center, recurr on left and right
    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return new KDNode(list.get(0));
    }
    if (randomization) {
      comparatorRandom.setCompareDim(dim);
      list.sort(comparatorRandom);
    } else {
      comp.setCompareDim(dim);
      list.sort(comp);
    }

    int midIndex = (int) java.lang.Math.ceil(((list.size() - 1) / 2.0));
    E mid = list.get(midIndex);
    KDNode curr = new KDNode(mid);
    int nextDim = 1;
    if (dim != numDimensions) {
      nextDim = dim + 1;
    }
    curr.setLeft(create(list.subList(0, midIndex), nextDim));
    curr.setRight(create(list.subList(midIndex + 1, list.size()), nextDim));
    return curr;
  }

  /**
   * returns the dimension value of the KDTree, that is set via the constructor.
   * @return an int representing the number of Dimensions of the KDTree
   */
  public int getNumDimensions() {
    return numDimensions;
  }

  /**
   * Public accessor method that returns the root of the tree (the top node).
   * @return the root (a KDNode) of the KDTree,
   * will be null if tree does not yet exist or is of size 0
   */
  public KDNode getRoot() {
    return root;
  }

  /**
   * This method is responsible for finding the nearest neighbors to a certain point.
   * @param k The number of neighbors we want to find
   * @param node The node that we will be using to compare distances.
   *             First call will be root of tree
   * @param pqIn Priority queue that will be updated with neighbors
   * @param dim Number of dimensions
   * @param targetDimensions Maps dimension to value
   * @param targ The node that we are calculating neighbors around if there is a node input
   * @return The priority queue that is now updated with the nearest neighbors
   */
  public PriorityQueue<KDNode> calcNearest(int k, KDNode node, PriorityQueue<KDNode> pqIn, int dim,
                                           HashMap<Integer, Double> targetDimensions, E targ) {
    pq = pqIn;
    //updates the priority queue
    this.calcNearestHelper(k, node, dim, targetDimensions, targ);
    return pq;
  }

  /*
   * recursive method, called in Neighbors and nearest to calculate the nearest
   */
  private void calcNearestHelper(int k, KDNode node,
                                 int dim, HashMap<Integer, Double> targetDimensions, E targ) {
    // if nearNeighs < k, then add node to list
    if (pq.size() < k) {
      //ensuring current node is not that of mainStar
      if (targ != null) {
        if (!node.getValue().getID().equals(targ.getID())) {
          pq.add(node);
        } else {
          if (node.getValue().getID().equals(root.getValue().getID())) {
            calcNearestHelper(k, node.getLeft(), (dim % numDimensions) + 1, targetDimensions, targ);
            calcNearestHelper(k, node.getRight(),
                (dim % numDimensions) + 1, targetDimensions, targ);
            return;
          } else {
            calcNearestHelper(k, node.getLeft(), (dim % numDimensions) + 1, targetDimensions, targ);
            calcNearestHelper(k, node.getRight(),
                (dim % numDimensions) + 1, targetDimensions, targ);
          }
        }
      } else {
        pq.add(node);
      }
    } else {
      //if curr node is closer to target than the FARTHESTCLOSESTNEIGHBOR, then add to list
      assert pq.peek() != null;
      if (this.calcDistance(pq.peek(), targetDimensions)
              > this.calcDistance(node, targetDimensions)) {
        if (targ != null) {
          //ensuring current node is not that of mainStar
          if (!node.getValue().getID().equals(targ.getID())) {
            pq.poll();
            pq.add(node);
          }
        } else {
          pq.poll();
          pq.add(node);
        }
      }
    }

    int nextDim = (dim % numDimensions) + 1;

    //If the euclidean distance between the target point and the farthest neighbor
    //you have is greater than the relevant axis distance* between the current node
    //and target point, recur on both children.
//    assert pq.peek() != null;
    if (calcDistance(pq.peek(), targetDimensions)
            > Math.abs(targetDimensions.get(dim) - node.getValue().getDimensions(dim))) {
      if (node.getLeft() != null) {
        calcNearestHelper(k, node.getLeft(), nextDim, targetDimensions, targ);
      }
      if (node.getRight() != null) {
        calcNearestHelper(k, node.getRight(), nextDim, targetDimensions, targ);
      }
      //If the previous if-statement is false and you do not need to recur down both children, then:
    } else {
      //If the current node's coordinate on the relevant axis is less than target's coordinate on
      // the relevant axis, recur on the right child.
      if (node.getValue().getDimensions(dim) < targetDimensions.get(dim)) {
        if (node.getRight() != null) {
          calcNearestHelper(k, node.getRight(), nextDim, targetDimensions, targ);
        }
        //Else if the current node's coordinate on the relevant axis is greater than
        // the target's coordinate on the relevant axis, recur on the left child.
      } else if (node.getValue().getDimensions(dim) > targetDimensions.get(dim)) {
        if (node.getLeft() != null) {
          calcNearestHelper(k, node.getLeft(), nextDim, targetDimensions, targ);
        }
      }

    }
  }

  /*
 Helper method to calculate the distance from a node given to the target
  */
  private double calcDistance(KDNode node, HashMap<Integer, Double> targetDimensions) {
    double dist = 0;
    for (int i = 1; i < targetDimensions.size() + 1; i++) {
      dist += Math.pow(node.getValue().getDimensions(i) - targetDimensions.get(i), 2);
    }
    return Math.pow(dist, SQRT);
  }

}
