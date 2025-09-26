import java.awt.*;
import javax.swing.*;

public class SideBarPanel extends JPanel {
    VariableCreatorPanel variablesPanel;

    public SideBarPanel() {
        setPreferredSize(new Dimension(150, 600));
        setBackground(Color.WHITE);
        setOpaque(true);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        BlocksPanel blocksPanel = new BlocksPanel();
        gbc.weighty = 0.6;
        add(blocksPanel, gbc);

        gbc.gridy = 1;
        variablesPanel = new VariableCreatorPanel();
        gbc.weighty = 0.4;
        add(variablesPanel, gbc);
    }

    public void setController(MainController controller) {
        variablesPanel.setController(controller);
    }
}