package filemanager;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

public class FileManager implements IOManager {
  private File file;
  private FileReader fr;
  private BufferedReader br;

  public void open(String src) throws IOException {
    this.file = new File(src);
    this.fr = new FileReader(this.file);
    this.br = new BufferedReader(this.fr);
  }

  public void open(File file) throws IOException {
    this.file = file;
    this.fr = new FileReader(this.file);
    this.br = new BufferedReader(this.fr);
  }

  public List<String> read() throws IOException {
    List<String> lines = new ArrayList<String>();
    String line = br.readLine();
    while (line != null) {
      lines.add(line);
      line = br.readLine();
    }
    return lines;
  }

  public void close() throws IOException {
    this.br.close();
    this.fr.close();
  }

  @Override
  public void write(String s) throws IOException {
    // TODO Auto-generated method stub

  }
}
