package main;

import java.io.IOException;

import gui.Grafica;
import gui.UserInterface;
import javafx.stage.Stage;
import socket.Client;

public class Application extends javafx.application.Application {
  
  public static void main(String[] args) {

    try {
      Client client = Client.init("localhost", 8090, (socket, message) -> {
        System.out.println(message);
      });
      client.sendMessage("client muito daora");
      System.out.println("aqui é o client");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // launch();
  }

  public void start(Stage window) throws Exception {
    init(window);
  }

  private void init(Stage window) {
    UserInterface gui = new Grafica();
    gui.init(window);
  }
}
