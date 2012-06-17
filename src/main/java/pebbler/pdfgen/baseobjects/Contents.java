package pebbler.pdfgen.baseobjects;

import java.lang.StringBuilder;

/** Usually contains a bytestream of information */
class Contents extends BodyObject {

  /* A sequence of characters (bytes) that encode text/images */
  String stream;

  /* Length, in characters (bytes) of the stream */
  int length;

  public Contents() {
    super();
    name = "Contents";
    stream = "";
  }

  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    return currentCode.append(pdfHeader()).append(displayStream()).append("\nendobj\n");
  }

  /** Create the PDF-code to be displayed in the PDF document.  Called by
   * Display. */
  private StringBuilder displayStream() {
    return (new StringBuilder())
      .append("/Length ").append(stream.length())
      .append("\n>>\nstream\n")
      .append(stream)
      .append("endstream");
  }

  protected void addStream(String stream) {
    this.stream += stream;
  }

  protected Page getPage() { return null; }

}

