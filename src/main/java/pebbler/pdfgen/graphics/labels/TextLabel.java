package pebbler.pdfgen.graphics.labels;

import pebbler.constants.FontInfo;
import pebbler.constants.GoColor;
import pebbler.pdfgen.graphics.GoGrid;
import pebbler.pdfgen.graphics.TextImage;
import pebbler.util.GoPoint;

public class TextLabel implements GoLabel {

  int ptSize = 12;
  FontInfo font;
  String text;
  TextImage textImage;
  GoPoint coords;
  int x_sect;
  int y_sect;

  public TextLabel(String text, FontInfo font, int x_sect, int y_sect, GoGrid grid) {
    this.coords = grid.getPixelSect(x_sect, y_sect);
    this.font = font;
    this.text = text;
    this.textImage = (new TextImage(text, font, grid.getLabelColor(x_sect, y_sect), coords.getx(), coords.gety())).center(); 
    this.x_sect = x_sect;
    this.y_sect = y_sect;
  }

  public TextLabel(String text, FontInfo font, int x_sect, int y_sect, GoGrid grid, GoColor color) {
    this.coords = grid.getPixelSect(x_sect, y_sect);
    this.font = font;
    this.text = text;
    this.textImage = new TextImage(text, font, color, coords.getx(), coords.gety());
    textImage.center(); 
    this.x_sect = x_sect;
    this.y_sect = y_sect;
  }

  public StringBuilder streamOut() {
    return textImage.streamOut();
  }

  public int xSect() {
    return x_sect;
  }

  public int ySect() {
    return y_sect;
  }

  public String toString() {
    return text + " " + font + " " + Integer.toString(ptSize);
  }
}
