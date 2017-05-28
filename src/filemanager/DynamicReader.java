package filemanager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import main.Application;
import gui.Grafica;
import socket.Client;

public class DynamicReader {
  private Client client;
  private List<String> lines;
  private String wordsOfActualLine[];
  private int linePointer;
  private int columnPointer;
  private int qtyWords;
  private Grafica ui;
  private boolean finished;

  public DynamicReader(IOManager io, File file, Grafica grafica) throws IOException {
    io.open(file);
    this.lines = io.read();
    this.lines.add("FIM!!");
    this.qtyWords = totalOfWords();
    this.linePointer = 0;
    this.columnPointer = 0;
    this.ui = grafica;
    readFirstLineIfExists();
  }

  private void readFirstLineIfExists() {
    if (lines.size() > 0) {
      this.wordsOfActualLine = getWordsOfLine(lines.get(0));
    }
  }

  public List<String> getList() {
    return this.lines;
  }

  private boolean isLastLine() {
    return linePointer == lines.size();
  }

  private boolean isLastColumn(String line[]) {
    return columnPointer == line.length;
  }

  private int totalOfWords() {
    int count = 0;
    for (String line : this.lines) {
      count += getWordsOfLine(line).length;
    }
    return count;
  }

  private String[] getWordsOfLine(String line) {
    String out[] = { " " };
    if (!line.trim().isEmpty()) {
      out = line.trim().split(" ");
    }
    return out;
  }

  public int getQtyWords() {
    return qtyWords;
  }

  public void nextWord(AfterLoadWord after) {
    if (!isFinished()) {
      if (!isLastColumn(this.wordsOfActualLine)) {
        String word = this.wordsOfActualLine[columnPointer++];
        after.exec(word);
      } else {
        toNextLine();
      }
    } else {
      ui.finishTimeline();
    }
  }

  private void toNextLine() {
    linePointer++;
    columnPointer = 0;
    if (!isLastLine()) {
      wordsOfActualLine = getWordsOfLine(lines.get(linePointer));
    } else {
      finished = true;
    }
  }

  public void reset() {
    columnPointer = 0;
    linePointer = 0;
    finished = false;
    readFirstLineIfExists();
  }

  public boolean isFinished() {
    return finished;
  }

}
