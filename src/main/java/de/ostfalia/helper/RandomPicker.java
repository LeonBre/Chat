package de.ostfalia.helper;

public final class RandomPicker {
  
  public static String generateRandomColor() {
    String [] possibleColorValues = {"green", "yellow", "red", "blue", "white", "black"};
    int randomPosition =(int) Math.round(Math.random() * possibleColorValues.length);
    return possibleColorValues[randomPosition];
  }
}
