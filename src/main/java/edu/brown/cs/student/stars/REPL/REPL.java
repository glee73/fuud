package edu.brown.cs.student.stars.REPL;
import edu.brown.cs.student.stars.Commands.Command;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for creating the REPL
 * by entering an infinite while loop to accept user input.
 * It uses a hashmap to see if the command entered by the user
 * is valid.
 */
public class REPL {

  /**
   * Empty constructor for REPL.
   */
  public REPL() {

  }

  /**
   * This method is responsible for creating the REPL. It does this by
   * entering an infinite while loop and constantly reading in user commands
   * before performing the input command if it is contained in the hashmap.
   * @param map A hashmap containing all the commands that the REPL can handle as keys
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  public void makeRepl(Map<String, Command> map) throws
      FileNotFoundException, SQLException, ClassNotFoundException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      String read = null;
      try {
        read = input.readLine();
      } catch (IOException e) {
        System.out.println("ERROR: Problem reading input");
      }
      if (read != null) {
        List<String> command = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(read);
        while (m.find()) {
          command.add(m.group(1));
        }
        //Only calls performCommand if the map contains the command
        if (command.size() > 0) {
          if (map.containsKey(command.get(0))) {
            map.get(command.get(0)).run(command);
          } else {
            System.out.println("ERROR: Invalid command");
          }
        }
      } else {
        break;
      }
    }
  }
}
