package edu.brown.cs.student.foodcrawl.DBCommands;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.Restaurant;
import edu.brown.cs.student.foodcrawl.DataStructures.User;
import edu.brown.cs.student.foodcrawl.UtilityFunctions.GenerateHashID;
import org.bson.Document;

import java.util.*;

import com.mongodb.Block;

import static com.mongodb.client.model.Filters.*;

/**
 * a class to manage our database connection.
 */
public class MongoDBConnection {

  MongoCollection<Document> usersCollection;
  MongoCollection<Document> restaurantsCollection;
  MongoCollection<Document> postsCollection;

  /**
   * the constructor, which establishes a connection to our database.
   */
  public MongoDBConnection() {
    MongoClientURI uri = new MongoClientURI(
      "mongodb+srv://tim:Ihatethisclass2!@foodcrawl.75jup.mongodb.net/test?retryWrites=true&w=majority&authSource=admin");

    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase("test");
    usersCollection = database.getCollection("users");
    restaurantsCollection = database.getCollection("restaurants");
    postsCollection = database.getCollection("posts");
  }

  /**
   * creates a user. assumes that the username is unique!!!
   * @param username the username, a string
   * @param password the password, a string
   */
  public void createUser(String username, String password) {
    Document doc = new Document("username", username)
        .append("password", password)
        .append("followers", Arrays.asList())
        .append("following", Arrays.asList())
        .append("pinned", Arrays.asList())
        .append("bio", "");
    usersCollection.insertOne(doc);
  }

