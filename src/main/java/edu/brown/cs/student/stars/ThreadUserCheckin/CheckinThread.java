package edu.brown.cs.student.stars.ThreadUserCheckin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * thread that continuously communicates with checkin server.
 */
public final class CheckinThread extends Thread {
  private long last = 0;
  private Map checkins;
  private boolean pause = false;
  static final long MSCONVERSION = 1000;

  // SQL globalvariable
  private Connection connect;

  /**
   * Method for synchronizing Map.
   */
  public CheckinThread() {
    checkins = Collections.synchronizedMap(new HashMap<>());
  }

  /**
   * runs the thread by querying the url for information on user checkins.
   */
  public synchronized void run() {
    List<List<String>> updates = null;

    long lastSec = 0;

    while (true) {
      long sec = System.currentTimeMillis() / MSCONVERSION;
      if (sec != lastSec && !pause) {
        try {
          updates = this.update();
        } catch (IOException e) {
          e.printStackTrace();
        }

        if (updates != null && !updates.isEmpty()) {
          for (List<String> el : updates) {
            double timestamp = Double.parseDouble(el.get(0));
            int id = Integer.parseInt(el.get(1));
            String name = el.get(2);
            double lat = Double.parseDouble(el.get(3));
            double lon = Double.parseDouble(el.get(4));

            // put in concurrent hashmap
            UserCheckin uc = new UserCheckin(id, name, timestamp, lat, lon);
            checkins.put(timestamp, uc);

            // create the table if it's not there yet
            try {
              createTable("data/maps/maps.sqlite3");
            } catch (ClassNotFoundException e) {
              e.printStackTrace();
            } catch (SQLException throwables) {
              throwables.printStackTrace();
            }
            // write to the table
            try {
              writeToTable(uc, "data/maps/maps.sqlite3");
            } catch (SQLException | ClassNotFoundException throwables) {
              throwables.printStackTrace();
            }

          }
        }
        lastSec = sec;
      }
    }
  }

  /**
   * creates a table.
   * @param filename string file path
   * @throws ClassNotFoundException exception
   * @throws SQLException exception
   */
  public void createTable(String filename) throws ClassNotFoundException, SQLException {
    // create a new table
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    connect = DriverManager.getConnection(urlToDB);

    // Statement stat = conn.createStatement();
    // stat.executeUpdate("PRAGMA foreign_keys=ON;");

    PreparedStatement prep;
    // prep = conn.prepareStatement("DROP TABLE checkins;");
    // prep.executeUpdate();

    prep = connect.prepareStatement("CREATE TABLE IF NOT EXISTS checkins ("
      + "userid INTEGER,"
      + "timecheckin DOUBLE,"
      + "username TEXT,"
      + "latitude DOUBLE,"
      + "longitude DOUBLE);");
      // + "PRIMARY KEY (userid, timecheckin));");
    prep.executeUpdate();
    prep.close();
    connect.close();
  }

  /**
   * writes to the database.
   * @param uc usercheckin
   * @param filename file name path
   * @throws SQLException exception
   * @throws ClassNotFoundException exception
   */
  public void writeToTable(UserCheckin uc, String filename)
      throws SQLException, ClassNotFoundException {
    PreparedStatement prep;
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    connect = DriverManager.getConnection(urlToDB);

    prep = connect.prepareStatement("INSERT INTO checkins VALUES (?, ?, ? , ?, ?);");
    prep.setInt(1, uc.getId());
    prep.setDouble(2, uc.getTimestamp());
    prep.setString(3, uc.getName());
    prep.setDouble(4, uc.getLat());
    prep.setDouble(5, uc.getLon());
    prep.executeUpdate();
    // System.out.println("inserted into sql");
    // prep = conn.prepareStatement("SELECT COUNT(username) FROM checkins;");
    // ResultSet rs = prep.executeQuery();
    // System.out.println(rs.getInt(1));
    // rs.close();
    prep.close();
    connect.close();
  }

  private synchronized List<List<String>> update() throws IOException {
    URL serverURL = new URL("http://localhost:8080?last=" + last);
    last = Instant.now().getEpochSecond();

    HttpURLConnection conn = (HttpURLConnection) serverURL.openConnection();
    conn.setRequestMethod("GET");

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    Pattern pattern = Pattern.compile("\\[(.*?)\\, (.*?)\\, \"(.*?)\", (.*?)\\, (.*?)\\]");
    String line;

    List<List<String>> output = new ArrayList<>();
    while ((line = br.readLine()) != null) {
      Matcher matcher = pattern.matcher(line);
      while (matcher.find()) {
        List<String> data = new ArrayList<>();
        String parsedTimestamp = matcher.group(1);
        if (parsedTimestamp.charAt(0) == '[') {
          data.add(parsedTimestamp.substring(1));
        } else {
          data.add(parsedTimestamp);
        }
        data.add(matcher.group(2));
        data.add(matcher.group(3));
        data.add(matcher.group(4));
        data.add(matcher.group(5));
        output.add(data);
      }
    }
    return output;
  }


  /**
   * gets the latest checkin updates. Refreshes hashmap so only new
   * checkin updates are returned next time.
   * @return map from a string to a double of timestamps to checkin objects
   */
  public Map<Double, UserCheckin> getLatestCheckins() {
    pause = true;
    Map<Double, UserCheckin> temp = checkins;
    checkins = Collections.synchronizedMap(new HashMap<>());
    pause = false;
    return temp;
  }
}
