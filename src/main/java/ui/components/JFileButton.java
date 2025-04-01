package ui.components;

import ui.UIDelegate;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class JFileButton extends JButton {
    public JFileButton(){
        ImageIcon folderIcon = null;
        try {
            URL resource = UIDelegate.class.getClassLoader().getResource("icons/folder.png");
            folderIcon = new ImageIcon(resource.toURI().toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        super.setIcon(folderIcon);
        super.setBorder(null);
    }
}
