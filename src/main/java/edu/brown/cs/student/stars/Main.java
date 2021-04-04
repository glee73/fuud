package edu.brown.cs.student.stars;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.brown.cs.student.stars.Commands.Command;
import edu.brown.cs.student.stars.Commands.DeleteUserData;
import edu.brown.cs.student.stars.Commands.MapCommand;
import edu.brown.cs.student.stars.Commands.Mock;
import edu.brown.cs.student.stars.Commands.NaiveNeighbors;
import edu.brown.cs.student.stars.Commands.NaiveRadius;
import edu.brown.cs.student.stars.Commands.Nearest;
import edu.brown.cs.student.stars.Commands.Neighbors;
import edu.brown.cs.student.stars.Commands.Radius;
import edu.brown.cs.student.stars.Commands.Route;
import edu.brown.cs.student.stars.Commands.StarsCommand;
import edu.brown.cs.student.stars.Commands.Ways;
import edu.brown.cs.student.stars.DataTypes.MapNode;
import edu.brown.cs.student.stars.DataTypes.MapWay;
import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.KDTree.KDNode;
import edu.brown.cs.student.stars.REPL.REPL;
import edu.brown.cs.student.stars.ThreadUserCheckin.CheckinThread;
import edu.brown.cs.student.stars.ThreadUserCheckin.UserCheckin;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;

import spark.QueryParamsMap;
import spark.Response;
import spark.Request;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;

import org.json.JSONObject;
import com.google.gson.Gson;


