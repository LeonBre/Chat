package de.ostfalia.helper;

public final class RandomPicker {
  
  public static String generateRandomColor() {
    String [] possibleColorValues = {"green", "yellow", "red", "blue", "white", "black"};
    int randomPosition =(int) Math.round(Math.random() * possibleColorValues.length);
    return possibleColorValues[randomPosition];
  }
  
  public static String generateRandomUsername() {
    String [] possibleRandomUsernames = {"Anonyme Ananas", "Anonymer Maulwurf", "Anonymer Karl der Käfer"
        , "Anonymer Mannbärschwein", "Anonymes Riesenkrokodil", "Anonymer Dinosaurier" 
        , "Anonymer Anonymer", "Anonymer Astronautenhelm", "Anonymer Apfel"};
    int randomPosition =(int) Math.round(Math.random() * possibleRandomUsernames.length);
    return possibleRandomUsernames[randomPosition];
  }
}
