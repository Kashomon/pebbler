package pebbler.project;

import static pebbler.util.Checks.checkNotEmpty;
import static pebbler.util.Checks.checkNotNull;

import pebbler.util.GoFileUtil;
import pebbler.util.Pair;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TypeSetter {

  private PebblerProject project;
  private CompiledPdfInfo pdfInfo;
  private BookSettings bookSettings;

  public TypeSetter(PebblerProject project) {
    this.project = project;
    this.pdfInfo = checkNotNull(project.getPdfInfo(),
        "PdfInfo must not be null when creating book!");
    this.bookSettings = checkNotNull(project.getBookSettings(),
        "BookSettings must not be null when creating book!");
  }

  public void createBook(String parentDir) {
    Map<String, List<String>> cached = pdfInfo.getAllFilenames();

    if (cached == null || cached.size() == 0) {
      throw new IllegalArgumentException(
          "Can't create a book without having" +
          "already compiled the sgfs to PDFs");
    }
    String latexFile = parentDir + File.separator
        + bookSettings.getFilename() + ".tex";
    StringBuilder bookBuilder = new StringBuilder();

    generateHeader(
        bookBuilder,
        bookSettings.getTitle(),
        bookSettings.getAuthor());

    generateProblems(bookBuilder, bookSettings, cached);

    generateFooter(bookBuilder);

    System.out.println("Writing the go book at: " + latexFile);
    GoFileUtil.silentWriteFile(latexFile, bookBuilder.toString());
  }

  private void generateProblems(
      StringBuilder builder,
      BookSettings settings,
      Map<String, List<String>> cached) {
    int maxProblemColumns = settings.getProblemColumns();
    int maxProblemRows = settings.getProblemRows();
    int maxAnswerColumns = settings.getAnswerColumns();
    int maxAnswerRows = settings.getAnswerRows();

    for (BookSettings.BookSection section : settings.getSections()) {

      /*
       * The flow:
       * ProblemRow + ProblemCommentsRow > Problem Page
       * AnswerRow + AnswerCommentsRow > Answer Page > AnswerPagesBuffer
       * Problem Page + AnswerPagesBuffer > book (builder)
       */
      StringBuilder problemPage = new StringBuilder();


      StringBuilder problemRow = new StringBuilder();
      StringBuilder problemRowComments = new StringBuilder();

      // We need a pageBuffer and a Pages buffer since there will typically be
      // multiple pages per problem.
      StringBuilder answerPagesBuffer = new StringBuilder();
      StringBuilder answerPage = new StringBuilder();

      StringBuilder answerRow = new StringBuilder();
      StringBuilder answerRowComments = new StringBuilder();

      if (section.getHeader() != null) {
        problemPage.append(generateSubHeader(section.getHeader()));
        answerPage.append(generateSubHeader("Answers", true));
      }

      startPage(problemPage, maxProblemColumns);
      startPage(answerPage, maxAnswerColumns);

      int curProblemColumn = 0;
      int curProblemRow = 0;
      int curAnswerColumn = 0;
      int curAnswerRow = 0;

      int numAnswers = 0; // resets when answers Page resets
      int numProblems = 0; // resets when problems Page resets;
      int totalNumProblems = 1; // 1-indexed.
      for (String sgf : section.getProblems()) {
        List<String> generated = cached.get(sgf);

        String problem = generated.get(0);
        includeGraphics(problemRow, problem);
        addTableCell(problemRow, curProblemColumn, maxProblemColumns);
        includeComments(
            problemRowComments,
            addProblemPrefix(pdfInfo.getVariationComments(problem),
                totalNumProblems));
        addTableCell(problemRowComments, curProblemColumn, maxProblemColumns);

        if (curProblemColumn == maxProblemColumns - 1) {
          problemPage.append(problemRow).append(problemRowComments);
          problemRow = new StringBuilder();
          problemRowComments = new StringBuilder();
        }

        curProblemColumn = (curProblemColumn + 1) % maxProblemColumns;
        numProblems++;

        // Add the problems.
        for (int i = 1; i <= settings.getVarsPerProblem() &&
            i < generated.size(); i++) {
          String answer = generated.get(i);
          includeGraphics(answerRow, answer);
          addTableCell(answerRow, curAnswerColumn, maxAnswerColumns);
          includeComments(answerRowComments,
              addProblemPrefix(pdfInfo.getVariationComments(answer),
                  totalNumProblems));
          addTableCell(answerRowComments, curAnswerColumn, maxAnswerColumns);
          if (curAnswerColumn == maxAnswerColumns - 1) {
            answerPage.append(answerRow).append(answerRowComments);
            answerRow = new StringBuilder();
            answerRowComments = new StringBuilder();
          }

          curAnswerColumn = (curAnswerColumn + 1) % maxAnswerColumns;
          numAnswers++;
          if (numAnswers == maxAnswerRows * maxAnswerColumns) {
            finishPageWithTrailer(answerPagesBuffer, answerPage);
            answerPage = startNewPage(maxAnswerColumns);
            numAnswers = 0;
          }
        }

        totalNumProblems++;

        if (numProblems == maxProblemRows * maxProblemColumns) {
          finishPageWithTrailer(answerPagesBuffer, answerPage);
          finishPageWithTrailer(builder, problemPage);
          finishPage(builder, answerPagesBuffer);
          answerPage = startNewPage(maxAnswerColumns);
          problemPage = startNewPage(maxProblemColumns);
          answerPagesBuffer = new StringBuilder();
          numProblems = 0;
          numAnswers = 0;
        }
      }

      // Clear out the buffers before moving on.
      if (answerPage.length() != 0) {
        finishPageWithTrailer(answerPagesBuffer, answerPage);
      }
      if (problemPage.length() != 0) {
        finishPageWithTrailer(builder, problemPage);
      }
      finishPage(builder, answerPagesBuffer);
    }
  }

  private StringBuilder startNewPage(int cols) {
    // return startLongTable(new StringBuilder(), cols);
    return startTabular(new StringBuilder(), cols);
  }

  private String addProblemPrefix(String comment, int totalNumProblems) {
    StringBuilder builder = new StringBuilder();
    builder.append("{\\centering");
    if (bookSettings.numberProblems()) {
      builder.append("\\textbf{Problem " + totalNumProblems)
          .append("}\\\\\n");
      if (comment != null && comment != "") {
        builder.append(comment);
      }
    }
    return builder.append("}").toString();
  }

  private StringBuilder includeGraphics(StringBuilder builder, String file) {
    return builder.append("\\includegraphics[scale=0.48]{")
        .append(file).append("}\n");
  }

  private StringBuilder addTableCell(
      StringBuilder builder,
      int curColumn,
      int maxColumns) {
    if ((curColumn % maxColumns) == maxColumns - 1)
      builder.append(" \\\\");
    else
      builder.append(" &");
    return builder.append("\n");
  }

  private StringBuilder includeComments(StringBuilder builder, String comment) {
    return builder.append(comment.replaceAll("\\[", "\\[")
        .replaceAll("\\]", "\\]"));
  }

  private String generateSubHeader(String header) {
    return generateSubHeader(header, false);
  }

  private String generateSubHeader(String header, boolean isAnswer) {
    String base = "\\begin{gotitle}\\textbf{" + header + "}\\end{gotitle}\n";
    return isAnswer ? base : "\\cleardoublepage" + base;
  }

  private StringBuilder startLongTable(StringBuilder page, int cols) {
    page.append("\\begin{longtable}")
      .append("{ ");
    for (int i = 0; i < cols; i++ )  {
      page.append("V ");
    }
    return page.append("}\n");
  }

  private StringBuilder endLongTable(StringBuilder builder) {
    return builder.append("\\end{longtable}\n").append("\\newpage\n");
  }

  private StringBuilder endTabular(StringBuilder builder) {
    return builder.append("\\end{tabular}\\end{table}\n\\newpage\n");
  }

  private StringBuilder startTabular(StringBuilder page, int cols) {
    page.append("\\begin{table}[ht]\n")
        .append("\\begin{tabular}{ ");
    for (int i = 0; i < cols; i++) {
      // page.append("p{4cm} ");
      page.append("V ");
    }
    return page.append("}\n");
  }

  private StringBuilder startPage(StringBuilder page, int cols) {
    //startLongTable(page, cols);
    startTabular(page, cols);
    return page;
  }

  private void finishPage(StringBuilder baseBuilder, StringBuilder page) {
    baseBuilder.append(page);
  }

  private StringBuilder finishPageWithTrailer(
      StringBuilder baseBuilder,
      StringBuilder page) {
    //return endLongTable(baseBuilder.append(page));
    return endTabular(baseBuilder.append(page));
  }

  private StringBuilder generateHeader(
      StringBuilder builder,
      String title,
      String author)  {
    builder
        .append("\\documentclass[10pt]{book}\n")
        .append("\\usepackage{amsmath,amssymb,graphicx,bbding,pifont}\n")
        .append("\\usepackage{longtable, array}\n")
        .append("\n")
        .append("\\newenvironment{gotitle}\n")
        .append("{\\begin{center}\\LARGE{}}\n")
        .append("{\\\\\\rule{10cm}{1.pt}\\end{center}}\n")
        .append("\n")
        .append("\\newcolumntype{V}{>{\\centering")
            .append("\\arraybackslash} p{.35\\linewidth}}\n")
        .append("\n")
        .append("\\newenvironment{littletitle}\n")
        .append("{\\begin{center}\\large{}}\n")
        .append("{\\end{center}}\n")
        .append("\n")
        .append("\\title{ \\Huge\\textsf{{").append(title).append("}}}\n")
        .append("\\date{}\n");
    if (author != null ) {
      builder.append("\\author{\\textsc{by ")
          .append(author)
          .append("}}\n");
    }
    return builder
        .append("\n")
        .append("\\begin{document}\n")
        .append("\n")
        .append("\\maketitle\n")
        .append("\n");
  }

  private void generateFooter(StringBuilder builder) {
    builder.append("\\end{document}\n");
  }
}
