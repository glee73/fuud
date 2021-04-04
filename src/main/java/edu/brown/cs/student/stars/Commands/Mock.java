package edu.brown.cs.student.stars.Commands;
import edu.brown.cs.student.stars.CSVParser.MockPersonDataInterpreter;
import edu.brown.cs.student.stars.CSVParser.CsvParser;
import edu.brown.cs.student.stars.DataTypes.MockPerson;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements command and handles the methodology for when the
 * command "mock" is entered in the CLI.
 */
public class Mock implements Command {

  /**
   * Class constructor.
   */
  public Mock() {
  }

  /**
   Run command will be called when "mock" is entered into command line.
   This method is implemented from the Command interface.
   It will for ingesting data from a mockdata set and creating MockPersons.
   */
  @Override
  public String run(List<String> input) throws FileNotFoundException {
    StringBuilder mockOutput = new StringBuilder();
    if (input.size() != 2) {
      System.out.println("Error: incorrect number of parameters for command");
      return "ERROR: incorrect number of parameters for command";
    } else {
      String filename = input.get(1);
      CsvParser parser = new CsvParser();
      if (!parser.run(filename)) {
        System.out.println("ERROR: unable to parse file");
        return "ERROR: unable to parse file";
      }
      ArrayList<String[]> data = parser.getDataItems();
      MockPersonDataInterpreter interpreter = new MockPersonDataInterpreter(data);
      interpreter.checkData();
      List<MockPerson> personList = interpreter.getPersonList();
      for (MockPerson mockPerson : personList) {
        System.out.println(mockPerson);
        mockOutput.append(mockPerson).append(" ");
      }
    }
    //Will print in format mockpeople: mockperson1 mockperson2 mockperson3...
    return mockOutput.toString();
  }
}
