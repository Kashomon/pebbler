package pebbler.gui;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;

import java.io.File;

import pebbler.project.PebblerProject;


public class PebblerConvert extends JButton {
  private final ResourceList resList;
  private final JTextField outLoc;

  public PebblerConvert(ResourceList resourceList, JTextField outLocation) {
    super("Convert to PDFs!");
    this.resList = resourceList;
    this.outLoc  = outLocation;
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        //PebblerProject project = resList.getProject();
        //SGFConvert.convertProject(project, outLoc.toString());
      }
    });
  }


}

