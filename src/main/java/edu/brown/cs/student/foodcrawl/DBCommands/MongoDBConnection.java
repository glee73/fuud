package edu.brown.cs.student.foodcrawl.DBCommands;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

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

  public void createUser() {
    Document doc = new Document("username", "ethan")
      .append("password", "lmfao");
    usersCollection.insertOne(doc);
  }

  public void check() {
    createUser();
    System.out.println("hi");
  }



}
