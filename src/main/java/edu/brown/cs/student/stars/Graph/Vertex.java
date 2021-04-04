package edu.brown.cs.student.stars.Graph;

import java.sql.SQLException;
import java.util.Set;

/**
 * This Class outlines the methods that a class that implements the vertex interface must have.
 * @param <V> Vertex type
 * @param <E> Edge type
 */
public interface Vertex<V extends Vertex<V, E>, E extends Edge<V, E>> {

  /**
   * Setter method that is called in Dijkstra's.
   * @param d distance value.
   */
  void setDistance(Double d);

  /**
   * Accessor method.
   * @return vertex's stored distance
   */
  double getDistance();

  /**
   * Setter method.
   * @param v Vertex to be set as this's previous.
   */
  void setPrev(V v);



  /**
   * Accessor method.
   * @return Vertex's outgoing edges
   * @throws SQLException if SQL query cannot be completed
   */
  Set<E> getEdges() throws SQLException;


  /**
   * Accessor method.
   * @param i key value for location hashmap
   * @return a dimension of location (long or lat)
   */
  Double getLocation(int i);

  /**
   * Accessor method.
   * @return Vertex's id
   */
  String getidString();

  /**
   * Method that defines how distance should be calculated between two vertices in Dijkstra's.
   * @param v End vertex
   * @return distance value
   */
  Double calcDistance(V v);

  /**
   * method that can be used to add heuristic to Dijkstra algorithm.
   * @param v vertex in which to calc value in regards to
   * @return a weight value to be added to Dijkstra's priority queue
   */
  Double getDijkstraHeuristic(V v);




}
