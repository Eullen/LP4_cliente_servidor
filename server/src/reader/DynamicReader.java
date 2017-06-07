package reader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.sun.security.ntlm.Client;

import filemanager.AfterLoadWord;
import filemanager.IOManager;

public class DynamicReader {
  private List<String> lines;
  private String wordsOfActualLine[];
  private int linePointer;
  private int columnPointer;
  private int qtyWords;
  private boolean finished;

  public DynamicReader(IOManager io, File file) throws IOException {
    io.open(file);
    this.lines = io.read();
    this.lines.add("FIM!!");
    this.qtyWords = totalOfWords();
    this.linePointer = 0;
    this.columnPointer = 0;
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
    if (!isFinish()) {
      if (!isLastColumn(this.wordsOfActualLine)) {
        String word = this.wordsOfActualLine[this.columnPointer++];
        after.exec(word);
      } else {
    	after.exec(new String());
        toNextLine();
      }
    } else {
    	after.exec("END_FILE");
    }
  }
  
  private boolean isBegin(){
	  return (isFirstColumn() && isFirstLine());
  }
  
  private boolean isFirstColumn(){
	  return columnPointer == 0;
  }
  
  private boolean isFirstLine(){
	  return linePointer == 0;
  }
  
  public void previousWord(AfterLoadWord after){
	  if (!isBegin()){
		  if (isFirstColumn()){
			  if (!isFirstLine()) toPreviousLine();	
			  this.columnPointer = wordsOfActualLine.length;
		  }
		  String word = this.wordsOfActualLine[this.columnPointer--];
		  after.exec(word);
	  }else{
		  after.exec("BEGIN_FILE");
	  }
  }
  
  private void toPreviousLine(){
	linePointer--;
	wordsOfActualLine = getWordsOfLine(lines.get(linePointer));
	columnPointer = 0;
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

  public boolean isFinish() {
    return finished;
  }
  
  public void toTheEnd(){
    this.linePointer   = this.lines.size()-1;
    this.columnPointer = this.lines.get(linePointer).split(" ").length - 1;
  }
  
  public void toTheBegin(){
    this.linePointer   = 0;
    this.columnPointer = 0;
    readFirstLineIfExists();
  }
  
  
  
  
}
