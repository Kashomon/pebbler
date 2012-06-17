package pebbler.pdfgen.graphics;

import pebbler.constants.FontInfo;
import pebbler.constants.GoColor;
import pebbler.util.GoCoord;

import java.lang.StringBuilder;

public class TextImage implements Graphic {

  int ptSize = 11;
  String text;
  FontInfo font;
  GoColor color;
  double x_pos;
  double y_pos;

  public TextImage(String text, FontInfo font, GoColor color, int x_pos, int y_pos) {
    this.text = text;
    this.font = font;
    this.color = color;
    this.x_pos = (double) x_pos;
    this.y_pos = (double) y_pos;
  }

  public TextImage center() {
    GoCoord coord = font.center(text, ptSize);
    x_pos = x_pos - coord.x;
    y_pos = y_pos - coord.y;
    return this;
  } 

  public TextImage setPtSize(int ptSize) {
    this.ptSize = ptSize;
    return this;
  }

  public StringBuilder streamOut() {
    StringBuilder outString = new StringBuilder();
    if (color == GoColor.BLACK) {
      outString.append("0 0 0 rg\n");
    } else {
      outString.append("1.000 1.000 1.000 rg\n");
    }
    outString.append("BT\n/"); 
    outString.append(font.getId()).append(" ").append(ptSize).append(" Tf\n"); 
    outString.append(x_pos).append(" ").append(y_pos).append(" Td \n");
    outString.append("(").append(text).append(")").append("Tj\n");
    outString.append("ET\nf\nQ\n");
    return outString;
  }


}
