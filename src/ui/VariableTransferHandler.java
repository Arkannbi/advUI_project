package ui;

import java.awt.datatransfer.*;
import javax.swing.*;


public class VariableTransferHandler extends TransferHandler {

    private static final DataFlavor VARIABLE_FLAVOR =
            new DataFlavor(String.class, "Variable");

    
    private static class VariableTransferable implements Transferable {
        private final String variable;

        public VariableTransferable(String variable) {
            this.variable = variable;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            // Only VARIABLE_FLAVOR is supported
            return new DataFlavor[]{VARIABLE_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            // Check if the requested flavor is supported
            return VARIABLE_FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            // Return the variable name if the flavor is supported
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            String variableName = "%" + variable.split(" ")[1];
            return variableName;
        }
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        // Create a Transferable from the selected variable name in a JList
        if (c instanceof JList<?>) {
            @SuppressWarnings("unchecked")
            JList<String> list = (JList<String>) c;
            String selected = list.getSelectedValue();
            if (selected != null) {
                return new VariableTransferable(selected);
            }
        }
        return null;
    }

    @Override
    public int getSourceActions(JComponent c) {
        // The variable from the sidebar are to be copied and not just moved
        return COPY;
    }
}