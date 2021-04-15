package edu.brown.cs.student.foodcrawl.DBCommands;

/**
 * a class to encrypt passwords
 */
public class Encryptor {
  /**
   * a simple encryption method utilizing hashCode
   * @param plaintext the plaintext password, a string
   * @return the encrypted string
   */
  public String encrypt(String plaintext) {
    return String.valueOf(plaintext.hashCode());
  }
}
