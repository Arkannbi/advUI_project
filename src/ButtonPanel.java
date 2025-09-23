import java.awt.*;
import javax.swing.*;

public class ButtonPanel extends JPanel {

    private final JButton runCodeButton;

    public ButtonPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 50));

        runCodeButton = new JButton("Run Code");
        add(runCodeButton, BorderLayout.CENTER);
    }

    public void setController(MainController controller) {
        runCodeButton.addActionListener(_ -> controller.runCode());
    }
}