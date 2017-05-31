package main;

import java.io.File;
import java.io.IOException;

import filemanager.FileManager;
import reader.ReaderServer;
import socket.Server;

public class Application {
  public static void main(String[] args) {
    try {

      File file = new File("Files\\teste.txt");
      System.out.println("FileExist: " + file.exists());
      ReaderServer readerServer = new ReaderServer(new FileManager(), file);
      Server.init(8090, (socket, message) -> {
        readerServer.exec(socket, message);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
