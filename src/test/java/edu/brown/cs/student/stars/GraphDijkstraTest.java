package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.Commands.MapCommand;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import edu.brown.cs.student.stars.Graph.AStarComparator;
import edu.brown.cs.student.stars.Graph.CompareHaversine;
import edu.brown.cs.student.stars.Graph.Dijkstra;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Class for further testing of the Graph Package classes
 */
public class GraphDijkstraTest {

  @Test
  public void aStarEndNotSet(){
    AStarComparator<MapNode, MapWay> aStar = new AStarComparator<>();
    MapCommand<MapNode, MapWay> map = new MapCommand<>();
    Connection conn = map.getConn();
    MapNode v1 = new MapNode("1", 1.0, 1.0, conn);
    MapNode v2 = new MapNode("2", 1.0, 1.0, conn);
    assertEquals(aStar.compare(v1, v2), 0);
  }

  @Test
  public void aStarEqualDist(){
    AStarComparator<MapNode, MapWay> aStar = new AStarComparator<>();
    MapCommand<MapNode, MapWay> map = new MapCommand<>();
    Connection conn = map.getConn();
    MapNode v1 = new MapNode("1", 1.0, 1.0, conn);
    v1.setDistance(v1.calcDistance(v1));
    MapNode v2 = new MapNode("2", 1.0, 1.0, conn);
    v2.setDistance(v2.calcDistance(v2));
    MapNode end = new MapNode("3", 0, 0, conn);
    aStar.setEnd(end);
    assertEquals(aStar.compare(v1, v2), 0);
  }

  @Test
  public void haversineComp() throws FileNotFoundException, SQLException, ClassNotFoundException {
    MapCommand<MapNode, MapWay> map = new MapCommand<>();
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    map.run(input);
    Connection conn = map.getConn();
    Dijkstra<MapNode, MapWay> dij = new Dijkstra<>(map);

    MapNode start = new MapNode("/n/0", -71.4, 41.82, conn);
    MapNode end = new MapNode("/n/5", -71.4003,41.8206, conn);
    CompareHaversine<MapNode, MapWay> comp = new CompareHaversine<>();
    dij.shortestPath(start, end, comp);
    List<String[]> path = dij.getnodePath();
    String[] answer = {"/n/0", "/n/1", "/n/1", "/n/2", "/n/2", "/n/5"};
    int z = 0;
    for (String[] strings : path) {
      for (String string : strings) {
        assertEquals(string, answer[z]);
        z++;
      }
    }
  }

}
