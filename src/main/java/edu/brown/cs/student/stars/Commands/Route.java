package edu.brown.cs.student.stars.Commands;


import edu.brown.cs.student.stars.DataTypes.DataType;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import edu.brown.cs.student.stars.Graph.AStarComparator;
import edu.brown.cs.student.stars.Graph.Dijkstra;
import edu.brown.cs.student.stars.KDTree.KDNode;
import edu.brown.cs.student.stars.KDTree.KDTree;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This class handles the functionality for the route command.
 * An instance of the object is instantiated to be run in the REPL.
 * It implements the Command interface,
 * as it is a command and can be added to the REPL.
 */
public class Route implements Command {
  private final MapCommand<MapNode, MapWay> mapCommand;
  private KDTree<MapNode> tree;
  private KDNode root;
  private final Dijkstra<MapNode, MapWay> dijkstra;
  private List<Double[]> guiPath;

  /**
   * Class constructor.
   * @param mapCommandIn so command has access to loaded data and connection to database
   */
  public Route(MapCommand<MapNode, MapWay> mapCommandIn) {
    mapCommand = mapCommandIn;
    dijkstra = new Dijkstra<>(mapCommand);
    guiPath = new ArrayList<>();
  }

  /**
   * Run method, in charge of logic for the route command.
   * @param input a list of strings, that was entered in from the CLI
   * @throws FileNotFoundException when incorrect file is inputted
   * @throws SQLException when invalid SQL query is made
   * @throws ClassNotFoundException when class not found
   */
  @Override
  public String run(List<String> input)
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    StringBuilder output = new StringBuilder();
    guiPath = new ArrayList<>();
    if (input.size() != 5) {
      System.out.println("ERROR: incorrect number of parameters");
      return "ERROR: incorrect number of parameters";
    }
    if (!mapCommand.getLoaded()) {
      System.out.println("ERROR: map data not loaded");
      return "ERROR: map data not loaded";
    }
    setTree(mapCommand.getTree());
    setRoot(mapCommand.getRoot());
    if (tree == null || root == null) {
      System.out.println("ERROR: No traversable nodes in uploaded database");
      return "ERROR: No traversable nodes in uploaded database";
    }
    double travLatStart = 0;
    double travLonStart = 0;
    double travLatEnd = 0;
    double travLonEnd = 0;
    String startID = null;
    String endID = null;
    try {
      //user inputted lat and long, as opposed to street names
      //getting start and end node based on points
      double lat1 = Double.parseDouble(input.get(1));
      double lon1 = Double.parseDouble(input.get(2));
      double lat2 = Double.parseDouble(input.get(3));
      double lon2 = Double.parseDouble(input.get(4));
      //getting IDs of starting and ending nearest nodes
      DataType startDT = calcNearestTraversable(lat1, lon1);
      startID = startDT.getID();
      travLatStart = startDT.getDimensions(2);
      travLonStart = startDT.getDimensions(1);
      DataType endDT = calcNearestTraversable(lat2, lon2);
      endID = endDT.getID();
      travLatEnd = endDT.getDimensions(2);
      travLonEnd = endDT.getDimensions(1);

    } catch (Exception e) {
      //users inputted route command using street names
      for (int i = 1; i < 5; i++) {
        if (!(input.get(i).startsWith("\"") && input.get(i).endsWith("\""))) {
          System.out.println("ERROR: street names not contained in quotes");
          return "ERROR: street names not contained in quotes";
        }
      }
      String street1 = input.get(1).substring(1, input.get(1).length() - 1);
      String cross1 = input.get(2).substring(1, input.get(2).length() - 1);
      String street2 = input.get(3).substring(1, input.get(3).length() - 1);
      String cross2 = input.get(4).substring(1, input.get(4).length() - 1);

      Connection conn = mapCommand.getConn();
      PreparedStatement prep1 = conn.prepareStatement(
              "SELECT node.id, node.latitude, node.longitude "
              + "FROM node, way WHERE way.type != '' AND way.type != 'unclassified' "
                      + "AND (way.name = '" + street1 + "' AND (way.start = node.id "
                      + "OR way.end = node.id)) INTERSECT SELECT "
                      + "node.id, node.latitude, node.longitude "
              + "FROM node, way WHERE way.type != '' AND way.type != 'unclassified' "
                      + "AND (way.name = '" + cross1 + "' "
                      + "AND (way.start = node.id OR way.end = node.id));");
      ResultSet rs1 = prep1.executeQuery();

      while (rs1.next()) {
        startID = rs1.getString(1);
        travLatStart = rs1.getDouble(2);
        travLonStart = rs1.getDouble(3);
      }
      rs1.close();
      prep1.close();

      PreparedStatement prep2 = conn.prepareStatement(
              "SELECT node.id, node.latitude, node.longitude "
              + "FROM node, way WHERE way.type != '' AND way.type != 'unclassified' "
                      + "AND (way.name = '"
          + street2 + "' AND (way.start = node.id OR way.end = node.id)) "
                      + "INTERSECT SELECT node.id, node.latitude, node.longitude "
              + "FROM node, way WHERE way.type != '' "
                      + "AND way.type != 'unclassified' AND (way.name = '"
          + cross2 + "' AND (way.start = node.id OR way.end = node.id));");
      ResultSet rs2 = prep2.executeQuery();
      while (rs2.next()) {
        endID = rs2.getString(1);
        travLatEnd = rs2.getDouble(2);
        travLonEnd = rs2.getDouble(3);
      }
      rs2.close();
      prep2.close();
    }
    MapNode start;
    MapNode end;
    if (startID == null || endID == null) {
      System.out.println("ERROR: Invalid input streets");
      return "ERROR: Invalid input streets";
    }
    start = new MapNode(startID, travLonStart, travLatStart, mapCommand.getConn());
    end = new MapNode(endID, travLonEnd, travLatEnd, mapCommand.getConn());

//    we could do: CompareHaversine comparator = new CompareHaversine();
//    if we didn't want to implement A*
    AStarComparator<MapNode, MapWay> comp2 = new AStarComparator<>();
    comp2.setEnd(end);
    dijkstra.shortestPath(start, end, comp2);
    List<String[]> nodepath = dijkstra.getnodePath();
    Map<String, String> edgePath = dijkstra.getPathEdges();
    guiPath = dijkstra.getNodePathDims();
    if (nodepath.size() == 0) {
      System.out.println(start.getidString() + " -/- " + end.getidString());
      return start.getidString() + " -/- " + end.getidString();
    }
    for (String[] path : nodepath) {
      String e = path[0];
      e += path[1];
      String edge = edgePath.get(e);
      System.out.println(path[0] + " -> " + path[1] + " : " + edge);
      output.append(path[0]).append(" -> ").append(path[1]);
      output.append(" : ").append(edge).append(" ");
    }
    //Will return in format: /n/1 -> /n/2 : /w/1 /n/2 -> /n/3 : /w/2 ...
    return output.toString();
  }

  /**
   * Method to get the nearest traversable node when given a lat and long.
   * Used to determine starting node when command is given lat/long values.
   * @param lat double of latitude
   * @param lon double of longitude
   * @return DataType used in KDTree that stores nearest traversable node information.
   */
  public DataType calcNearestTraversable(double lat, double lon) {
    HashMap<Integer, Double> targetDimensions = new HashMap<>();
    targetDimensions.put(1, lon);
    targetDimensions.put(2, lat);
    NonNaiveComparator comparator = new NonNaiveComparator();
    Double[] dims = {lon, lat};
    comparator.setTargetDimensions(dims);
    PriorityQueue<KDNode> pq = new PriorityQueue<>(1, comparator);
    pq = tree.calcNearest(1, root, pq, 1, targetDimensions, null);
    KDNode answer = pq.poll();
    assert answer != null;
    return answer.getValue();
  }

  /**
   * Public setter method used in jUnit testing.
   * @param node KDNode to set as root
   */
  public void setRoot(KDNode node) {
    root = node;
  }

  /**
   * Public setter method used in jUnit testing to set tree.
   * @param treeSet KDTree to set as global tree
   */
  public void setTree(KDTree<MapNode> treeSet) {
    tree = treeSet;
  }

  /**
   * Method for gui, to be called after run.
   * to obtain the route's path in terms of coordinates
   * @return List of [longitude, latitude] of node path
   * List will be of size zero if no path exists.
   */
  public List<Double[]> returnForGui() {
    return guiPath;
  }

}
