package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.Commands.MapCommand;
import edu.brown.cs.student.stars.Commands.Route;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import org.junit.Test;
import com.google.common.base.Stopwatch;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *  Class for handling testing of the Route command
 */
public class RouteTest {

  /**
   * Simple good test example for route command using lat & longitudes.
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void simpleGoodRouteNumTest() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Route route = new Route(mapcom);
    List<String> str = new ArrayList<>();
    str.add("route");
    str.add("41.81");
    str.add("-71.4");
    str.add("41.81");
    str.add("-71.4003");
    assertEquals(route.run(str),"/n/0 -> /n/3 : /w/2 ");
  }

  /**
   * Simple good test example for route command using streets/intersections.
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void simpleGoodRouteStreetTest() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Route route = new Route(mapcom);
    List<String> str = new ArrayList<>();
    str.add("route");
    str.add("\"Sootball Ln\"");
    str.add("\"Chihiro Ave\"");
    str.add("\"Sootball Ln\"");
    str.add("\"Yubaba St\"");
    assertEquals(route.run(str),"/n/1 -> /n/4 : /w/3 ");
  }

  /**
   * Test when route command is given incorrect number of parameters
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
    Route route = new Route(mapcom);
    List<String> str = new ArrayList<>();
    str.add("route");
    str.add("Sootball Ln");
    str.add("Chihiro Ave");
    str.add("Sootball Ln");
    str.add("Yubaba St");
    str.add("a");
    assertEquals(route.run(str),"ERROR: incorrect number of parameters");
  }

  /**
   * Test when maps Data has not been laoded yet
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Test
  public void noMapsData() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    Route route = new Route(mapcom);
    List<String> str = new ArrayList<>();
    str.add("route");
    str.add("Sootball Ln");
    str.add("Chihiro Ave");
    str.add("Sootball Ln");
    str.add("Yubaba St");
    assertEquals(route.run(str),"ERROR: map data not loaded");
  }


//  @Test
//  public void cacheSpeedTest() throws FileNotFoundException, SQLException, ClassNotFoundException {
//    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
//    List<String> input = new ArrayList<>();
//    input.add("map");
//    input.add("data/maps/maps.sqlite3");
//    Stopwatch timer3 = Stopwatch.createStarted();
//    mapcom.run(input);
//    System.out.println("Map took: " + timer3.stop());
//    Route route = new Route(mapcom);
//    List<String> str = new ArrayList<>();
//    str.add("route");
//    str.add("\"Waterman Street\"");
//    str.add("\"Prospect Street\"");
//    str.add("\"Thayer Street\"");
//    str.add("\"Angell Street\"");
//    //long startTime = System.nanoTime();
//    Stopwatch timer1 = Stopwatch.createStarted();
//    route.run(str);
//    System.out.println("Method first run took: " + timer1.stop());
//    Stopwatch timer2 = Stopwatch.createStarted();
//    route.run(str);
//    System.out.println("Method second run took: " + timer2.stop());
////    assertEquals(route.run(str),"ERROR: map data not loaded");
//
//  }

}
