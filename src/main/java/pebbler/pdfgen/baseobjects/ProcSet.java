package pebbler.pdfgen.baseobjects;

import java.lang.StringBuilder;

/* ... */
class ProcSet extends BodyObject {

  public ProcSet() {
    super();
    name = "ProcSet";
    type = "ProcSet";
  }

  public StringBuilder display(StringBuilder currentCode, CrossRef table) {
    return currentCode
      .append(pdfShortHeader())
      .append("[ /PDF /Text ]\nendobj\n");
  }

  protected Page getPage() { return null; }

}


