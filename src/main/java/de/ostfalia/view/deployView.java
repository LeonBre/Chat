package de.ostfalia.view;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class deployView {
  public String deployChat(){
    try {
        Runtime.getRuntime().exec("/home/pi/scripts/deployChat.sh");
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
  }
}
