package pebbler.project;

import pebbler.constants.Cropping;
import pebbler.parsing.ParseException;

import junit.framework.TestCase;

public class VariationTest extends TestCase {

  private static final String shortExample = "{\"moves\":[\"1.2\", \"3\"]}";
  private static final String longExample =
    "{\n" +
    "\"moves\":[\"5.3\", \"6\"],\n" +
    "\"name\":\"foo\",\n" +
    "\"description\":\"bar\",\n" +
    "\"cropbox\": \"TopLeft\",\n" +
    "\"descriptionFromSgf\":false,\n" +
    "\"continueToEnd\":true,\n" +
    "\"useMoveNumbering\":true\n" +
    "}";

  public void testFromJson() throws Exception {
    Variation var = Variation.fromJson(shortExample);
    assertEquals("move1", 1, var.getMoves().get(0).getMove());
    assertEquals("branch1", 2, var.getMoves().get(0).getBranch());
    assertEquals("move2", 3, var.getMoves().get(1).getMove());
    assertEquals("branch2", 0, var.getMoves().get(1).getBranch());
    assertTrue("desc fromsgf", var.descriptionFromSgf());
  }

  public void testFullJsonExample() throws Exception {
    Variation var = Variation.fromJson(longExample);
    assertEquals("name", "foo", var.getName());
    assertEquals("description", "bar", var.getDescription());
    assertEquals("move1", 5, var.getMoves().get(0).getMove());
    assertEquals("cropbox", Cropping.TopLeft, var.getCropbox());
    assertTrue("toend", var.continueToEnd());
    assertTrue("desc fromsgf", !var.descriptionFromSgf());
    assertTrue("moveNumb", var.useMoveNumbering());
  }

  public void testJsonRoundTrip() throws Exception {
    Variation var = Variation.fromJson(longExample);
    String output = var.toJson().toString();
    String secondOutput = Variation.fromJson(output).toJson().toString();
    assertEquals("outputs", output, secondOutput);
  }
}
