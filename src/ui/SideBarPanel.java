package ui;
import blocks.BlocksPanel;
import codeGenerator.CodeGenerator;
import java.awt.*;
import javax.swing.*;

public class SideBarPanel extends JPanel {
    VariableCreatorPanel variablesPanel;

    public SideBarPanel() {
        setPreferredSize(new Dimension(200, 600));
        setBackground(Color.WHITE);
        setOpaque(true);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createBevelBorder(1, Color.gray, Color.white));

        BlocksPanel blocksPanel = new BlocksPanel();
        variablesPanel = new VariableCreatorPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blocksPanel, variablesPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);

        add(splitPane);
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        variablesPanel.setCodeGenerator(codeGenerator);
    }
}