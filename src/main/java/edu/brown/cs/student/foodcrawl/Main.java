package edu.brown.cs.student.foodcrawl;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.student.foodcrawl.DBCommands.FeedPage;
import edu.brown.cs.student.foodcrawl.DBCommands.Encryptor;
import edu.brown.cs.student.foodcrawl.DBCommands.MongoDBConnection;
import edu.brown.cs.student.foodcrawl.DBCommands.RecommendationAlgo;
import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.Restaurant;
import edu.brown.cs.student.foodcrawl.DataStructures.User;
import edu.brown.cs.student.foodcrawl.UtilityFunctions.TimestampComparator;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONArray;
import org.json.JSONException;
import spark.ExceptionHandler;

import spark.Response;
import spark.Request;
import spark.Spark;
import spark.Route;

import com.google.common.collect.ImmutableMap;

import org.json.JSONObject;
import com.google.gson.Gson;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  private static final Gson GSON = new Gson();
  private static MongoDBConnection connection;
  private static final Encryptor encryptor = new Encryptor();

  /**
   * The initial method called when execution begins.
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
    connection = new MongoDBConnection();
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
   * runs our app BE.
   */
  private void run() {

    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    runSparkServer();
  }

  private static int getHerokuAssignedPort() {
    ProcessBuilder pb = new ProcessBuilder();
    if (pb.environment().get("PORT") != null) {
      return Integer.parseInt(pb.environment().get("PORT"));
    }
    return DEFAULT_PORT;
  }
  /**
   * runs the spark server.
   * @param port given number
   */
  private void runSparkServer() {
    Spark.port(getHerokuAssignedPort());
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
    Spark.post("/searchforuser", new UserHandler());
    Spark.post("/searchforrestaurants", new RestHandler());
    Spark.post("/searchbytags", new RestTagsHandler());
    Spark.post("/user", new ProfileHandler());
    Spark.post("/userposts", new UserPostsHandler());
    Spark.post("/feed", new FeedHandler());
    Spark.post("/addpost", new AddPostHandler());
    Spark.post("/login", new LoginHandler());
    Spark.post("/signup", new SignUpHandler());
    Spark.post("/logout", new LogoutHandler());
    Spark.post("/searchtopost", new SearchHandler());
    Spark.post("/restaurantbyid", new GetRestaurantByIDHandler());
    Spark.post("/addfollower", new AddFollowerHandler());
    Spark.post("/unfollow", new UnfollowHandler());
    Spark.post("/deletepost", new DeletePostHandler());
    Spark.post("/recommended", new GetRecommendedHandler());
    Spark.post("/addpin", new AddPinHandler());
    Spark.post("/unpin", new UnPinHandler());
    Spark.post("/getpinned", new GetPinnedHandler());
    Spark.post("/checkpin", new CheckPinnedHandler());
    Spark.post("/bio", new BioHandler());
    Spark.post("/profilepic", new ProfilePicHandler());
    Spark.post("/checkfollow", new CheckFollowingHandler());
    Spark.post("/getfollowers", new GetFollowersUsers());
    Spark.post("/getfollowing", new GetFollowingUsers());
  }

  /**
   * handles requests for a user by username.
   */
  private static class UserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("query");
      User user = connection.getUserByUsername(username);
      List<User> result = new ArrayList<>();
      result.add(user);
      Map<String, Object> vars = ImmutableMap.of("user", result);
      return GSON.toJson(vars);
    }
  }

  /**
   * handles requests for restaurants by restaurant name
   */
  private static class RestHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String restName = data.getString("query");
      String username = data.getString("username");
      List<Restaurant> rest = connection.searchRestaurantByName(restName);
      List<Boolean> bools = new ArrayList<>();
      for (Restaurant r : rest) {
        boolean b = connection.isPinned(username, r.getId());
        bools.add(b);
      }
      Map<String, Object> vars = ImmutableMap.of(
              "restaurant", rest, "pinned", bools);
      return GSON.toJson(vars);
    }
  }

  /**
   * handles requests for a search by tags, returning all restaurants with at least one.
   * of the requested tags
   */
  private static class RestTagsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      JSONArray t = data.getJSONArray("tags");
      List<String> tags = new ArrayList<>();
      for (int i = 0; i < t.length(); i++) {
        tags.add(t.getString(i));
      }
      String username = data.getString("username");
      List<Restaurant> rests = connection.searchByTags(tags);
      List<Boolean> bools = new ArrayList<>();
      for (Restaurant r : rests) {
        boolean b = connection.isPinned(username, r.getId());
        bools.add(b);
      }
      Map<String, Object> vars = ImmutableMap.of(
              "restaurants", rests, "pinned", bools);
      return GSON.toJson(vars);
    }
  }


  /**
   * handles requests for a user's own profile
   */
  private static class ProfileHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      User user = connection.getUserByUsername(username);
      Map<String, Object> vars = ImmutableMap.of("user", user);
      return GSON.toJson(vars);
    }
  }

  /**
   * handles requests for the posts of a user by username, returned sorted by timestamp
   */
  private static class UserPostsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      List<Post> posts = connection.getPostsFromUser(username);
      posts.sort(new TimestampComparator());
      Map<String, Object> vars = ImmutableMap.of("posts", posts);
      return GSON.toJson(vars);
    }
  }

  /**
   * handles requests for a user's feed/explore page, the posts of the people they follow.
   * ordered by timestamp
   */
  private static class FeedHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      List<Post> news = FeedPage.getFeedPagePosts(username);
      news.sort(new TimestampComparator());

      HashMap<String, String>  usernameToPicture = new HashMap<>();
      for (Post p: news) {
        String u = p.getUser();
        String pic = connection.getUserByUsername(u).getPic();
        if (pic == null) {
          usernameToPicture.put(u, "none");
        } else {
          usernameToPicture.put(u, pic);
        }
      }

      Map<String, Object> vars = ImmutableMap.of("feed", news, "usernameToPic", new JSONObject(usernameToPicture));
      return GSON.toJson(vars);
    }
  }

  /**
   * handles requests for a restaurant by ID.
   */
  private static class GetRestaurantByIDHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String id = data.getString("id");
      Restaurant r = connection.getRestaurantByID(id);
      Map<String, Object> vars = ImmutableMap.of("restaurant", r);
      return GSON.toJson(vars);
    }
  }

  /**
   * handles a request to add a post to the database.
   */
  private static class AddPostHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      try {
//        JSONArray p = data.getJSONArray("pictures");
        List<String> pictures = new ArrayList<>();
//        for (int i = 0; i < p.length(); i++) {
//          pictures.add(p.getString(i));
//        }
        Restaurant r = connection.getRestByName(data.getString(
                "restaurantName"));
        connection.createPost(data.getString("text"), data.getInt("review"),
                pictures, r.getId(), data.getString("username"),
                data.getString("timestamp"), data.getString("pic"));
        Map<String, Object> vars = ImmutableMap.of("success", true);
        return GSON.toJson(vars);
      } catch (Exception e) {
        Map<String, Object> vars = ImmutableMap.of("success", false);
        return GSON.toJson(vars);
      }
    }
  }

  /**
   * handles login requests.
   */
  private static class LoginHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      String password = data.getString("password");
      String encrypted = encryptor.encrypt(password);
      if (!connection.checkUsernameExists(username)) {
        Map<String, Object> vars = ImmutableMap.of("success", false, "message", "no such user exists");
        return GSON.toJson(vars);
      }
      User u = connection.getUserByUsername(username);
      if (u.getPassword().equals(encrypted)) {
        request.session().attribute("USERID", username);
        Map<String, Object> vars = ImmutableMap.of("success", true, "message", "logged in!");
        return GSON.toJson(vars);
      } else {
        Map<String, Object> vars = ImmutableMap.of("success", false, "message", "incorrect password");
        return GSON.toJson(vars);
      }
    }
  }

  /**
   * handles signup requests.
   */
  private static class SignUpHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      String password = data.getString("password");
      String password2 = data.getString("password2");
      String encrypted = encryptor.encrypt(password);
      if (connection.checkUsernameExists(username)) {
        Map<String, Object> vars = ImmutableMap.of("success", false, "message", "username already taken");
        return GSON.toJson(vars);
      } else if (!password.equals(password2)) {
        Map<String, Object> vars = ImmutableMap.of("success", false, "message", "passwords do not match");
        return GSON.toJson(vars);
      } else {
        connection.createUser(username, encrypted);
        request.session().attribute("USERID", username);
        Map<String, Object> vars = ImmutableMap.of("success", true, "message", "user added!");
        return GSON.toJson(vars);
      }
    }
  }

  /**
   * handles logout requests.
   */
  private static class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      request.session().removeAttribute("USERID");
      Map<String, Object> vars = ImmutableMap.of("success", true, "message", "logged out");
      return GSON.toJson(vars);
    }
  }

  /**
   * handles searches for a restaurant by name (returning only ID).
   */
  private static class SearchHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      //Restaurant r = connection.getRestByName(
              //data.getString("restaurantName"));
      List<Restaurant> rs = connection.searchRestaurantByName(data.getString("restaurantName"));
      Map<String, Object> vars;
      if (rs.isEmpty()) {
        vars = ImmutableMap.of("success", false, "restaurant", false);
      } else {
        Restaurant r = rs.get(0);
        if (r == null) {
          vars = ImmutableMap.of("success", false, "restaurant", false);
        } else {
          vars = ImmutableMap.of("success", r.getId(), "restaurant", rs);
          // vars = ImmutableMap.of("success", r.getId());
        }
      }
      return GSON.toJson(vars);
    }
  }

  /**
   * handles requests to delete a post.
   */
  private static class DeletePostHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String id = data.getString("id");
      boolean result = connection.deletePost(id);
      Map<String, Object> vars = ImmutableMap.of("success", result);
      return GSON.toJson(vars);
    }
  }


  /**
   * handles requests to add a new follower/following pair
   */
  private static class AddFollowerHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      boolean result = connection.addFollower(data.getString("follower"),
              data.getString("followed"));
      Map<String, Object> vars = ImmutableMap.of("success", result);
      return GSON.toJson(vars);
    }
  }

  /**
   * handles requests to add a new follower/following pair
   */
  private static class UnfollowHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      boolean result = connection.deleteFollower(data.getString("follower"),
              data.getString("followed"));
      Map<String, Object> vars = ImmutableMap.of("success", result);
      return GSON.toJson(vars);
    }
  }

  /**
   *
   * a class to handle adding pinned restaurants.
   */
  private static class AddPinHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      String restID = data.getString("restID");
      boolean result = connection.addPinned(user, restID);
      Map<String, Object> vars = ImmutableMap.of("success", result);
      return GSON.toJson(vars);
    }
  }

  /**
   * a class to handle getting recommended restaurants.
   */
  private static class GetRecommendedHandler implements Route {
    public String handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      List<Restaurant> recommendedRests = RecommendationAlgo.recommend(username);
      List<Boolean> bools = new ArrayList<>();
      for (Restaurant r : recommendedRests) {
        boolean b = connection.isPinned(username, r.getId());
        bools.add(b);
      }

      Map<String, Object> vars = ImmutableMap.of("recommended", recommendedRests, "bools", bools);
      return GSON.toJson(vars);
    }
  }

  /**
   * a class to handle removing pinned restaurants.
   */
  private static class UnPinHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      String restID = data.getString("restID");
      connection.unPin(user, restID);
      Map<String, Object> vars = ImmutableMap.of("success", true);
      return GSON.toJson(vars);
    }
  }

  /**
   * a class to handle returning pinned restaurants.
   */
  private static class GetPinnedHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      User u = connection.getUserByUsername(user);
      if (u.getPinned() == null) {
        Map<String, Object> vars = ImmutableMap.of("pinned", new ArrayList<>());
        return GSON.toJson(vars);
      }
      List<String> pinned = u.getPinned();
      List<Restaurant> rests = new ArrayList<>();
      for (String restID : pinned) {
        Restaurant r = connection.getRestaurantByID(restID);
        rests.add(r);
      }
      Map<String, Object> vars = ImmutableMap.of("pinned", rests);
      return GSON.toJson(vars);
    }
  }

  /**
   * a class to handle checking if a restaurant is pinned.
   */
  private static class CheckPinnedHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      String restID = data.getString("restID");
      boolean pinned = connection.isPinned(user, restID);
      Map<String, Object> vars = ImmutableMap.of("ispinned", pinned);
      return GSON.toJson(vars);
    }
  }

  /**
   * a class to handle checking if a user is following someone.
   */
  private static class CheckFollowingHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String u1 = data.getString("follower");
      String u2 = data.getString("following");
      boolean isFollowing = connection.checkIfUserIsFollowingSomeone(u1, u2);
      Map<String, Object> vars = ImmutableMap.of("isfollowing", isFollowing);
      return GSON.toJson(vars);
    }
  }

  /**
   * a class to handle updating user bios.
   */
  private static class BioHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      String bio = data.getString("bio");
      connection.updateBio(user, bio);
      Map<String, Object> vars = ImmutableMap.of("success", true);
      return GSON.toJson(vars);
    }
  }

  /**
   * a class to handle updating user profile pictures.
   */
  private static class ProfilePicHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      String pic = data.getString("pic");
      connection.updateProfilePic(user, pic);
      Map<String, Object> vars = ImmutableMap.of("success", true);
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

  private static class GetFollowersUsers implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      User u = connection.getUserByUsername(user);
      List<String> followers = u.getFollowers();
      List<User> followersToSend = new ArrayList<>();
      for (String f : followers) {
        User fu = connection.getUserByUsername(f);
        followersToSend.add(fu);
      }
      Map<String, Object> vars = ImmutableMap.of("followers", followersToSend);
      return GSON.toJson(vars);
    }
  }

  private static class GetFollowingUsers implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String user = data.getString("username");
      User u = connection.getUserByUsername(user);
      List<String> following = u.getFollowing();
      List<User> followingToSend = new ArrayList<>();
      for (String f : following) {
        User fu = connection.getUserByUsername(f);
        followingToSend.add(fu);
      }
      Map<String, Object> vars = ImmutableMap.of("following", followingToSend);
      return GSON.toJson(vars);
    }
  }


}
