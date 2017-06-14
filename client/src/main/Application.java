package main;

import java.io.IOException;
import java.net.UnknownHostException;

import gui.Grafica;
import javafx.stage.Stage;
import utils.MyAlert;

/**
 * The Class Application.
 */
public class Application extends javafx.application.Application {
	private Grafica gui;

	/**
	 * É o método main.
	 *
	 * @param args
	 *            argumentos externos
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage window) {
		try {
			this.init(window);
		} catch (IOException e) {
			MyAlert.makeErrorAlert("Erro interno no servidor");
			System.exit(0);
		}
	}

	/**
	 * Inicia o programa
	 *
	 * @param window
	 *            é um stage do javaFX
	 * @throws UnknownHostException
	 *             é uma exceção desconhecida do host
	 * @throws IOException
	 *             é sinal que uma exceção de I/O ocorreu.
	 */
	private void init(Stage window) throws UnknownHostException, IOException {
		this.gui = new Grafica("127.0.0.1", 8090);
		this.gui.init(window);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop() {
		this.gui.finishTimeline();
	}
}
