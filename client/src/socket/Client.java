package socket;

import java.net.*;
import java.io.*;

public class Client{
  private Socket               socket;
  private SocketMessageHandler handler;

  private Client(Socket socket, SocketMessageHandler handler) throws UnknownHostException, IOException {
    this.socket   = socket;
    this.handler  = handler;
  }

  public Client(Socket socketIn) {
    socket = socketIn;
  }

  public void sendMessage(String mess) throws IOException {
    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
    output.println(mess);
    receive(socket, handler);
  }

  public void receiveMessage(SocketMessageHandler messageHandler) throws UnknownHostException, IOException {
    receive(socket, messageHandler);
  }

  private void receive(Socket socket, SocketMessageHandler messageHandler) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String line = input.readLine();
    messageHandler.receive(socket, line);
  }


  public void close() throws IOException {
    socket.close();
  }

  public static Client init(String host, int port, SocketMessageHandler messageHandler)
      throws UnknownHostException, IOException {
    Client client = new Client(new Socket(host, port), messageHandler);
    return client;
  }

}
