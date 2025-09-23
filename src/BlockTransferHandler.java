import java.awt.Point;
import java.awt.datatransfer.*;
import java.io.IOException;
import javax.swing.*;

public class BlockTransferHandler extends TransferHandler {
    private static final DataFlavor TOOL_BLOCK_FLAVOR =
            new DataFlavor(Block.class, "ToolBlock");

    private static class BlockTransferable implements Transferable {
        private final Block block;

        public BlockTransferable(Block block) {
            this.block = block;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{TOOL_BLOCK_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return TOOL_BLOCK_FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return block;
        }
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
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
        return COPY;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(TOOL_BLOCK_FLAVOR);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean importData(TransferSupport support) {
        
        if (!canImport(support)) {
            return false;
        }

        try {
            Transferable t = support.getTransferable();
            Block originalBlock = (Block) t.getTransferData(TOOL_BLOCK_FLAVOR);

            // Create a NEW instance of Block
            String title = ((JLabel) originalBlock.getComponent(0)).getText();
            int nbInputs = originalBlock.getInputs().size();
            int nbOutputs = originalBlock.getOutputs().size();

            Block newBlock = new Block(title, nbInputs, nbOutputs);

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
