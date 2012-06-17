package pebbler.pdfgen.graphics;

import pebbler.constants.*;
import pebbler.util.*;

import java.lang.StringBuilder;

public class GoStone implements Graphic {

  public GoColor color;
  Circle circ;
  GoPoint pixLoc;
  public int x_sect;
  public int y_sect;

  public GoStone(GoColor color, int x, int y, GoGrid grid) {
    pixLoc = grid.getPixelSect(x, y);
    x_sect = x;
    y_sect = y; 
    double radius = (grid.getSpacing() >> 1);
    circ = new Circle(radius, pixLoc.getx(), pixLoc.gety(), color);
  }

  public StringBuilder streamOut() {
    return circ.streamOut();
  }
}
