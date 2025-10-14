package ui;

import blocks.BlocksPanel;
import codeGenerator.CodeGenerator;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import settings.Settings;

public class SideBarPanel extends JPanel {
    VariableCreatorPanel variablesPanel;

    public SideBarPanel() {
        setPreferredSize(new Dimension(200, 600));
        setBackground(Settings.getInstance().secondaryColor);
        setOpaque(true);
        setLayout(new BorderLayout());

        BlocksPanel blocksPanel = new BlocksPanel();
        variablesPanel = new VariableCreatorPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blocksPanel, variablesPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
        splitPane.setBackground(Settings.getInstance().tertiaryColor);

        Color dividerColor = Settings.getInstance().tertiaryColor;
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                BasicSplitPaneDivider divider = super.createDefaultDivider();
                divider.setBackground(dividerColor);
                divider.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, dividerColor.darker()));
                return divider;
            }
        });

        add(splitPane);
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        variablesPanel.setCodeGenerator(codeGenerator);
    }
}
