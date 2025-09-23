import java.awt.*;
import javax.swing.*;

public class ToolboxPanel extends JPanel {
    private final JList<Block> blockList;
    private final Block debugBlock;
    private final Block emptyBlock;
    private MainController controller;

    public ToolboxPanel() {
        setMinimumSize(new Dimension(150, 600));
        setBackground(Color.WHITE);
        setOpaque(true);

        blockList = new JList<>();
        // Set the custom cell renderer here
        blockList.setCellRenderer(new BlockRenderer());

        DefaultListModel<Block> model = new DefaultListModel<>();

        debugBlock = new Block("Debug Block", 1, 1);
        model.addElement(debugBlock);

        emptyBlock = new Block("Empty Block", 1, 1);
        model.addElement(emptyBlock);

        blockList.setModel(model);
        blockList.setDragEnabled(true);
        blockList.setTransferHandler(new BlockTransferHandler());
        blockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(blockList);
    }
    
    public void setController(MainController controller) {
        this.controller = controller;
        // debugBlock.addActionListener(_ -> controller.addDebugBlock());
    }
}