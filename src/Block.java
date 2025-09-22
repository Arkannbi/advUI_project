import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Block extends JPanel {
	private List<Port> inputs;
	private List<Port> outputs;
	
	public Block(String title, int nbInputs, int nbOutputs) {
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	    setBackground(new Color(230, 230, 250));
	    
	    // Block title
	    JLabel titleLabel = new JLabel(title);
        add(titleLabel, BorderLayout.NORTH);
        
        // Left panel containing inputs
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        for (int i = 0; i < nbInputs; i++) {
            Port p = new Port(true, "i_" + i);
            inputs.add(p);
            leftPanel.add(p);
        }
        
        // Right panel containing outputs
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        for (int i = 0; i < nbOutputs; i++) {
            Port p = new Port(false, "j_" + i);
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
}
