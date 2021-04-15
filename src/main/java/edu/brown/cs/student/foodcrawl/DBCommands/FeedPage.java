package edu.brown.cs.student.foodcrawl.DBCommands;

import edu.brown.cs.student.foodcrawl.DataStructures.Post;
import edu.brown.cs.student.foodcrawl.DataStructures.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeedPage {

  /**
   * gets all posts the user would see on their feed page.
   * gets all posts from all of their followers. sorted in order by timestamp.
   * @param username
   * @return list of posts
   */
  public static List<Post> getFeedPagePosts(String username) {
    // call the db command
  // TODO: sortt!!!!
    List<Post> feedpage = new ArrayList<>();
    MongoDBConnection m = new MongoDBConnection();
    User u = m.getUserByUsername(username);
    if (u == null) {
      return feedpage;
    }
    List<String> usersYouFollow = u.getFollowing();
    if (usersYouFollow == null) {
      return feedpage;
    }
    for (String us : usersYouFollow) {
      List<Post> pst = m.getPostsFromUser(us);
      feedpage.addAll(pst);
    }
    /*
    feedpage.sort(new Comparator<Post>() {
      @Override
      public int compare(Post o1, Post o2) {
        String t1 = o1.getTimestamp();
        String t2 = o2.getTimestamp();
        
        return 0;
      }
    }) */
    return feedpage;
  }
}
