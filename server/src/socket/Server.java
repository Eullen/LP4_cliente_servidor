package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends Thread {
  private Socket               socket;
  private SocketMessageHandler messageHandler;
  private static ServerSocket  serverSocket;

  private Server(Socket socket, SocketMessageHandler messageHandler) throws UnknownHostException, IOException {
    this.messageHandler = messageHandler;
    this.socket         = socket;
  }

  public void receiveMessage(Socket socketIn)
      throws UnknownHostException, IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(socketIn.getInputStream()));
    while(true){
      String line = input.readLine();
      messageHandler.receive(socketIn, line);
      if(line == null) break;
    }
  }

  public static void sendMessage(Socket socket, String mess){
    PrintStream out;
    try {
      out = new PrintStream(socket.getOutputStream());
      out.println(mess);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  
  }

  @Override
  public void run() {
    try {
      receiveMessage(socket);
    } catch (Exception e1) {
    	close();
    }
  }

  public static void init(int port, SocketMessageHandler message) throws IOException {
    serverSocket = new ServerSocket(port);
    while (true) {
      Socket client = serverSocket.accept();
      Server server = new Server(client, message);
      server.start();
    }
  }
  
  public void close(){
	try {
		socket.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
