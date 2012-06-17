package pebbler.project;

import pebbler.util.GoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Move {

  private List<Move> children;
  private Map<String, List<String>> properties;
  private int moveNum;

  String color = "none"; // Should probably be enum
  private Move parent = null; // only set by the parent

  public Move(int moveNum) {
    children = new ArrayList<Move>();
    properties = new HashMap<String, List<String>>();
    this.moveNum = moveNum;
  }

  public Move(int moveNum, Move parent, Map<String, List<String>> props) {
    children = new ArrayList<Move>();
    properties = props;
    this.parent = parent;
    this.moveNum = moveNum;
  }

  public Move getParent() {
    return this.parent;
  }

  public Map<String, List<String>> getProperties() {
    return this.properties;
  }

  public boolean hasProperty(String prop) {
    return properties.containsKey(prop);
  }

  public List<String> getProperty(String prop) {
    return properties.get(prop);
  }

  public String getFirstProperty(String prop) {
    return properties.get(prop).get(0);
  }

  public List<Move> getChildren() {
    return this.children;
  }

  public Move getFirstChild() {
    return getChild(0);
  }

  public Move getChild(int branchNumber) {
    if (this.children.size() != 0) {
      return this.children.get(branchNumber);
    } else {
      return null;
    }
  }

  public int getMoveNum() { return this.moveNum; }

  public Move createChild(Map<String, List<String>> props) {
    int newMoveNum = this.moveNum + 1;
    Move m = new Move(newMoveNum, this, props);
    children.add(m);
    return m;
  }

  public void addChild(Move m) {
    children.add(m);
    m.parent = this;
  }


  public Move addProperty(String token, List<String> data) {
    properties.put(token, data);
    return this;
  }

  public Move addProperties(Map<String, List<String>> props) {
    properties.putAll(props);
    return this;
  }

  /*
   * Displayers
   */
  public String showProperties() {
    Set<String> keySet = properties.keySet();
    StringBuilder out = new StringBuilder();
    for (String s: keySet) {
      out.append(s);
      out.append("->");
      List<String> props = properties.get(s);
      for (String p: props) {
        out.append(p);
        out.append(",");
      }
      out.deleteCharAt(out.length() - 1);
      out.append("\n");
    }

    return out.toString();
  }

  /*
   * Important for display
   */
  public static GoPoint coordToInt(String coord) {
    String lcoord = coord.toLowerCase();
    if (coord.length() != 2) {
      // Error
    }
    int a = 'a';
    int x = lcoord.charAt(0) - a + 1;
    int y = 19 - (lcoord.charAt(1) - a);
    return new GoPoint( x, y );
  }

  public static void displayCoord(int[] coord) {
    System.out.println("(" + coord[0] + "," + coord[1] + ")");
  }

  public StringBuilder toBuffered(String tab, StringBuilder builder) {
    builder.append(tab).append("Mv: ").append(moveNum).append("\n");

    for (String key: properties.keySet()) {
      builder.append(key).append(": ").append(properties.get(key)).append("\n");
    }

    for (int i = 0; i < children.size(); i++) {
      String newTab = addTabs(i, tab);
      children.get(i).toBuffered(newTab, builder);
    }

    return builder;
  }

  public static String addTabs(int num, String cur) {
    String newTabs = "";
    for (int i = 0; i < num; i++) {
      newTabs += "  ";
    }
    return newTabs;
  }
}

