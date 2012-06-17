package pebbler.pdfgen.baseobjects;

/** Trailer is the first object that is created and is called directly from the
 * document class. The trailer is a little unusual since it is not a true
 * indirect object with an ID.  However, we can treat it as such for the
 * purpose of our PDF AST.
 *
 * Links to: Root, Info; 
 */

class Trailer extends BodyObject {

  /* The size is not set until compile time.  Size corresponds to the number of
   * indirect objects (+ the one free object) in the PDF. */
  int size;
    
  public Trailer() {
    super();
    this.linkedObjects.add(new Root());
    this.linkedObjects.add(new Info());
    this.name = "trailer";
  }

  /** Output the PDF code to be displayed.  We assume that all the child
   * objects have already done their compiling/displaing. 
   */
  StringBuilder display(StringBuilder currentCode, CrossRef table) {

    int xrefloc = currentCode.length() + 1;

    // Render the XRefTable
    currentCode.append(table.compile());

    // Render the Trailer 
    currentCode.append("trailer\n<<\n");
    for (int i = 0; i < linkedObjects.size(); i++) {
      currentCode.append(linkedObjects.get(i).pdfNameRef());
    }

    return currentCode
      .append("/Size ").append(table.numObjects() + 1).append("\n>>\n")
      .append("startxref\n").append(xrefloc).append("\n");
  }

  /** Get a page so we can add a graphic to it.  Note that this method is
   * called by Document. */
  protected Page getPage() {
    return linkedObjects.get(0).getPage();
  }
}


