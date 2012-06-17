package pebbler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GoFileUtil {

  public static String readFile(String filename) throws IOException {
    return readFile(new File(filename));
  }

  public static String readFile(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    StringBuilder builder = new StringBuilder();
    String line = reader.readLine();
    while (line != null) {
      builder.append(line + "\n");
      line = reader.readLine();  
    }   
    reader.close();
    return builder.toString();
  }

  public static String silentRead(File file) {
    try {
      return readFile(file);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  public static String silentRead(String filename) {
    try {
      return readFile(filename);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean isSgf(String filename) {
    return filename.endsWith(".sgf");
  }

  public static String ensureSgf(String filename) {
    if (isSgf(filename)) { 
      return filename;
    } else {
      return filename + ".sgf";
    }
  }

  public static String[] trimSegments(String[] segments) {
    for (int i = 0; i < segments.length; i++ ) {
      segments[i] = segments[i].trim(); 
    }
    return segments;
  }

  public static void writeFile(String filename, String contents) 
      throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    writer.write(contents);
    writer.close();
  }

  public static void silentWriteFile(String filename, String contents) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
      writer.write(contents);
      writer.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