/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final int TIMER_DELAY = 2000;

  private static final StarsCommand STARSCOMMAND = new StarsCommand();
  private static final NaiveNeighbors NAIVENEIGHBORSCOMMAND = new NaiveNeighbors(STARSCOMMAND);
  private static final NaiveRadius NAIVERADIUS = new NaiveRadius(STARSCOMMAND);
  private static final Neighbors NEIGHBORSCOMMAND = new Neighbors(STARSCOMMAND);
  private static final Radius RADIUSCOMMAND = new Radius(STARSCOMMAND);
  private static final MapCommand<MapNode, MapWay> MAPCOMMAND = new MapCommand<>();
  private static final Nearest NEARESTCOMMAND = new Nearest(MAPCOMMAND);
  private static final Ways WAYS = new Ways(MAPCOMMAND);
  private static final Route ROUTE = new Route(MAPCOMMAND);
  private static final DeleteUserData DELETECOMMAND = new DeleteUserData();
  private static final Gson GSON = new Gson();

  private static CheckinThread t;
  private static Map<String, Set<UserCheckin>> userCheckinMap;
  private static List<String[]> userLog = new ArrayList<>();
  // map of latest checkins: maps user id to their latest checkins

  /**
   * The initial method called when execution begins.
   * @param args An array of command line arguments
   * @throws IOException when error regarding reading input occurs
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
    new Main(args).run();
  }

  private final String[] args;

  /**
   * main constructor.
   * @param args args.
   */
  private Main(String[] args) {
    this.args = args;
  }

  /**
   * runs our maps BE.
   * @throws IOException exception
   * @throws SQLException exception
   * @throws ClassNotFoundException exception
   */
  private void run() throws IOException, SQLException, ClassNotFoundException {

    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    REPL repl = new REPL();
    Mock mockCommand = new Mock();
    Map<String, Command> map = new HashMap<>();
    map.put("stars", STARSCOMMAND);
    map.put("naive_neighbors", NAIVENEIGHBORSCOMMAND);
    map.put("naive_radius", NAIVERADIUS);
    map.put("mock", mockCommand);
    map.put("neighbors", NEIGHBORSCOMMAND);
    map.put("radius", RADIUSCOMMAND);
    map.put("map", MAPCOMMAND);
    map.put("nearest", NEARESTCOMMAND);
    map.put("ways", WAYS);
    map.put("route", ROUTE);
    map.put("delete_user_data", DELETECOMMAND);
    repl.makeRepl(map);

  }

  /**
   * creates free marker egine.
   * @return free marker engine
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * runs the spark server.
   * @param port given number
   * @throws FileNotFoundException exception
   * @throws SQLException exception
   * @throws ClassNotFoundException exception
   */
  private void runSparkServer(int port)
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.post("/route", new RouteHandler());
    Spark.post("/ways", new WaysHandler());
    Spark.post("/nearest", new NearestHandler());
    Spark.get("/checkin", new CheckinHandler());
    Spark.post("/userdata", new UserDataHandler());
    runCheckin();
  }

  /**
   * runs the checkin/update functionality for maps4.
   */
  public void runCheckin() {
    userCheckinMap = new HashMap<>();
    t = new CheckinThread();
    t.start();
    Timer timer = new Timer();
    timer.schedule(new CallGetLatestCheckins(), 0, TIMER_DELAY);
  }

  /**
   * class used in the timer instance.
   */
  private static class CallGetLatestCheckins extends TimerTask {
    /**
     * makes the call to getLatestCheckins().
     */
    public void run() {
      Map<Double, UserCheckin> map = t.getLatestCheckins();
      // update map with most recent checkins we just got
      for (Double ts : map.keySet()) {
        UserCheckin u = map.get(ts);
        String name = u.getName();
        if (userCheckinMap.containsKey(name)) {
          userCheckinMap.get(name).add(u);
        } else {
          userCheckinMap.put(name, new HashSet<>());
          userCheckinMap.get(name).add(u);
        }

      }
      Set<Double> s = map.keySet();
      List<Double> timestampList = new ArrayList<>();
      timestampList.addAll(s);
      Collections.sort(timestampList);
      for (int i = 0; i < timestampList.size(); i++) {
        Double key = timestampList.get(i);
        UserCheckin user = map.get(key);
        String info = user.getName() + " checked in to " + user.getLat()
            + " " + user.getLon() + " at " + user.getTimestamp();
        userLog.add(new String[] {info, user.getName()});
      }

    }
  }

  /**
   * Handle requests to the front page of our Stars website.
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars: Query the database", "answer", " ");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handles Query Requests.
   */
  private static class SubmitHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) throws FileNotFoundException {
      Request req = request;
      QueryParamsMap qm = req.queryMap();
      String input = qm.value("text");
      String naiveType = qm.value("naiveradio");
      String commandType = qm.value("commandradio");

      if (input != null && !input.equals("")) {
        String modifiedInput = "filler " + input;

        List<String> lineParams = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(modifiedInput);
        while (m.find()) {
          lineParams.add(m.group(1));
        }
        List<Star> answerStar;
        List<KDNode> answerNode;
        StringBuilder println = new StringBuilder("Closest stars:");
        if (naiveType.equals("naive")) {
          if (commandType.equals("neighbors")) {
            NAIVENEIGHBORSCOMMAND.run(lineParams);
            answerStar = NAIVENEIGHBORSCOMMAND.getAnswerStarsList();
          } else {
            NAIVERADIUS.run(lineParams);
            answerStar = NAIVERADIUS.getAnswerStarsList();
          }
          for (int i = 0; i < answerStar.size(); i++) {
            println.append(" ").append(i + 1).append(") ");
            println.append("StarID: ").append(answerStar.get(i).getstarID());
            println.append(" Star Name: ")
                .append(answerStar.get(i).getProperName());
            println.append(" X-coordinate: ").append(answerStar.get(i).getX());
            println.append(" Y-coordinate: ").append(answerStar.get(i).getY());
            println.append(" Z-coordinate: ").append(answerStar.get(i).getZ());
          }
        } else {
          if (commandType.equals("neighbors")) {
            NEIGHBORSCOMMAND.run(lineParams);
            answerNode = NEIGHBORSCOMMAND.getAnswerKDNodeList();
          } else {
            RADIUSCOMMAND.run(lineParams);
            answerNode = RADIUSCOMMAND.getAnswerKDNodeList();
          }
          for (int i = 0; i < answerNode.size(); i++) {
            println.append(" ").append(i).append(") ");
            println.append("StarID: ").append(answerNode.get(i).getValue().getID());
            println.append(" X-coordinate: ")
                .append(answerNode.get(i).getValue().getDimensions(1));
            println.append(" Y-coordinate: ")
                .append(answerNode.get(i).getValue().getDimensions(2));
            println.append(" Z-coordinate: ")
                .append(answerNode.get(i).getValue().getDimensions(3));
          }
        }
        Map<String, Object> variables = ImmutableMap.of("title", "Stars: Query the database",
            "answer", println.toString());
        return new ModelAndView(variables, "query.ftl");
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars: Query the database", "answer", " ");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Class that handles getting route for frontend.
   */
  private static class RouteHandler implements spark.Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      String sLat = data.getString("srclat");
      String sLon = data.getString("srclong");
      String dLat = data.getString("destlat");
      String dLon = data.getString("destlong");

      List<String> input = new ArrayList<>();
      input.add("route");
      input.add(String.valueOf(sLat));
      input.add(String.valueOf(sLon));
      input.add(String.valueOf(dLat));
      input.add(String.valueOf(dLon));
      String answer = ROUTE.run(input);
      //List of [longitude, latitude] of node path
      //List will be of size zero if no path exists.
      List<Double[]> routeCoordinate = ROUTE.returnForGui();

      Map<String, Object> variables = ImmutableMap.of("route", answer,
          "routeCoor", routeCoordinate);
      return GSON.toJson(variables);
    }
  }

  /**
   * Class that handles getting all ways of a bounding box, for maps3 frontend.
   */
  private static class WaysHandler implements spark.Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      double sLat = data.getDouble("srclat");
      double sLon = data.getDouble("srclong");
      double dLat = data.getDouble("destlat");
      double dLon = data.getDouble("destlong");

      /*
      For each Double[]:
      index 0 = ways' starting latitude
      index 1 = way's starting longitude
      index 2 = way's ending latitude
      index 3 = way's ending longitude
      index 4 = 1 if way is traversable, 0 if not
       */
      List<Double[]> dims = WAYS.getAllWaysDimensions(sLat, sLon, dLat, dLon);

      Map<String, Object> variables = ImmutableMap.of("ways", dims);
      return GSON.toJson(variables);
    }
  }

  /**
   * Class that handles getting nearest traversable node for maps3 frontend.
   */
  private static class NearestHandler implements spark.Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      double lat = data.getDouble("lat");
      double lon = data.getDouble("lon");
      List<String> input = new ArrayList<>();
      input.add("nearest");
      input.add(String.valueOf(lat));
      input.add(String.valueOf(lon));

      //return order: latitude, longitude
      List<Double> dims = NEARESTCOMMAND.getNearestDim(input);

      Map<String, Object> variables = ImmutableMap.of("nearest", dims);
      return GSON.toJson(variables);
    }
  }

  /**
   * Class that handles getting log of checkin information for front end.
   */
  private static class CheckinHandler implements spark.Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      Map<String, Object> variables = ImmutableMap.of("checkin", userLog);
      return GSON.toJson(variables);
    }
  }

  /**
   * class that handles getting information of a specific user for frontend.
   */
  private static class UserDataHandler implements spark.Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");

      List<String> userdata;
      if (userCheckinMap.containsKey(username)) {
        Set<UserCheckin> ucs = userCheckinMap.get(username);
        userdata = new ArrayList<>();
        for (UserCheckin user : ucs) {
          String info = user.getName() + " checked in to " + user.getLat()
              + " " + user.getLon() + " at " + user.getTimestamp() + ". ";
          userdata.add(info);
        }
      } else {
        userdata = new ArrayList<>();
        userdata.add("No user found with username: " + username);
      }
      Map<String, Object> variables = ImmutableMap.of("userdata", userdata);
      return GSON.toJson(variables);
    }
  }

}
