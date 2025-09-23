import java.awt.*;
import javax.swing.*;

public class ConsolePanel extends JPanel {
    private final JTextArea textArea;

    public ConsolePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 150));

        textArea = new JTextArea("\n  >> Welcome to the newest no code game engine!");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to append logs to the text area
    public void appendLog(String log) {
        textArea.append("\n  >> " + log);
    }
}
