package pebbler.pdfgen.baseobjects;

import pebbler.constants.Cropping;

import java.lang.StringBuilder;

/** Page is a specific instantiation of pages */
class Page extends BodyObject {

  /* MediaBox creates the page dimensions of the PDF */
  int[] mediaBox;

  /* A page will always contain a pages parent; */
  Pages parent;
  Contents con;
  Resources res;

  public Page(Pages parent) {
    super();
    this.parent = parent;
    name = "Page";
    type = "Page";
    setMediaBox(Cropping.Full);
    con = new Contents();
    res = new Resources();
    linkedObjects.add(con);
    linkedObjects.add(res);
  }

  public void setMediaBox(Cropping crop) {
    mediaBox = crop.getCropbox();
  }

  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    currentCode.append(pdfHeader()).append(pdfType());
    for (int i = 0; i < linkedObjects.size(); i++ ) {
      currentCode.append(linkedObjects.get(i).pdfNameRef());
    }
    return currentCode
      .append(pdfMediaBox())
      .append("/Parent ").append(parent.getReference()).append("\n")
      .append(pdfFooter());
  }

  /** PDF Media Box is useful for reducin the code in display.  It is just used
   * to display pdf code for the mediaBox (i.e. the page dimensions) for the
   * Page object */
  String pdfMediaBox() {
    String mediaout = "/MediaBox [";
    for (int i = 0; i < 4; i++) {
      mediaout += mediaBox[i];
      if (i < 3) {
        mediaout += " ";
      }
    }
    mediaout += "]\n";
    return mediaout;
  }

  protected void addGraphicStream(String pdfStream) {
    /* Note that we are assuming that the 0th object is the contents.  There
     * should be a more robust way to deal with this and also some error
     * checking. */
    con.addStream(pdfStream);
  }

  /**
   *  Doesn't make much sense to get a page when you're already a page... 
   */ 
  protected Page getPage() { return null; }
}

