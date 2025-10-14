package blocks;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import settings.Settings;

public class BlocksPanel extends JPanel {
    // Actions
    private final JPanel debugBlock;
    private final JPanel setVarBlock;
    private final JPanel newObjectBlock;

    // Events
    private final JPanel onStartBlock;
    private final JPanel onFrame;
    private final JPanel onSpacePressed;
    private final JPanel onLeftPressed;
    private final JPanel onRightPressed; 
    private final JPanel onUpPressed;
    private final JPanel onDownPressed;

    // Intermediary
    private final JPanel addBlock;
    private final JPanel subBlock;
    private final JPanel multBlock;
    private final JPanel divBlock;
    private final JPanel convertBlock;

    // Logic
    private final JPanel ifBlock;
    private final JPanel ifElseBlock;

    // Condition
    private final JPanel notBlock;
    private final JPanel andBlock;
    private final JPanel orBlock;
    private final JPanel xorBlock;
    private final JPanel inferiorBlock;
    private final JPanel inferiorStrictBlock;
    private final JPanel equalsBlock;

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
        onStartBlock = createBlockPanel("On Start", BlockType.Event, List.of(), List.of("Start"));
        onFrame = createBlockPanel("On Frame", BlockType.Event, List.of(), List.of("Frame"));
        onSpacePressed = createBlockPanel("On KeyPressed (Space)", BlockType.Event, List.of(), List.of("Space"));
        onLeftPressed = createBlockPanel("On KeyPressed (Left)", BlockType.Event, List.of(), List.of("Left"));
        onRightPressed = createBlockPanel("On KeyPressed (Right)", BlockType.Event, List.of(), List.of("Right"));
        onUpPressed = createBlockPanel("On KeyPressed (Up)", BlockType.Event, List.of(), List.of("Up"));
        onDownPressed = createBlockPanel("On KeyPressed (Down)", BlockType.Event, List.of(), List.of("Down"));

        // action
        debugBlock = createBlockPanel("Debug Block", BlockType.Action, List.of("In","content"), List.of("Out"));
        setVarBlock = createBlockPanel("Set Variable", BlockType.Action, List.of("In", "Name","Value"), List.of("Out"));
        newObjectBlock = createBlockPanel("Create Object", BlockType.Action, List.of("In", "X", "Y", "Width", "Height"), List.of("Out"));

        // intermediary
        addBlock = createBlockPanel("Add", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        subBlock = createBlockPanel("Substract", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        multBlock = createBlockPanel("Multiply", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        divBlock = createBlockPanel("Divide", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));
        convertBlock = createBlockPanel("Convert", BlockType.Intermediary, List.of("Value", "Value"), List.of("Out"));

        // logic
        ifBlock = createBlockPanel("If Block", BlockType.Logic, List.of("In", "Condition"), List.of("Then"));
        ifElseBlock = createBlockPanel("If Else Block", BlockType.Logic, List.of("In", "Condition"), List.of("Then", "Else"));

        // condition
        notBlock = createBlockPanel("Not", BlockType.Condition, List.of("Condition"), List.of("Out"));
        andBlock = createBlockPanel("And", BlockType.Condition, List.of("Condition", "Condition"), List.of("Out"));
        orBlock = createBlockPanel("Or", BlockType.Condition, List.of("Condition", "Condition"), List.of("Out"));
        xorBlock = createBlockPanel("Xor", BlockType.Condition, List.of("Condition", "Condition"), List.of("Out"));
        inferiorBlock = createBlockPanel("Inferior to", BlockType.Condition, List.of("Value", "Value"), List.of("Out"));
        inferiorStrictBlock = createBlockPanel("Strictly Inferior to", BlockType.Condition, List.of("Value", "Value"), List.of("Out"));
        equalsBlock = createBlockPanel("Equals", BlockType.Condition, List.of("Value", "Value"), List.of("Out"));


        // Add all blocks to the model
        filterBlocks(null);

        JScrollPane scrollPane = new JScrollPane(blockJList);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        scrollPane.setOpaque(false);
        
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void filterBlocks(BlockType type) {
       model.clear();
       if (type != null) {
            switch (type) {
                case Event -> {
                    model.addElement((Block)onStartBlock.getComponent(0));
                    model.addElement((Block)onFrame.getComponent(0));
                    model.addElement((Block)onSpacePressed.getComponent(0));
                    model.addElement((Block)onLeftPressed.getComponent(0));
                    model.addElement((Block)onRightPressed.getComponent(0));
                    model.addElement((Block)onUpPressed.getComponent(0));
                    model.addElement((Block)onDownPressed.getComponent(0));
                }
                case Intermediary -> {
                    model.addElement((Block)addBlock.getComponent(0));
                    model.addElement((Block)subBlock.getComponent(0));
                    model.addElement((Block)multBlock.getComponent(0));
                    model.addElement((Block)divBlock.getComponent(0));
                    model.addElement((Block)convertBlock.getComponent(0));
                }
                case Action -> {
                    model.addElement((Block)debugBlock.getComponent(0));
                    model.addElement((Block)setVarBlock.getComponent(0));
                    model.addElement((Block)newObjectBlock.getComponent(0));
                }
                case Logic -> {
                    model.addElement((Block)ifBlock.getComponent(0));
                    model.addElement((Block)ifElseBlock.getComponent(0));

                    model.addElement((Block)notBlock.getComponent(0));
                    model.addElement((Block)andBlock.getComponent(0));
                    model.addElement((Block)orBlock.getComponent(0));
                    model.addElement((Block)xorBlock.getComponent(0));
                    model.addElement((Block)equalsBlock.getComponent(0));
                    model.addElement((Block)inferiorBlock.getComponent(0));
                    model.addElement((Block)inferiorStrictBlock.getComponent(0));
                }
                default -> {
                }
            }
       } else {
            // Events
            model.addElement((Block)onStartBlock.getComponent(0));
            model.addElement((Block)onFrame.getComponent(0));
            model.addElement((Block)onSpacePressed.getComponent(0));
            model.addElement((Block)onLeftPressed.getComponent(0));
            model.addElement((Block)onRightPressed.getComponent(0));
            model.addElement((Block)onUpPressed.getComponent(0));
            model.addElement((Block)onDownPressed.getComponent(0));
            // Intermediary
            model.addElement((Block)addBlock.getComponent(0));
            model.addElement((Block)subBlock.getComponent(0));
            model.addElement((Block)multBlock.getComponent(0));
            model.addElement((Block)divBlock.getComponent(0));
            model.addElement((Block)convertBlock.getComponent(0));
            // Actions
            model.addElement((Block)debugBlock.getComponent(0));
            model.addElement((Block)setVarBlock.getComponent(0));
            model.addElement((Block)newObjectBlock.getComponent(0));
            // Logic
            model.addElement((Block)ifBlock.getComponent(0));
            model.addElement((Block)ifElseBlock.getComponent(0));
            // Condition
            model.addElement((Block)notBlock.getComponent(0));
            model.addElement((Block)andBlock.getComponent(0));
            model.addElement((Block)orBlock.getComponent(0));
            model.addElement((Block)xorBlock.getComponent(0));
            model.addElement((Block)equalsBlock.getComponent(0));
            model.addElement((Block)inferiorBlock.getComponent(0));
            model.addElement((Block)inferiorStrictBlock.getComponent(0));
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

    private JPanel createBlockPanel(String title, BlockType type, List<String> inputs, List<String> outputs) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        Block block = new Block(title, type, inputs, outputs);
        // Ajouter un padding au block
        block.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        panel.add(block, BorderLayout.CENTER);
        return panel;
    }

}
