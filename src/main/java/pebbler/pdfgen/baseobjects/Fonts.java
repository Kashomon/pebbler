package pebbler.pdfgen.baseobjects;

/** Font class! */
class Font extends BodyObject {
  String fontName;
  String fontType;
  String fontID;

  public Font(String fontName, String fontType, String fontID) {
    super();
    name = fontName;
    type = "Font";
    this.fontName = fontName;
    this.fontType = fontType;
    this.fontID = fontID;
  }

  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    return currentCode
      .append(pdfHeader())
      .append(pdfType())
      .append("/Subtype /").append(fontType).append("\n")
      .append("/BaseFont /").append(fontName).append("\n")
    //.append("/Enconding /MacRomanEncoding\n");
      .append(pdfFooter());
  }

  Page getPage() { return null; }

}

