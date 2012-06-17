package pebbler.pdfgen.graphics;

import pebbler.constants.FontInfo;
import pebbler.constants.GoColor;
import pebbler.pdfgen.graphics.labels.GoLabel;
import pebbler.pdfgen.graphics.labels.TextLabel;
import pebbler.util.GoPoint;

import java.util.ArrayList;
import java.util.List;

public class GoGrid implements Graphic {

  int numLines = 19;
  int spacing = 20;

  // Only used for drawing 
  Line[] horzLines; // Horizontal lines of the Go Board
  Line[] vertLines; // Vertical lines of the Go Board
  // Due to symmetry, don't need an n x n matrix.
  int[] xSects; 
  int[] ySects;

  GoBoxWrapper box;

  // Eventually, there should be an abstraction for the intersections that has
  // an O(1) access time to get the data from any intersection

  List<GoStone> goStones = new ArrayList<GoStone>();
  List<OrdinalPt> ordPts;
  List<GoLabel> labels = new ArrayList<GoLabel>();
  List<Triangle> triangles = new ArrayList<Triangle>();

  /** Create a a Go Grid with numLines horzontal Lines and numLines vertical
   * Lines. Assume that numLines is odd 
   *
   * Some notes about numbering:
   *  Normally, we number the go board starting in the bottom left corner.
   *  i.e.,
   *
   *  (A, 19)
   *    .
   *    .
   *    .
   *  
   *  (A, 1)   ...   (T, 1)  (skipping I)
   *
   *  However, in an SGF, Go boards are numbered from the top left.
   *  I.e.,
   *
   *  (a, a)  ... (a, a)
   *    .
   *    .
   *    .
   *  (a, s)
   */
  public GoGrid(int center_x, int center_y, int spacing, int numLines) {
    if (numLines % 2 == 0) {
      // TODO: throw an exception.
      System.out.println("Error! Even number of lines.");
      return;
    } 

    this.spacing = spacing;
    this.numLines = numLines;
    this.box = new GoBoxWrapper(center_x, center_y);
    createGoLines().findIntersects();
  }

  /** A constructor that assumes a 19x19 board */
  public GoGrid(int center_x, int center_y) {
    this.box = new GoBoxWrapper(center_x, center_y);
    createGoLines().findIntersects().findOrdinals();
  }

  public GoGrid() {
    this.box = new GoBoxWrapper(200, 200);
    createGoLines().findIntersects().findOrdinals();
  }

  private GoGrid createGoLines() {
    horzLines = new Line[numLines];
    vertLines = new Line[numLines];
    for (int i = 0; i < 19; i++) {
      horzLines[i] = new Line(box.left, box.bot + spacing * i, box.right, box.bot + spacing * i);
      vertLines[i] = new Line(box.left + spacing * i, box.bot, box.left + spacing * i, box.top);
    }

    return this;
  }

  /** Called by the constructor and used to find Intersects takes the
   * coordinates for the bottome left and then uses the spacing to calculate
   * all the intersections (numLines^2). */
  private GoGrid findIntersects() {
    xSects = new int[numLines];
    ySects = new int[numLines];
    for (int i = 0; i < numLines; i++) {
      xSects[i] = box.left + spacing * i;
      ySects[i] = box.bot + spacing * i;
    }  
    return this;
  }

  private GoGrid findOrdinals() {
    ordPts = new ArrayList<OrdinalPt>();
    ordPts.add(new OrdinalPt((numLines >> 1) + 1, (numLines >> 1) + 1, this));
    if (numLines == 19) {
      ordPts.add(new OrdinalPt(4, 4, this));
      ordPts.add(new OrdinalPt(4, 10, this));
      ordPts.add(new OrdinalPt(4, 16, this));
      ordPts.add(new OrdinalPt(10, 4, this));
      ordPts.add(new OrdinalPt(10, 16, this));
      ordPts.add(new OrdinalPt(16, 4, this));
      ordPts.add(new OrdinalPt(16, 10, this));
      ordPts.add(new OrdinalPt(16, 16, this));
    }
    return this;
  }


  /** Get the pixel coordinate of the (a, b) intersect -- e.g., (4,4) */
  public GoPoint getPixelSect(int a, int b) {
    if (a > 0 && b > 0 && a <= numLines && b <= numLines) {
    /* Need to do some error checking here */
      return new GoPoint(xSects[a-1], ySects[b-1]);
    } else {
      System.out.println("Error! Intersection outside bounds.");
      return null;
    }
  } 

  /** 
   * Return the number of lines that intersect a side 
   * */
  public int getNumLines() { return numLines; }

  /** 
   * Return the spacing between the lines 
   * */
  public int getSpacing() { return spacing; }


  /**
   * Returns the optimal label-color;
   */
  public GoColor getLabelColor(int x_sect, int y_sect) {
    GoStone toFind = null;
    for (GoStone stone: goStones) {
      if (stone.x_sect == x_sect && stone.y_sect == y_sect) {
        toFind = stone;
      }
    }
    if (toFind == null) {
      return GoColor.BLACK;
    } else {
      return toFind.color.opposite();
    }
  }

  /**
   * Adding methods: Ultimately these will be the only public methods
   */
  public GoGrid addStone(int x, int y, GoColor color) {
    goStones.add(new GoStone(color, x, y, this));
    return this;
  }

  public GoGrid addOrdinal(int x, int y) {
    ordPts.add(new OrdinalPt(x, y, this));
    return this;
  }

  public GoGrid addTextLabel(String text, int x, int y) {
    labels.add(new TextLabel(text, FontInfo.HELVETICA, x, y, this));
    return this;
  }

  public GoGrid addTextLabel(String text, int x, int y, GoColor color) {
    labels.add(new TextLabel(text, FontInfo.HELVETICA, x, y, this, color));
    return this;
  }

  public GoGrid addTriangle(int x, int y, GoColor color) {
    triangles.add(new Triangle(x, y, color));
    return this;
  }

  public GoGrid addTextLabel(
      String text, 
      FontInfo font, 
      int x, 
      int y, 
      GoColor color) {
    GoLabel lbl = new TextLabel(text, font, x, y, this, color);
    labels.add(lbl);
    return this;
  }

  /** Stream Out produces the PDF code that will eventually be put in the
   * stream of a contents object in the PDF */
  public StringBuilder streamOut() {
    StringBuilder buffer = new StringBuilder();

    for (int i = 0; i < numLines; i++) {
      if (i == 0 || i == numLines - 1) {
        buffer.append("2 w\n"); // Make the edge lines Bold
      } else if (i == 1)  {
        buffer.append("0.5 w\n");
      }
      buffer.append(horzLines[i].streamOut());
      buffer.append(vertLines[i].streamOut());
    }

    for(OrdinalPt opt: ordPts) {
      buffer.append(opt.streamOut());
    }

    for (GoStone stone: goStones) {
      buffer.append(stone.streamOut());
    } 

    for (GoLabel label: labels) {
      buffer.append(label.streamOut());
    }

    for (Triangle triangle : triangles) {
      buffer.append(triangle.streamOut());
    }

    return buffer;
  }
}
