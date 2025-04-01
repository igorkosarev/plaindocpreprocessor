package ui.components;

import javax.swing.*;
import javax.swing.border.LineBorder;

import static ui.UIStyles.*;

public class JPathTextField extends JTextField {
    public JPathTextField(boolean isEditable) {
        super.setBackground(TEXT_FIELD_BACKGROUND_COLOR);
        super.setForeground(MAIN_TEXT_COLOR);
        super.setBorder(new LineBorder(TEXT_FIELD_BORDER_COLOR, 1));
        super.setEditable(isEditable);
    }
}
