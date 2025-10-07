import java.awt.*;
import java.util.List;
import javax.swing.*;

public class BlocksPanel extends JPanel {
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
    private final Block convertBlock;

    // Logic
    private final Block ifBlock;
    private final Block ifElseBlock;

    // Bloc lists
    private final DefaultListModel<Block> model;
    private final JList<Block> blockJList;

    public BlocksPanel() {
        setLayout(new BorderLayout());

    JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 0, 0));

    addFilterButton(buttonPanel, "All Blocks", null, "ressources/icons/all.png");
    addFilterButton(buttonPanel, "Event Blocks", BlockType.Event, "ressources/icons/event.png");
    addFilterButton(buttonPanel, "Itermediary Blocks", BlockType.Intermediary, "ressources/icons/action.png");
    addFilterButton(buttonPanel, "Action Blocks", BlockType.Action, "ressources/icons/intermediary.png");
    addFilterButton(buttonPanel, "Logic Blocks", BlockType.Logic, "ressources/icons/logic.png");

    // Add panel to layout
    add(buttonPanel, BorderLayout.NORTH);


        // JList Setup
        model = new DefaultListModel<>();
        blockJList = new JList<>(model);
        blockJList.setCellRenderer(new BlockRenderer());
        blockJList.setDragEnabled(true);
        blockJList.setTransferHandler(new BlockTransferHandler());
        blockJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize blocks
        onStartBlock = new Block("On Start", BlockType.Event, List.of(), List.of("Start"));
        onFrame = new Block("On Frame", BlockType.Event, List.of(), List.of("On frame"));
        onSpacePressed = new Block("On KeyPressed (Space)", BlockType.Event, List.of(), List.of("Space"));
        onLeftPressed = new Block("On KeyPressed (Left)", BlockType.Event, List.of(), List.of("Left"));
        onRightPressed = new Block("On KeyPressed (Right)", BlockType.Event, List.of(), List.of("Right"));
        onUpPressed = new Block("On KeyPressed (Up)", BlockType.Event, List.of(), List.of("Up"));
        onDownPressed = new Block("On KeyPressed (Down)", BlockType.Event, List.of(), List.of("Down"));

        debugBlock = new Block("Debug Block", BlockType.Action, List.of("In","content"), List.of("Out"));
        setVarBlock = new Block("Set Variable", BlockType.Action, List.of("In", "Variable Name","Value"), List.of("Out"));

        addBlock = new Block("Add", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        subBlock = new Block("Substract", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        multBlock = new Block("Multiply", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        divBlock = new Block("Divide", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        convertBlock = new Block("Convert", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));

        ifBlock = new Block("If Block", BlockType.Logic, List.of("In", "Condition"), List.of("Then"));
        ifElseBlock = new Block("If Else Block", BlockType.Logic, List.of("In", "Condition"), List.of("Then", "Else"));

        // Add all blocks to the model
        filterBlocks(null);

        JScrollPane scrollPane = new JScrollPane(blockJList);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void filterBlocks(BlockType type) {
       model.clear();
       if (type != null) {
            switch (type) {
                case Event -> {
                    model.addElement(onStartBlock);
                    model.addElement(onFrame);
                    model.addElement(onSpacePressed);
                    model.addElement(onLeftPressed);
                    model.addElement(onRightPressed);
                    model.addElement(onUpPressed);
                    model.addElement(onDownPressed);
                }
                case Intermediary -> {
                    model.addElement(addBlock);
                    model.addElement(subBlock);
                    model.addElement(multBlock);
                    model.addElement(divBlock);
                    model.addElement(convertBlock);
                }
                case Action -> {
                    model.addElement(debugBlock);
                    model.addElement(setVarBlock);
                }
                case Logic -> {
                    model.addElement(ifBlock);
                    model.addElement(ifElseBlock);
                }
                default -> {
                }
            }
       } else {
            // Events
            model.addElement(onStartBlock);
            model.addElement(onFrame);
            model.addElement(onSpacePressed);
            model.addElement(onLeftPressed);
            model.addElement(onRightPressed);
            model.addElement(onUpPressed);
            model.addElement(onDownPressed);
            // Intermediary
            model.addElement(addBlock);
            model.addElement(subBlock);
            model.addElement(multBlock);
            model.addElement(divBlock);
            model.addElement(convertBlock);
            // Actions
            model.addElement(debugBlock);
            model.addElement(setVarBlock);
            // Logic
            model.addElement(ifBlock);
            model.addElement(ifElseBlock);
       }
    }

    private void addFilterButton(JPanel buttonPanel, String buttonName, BlockType filterType, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JButton button = new JButton(icon);
        
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));

        button.setPreferredSize(new Dimension(30, 30));
        button.setMaximumSize(new Dimension(30, 30));
        
        button.setToolTipText(buttonName);
        button.addActionListener(_ -> filterBlocks(filterType));

        buttonPanel.add(button);
    }
}
