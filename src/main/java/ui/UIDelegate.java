package ui;

import ui.components.JCustomLabel;
import ui.components.JFileButton;
import ui.components.JNumberTextField;
import ui.components.JPathTextField;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import static ui.UIStyles.*;

import java.util.logging.Logger;


public class UIDelegate {
    private static final String START_BUTTON_TEXT = "Start";
    private static final String STOP_BUTTON_TEXT = "Stop";
    private static final String DEFAULT_DELAY_TEXT = "1000";
    private static final JFrame frame = new JFrame("Doctavista");
    private static final JPathTextField sourceDirectoryTextField = new JPathTextField(false);
    private static final JFileButton sourceDirectorySelectButton = new JFileButton();
    private static final JCustomLabel sourceDirectoryLabel = new JCustomLabel("Source directory:");
    private static final JPathTextField resultDirectoryTextField = new JPathTextField(false);
    private static final JFileButton resultDirectorySelectButton = new JFileButton();
    private static final JCustomLabel resultDirectoryLabel = new JCustomLabel("Result directory:");
    private static final JPathTextField structureDirectoryTextField = new JPathTextField(false);
    private static final JFileButton structureDirectorySelectButton = new JFileButton();
    private static final JCustomLabel structureDirectoryLabel = new JCustomLabel("Structure directory:");
    private static final JNumberTextField delayTextField = new JNumberTextField(DEFAULT_DELAY_TEXT);
    private static final JCustomLabel delayLabel = new JCustomLabel("Update delay:");
    private static final JCustomLabel delayDimensionLabel = new JCustomLabel("ms");

    private static final JButton startButton = new JButton(START_BUTTON_TEXT);

    private static final JButton stopButton = new JButton(STOP_BUTTON_TEXT);

    private final Logger log;

    private static UIController controller;


    static {
        frame.setSize(680, 200);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        sourceDirectoryLabel.setBounds(10, 10, 130, 20);
        sourceDirectoryTextField.setBounds(150, 10, 496, 20);
        sourceDirectorySelectButton.setBounds(650, 10, 20, 20);
        sourceDirectorySelectButton.addActionListener(e -> choseFolderAction(sourceDirectoryTextField));

        resultDirectoryLabel.setBounds(10, 40, 130, 20);
        resultDirectoryTextField.setBounds(150, 40, 496, 20);
        resultDirectorySelectButton.setBounds(650, 40, 20, 20);
        resultDirectorySelectButton.addActionListener(e -> choseFolderAction(resultDirectoryTextField));

        structureDirectoryLabel.setBounds(10, 70, 130, 20);
        structureDirectoryTextField.setBounds(150, 70, 496, 20);
        structureDirectorySelectButton.setBounds(650, 70, 20, 20);
        structureDirectorySelectButton.addActionListener(e -> choseFolderAction(structureDirectoryTextField));

        delayLabel.setBounds(10, 100, 130, 20);
        delayDimensionLabel.setBounds(255, 100, 30, 20);
        delayTextField.setBounds(150, 100, 100, 20);


        startButton.setBounds(200, 135, 120, 20);
        startButton.setOpaque(true);
        startButton.setBackground(BUTTON_COLOR);
        startButton.setForeground(BUTTON_TEXT_COLOR);
        startButton.setBorder(new LineBorder(BUTTON_BORDER_COLOR, 1));
        startButton.setFocusPainted(false);

        stopButton.setBounds(340, 135, 120, 20);
        stopButton.setOpaque(true);
        stopButton.setBackground(BUTTON_COLOR);
        stopButton.setForeground(BUTTON_TEXT_COLOR);
        stopButton.setBorder(new LineBorder(BUTTON_BORDER_COLOR, 1));
        stopButton.setFocusPainted(false);
        stopButton.setEnabled(false);

        frame.add(startButton);
        frame.add(stopButton);
        frame.add(structureDirectorySelectButton);
        frame.add(structureDirectoryTextField);
        frame.add(structureDirectoryLabel);

        frame.add(resultDirectorySelectButton);
        frame.add(resultDirectoryTextField);
        frame.add(resultDirectoryLabel);

        frame.add(sourceDirectorySelectButton);
        frame.add(sourceDirectoryTextField);
        frame.add(sourceDirectoryLabel);

        frame.add(delayLabel);
        frame.add(delayTextField);
        frame.add(delayDimensionLabel);
    }

    public UIDelegate(Logger log) {
        this.log = log;
    }

    public void useUI() {

        startButton.addActionListener(e -> start());
        stopButton.addActionListener(e -> stop());
        frame.setLayout(null);
        frame.setVisible(true);

        controller = new UIController(log);
    }


    private static void choseFolderAction(JTextField directoryField) {

        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            directoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        } else
            directoryField.setText("");
    }


    private void start() {

        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        sourceDirectorySelectButton.setEnabled(false);
        resultDirectorySelectButton.setEnabled(false);
        structureDirectorySelectButton.setEnabled(false);

        if (structureDirectoryTextField.getText() == null || structureDirectoryTextField.getText().isEmpty() || delayTextField.getText().isEmpty()) {
            controller.start(sourceDirectoryTextField.getText(), resultDirectoryTextField.getText());

        } else if (structureDirectoryTextField.getText() == null || structureDirectoryTextField.getText().isEmpty()) {
            controller.start(sourceDirectoryTextField.getText(), resultDirectoryTextField.getText(), delayTextField.getNumber());

        } else {
            controller.start(sourceDirectoryTextField.getText(), resultDirectoryTextField.getText(), structureDirectoryTextField.getText(), delayTextField.getNumber());
        }
    }

    private void stop() {

        controller.stop();

        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        sourceDirectorySelectButton.setEnabled(true);
        resultDirectorySelectButton.setEnabled(true);
        structureDirectorySelectButton.setEnabled(true);
    }
}

