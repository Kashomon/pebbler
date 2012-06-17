package pebbler.project;

import junit.framework.TestCase;

public class DiagramSettingsTest extends TestCase {

  private static final String shortExample = 
    "{" + 
    "\"sgfFile\":\"foo/foobar.sgf\"," +
    "\"variations\":[{\"moves\" = [\"1.2\", \"3\"]}]" +
    "}";

  public void testFullDiagramSettingsParse() throws Exception {
    DiagramSettings settings = DiagramSettings.fromJson(shortExample);
    assertEquals("file", "foobar.sgf", settings.getSgfFile().getName());
    assertEquals("path", "foo/foobar.sgf", settings.getSgfFile().getPath());
    assertEquals("move", 1,
        settings.getVariations().get(0).getMoves().get(0).getMove());
  }

  public void testDiagramSettingsRoundTrip() throws Exception {
    DiagramSettings settings = DiagramSettings.fromJson(shortExample);
    String output = settings.toString();
    String output2 = DiagramSettings.fromJson(output).toString();
    assertEquals("output", output, output2);
  }
}
