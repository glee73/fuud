package edu.brown.cs.student.foodcrawl.DBCommands;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.model.Updates;
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
import java.util.ArrayList;
import java.util.List;


public class MongoDBConnection {

  MongoCollection<Document> usersCollection;
  MongoCollection<Document> restaurantsCollection;
  MongoCollection<Document> postsCollection;

  public MongoDBConnection() {
    MongoClientURI uri = new MongoClientURI(
      "mongodb+srv://tim:Ihatethisclass2!@foodcrawl.75jup.mongodb.net/test?retryWrites=true&w=majority");

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
        .append("following", Arrays.asList());
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
        List<String> following = (List<String>) document.get("followers");
        List<String> followers = (List<String>) document.get("following");
        found[0] = new User(username, password, followers, following);
      }
    };
    usersCollection.find(eq("username", username))
      .forEach(existsBlock);
    return found[0];
  }

  public void checkUser() {
    //createUser("bob", "pw");
    //createUser("ethan", "yuh");
    //addFollower("bob", "ethan");
    System.out.println(checkUsernameExists("ethan"));
    System.out.println(checkUsernameExists("ethhhjian"));
    System.out.println("hi");

    System.out.println(getUserByUsername("ethan").getFollowers());
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

}
