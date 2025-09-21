import java.awt.*;
import java.util.List;

import javax.swing.*;

public class Block extends JPanel {
	private List<Port> inputs;
	private List<Port> outputs;
	
	public Block(String title, int nbInputs, int nbOutputs) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	    setBackground(new Color(230, 230, 250));
	    
	    // Block title
	    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Left panel containing inputs
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        for (int i = 0; i < nbInputs; i++) {
            Port p = new Port(true);
            inputs.add(p);
            leftPanel.add(p);
        }
        
        // Right panel containing outputs
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        for (int i = 0; i < nbOutputs; i++) {
            Port p = new Port(false);
            outputs.add(p);
            rightPanel.add(p);
        }

        // Add them to this
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
	}
	
	 public List<Port> getInputs() {
		 return inputs;
	 }
	 
	 public List<Port> getOutputs() {
		 return outputs;
	 }
}
