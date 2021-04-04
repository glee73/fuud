package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.DataTypes.Star;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the methodology for the naive implementation of
 * the radius command. It implements the Command interface,
 * as it is a command and can be added to the REPL.
 */
public class NaiveRadius implements Command {
  private final StarsCommand starsCommand;
  private List<Star> starsList;
  private int numStars;
  private double x;
  private double y;
  private double z;
  private List<Star> answerStarsList;
  private Star mainStar;
  private static final double SQRT = 0.5;

  /**
   * Class constructor.
   * @param starsCommand so that this command has access to the loaded stars data.
   */
  public NaiveRadius(StarsCommand starsCommand) {
    this.starsCommand = starsCommand;
  }

  /**
   Run command will be called when "naive_radius" is entered into command line.
   This method is implemented from the Command interface.
   It will be used for calculating the stars within a certain radius using a naive
   implementation, which only uses a list of Stars.
   */
  @Override
  public String run(List<String> input) {
    if (!starsCommand.getstarsLoaded()) {
      System.out.println("ERROR: No stars data loaded");
      return "ERROR: No stars data loaded";
    }
    if (input.size() == 5 || input.size() == 3) {
      starsList = starsCommand.getStarsList();
      answerStarsList = new ArrayList<>();
      double radius;
      try {
        radius = Double.parseDouble(input.get(1));
      } catch (Exception e) {
        System.out.println("ERROR: radius is not a number");
        return "ERROR: radius is not a number";
      }
      if (radius < 0) {
        System.out.println("ERROR: radius < 0");
        return "ERROR: radius < 0";
      } else {
        numStars = starsCommand.getNumStars();
        if (input.size() == 3) {
          String starName = input.get(2);
          if (starName.startsWith("\"") && starName.endsWith("\"")) {
            starName = starName.substring(1, starName.length() - 1);
          } else {
            System.out.println("ERROR: Star Name should be contained in \"\"");
            return "ERROR: Star Name should be contained in \"\"";
          }
          boolean nonExist = true;
          for (int i = 0; i < numStars; i++) {
            if (starsList.get(i).getProperName().equals(starName)) {
              mainStar = this.starsList.get(i);
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
        } else {
          try {
            x = Double.parseDouble(input.get(2));
            y = Double.parseDouble(input.get(3));
            z = Double.parseDouble(input.get(4));
          } catch (Exception e) {
            System.out.println("ERROR: x, y, z parameter(s) are of wrong type");
            return "ERROR: x, y, z parameter(s) are of wrong type";
          }
        }
      }
      //Will return id1 id2 id3 ...
      return this.returnInRadius(radius, input.size());

    } else {
      System.out.println("ERROR: incorrect number of parameters for command");
      return "ERROR: incorrect number of parameters for command";
    }
  }

  /*
  Private method used for calculating and determining stars within a certain radius
  Does not return anything, as it prints the starID of stars within the radius
   */
  private String returnInRadius(double rad, int inputSize) {
    //first sort
    StringBuilder output = new StringBuilder();
    SortByStar comparator = new SortByStar();
    comparator.setX(x);
    comparator.setY(y);
    comparator.setZ(z);
    this.starsList.sort(comparator);
    if (inputSize == 5) {
      for (int i = 0; i < numStars; i++) {
        if (Math.pow(Math.pow(x - starsList.get(i).getX(), 2)
            + Math.pow(y - starsList.get(i).getY(), 2)
            + Math.pow(z - starsList.get(i).getZ(), 2), SQRT) <= rad) {
          System.out.println(starsList.get(i).getstarID());
          output.append(starsList.get(i).getstarID()).append(" ");
          answerStarsList.add(starsList.get(i));
        }
      }
    } else {
      for (int i = 0; i < numStars; i++) {
        if (Math.pow(Math.pow(x - starsList.get(i).getX(), 2)
            + Math.pow(y - starsList.get(i).getY(), 2)
            + Math.pow(z - starsList.get(i).getZ(), 2), SQRT) <= rad) {
          if (!starsList.get(i).getID().equals(mainStar.getID())) {
            System.out.println(starsList.get(i).getstarID());
            output.append(starsList.get(i).getstarID()).append(" ");
            answerStarsList.add(starsList.get(i));
          }
        }
      }
    }
    return output.toString();
  }

  /**
   * Public accessor method to get the answer of stars within radius.
   * Used in jUnit testing and the gui.
   * @return answerStarsList, a list of stars within radius
   */
  public List<Star> getAnswerStarsList() {
    return answerStarsList;
  }

}
