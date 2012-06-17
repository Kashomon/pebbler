package pebbler.pdfgen.graphics;

import pebbler.constants.GoColor;

public class Circle implements Graphic {

  double bmod;
  int center_x;
  int center_y;
  double radius;
  GoColor fillColor;

  public Circle(double radius, int center_x, int center_y, GoColor color) {
    this.radius = radius;
    this.center_x = center_x;
    this.center_y = center_y;
    bmod = 0.5522847 * radius;
    fillColor = color;
  }

  public StringBuilder streamOut() {
    double left = center_x - radius;
    double right = center_x + radius;
    double top = center_y + radius;
    double bot = center_y - radius;

    /* The way to approximate a circle in PDF code is to use bezier curves */
    StringBuilder circOut = new StringBuilder();
    circOut.append(".5 w\n");

    if (fillColor.equals(GoColor.WHITE)) {
      circOut.append("1.000 1.000 1.000 rg\n");
    } else {
      circOut.append("0.000 0.000 0.000 rg\n");
    }   

    circOut.append(center_x)
        .append(" ")
        .append(bot)
        .append(" m\n");

    /* 1st 1/4 of the circle */
    circOut.append(center_x + bmod)
        .append(" ")
        .append(bot)
        .append(" ")
        .append(right)
        .append(" ")
        .append(center_y - bmod)
        .append(" ")
        .append(right)
        .append(" ")
        .append(center_y)
        .append(" c\n");
    /* stoneOut = stoneOut + (center_x + BMOD) + " " + bot + " "
                        + right + " " + (center_y - BMOD) + " "
                        + right + " " + center_y + " c\n"; */

    /* 2nd 1/4 of the circle */
    circOut.append(right)
        .append(" ")
        .append(center_y + bmod)
        .append(" ")
        .append(center_x + bmod)
        .append(" ")
        .append(top)
        .append(" ")
        .append(center_x)
        .append(" ")
        .append(top)
        .append(" c\n");

    /* stoneOut = stoneOut + right + " " + (center_y + BMOD) + " "
                        + (center_x + BMOD) + " " +  top  + " "
                        + center_x + " " + top + " c\n"; */

    /* 3rd 1/4 of the circle */
    circOut.append(center_x - bmod)
        .append(" ")
        .append(top)
        .append(" ")
        .append(left)
        .append(" ")
        .append(center_y + bmod)
        .append(" ")
        .append(left)
        .append(" ")
        .append(center_y)
        .append(" c\n");
    /* stoneOut = stoneOut + (center_x - BMOD) + " " + top + " "
                        + left + " " + (center_y + BMOD) + " "
                        + left + " " + center_y + " c\n"; */

    /* 4th 1/4 of the circle */
    circOut.append(left)
        .append(" ")
        .append(center_y - bmod)
        .append(" ")
        .append(center_x - bmod)
        .append(" ")
        .append(bot)
        .append(" ")
        .append(center_x)
        .append(" ")
        .append(bot)
        .append(" c\n");

    /* stoneOut = stoneOut + left + " " + (center_y - BMOD) + " "
                        + (center_x - BMOD) + " " + bot  + " "
                        + center_x + " " + bot + " c\n"; */
    circOut.append("B\n");
    return circOut;
  }


}
