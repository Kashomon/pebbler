package pebbler.pdfgen.baseobjects;

import pebbler.constants.Cropping;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * The Document is the root-node for the entire PDF Abstract Syntax Tree.
 *
 *  In general, we have the following structure
 *
 *  Document - Trailer     -    Root - Pages - Page
 *           - XRefTable        Info -       - Page
 *                                           - Page
 *                                             ...
 *
 *  Page - Contents - Streams
 *       - MediaBox
 *       - Resources - Fonts
 */
public class Document {

  String header;
  String filename;
  Trailer trailer;
  CrossRef table;

  /**
   * Constructs a basic empty document
   */
  public Document() {
    header = "%PDF-1.3\n";
    this.filename = filename;
    /* Initialize the two root-parts of a PDF */
    trailer = new Trailer();
    table = new CrossRef();
  }

  /**
   * Shows the basic structure of the PDF.  Primarily for debugging.
   * */
  public void showStructure() {
    System.out.print("Document\n");
    trailer.findID(0);
    trailer.showStructure(1);
  }

  public void setCropping(Cropping crop) {
    getPage().setMediaBox(crop);
  }

  /**
   * Compile the document into a string.
   */
  public StringBuilder compile() {
    trailer.findID(0); // Traverse the tree first to compute the IDs.
    return trailer.compile(new StringBuilder(header), table).append("%%EOF");
  }

  /**
   * Write the compiled string to a file named out.pdf
   */
  public void write(String filename) {
    String code = compile().toString();
    try {
      FileOutputStream out = new FileOutputStream(filename);
      new PrintStream(out).print(code.toString()) ;
    } catch (IOException e) {
      System.err.println("Unable to write to file");
      System.exit(-1);
    }
  }

  /** Need to have a page grabbing feature so that we can create graphics */
  private Page getPage() {
    return trailer.getPage();
  }

  /** Add a graphic stream already in PDF code to a free page */
  public void addStream(StringBuilder stream) {
    this.getPage().addGraphicStream(stream.toString());
  }

}
