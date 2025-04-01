package ui.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.KeyEvent;
import static ui.UIStyles.*;

public class JNumberTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    public JNumberTextField(String value){
        super(value);
        super.setBackground(TEXT_FIELD_BACKGROUND_COLOR);
        super.setForeground(MAIN_TEXT_COLOR);
        super.setBorder(new LineBorder(TEXT_FIELD_BORDER_COLOR, 1));
        super.setCaretColor(MAIN_TEXT_COLOR);
    }

    @Override
    public void processKeyEvent(KeyEvent ev) {

        char c = ev.getKeyChar();

        if ((Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            super.processKeyEvent(ev);
        }
        ev.consume();
    }

    /**
     * As the user is not even able to enter a dot ("."), only integers (whole numbers) may be entered.
     */
    public int getNumber() {
        int result;
        String text = getText();
        if (text != null && !"".equals(text)) {
            result = Integer.parseInt(text);
        } else {
            result = 0;
        }
        return result;
    }
}
