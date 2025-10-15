package blocks;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import settings.Settings;


public final class Block extends JPanel {
    private final String title;
	private final List<Port> inputs;
	private final List<Port> outputs;
    private final BlockType type;
	private final List<String> inputNames;
	private final List<String> outputNames;

    public Block(String title, BlockType type, List<String> inputNames, List<String> outputNames) {
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.title = title;
        this.type = type;
        this.inputNames = inputNames;
        this.outputNames = outputNames;

        setLayout(new BorderLayout());
        setOpaque(false);

        // Custom rounded border with color based on type
        setBorder(new RoundedBorder(getBorderColor(type), 2, 15));

        // Block title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD + Font.ITALIC, 14f));
        titleLabel.setForeground(Settings.getInstance().textColor); // White font
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Left panel containing inputs
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(0, 1, 0, 5));
        leftPanel.setOpaque(false);
        for (int i = 0; i < inputNames.size(); i++) {
            boolean isActivationPort = (i == 0 && (type == BlockType.Action || type == BlockType.Logic));
            Port p = new Port(this, true, inputNames.get(i), isActivationPort);
            inputs.add(p);
            leftPanel.add(p);
        }

        // Right panel containing outputs
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1, 0, 5));
        rightPanel.setOpaque(false);
        for (int i = 0; i < outputNames.size(); i++) {
            boolean isActivationPort = ((i == 0 && (type == BlockType.Action || type == BlockType.Event)) || type == BlockType.Logic);
            Port p = new Port(this, false, outputNames.get(i), isActivationPort);
            outputs.add(p);
            rightPanel.add(p);
        }

        // Wrap them so that they keep their preferred size
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

            // LOGIC
            case "If Block" -> {
                String condition = serializeFloatValue(inputs.get(1).getDefaultValue());
                return "if (" + condition + ") { \n%s\n }";
            }
            case "If Else Block" -> {
                String condition = serializeFloatValue(inputs.get(1).getDefaultValue());
                return "if (" + condition + ") { \n%s\n } else { \n%s\n }";
            }

            // CONDITION
            case "Not" -> {
                String condition = inputs.get(0).getDefaultValue();
                var output = "%!" + serializeFloatValue(condition);
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "And" -> {
                String condition1 = inputs.get(0).getDefaultValue();
                String condition2 = inputs.get(1).getDefaultValue();
                var output = "%(" + serializeFloatValue(condition1) + " && " + serializeFloatValue(condition2) + ")" ;
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Or" -> {
                String condition1 = inputs.get(0).getDefaultValue();
                String condition2 = inputs.get(1).getDefaultValue();
                var output = "%(" + serializeFloatValue(condition1) + " || " + serializeFloatValue(condition2) + ")" ;
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Xor" -> {
                String condition1 = inputs.get(0).getDefaultValue();
                String condition2 = inputs.get(1).getDefaultValue();
                var output = "%(" + serializeFloatValue(condition1) + " ^ " + serializeFloatValue(condition2) + ")" ;
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Inferior to" -> {
                String number1 = inputs.get(0).getDefaultValue();
                String number2 = inputs.get(1).getDefaultValue();
                var output = "%" + serializeFloatValue(number1) + " <= " + serializeFloatValue(number2);
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Stricly Inferior to" -> {
                String number1 = inputs.get(0).getDefaultValue();
                String number2 = inputs.get(1).getDefaultValue();
                var output = "%" + serializeFloatValue(number1) + " < " + serializeFloatValue(number2);
                outputs.get(0).setOutputValue(output);
                return "";
            }
            case "Equals" -> {
                String number1 = inputs.get(0).getDefaultValue();
                String number2 = inputs.get(1).getDefaultValue();
                var output = "%" + serializeFloatValue(number1) + " == " + serializeFloatValue(number2);
                outputs.get(0).setOutputValue(output);
                return "";
            }

            // ACTION
            case "Debug Block" -> {
                String message = inputs.get(1).getDefaultValue();
                return "System.out.println(" + serializeStringValue(message) + ");\n";
            }
            case "Set Variable" -> {
                String variableName = serializeFloatValue(inputs.get(1).getDefaultValue());
                String variableValue = inputs.get(2).getDefaultValue();
                return variableName + " = " + serializeFloatValue(variableValue) + ";";
            }

            // INTERMEDIARY
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
            case "Random" -> {
            	String min = inputs.get(0).getDefaultValue();
            	String max = inputs.get(1).getDefaultValue();
            	var output = "%(int) ((Math.random() * (" + serializeFloatValue(max) + " - " + serializeFloatValue(min) + ")) + " + serializeFloatValue(min) + ")";
            	outputs.get(0).setOutputValue(output);
            	return "";
            }

            // EVENT
            case "On Start" -> {
                return "System.out.println(\"On Start event triggered!\");\n%s";
            }
            case "On Frame" -> {
                return "%s";
            }
            case "On KeyPressed (Space)" -> {
                return """
                            if (keysPressed.contains(KeyEvent.VK_SPACE)) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Left)" -> {
                return """
                            if (keysPressed.contains(KeyEvent.VK_LEFT)) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Right)" -> {
                return """
                            if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Up)" -> {
                return """
                            if (keysPressed.contains(KeyEvent.VK_UP)) {
                                %s
                            }
                       """;
            }
            case "On KeyPressed (Down)" -> {
                return """
                            if (keysPressed.contains(KeyEvent.VK_DOWN)) {
                                %s
                            }
                       """;
            }
            case "Create Object" -> {
            	String name = inputs.get(1).getDefaultValue().toLowerCase();
                String x = inputs.get(2).getDefaultValue();
                String y = inputs.get(3).getDefaultValue();
                String width = inputs.get(4).getDefaultValue();
                String height = inputs.get(5).getDefaultValue();
                return "objects.put(\"" + name + "\", new GameObject(" 
                    + x + ", " + y + ", " + width + ", " + height + ", "
                    + "Color.black" + ", true));\n";
            }
            case "Set Object Variable" -> {
            	String objectName = inputs.get(1).getDefaultValue().toLowerCase();
            	String varName = inputs.get(2).getDefaultValue().toLowerCase();
            	String value = inputs.get(3).getDefaultValue();
            	return "objects.get(\"" + objectName + "\")." + varName + " = " + serializeFloatValue(value) + ";";
            }
            case "Get Object Variable" -> {
            	String objectName = inputs.get(0).getDefaultValue().toLowerCase();
            	String varName = inputs.get(1).getDefaultValue().toLowerCase();
            	var output = "objects.get(\"" + objectName + "\")." + varName;
            	outputs.get(0).setOutputValue(output);
            	return "";
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
            nextBlocks.add(nb);
            
        }

        return nextBlocks;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getInputNames() {
        return inputNames;
    }
    
    public List<String> getOutputNames() {
        return outputNames;
    }
    
    public Block copy() {
    	Block copy = new Block(this.title, this.type, new ArrayList<>(this.inputNames), new ArrayList<>(this.outputNames));
    	return copy;
    }

    public Color getBorderColor(BlockType type) {
        return switch (type) {
            case Action -> Color.RED;
            case Logic -> Color.MAGENTA;
            case Condition -> Color.PINK;
            case Event -> Color.GREEN;
            default -> Color.BLUE;
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Settings.getInstance().baseColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paintComponent(g);
        g2d.dispose();
    }

}


