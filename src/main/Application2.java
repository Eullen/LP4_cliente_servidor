package main;

import java.io.IOException;

import socket.Server;

public class Application2 {
  public static void main(String[] args) {
    try {
      Server.init(8090, (socket, message) -> {
        System.out.println(message);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