  /**
   * given a username, returns boolean indicating if it exists already.
   * @param username the username, a string
   * @return if it exists, a boolean
   */
  public boolean checkUsernameExists(String username) {
    final boolean[] found = {false};
    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        found[0] = true;
      }
    };
    usersCollection.find(eq("username", username))
        .forEach(existsBlock);
    return found[0];
  }

  /**
   * adds a follower to the userFollowed, and adds the userFollowed to the following list.
   * will not add duplicate followers
   * @param follower the username of the follower, a string
   * @param userFollowed ther username of the user follower, a string
   */
  public boolean addFollower(String follower, String userFollowed) {
    if (!checkIfUserIsFollowingSomeone(follower, userFollowed)) {
      usersCollection.updateOne(eq("username", userFollowed),
        Updates.addToSet("followers", follower));
      usersCollection.updateOne(eq("username", follower),
        Updates.addToSet("following", userFollowed));
      return true;
    } else {
      return false;
    }
  }

  /**
   * returns whether or not the follower is following the userFollowed already.
   * @param follower
   * @param userFollowed
   * @return boolean
   */
  public boolean checkIfUserIsFollowingSomeone(String follower, String userFollowed) {
    User u = getUserByUsername(follower);
    List<String> following = u.getFollowing();
    return following.contains(userFollowed);
  }

  /**
   * updates the bio string.
   * @param username
   * @param bio
   */
  public void updateBio(String username, String bio) {
    usersCollection.updateOne(eq("username", username), Updates.set("bio", bio));
  }

  /**
   * a method to determine if a user has a restaurant pinned.
   * @param username the user in question
   * @param restID the restaurant id
   * @return a boolean indicating if the user has the restaurant pinned
   */
  public boolean isPinned(String username, String restID) {
    User u = getUserByUsername(username);
    if (u.getPinned() == null) {
      return false;
    } else {
      return u.getPinned().contains(restID);
    }
  }

  /**
   * a method to add a pinned restaurant for a user.
   * @param username the user, a string
   * @param restID the restaurant id, a string
   */
  public boolean addPinned(String username, String restID) {
    if (!isPinned(username, restID)) {
      usersCollection.updateOne(eq("username", username),
        Updates.addToSet("pinned", restID));
      return true;
    } else {
      return false;
    }
  }

  /**
   * a method to unpin a restaurant from a user's pinned list.
   * @param username the user, a string
   * @param restID the restaurant id, a string
   */
  public void unPin(String username, String restID) {
    User u = getUserByUsername(username);
    List<String> rests = u.getPinned();
    if (rests == null) {
      return;
    }
    rests.remove(restID);

    usersCollection.updateOne(eq("username", username), Updates.set("pinned", rests));
  }

  /**
   * a method to update a user's profile picture.
   * @param username the user
   * @param pic the photo
   */
  public void updateProfilePic(String username, String pic) {
    usersCollection.updateOne(eq("username", username), Updates.set("pic", pic));
  }

  /**
   * returns a user instance based off of the username.
   * returns null if not found
   * @param username string
   * @return user schema or null
   */
  public User getUserByUsername(String username) {
    final User[] found = {null};
    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        String username = document.getString("username");
        String password = document.getString("password");
        List<String> following = (List<String>) document.get("following");
        List<String> followers = (List<String>) document.get("followers");
        List<String> pinned = (List<String>) document.get("pinned");
        String bio = document.getString("bio");
        String pic = document.getString("pic");

        found[0] = new User(username, password, followers, following, pinned, bio, pic);
      }
    };
    usersCollection.find(eq("username", username))
        .forEach(existsBlock);
    return found[0];
  }

  /**
   * gets the restaurant by name.
   * @param name the name, a string
   * @return the restaurant
   */
  public Restaurant getRestByName(String name) {
    final Restaurant[] found = {null};
    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        String id = document.getString("id");
        String name = document.getString("name");
        String address = document.getString("address");
        List<String> tags = (List<String>) document.get("tags");
        found[0] = new Restaurant(name, address, tags, id);
      }
    };
    restaurantsCollection.find(eq("name", name)).forEach(existsBlock);
    return found[0];
  }

  /**
   * gets the restaurant by name.
   * @param name the name, a string
   * @return the restaurant
   */
  public List<Restaurant> getAllRestsWithName(String name) {
    final List<Restaurant> found = new ArrayList<>();
    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        String id = document.getString("id");
        String name = document.getString("name");
        String address = document.getString("address");
        List<String> tags = (List<String>) document.get("tags");
        found.add(new Restaurant(name, address, tags, id));
      }
    };
    restaurantsCollection.find(eq("name", name)).forEach(existsBlock);
    return found;
  }

  /**
   * get a restaurant by id
   * @param id the id, a string
   * @return the corresponding restaurant
   */
  public Restaurant getRestaurantByID(String id) {
    final Restaurant[] found = {null};
    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        String id = document.getString("id");
        String name = document.getString("name");
        String address = document.getString("address");
        List<String> tags = (List<String>) document.get("tags");
        found[0] = new Restaurant(name, address, tags, id);
      }
    };
    restaurantsCollection.find(eq("id", id))
      .forEach(existsBlock);
    return found[0];
  }

  /**
   * returns all restaurants with any matching tags.
   * @param tags a list of string tags
   * @return list of restaurants
   */
  public List<Restaurant> searchByTags(List<String> tags) {
    final List<Restaurant> found = new ArrayList<>();
    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        String id = document.getString("id");
        String name = document.getString("name");
        String address = document.getString("address");
        List<String> tags = (List<String>) document.get("tags");
        found.add(new Restaurant(name, address, tags, id));
      }
    };
    restaurantsCollection.find(in("tags", tags))
      .forEach(existsBlock);
    return found;
  }


  /**
   * creates a restaurant. assumes that the username is unique!!!
   * @param name the name, a string
   * @param address the address, a string
   */
  public void createRestaurant(String name, String address) {
    Document doc = new Document("name", name)
        .append("address", address)
        .append("tags", Arrays.asList())
        .append("id", GenerateHashID.generateUUID());
    restaurantsCollection.insertOne(doc);
  }

  /**
   * a method to add a tag to a restaurant.
   * @param tag the tag to add, a string
   * @param restaurantID the restaurant id, a string
   */
  public void addTag(String tag, String restaurantID) {
    restaurantsCollection.updateOne(eq("id", restaurantID),
        Updates.addToSet("tags", tag));
  }

  /**
   * a method to delete a post.
   * @param id the id of the post, a string
   * @return a boolean indicating if the post was successfully deleted
   */
  public boolean deletePost(String id) {
    try {
      postsCollection.deleteOne(Filters.eq("id", id));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * a method to delete a follower.
   * @param follower the username currently following, a string
   * @param followed the username currently followed, a string
   * @return a boolean indicating success
   */
  public boolean deleteFollower(String follower, String followed) {
    try {
      User u1 = getUserByUsername(follower);
      User u2 = getUserByUsername(followed);
      List<String> u1following = u1.getFollowing();
      List<String> u2followers = u2.getFollowers();
      u1following.remove(followed);
      u2followers.remove(followed);
      usersCollection.updateOne(eq("username", follower), Updates.set("following", u1following));
      usersCollection.updateOne(eq("username", followed), Updates.set("followers", u2followers));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * a method to create a post.
   * @param text the text of the post, a string
   * @param reviewOutOfTen the review, an integer
   * @param pictures any pictures, a list of string links
   * @param restaurantID the id of the corresponding restaurant, a string
   * @param username the username of the posting user, a string
   * @param timestamp the timestamp of the post, a string
   * @param b the base64 encoded photo, a string
   */
  public void createPost(String text, int reviewOutOfTen, List<String> pictures,
                         String restaurantID, String username, String timestamp, String b) {
    Document doc = new Document("text", text)
        .append("review", reviewOutOfTen)
        .append("pictures", pictures)
        .append("restaurantID", restaurantID)
        .append("id", GenerateHashID.generateUUID())
        .append("username", username)
        .append("timestamp", timestamp)
        .append("pic", b);
    postsCollection.insertOne(doc);
  }

  /**
   * a method to get all the posts of a user.
   * @param username the username, a string
   * @return the list of posts
   */
  public List<Post> getPostsFromUser(String username) {
    //final Post[] found = {null};
    List<Post> posts = new ArrayList<>();

    // Strip quotes!!!
    username = username.replace("\"", "");

    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        String text = document.getString("text");
        int review = document.getInteger("review");
        List<String> pictures =  (List<String>) document.get("pictures");
        String restaurantID = document.getString("restaurantID");
        String id = document.getString("id");
        String username = document.getString("username");
        String timestamp = document.getString("timestamp");

        /*
        Binary bin = document.get("blob", org.bson.types.Binary.class);
        if (bin != null) {
          System.out.println(new String(bin.getData()));
        } */
        String bin = document.getString("pic");

        posts.add(new Post(text, review, pictures, username, restaurantID, id, timestamp, bin));
      }
    };
    postsCollection.find(eq("username", username))
        .forEach(existsBlock);
    return posts;
  }

  /**
   * a method to compute global ratings.
   * @return a hashmap of restaurant ids to ratings
   */
  public HashMap<String, Double> computeRatings() {

    Map<String, Integer> ratings = new HashMap<>();
    Map<String, Integer> numRatings = new HashMap<>();

    Block<Document> existsBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        int review = document.getInteger("review");
        String restaurantID = document.getString("restaurantID");

        if (ratings.containsKey(restaurantID)) {
          ratings.put(restaurantID, ratings.get(restaurantID) + review);
        } else {
          ratings.put(restaurantID, review);
        }

        if (numRatings.containsKey(restaurantID)) {
          numRatings.put(restaurantID, numRatings.get(restaurantID) + 1);
        } else {
          numRatings.put(restaurantID, 1);
        }
      }
    };
    postsCollection.find()
      .forEach(existsBlock);

    HashMap<String, Double> avgRatings = new HashMap<>();
    for (String rID : numRatings.keySet()) {
      int numberOfRatings = numRatings.get(rID);
      avgRatings.put(rID, ((double) ratings.get(rID)) / numberOfRatings);
    }

    return avgRatings;
  }

}
