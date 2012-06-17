package pebbler.parsing;

import java.util.*;

public class TokenUtility {

  private TokenUtility () {}

  public static int charA = 'A';
  public static int charZ = 'Z';
  public static int chara = 'a';
  public static int charz = 'z';

  public static boolean isDelimiter(char c) {
    return c == ')' || c == '(' || c == ';';
  }

  public static boolean isSyntax (char c) {
    return c == ')' || c == '(' || c == ';' || c == '[' || c == ']';
  }
}
