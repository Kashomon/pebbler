package pebbler.gui;

import pebbler.project.PebblerProject;

import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class ResourceList extends JTable {

  DefaultTableModel model;

  public ResourceList(String[][] filesParams, String[] headers, DefaultTableModel defModel) {
    super(defModel);
    this.model = defModel;
    model.setColumnIdentifiers(headers);
    for (int i = 0; i < filesParams.length; i++) {
      model.addRow(filesParams[i]);
    }
    setShowHorizontalLines(false);
    setShowVerticalLines(false);
  }

  public void addFilesOrDir(Component parent) {
    final JFileChooser resfc = new JFileChooser();
    resfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

    int returnVal = resfc.showOpenDialog(parent);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = resfc.getSelectedFile();

      if (file.isDirectory()) {

        List<File> listing =  Arrays.asList(file.listFiles());
        for (File lf: listing) {
          if (file.getName().endsWith(".sgf")) {
            addEntry(file.getAbsolutePath());
          }
        }

      } else if (file.isFile()) {
        if (file.getName().endsWith(".sgf")) {
          addEntry(file.getAbsolutePath());
        } }
    } else {
      return;
    }
  }

  public void deleteSelectedRows() {
    int[] rows = this.getSelectedRows();
    for (int i = 0; i < rows.length; i++){
      model.removeRow(rows[i] - i);
    }
  }

  public void addEntry(String fileLoc) {
    String[] row = { fileLoc, "Problem" };
    this.model.addRow(row);
  }

  public void addEntry(String fileLoc, String type) {
    String[] row = { fileLoc, type };
    this.model.addRow(row);
  }

  public void loadProject(String location) {
    // pebblerProject.load(location);
    throw new RuntimeException("Unsupported Operation");
  }

  public void saveProject(String location) {
    throw new RuntimeException("Unsupported Operation");
  }

}
