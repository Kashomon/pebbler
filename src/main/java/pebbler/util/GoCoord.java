package pebbler.util;

public class GoCoord {

  public double x;
  public double y;

  public GoCoord(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public String toString() {
    return "(" + x + "," + y + ")";
  }

}
