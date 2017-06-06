package main;

import java.io.IOException;
import java.net.UnknownHostException;

import gui.Grafica;
import javafx.stage.Stage;

/**
 * The Class Application.
 */
public class Application extends javafx.application.Application {
  private Grafica gui;
  
  /**
   * É o método main.
   *
   * @param args argumentos externos
   */
  public static void main(String[] args) {
    launch(args);
  }

  /* (non-Javadoc)
   * @see javafx.application.Application#start(javafx.stage.Stage)
   */
  public void start(Stage window) throws Exception {
    init(window);
  }

  /**
   * Inicia o programa
   *
   * @param window é um stage do javaFX
   * @throws UnknownHostException é uma exceção desconhecida do host
   * @throws IOException é sinal que uma exceção de I/O ocorreu.
   */
  private void init(Stage window) throws UnknownHostException, IOException {
    gui = new Grafica("127.0.0.1", 8090);
    gui.init(window);
  }
  
  /* (non-Javadoc)
   * @see javafx.application.Application#stop()
   */
  public void stop(){
	  gui.finishTimeline();
  }
}
