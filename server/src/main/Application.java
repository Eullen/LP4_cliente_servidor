package main;

import java.io.File;
import java.io.IOException;

import filemanager.DynamicReader;
import filemanager.FileManager;
import socket.Server;

public class Application {
  public static void main(String[] args) {
    try {
    	
      File file = new File("Files\\teste.txt");
      System.out.println("FileExist: " + file.exists());
      DynamicReader reader = new DynamicReader(new FileManager(), file);
      Server.init(8090, (socket, message) -> {
        System.out.println(message);
        switch(message){
	        case "NEXT_WORD":
	          reader.nextWord((word)->{
	            try {
                Server.sendMessage(socket,word);
              } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
	          });
	        	break;
	        case "PREVIOUS_WORD":
	          
	        	break;
	        case "RESET":
	          reader.reset();
	        	break;
	        case "END":
	          reader.toTheEnd();
	        	break;	
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
