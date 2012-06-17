package pebbler.parsing;

import pebbler.project.Move;
import pebbler.project.SgfInstance;

import junit.framework.TestCase;

public class ParserTest extends TestCase {

  public static String examples = "src/main/resources/examples/";

  public void testParsingRootGM() throws Exception {
    Parser p = new Parser();
    SgfInstance g = p.parse(examples + "easy.sgf");
    String parp = g.getRoot().getProperties().get("GM").get(0);
    assertEquals(parp, "1");
  }

  public void testParsing1stMove() throws Exception {
    Parser p = new Parser();
    SgfInstance g = p.parse(examples + "easy.sgf");
    String parp = g.getRoot().getFirstChild().getProperties().get("B").get(0);
    assertEquals(parp, "pa");
  }

  public void testParsing1stMoveCoord() throws Exception {
    Parser p = new Parser();
    SgfInstance g = p.parse(examples + "easy.sgf");
    String parp = g.getRoot().getFirstChild().getProperties().get("B").get(0);
    int[] expected = { 16, 19 };
    assertEquals(expected[0], Move.coordToInt("pa").x);
    assertEquals(expected[1], Move.coordToInt("pa").y);
  }
}

