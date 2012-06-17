package pebbler.pdfgen.baseobjects;

/* Not currently using ContentStream */
class ContentStream extends BodyObject {

  public ContentStream() {
    super();
    name = "ContentStream";
  }

  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    return currentCode;
  }

  protected Page getPage() { return null; }

}

