package edu.brown.cs.student.stars.CSVParser;
import edu.brown.cs.student.stars.DataTypes.Star;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to correctly further parse data
 * and create Stars accordingly. It is used to type check
 * according to the Stars DataType structure.
 * (For example it ensures that the x,y,z parameters are numbers).
 */
public class StarDataInterpreter {
  private ArrayList<String[]> dataItems;
  private List<Star> starList;
  private int numStars = 0;

  /**
   * Class constructor.
   * @param data parsed data rows from the CSVParser.
   */
  public StarDataInterpreter(ArrayList<String[]> data) {
    dataItems = data;
    starList = new ArrayList<>();
  }

  /**
   * Method called from the StarsCommand to create a list of Stars.
   * Goes through CSV parsed data, and interprets it accordingly.
   * Handles type checking of the csv data (is the starID an integer?, etc.)
   * @return boolean: true if successful, false otherwise
   */
  public boolean checkData() {
    for (String[] dataRow : dataItems) {
      //number of fields in MockPerson DataType
      int numFields = 5;
      if (dataRow.length != numFields) {
        System.out.println("ERROR: incorrect csv file structure");
        return false;
      }
      //Would do type checking here if not all data was being stored as strings
      int starId;
      String properName;
      double x;
      double y;
      double z;
      try {
        starId = Integer.parseInt(dataRow[0]);
        properName = dataRow[1];
        x = Double.parseDouble(dataRow[2]);
        y = Double.parseDouble(dataRow[3]);
        z = Double.parseDouble(dataRow[4]);
      } catch (Exception e) {
        System.out.println("ERROR: csv file has wrong data types");
        return false;
      }
      Star star = new Star(starId, properName, x, y, z);
      starList.add(star);
      numStars++;
    }
    return true;
  }

  /**
   * Public accessor method.
   * @return a list of Stars
   */
  public List<Star> getStarList() {
    return starList;
  }

  /**
   * Public accessor method.
   * @return number of stars that were read in from CSV file data
   */
  public int getNumStars() {
    return numStars;
  }
}
