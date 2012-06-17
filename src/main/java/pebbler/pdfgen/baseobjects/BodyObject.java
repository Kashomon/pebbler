package pebbler.pdfgen.baseobjects;

import java.util.LinkedList;
import java.util.List;

/**
 *  Body Object is the base object from which all the standard PDF objects extend 
 */
abstract class BodyObject {

  String type;
  List<BodyObject> linkedObjects;

  /* The name field is useful for debugging (used in showStructure). */
  String name;

  /* these below can't be determined until the structure of the pdf document is
  * determined  */
  int objectID;
  int offset;

  /** 
   * This constructor is useful for initializing some fields for each object
   */
  public BodyObject() {
    linkedObjects = new LinkedList<BodyObject>();
    objectID = -1;
    type = "";
  }

  /* ------- Public Methods for all BodyObjects ------- */
  public int getID() { return objectID; }

  public String getReference() { return objectID + " 0 R"; }

  public String getType() { return type; }

  public String getName() { return name; }

  /* For compact code generation */
  public String pdfShortHeader() { return objectID + " 0 obj\n"; }

  public String pdfHeader() { return objectID + " 0 obj\n<<\n"; }

  public String pdfFooter() { return ">>\nendobj\n"; }

  public String pdfType() { return "/Type /" + type + "\n"; }

  public String pdfNameRef() { return "/" + name + " " + getReference() + "\n"; }

  /** Compile generates the final PDF code as String output.
   * Note that the PDF code generated will be dependent on the particular type of
   * body object, which is instantiated in display. 
   */
  public StringBuilder compile(StringBuilder currentCode, CrossRef table) {
    StringBuilder modCode = currentCode;
    for (int i = 0; i < linkedObjects.size(); i++) {
      modCode = linkedObjects.get(i).compile(modCode, table);
    }
    if (!(name.equals("trailer"))) {
      table.addEntry(modCode.length() + 1); 
    }
    return display(modCode, table);
  }

  /** Every object (except the trailer) needs an ID, and so findID finds the
   * ID (often for the purpsoe of setting the ID of the parent) by traversing
   * the PDF structure and returning the ID from the child object. */
  int findID(int inID) {
    int passedID = inID;
    for (int i = 0; i < linkedObjects.size(); i++) {
      passedID = linkedObjects.get(i).findID(passedID);
    }  
    objectID = passedID + 1;
    return objectID;
  } 
  
  /** Show Structure is useful for showing the outlined structure of the PDF,
   * which can be a useful debugging tool */
  public void showStructure(int tabs) {
    String spacing = "";
    for (int t = 0; t < tabs; t++) {
      spacing = spacing + "  ";
    } 
    System.out.print(spacing + name + ": ID-" + objectID + "\n");
    
    /* Traverse the linked objects and print the result */
    for (int i = 0; i < linkedObjects.size(); i++) {
      BodyObject x = linkedObjects.get(i);
      x.showStructure(tabs + 1);
    } 
  } 
  
  /* ------ Abstract Methods ------ */
  /** To be instantiated by each object implementation below and which will
   * produce PDF-code for according to the needs of the particular object */
  abstract StringBuilder display(StringBuilder currentCode, CrossRef table);
  abstract Page getPage();
  
}

