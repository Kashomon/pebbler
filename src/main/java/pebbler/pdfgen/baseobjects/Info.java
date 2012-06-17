package pebbler.pdfgen.baseobjects;

import java.lang.StringBuilder;

/** Info contains information about the document's creation. */
class Info extends BodyObject {

  String creator;
  String producer;

  public Info() {
    super();
    name = "Info";
    producer = "Pebbler";
    creator  = "Josh Hoak";
  }

  public Info(String producer, String creator) {
    super();
    name = "Info";
    this.producer = producer;
    this.creator  = creator;
  }

  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    return currentCode
      .append(pdfHeader())
      .append("/Producer (").append(this.producer).append(")\n")
      .append("/Creator (").append(this.creator).append(")\n")
      .append(pdfFooter());
  }

  protected Page getPage() { return null; }
}


