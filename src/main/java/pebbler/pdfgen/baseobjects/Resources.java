package pebbler.pdfgen.baseobjects;

import java.util.HashMap;
import java.util.Map;

/** Resources gives information about the fonts and patterns that are used */
class Resources extends BodyObject {
  int numFonts;
  int numPatterns;
  Map<String, Integer> fids = new HashMap<String, Integer>();

  public Resources() {
    super();
    name = "Resources";

    /* This would be a great place to use a hash map */
    linkedObjects.add(new ProcSet());
    linkedObjects.add(new Font("Times-Roman", "Type1", "F13"));
    fids.put("Times-Roman", new Integer(13));
    linkedObjects.add(new Font("Helvetica", "Type1", "F12"));
    fids.put("Helvetica", new Integer(12));
  }

  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    currentCode
      .append(pdfHeader())
      .append("/Font <<\n");

    /* Get the Fonts */
    for (int i = 0; i < linkedObjects.size(); i++) {
      BodyObject curobj = linkedObjects.get(i);
      if (curobj.getType() == "Font") {
        currentCode
          .append("/F").append(fids.get(curobj.getName()).intValue())
          .append(" ").append(curobj.getReference()).append("\n");
      }
    }
    currentCode.append(">>\n");

    /* Get the ProcHeader */
    for (int i = 0; i < linkedObjects.size(); i++) {
      if (linkedObjects.get(i).getType() == "ProcSet") {
        currentCode.append(linkedObjects.get(i).pdfNameRef());
      }
    }
    return currentCode.append(pdfFooter());
  }

  protected Page getPage() { return null; }
}

