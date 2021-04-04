package edu.brown.cs.student.stars.DataTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the parameters for a MockPerson, which implements DataType.
 * It is used to store the appropriate information for each MockPerson.
 */
public class MockPerson implements DataType {

  private final String firstName;
  private final String lastName;
  private final String dateTime;
  private final String emailAddress;
  private final String gender;
  private final String address;
  private Map<Integer, String> dimensions;

  /**
   * Class constructor.
   * @param firstname a string of MockPerson's firstname
   * @param lastname a string of MockPerson's lastname
   * @param date a string of MockPerson's date
   * @param email a string of MockPerson's email
   * @param gen a string of MockPerson's gender
   * @param add a string of MockPerson's address
   */
  public MockPerson(String firstname, String lastname, String date, String email,
                    String gen, String add) {
    firstName = firstname;
    lastName = lastname;
    dateTime = date;
    emailAddress = email;
    gender = gen;
    address = add;
    dimensions = new HashMap<>();
    this.setDimensions();

  }

  /**
   * Method implemented from the DataType interface.
   * Set's the MockPerson's dimensions, which is necessary for allowing MockPerson
   * to be treated like a DataType, and thus have the ability to be added to KDNodes, etc.
   */
  @Override
  public void setDimensions() {
    dimensions.put(1, firstName);
    dimensions.put(2, lastName);
    dimensions.put(3, dateTime);
    dimensions.put(4, emailAddress);
    dimensions.put(5, gender);
    dimensions.put(6, address);

  }

  /**
   * Method implemented from the DataType interface.
   * @param dim an integer representing the dimension number that you would want to be accessed
   * @return null, as this method should not be used, since we do not currently want to preform
   * any types of action on MockPerson that would need to invoke this method.
   */
  @Override
  public Double getDimensions(int dim) {
    //return dimensions.get(dim);
    return null;
  }


  /**
   * Method that returns a unique identifier for each instance of the DataType.
   * (For MockPerson, for example, it could be a SSN)
   * @return currently returns 1, as we have not yet determined for MockPerson how we
   * want to define our unique identifier
   */
  @Override
  public String getID() {
    return firstName;
  }

}
