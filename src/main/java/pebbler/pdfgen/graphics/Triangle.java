package pebbler.pdfgen.graphics;

import pebbler.constants.GoColor;

// TODO: Finish this class
public class Triangle implements Graphic {
  
  private int x;
  private int y;
  private GoColor color;

  public Triangle(int x, int y, GoColor color) {
    this.x = x;
    this.y = y;
    this.color = color;
  }

  // TODO: Add this
  public StringBuilder streamOut() {
    return new StringBuilder();
  }
}
