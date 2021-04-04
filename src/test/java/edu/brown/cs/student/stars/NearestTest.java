package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.Commands.MapCommand;
import edu.brown.cs.student.stars.Commands.Nearest;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Testing class for the nearest command.
 */
public class NearestTest {

  /**
   * Simple good test example for nearest command.
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void simpleGoodTest() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Nearest near = new Nearest(mapcom);
    List<String> str = new ArrayList<>();
    str.add("nearest");
    str.add("-100");
    str.add("100");
    assertEquals(near.run(str),"/n/0");
  }

  /**
   * Test for when incorrect number of params are entered into CLI with way nearest command
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void incorrectNumParams() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Nearest near = new Nearest(mapcom);
    List<String> str = new ArrayList<>();
    str.add("nearest");
    str.add("-100");
    str.add("100");
    str.add("a");
    assertEquals(near.run(str),"ERROR: incorrect number of parameters for command");
  }

  /**
   * Test for when incorrect type of params are entered into CLI with way nearest command
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void incorrectTypeParams() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Nearest near = new Nearest(mapcom);
    List<String> str = new ArrayList<>();
    str.add("nearest");
    str.add("-100");
    str.add("a");
    assertEquals(near.run(str),"ERROR: latitude/longitude parameter(s) are of wrong type");
  }

  /**
   * Test for when no maps data has been loaded
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void noMapsLoaded() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    Nearest near = new Nearest(mapcom);
    List<String> str = new ArrayList<>();
    str.add("nearest");
    str.add("-100");
    str.add("100");
    assertEquals(near.run(str),"ERROR: No maps data loaded");
  }

}
