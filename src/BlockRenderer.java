import java.awt.Component;
import javax.swing.*;

public class BlockRenderer implements ListCellRenderer<Block> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Block> list, Block block, int index, boolean isSelected, boolean cellHasFocus) {
        // Here, 'block' is the actual Block object from your list model.
        // We'll return this same component.
        
        // This handles the selection highlighting.
        if (isSelected) {
            block.setBorder(BorderFactory.createLineBorder(list.getSelectionBackground(), 2));
            block.setBackground(list.getSelectionBackground());
        } else {
            // Restore the original border and background when not selected.
            block.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 2));
            block.setBackground(new java.awt.Color(230, 230, 250));
        }

        return block;
    }
}