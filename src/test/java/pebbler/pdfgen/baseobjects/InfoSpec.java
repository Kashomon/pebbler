package pebbler.pdfgen.baseobjects;

import junit.framework.TestCase;

public class InfoSpec extends TestCase {

  public void testInfoDisplay() {
    BodyObject k = new Info("tester", "jh");
    String out = k.display(new StringBuilder(), null).toString();
    String expected = (new StringBuilder())
      .append("-1 0 obj\n")
      .append("<<\n")
      .append("/Producer (tester)\n")
      .append("/Creator (jh)\n")
      .append(">>\n")
      .append("endobj\n")
      .toString();

    assertEquals(out, expected);
  }   
 
}
