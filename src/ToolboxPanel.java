import java.awt.*;
import javax.swing.*;

public class ToolboxPanel extends JPanel {
    private final JList<Block> blockJList;
    private final Block debugBlock;
    private final Block emptyBlock;
    

    public ToolboxPanel() {
        setMinimumSize(new Dimension(150, 600));
        setBackground(Color.WHITE);
        setOpaque(true);

        blockJList = new JList<>();
        // Set the custom cell renderer here
        blockJList.setCellRenderer(new BlockRenderer());

        DefaultListModel<Block> model = new DefaultListModel<>();

        debugBlock = new Block("Debug Block", 1, 1);
        model.addElement(debugBlock);

        emptyBlock = new Block("Empty Block", 1, 1);
        model.addElement(emptyBlock);

        blockJList.setModel(model);
        blockJList.setDragEnabled(true);
        blockJList.setTransferHandler(new BlockTransferHandler());
        blockJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(blockJList);
    }
}