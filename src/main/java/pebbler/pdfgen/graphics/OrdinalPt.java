package pebbler.pdfgen.graphics;

import pebbler.constants.GoColor;
import pebbler.util.GoPoint;

import java.lang.StringBuilder;

class OrdinalPt implements Graphic {

  Circle circ;
  GoPoint pixLoc;

  public OrdinalPt(int x, int y, GoGrid grid) {
    pixLoc = grid.getPixelSect(x, y);
    double radius = (grid.getSpacing() >> 3) - 0.5;
    circ = new Circle(radius, pixLoc.getx(), pixLoc.gety(), GoColor.BLACK);
  }

  public StringBuilder streamOut() {
    return circ.streamOut();
  }
}



