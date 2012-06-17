package pebbler.constants;

import pebbler.constants.FontInfo;
import pebbler.util.GoCoord;

public enum FontInfo {
  HELVETICA,
  TIMES_NEW_ROMAN;

  //0     1     2     3     4     5     6     7     8     9 
  public static int[] helveticaWidths = {                       
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    //  0-9
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    //  10-19
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    //  20-29
    0,    0,    278,  0,    0,    0,    0,    0,    0,    0,    //  30-39
    0,    0,    0,    0,    0,    0,    0,    0,    556,  556,  //  40-49 (nums)
    556,  556,  556,  556,  556,  556,  556,  556,  0,    0,    //  50    (nums)
    0,    0,    0,    0,    0,    667,  667,  722,  722,  667,  //  60    (capitals)
    611,  778,  722,  278,  500,  667,  556,  833,  722,  778,  //  70
    667,  778,  722,  667,  611,  722,  667,  944,  667,  667,  //  80
    611,  0,    0,    0,    0,    0,    0,    556,  556,  500,  //  90    (lower case)
    566,  566,  278,  556,  556,  222,  222,  500,  222,  833,  //  100
    566,  566,  566,  566,  333,  500,  278,  556,  500,  722,  //  110
    500,  500,  500,  0,    0,    0,    0,    0,    0,    0,    //  120
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0     //  130
  };

  public static int helveticaHeight = 523;

  public static FontInfo getFont(String font) {
    String lowerFont = font.toLowerCase();
    if (lowerFont == "helvetica") {
      return HELVETICA;
    } else if (lowerFont == "times new roman" ||
               lowerFont == "times_new_roman" ||
               lowerFont == "timesnewroman") {
      return TIMES_NEW_ROMAN;
    }
    // uh what?
    throw new AssertionError("Unknown font: " + font);
  }

  public String getId() {
    switch(this) {
      case HELVETICA:         return "F12";
      case TIMES_NEW_ROMAN:   return "F13";
    }
    throw new AssertionError("Unknown font: " + this.toString());
  }

  public int[] getWidths() {
    switch(this) {
      case HELVETICA:     return helveticaWidths;
    }
    throw new AssertionError("Unknown font: " + this.toString());
  }

  public int getHeight() {
    switch(this) {
      case HELVETICA:     return helveticaHeight;
    }
    throw new AssertionError("Unknown font: " + this.toString());
  }

  /**
   *  Returns the amt to move the text left given some text and a ptSize.
   */ 
  public GoCoord center(String text, int ptSize) {
    int[] widths = this.getWidths();
    int height   = this.getHeight();

    long totalLength = 0;
    for (int i = 0; i < text.length(); i++) {
      totalLength = totalLength + widths[text.charAt(i)];
    }

    // Positive values move things left
    double shiftLength = (totalLength * ptSize) / (1000. * 2);

    // Positive values move things down.
    double shiftHeight = (height * ptSize) / (1000. * 2);

    // A dirty hack -- it doesn't look right to me without this.
    double fudgefactor = 0.5;
    return new GoCoord(
        shiftLength,
        shiftHeight + fudgefactor); // fudge down
  }

}
