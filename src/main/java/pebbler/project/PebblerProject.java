package pebbler.project;

import pebbler.constants.Cropping;
import pebbler.util.GoFileUtil;
import pebbler.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The PebblerProject is the basis for all configuration for Pebbler.
 *
 * The serialize form looks like:
 * {
 *  "cropbox" : 
 *  "bookSettings" : { ... }
 *  "diagrams" : [...]
 * }
 */
public final class PebblerProject {

  // TODO: Put this into a contained Globals object.
  private static Cropping cropbox = Cropping.Full;

  private BookSettings bookSettings;
  private List<DiagramSettings> diagrams;

  // Null until compile() occurs.
  CompiledPdfInfo pdfInfo;

  private PebblerProject(Builder builder) {
    this.diagrams = builder.diagrams;
    this.bookSettings = builder.bookSettings;
    cropbox = builder.cropbox;
  }

  public List<DiagramSettings> getDiagrams()  {
    List<DiagramSettings> copy = new LinkedList<DiagramSettings>();
    // No need for deep copy because DiagramSettings objects are immutable.
    copy.addAll(diagrams);
    return copy;
  }

  public BookSettings getBookSettings() {
    return bookSettings;
  }

  public CompiledPdfInfo getPdfInfo() {
    return pdfInfo;
  }

  public static Cropping getDefaultCropbox() {
    return cropbox;
  }

  private static void setDefaultCropbox(Cropping cropbox) {
    PebblerProject.cropbox = cropbox;
  }

  private static final String DIAGRAM_SETTINGS = "diagrams";
  private static final String BOOK_SETTINGS = "bookSettings";
  private static final String CROPBOX = "cropbox";
  /**
   * The JSON looks like the following.
   *
   * {
   *  "bookSettings" : { ... }
   *  "diagrams" : [...]
   * }
   *
   * TODO: add op-level settings (like updateTime
   */
  public JsonElement toJson() {
    JsonObject out = new JsonObject();
    JsonArray diaArr = new JsonArray();
    for (DiagramSettings settings : diagrams) {
      diaArr.add(settings.toJson());
    }
    out.add(DIAGRAM_SETTINGS, diaArr);
    if (bookSettings != null) {
      out.add(BOOK_SETTINGS, bookSettings.toJson());
    }
    if (cropbox != Cropping.Full) {
      out.add(CROPBOX, bookSettings.toJson());
    }
    return out;
  }

  @Override
  public String toString() {
    return toJson().toString();
  }

  public static PebblerProject fromJson(String json) {
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(json);
    return fromJson(parsed);
  }

  public static PebblerProject fromJson(JsonElement elem) {
    JsonObject obj = elem.getAsJsonObject();
    Builder builder = new Builder();
    // This has to be parsed immediately to set the default Cropbox.
    if (obj.has(CROPBOX)) {
      Cropping cropbox = Cropping.valueOf(
            obj.getAsJsonPrimitive(CROPBOX).getAsString());
      PebblerProject.setDefaultCropbox(cropbox);
      builder.cropbox(cropbox);
    }
    for (JsonElement diaElem : obj.getAsJsonArray(DIAGRAM_SETTINGS)) {
      builder.addDiagram(DiagramSettings.fromJson(diaElem));
    }
    if (obj.has(BOOK_SETTINGS)) {
      builder.bookSettings(BookSettings.fromJson(obj.get(BOOK_SETTINGS)));
    }
    return builder.build();
  }

  /**
   * Compile the PDFs.
   */
  public void compile(String parentDir) {
    int diagramNum = 1;
    pdfInfo = new CompiledPdfInfo();
    for (DiagramSettings diagram : diagrams) {
      diagram.compile(pdfInfo, parentDir, diagramNum);
      diagramNum++;
    }
  }

  public static class Builder {
    Cropping cropbox = Cropping.Full;
    BookSettings bookSettings = null;
    List<DiagramSettings> diagrams = new LinkedList<DiagramSettings>();

    public Builder addDiagram(DiagramSettings diagram) {
      diagrams.add(diagram);
      return this;
    }

    public Builder cropbox(Cropping cropbox) {
      this.cropbox = cropbox;
      return this;
    }

    public Builder bookSettings(BookSettings bookSettings) {
      this.bookSettings = bookSettings;
      return this;
    }

    public PebblerProject build() {
      return new PebblerProject(this);
    }
  }
}
