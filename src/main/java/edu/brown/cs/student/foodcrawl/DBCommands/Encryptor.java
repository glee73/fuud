package edu.brown.cs.student.foodcrawl.DBCommands;

public class Encryptor {
  public String encrypt(String plaintext) {
    return String.valueOf(plaintext.hashCode());
  }
}
