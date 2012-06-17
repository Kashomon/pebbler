package pebbler.project;

import static pebbler.constants.GoColor.BLACK;
import static pebbler.constants.GoColor.WHITE;
import static pebbler.util.Checks.checkNotEmpty;
import static pebbler.util.Checks.checkNotNull;

import pebbler.constants.Cropping;
import pebbler.constants.FontInfo;
import pebbler.constants.GoColor;
import pebbler.parsing.ParseException;
import pebbler.pdfgen.baseobjects.Document;
import pebbler.pdfgen.graphics.GoGrid;
import pebbler.project.Move;
import pebbler.project.SgfInstance;
import pebbler.util.GoFileUtil;
import pebbler.util.GoPoint;
import pebbler.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variation {

  private List<VariationMove> moves; //Required
  private String name; // Nullable
  private String description; // Nullable
  // Nullable.  Required to be 4 long, if specified.
  private Cropping cropbox; // default = [1,1,19,19]
  private boolean continueToEnd; // default = false
  private boolean descriptionFromSgf; // default = true
  private boolean useMoveNumbering; // default = false

  private static int HIGHEST_MOVE = 10000; // for continueToEnd;

  private Variation(Builder builder) {
    this.moves = checkNotEmpty(builder.moves);
    this.name = builder.name; // can be null
    this.description = builder.description; // can be null.
    this.cropbox = checkNotNull(builder.cropbox);
    this.descriptionFromSgf = builder.descriptionFromSgf;
    this.continueToEnd = builder.continueToEnd;
    this.useMoveNumbering = builder.useMoveNumbering;
  }

  public List<VariationMove> getMoves() {
    List<VariationMove> copy = new LinkedList<VariationMove>();
    copy.addAll(moves);
    return copy;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Cropping getCropbox() {
    return cropbox;
  }

  public boolean continueToEnd() {
    return continueToEnd;
  }

  public boolean descriptionFromSgf() {
    return descriptionFromSgf;
  }

  public boolean useMoveNumbering() {
    return useMoveNumbering;
  }

  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String MOVES = "moves";
  private static final String TOEND = "continueToEnd";
  private static final String DESCRIPTION_SGF = "descriptionFromSgf";
  private static final String MOVE_NUMBERING = "useMoveNumbering";
  private static final String CROPBOX = "cropbox";
  /**
   * The json looks like the following.
   *  [
   *    {
   *      "name" : "foo",
   *      "description" : "bar",
   *      "descriptionFromSgf" : true
   *      "moves" : [ "1", "2.1", ... ],
   *      "continueToEnd" : true
   *      "cropbox" : "TopRight" // see constants.Cropping
   *    }
   *  ]
   */
  public JsonElement toJson() {
    JsonObject out = new JsonObject();
    if (name != null) {
      out.addProperty(NAME, name);
    }
    if (description != null) {
      out.addProperty(DESCRIPTION, description);
    }
    if (continueToEnd) {
      out.addProperty(TOEND, continueToEnd);
    }
    if (!descriptionFromSgf) {
      out.addProperty(DESCRIPTION_SGF, descriptionFromSgf);
    }
    if (useMoveNumbering) {
      out.addProperty(MOVE_NUMBERING, useMoveNumbering);
    }
    if (cropbox != Cropping.Full) {
      out.addProperty(CROPBOX, cropbox.toString());
    }

    JsonArray movesArr = new JsonArray();
    for (VariationMove move : moves) {
      movesArr.add(move.toJson());
    }
    out.add(MOVES, movesArr);
    return out;
  }

  @Override
  public String toString() {
    return toJson().toString();
  }


  public static Variation fromJson(String str) {
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(str);
    return fromJson(parsed);
  }

  public static Variation fromJson(JsonElement elem) {
    JsonObject obj = elem.getAsJsonObject();

    Builder builder = new Builder();
    if (obj.has(NAME)) {
      builder.setName(obj.getAsJsonPrimitive(NAME).getAsString());
    }
    if (obj.has(DESCRIPTION)) {
      builder.setDescription(obj.getAsJsonPrimitive(DESCRIPTION)
          .getAsString());
    }
    if (obj.has(TOEND)) {
      builder.setContinueToEnd(obj.getAsJsonPrimitive(TOEND)
          .getAsBoolean());
    }
    if (obj.has(DESCRIPTION_SGF)) {
      builder.setDescriptionFromSgf(obj.getAsJsonPrimitive(DESCRIPTION_SGF)
          .getAsBoolean());
    }
    if (obj.has(MOVE_NUMBERING)) {
      builder.setUseMoveNumbering(obj.getAsJsonPrimitive(MOVE_NUMBERING)
          .getAsBoolean());
    }
    if (obj.has(CROPBOX)) {
      builder.setCropbox(Cropping.valueOf(
          obj.getAsJsonPrimitive(CROPBOX).getAsString()));
    }

    int lastMove = -1;
    for (JsonElement moveElem : obj.getAsJsonArray(MOVES)) {
      VariationMove mv = VariationMove.fromJson(moveElem);
      if (mv.getMove() <= lastMove) {
        throw new RuntimeException(
          "Moves must be monotonically increasing." +
          "  LastMove: " + lastMove + ", Current Move: "  + mv.getMove());
      }
      builder.addMove(mv);
    }
    return builder.build();
  }

  // Compile the PDF
  public Pair<String, String> compile(
      String parentDir,
      SgfInstance sgf,
      String sgfName,
      int varNum) {
    String outfileName = getOutputName(sgfName, varNum);
    outfileName = parentDir + File.separator + outfileName + ".pdf";
    System.out.println("Writing: " + outfileName);

    Document doc = new Document();
    doc.setCropping(this.cropbox);
    GoGrid grid = new GoGrid();

    String[] props = {"AW", "AB", "W", "B"};
    String commentProp = "C";
    GoColor[] colors = {WHITE, BLACK, WHITE, BLACK};

    List<VariationMove> moveMarkers = getMoves();
    if (continueToEnd) {
      moveMarkers.add(new VariationMove(HIGHEST_MOVE));
    }
    VariationMove start = moveMarkers.get(0);
    Move curMove = sgf.getRoot();

    String commentString = descriptionFromSgf ? "" : description;

    int seenMoves = 1; // for numbering purposes

    // Holy hell this needs to be refactored.
    // Loop All the Things!
    for (VariationMove varMove : moveMarkers) {

      // Add the moves that are less than the moves listed in the variation to
      // provide context.  This needs to be more clever for Kos and Captures.
      while(curMove != null
          && curMove.getMoveNum() <= varMove.getMove()) {

        if (curMove.hasProperty("C")) {
          if ((varMove != start &&
              descriptionFromSgf &&
              curMove.hasProperty("C")) ||
              (moveMarkers.size() == 1 && !continueToEnd)) {
            commentString += curMove.getProperties().get("C").get(0);
          }
        }

        // Props:
        for (int i = 0; i < props.length; i++) {
          String prop = props[i];
          GoColor color = colors[i];
          if (curMove.hasProperty(prop)) {
            List<String> coords = curMove.getProperties().get(prop);
            for (String c : coords) {
              GoPoint pt = Move.coordToInt(c);
              grid.addStone(pt.x, pt.y, color);
              if (varMove != start && (prop == "W" || prop == "B")) {
                grid.addTextLabel("" + seenMoves, FontInfo.HELVETICA, pt.x,
                    pt.y, color.opposite());
              }
            }
          }
        }

        // Branch switching logic. NOTE: Branches are 0 indexed.  This should
        // probably change.
        if (curMove.getMoveNum() == varMove.getMove()) {
          curMove = curMove.getChild(varMove.getBranch());
        } else {
          curMove = curMove.getFirstChild();
        }

        if (varMove != start) {
          seenMoves++;
        }
      }
    }
    doc.addStream(grid.streamOut());
    doc.write(outfileName);
    return Pair.of(outfileName, commentString);
  }

  public String getOutputName(String sgfName, int varNum) {
    return name != null ? sgfName + "_" + name : sgfName + "_" + varNum;
  }

  public static class Builder {
    List<VariationMove> moves = new LinkedList<VariationMove>();
    String name = null;
    String description = null;
    Cropping cropbox = PebblerProject.getDefaultCropbox();
    boolean continueToEnd = false;
    boolean descriptionFromSgf = true;
    boolean useMoveNumbering = false;

    public Builder addMove(int move) {
      moves.add(new VariationMove(move));
      return this;
    }

    public Builder addMove(int move, int branch) {
      moves.add(new VariationMove(move, branch));
      return this;
    }

    public Builder addMove(VariationMove move) {
      moves.add(move);
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setCropbox(Cropping cropbox) {
      this.cropbox = cropbox;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setContinueToEnd(boolean continueToEnd) {
      this.continueToEnd = continueToEnd;
      return this;
    }

    public Builder setDescriptionFromSgf(boolean descriptionFromSgf) {
      this.descriptionFromSgf = descriptionFromSgf;
      return this;
    }

    public Builder setUseMoveNumbering(boolean useMoveNumbering) {
      this.useMoveNumbering = useMoveNumbering;
      return this;
    }

    public Variation build() {
      return new Variation(this);
    }
  }

  public static class VariationMove {
    private static final int PRIMARY_BRANCH = 0;
    private int moveNumber;
    private int branchNumber;

    public VariationMove(int moveNumber) {
      this(moveNumber, PRIMARY_BRANCH);
    }

    public VariationMove(int moveNumber, int branchNumber) {
      if (moveNumber < 0 || branchNumber < 0) {
        throw new IllegalArgumentException(
            "Move and Branch numbers cannot be negative: " +
            "move:" + moveNumber + ", " + "branch:" + branchNumber);
      }
      this.moveNumber = moveNumber;
      this.branchNumber = branchNumber;
    }

    @Override
    public String toString() {
      if (branchNumber == PRIMARY_BRANCH) {
        return String.valueOf(moveNumber);
      } else {
        return moveNumber + "." + branchNumber;
      }
    }

    public int getBranch() {
      return branchNumber;
    }

    public int getMove() {
      return moveNumber;
    }

    public JsonElement toJson() {
      return new JsonPrimitive(toString());
    }

    public static VariationMove fromJson(JsonElement elem) {
      String[] parts = elem.getAsJsonPrimitive().getAsString().split("\\.");
      int move = Integer.parseInt(parts[0]);
      int branch = parts.length >= 2 ? Integer.parseInt(parts[1]) :
          PRIMARY_BRANCH;
      return new VariationMove(move, branch);
    }
  }
}
