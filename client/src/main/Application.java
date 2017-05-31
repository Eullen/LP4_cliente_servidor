package main;

import java.io.IOException;
import java.net.UnknownHostException;

import gui.Grafica;
import gui.UserInterface;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
  
  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage window) throws Exception {
    init(window);
  }

  private void init(Stage window) throws UnknownHostException, IOException {
    UserInterface gui = new Grafica("localhost", 8090);
    gui.init(window);
  }
}
