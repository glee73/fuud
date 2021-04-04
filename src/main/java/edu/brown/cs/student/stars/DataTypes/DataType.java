package edu.brown.cs.student.stars.DataTypes;

/**
 * Interface implemented by Star and MockPerson.
 * Used to allow the KDTree work for information besides stars.
 * Used to define how different information types can be stored.
 */
public interface DataType {

  /**
   * Method used to set the dimension of the specific DataType.
   * (For example Star has dimension 3-- as seen in the KDTree).
   */
  void setDimensions();

  /**
   * Method used to acces the value at a certain dimension.
   * @param dim number to be accessed
   * @return value at said dimension
   * (For example, for a star, if dim=1, this method will return
   * the star's x value).
   */
  Double getDimensions(int dim);

  /**
   * Accessor method.
   * @return a mapped unique identify to each DataType object
   */
  String getID();
}
