import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class VariableCreatorPanel extends JPanel {
    private MainController controller;

    private final JTextField variableNameField; 			    // Text field to enter the variable name
    private final JComboBox<String> typeSelector; 			    // Type Selcetor (to define the type of the variable)
    private final JButton addButton; 					        // Add button that will add the variable to the lists of variables
    private final DefaultListModel<String> variableListModel; 	// The model that holds the data (strings) for the JList
    private final JList<String> variableJList; 				    // The Swing component that displays the list of variable strings
    private final List<Map<String, String>> variables; 		    // Internal list to store variables as maps (name and type)

    public VariableCreatorPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Variable Creator"));
        setBackground(Color.WHITE);
        setOpaque(true);

        // Panel for variable creation (horizontal layout)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setPreferredSize(new Dimension(0, 100));
        variableNameField = new JTextField(10);
        String[] types = {"int", "float", "bool"};
        typeSelector = new JComboBox<>(types);
        addButton = new JButton("Add Variable");

        // Inputs
        inputPanel.add(variableNameField);
        inputPanel.add(typeSelector);
        inputPanel.add(addButton);

        // List of existing variables setup
        variableListModel = new DefaultListModel<>();
        variableJList = new JList<>(variableListModel);

        // Scroll Pane to store the list of variables
        JScrollPane scrollPane = new JScrollPane(variableJList);
        scrollPane.setPreferredSize(new Dimension(0, 200)); // Ensure visible space

        // Internal list to store variables (name, type)
        variables = new ArrayList<>();


        // Action to add a variable
        addButton.addActionListener((ActionEvent _) -> {
            String variableName = variableNameField.getText().trim();
            String type = (String) typeSelector.getSelectedItem();
            if (!variableName.isEmpty()) {
                Map<String, String> var = new HashMap<>();
                var.put("name", variableName);
                var.put("type", type);
                variables.add(var);
                // Add a display string to the JList model
                variableListModel.addElement(variableName + " (" + type + ")");
                variableNameField.setText("");
                this.controller.setVariables(variables);
            }
        });

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

    public void setController(MainController controller) {
        this.controller = controller;
    }
}