package edu.brown.cs.student.stars.DataTypes;
import edu.brown.cs.student.stars.Graph.Vertex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * MapNode class defines how Vertex information that is specific to mapData will be stored.
 */
public class MapNode implements DataType, Vertex<MapNode, MapWay> {
  private final String idString;
  private final double longitude;
  private final double latitude;
  private Map<Integer, Double> dimensions;
  private Set<MapWay> outgoingEdges;
  private MapNode prev;
  private Double dist = null;
  private Map<Integer, Double> locations;
  private Connection conn;
  private final double earthRadius = 3958.8;

  /**
   * Class Constructor.
   * @param id String to be set as Node ID
   * @param lon double, represents MapNode's longitude
   * @param lat double, represents MapNode's latitude
   * @param connIn connection to database
   */
  public MapNode(String id, double lon, double lat, Connection connIn) {
    idString = id;
    longitude = lon;
    latitude = lat;
    dimensions = new HashMap<Integer, Double>();
    this.setDimensions();
    outgoingEdges = new HashSet<>();
    prev = null;
    locations = new HashMap<>();
    locations.put(1, lon);
    locations.put(2, lat);
    conn = connIn;
  }


  @Override
  public void setDimensions() {
    dimensions.put(1, longitude);
    dimensions.put(2, latitude);

  }

  @Override
  public Double getDimensions(int dim) {
    return dimensions.get(dim);
  }

  @Override
  public String getID() {
    return idString;
  }

  @Override
  public void setDistance(Double d) {
    dist = d;
  }

  /**
   * Getter method of distance values.
   * @return distance that was set in Dijkstra.
   */
  @Override
  public double getDistance() {
    if (dist == null) {
      System.out.println("ERROR: dist is null");
    }
    return dist;
  }

  /**
   * Setter method used in Dijkstra's.
   * @param v value to set prev to.
   */
  @Override
  public void setPrev(MapNode v) {
    prev = v;
  }

  /**
   * Queries for outgoing edges of node if not already retrieved.
   * @return set of MapWay edges
   * @throws SQLException if SQL query is invalid
   */
  @Override
  public Set<MapWay> getEdges() throws SQLException {
    //if no outgoingEdges, query for them
    if (outgoingEdges.size() == 0) {
      PreparedStatement prep = conn.prepareStatement(
          "SELECT way.id, node.id, node.latitude, node.longitude "
          + "FROM node JOIN way ON way.end = node.id "
          + "WHERE way.type != '' AND way.type != 'unclassified' AND way.start = '"
              + this.idString + "';");
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        String wayID = rs.getString(1);
        String nodeEndId = rs.getString(2);
        double nodeEndLat = rs.getDouble(3);
        double nodeEndLon = rs.getDouble(4);
        MapWay way = new MapWay(wayID);
        way.setStart(this);
        MapNode end = new MapNode(nodeEndId, nodeEndLon, nodeEndLat, conn);
        way.setEnd(end);
        outgoingEdges.add(way);
      }
      rs.close();
      prep.close();
    }
    return outgoingEdges;
  }

  /**
   * Get's dimension of MapNode's location.
   * @param i , and int representing location dimension
   * @return if i equals 1, will return longitude. If i equals will return lat
   */
  @Override
  public Double getLocation(int i) {
    return locations.get(i);
  }

  /**
   * @return String ID
   */
  @Override
  public String getidString() {
    return idString;
  }

  /**
   * Set's method for calculating distance from inputted mapNode.
   * For MapNode, this will be a haversine distance method.
   * @param mapNode MapNode to get location for end distance
   * @return double, distance
   */
  @Override
  public Double calcDistance(MapNode mapNode) {
    double consta = 2 * earthRadius;
    return consta * Math.asin(Math.sqrt(calcHaversine(this.locations.get(2),
        mapNode.getLocation(2)) + (Math.cos(
        this.locations.get(2)) * Math.cos(mapNode.getLocation(2))
        * calcHaversine(this.locations.get(1), mapNode.getLocation(1)))));
  }

  /**
   * Method that can be used to add heuristic to Dijkstra's.
   * For MapNode, this will be A* calculation
   * @param mapNode will be the end node
   * @return Double to be added to PQ weight
   */
  @Override
  public Double getDijkstraHeuristic(MapNode mapNode) {
    return this.calcDistance(mapNode);
  }

  //Helper method for calculating haversine distance for mapNodes
  private double calcHaversine(double latlong1, double latlong2) {
    double difference = Math.abs(latlong1 - latlong2);
    double distance = (1 - Math.cos(difference)) / 2;
    return distance;
  }

}

