package pebbler.gui;

import pebbler.gui.ResourceList;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class InitializeGui {

  public static void create() {
    EventQueue.invokeLater(new Runnable() {
      public void run () {
        JFrame f = createMainFrame();
        f.setVisible(true);
      }
    });
  }

  public static JFrame createMainFrame() {
    JFrame f = new JFrame("Pebbler");
    JPanel basePanel = new JPanel();
    basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));

    // Resource Initialization
    String[][] filesParams = { {  "../src/main/resources/examples/Kong-Jie_Wang-Xi.sgf", "Problem"} };
    String[] headers = {"Filenames", "Render Options"} ;
    final ResourceList resList = new ResourceList(filesParams, headers, new DefaultTableModel(0, 2));

    // Output location
    JPanel outPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

    JLabel outLabel = new JLabel("Output Location:   ");
    outPanel.add(outLabel);

    JTextField outField = new JTextField(25);
    outField.setText("output/");
    outPanel.add(outField);

    ImageIcon openIcon = new ImageIcon(InitializeGui.class.getResource("/media/Folder-open-scaled2.png"));
    JButton openDir = new JButton(openIcon);
    outPanel.add(openDir);

    basePanel.add(outPanel);
    basePanel.add(Box.createRigidArea(new Dimension(0, 2)));
    // ActionPanel
    ActionPanel actionPanel = new ActionPanel(resList);
    basePanel.add(actionPanel);
    basePanel.add(Box.createRigidArea(new Dimension(0, 2)));

    // Main Resource Display Section
    JScrollPane scroller = new JScrollPane(resList);
    scroller.setPreferredSize(new Dimension(200, 200));
    scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    basePanel.add(scroller);
    basePanel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Convert Button
    JPanel convertPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    JButton pconvert = new PebblerConvert(resList, outField);
    convertPanel.add(pconvert);
    basePanel.add(convertPanel);
    basePanel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Finalizing the JFrame
    f.add(basePanel);
    JMenuBar menubar = new PebblerMenuBar(resList);
    f.setJMenuBar(menubar);
    f.setSize(500,450);
    f.setLocationRelativeTo(null); // needs to be changed
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    return f;
  }

}
