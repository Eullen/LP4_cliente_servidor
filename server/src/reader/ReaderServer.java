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
		System.out.println(message);
		switch (message) {
		case "PREVIOUS_WORD":
			this.reader.previousWord((word) -> {
				Server.sendMessage(socket, word);
			});
			break;
		case "NEXT_WORD":
			this.reader.nextWord((word) -> {
				Server.sendMessage(socket, word);
			});
			break;
		case "BEGIN":
			this.reader.toTheBegin();
			break;
		case "END":
			this.reader.toTheEnd();
			break;
		case "RESET":
			this.reader.reset();
			Server.sendMessage(socket, "RESETADO");
			break;
		}
	}
}
