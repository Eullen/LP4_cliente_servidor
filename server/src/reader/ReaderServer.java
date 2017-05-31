package reader;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import filemanager.IOManager;
import socket.Server;

public class ReaderServer {
  private DynamicReader reader;

  public ReaderServer(IOManager io, File file) throws IOException {
    this.reader = new DynamicReader(io, file);
  }

  public void exec(Socket socket, String message) {
    switch (message) {
    case "NEXT_WORD":
      reader.nextWord((word) -> {
        Server.sendMessage(socket, word);
      });
      break;
    case "PREVIOUS_WORD":

      break;
    case "RESET":
      reader.reset();
      Server.sendMessage(socket, "RESETADO");
      break;
    case "END":
      reader.toTheEnd();
      break;
    default:

      break;
    }
  }
}
