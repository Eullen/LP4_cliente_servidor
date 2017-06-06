package gui;

import java.io.IOException;
import java.net.UnknownHostException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import socket.Client;

public class Grafica implements UserInterface {
  private Button pause;
  private Button play;
  private Button stop;
  private Label text;
  private Timeline timeline;
  private Slider slider;
  private Client client;

  public Grafica(String host, int port) throws UnknownHostException, IOException {
    // TODO Auto-generated constructor stub
    this.client = Client.init(host, port, (socket, message) -> {
      if (message.equals("END_FILE")) {
        finishTimeline();
      } else if (!message.equals("RESETADO")) {
        print(message);
      }
    });
  }

  @Override
  public void print(String text) {
    this.text.setText(text);
  }

  @Override
  public void init(Stage window) {
    this.timeline = new Timeline();
    this.slider = new Slider();
    this.pause = new Button("Pause");
    this.play = new Button("Play");
    this.stop = new Button("Stop");
    this.text = new Label();
    this.text.setPrefSize(600, 400);
    this.text.setFont(new Font("Verdana", 16));
    this.text.setTextFill(Color.BLACK);

    createAnimation();

    VBox vBox = new VBox(4);
    Scene scene = new Scene(vBox, 600, 500);
    HBox hBox = new HBox(3);
    vBox.getChildren().add(this.text);
    vBox.getChildren().add(this.slider);
    hBox.getChildren().add(this.pause);
    hBox.getChildren().add(this.play);
    hBox.getChildren().add(this.stop);
    vBox.getChildren().add(hBox);
    hBox.setAlignment(Pos.CENTER);

    this.text.setAlignment(Pos.CENTER);

    this.stop.setDisable(true);
    this.pause.setDisable(true);

    this.slider.setMin(0.1);
    this.slider.setMax(15);
    this.slider.setValue(5);
    this.slider.setBlockIncrement(0.12);

    this.slider.valueProperty().addListener((event) -> {
      this.timeline.pause();
      changeInterval((float)(15.9 - this.slider.getValue()));
      if (this.play.isDisabled()) {
        this.timeline.play();
      }
    });

    this.play.setOnAction((event) -> {
      this.text.setText(new String());
      this.play.setDisable(true);
      this.stop.setDisable(false);
      this.pause.setDisable(false);
      this.timeline.play();
    });

    this.pause.setOnAction((event) -> {
      this.timeline.pause();
      this.play.setDisable(false);
      this.pause.setDisable(true);
    });

    this.stop.setOnAction((event) -> {
      this.text.setText("");
      this.timeline.stop();
      try {
        client.sendMessage("RESET");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      this.play.setDisable(false);
      this.stop.setDisable(true);
      this.pause.setDisable(true);
    });
    window.setScene(scene);
    window.show();
  }

  private void createAnimation() {
    this.timeline = new Timeline();
    this.timeline.setCycleCount(Timeline.INDEFINITE);
    configTimeline(Duration.millis(500), onAction());
  }

  private void changeInterval(float interval) {
    this.timeline = new Timeline();
    this.timeline.setCycleCount(Timeline.INDEFINITE);
    configTimeline(Duration.millis(interval * 100), onAction());
  }

  private void configTimeline(Duration duration, EventHandler<ActionEvent> event) {
    this.timeline.getKeyFrames().add(new KeyFrame(duration, event));
  }

  public EventHandler<ActionEvent> onAction() {
    return (event) -> {
      try {
        client.sendMessage("NEXT_WORD");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    };
  }

  public void finishTimeline() {
    this.timeline.stop();
    this.timeline.setCycleCount(0);
    try {
      client.sendMessage("RESET");
    } catch (IOException e) {
      e.printStackTrace();
    }
    resetButtons();
    changeInterval((float) (15.9 - this.slider.getValue()));
  }

  private void resetButtons() {
    this.play.setDisable(false);
    this.stop.setDisable(true);
    this.pause.setDisable(true);
  }

}