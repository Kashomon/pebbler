package pebbler.pdfgen.baseobjects;

import java.util.LinkedList;
import java.util.List;

/** Cross Reference Table contains random access information (byte offset)
 * about each object (see BaseObjects). They must be ordered by objectID number.
 */
public class CrossRef { 

  List<String> entries;

  public CrossRef() {
    entries = new LinkedList<String>();
    entries.add("0000000000 65535 f\n");
  }

  public int numObjects() {
    return entries.size() - 1;
  }

  /** Given a character offset, construct a new entry for the table and add it
   * to the entries linked list */
  public void addEntry(int offset) {

    // Should be using a string buffer here..
    char[] entry = new char[10 + 1 + 5 + 1 + 2];
    String offstr = String.valueOf(offset);

    int index = 0;
    for (int i = 0; i < 10 - offstr.length(); i++) {
      entry[i] = '0';
      index++;
    }
    int oldIndex = index;
    for (int i = 0; i < offstr.length(); i++) {
      entry[oldIndex + i] = offstr.charAt(i);
      index++;
    }
    entry[index] = ' ';
    index++;
    for (int i = 0; i < 5; i++ ) {
      entry[index] = '0';
      index++;
    }
    entry[index] = ' ';
    entry[index + 1] = 'n';
    entry[index + 2] = '\n';
    entries.add(new String(entry));
  }

  /** Return the PDF version of the Cross Reference table (inserted before the
   * trailer) */
  public String compile() {
    String xrefout = "xref\n0 " + entries.size() + "\n";
    for (int i = 0; i < entries.size(); i++) {
      xrefout = xrefout + entries.get(i);  
    }
    return xrefout;
  }

}
