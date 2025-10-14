package blocks;

import java.awt.*;
import javax.swing.*;
import settings.Settings;

public class BlockRenderer implements ListCellRenderer<Block> {
    @Override
    public Component getListCellRendererComponent(
            JList<? extends Block> list,
            Block block,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {



                
        // Selection Highlight
        if (isSelected) {
            // Use a custom rounded border for selection
            block.setBorder(new RoundedBorder(list.getSelectionBackground(), 2, 15));
            block.setBackground(list.getSelectionBackground());
        } else {
            // Restore the original border and background
            block.setBorder(new RoundedBorder(block.getBorderColor(block.getType()), 2, 15));
            block.setBackground(Settings.getInstance().baseColor);
        }

        return block;
    }
}

