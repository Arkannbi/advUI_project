package ui;
import blocks.BlocksPanel;
import codeGenerator.CodeGenerator;
import java.awt.*;
import javax.swing.*;
import settings.Settings;

public class SideBarPanel extends JPanel {
    VariableCreatorPanel variablesPanel;

    public SideBarPanel() {
        setPreferredSize(new Dimension(200, 600));
        setBackground(Settings.getInstance().secondaryColor);
        setOpaque(true);
        setLayout(new BorderLayout());
        // setBorder(BorderFactory.createBevelBorder(1, Color.gray, Color.white));

        BlocksPanel blocksPanel = new BlocksPanel();
        variablesPanel = new VariableCreatorPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blocksPanel, variablesPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
        splitPane.setOpaque(true);
        splitPane.setBackground(Settings.getInstance().baseColor);

        add(splitPane);
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        variablesPanel.setCodeGenerator(codeGenerator);
    }
}