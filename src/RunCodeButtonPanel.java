import java.awt.*;
import javax.swing.*;

public class RunCodeButtonPanel extends JPanel {

    private final JButton runCodeButton;

    public RunCodeButtonPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 50));

        runCodeButton = new JButton("Run Code");
        add(runCodeButton, BorderLayout.CENTER);
    }

    public void setController(CodeGenerator controller) {
        runCodeButton.addActionListener(_ -> controller.runCode());
    }
}