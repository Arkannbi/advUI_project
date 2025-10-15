package ui;
import blocks.Block;
import codeGenerator.CodeGenerator;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {

    private final SideBarPanel sidebarPanel;
    private final Canvas canvas;
    private final HeaderPanel headerPanel;
    private final ConsolePanel consolePanel;

    public MainFrame() {
        setTitle("No-Code Platformer Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.sidebarPanel = new SideBarPanel();
        this.canvas = new Canvas();
        this.headerPanel = new HeaderPanel();
        this.consolePanel = new ConsolePanel();

        // Create a center container with its own BorderLayout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(canvas, BorderLayout.CENTER);
        centerPanel.add(consolePanel, BorderLayout.SOUTH);

        add(sidebarPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        headerPanel.setCodeGenerator(codeGenerator);
        sidebarPanel.setCodeGenerator(codeGenerator);
    }

    public void showLog(String log) {
        consolePanel.appendLog(log);
    }

    public List<Block> getCanvasBlocks() {
        return canvas.getBlocks();
    }
}
