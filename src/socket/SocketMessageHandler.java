package socket;

import java.net.Socket;

public interface SocketMessageHandler {
  public void receive(Socket socket, String message);
}
