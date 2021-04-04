package edu.brown.cs.student.stars.CSVParser;

import edu.brown.cs.student.stars.DataTypes.MockPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to correctly further parse data
 * and create MockPersons accordingly. It is used to type check
 * according to the MockPerson DataType structure.
 */
public class MockPersonDataInterpreter {
  private ArrayList<String[]> dataItems;
  private List<MockPerson> personList;

  /**
   * Class constructor.
   * @param data parsed data rows from the CSVParser.
   */
  public MockPersonDataInterpreter(ArrayList<String[]> data) {
    dataItems = data;
    personList = new ArrayList<>();
  }

  /**
   * Method called from the Mock class to create a list of MockPersons.
   * Goes through CSV parsed data, and interprets it accordingly.
   * @return boolean: true if successful, false otherwise (incorrect csv file structure)
   */
  public boolean checkData() {

    for (String[] dataRow : dataItems) {
      //number of fields in MockPerson DataType
      int numFields = 6;
      if (dataRow.length != numFields) {
        System.out.println("ERROR: incorrect csv file structure");
        return false;
      }
      //Would do type checking here if not all data was being stored as strings
      String firstname = dataRow[0];
      String lastname = dataRow[1];
      String datetime = dataRow[2];
      String email = dataRow[3];
      String gender = dataRow[4];
      String address = dataRow[5];
      MockPerson person = new MockPerson(firstname, lastname, datetime, email, gender, address);
      personList.add(person);
    }
    return true;
  }

  /**
   * Public accessor method.
   * @return list of MockPersons
   */
  public List<MockPerson> getPersonList() {
    return personList;
  }


}

