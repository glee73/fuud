package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

/**
 * Public class that contains the logic for the Ways command.
 */
public class Ways implements Command {

  private final MapCommand<MapNode, MapWay> mapCommand;
  private static final int SEVEN = 7;
  private static final int EIGHT = 8;

  /**
   * Class Constructor.
   * @param mapCommandIn so Ways has access to loaded Map data and connection to DB
   */
  public Ways(MapCommand<MapNode, MapWay> mapCommandIn) {
    mapCommand = mapCommandIn;
  }

  /**
   * Method that handles the logic when the ways command is run.
   * @param input a list of strings, that was entered in from the CLI
   * @throws FileNotFoundException when incorrect file is inputted
   * @throws SQLException when invalid SQL query is made
   * @throws ClassNotFoundException when class not found
   */
  @Override
  public String run(List<String> input)
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    StringBuilder output = new StringBuilder();
    if (!mapCommand.getLoaded()) {
      System.out.println("ERROR: must load map data first");
      return "ERROR: must load map data first";
    }
    if (input.size() != 5) {
      System.out.println("ERROR: incorrect number of parameters");
      return "ERROR: incorrect number of parameters";
    }
    Connection conn = mapCommand.getConn();
    double lat1;
    double lon1;
    double lat2;
    double lon2;
    try {
      lat1 = Double.parseDouble(input.get(1));
      lon1 = Double.parseDouble(input.get(2));
      lat2 = Double.parseDouble(input.get(3));
      lon2 = Double.parseDouble(input.get(4));
    } catch (Exception e) {
      System.out.println("ERROR: parameter(s) are of wrong type");
      return "ERROR: parameter(s) are of wrong type";
    }
    if (lat1 < lat2 || lon2 < lon1) {
      System.out.println("ERROR: NW and SE points reverse");
      return "ERROR: NW and SE points reverse";
    }
    if (lat2 < lat1) {
      double mid = lat1;
      lat1 = lat2;
      lat2 = mid;
    }
    List<String> waysList = new ArrayList<>();
    PreparedStatement prep = conn.prepareStatement("SELECT node.id, node.longitude, node.latitude "
        + "FROM node WHERE longitude BETWEEN '" + lon1 + "' AND '" + lon2 + "'"
        + " AND latitude BETWEEN '" + lat1 + "' AND '" + lat2 + "';");

    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      waysList.add(rs.getString(1));
    }
    rs.close();
    prep.close();
    Set<String> answer = new HashSet<>();
    for (int i = waysList.size() - 1; i >= 0; i--) {
      prep = conn.prepareStatement("SELECT DISTINCT id "
          + "FROM way WHERE way.type != '' AND way.type != 'unclassified' AND way.start IN('"
          + waysList.get(i) + "') OR way.end IN('" + waysList.get(i) + "');");
      rs = prep.executeQuery();
      while (rs.next()) {
        answer.add(rs.getString(1));
      }
      rs.close();
      prep.close();
    }
    Iterator<String> iterate = answer.iterator();
    List<String> answer2 = new ArrayList<>();
    while (iterate.hasNext()) {
      String current = iterate.next();
      answer2.add(current);
    }
//    Collections.reverse(answer2);
    AlphabeticalComparator comp = new AlphabeticalComparator();
    answer2.sort(comp);
    for (String s : answer2) {
      System.out.println(s);
      output.append(s).append(" ");
    }
    //Will return output in format: /w/1 /w/2 /w/3 ...
    return output.toString();
  }


  /* Helper method for getAllWaysDimensions */
  @SuppressWarnings("checkstyle:MagicNumber")
  private List<MapWay> getAllWays(Double lat1, Double lon1, Double lat2, Double lon2) {
    PreparedStatement prep;
    Connection conn = mapCommand.getConn();
    ResultSet rs;

    List<MapWay> result = new ArrayList<>();

    try {
      prep = conn.prepareStatement("SELECT * FROM way JOIN node ON node.id = way.start "
        + "WHERE node.latitude BETWEEN ? AND ? INTERSECT "
        + "SELECT * FROM way JOIN node ON node.id = way.start WHERE node.longitude "
        + "BETWEEN ? AND ? UNION "
        + "SELECT * FROM way JOIN node ON node.id = way.end WHERE node.latitude "
        + "BETWEEN ? AND ? INTERSECT "
        + "SELECT * FROM way JOIN node ON node.id = way.end WHERE node.longitude "
        + "BETWEEN ? AND ?;");
      prep.setDouble(1, lat2);
      prep.setDouble(2, lat1);
      prep.setDouble(3, lon1);
      prep.setDouble(4, lon2);
      prep.setDouble(5, lat2);
      prep.setDouble(6, lat1);
      prep.setDouble(SEVEN, lon1);
      prep.setDouble(EIGHT, lon2);
      rs = prep.executeQuery();
      if (!rs.isClosed()) {
        while (rs.next()) {
          MapWay way = new MapWay(rs.getString(1), rs.getString(2),
              rs.getString(3), rs.getString(4), rs.getString(5));
          result.add(way);
        }
      }
      prep.close();
      rs.close();
    } catch (SQLException s) {
      System.out.println("ERROR: could not query way and node data");
    }

    return result;
  }

  /**
   * gets all of the ways within the specified bounding box.
   * @param lat1 latitude of NW bounding box point
   * @param lon1 longitude of NW bounding box point
   * @param lat2 latitude of SE bounding box point
   * @param lon2 longitude of SE bounding box point
   * @return List of an array of Doubles, where in each array
   * index 0 = ways' starting latitude
   * index 1 = way's starting longitude
   * index 2 = way's ending latitude
   * index 3 = way's ending longitude
   * index 4 = 1 if way is traversable, 0 if not
   * There is an array of Doubles for each way that falls in the bounding box
   * @throws SQLException if SQL query fails
   */
  public List<Double[]> getAllWaysDimensions(Double lat1, Double lon1, Double lat2, Double lon2)
    throws SQLException {
    PreparedStatement prep;
    Connection conn = mapCommand.getConn();
    ResultSet rs;
    List<MapWay> ways = getAllWays(lat1, lon1, lat2, lon2);
    List<Double[]> dims = new ArrayList<>();
    for (int i = 0; i < ways.size(); i++) {
      MapWay way = ways.get(i);
      String start = way.getStartID();
      prep = conn.prepareStatement("SELECT * FROM node "
          + "WHERE id = '" + start + "';"
      );
      rs = prep.executeQuery();

      Double i1 = Double.parseDouble(rs.getString(2));
      Double i2 = Double.parseDouble(rs.getString(3));

      rs.close();
      prep.close();
      String end = way.getEndID();
      prep = conn.prepareStatement("SELECT * FROM node "
          + "WHERE id = '" + end + "';"
      );
      rs = prep.executeQuery();

      Double i3 = Double.parseDouble(rs.getString(2));
      Double i4 = Double.parseDouble(rs.getString(3));


      rs.close();
      prep.close();
      //i5 = 1 if traverable, 0 if nontraversable
      Double i5 = 0.0;
      if (!way.getType().equals("") && !way.getType().equals("unclassified")) {
        i5 = 1.0;
      }
      Double[] wayDim = {i1, i2, i3, i4, i5};
      dims.add(wayDim);
    }
    return dims;
  }
}

