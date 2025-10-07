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
            var isActivationPort = (i==0 && (type == BlockType.Action || type == BlockType.Logic));
            Port p = new Port(this, true, "i_" + i, isActivationPort);
            inputs.add(p);
            leftPanel.add(p);
        }
        
        // Right panel containing outputs
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1, 0, 5));
        rightPanel.setOpaque(false);

        for (int i = 0; i < nbOutputs; i++) {
            var isActivationPort = ((i==0 && (type == BlockType.Action || type == BlockType.Event)) || type == BlockType.Logic);
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

     // If the value starts with "%" it is a variable, else it is a constant
     private String serializeStringValue(String value) {
        if (value != null && !value.isEmpty()) {
            if (value.charAt(0) == '%') {
                return value.substring(1);
            }
            return "\"" + value + "\"";
        }
        return "0";
    }

    // If the value starts with "%" it is a variable but we don't need the quote for a constant now
    private String serializeFloatValue(String value) {
        if (value != null && !value.isEmpty()) {
            if (value.charAt(0) == '%') {
                return value.substring(1);
            }
            return value;
        }
        return "0";
    }


    // --- Import the code to the generated file ---
    public String getCode() {
        switch (title) {
            case "If Block" -> {
                String condition = inputs.get(1).getDefaultValue();
                return "if (" + condition + ") { \n%s\n }";
            }
            case "If Else Block" -> {
                String condition = inputs.get(1).getDefaultValue();
                return "if (" + condition + ") { \n%s\n } else { \n%s\n }";
            }
            case "Debug Block" -> {
                String message = inputs.get(1).getDefaultValue();
                return "System.out.println(" + serializeStringValue(message) + ");\n";
            }
            case "Set Variable" -> {
                String variableName = inputs.get(1).getDefaultValue();
                String variableValue = inputs.get(2).getDefaultValue();
                return variableName + " = " + serializeFloatValue(variableValue) + ";";
            }
            case "Add" -> {
                String number1 = inputs.get(0).getDefaultValue();
                String number2 = inputs.get(1).getDefaultValue();
                var output = "%" + serializeFloatValue(number1) + " + " + serializeFloatValue(number2);
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Substract" -> {
                String number1 = inputs.get(0).getDefaultValue();
                String number2 = inputs.get(1).getDefaultValue();
                var output = "%" + serializeFloatValue(number1) + " - " + serializeFloatValue(number2);
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Multiply" -> {
                String number1 = inputs.get(0).getDefaultValue();
                String number2 = inputs.get(1).getDefaultValue();
                var output = "%" + serializeFloatValue(number1) + " * " + serializeFloatValue(number2);
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Divide" -> {
                String number1 = inputs.get(0).getDefaultValue();
                String number2 = inputs.get(1).getDefaultValue();
                var output = "%" + serializeFloatValue(number1) + " / " + serializeFloatValue(number2);
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Convert" -> {
                String inputValue = inputs.get(0).getDefaultValue();
                String newType = inputs.get(1).getDefaultValue();
                var output = "%(" + newType + ")(" + serializeFloatValue(inputValue) + ")";
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "On Start" -> {
                return "System.out.println(\"On Start event triggered!\");\n%s";
            }
            case "On Frame" -> {
                return "%s";
            }
            case "On KeyPressed (Space)" -> {
                return """
                            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Left)" -> {
                return """
                            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Right)" -> {
                return """
                            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Up)" -> {
                return """
                            if (e.getKeyCode() == KeyEvent.VK_UP) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Down)" -> {
                return """
                            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                %s
                            }
                       """;
            }
            default -> {
                return "System.out.println(\"hey from another block!\");";
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

    public String getTitle() {
        return title;
    }

}
