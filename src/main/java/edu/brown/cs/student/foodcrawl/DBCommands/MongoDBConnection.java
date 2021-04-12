package edu.brown.cs.student.foodcrawl.DBCommands;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.model.Updates;
import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.Restaurant;
import edu.brown.cs.student.foodcrawl.DataStructures.User;
import edu.brown.cs.student.foodcrawl.UtilityFunctions.GenerateHashID;
import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import spark.Response;

import java.util.ArrayList;
import java.util.List;


public class MongoDBConnection {

  MongoCollection<Document> usersCollection;
  MongoCollection<Document> restaurantsCollection;
  MongoCollection<Document> postsCollection;

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
   * @param username
   * @param password
   */
  public void createUser(String username, String password) {
    Document doc = new Document("username", username)
        .append("password", password)
        .append("followers", Arrays.asList())
        .append("following", Arrays.asList())
        .append("bio", "");
    usersCollection.insertOne(doc);
  }

  /**
   * given a username, returns boolean indicating if it exists already.
   * @param username
   * @return
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
   * @param follower
   * @param userFollowed
   */
  public void addFollower(String follower, String userFollowed) {
    usersCollection.updateOne(eq("username", userFollowed),
        Updates.addToSet("followers", follower));
    usersCollection.updateOne(eq("username", follower),
        Updates.addToSet("following", userFollowed));
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
        String bio = document.getString("bio");

        found[0] = new User(username, password, followers, following, bio);
      }
    };
    usersCollection.find(eq("username", username))
        .forEach(existsBlock);
    return found[0];
  }

  /**
   * gets the restaurant by name.
   * @param name
   * @return
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
   * returns all restaurants with any matching tags.
   * @param tags
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

  public void checkUser() {
    //createUser("bob", "pw");
    //createUser("ethan", "yuh");
    //addFollower("bob", "ethan");
    System.out.println(checkUsernameExists("ethan"));
    System.out.println(checkUsernameExists("ethhhjian"));
    System.out.println("hi");

    System.out.println(getUserByUsername("ethan").getBio());
    System.out.println(getUserByUsername("jasdiof"));
  }

  /**
   * creates a restaurant. assumes that the username is unique!!!
   * @param name string
   * @param address string
   */
  public void createRestaurant(String name, String address) {
    Document doc = new Document("name", name)
        .append("address", address)
        .append("tags", Arrays.asList())
        .append("id", GenerateHashID.generateUUID());
    restaurantsCollection.insertOne(doc);
  }

  public void addTag(String tag, String restaurantID) {
    restaurantsCollection.updateOne(eq("id", restaurantID),
        Updates.addToSet("tags", tag));
  }

  public void checkRestaurant() {
    //createRestaurant("McDonalds", "thayer st");
    addTag("burgers", "b3b2525248bf4578b0b5847f58dd3fab");
  }

  public void createPost(String text, int reviewOutOfTen, List<String> pictures,
                         String restaurantID, String username, String timestamp) {
    Document doc = new Document("text", text)
      .append("review", reviewOutOfTen)
      .append("pictures", pictures)
      .append("restaurantID", restaurantID)
      .append("id", GenerateHashID.generateUUID())
      .append("username", username)
      .append("timestamp", timestamp);
    postsCollection.insertOne(doc);
  }

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

        posts.add(new Post(text, review, pictures, username, restaurantID, id, timestamp));
      }
    };
    postsCollection.find(eq("username", username))
        .forEach(existsBlock);
    return posts;
  }

  public void checkPost() {
    //createPost("I just went to the worst restaurant!", 0,
      //new ArrayList<>(), "001", "ethan", "10:03");

    //System.out.println(getPostsFromUser("sdf").size());


    // createRestaurant("Bajas Tex Mex", "273 Thayer St, Providence, Rhode Island");
  }

}
