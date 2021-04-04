package edu.brown.cs.student.stars.Graph;

import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.stars.Commands.MapCommand;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This class is responsible for handling all logic surrounding the Dijkstra
 * algorithm.
 * @param <V> A generic type that extends the vertex interface
 * @param <E> A generic type that extends the edge interface
 */
public class Dijkstra<V extends Vertex<V, E>, E extends Edge<V, E>> {
  private HashSet<String> visited;
  private Map<String, String> prev;
  private Map<String, Double> distances;
  private final MapCommand<V, E> mapCommand;
  private Map<String, String> pathEdges;
  private List<String[]> pathNodes;
  private LoadingCache<String, List<V>> cache;
  private List<Double[]> pathDim;
  private Map<String, Double[]> nodeDims;

  private V startNode;
  private V endNode;

  /**
   * class constructor.
   * @param start starting node
   * @param end ending node
   * @param mapCommandIn instance of the map command
   */
  public Dijkstra(V start, V end, MapCommand<V, E> mapCommandIn) {
    this.startNode = start;
    this.endNode = end;
    this.mapCommand = mapCommandIn;
  }

  /**
   * Class constructor.
   * @param mapCommandIn An instance of the map command that is used to reference loaded data.
   */
  public Dijkstra(MapCommand<V, E> mapCommandIn) {
    visited = new HashSet<>();
    mapCommand = mapCommandIn;
    prev = new HashMap<>();
    distances = new HashMap<>();
    pathEdges = new HashMap<>();
    pathNodes = new ArrayList<>();
    cache = mapCommand.getCache();
    nodeDims = new HashMap<>();
    pathDim = new ArrayList<>();
  }

  /**
   * method that calculates shortest path from starting vertex to ending vertex.
   * Will update global variables pathEdges, pathNodes accordingly
   * @param start starting vertex
   * @param end ending vertex
   * @param comp comparator used for distance calculations
   * @throws SQLException could be thrown if issue loading edges for vertices
   */
  public void shortestPath(V start, V end, Comparator<V> comp) throws SQLException {
    if (start == null || end == null) {
      System.out.println("ERROR: start/end is null");
    }
    //reinitializing global variables
    visited = new HashSet<>();
    prev = new HashMap<>();
    distances = new HashMap<>();
    pathEdges = new HashMap<>();
    pathNodes = new ArrayList<>();
    nodeDims = new HashMap<>();
    pathDim = new ArrayList<>();
    PriorityQueue<V> pq = new PriorityQueue<>(1, comp);

    assert start != null;
    start.setDistance(0.0);
    distances.put(start.getidString(), 0.0);
    prev.put(start.getidString(), null);
    start.setPrev(null);
    pq.add(start);
    nodeDims.put(start.getidString(), new Double[] {start.getLocation(1), start.getLocation(2)});
    assert end != null;
    while (!pq.isEmpty() && !visited.contains(end.getidString())) {
      V u = pq.poll(); //getting vertex with minimum distance
      cache = mapCommand.getCache();
      assert u != null;
      List<V> neighbors = cache.getUnchecked(u.getidString());
      for (V neighbor : neighbors) {
        nodeDims.put(neighbor.getidString(), new Double[] {
            neighbor.getLocation(1), neighbor.getLocation(2)});
        if (!prev.containsKey(neighbor.getidString())) {
          neighbor.setPrev(null);
          neighbor.setDistance((double) Integer.MAX_VALUE);
          prev.put(neighbor.getidString(), null);
          distances.put(neighbor.getidString(), (double) Integer.MAX_VALUE);
          pq.add(neighbor);
        }
      }
      visited.add(u.getidString());
      Set<E> edges = u.getEdges(); //getting vertex's outgoing edges
      for (E e : edges) {
        V v = e.getEnd(); //getting end node, if not yet visited
        nodeDims.put(v.getidString(), new Double[] {v.getLocation(1), v.getLocation(2)});
        if (!visited.contains(v.getidString())) {
          String edgePath = u.getidString();
          edgePath += v.getidString();
          pathEdges.put(edgePath, e.getId());
          if (distances.get(v.getidString())
              > distances.get(u.getidString()) + u.calcDistance(v)) {
            v.setPrev(u);
            distances.replace(v.getidString(),
                distances.get(u.getidString()) + u.calcDistance(v));
            v.setDistance(distances.get(u.getidString()) + u.calcDistance(v));
            prev.replace(v.getidString(), u.getidString());
            pq.remove(v);
            pq.add(v);
          }
        }
      }
//      }
    }
    List<String> answerV = buildAnswerPath(end);
    if (answerV.size() > 1) {
      // have to reverse the list because we want to go from start to end
      for (int i = answerV.size() - 1; i > 0; i--) {
        String[] nodePath = {answerV.get(i), answerV.get(i - 1)};
        pathDim.add(nodeDims.get(answerV.get(i)));
        pathNodes.add(nodePath);
      }
      pathDim.add(nodeDims.get(answerV.get(0)));
    } //else there is no path
  }

  /**
   * builds the path for the shortest route.
   * @param end end vertex
   * @return list of node id strings
   */
  public List<String> buildAnswerPath(V end) {
    List<String> answerV = new ArrayList<>();
    String nodeID = end.getidString();
    String prevId = prev.get(nodeID);
    while (prevId != null) {
      answerV.add(nodeID);
      nodeID = prevId;
      prevId = prev.get(nodeID);
    }
    answerV.add(nodeID);
    return answerV;
  }

  /**
   * method that returns the node path from start to end of dijkstra's.
   * @return list of node ids from start to end
   */
  public List<String[]> getnodePath() {
    return pathNodes;
  }

  /**
   * method that gets the edges used in the path from start to end of dijkstra's.
   * @return Hashmap, mapping the node ids of a segment to the edge id that connects them
   */
  public Map<String, String> getPathEdges() {
    return pathEdges;
  }

  /**
   * Method used for gui.
   * @return the dimensions of the nodes in the path [longitude, latitude]
   * size of List will be zero if no path exists
   */
  public List<Double[]> getNodePathDims() {
    return pathDim;
  }

}
