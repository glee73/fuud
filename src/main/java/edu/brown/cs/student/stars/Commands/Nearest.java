package edu.brown.cs.student.stars.Commands;

import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import edu.brown.cs.student.stars.KDTree.KDNode;
import edu.brown.cs.student.stars.KDTree.KDTree;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The nearest class handles the functionality for the nearest command.
 * It implements the Command interface,
 * as it is a command and can be added to the REPL.
 */
public class Nearest implements Command {

  private final MapCommand<MapNode, MapWay> mapCommand;
  private final HashMap<Integer, Double> targetDimensions = new HashMap<>();
  private List<Double> nearestLatLon = new ArrayList<>();

  /**
   * Class Constructor.
   * @param mapCommandIn instance of the MapCommands, so that nearest
   * has reference to mapData that has been loaded, as well as connection to DB
   */
  public Nearest(MapCommand<MapNode, MapWay> mapCommandIn) {
    mapCommand = mapCommandIn;

  }

  /**
   * Run command is called when nearest is entered in REPL.
   * @param input a list of strings, that was entered in from the CLI
   * @return String with answer
   * @throws FileNotFoundException when incorrect file is inputted
   * @throws SQLException when invalid SQL query is made
   * @throws ClassNotFoundException when class not found
   */
  @Override
  public String run(List<String> input)
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    if (input.size() != 3) {
      System.out.println("ERROR: incorrect number of parameters for command");
      return "ERROR: incorrect number of parameters for command";
    }
    double lat;
    double lon;
    try {
      lat = Double.parseDouble(input.get(1));
      lon = Double.parseDouble(input.get(2));
    } catch (Exception e) {
      System.out.println("ERROR: latitude/longitude parameter(s) are of wrong type");
      return "ERROR: latitude/longitude parameter(s) are of wrong type";
    }
    targetDimensions.put(1, lon);
    targetDimensions.put(2, lat);
    if (!mapCommand.getLoaded()) {
      System.out.println("ERROR: No maps data loaded");
      return "ERROR: No maps data loaded";
    }
    KDTree<MapNode> tree = mapCommand.getTree();
    KDNode root = mapCommand.getRoot();
    if (tree == null || root == null) {
      //this would be the case where mapsData was loaded but contained
      //no traversable nodes
      return "";
    }
    NonNaiveComparator comparator = new NonNaiveComparator();
    Double[] dims = {lon, lat};
    comparator.setTargetDimensions(dims);
    PriorityQueue<KDNode> pq = new PriorityQueue<>(1, comparator);
    pq = tree.calcNearest(1, root, pq, 1, targetDimensions, null);
    KDNode answer = pq.poll();
    assert answer != null;
    System.out.println(answer.getValue().getID());
    nearestLatLon = new ArrayList<>();
    nearestLatLon.add(answer.getValue().getDimensions(2));
    nearestLatLon.add(answer.getValue().getDimensions(1));
    return answer.getValue().getID();
  }

  /**
   * Method used in mapsGUI for getting the nearest's latitude and longitude .
   * @param input command line repl
   * @return list of latitude, longitude
   * @throws FileNotFoundException if file not found (from this.run)
   * @throws SQLException when invalid SQL query is made (from this.run)
   * @throws ClassNotFoundException when class not found (from this.run)
   */
  public List<Double> getNearestDim(List<String> input)
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    this.run(input);
    return nearestLatLon;
  }

}
