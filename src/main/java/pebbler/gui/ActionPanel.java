package pebbler.gui;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Component;

import javax.swing.JButton; 
import javax.swing.JPanel; 
import javax.swing.JTextField; 
import javax.swing.JList;
import javax.swing.ImageIcon;

import java.io.File;

public class ActionPanel extends JPanel { 

  ResourceList resList;

  public ActionPanel(ResourceList resourceList) {
    super(new FlowLayout(FlowLayout.CENTER, 0, 0));
    this.resList = resourceList;

    ImageIcon addIcon = new ImageIcon(InitializeGui.class.getResource("/media/Gnome-colors-list-add.png"));
    final JButton addButton = new JButton(addIcon);
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        resList.addFilesOrDir(addButton);
      }   
    }); 
    this.add(addButton);

    ImageIcon deleteIcon = new ImageIcon(InitializeGui.class.getResource("/media/Edit-delete.png"));
    final JButton deleteButton = new JButton(deleteIcon);
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        resList.deleteSelectedRows();
      }   
    }); 
    this.add(deleteButton);
  }
}
