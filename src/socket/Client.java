package socket;

import java.net.*;
import java.io.*;

public class Client extends Thread {
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
  }

  public void receiveMessage(SocketMessageHandler messageHandler) throws UnknownHostException, IOException {
    receive(socket, messageHandler);
  }

  private void receive(Socket socket, SocketMessageHandler messageHandler) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    while (true) {
      String line = input.readLine();
      messageHandler.receive(socket, line);
      if (line == null) break;
    }
  }

  @Override
  public void run() {
    try {
      receive(socket, handler);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("deprecation")
  public void close() throws IOException {
    this.stop();
    socket.close();
  }

  public static Client init(String host, int port, SocketMessageHandler messageHandler)
      throws UnknownHostException, IOException {
    Client client = new Client(new Socket(host, port), messageHandler);
    client.start();
    return client;
  }

}
