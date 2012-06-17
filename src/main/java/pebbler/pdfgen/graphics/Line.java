package pebbler.pdfgen.graphics;

public class Line implements Graphic {
  int x1, y1;
  int x2, y2;

  public Line(int x1, int y1, int x2, int y2) {
    this.x1 = x1;
    this.x2 = x2;
    this.y1 = y1;
    this.y2 = y2;
  }

  public StringBuilder streamOut () {
    StringBuilder stream = new StringBuilder();
    stream.append(x1).append(" ").append(y1).append(" m\n");
    stream.append(x2).append(" ").append(y2).append(" l\nB\n");
    return stream;
  } 


}
