package pebbler.parsing;

import static pebbler.parsing.TokenUtility.isSyntax;

import pebbler.project.SgfInstance;
import pebbler.project.Move;
import pebbler.util.GoFileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Truly, this should be done with a parser generator.
 * Grammar Productions:
 * T      -> (T)T   || e
 * S      -> ;PS    || e
 * P      -> k[C]P  || e
 * C      -> cC
 * c      -> Char
 * k      -> token
 */
public class Parser {

  int index;
  String contents;

  public Parser() {
    this.contents = "";
    this.index = 0;
  }

  public static Parser instance() {
    return new Parser();
  }

  public SgfInstance parse(String filename) throws IOException {
    contents = GoFileUtil.readFile(filename);
    index = 0;
    return constructGame();
  }

  public SgfInstance reparse() throws IOException {
    if (contents == "") {
      return new SgfInstance();
    }
    index = 0;
    return constructGame();
  }

  protected char nextChar() {
    index++;
    return contents.charAt(index);
  }

  protected char getChar() {
    return contents.charAt(index);
  }

  protected void pushChar() {
    index--;
  }

  /*
   * Get everything after a semicolon but before another delimiter.
   */
  protected Map<String, List<String>> getProperties() {
    Map<String, List<String>> props = new HashMap<String, List<String>>();

    char c = nextChar();
    while(Character.isLetter(c) || Character.isWhitespace(c)) {
      if (Character.isWhitespace(c)) {
      } else {
        pushChar();
        addProperty(props);
      }
      c = nextChar();
    }
    pushChar();

    return props;
  }


  /*
   * Get a Property:  \w{1-2}[\w+]*
   * Adds a property to the Map;
   */
  protected void addProperty(Map<String, List<String>> props) {
    StringBuilder propName = new StringBuilder();

    char c = nextChar();
    if (isSyntax(c) || index >= contents.length()) {
      return ; 
    }

    while (!isSyntax(c) && index < contents.length()) {
      if (c != '\n' && c != ' ') {
        propName.append(c);
      }
      c = nextChar();
    }

    pushChar();
    String prop = propName.toString();
    List<String> data = getData();
    props.put(prop.toUpperCase(), data);
  }

  /*
   * Get the data: [\w+]*
   */
  protected List<String> getData() {
    List<String> dataFields = new LinkedList<String>();
    StringBuilder dataBuff = new StringBuilder();

    char c = nextChar();

    while (c == '[') {
      char prev = contents.charAt(index);
      c = nextChar();
      while ((c != ']' || (c == ']' && prev == '\\')) && index < contents.length()) {
        dataBuff.append(c);
        prev = c;
        c = nextChar();
      }

      String buffValue = dataBuff.toString();
      if (buffValue.length() != 0) {
        dataFields.add(buffValue);
        dataBuff = new StringBuilder();
      }

      c = nextChar();
    }

    pushChar();

    return dataFields;
  }

  protected SgfInstance constructGame() {
    SgfInstance game = new SgfInstance();
    Move lastMove = game.getRoot();
    Stack<Move> parents = new Stack<Move>();

    while (index < contents.length()) {
      char c = getChar();
      if (c == '(') {
        if (parents.size() == 0) {
          if (nextChar() == ';') {
            parents.push(lastMove.addProperties(getProperties()));
            parents.push(lastMove);
          } else {
            // should throw parse error
          }
        } else {
          parents.push(lastMove);
        }

      } else if (c == ')' ) {
        lastMove = parents.pop();

      } else if (c == ';') {
        lastMove = lastMove.createChild(getProperties()); //swap moves

      } else {
        //throw UnknownTokenException
      }
      index++;
    }
    return game;
  }

}

