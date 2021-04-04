package edu.brown.cs.student.stars.Commands;
import edu.brown.cs.student.stars.CSVParser.StarDataInterpreter;
import edu.brown.cs.student.stars.CSVParser.CsvParser;
import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.KDTree.KDNode;
import edu.brown.cs.student.stars.KDTree.KDTree;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles loading and creating stars from a csvfile.
 * This class is run when the "stars" command is entered into the CLI.
 * This class is passed into other commands (NaiveNeighbors, NaiveRadius, Neighbors, Radius)
 * as it holds the stars data.
 */
public class StarsCommand implements Command {
  private List<Star> starsList;
  private int numStars;
  private boolean starsLoaded = false;
  private KDNode root;
  private KDTree<Star> tree;

  /**
   * Class constructor.
   */
  public StarsCommand() {
  }

  /**
   Run command will be called when "stars" is entered into command line.
   This method is implemented from the Command interface
   */
  @Override
  public String run(List<String> input) throws FileNotFoundException {
    if (input.size() != 2) {
      System.out.println("ERROR: incorrect number of parameters for command");
      return "ERROR: incorrect number of parameters for command";
    } else {
      String filename = input.get(1);
      CsvParser parser = new CsvParser();
      if (!parser.run(filename)) {
        System.out.println("ERROR: unable to parse file");
        return "ERROR: unable to parse file";
      }
      ArrayList<String[]> data = parser.getDataItems();
      StarDataInterpreter interpreter = new StarDataInterpreter(data);
      boolean success = interpreter.checkData();
      if (!success) {
        System.out.println("ERROR: data could not be processed");
        return "ERROR: data could not be processed";
      }
      starsList = interpreter.getStarList();
      numStars = interpreter.getNumStars();
      starsLoaded = true;
      this.makeKDTree();
      System.out.println("Read " + numStars + " stars from " + filename);
      return "Read " + numStars + " stars from " + filename;
    }
  }

  /**
   * Public accessor method.
   * @return list of stars that are loaded
   */
  public List<Star> getStarsList() {
    return starsList;
  }

  /**
   * Public accessor method.
   * @return number of stars that are loaded
   */
  public int getNumStars() {
    return numStars;
  }

  /**
   * Public accessor method.
   * @return true if there are stars loaded, false otherwise
   */
  public boolean getstarsLoaded() {
    return starsLoaded;
  }

  private void makeKDTree() {
    tree = new KDTree<>(3, starsList, true);
    root = tree.getRoot();
  }

  /**
   * Public accessor method.
   * @return the root (a KDNode) of a KDTree.
   */
  public KDNode getRoot() {
    return root;
  }

  /**
   * Public accessor method.
   * @return the kdTree
   */
  public KDTree<Star> getTree() {
    return tree;
  }

  /**
   * public method used for jUnit testing, to load in stars without a csv file.
   * @param testingStarsList a list of Stars
   */
  public void loadStarsForTesting(List<Star> testingStarsList) {
    starsLoaded = true;
    starsList = testingStarsList;
    numStars = testingStarsList.size();
    this.makeKDTree();
  }

}
