package pebbler.gui;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.JFileChooser;

import java.net.URL;
import java.io.File;

import pebbler.*;


public class PebblerMenuBar extends JMenuBar {

  //public static String media = "media/";
  private ResourceList resList;

  public PebblerMenuBar(ResourceList resourceList) { 
    super();
    this.resList = resourceList;

    // File Menu 
    JMenu file = new JMenu("File"); 
    file.setMnemonic(KeyEvent.VK_F);

    // Open 
    ImageIcon openIcon = new ImageIcon(InitializeGui.class.getResource("/media/Folder-open-scaled2.png"));
    final JMenuItem openMenuItem = new JMenuItem("Add File/Dir", openIcon);
    openMenuItem.setToolTipText("Open Go File and add to processing list");
    openMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        resList.addFilesOrDir(openMenuItem);
      }
    });
    file.add(openMenuItem);
    
    // Exit 
    ImageIcon exitIcon = new ImageIcon(InitializeGui.class.getResource("/media/Gnome-colors-window-close-scaled.png"));
    final JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
    exitMenuItem.setToolTipText("Exit Pebbler");
    exitMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });
    file.add(exitMenuItem);
    


    this.add(file);
  }

}
 
