package ui;
import codeGenerator.CodeGenerator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import settings.Settings;

public final class VariableCreatorPanel extends JPanel {
    private CodeGenerator codeGenerator;

    private final JTextField variableNameField; 			    // Text field to enter the variable name
    private final JComboBox<String> typeSelector; 			    // Type Selcetor (to define the type of the variable)
    private final JButton addButton; 					        // Add button that will add the variable to the lists of variables
    private final DefaultListModel<String> variableListModel; 	// The model that holds the data (strings) for the JList
    private final JList<String> variableJList; 				    // The Swing component that displays the list of variable strings
    private final List<Map<String, String>> variables; 		    // Internal list to store variables as maps (name and type)

    public VariableCreatorPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Variable Creator",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null,
            Settings.getInstance().textColor
        ));

        setBackground(Settings.getInstance().baseColor);
        setOpaque(true);

        // Panel for variable creation (horizontal layout)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setPreferredSize(new Dimension(0, 100));
        inputPanel.setBackground(Settings.getInstance().baseColor);
        variableNameField = new JTextField(10);
        variableNameField.setBackground(Settings.getInstance().baseColor);
        variableNameField.setForeground(Settings.getInstance().textColor);
        String[] types = {"boolean", "int", "float", "String"};
        typeSelector = new JComboBox<>(types);
        typeSelector.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setBackground(Settings.getInstance().buttonColor);
                arrowButton.setForeground(Settings.getInstance().textColor);
                return arrowButton;
            }
        });
        typeSelector.setBackground(Settings.getInstance().buttonColor);
        typeSelector.setForeground(Settings.getInstance().textColor);
        addButton = new JButton("Add Variable");
        addButton.setBackground(Settings.getInstance().buttonColor);
        addButton.setForeground(Settings.getInstance().textColor);

        // Inputs
        inputPanel.add(variableNameField);
        inputPanel.add(typeSelector);
        inputPanel.add(addButton);


        // List of existing variables setup
        variableListModel = new DefaultListModel<>();
        variableJList = new JList<>(variableListModel);

        // Enable drag from the variable list using the custom TransferHandler
        variableJList.setTransferHandler(new VariableTransferHandler());
        variableJList.setDragEnabled(true); 
        variableJList.setBackground(Settings.getInstance().secondaryColor);
        variableJList.setForeground(Settings.getInstance().textColor);

        // Scroll Pane to store the list of variables
        JScrollPane scrollPane = new JScrollPane(variableJList);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        // Internal list to store variables (name, type)
        variables = new ArrayList<>();


        // Action to add a variable
        addButton.addActionListener((ActionEvent e) -> {
            String varName = variableNameField.getText().trim();
            String varType = (String) typeSelector.getSelectedItem();
            String varValue;
            switch (varType) {
                case "boolean" -> varValue = "true";
                case "float" -> varValue = "0";
                case "int" -> varValue = "0";
                case "String" -> varValue = "\"\"";
                default -> {
                    varValue = "0";
                }
            }
            addVariable(varName, varType, varValue);
        });

        addVariable("deltaTime", "float", "0.0f");
        addVariable("player_initial_x", "int", "200");
        addVariable("player_initial_y", "int", "200");
        addVariable("player_initial_width", "int", "50");
        addVariable("player_initial_height", "int", "50");
        // Add components to main panel
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }


    // Getter for the list of variables
    public List<Map<String, String>> getVariables() {
        return variables;
    }

    // Setter for the list of variables (useful for loading existing variables)
    public void setVariables(List<Map<String, String>> variables) {
        this.variables.clear();
        this.variables.addAll(variables);
        updateVariableListModel();
    }

    // Updates the JList model from the internal variables list
    private void updateVariableListModel() {
        variableListModel.clear();
        for (Map<String, String> var : variables) {
            variableListModel.addElement(var.get("name") + " (" + var.get("type") + ")");
        }
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
        this.codeGenerator.setVariables(variables);
    }

    public void addVariable(String varName, String varType, String varValue) {
        if (!varName.isEmpty()) {
            Map<String, String> var = new HashMap<>();
            var.put("name", varName);
            var.put("type", varType);
            var.put("value", varValue);
            variables.add(var);
            // Add a display string to the JList model
            variableListModel.addElement(varType + " " + varName + " : " + varValue);
            variableNameField.setText("");
            if (this.codeGenerator != null) this.codeGenerator.setVariables(variables);
        }
    }
}