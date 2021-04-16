package edu.brown.cs.student.foodcrawl.DBCommands;

import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.Restaurant;
import edu.brown.cs.student.foodcrawl.DataStructures.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RecommendationAlgo {

  /**
   * returns 3 recommended restaurants.
   * @param username user we are generating recommmendations for
   * @return list of restaurants wee recommend
   */
  public static List<Restaurant> recommend(String username) {
    MongoDBConnection m = new MongoDBConnection();
    // results is the list of restaurant ids we recommend
    List<String> results = recommendBasedOnUserSimilarity(username);
    // restaurantsList is the list of restaurants that correspond to the results' ids
    List<Restaurant> restaurantList = new ArrayList<>();
    for (String rID : results) {
      Restaurant r = m.getRestaurantByID(rID);
      restaurantList.add(r);
      // only getting top 3
      if (restaurantList.size() == 3) {
        break;
      }
    }
//    if (restaurantList.size() == 0) {
//      return null;
//    }
    if (restaurantList.size() < 3) {
      // get the globally highest recommended restaurants based on all posts
      // and use these posts if not enough are returned from recommendBasedOnUserSimilarity
      List<String> theBest = globallyHighestReviewedRestaurants();
      for (String bestRID : theBest) {
        if (!results.contains(bestRID)) {
          Restaurant bestR = m.getRestaurantByID(bestRID);
          restaurantList.add(bestR);
        }
        if (restaurantList.size() == 3) {
          break;
        }
      }
      return restaurantList;

    } else {
      return restaurantList;
    }
  }

  public static List<String> globallyHighestReviewedRestaurants() {
    // get the avg ratings
    MongoDBConnection m = new MongoDBConnection();
    HashMap<String, Double> avgRatings = m.computeRatings();
    // maps restaurant id to avg rating

    // sort by value to get the top restaurants
    CompareByValueDouble comp = new CompareByValueDouble(avgRatings);
    TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(comp);
    sortedMap.putAll(avgRatings);

    // get the restaurant ids of the top 5 restauants;
    List<String> topRestaurants = new ArrayList<>();
    for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
      String key = entry.getKey();
      Double value = entry.getValue();
      System.out.println(key + " => " + value);

      topRestaurants.add(key);

      if (topRestaurants.size() == 5) {
        break;
      }
    }

    return topRestaurants;
  }

  /**
   * returns the restaurants ids of restaurants that your most similar users have eaten at
   * that youo haven't eaten at yet.
   * @param username given user we are computing recommendations for
   * @return list of restaurant ids
   */
  public static List<String> recommendBasedOnUserSimilarity(String username) {
    MongoDBConnection m = new MongoDBConnection();
    User u = m.getUserByUsername(username);
    List<String> following = u.getFollowing();
    List<User> followingUsers = new ArrayList<>();
    Map<String, Integer> similarity = new HashMap<>();
    // maps the similarity of each user to its similarity score

    for (String followingUser : following) {
      similarity.put(followingUser, 0);
      User fu = m.getUserByUsername(followingUser);
      followingUsers.add(fu);
    }

    // if the two users are mutual followers (they both follow each other, increase similarity
    for (String followerUser : u.getFollowers()) {
      for (String followingUser : following) {
        if (followerUser.equals(followingUser)) {
          if (similarity.containsKey(followerUser)) {
            int curScore = similarity.get(followerUser);
            int newScore = curScore + 10;
            similarity.put(followerUser, newScore);
          }
        }
      }
    }

    // get posts from the user's following users and compare for similarity
    List<Post> userPosts = m.getPostsFromUser(username);
    // maps from restaurant id to rating the given user gave it
    Map<String, Integer> restaurantRatings = new HashMap<>();
    List<String> restaurantsUserHasBeenTo = new ArrayList<>();

    for (Post p : userPosts) {
      int rating = p.getReviewOutOfTen();
      String id = p.getRestaurantID();
      restaurantRatings.put(id, rating);
      restaurantsUserHasBeenTo.add(id);
    }

    // add similarity score if they've posted about the same restaurant
    for (User fuu : followingUsers) {
      List<Post> fuuPosts = m.getPostsFromUser(fuu.getUsername());
      for (Post po: fuuPosts) {
        // check if the followerUser has reviewed the same restaurant the given user has
        String rid = po.getRestaurantID();
        int rating = po.getReviewOutOfTen();
        if (restaurantRatings.containsKey(rid)) {
          int difference = Math.abs(restaurantRatings.get(rid) - rating);
          int extraSimilarity = 10 - difference;

          //System.out.println(similarity);
          //System.out.println(fuu);
          int curSim = similarity.get(fuu.getUsername());
          similarity.put(fuu.getUsername(), extraSimilarity + curSim);
        }
      }
    }

    // go through the dictionary and pick out the top 3 users with the highest scores
    CompareByValue comp = new CompareByValue(similarity);
    TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(comp);
    sorted_map.putAll(similarity);

    // get a list of the top 3 users
    List<String> topThreeUsers = new ArrayList<>();
    for(Map.Entry<String, Integer> entry : sorted_map.entrySet()) {
      String key = entry.getKey();
      Integer value = entry.getValue();
      System.out.println(key + " => " + value);

      topThreeUsers.add(key);

      if (topThreeUsers.size() == 3) {
        break;
      }
    }

    // get a set of all the restaurants that those top3 users have been to but the user hasn't been to
    Set<String> topThreesRestaurants = new HashSet<>();
    for (String topUsername : topThreeUsers) {
      // get all of their restaurants and add it to a set
      List<Post> postsFromTopUser = m.getPostsFromUser(topUsername);
      for (Post pFromTopUser : postsFromTopUser) {
        topThreesRestaurants.add(pFromTopUser.getRestaurantID());
      }
    }

    for (String rids : restaurantsUserHasBeenTo) {
      if (topThreesRestaurants.contains(rids)) {
        topThreesRestaurants.remove(rids);
      }
    }

    List<String> aList = new ArrayList<String>(topThreesRestaurants);
    return aList;

  }
}

// based off of response to https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values

/**
 * comparator class for sorting hashmap by value in descending order.
 */
class CompareByValue implements Comparator<String> {
  Map<String, Integer> theMap;

  public CompareByValue(Map<String, Integer> theMap) {
    this.theMap = theMap;
  }

  // sorts from greatest to least
  public int compare(String a, String b) {
    if (theMap.get(a) >= theMap.get(b)) {
      return -1;
    } else {
      return 1;
    }
  }
}

class CompareByValueDouble implements Comparator<String> {
  Map<String, Double> theMap;

  public CompareByValueDouble(Map<String, Double> theMap) {
    this.theMap = theMap;
  }

  // sorts from greatest to least
  public int compare(String a, String b) {
    if (theMap.get(a) >= theMap.get(b)) {
      return -1;
    } else {
      return 1;
    }
  }
}
