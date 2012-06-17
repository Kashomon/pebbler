package pebbler.project;

import static pebbler.util.Checks.checkNotEmpty;
import static pebbler.util.Checks.checkNotNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.LinkedList;
import java.util.List;

public final class BookSettings {

  private String author;
  private String title;
  private String filename;
  private List<BookSection> sections;
  private int problemColumns;
  private int problemRows;
  private int answerColumns;
  private int answerRows;
  private int varsPerProblem;
  private boolean numberProblems;

  private BookSettings(Builder builder) {
    this.title = checkNotNull(builder.title);
    this.filename = checkNotNull(builder.filename);
    this.author = builder.author;
    this.sections = checkNotEmpty(builder.sections);
    this.problemColumns = builder.problemColumns;
    this.problemRows = builder.problemRows;
    this.answerColumns = builder.answerColumns;
    this.answerRows = builder.answerRows;
    this.varsPerProblem = builder.varsPerProblem;
    this.numberProblems = builder.numberProblems;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public String getFilename() {
    return filename;
  }

  public int getProblemColumns() {
    return problemColumns;
  }

  public int getProblemRows() {
    return problemRows;
  }

  public int getAnswerColumns() {
    return answerColumns;
  }

  public int getAnswerRows() {
    return answerRows;
  }

  public int getVarsPerProblem() {
    return varsPerProblem;
  }

  public boolean numberProblems() {
    return numberProblems;
  }

  public List<BookSection> getSections() {
    LinkedList<BookSection> copy = new LinkedList<BookSection>();
    copy.addAll(sections);
    return copy;
  }

  private static final String TITLE = "title";
  private static final String AUTHOR = "author";
  private static final String FILENAME = "filename";
  private static final String SECTIONS = "sections";
  private static final String PROBLEM_COLUMNS = "problemColumns";
  private static final String PROBLEM_ROWS = "problemRows";
  private static final String ANSWER_COLUMNS = "answerColumns";
  private static final String ANSWER_ROWS = "answerColumns";
  private static final String VARS_PER_PROBLEM = "varsPerProblem";
  private static final String NUMBER_PROBLEMS = "numberProblems";
  /**
   * Serialized, looks like.
   *
   * {
   *  "title": "someTitle",
   *  "author": "SomeAuthor",
   *  "problemColumns : 3,
   *  ...
   *  "sections": [
   *    { "header" : "myheader", "problemsNames" : [ "easy.sgf"] },
   *    { ...  }
   *   ]
   *
   */
  public JsonElement toJson() {
    JsonObject out = new JsonObject();
    JsonArray jsonSections = new JsonArray();
    for (BookSection section : sections) {
      jsonSections.add(section.toJson());
    }
    out.add(SECTIONS, jsonSections);

    if (title != DEFAULT_TITLE) {
      out.addProperty(TITLE, title);
    }
    if (author != null) {
      out.addProperty(AUTHOR, author);
    }
    if (filename != DEFAULT_FILENAME) {
      out.addProperty(FILENAME, filename);
    }
    if (problemColumns != DEFAULT_COLUMNS) {
      out.addProperty(PROBLEM_COLUMNS, problemColumns);
    }
    if (problemRows != DEFAULT_ROWS) {
      out.addProperty(PROBLEM_ROWS, problemRows);
    }
    if (answerColumns != DEFAULT_COLUMNS) {
      out.addProperty(ANSWER_COLUMNS, answerColumns);
    }
    if (answerRows != DEFAULT_ROWS) {
      out.addProperty(ANSWER_ROWS, answerRows);
    }
    if (varsPerProblem != DEFAULT_COLUMNS) {
      out.addProperty(VARS_PER_PROBLEM, varsPerProblem);
    }
    if (!numberProblems) {
      out.addProperty(NUMBER_PROBLEMS, numberProblems);
    }
    return out;
  }

  @Override
  public String toString() {
    return this.toJson().toString();
  }

  public static BookSettings fromJson(String json) {
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(json);
    return fromJson(parsed);
  }

  public static BookSettings fromJson(JsonElement elem) {
    JsonObject obj = elem.getAsJsonObject();
    Builder builder = new Builder();
    if (obj.has(TITLE)) {
      builder.title(obj.getAsJsonPrimitive(TITLE).getAsString());
    }
    if (obj.has(AUTHOR)) {
      builder.author(obj.getAsJsonPrimitive(AUTHOR).getAsString());
    }
    if (obj.has(FILENAME)) {
      builder.filename(obj.getAsJsonPrimitive(FILENAME).getAsString());
    }
    if (obj.has(PROBLEM_COLUMNS)) {
      builder.problemColumns(obj.getAsJsonPrimitive(PROBLEM_COLUMNS)
          .getAsInt());
    }
    if (obj.has(PROBLEM_ROWS)) {
      builder.problemRows(obj.getAsJsonPrimitive(PROBLEM_ROWS)
          .getAsInt());
    }
    if (obj.has(ANSWER_COLUMNS)) {
      builder.answerColumns(obj.getAsJsonPrimitive(ANSWER_COLUMNS)
          .getAsInt());
    }
    if (obj.has(ANSWER_ROWS)) {
      builder.answerRows(obj.getAsJsonPrimitive(ANSWER_ROWS)
          .getAsInt());
    }
    if (obj.has(VARS_PER_PROBLEM)) {
      builder.varsPerProblem(obj.getAsJsonPrimitive(VARS_PER_PROBLEM)
          .getAsInt());
    }
    if (obj.has(NUMBER_PROBLEMS)) {
      builder.numberProblems(obj.getAsJsonPrimitive(NUMBER_PROBLEMS)
          .getAsBoolean());
    }
    for (JsonElement jsonSection: obj.getAsJsonArray(SECTIONS)) {
      builder.addSection(BookSection.fromJson(jsonSection));
    }
    return builder.build();
  }

  private static final int DEFAULT_COLUMNS = 3;
  private static final int DEFAULT_ROWS = 3;
  private static final String DEFAULT_TITLE = "A Collection of Problems";
  private static final String DEFAULT_FILENAME = "pebblerbook";
  public static class Builder {
    private String title = DEFAULT_TITLE;
    private String author = null;
    private String filename = DEFAULT_FILENAME;
    private List<BookSection> sections = new LinkedList<BookSection>();
    private int problemColumns = DEFAULT_COLUMNS;
    private int problemRows = DEFAULT_ROWS;
    private int answerColumns = DEFAULT_COLUMNS;
    private int answerRows = DEFAULT_ROWS;
    int varsPerProblem = DEFAULT_COLUMNS;
    boolean numberProblems = true;

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder author(String author) {
      this.author = author;
      return this;
    }

    public Builder filename(String filename) {
      this.filename = filename;
      return this;
    }

    public Builder problemColumns(int problemColumns) {
      this.problemColumns = problemColumns;
      return this;
    }

    public Builder problemRows(int problemRows) {
      this.problemRows = problemRows;
      return this;
    }

    public Builder answerColumns(int answerColumns) {
      this.answerColumns = answerColumns;
      return this;
    }

    public Builder answerRows(int answerRows) {
      this.answerRows = answerRows;
      return this;
    }

    public Builder varsPerProblem(int varsPerProblem) {
      this.varsPerProblem = varsPerProblem;
      return this;
    }

    public Builder numberProblems(boolean numberProblems) {
      this.numberProblems = numberProblems;
      return this;
    }

    public Builder addSection(BookSection section) {
      sections.add(section);
      return this;
    }

    public BookSettings build() {
      return new BookSettings(this);
    }
  }

  public static class BookSection {
    private String header;
    private List<String> problemNames;

    public BookSection(String header, List<String> problemNames) {
      this.header = header;
      this.problemNames = checkNotEmpty(problemNames, "problemNames");
    }

    public String getHeader() {
      return header;
    }

    public List<String> getProblems() {
      LinkedList<String> copy = new LinkedList<String>();
      copy.addAll(problemNames);
      return copy;
    }

    private static final String HEADER = "header";
    private static final String PROBLEMS = "problems";
    public JsonElement toJson() {
      JsonObject out = new JsonObject();
      if (header != null) {
        out.addProperty(HEADER, header);
      }
      JsonArray arr = new JsonArray();
      for (String name : problemNames) {
        arr.add(new JsonPrimitive(name));
      }
      out.add(PROBLEMS, arr);
      return out;
    }

    public static BookSection fromJson(JsonElement elem) {
      JsonObject obj = elem.getAsJsonObject();
      String header = obj.has(HEADER) ?
          obj.getAsJsonPrimitive(HEADER).getAsString() : null;
      LinkedList<String> names = new LinkedList<String>();
      for (JsonElement probElem : obj.get(PROBLEMS).getAsJsonArray()) {
        names.add(probElem.getAsString());
      }
      return new BookSection(header, names);
    }
  }
}
