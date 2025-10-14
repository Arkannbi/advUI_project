package ui;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import settings.Settings;

public class ConsolePanel extends JPanel {
    private final JTextArea textArea;

    public ConsolePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 150));


        textArea = new JTextArea("\n  >> Welcome to the newest no code game engine!");
                File font_file = new File("ressources/fonts/CascadiaCode.ttf");
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
            font = font.deriveFont(Font.ITALIC, 12f);
            textArea.setFont(font);
        }
        catch (IOException | FontFormatException e)  {
        	e.printStackTrace();
        }
        textArea.setEditable(false);

        textArea.setBackground(Settings.getInstance().tertiaryColor);
        textArea.setForeground(Settings.getInstance().textColor);
        textArea.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to append logs to the text area
    public void appendLog(String log) {
        textArea.append("\n  >> " + log);
        textArea.setCaretPosition(textArea.getDocument().getLength()); // scrolls to the bottom
    }

}
