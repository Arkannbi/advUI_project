import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {

    private final ToolboxPanel toolboxPanel;
    private final Canvas canvas;
    private final RunCodeButtonPanel buttonPanel;
    private final ConsolePanel consolePanel;

    private MainController controller;

    public MainFrame() {
        setTitle("No-Code Platformer Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create the individual panels
        this.toolboxPanel = new ToolboxPanel();
        this.canvas = new Canvas();
        this.buttonPanel = new RunCodeButtonPanel();
        this.consolePanel = new ConsolePanel();

        // Canvas
        // canvas.setMainController(controller);
        Block block1 = new Block("Addition", BlockType.Intermediary, 7, 1);
        Block block2 = new Block("Multiply", BlockType.Intermediary, 2, 1);

        // Blocks on the canva
        canvas.addBlock(block1, 50, 100);
        canvas.addBlock(block2, 400, 200);

        // Add the panels to the frame
        add(toolboxPanel, BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        add(consolePanel, BorderLayout.SOUTH);

    }

    public void setController(MainController controller) {
        buttonPanel.setController(controller);
    }

    public void updateCodeDisplay(String code) {
        // canvas.updateCodeDisplay(code);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Execution Output", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLog(String log) {
        consolePanel.appendLog(log);
    }

    public List<Block> getCanvasBlocks() {
        return canvas.getBlocks();
    }
}