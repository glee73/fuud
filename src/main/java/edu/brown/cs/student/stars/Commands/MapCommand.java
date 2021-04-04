package edu.brown.cs.student.stars.Commands;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.Graph.Edge;
import edu.brown.cs.student.stars.Graph.Vertex;
import edu.brown.cs.student.stars.KDTree.KDNode;
import edu.brown.cs.student.stars.KDTree.KDTree;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles Loading in Map data from a sqlite3 file.
 * @param <V> Any class object that extends the Vertex interface
 * @param <E> Any class object that extends the Edge interface
 *
 */
public class MapCommand<V extends Vertex<V, E>, E extends Edge<V, E>> implements Command {
  private static Connection conn;
  private List<MapNode> traversableList;
  private KDNode root = null;
  private KDTree<MapNode> tree = null;
  private Boolean loaded = false;
  private LoadingCache<String, List<V>> cache;
  private final int cacheSize = 8000;

  /**
   * Class constructor.
   */
  public MapCommand() {

  }

  /**
   * @param input a list of strings, that was entered in from the CLI.
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  @Override
  public String run(List<String> input)
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    if (input.size() != 2) {
      System.out.println("ERROR: Incorrect number of parameters for map command");
      return "ERROR: Incorrect number of parameters for map command";
    }
    String path = input.get(1);
    if (!Files.exists(Paths.get(path))) {
      System.out.println("ERROR: Invalid file");
      return "ERROR: Invalid file";
    }
    //establishing connection to the database
    this.establishDatabase(path);
    //make update TraversableNodes List
    this.getTraversableNodes();
    CacheLoader<String, List<V>> loader = new CacheLoader<>() {
      /**
       * Defining that our cache should check query for outgoing nodes
       * @param startID start Node ID
       * @return List of Connecting Nodes
       * @throws Exception if SQL query fails
       */
      @Override
      public List<V> load(String startID) throws Exception {
        PreparedStatement prep = conn.prepareStatement(
            "SELECT node.id, node.latitude, node.longitude "
                + "FROM way JOIN node ON way.end = node.id "
                + "WHERE way.start = '" + startID + "' AND way.type!=''"
            + " AND way.type!='unclassified';"
        );
        ResultSet rs = prep.executeQuery();
        List<V> neighbors = new ArrayList<>();
        while (rs.next()) {
          String nodeID = rs.getString(1);
          double nodeLat = rs.getDouble(2);
          double nodeLong = rs.getDouble(3);

          V end = (V) new MapNode(nodeID, nodeLong, nodeLat, conn);

          neighbors.add(end);
        }
        rs.close();
        prep.close();
        return neighbors;
      }
    };
    cache = CacheBuilder.newBuilder().maximumSize(cacheSize).build(loader);
    //Make the KDTree using the traversableNodes
    tree = new KDTree<>(2, traversableList, false);
    root = tree.getRoot();
    loaded = true;
    System.out.println("map set to " + path);
    return "map set to " + path;
  }

  /*
  Private helper method to establish connection to the database
   */
  private void establishDatabase(String path) throws SQLException, ClassNotFoundException {
    //Establishing database connection:
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + path;
    conn = DriverManager.getConnection(urlToDB);
    Statement statement = conn.createStatement();
    statement.executeUpdate("PRAGMA foreign_keys=ON;");

  }

  /*
  Private helper method to set traversable Nodes list
   */
  private void getTraversableNodes() throws SQLException {
    traversableList = new ArrayList<>();
    PreparedStatement prep = conn.prepareStatement("SELECT DISTINCT node.id, longitude, latitude "
        + "FROM node, way WHERE (node.id = way.start OR node.id = way.end) AND way.type!='' "
        + "AND way.type!='unclassified';");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      MapNode node = new MapNode(rs.getString(1), rs.getDouble(2), rs.getDouble(3), conn);
      traversableList.add(node);
    }
    rs.close();
    prep.close();
  }

  /**
   * public accessor method.
   * @return KDTree
   */
  public KDTree<MapNode> getTree() {
    return tree;
  }

  /**
   * public accessor method.
   * @return root
   */
  public KDNode getRoot() {
    return root;
  }

  /**
   * public accessor method.
   * @return Connection
   */
  public Connection getConn() {
    return conn;
  }

  /**
   * Public accessor method.
   * @return boolean, whose value represents if map data was loaded or not
   */
  public boolean getLoaded() {
    return loaded;
  }

  /**
   * Public accessor method.
   * @return loadingCache that is used in Dijkstra's
   * maps vertex ID to its outgoing vertices
   */
  public LoadingCache<String, List<V>> getCache() {
    return cache;
  }

}
