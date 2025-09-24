import java.awt.*;
import javax.swing.*;

public class ToolboxPanel extends JPanel {
    private final JList<Block> blockJList;
    private final Block debugBlock;
    private final Block emptyBlock;
    private final Block onStartBlock;
    

    public ToolboxPanel() {
        setMinimumSize(new Dimension(150, 600));
        setBackground(Color.WHITE);
        setOpaque(true);

        blockJList = new JList<>();
        // Set the custom cell renderer here
        blockJList.setCellRenderer(new BlockRenderer());

        DefaultListModel<Block> model = new DefaultListModel<>();

        debugBlock = new Block("Debug Block", BlockType.Action, 1, 1);
        model.addElement(debugBlock);

        emptyBlock = new Block("Set Message", BlockType.Action, 2, 1);
        model.addElement(emptyBlock);

        onStartBlock = new Block("On Start", BlockType.Event, 0, 1);
        model.addElement(onStartBlock);

        blockJList.setModel(model);
        blockJList.setDragEnabled(true);
        blockJList.setTransferHandler(new BlockTransferHandler());
        blockJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(blockJList);
    }
}