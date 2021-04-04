package edu.brown.cs.student.stars.Graph;

/**
 * This Class outlines the methods that a class that implements the edge interface must have.
 * @param <V> Vertex type
 * @param <E> Edge type
 */
public interface Edge<V extends Vertex<V, E>, E extends Edge<V, E>> {

  /**
   * Setter method.
   * @param v Vertex to be set as edge's start.
   */
  void setStart(V v);

  /**
   * Accessor method.
   * @return Vertex connected at edge's start.
   */
  V getStart();

  /**
   * Setter method.
   * @param v Vertex to be set as edge's end.
   */
  void setEnd(V v);

  /**
   * Accessor method.
   * @return Vertex connected ot edge's end.
   */
  V getEnd();

  /**
   * Accessor method.
   * @return edge's id
   */
  String getId();

}
