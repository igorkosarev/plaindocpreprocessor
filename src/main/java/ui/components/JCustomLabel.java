package ui.components;

import javax.swing.*;

import static ui.UIStyles.MAIN_TEXT_COLOR;

public class JCustomLabel extends JLabel {
    public JCustomLabel(String value){
        super(value);
        super.setForeground(MAIN_TEXT_COLOR);
    }
}
