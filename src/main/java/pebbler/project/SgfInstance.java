package pebbler.project;

import pebbler.parsing.Parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SgfInstance {

  public Map<String, String> metaData;
  public Move root;
  int size = 19;

  public SgfInstance(Map<String, String> metaData, Move root) {
    this.metaData = metaData;
    this.root = root;
  }

  public SgfInstance() {
    this.metaData = new HashMap<String, String>();
    this.root = new Move(0);
  }

  public Move getRoot() {
    return this.root;
  }

  /**
   * For use post-parsing
   */
  public SgfInstance findProperties() {
    Map<String, List<String>> props = this.root.getProperties();
    if (props.containsKey("SZ")) {
      size = Integer.parseInt(props.get("SZ").get(0));
    }
    return this;
  }

  public String toString() {
    return root.toBuffered("", new StringBuilder()).toString();
  }

  public static SgfInstance fromFilename(String sgfName) {
    try {
      return Parser.instance().parse(sgfName);
    } catch (IOException e) {
      throw new IllegalArgumentException("Couldn't find file: " + sgfName);
    }
  }
}
