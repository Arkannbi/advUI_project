package ui;
import codeGenerator.CodeGenerator;
import java.awt.*;
import javax.swing.*;
import settings.Settings;

public class HeaderPanel extends JPanel {

    private final JButton runCodeButton;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 50));

        runCodeButton = new JButton("Run Code");
        runCodeButton.setBackground(Settings.getInstance().buttonColor);
        runCodeButton.setForeground(Settings.getInstance().textColor);
        add(runCodeButton, BorderLayout.CENTER);
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        runCodeButton.addActionListener(e -> codeGenerator.runCode());
    }
}