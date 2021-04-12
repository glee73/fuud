package edu.brown.cs.student.foodcrawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.student.foodcrawl.DBCommands.ComplexFunctionality;
import edu.brown.cs.student.foodcrawl.DBCommands.DBConnectionManager;
import edu.brown.cs.student.foodcrawl.DBCommands.MongoDBConnection;
import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.Restaurant;
import edu.brown.cs.student.foodcrawl.DataStructures.User;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONArray;
import spark.ExceptionHandler;
import spark.ModelAndView;

import spark.QueryParamsMap;
import spark.Response;
import spark.Request;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
import spark.Route;

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

  private static final Gson GSON = new Gson();
  private static MongoDBConnection connection;

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
    connection = new MongoDBConnection();
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
   */
  private void run() {

    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

//    if (options.has("gui")) {
    runSparkServer((int) options.valueOf("port"));
//    }
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
  private void runSparkServer(int port) {
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

    // Setup Spark Routes
    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.post("/user", new UserHandler());
    Spark.post("/userposts", new UserPostsHandler());
    Spark.post("/restaurant", new RestHandler());
    Spark.post("/tags", new RestTagsHandler());
    Spark.post("/feed", new FeedHandler());
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

  private static class UserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      User user = connection.getUserByUsername(username);
      User userb = new User(username, "yuh");
      Map<String, Object> vars = ImmutableMap.of("user", user);
      return GSON.toJson(vars);
    }
  }

  private static class UserPostsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      List<Post> posts = connection.getPostsFromUser(username);
      Map<String, Object> vars = ImmutableMap.of("posts", posts);
      return GSON.toJson(vars);
    }
  }

  private static class RestHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String name = data.getString("name");
      Restaurant rest = connection.getRestByName(name);
      Map<String, Object> vars = ImmutableMap.of("restaurant", rest);
      return GSON.toJson(vars);

    }
  }

  private static class RestTagsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      JSONArray t = data.getJSONArray("tags");
      List<String> tags = new ArrayList<>();
      for (int i=0; i < t.length(); i++) {
        tags.add(t.getString(i));
      }
      List<Restaurant> rests = connection.searchByTags(tags);
      Map<String, Object> vars = ImmutableMap.of("restaurants", rests);
      return GSON.toJson(vars);
    }
  }

  private static class FeedHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      List<Post> news = ComplexFunctionality.getFeedPagePosts(username);
      Map<String, Object> vars = ImmutableMap.of("feed", news);
      return GSON.toJson(vars);
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

}
