package ui;
import codeGenerator.CodeGenerator;
import java.awt.*;
import javax.swing.*;
import settings.Settings;

public class RunCodeButtonPanel extends JPanel {

    private final JButton runCodeButton;

    public RunCodeButtonPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 50));

        runCodeButton = new JButton("Run Code");
        runCodeButton.setBackground(Settings.getInstance().tertiaryColor);
        runCodeButton.setForeground(Settings.getInstance().textColor);
        add(runCodeButton, BorderLayout.CENTER);
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        runCodeButton.addActionListener(e -> codeGenerator.runCode());
    }
}