package pebbler.project;

import junit.framework.TestCase;

public class BookSettingsTest extends TestCase {

  private static final String shortExample = 
    "{" + 
    "\"title\" : \"Problems\",\n" + 
    "\"author\" : \"Josh\",\n" +
    "\"problemRows\" : 4," +
    "\"sections\" : [\n"+
      "{\"header\" : \"foo\", \"problems\" : [ \"easy.sgf\", \"hard.sgf\"]}]\n" +
    "}";

  public void testBookSettings() throws Exception {
    BookSettings settings = BookSettings.fromJson(shortExample);
    assertEquals("title", "Problems", settings.getTitle());
    assertEquals("author", "Josh", settings.getAuthor());
    assertEquals("problemRows", 4, settings.getProblemRows());
    assertEquals("header", "foo", settings.getSections().get(0).getHeader());
    assertEquals("problems", "easy.sgf", 
        settings.getSections().get(0).getProblems().get(0));
  }

  public void testBookSettingsRoundTrip() throws Exception {
    BookSettings settings = BookSettings.fromJson(shortExample);
    String output = settings.toString();
    String output2 = BookSettings.fromJson(output).toString();
    assertEquals("output", output, output2);
  }
}
