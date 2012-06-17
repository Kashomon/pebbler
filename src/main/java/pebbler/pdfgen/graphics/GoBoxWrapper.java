package pebbler.pdfgen.graphics;

/**
 *  BoxWrapper is a data type to wrap all the usefule constants that will be
 *  use for constructing and drawing the GoGrida
 */
public class GoBoxWrapper {

  private int spacing = 20;
  private int numLines = 19;
  public int center_x;
  public int center_y;
  public int top;
  public int bot;
  public int left;
  public int right;

  public GoBoxWrapper(int center_x, int center_y, int spacing, int numLines) {
    this.spacing = spacing;
    this.numLines = numLines;
    createBoxWrapper(center_x, center_y);
  }

  public GoBoxWrapper(int center_x, int center_y) {
    createBoxWrapper(center_x, center_y);
  }

  public GoBoxWrapper createBoxWrapper(int center_x, int center_y) {
    this.center_x = center_x;
    this.center_y = center_y;
    int halfnum = (numLines - 1) >> 1;
    top    = center_y + spacing * halfnum;
    bot    = center_y - spacing * halfnum;
    left   = center_x - spacing * halfnum; 
    right  = center_x + spacing * halfnum;
    return this;
  }

}


