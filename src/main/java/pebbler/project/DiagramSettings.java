package pebbler.project;

import static pebbler.util.Checks.checkNotEmpty;
import static pebbler.util.Checks.checkNotNull;

import pebbler.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Diagram configuration for an Sgf.
 */
public class DiagramSettings {

  private File sgfFile;
  private List<Variation> variations;

  public DiagramSettings(File sgfFile, List<Variation> variations) {
    this.sgfFile = checkNotNull(sgfFile);
    List<Variation> copy = new LinkedList<Variation>();
    copy.addAll(variations);
    this.variations = checkNotEmpty(copy);
  }

  public File getSgfFile() {
    return sgfFile;
  }

  public List<Variation> getVariations() {
    List<Variation> copy = new LinkedList<Variation>();
    copy.addAll(variations);
    return copy;
  }


  private static final String SGF_FILE = "sgfFile";
  private static final String VARIATIONS = "variations";
  /**
   * The json looks like the following.
   * {
   *  "sgfFile" = "filename.sgf",
   *  "variations = [ ... ],  //see Variations.
   * }
   *
   */
  public JsonElement toJson() {
    JsonObject out = new JsonObject();
    out.addProperty(SGF_FILE, sgfFile.getPath());
    JsonArray varsArr = new JsonArray();
    for (Variation var : variations) {
      varsArr.add(var.toJson());
    }
    out.add(VARIATIONS, varsArr);
    return out;
  }


  @Override
  public String toString() {
    return toJson().toString();
  }

  public static DiagramSettings fromJson(String json) {
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(json);
    return fromJson(parsed);
  }

  public static DiagramSettings fromJson(JsonElement elem) {
    JsonObject obj = elem.getAsJsonObject();
    String filename = obj.getAsJsonPrimitive(SGF_FILE).getAsString();
    if (filename.lastIndexOf(".sgf") == -1) {
      throw new RuntimeException("SgfFiles must end in .sgf. Filename: " +
         filename);
    }
    List<Variation> variations = new LinkedList<Variation>();
    JsonArray varArr = obj.getAsJsonArray(VARIATIONS);
    for (JsonElement varElem : varArr) {
      variations.add(Variation.fromJson(varElem));
    }
    return new DiagramSettings(new File(filename), variations);
  }

  public void compile(
      CompiledPdfInfo pdfInfo,
      String parentDir, 
      int diagramNum) {
    String cannonicalName = parentDir + File.separator + sgfFile.getPath();
    File canFile = new File(cannonicalName);
    if (!canFile.exists() || !canFile.isFile()) {
      throw new RuntimeException("Couldn't find the sgf file: " +
          canFile.getPath());
    }

    SgfInstance sgf = SgfInstance.fromFilename(cannonicalName);
    List<String> cachedVariationPaths = new LinkedList<String>();

    int varNum = 1;
    for (Variation var : variations) {
      Pair<String, String> filePath_comments = var.compile(parentDir, sgf,
          sgfFile.getName().replaceAll(".sgf$", ""), varNum);
      cachedVariationPaths.add(filePath_comments.first());
      pdfInfo.addComments(
          filePath_comments.first(),
          filePath_comments.second());
      varNum++;
    }
    pdfInfo.addFilenames(sgfFile.getName(), cachedVariationPaths);
  }
}
