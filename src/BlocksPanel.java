import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class BlocksPanel extends JPanel{
    // Actions
    private final Block debugBlock;
    private final Block setVarBlock;

    // Events
    private final Block onStartBlock;
    private final Block onFrame;
    private final Block onSpacePressed;
    private final Block onLeftPressed;
    private final Block onRightPressed;
    private final Block onUpPressed;
    private final Block onDownPressed;

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

        debugBlock = new Block("Debug Block", BlockType.Action, List.of("In", "Str"), List.of("Out")) ;
        model.addElement(debugBlock);

        setVarBlock = new Block("Set Variable", BlockType.Action, List.of("In", "Var", "Value"), List.of("Out"));
        model.addElement(setVarBlock);

        onStartBlock = new Block("On Start", BlockType.Event, List.of(), List.of("Start"));
        model.addElement(onStartBlock);

        onFrame = new Block("On Frame", BlockType.Event, List.of(), List.of("Frame"));
        model.addElement(onFrame);
        
        onSpacePressed = new Block("On KeyPressed (Space)", BlockType.Event, List.of(), List.of("Space"));
        model.addElement(onSpacePressed);
        
        onLeftPressed = new Block("On KeyPressed (Left)", BlockType.Event, List.of(), List.of("Left"));
        model.addElement(onLeftPressed);
        
        onRightPressed = new Block("On KeyPressed (Right)", BlockType.Event, List.of(), List.of("Right"));
        model.addElement(onRightPressed);
        
        onUpPressed = new Block("On KeyPressed (Up)", BlockType.Event, List.of(), List.of("Up"));
        model.addElement(onUpPressed);
        
        onDownPressed = new Block("On KeyPressed (Down)", BlockType.Event, List.of(), List.of("Down"));
        model.addElement(onDownPressed);

        addBlock = new Block("Add", BlockType.Intermediary, List.of("x1", "x2"), List.of("Out"));
        model.addElement(addBlock);
                
        subBlock = new Block("Substract", BlockType.Intermediary, List.of("x1", "x2"), List.of("Out"));
        model.addElement(subBlock);

        multBlock = new Block("Multiply", BlockType.Intermediary, List.of("x1", "x2"), List.of("Out"));
        model.addElement(multBlock);

        divBlock = new Block("Divide", BlockType.Intermediary, List.of("x1", "x2"), List.of("Out"));
        model.addElement(divBlock);

        blockJList.setModel(model);
        blockJList.setDragEnabled(true);
        blockJList.setTransferHandler(new BlockTransferHandler());
        blockJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(blockJList);
    }
}
