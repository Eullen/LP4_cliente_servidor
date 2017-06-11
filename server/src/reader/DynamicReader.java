package reader;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
		this.qtyWords = this.totalOfWords();
		this.linePointer = 0;
		this.columnPointer = 0;
		this.readFirstLineIfExists();
	}

	private void readFirstLineIfExists() {
		if (this.lines.size() > 0) {
			this.wordsOfActualLine = this.getWordsOfLine(this.lines.get(0));
		}
	}

	public List<String> getList() {
		return this.lines;
	}

	private boolean isLastLine() {
		return this.linePointer == this.lines.size();
	}

	private boolean isLastColumn(String line[]) {
		return this.columnPointer == line.length;
	}

	private int totalOfWords() {
		int count = 0;
		for (String line : this.lines) {
			count += this.getWordsOfLine(line).length;
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
		return this.qtyWords;
	}

	public void nextWord(AfterLoadWord after) {
		if (!this.isFinish()) {
			if (!this.isLastColumn(this.wordsOfActualLine)) {
				String word = this.wordsOfActualLine[this.columnPointer++];
				after.exec(word);
			} else {
				after.exec(new String());
				this.toNextLine();
			}
		} else {
			after.exec("END_FILE");
		}
	}

	private boolean isBegin() {
		return (this.isFirstColumn() && this.isFirstLine());
	}

	private boolean isFirstColumn() {
		return this.columnPointer == 0;
	}

	private boolean isFirstLine() {
		return this.linePointer == 0;
	}

	public void previousWord(AfterLoadWord after) {
		if (!this.isBegin()) {
			if (this.isFirstColumn()) {
				if (!this.isFirstLine()) {
					this.toPreviousLine();
				}
				this.columnPointer = this.wordsOfActualLine.length;
			}
			String word = this.wordsOfActualLine[this.columnPointer--];
			after.exec(word);
		} else {
			after.exec("BEGIN_FILE");
		}
	}

	private void toPreviousLine() {
		this.linePointer--;
		this.wordsOfActualLine = this.getWordsOfLine(this.lines.get(this.linePointer));
		this.columnPointer = 0;
	}

	private void toNextLine() {
		this.linePointer++;
		this.columnPointer = 0;
		if (!this.isLastLine()) {
			this.wordsOfActualLine = this.getWordsOfLine(this.lines.get(this.linePointer));
		} else {
			this.finished = true;
		}
	}

	public void reset() {
		this.columnPointer = 0;
		this.linePointer = 0;
		this.finished = false;
		this.readFirstLineIfExists();
	}

	public boolean isFinish() {
		return this.finished;
	}

	public void toTheEnd() {
		this.linePointer = this.lines.size() - 1;
		this.columnPointer = this.lines.get(this.linePointer).split(" ").length - 1;
	}

	public void toTheBegin() {
		this.linePointer = 0;
		this.columnPointer = 0;
		this.readFirstLineIfExists();
	}

}
