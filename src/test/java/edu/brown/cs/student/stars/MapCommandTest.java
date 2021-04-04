package edu.brown.cs.student.stars;


import edu.brown.cs.student.stars.Commands.MapCommand;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Testing class for the mapCommand, which loads in map data
 */
public class MapCommandTest {

  /**
   ** A simple test of correctly loading mapData.
   * @throws FileNotFoundException when incorrect file is inputted
   * @throws SQLException when invalid SQL query is made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void simpleRun() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> map = new MapCommand<>();
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    assertEquals(map.run(input), "map set to " + input.get(1));
    assertNotNull(map.getRoot());
    assertNotNull(map.getTree());
  }

  /**
   * Test when map Command is given incorrect database file path.
   * @throws Exception that filename is incorrect
   */
  @Test
  public void fakeDB() throws Exception{
    MapCommand<MapNode, MapWay> map = new MapCommand<>();
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/fake");
    map.run(input);
  }

  /**
   * Test for when map Command is given input w wrong number of parameters.
   * @throws FileNotFoundException when incorrect file is inputted
   * @throws SQLException when invalid SQL query is made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void RunWrongParam() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> map = new MapCommand<>();
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/maps.sqlite3");
    input.add("extra");
    assertEquals(map.run(input), "ERROR: Incorrect number of parameters for map command");

  }

}

