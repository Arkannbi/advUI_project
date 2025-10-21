package ui;

import codeGenerator.CodeGenerator;
import java.awt.*;
import javax.swing.*;
import settings.Settings;

public class HeaderPanel extends JPanel {

    private final JButton runCodeButton;
    private final JButton loadButton;
    private final JButton saveButton;
    private final JButton exportButton;
    private final JButton clearButton;

    private final Canvas canvas;

    private CodeGenerator codeGenerator;

    public HeaderPanel(Canvas canvas) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setPreferredSize(new Dimension(800, 50));
        setBackground(Settings.getInstance().baseColor);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Settings.getInstance().tertiaryColor));
        setOpaque(true);

        // Initialisation des boutons
        runCodeButton = new JButton("Run Code");
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        exportButton = new JButton("Export");
        clearButton = new JButton("Clear Code");

        this.canvas = canvas;

        Color textColor = Settings.getInstance().textColor;

        JButton[] buttons = { runCodeButton, loadButton, saveButton, exportButton, clearButton };
        for (JButton button : buttons) {
            button.setBackground(Settings.getInstance().secondaryColor);
            button.setForeground(textColor);
            add(button);
        }
        runCodeButton.setBackground(Settings.getInstance().buttonColor);
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;

        runCodeButton.addActionListener(e -> codeGenerator.runCode());

        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Project");
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                saveProject(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Load Project");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                loadProject(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        exportButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Export Project");
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                exportProject(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        clearButton.addActionListener(e -> {
            canvas.restart();
        });
    }

    private void saveProject(String filepath) {
        if (codeGenerator != null) {
            codeGenerator.saveProject(filepath);
        }
    }

    private void loadProject(String filepath) {
        if (codeGenerator != null) {
            codeGenerator.loadProject(filepath);
        }
    }

    private void exportProject(String dirPath) {
        if (codeGenerator != null) {
            codeGenerator.exportProject(dirPath, "GameRunner");
        }
    }
}
