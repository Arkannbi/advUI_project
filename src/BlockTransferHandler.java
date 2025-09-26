import java.awt.Point;
import java.awt.datatransfer.*;
import java.io.IOException;
import javax.swing.*;

// Custom TransferHandler for enabling drag-and-drop of Block from the toolboxPanel (the sidebar) and the canvas.
public class BlockTransferHandler extends TransferHandler {

    // Custom DataFlavor to identify Block objects during transfer
    private static final DataFlavor BLOCK_FLAVOR =
            new DataFlavor(Block.class, "Block");

    
    //  Inner class to wrap a Block object for transfer.     
    private static class BlockTransferable implements Transferable {
        private final Block block;

        public BlockTransferable(Block block) {
            this.block = block;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            // Only BLOCK_FLAVOR is supported
            return new DataFlavor[]{BLOCK_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            // Check if the requested flavor is supported
            return BLOCK_FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            // Return the Block object if the flavor is supported
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return block;
        }
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        // Create a Transferable from the selected Block in a JList
        if (c instanceof JList<?>) {
            @SuppressWarnings("unchecked")
            JList<Block> list = (JList<Block>) c;
            Block selected = list.getSelectedValue();
            if (selected != null) {
                return new BlockTransferable(selected);
            }
        }
        return null;
    }

    @Override
    public int getSourceActions(JComponent c) {
        // The blocks from the sidebar are to be copied and not just moved
        return COPY;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        // Check if the drop target can accept the Block data
        return support.isDataFlavorSupported(BLOCK_FLAVOR);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    // Handle the actual data import (drop) operation
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        try {
            // Extract the Block object from the Transferable
            Transferable t = support.getTransferable();
            Block originalBlock = (Block) t.getTransferData(BLOCK_FLAVOR);

            // Create a new instance of Block with the same properties
            String title = ((JLabel) originalBlock.getComponent(0)).getText();
            BlockType type = originalBlock.getType();
            int nbInputs = originalBlock.getInputs().size();
            int nbOutputs = originalBlock.getOutputs().size();
            Block newBlock = new Block(title, type, nbInputs, nbOutputs);

            // Add the new Block to the Canvas at the drop location
            JComponent target = (JComponent) support.getComponent();
            if (target instanceof Canvas canvas) {
                Point dropPoint = support.getDropLocation().getDropPoint();
                canvas.addBlock(newBlock, dropPoint.x, dropPoint.y);
            } else {
                return false;
            }
            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
