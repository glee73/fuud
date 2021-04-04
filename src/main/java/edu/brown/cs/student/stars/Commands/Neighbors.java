package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.KDTree.KDNode;
import edu.brown.cs.student.stars.KDTree.KDTree;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class handles the methodology for the non-naive implementation of the neighbors command.
 * It implements the Command interface,
 * as it is a command and can be added to the REPL.
 */
public class Neighbors implements Command {
  private final StarsCommand starsCommand;
  private Star mainStar = null;
  private double x; //X value of target
  private double y; //Y value of target
  private double z; //Z value of target
  private List<KDNode> answerKDNodeList;
  private final HashMap<Integer, Double> targetDimensions = new HashMap<>();

  /**
   * Class constructor.
   * @param starsCommand so that command has access to loaded Stars data.
   */
  public Neighbors(StarsCommand starsCommand) {
    this.starsCommand = starsCommand;
  }


  /**
   Run command will be called when "neighbors" is entered into command line.
   This method is implemented from the Command interface.
   It will be used for calculating the nearest neighbors using the KDTree.
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
      int k;
      try {
        k = java.lang.Integer.parseInt(input.get(1));
      } catch (Exception e) {
        System.out.println("ERROR: incorrect type for k param");
        return "ERROR: incorrect type for k param";
      }
      if (k < 0) {
        System.out.println("ERROR: k < 0");
        return "ERROR: k < 0";
      }
      if (k == 0) {
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
        for (int i = 0; i < starsCommand.getNumStars(); i++) {
          if (starsList.get(i).getProperName().equals(starName)) {
            mainStar = starsList.get(i);
            //setting target values
            x = mainStar.getX();
            y = mainStar.getY();
            z = mainStar.getZ();
            targetDimensions.put(1, mainStar.getX());
            targetDimensions.put(2, mainStar.getY());
            targetDimensions.put(3, mainStar.getZ());

          }
        }
        if (k == 1 && mainStar != null) {
          return output.toString();
        }
        if (mainStar == null) {
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
          targetDimensions.put(1, Double.parseDouble(input.get(2)));
          targetDimensions.put(2, Double.parseDouble(input.get(3)));
          targetDimensions.put(3, Double.parseDouble(input.get(4)));
        } catch (Exception e) {
          System.out.println("ERROR: x, y, z parameter(s) are of wrong type");
          return "ERROR: x, y, z parameter(s) are of wrong type";
        }
      }
      NonNaiveComparator comparator = new NonNaiveComparator();
      Double[] dims = {x, y, z};
      comparator.setTargetDimensions(dims);
      PriorityQueue<KDNode> pq = new PriorityQueue<>(k, comparator);
      KDNode root = starsCommand.getRoot();
      KDTree<Star> tree = starsCommand.getTree();
      pq = tree.calcNearest(k, root, pq, 1, targetDimensions, mainStar);
      //calcNeighbors(k, root, 1);
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

  /**
   * Public accessor method.
   * Used in testing and the gui
   * @return list of KDNodes
   */
  public List<KDNode> getAnswerKDNodeList() {
    return answerKDNodeList;
  }

}
