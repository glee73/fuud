package edu.brown.cs.student.stars.DataTypes;

import edu.brown.cs.student.stars.Graph.Edge;

/**
 * MapWay class is used to store edge information loaded from the MapCommand.
 */
public class MapWay implements  Edge<MapNode, MapWay> {

  private MapNode start;
  private MapNode end;
  private String id;

  /**
   * added these fields from Ethan's implementation.
   */
  private String name;
  private String type;
  private String startID;
  private String endID;

  /**
   * Class Constructor.
   * @param idIn value to be set as MapWay's id
   */
  public MapWay(String idIn) {
    id = idIn;
    start = null;
    end = null;
  }

  /**
   * constructor for ethan's representation of a way.
   * @param id way id
   * @param name way name
   * @param type way type
   * @param startID starting node id
   * @param endID ending node id
   */
  public MapWay(String id, String name, String type, String startID, String endID) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.startID = startID;
    this.endID = endID;
  }


  @Override
  public void setStart(MapNode v) {
    start = v;
  }

  @Override
  public MapNode getStart() {
    return start;
  }

  @Override
  public void setEnd(MapNode v) {
    end = v;
  }

  @Override
  public MapNode getEnd() {
    return end;
  }

  @Override
  public String getId() {
    return id;
  }

  /**
   * public accessor method.
   * @return staritng node ID
   */
  public String getStartID() {
    return startID;
  }

  /**
   * Public accessor method.
   * @return ending node ID
   */
  public String getEndID() {
    return endID;
  }

  /**
   * Public accessor method.
   * @return way type
   */
  public String getType() {
    return type;
  }

}
