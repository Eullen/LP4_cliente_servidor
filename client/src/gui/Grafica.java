package gui;

import java.io.IOException;
import java.net.UnknownHostException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import socket.Client;

public class Grafica implements UserInterface {
	private Button pause;
	private Button play;
	private Button stop;
	private Button next;
	private Button previous;
	private Button end;
	private Button begin;
	private Label text;
	private Timeline timeline;
	private Slider slider;
	private Client client;

	public Grafica(String host, int port) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.client = Client.init(host, port, (socket, message) -> {
			switch (message) {
			case "END_FILE":
				this.finishTimeline();
				break;
			case "BEGIN_FILE":
				this.print("---");
				break;
			case "RESETADO":
				System.out.println(message);
				break;
			default:
				this.print(message);
				break;
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
		this.next = new Button(">|");
		this.previous = new Button("|<");
		this.end = new Button(">>");
		this.begin = new Button("<<");
		this.text = new Label();
		this.text.setPrefSize(600, 400);
		this.text.setFont(new Font("Verdana", 16));
		this.text.setTextFill(Color.BLACK);

		this.createAnimation();

		VBox vBox = new VBox(4);
		Scene scene = new Scene(vBox, 600, 500);
		HBox primaryButtonsBox = new HBox(3);
		HBox secondaryButtonsBox = new HBox(4);
		vBox.getChildren().add(this.text);
		vBox.getChildren().add(this.slider);
		primaryButtonsBox.getChildren().add(this.pause);
		primaryButtonsBox.getChildren().add(this.play);
		primaryButtonsBox.getChildren().add(this.stop);
		vBox.getChildren().add(primaryButtonsBox);
		secondaryButtonsBox.getChildren().add(this.begin);
		secondaryButtonsBox.getChildren().add(this.previous);
		secondaryButtonsBox.getChildren().add(this.next);
		secondaryButtonsBox.getChildren().add(this.end);
		vBox.getChildren().add(secondaryButtonsBox);
		primaryButtonsBox.setAlignment(Pos.CENTER);
		secondaryButtonsBox.setAlignment(Pos.CENTER);

		this.text.setAlignment(Pos.CENTER);

		this.stop.setDisable(true);
		this.pause.setDisable(true);

		this.slider.setMin(0.1);
		this.slider.setMax(15);
		this.slider.setValue(5);
		this.slider.setBlockIncrement(0.12);

		this.slider.valueProperty().addListener((event) -> {
			this.timeline.pause();
			this.changeInterval((float) (15.9 - this.slider.getValue()));
			if (this.play.isDisabled()) {
				this.timeline.play();
			}
		});

		this.play.setOnAction((event) -> {
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
			this.client.sendMessage("RESET");
			this.play.setDisable(false);
			this.stop.setDisable(true);
			this.pause.setDisable(true);
		});

		this.previous.setOnAction((event) -> {
			// timeline t� parada esperando messagem e ai?
			this.timeline.stop();
			this.client.sendMessage("PREVIOUS_WORD");
		});
		window.setScene(scene);
		window.show();
	}

	private void createAnimation() {
		this.timeline = new Timeline();
		this.timeline.setCycleCount(Animation.INDEFINITE);
		this.configTimeline(Duration.millis(500), this.onAction());
	}

	private void changeInterval(float interval) {
		this.timeline = new Timeline();
		this.timeline.setCycleCount(Animation.INDEFINITE);
		this.configTimeline(Duration.millis(interval * 100), this.onAction());
	}

	private void configTimeline(Duration duration, EventHandler<ActionEvent> event) {
		this.timeline.getKeyFrames().add(new KeyFrame(duration, event));
	}

	public EventHandler<ActionEvent> onAction() {
		return (event) -> this.client.sendMessage("NEXT_WORD");
	}

	public void finishTimeline() {
		this.timeline.stop();
		this.timeline.setCycleCount(0);
		this.client.sendMessage("RESET");
		this.resetButtons();
		this.changeInterval((float) (15.9 - this.slider.getValue()));
	}

	private void resetButtons() {
		this.play.setDisable(false);
		this.stop.setDisable(true);
		this.pause.setDisable(true);
	}

}