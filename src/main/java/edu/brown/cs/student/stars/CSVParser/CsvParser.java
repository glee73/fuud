package edu.brown.cs.student.stars.CSVParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * This class is used to parse csv files.
 * It extracts the header information and each row of data.
 * Each row of data is then further parsed using an interpreter
 * (MockPersonDataInterpreter or StarDataInterpreter) to check types and
 * formatting according to its appropriate data structure.
 */
public class CsvParser {

  private int numRows = 0;
  private String header;
  private ArrayList<String[]> dataItems;

  /**
   * Class constructor.
   */
  public CsvParser() {
    dataItems = new ArrayList<>();
  }

  /**
   * Method that given a csv file path, parses the file and creates an
   * ArrayList of the parsed rows of information.
   * @param filename String of the filename/path to be parsed
   * @return boolean value true if run was completed successfully, false otherwise
   */
  public boolean run(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      header = br.readLine(); //data field names
      numRows = 0; //resetting size rows
      String data;
      while ((data = br.readLine()) != null) {
        String[] dataCol = data.split(",");
        dataItems.add(dataCol);
        numRows++;
      }
    } catch (Exception e) {
      System.out.println("ERROR: Invalid file");
      return false;
    }
    return true;
  }

  /**
   * Public accessor method.
   * @return ArrayList of parsed rows of information
   */
  public ArrayList<String[]> getDataItems() {
    return dataItems;
  }

  /**
   * Public accessor method.
   * @return number of data rows that was read in from csv file in the run method
   */
  public int getNumRows() {
    return numRows;
  }

  /**
   * Public accessor method.
   * @return header row of the csv file
   */
  public String getHeader() {
    return header;
  }
}
