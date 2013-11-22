// Copyright (c) 2011 by Joshua Hoak
// Licensed under the MIT License

package pebbler;

import static pebbler.util.GoFileUtil.silentRead;

import pebbler.gui.InitializeGui;
import pebbler.project.PebblerProject;
import pebbler.project.TypeSetter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The entry point for Pebbler.
 *
 */
public class StartPebbler {

  public static void main(String[] args) {
    if (args.length <= 0) {
      System.out.println("At least one arg (pebbler project) must be" +
          "specified");
    }

    // Quarter-assed flag parsing.
    String projectFileName = args[0];
    String nextFlag = args.length > 1 ? args[1] : "";
    boolean createBook = nextFlag.equals("--createBook") ? true : false;

    File projectFile = new File(projectFileName);
    if (!projectFile.exists() || !projectFile.isFile()) {
      System.out.println("Couldn't find file: " + projectFile.getPath());
      System.exit(-1);
    }
    System.out.println("Getting file from: " + projectFile.getPath());
    PebblerProject project = PebblerProject.fromJson(
        silentRead(projectFile));

    File fullFile = new File(projectFile.getAbsolutePath());
    String fullPath = fullFile.getParentFile().getAbsolutePath();
    project.compile(fullPath);

    if (createBook) {
      TypeSetter typeSetter = new TypeSetter(project);
      typeSetter.createBook(fullPath);
    }
  }
}
