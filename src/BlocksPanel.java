
import javax.swing.*;;

public class BlocksPanel extends JPanel{
    // Actions
    private final Block debugBlock;
    private final Block setVarBlock;

    // Events
    private final Block onStartBlock;
    private final Block onSpacePressed;
    private final Block onFrame;

    // Intermediary
    private final Block addBlock;
    private final Block subBlock;
    private final Block multBlock;
    private final Block divBlock;
    

    public BlocksPanel() { 
        JList<Block> blockJList = new JList<>();

        // Set the custom cell renderer here
        blockJList.setCellRenderer(new BlockRenderer());

        DefaultListModel<Block> model = new DefaultListModel<>();

        debugBlock = new Block("Debug Block", BlockType.Action, 2, 1);
        model.addElement(debugBlock);

        setVarBlock = new Block("Set Variable", BlockType.Action, 3, 1);
        model.addElement(setVarBlock);

        onStartBlock = new Block("On Start", BlockType.Event, 0, 1);
        model.addElement(onStartBlock);
        
        onSpacePressed = new Block("On KeyPressed (Space)", BlockType.Event, 0, 1);
        model.addElement(onSpacePressed);

        onFrame = new Block("On Frame", BlockType.Event, 0, 1);
        model.addElement(onFrame);

        addBlock = new Block("Add", BlockType.Intermediary, 2, 1);
        model.addElement(addBlock);
                
        subBlock = new Block("Substract", BlockType.Intermediary, 2, 1);
        model.addElement(subBlock);

        multBlock = new Block("Multiply", BlockType.Intermediary, 2, 1);
        model.addElement(multBlock);

        divBlock = new Block("Divide", BlockType.Intermediary, 2, 1);
        model.addElement(divBlock);

        blockJList.setModel(model);
        blockJList.setDragEnabled(true);
        blockJList.setTransferHandler(new BlockTransferHandler());
        blockJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(blockJList);
    }
}
