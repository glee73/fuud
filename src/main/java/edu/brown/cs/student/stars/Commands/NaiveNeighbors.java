package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.DataTypes.Star;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the methodology for the naive implementation
 * of the neighbors command. It implements the Command interface,
 * as it is a command and can be added to the REPL.
 */
public class NaiveNeighbors implements Command {
  private final StarsCommand starsCommand;
  private List<Star> starsList;
  private double x;
  private double y;
  private double z;
  private List<Star> answerStarsList;

  /**
   * Class constructor.
   * @param starsCommand class, so that it has access to star data
   */
  public NaiveNeighbors(StarsCommand starsCommand) {
    this.starsCommand = starsCommand;
  }

  /**
   Run command will be called when "naive_neighbors" is entered into command line.
   This method is implemented from the Command interface.
   It will be used for calculating the nearest neighbors using a naive implementation,
   using only a List of Stars.
   */
  @Override
  public String run(List<String> input) {
    if (!starsCommand.getstarsLoaded()) {
      System.out.println("ERROR: No stars data loaded");
      return "ERROR: No stars data loaded";
    }
    answerStarsList = new ArrayList<>();
    if (input.size() == 3 || input.size() == 5) {
      String checks = this.checks(input);
      if (checks != null) {
        return checks;
      }
      boolean nonExist = true;

      starsList = starsCommand.getStarsList();
      int numStars = starsCommand.getNumStars();
      StringBuilder output = new StringBuilder();
      if (input.size() == 3) {
        String starName = input.get(2);
        //checking for "" around string
        if (starName.startsWith("\"") && starName.endsWith("\"")) {
          starName = starName.substring(1, starName.length() - 1);
        } else {
          System.out.println("ERROR: Star Name should be contained in \"\"");
          return "ERROR: Star Name should be contained in \"\"";
        }
        for (int i = 0; i < numStars; i++) {
          if (starsList.get(i).getProperName().equals(starName)) {
            Star mainStar = this.starsList.get(i);
            x = mainStar.getX();
            y = mainStar.getY();
            z = mainStar.getZ();
            nonExist = false;
          }
        }
      } else {
        try {
          x = Double.parseDouble(input.get(2));
          y = Double.parseDouble(input.get(3));
          z = Double.parseDouble(input.get(4));
        } catch (Exception e) {
          System.out.println("ERROR: x, y, z parameter(s) are of wrong type");
          return "ERROR: x, y, z parameter(s) are of wrong type";
        }
        nonExist = false;
      }

      if (!nonExist) {
        this.sortStars();
        int returnNum;
        try {
          returnNum = java.lang.Integer.parseInt(input.get(1));
        } catch (Exception e) {
          System.out.println("ERROR: # of neighbors parameter is of wrong type");
          return "ERROR: # of neighbors parameter is of wrong type";
        }
        if (input.size() == 3) {
          if (returnNum > numStars) {
            returnNum = numStars;
          }
          if (numStars == 1 && returnNum == 1) {
            return "";
          }
          //starting at i=1 since 0th index is the mainstar itself
          for (int i = 1; i < returnNum + 1; i++) {
            System.out.println(this.starsList.get(i).getstarID());
            output.append(this.starsList.get(i).getstarID()).append(" ");
            answerStarsList.add(this.starsList.get(i));
          }
          //Will return id1 id2 id3 ...
          return output.toString();
        } else {
          if (returnNum > numStars) {
            returnNum = numStars;
          }
          for (int i = 0; i < returnNum; i++) {
            System.out.println(this.starsList.get(i).getstarID());
            output.append(this.starsList.get(i).getstarID()).append(" ");
            answerStarsList.add(this.starsList.get(i));
          }
          //Will return id1 id2 id3 ...
          return output.toString();
        }

      } else {
        System.out.println("ERROR: star you are trying to reference does not exist");
        return "ERROR: star you are trying to reference does not exist";
      }
    } else {
      System.out.println("ERROR: incorrect number of parameters for command " + input.size());
      return "ERROR: incorrect number of parameters for command " + input.size();
    }
  }

  //checks that neighbors are non-negative, etc.
  private String checks(List<String> input) {
    try {
      if (java.lang.Integer.parseInt(input.get(1)) < 0) {
        System.out.println("ERROR: Number of neighbors must be non-negative.");
        return "ERROR: Number of neighbors must be non-negative.";
      }
    } catch (Exception e) {
      System.out.println("ERROR: # of neighbors parameter is of wrong type");
      return "ERROR: # of neighbors parameter is of wrong type";
    }
    return null;
  }

  //sorting stars by distance from main star
  private void sortStars() {
    SortByStar comparator = new SortByStar();
    comparator.setX(x);
    comparator.setY(y);
    comparator.setZ(z);
    this.starsList.sort(comparator);
  }

  /**
   * Public accessor method to get answer of nearest neighbors in terms of Stars.
   * Used in jUnit testing and with the gui
   * @return a list of Stars
   */
  public List<Star> getAnswerStarsList() {
    return answerStarsList;
  }

}
