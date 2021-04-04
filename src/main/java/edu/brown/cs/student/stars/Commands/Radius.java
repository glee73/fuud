package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.KDTree.KDNode;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class handles the methodology for the non-naive implementation of the radius command.
 * The radius command returns stars within a certain radius from a target location
 * (x, y, z coordinate) or from a certain star (given the star's name).
 * It implements the Command interface,
 * as it is a command and can be added to the REPL.
 */
public class Radius implements Command {

  private final StarsCommand starsCommand;
  private PriorityQueue<KDNode> pq;
  private double x; //target coordinate
  private double y; //target coordinate
  private double z; //target coordinate
  private Star mainStar = null;
  private List<KDNode> answerKDNodeList;
  private static final double SQRT = 0.5;

  /**
   * Class constructor.
   * @param starsCommand so that the class has access to the loaded stars data.
   */
  public Radius(StarsCommand starsCommand) {
    this.starsCommand = starsCommand;
  }

  /**
   Run command will be called when "radius" is entered into command line.
   This method is implemented from the Command interface.
   It will be used for calculating the stars within a certain radius using a
   non-naive implementation, which uses the KDTree.
   */
  @Override
  public String run(List<String> input) throws FileNotFoundException {
    StringBuilder output = new StringBuilder();
    if (!starsCommand.getstarsLoaded()) {
      System.out.println("ERROR: No stars data loaded");
      return "ERROR: No stars data loaded";
    }
    if (input.size() != 3 && input.size() != 5) {
      System.out.println("ERROR: incorrect number of parameters for command");
      return "ERROR: incorrect number of parameters for command";
    } else {
      answerKDNodeList = new ArrayList<>();
      List<Star> starsList = starsCommand.getStarsList();
      double r;
      try {
        r = Double.parseDouble(input.get(1));
      } catch (Exception e) {
        System.out.println("ERROR: incorrect type for r param");
        return "ERROR: incorrect type for r param";
      }
      if (r < 0) {
        System.out.println("ERROR: r < 0");
        return "ERROR: r < 0";
      }
      if (r == 0) {
        return output.toString();
      }
      if (input.size() == 3) {
        String starName = input.get(2);
        if (starName.startsWith("\"") && starName.endsWith("\"")) {
          starName = starName.substring(1, starName.length() - 1);
        } else {
          System.out.println("ERROR: Star Name should be contained in \"\"");
          return "ERROR: Star Name should be contained in \"\"";
        }
        boolean nonExist = true;
        for (int i = 0; i < starsCommand.getNumStars(); i++) {
          if (starsList.get(i).getProperName().equals(starName)) {
            mainStar = starsList.get(i);
            //setting target values
            x = mainStar.getX();
            y = mainStar.getY();
            z = mainStar.getZ();
            nonExist = false;
          }
        }
        if (nonExist) {
          System.out.println("ERROR: star you are trying to reference does not exist");
          return "ERROR: star you are trying to reference does not exist";
        }
      }
      if (input.size() == 5) {
        try {
          //setting target values
          x = Double.parseDouble(input.get(2));
          y = Double.parseDouble(input.get(3));
          z = Double.parseDouble(input.get(4));
        } catch (Exception e) {
          System.out.println("ERROR: x, y, z parameter(s) are of wrong type");
          return "ERROR: x, y, z parameter(s) are of wrong type";
        }
      }

      NonNaiveComparator comparator = new NonNaiveComparator();
      Double[] dims = {x, y, z};
      comparator.setTargetDimensions(dims);
      pq = new PriorityQueue<>(comparator);
      KDNode root = starsCommand.getRoot();
      calcRadius(r, root, 1);
      List<KDNode> nodeList = new ArrayList<>();
      int size = pq.size();
      for (int i = 0; i < size; i++) {
        KDNode node = pq.poll();
        nodeList.add(node);
      }
      for (int j = nodeList.size() - 1; j > -1; j--) {
        System.out.println(nodeList.get(j).getValue().getID());
        output.append(nodeList.get(j).getValue().getID()).append(" ");
        answerKDNodeList.add(nodeList.get(j));
      }
    }
    //Will return id1 id2 id3 ...
    return output.toString();
  }



  /*
  Helper method used for the actual calculation of the stars that are within a certain radius
   */
  private void calcRadius(double radius, KDNode node, int dim) {
    double targetDimVal;
    if (dim == 1) {
      targetDimVal = x;
    } else if (dim == 2) {
      targetDimVal = y;
    } else {
      targetDimVal = z;
    }
    int nextDim;
    if (dim == 3) {
      nextDim = 1;
    } else {
      nextDim = dim + 1;
    }
    // if the current node axis distance from the target is less than the radius
    if (Math.abs(node.getValue().getDimensions(dim) - targetDimVal) < radius) {
      if (calcDistance(node) < radius) {
        if (mainStar != null) {
          if (!mainStar.getID().equals(node.getValue().getID())) {
            pq.add(node);
          }
        } else {
          pq.add(node);
        }
      }
      if (node.getLeft() != null) {
        calcRadius(radius, node.getLeft(), nextDim);
      }
      if (node.getRight() != null) {
        calcRadius(radius, node.getRight(), nextDim);
      }
    } else if (node.getValue().getDimensions(dim) > targetDimVal) {
      if (node.getLeft() != null) {
        calcRadius(radius, node.getLeft(), nextDim);
      }
    } else if (node.getValue().getDimensions(dim) < targetDimVal) {
      if (node.getRight() != null) {
        calcRadius(radius, node.getRight(), nextDim);
      }
    }
  }


  /**
   * Public accessor method to get answer of those within radius in terms of KDNodes
   * Used for jUnit testing and the gui.
   * @return list of KDNodes
   */
  public List<KDNode> getAnswerKDNodeList() {
    return answerKDNodeList;
  }

  /*
  Helper method to calculate the distance from a node given to the target
   */
  private double calcDistance(KDNode node) {
    double xDist = Math.pow(node.getValue().getDimensions(1) - x, 2);
    double yDist = Math.pow(node.getValue().getDimensions(2) - y, 2);
    double zDist = Math.pow(node.getValue().getDimensions(3) - z, 2);
    return Math.pow(xDist + yDist + zDist, SQRT);
  }


}
