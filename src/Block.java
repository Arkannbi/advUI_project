import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Block extends JPanel {
    private final String title;
	private final List<Port> inputs;
	private final List<Port> outputs;

	public Block(String title, int nbInputs, int nbOutputs) {
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
        this.title = title;
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
            Port p = new Port(true, "i_" + i);
            inputs.add(p);
            leftPanel.add(p);
        }
        
        // Right panel containing outputs
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1, 0, 5));
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


     // --- Import the code to the generated file ---
    public String getCode() {
        switch (title) {
            case "Debug Block" -> {
                System.out.println("---");
                System.out.println("debug");
                System.out.println(title);
                return "System.out.println(\"hey from the debug block!\");\n%s";
            }
            case "Empty Block" -> {
                System.out.println("---");
                System.out.println("Empty");
                System.out.println(title);
                return "System.out.println(\"hey from an empty block!\");\n%s";
            }
            default -> {
                System.out.println("---");
                System.out.println("not debug");
                System.out.println(title);
                return "System.out.println(\"hey from another block!\");\n%s";
            }
        }
    }

}
