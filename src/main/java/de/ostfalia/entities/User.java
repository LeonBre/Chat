package de.ostfalia.entities;

public class User {
  private String username;
  private String color;
  
  public User (String username, String color) {
    this.username = username;
    this.color = color;
  }
  
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getColor() {
    return color;
  }
  public void setColor(String color) {
    this.color = color;
  }
}
