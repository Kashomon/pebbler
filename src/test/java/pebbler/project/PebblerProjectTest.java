package pebbler.project;

import pebbler.parsing.ParseException;

import junit.framework.TestCase;

public class PebblerProjectTest extends TestCase {

  private static final String shortExample = 
    "{" +
    "\"diagrams\":[" + 
      "{" + 
      "\"sgfFile\":\"foo/foobar.sgf\"," +
      "\"variations\":[{\"moves\" = [\"1.2\", \"3\"]}]" +
      "}," +
      "{" + 
      "\"sgfFile\":\"bar.sgf\"," +
      "\"variations\":[{\"moves\" = [\"1.2\", \"3\"]}]" +
      "}" +
    "]}";
    
  public void testFullProjectParse() throws Exception {
    PebblerProject proj = PebblerProject.fromJson(shortExample);
    assertEquals("name1", "foobar.sgf", proj.getDiagrams()
        .get(0).getSgfFile().getName());
    assertEquals("name2", "bar.sgf", proj.getDiagrams()
        .get(1).getSgfFile().getName());
  }

  public void testProjectRoundTrip () throws Exception {
    PebblerProject proj = PebblerProject.fromJson(shortExample);
    String output = proj.toString();
    String output2 = PebblerProject.fromJson(output).toString();
    assertEquals("output", output, output2);
  }
}
