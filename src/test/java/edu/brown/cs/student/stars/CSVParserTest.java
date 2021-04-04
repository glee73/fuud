package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.CSVParser.CsvParser;
import edu.brown.cs.student.stars.CSVParser.MockPersonDataInterpreter;
import edu.brown.cs.student.stars.CSVParser.StarDataInterpreter;
import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.REPL.REPL;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Testing class for CSVParser package (CSVParser, MockPersonDataINterpreter, and StarDataInterpreter classes)
 */
public class CSVParserTest {

  /**
   ** A simple test of the CSVParser using the mockaroo data.
   */
  @Test
  public void testCsvParser() {
    CsvParser parser = new CsvParser();
    String path = "data/stars/mockaroo_data.csv";
    parser.run(path);
    assertEquals(parser.getNumRows(), 1000);
  }

  /**
   * tests that CSVParser throws exception when filename is not valid
   * @throws Exception when filename is not valid
   */
  @Test
  public void csvParserFakeFile() throws Exception{
    CsvParser parser = new CsvParser();
    parser.run("fakefile");
  }


  /**
   * tests that StarDataInterpreter will throw exception if uploaded csv file
   * has a data row where the x,y,z value is not a number
   * (In this case incorrectCsvType.csv has incorrect data: 1,a,x,0,0)
   * @throws Exception when csvfile has incorrect type of params
   */
  @Test
  public void IncorrectCsvTypeStarDataInterpreter() throws Exception {
    CsvParser parser = new CsvParser();
    parser.run("data/stars/incorrectCsvType.csv");
    ArrayList<String[]> data = parser.getDataItems();
    StarDataInterpreter interpreter = new StarDataInterpreter(data);
    interpreter.checkData();
  }

  /**
   * a simple test for the checkData command of the starDataInterpreter
   * when csv file is correct
   */
  @Test
  public void successfulStarDataInterpreter() {
    CsvParser parser = new CsvParser();
    parser.run("data/stars/ten-star.csv");
    ArrayList<String[]> data = parser.getDataItems();
    StarDataInterpreter interpreter = new StarDataInterpreter(data);
    boolean success = interpreter.checkData();
    assertTrue(success);
    List<Star> starsList = interpreter.getStarList();
    assertEquals(starsList.size(), 10);
    assertEquals(interpreter.getNumStars(), 10);
  }

  /**
   * a simple test for the checkData command of the starDataInterpreter
   * when csv file is not valid
   */
  @Test
  public void unsuccessfulStarDataInterpreter() {
    CsvParser parser = new CsvParser();
    parser.run("data/stars/mockaroo_data.csv");
    ArrayList<String[]> data = parser.getDataItems();
    StarDataInterpreter interpreter = new StarDataInterpreter(data);
    boolean success = interpreter.checkData();
    assertFalse(success);
  }

  /**
   * a simple test for the checkData command of the MockPersonDataInterpreter when
   * csv file is correct
   */
  @Test
  public void successfulMockPersonInterpreter() {
    CsvParser parser = new CsvParser();
    parser.run("data/stars/mockaroo_data.csv");
    ArrayList<String[]> data = parser.getDataItems();
    MockPersonDataInterpreter interpreter = new MockPersonDataInterpreter(data);
    boolean success = interpreter.checkData();
    assertTrue(success);
    assertEquals(interpreter.getPersonList().size(), 1000);
  }

  /**
   * a simple test for the checkData command of the MockPersonDataInterpreter when
   * csv file is not valid
   */
  @Test public void unsuccessfulMockPersonInterpeter() {
    CsvParser parser = new CsvParser();
    parser.run("data/stars/ten-star.csv");
    ArrayList<String[]> data = parser.getDataItems();
    MockPersonDataInterpreter interpreter = new MockPersonDataInterpreter(data);
    boolean success = interpreter.checkData();
    assertFalse(success);
  }
}
