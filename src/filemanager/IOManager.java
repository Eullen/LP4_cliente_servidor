package filemanager;

import java.util.List;
import java.io.File;
import java.io.IOException;

public interface IOManager {
  public void open(String src) throws IOException;

  public void open(File file) throws IOException;

  public void write(String s) throws IOException;

  public List<String> read() throws IOException;

  public void close() throws IOException;
}
