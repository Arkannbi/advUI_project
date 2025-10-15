package blocks;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import settings.Settings;

public class BlocksPanel extends JPanel {
    // Actions
    private final Block debugBlock;
    private final Block setVarBlock;
    private final Block newObjectBlock;
    private final Block setObjectVarBlock;

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
    private final Block randomBlock;

    // Logic
    private final Block ifBlock;
    private final Block ifElseBlock;

    // Condition
    private final Block notBlock;
    private final Block andBlock;
    private final Block orBlock;
    private final Block xorBlock;
    private final Block inferiorBlock;
    private final Block inferiorStrictBlock;
    private final Block equalsBlock;

    // Bloc lists
    private final DefaultListModel<Block> model;
    private final JList<Block> blockJList;

    public BlocksPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

	    JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 0, 0));
	    
	    addFilterButton(buttonPanel, "All Blocks", null, "ressources/icons/all.png");
	    addFilterButton(buttonPanel, "Event Blocks", BlockType.Event, "ressources/icons/event.png");
	    addFilterButton(buttonPanel, "Intermediary Blocks", BlockType.Intermediary, "ressources/icons/action.png");
	    addFilterButton(buttonPanel, "Action Blocks", BlockType.Action, "ressources/icons/intermediary.png");
	    addFilterButton(buttonPanel, "Logic Blocks", BlockType.Logic, "ressources/icons/logic.png");
	
	    // Add panel to layout
        buttonPanel.setOpaque(false);
	    add(buttonPanel, BorderLayout.NORTH);


        // JList Setup
        model = new DefaultListModel<>();
        blockJList = new JList<>(model);
        blockJList.setCellRenderer(new BlockRenderer());
        blockJList.setDragEnabled(true);
        blockJList.setTransferHandler(new BlockTransferHandler());
        blockJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        blockJList.setBackground(Settings.getInstance().baseColor);
        blockJList.setOpaque(true);


        // Initialize blocks
        // event
        onStartBlock = new Block("On Start", BlockType.Event, List.of(), List.of("Start"));
        onFrame = new Block("On Frame", BlockType.Event, List.of(), List.of("Frame"));
        onSpacePressed = new Block("On KeyPressed (Space)", BlockType.Event, List.of(), List.of("Space"));
        onLeftPressed = new Block("On KeyPressed (Left)", BlockType.Event, List.of(), List.of("Left"));
        onRightPressed = new Block("On KeyPressed (Right)", BlockType.Event, List.of(), List.of("Right"));
        onUpPressed = new Block("On KeyPressed (Up)", BlockType.Event, List.of(), List.of("Up"));
        onDownPressed = new Block("On KeyPressed (Down)", BlockType.Event, List.of(), List.of("Down"));

        // action
        debugBlock = new Block("Debug Block", BlockType.Action, List.of("In","content"), List.of("Out"));
        setVarBlock = new Block("Set Variable", BlockType.Action, List.of("In", "Name","Value"), List.of("Out"));
        newObjectBlock = new Block("Create Object", BlockType.Action, List.of("In", "Name", "X", "Y", "Width", "Height"), List.of("Out"));
        setObjectVarBlock = new Block("Set Object Variable", BlockType.Action, List.of("In", "Object", "Variable", "Value"), List.of("Out"));

        // intermediary
        addBlock = new Block("Add", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        subBlock = new Block("Substract", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        multBlock = new Block("Multiply", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        divBlock = new Block("Divide", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        convertBlock = new Block("Convert", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        randomBlock = new Block("Random", BlockType.Intermediary, List.of("Min", "Max"), List.of("Out"));

        // logic
        ifBlock = new Block("If Block", BlockType.Logic, List.of("In", "Condition"), List.of("Then"));
        ifElseBlock = new Block("If Else Block", BlockType.Logic, List.of("In", "Condition"), List.of("Then", "Else"));

        // condition
        notBlock = new Block("Not", BlockType.Condition, List.of("Condition"), List.of("Out"));
        andBlock = new Block("And", BlockType.Condition, List.of("Condition", "Condition"), List.of("Out"));
        orBlock = new Block("Or", BlockType.Condition, List.of("Condition", "Condition"), List.of("Out"));
        xorBlock = new Block("Xor", BlockType.Condition, List.of("Condition", "Condition"), List.of("Out"));
        inferiorBlock = new Block("Inferior to", BlockType.Condition, List.of("Value", "Value"), List.of("Out"));
        inferiorStrictBlock = new Block("Strictly Inferior to", BlockType.Condition, List.of("Value", "Value"), List.of("Out"));
        equalsBlock = new Block("Equals", BlockType.Condition, List.of("Value", "Value"), List.of("Out"));


        // Add all blocks to the model
        filterBlocks(null);

        JScrollPane scrollPane = new JScrollPane(blockJList);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        scrollPane.setOpaque(false);
        
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

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
                    model.addElement(randomBlock);
                }
                case Action -> {
                    model.addElement(debugBlock);
                    model.addElement(setVarBlock);
                    model.addElement(newObjectBlock);
                    model.addElement(setObjectVarBlock);
                }
                case Logic -> {
                    model.addElement(ifBlock);
                    model.addElement(ifElseBlock);

                    model.addElement(notBlock);
                    model.addElement(andBlock);
                    model.addElement(orBlock);
                    model.addElement(xorBlock);
                    model.addElement(equalsBlock);
                    model.addElement(inferiorBlock);
                    model.addElement(inferiorStrictBlock);
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
            model.addElement(randomBlock);
            // Actions
            model.addElement(debugBlock);
            model.addElement(setVarBlock);
            model.addElement(newObjectBlock);
            model.addElement(setObjectVarBlock);
            // Logic
            model.addElement(ifBlock);
            model.addElement(ifElseBlock);
            // Condition
            model.addElement(notBlock);
            model.addElement(andBlock);
            model.addElement(orBlock);
            model.addElement(xorBlock);
            model.addElement(equalsBlock);
            model.addElement(inferiorBlock);
            model.addElement(inferiorStrictBlock);
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
        button.addActionListener(e -> filterBlocks(filterType));

        buttonPanel.add(button);
    }
}
