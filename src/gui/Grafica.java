package gui;

import java.io.File;

import javax.swing.JOptionPane;

import filemanager.DynamicReader;
import filemanager.FileManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Grafica implements UserInterface {
  private Button pause;
  private Button play;
  private Button stop;
  private Label text;
  private MenuBar menu;
  private FileChooser fileChooser;
  private Timeline timeline;
  private Slider slider;
  private DynamicReader reader;

  @Override
  public void print(String text) {
    this.text.setText(text);
  }

  @Override
  public void init(Stage window) {
    this.timeline = new Timeline();
    this.slider = new Slider();
    this.fileChooser = new FileChooser();
    this.pause = new Button("Pause");
    this.play = new Button("Play");
    this.stop = new Button("Stop");
    this.text = new Label();
    this.menu = new MenuBar();
    this.text.setPrefSize(600, 400);
    this.text.setFont(new Font("Verdana", 16));
    this.text.setTextFill(Color.BLACK);

    // create menu
    Menu menuFile = new Menu("File");
    MenuItem open = new MenuItem("Open");
    menuFile.getItems().add(open);

    // openFile
    open.setOnAction((event) -> openFile(window));

    this.menu.getMenus().addAll(menuFile);
    VBox vBox = new VBox(4);
    Scene scene = new Scene(vBox, 600, 500);
    HBox hBox = new HBox(3);
    vBox.getChildren().addAll(menu);
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
    this.play.setDisable(true);
    this.slider.setDisable(true);

    this.slider.setMin(0.1);
    this.slider.setMax(15);
    this.slider.setValue(5);
    this.slider.setBlockIncrement(0.12);

    this.slider.valueProperty().addListener((event) -> {
      this.timeline.pause();
      changeInterval((float) this.slider.getValue());
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
      this.reader.reset();
      this.play.setDisable(false);
      this.stop.setDisable(true);
      this.pause.setDisable(true);
    });
    window.setScene(scene);
    window.show();
  }

  public void openFile(Stage window) {
    File file = fileChooser.showOpenDialog(window);
    if (file != null) {
      try {
        this.timeline.stop();
        print(new String());
        this.reader = new DynamicReader(new FileManager(), file, this);
        this.play.setDisable(false);
        this.slider.setDisable(false);
        createAnimation();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void createAnimation() {
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
    return (event) -> this.reader.nextWord((word) -> print(word));
  }

  public void finishTimeline() {
    this.timeline.stop();
    this.timeline.setCycleCount(0);
    this.reader.reset();
    resetButtons();
    createAnimation();
  }

  private void resetButtons() {
    this.play.setDisable(false);
    this.stop.setDisable(true);
    this.pause.setDisable(true);
  }
}