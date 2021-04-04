package edu.brown.cs.student.stars.Commands;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * class for the delete user data sql command.
 */
public class DeleteUserData implements Command {

  /**
   * runs the command.
   * @param input a list of strings, that was entered in from the CLI
   * @return string representing if command ran okay
   * @throws FileNotFoundException exception
   * @throws SQLException exception
   * @throws ClassNotFoundException exception
   */
  @Override
  public String run(List<String> input) throws FileNotFoundException, SQLException,
    ClassNotFoundException {
    if (input.size() != 3) {
      System.out.println("Error: incorrect number of parameters for command");
      return "ERROR: incorrect number of parameters for command";
    } else {
      String username = input.get(1) + " " + input.get(2);
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:data/maps/maps.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);

      // Statement stat = conn.createStatement();
      // stat.executeUpdate("PRAGMA foreign_keys=ON;");

      PreparedStatement prep;
      prep = conn.prepareStatement("DELETE FROM checkins WHERE username = ?;");
      prep.setString(1, username);
      prep.executeUpdate();
      prep.close();
      conn.close();
      System.out.println("successfully deleted user info from table: " + username);
      return "successfully deleted user info from table";
    }
  }
}
