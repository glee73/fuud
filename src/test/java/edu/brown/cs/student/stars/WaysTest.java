package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.Commands.MapCommand;
import edu.brown.cs.student.stars.Commands.Ways;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Class for testing the ways command
 */
public class WaysTest {


  @Test
  public void simpleGoodWayTest() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Ways way = new Ways(mapcom);
    List<String> str = new ArrayList<>();
    str.add("ways");
    str.add("42");
    str.add("-72");
    str.add("41.8");
    str.add("-71.3");
    assertEquals(way.run(str),"/w/0 /w/1 /w/2 /w/3 /w/4 /w/5 /w/6 ");
  }

  @Test
  public void mapDataNotLoaded()
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> map = new MapCommand<>();
    Ways way = new Ways(map);
    List<String> str = new ArrayList<>();
    str.add("ways");
    str.add("42");
    str.add("-72");
    str.add("41.8");
    str.add("-71.3");
    assertEquals(way.run(str), "ERROR: must load map data first");
  }

  @Test
  public void incorrectNumParams() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Ways way = new Ways(mapcom);
    List<String> str = new ArrayList<>();
    str.add("ways");
    str.add("42");
    str.add("-72");
    str.add("41.8");
    str.add("-71.3");
    str.add("a");
    assertEquals(way.run(str),"ERROR: incorrect number of parameters");
  }

  @Test
  public void incorrectParamType() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Ways way = new Ways(mapcom);
    List<String> str = new ArrayList<>();
    str.add("ways");
    str.add("42");
    str.add("-72");
    str.add("41.8");
    str.add("a");
    assertEquals(way.run(str),"ERROR: parameter(s) are of wrong type");
  }

  @Test
  public void pointsReversed() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> mapcom = new MapCommand<>();
    List<String> strings = new ArrayList<>();
    strings.add("map");
    strings.add("data/maps/smallMaps.sqlite3");
    mapcom.run(strings);
    Ways way = new Ways(mapcom);
    List<String> str = new ArrayList<>();
    str.add("ways");
    str.add("41.8");
    str.add("-71.3");
    str.add("42");
    str.add("-72");
    assertEquals(way.run(str),"ERROR: NW and SE points reverse");
  }

}
