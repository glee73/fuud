package edu.brown.cs.student.stars.Commands;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface is implemented by the different command classes.
 * It allows command classes to be added to the REPL's hashmap,
 * so that the different commands and their
 * appropriate actions can be easily run, accessed, and added.
 */
public interface Command {

  /**
   * method to be inherited by all command classes.
   * Will establish what happens when a command is "run"
   * @param input a list of strings, that was entered in from the CLI
   * @throws FileNotFoundException if an invalid csv file is entered for loading data
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   * @return String that will contain the appropriate ouput or error message
   */
  String run(List<String> input) throws FileNotFoundException, SQLException, ClassNotFoundException;
}
