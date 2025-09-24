import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class Block extends JPanel {
    private final String title;
	private final List<Port> inputs;
	private final List<Port> outputs;
    private final BlockType type;

	public Block(String title, BlockType type, int nbInputs, int nbOutputs) {
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
        this.title = title;
        this.type = type;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	    setBackground(new Color(230, 230, 250));
	    
	    // Block title
	    JLabel titleLabel = new JLabel(title);
        add(titleLabel, BorderLayout.NORTH);
        
        // Left panel containing inputs
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(0, 1, 0, 5));
        leftPanel.setOpaque(false);

        for (int i = 0; i < nbInputs; i++) {
            var isActivationPort = (i==0 && type == BlockType.Action);
            Port p = new Port(this, true, "i_" + i, isActivationPort);
            inputs.add(p);
            leftPanel.add(p);
        }
        
        // Right panel containing outputs
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1, 0, 5));
        rightPanel.setOpaque(false);

        for (int i = 0; i < nbOutputs; i++) {
            var isActivationPort = (i==0 && (type == BlockType.Action || type == BlockType.Event));
            Port p = new Port(this, false, "j_" + i, isActivationPort);
            outputs.add(p);
            rightPanel.add(p);
        }

        // Wrap them so that they keep their preferred size instead of stretching
        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftWrapper.setOpaque(false);
        leftWrapper.add(leftPanel);

        JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightWrapper.setOpaque(false);
        rightWrapper.add(rightPanel);

        add(leftWrapper, BorderLayout.WEST);
        add(rightWrapper, BorderLayout.EAST);
	}
	
	 public List<Port> getInputs() {
		 return inputs;
	 }
	 
	 public List<Port> getOutputs() {
		 return outputs;
	 }

     public BlockType getType() {
        return type;
     }


     // --- Import the code to the generated file ---
    public String getCode() {
        System.out.println(title);
        switch (title) {
            case "Debug Block" -> {
                return "System.out.println(\"hey from the debug block!\");\n        %s";
            }
            case "Set Message" -> {
                String message = inputs.get(1).getDefaultValue();
                return "label.setText(\"" + message + "\");\n       %s";
            }
            case "On Start" -> {
                return "System.out.println(\"Starting the chain of blocks...\");\n      %s";
            }
            default -> {
                return "System.out.println(\"hey from another block!\");\n      %s";
            }
        }
    }

    public List<Block> getNextBlocks() {
        List<Block> nextBlocks = new ArrayList<>();
        
        for (var output : outputs) {
            var nb = output.getConnectedBlock();
            if (nb != null) nextBlocks.add(nb);
        }

        return nextBlocks;
    }

}
