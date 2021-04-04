package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.Commands.NaiveNeighbors;
import edu.brown.cs.student.stars.Commands.Neighbors;
import edu.brown.cs.student.stars.Commands.StarsCommand;
import edu.brown.cs.student.stars.DataTypes.Star;
import edu.brown.cs.student.stars.KDTree.KDNode;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class handles most of the testing for the neighbors and naive_neighbors commands
 */
public class NeighborsTest {


  /**
   * This test randomly generates star data.
   * It then compares the output of the naive and non-naive implementation of the neighbors command
   * on this data using a randomly generated numNeighbors value and randomly generated X Y Z target values.
   * Because the naive and non-naive commands will output star ids in a random order if they are equidistant
   * from the target value, this test does not compare that the stars of the two implementations are the same,
   * but rather that their distances are the same.
   * @throws FileNotFoundException when file is not found
   */
  @Test
  public void RandomGenTestNaiveNotNaiveNeighbors() throws FileNotFoundException { //Note: Math.random just generates a numb between 0 and 1
    //This for loop is currently commented out to improve time efficiency when building.
    //However, this can be run as many times as you would like to continuously generate new random inputs for testing
    //for (int q=0; q< 100; q++) {
      //randomly generate number of stars
      StarsCommand starsCommand = new StarsCommand();
      Neighbors neighbors = new Neighbors(starsCommand);
      NaiveNeighbors naiveNeighbors = new NaiveNeighbors(starsCommand);
      List<Star> starsList = new ArrayList<>();

      int numStars = (int) (Math.random() * 10) + 1;
      for (int i=0; i < numStars; i++ ) {
        double x = (Math.random() * 100) -50; //random x value between -50 and 50
        double y = (Math.random() * 100) -50;
        double z = (Math.random() * 100) -50;
        Star star = new Star(i, "testStar", x, y, z);
        starsList.add(star);
      }
      starsCommand.loadStarsForTesting(starsList);
      int neigh = (int) (Math.random() * 20) - 10;
      double targetX = (Math.random() * 100) -50;
      double targetY = (Math.random() * 100) -50;
      double targetZ = (Math.random() * 100) -50;
      List<String> input = new ArrayList<>();
      input.add("filler");
      input.add(String.valueOf(neigh));
      input.add(String.valueOf(targetX));
      input.add(String.valueOf(targetY));
      input.add(String.valueOf(targetZ));
      neighbors.run(input);
      List<KDNode>  non_naive = neighbors.getAnswerKDNodeList();
      naiveNeighbors.run(input);
      List<Star> naive = naiveNeighbors.getAnswerStarsList();
      assertEquals(naive.size(), non_naive.size());
      for (int j= 0; j < naive.size(); j++){
        double naive_dist = Math.pow((naive.get(j).getDimensions(1) - targetX), 2);
        naive_dist += Math.pow((naive.get(j).getDimensions(2) - targetY), 2);
        naive_dist += Math.pow((naive.get(j).getDimensions(3) - targetZ), 2);
        double dist = Math.pow((non_naive.get(j).getValue().getDimensions(1) - targetX), 2);
        dist += Math.pow((non_naive.get(j).getValue().getDimensions(2) - targetY), 2);
        dist += Math.pow((non_naive.get(j).getValue().getDimensions(3) - targetZ), 2);
        assertEquals(naive_dist, dist, .1);
      }
    //}
  }

  /**
   * Testing that exception is caught when target x,y,z are of incorrect type
   * (not doubles) when running Neighbors command
   * @throws Exception throws an exception
   */
  @Test
  public void NeighborsInvalidTargetDim() throws Exception {
    StarsCommand starsCommand = new StarsCommand();
    Neighbors neighbors = new Neighbors(starsCommand);
    List<Star> starsList = new ArrayList<>();
    Star star0 = new Star(0, "testStar", 0, 0 , 0);
    starsList.add(star0);
    starsCommand.loadStarsForTesting(starsList);
    List<String> input = new ArrayList<>();
    input.add("neighbors");
    input.add("3");
    input.add("x"); //invalid target X param
    input.add("0");
    input.add("0");
    neighbors.run(input);
  }

  /**
   * Testing that exception is caught when incorrect parameter input for
   * if k param is not an int when running Neighbors command
   * @throws Exception throws an exception
   */
  @Test
  public void NeighborsInvalidKparam() throws Exception {
    StarsCommand starsCommand = new StarsCommand();
    Neighbors neighbors = new Neighbors(starsCommand);
    List<Star> starsList = new ArrayList<>();
    Star star0 = new Star(0, "testStar", 0, 0 , 0);
    starsList.add(star0);
    starsCommand.loadStarsForTesting(starsList);
    List<String> input = new ArrayList<>();
    input.add("neighbors");
    input.add("x");
    input.add("0"); //invalid target X param
    input.add("0");
    input.add("0");
    neighbors.run(input);
  }

  /**
   * Testing that exception is caught when target x,y,z are of incorrect type
   * when running NaiveNeighbors command
   * @throws Exception throws an exception
   */
  @Test
  public void NaiveNeighborsInvalidTargetDim() throws Exception {
    StarsCommand starsCommand = new StarsCommand();
    NaiveNeighbors naiveNeighbors = new NaiveNeighbors(starsCommand);
    List<Star> starsList = new ArrayList<>();
    Star star0 = new Star(0, "testStar", 0, 0 , 0);
    starsList.add(star0);
    starsCommand.loadStarsForTesting(starsList);
    List<String> input = new ArrayList<>();
    input.add("naive_neighbors");
    input.add("3");
    input.add("x"); //invalid target X param
    input.add("0");
    input.add("0");
    naiveNeighbors.run(input);
  }

  /**
   * Testing that exception is caught when incorrect parameter input for
   * if k param is not an int
   * when running NaiveNeighbors command
   * @throws Exception
   */
  @Test
  public void NaiveNeighborsInvalidKparam() throws Exception {
    StarsCommand starsCommand = new StarsCommand();
    NaiveNeighbors naiveNeighbors = new NaiveNeighbors(starsCommand);
    List<Star> starsList = new ArrayList<>();
    Star star0 = new Star(0, "testStar", 0, 0 , 0);
    starsList.add(star0);
    starsCommand.loadStarsForTesting(starsList);
    List<String> input = new ArrayList<>();
    input.add("naive_neighbors");
    input.add("x");
    input.add("0"); //invalid target X param
    input.add("0");
    input.add("0");
    naiveNeighbors.run(input);
  }

  /**
   * Testing that exception is caught when incorrect parameter input for
   * if k param is less than zero
   * when running NaiveNeighbors command
   * @throws Exception
   */
  @Test
  public void NaiveNeighborsKLessThanZero() throws Exception {
    StarsCommand starsCommand = new StarsCommand();
    NaiveNeighbors naiveNeighbors = new NaiveNeighbors(starsCommand);
    List<Star> starsList = new ArrayList<>();
    Star star0 = new Star(0, "testStar", 0, 0 , 0);
    starsList.add(star0);
    starsCommand.loadStarsForTesting(starsList);
    List<String> input = new ArrayList<>();
    input.add("naive_neighbors");
    input.add("-1");
    input.add("0"); //invalid target X param
    input.add("0");
    input.add("0");
    naiveNeighbors.run(input);
  }

}


