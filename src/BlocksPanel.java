import java.awt.*;
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
        onStartBlock = new Block("On Start", BlockType.Event, 0, 1);
        onFrame = new Block("On Frame", BlockType.Event, 0, 1);
        onSpacePressed = new Block("On KeyPressed (Space)", BlockType.Event, 0, 1);
        onLeftPressed = new Block("On KeyPressed (Left)", BlockType.Event, 0, 1);
        onRightPressed = new Block("On KeyPressed (Right)", BlockType.Event, 0, 1);
        onUpPressed = new Block("On KeyPressed (Up)", BlockType.Event, 0, 1);
        onDownPressed = new Block("On KeyPressed (Down)", BlockType.Event, 0, 1);

        debugBlock = new Block("Debug Block", BlockType.Action, 2, 1);
        setVarBlock = new Block("Set Variable", BlockType.Action, 3, 1);

        addBlock = new Block("Add", BlockType.Intermediary, 2, 1);
        subBlock = new Block("Substract", BlockType.Intermediary, 2, 1);
        multBlock = new Block("Multiply", BlockType.Intermediary, 2, 1);
        divBlock = new Block("Divide", BlockType.Intermediary, 2, 1);
        convertBlock = new Block("Convert", BlockType.Intermediary, 2, 1);

        ifBlock = new Block("If Block", BlockType.Logic, 2, 1);
        ifElseBlock = new Block("If Else Block", BlockType.Logic, 2, 2);

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
