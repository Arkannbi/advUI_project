import java.awt.*;

import javax.swing.*;

public class Port extends JPanel {
	private final boolean isInput;
	private final String name;
	private boolean isConnected;
	private JTextField defaultField;
	private JComponent clickablePart;
	
	public Port(boolean isInput, String name) {
		this.isInput = isInput;
		this.isConnected = false;
		this.defaultField = new JTextField();
		this.name = name;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		
		if (isInput) {
			setPreferredSize(new Dimension(90,20));
		}
		else {
			setPreferredSize(new Dimension(40,20));
		}
		
		this.clickablePart = new JComponent() {
			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(isInput ? Color.BLUE : Color.RED);
                if (!isConnected) {
                	g.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                }
                else {
                	g.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
                }
            }
        };
        
        clickablePart.setPreferredSize(new Dimension(16, 16));
        
        JLabel title = new JLabel(name);
        
        if (isInput) {
        	defaultField.setPreferredSize(new Dimension(50,20));
        	
        	this.add(clickablePart, BorderLayout.WEST);
        	this.add(title, BorderLayout.CENTER);
        	this.add(defaultField, BorderLayout.EAST);
        }
        else {
        	this.add(title, BorderLayout.CENTER);
        	this.add(clickablePart, BorderLayout.EAST);
        }
	}

	public boolean isInput() {
		return isInput;
	}
	
	public String getName() {
		return name;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void connect() {
		isConnected = true;
	}
	
	public void disconnect() {
		isConnected = false;
	}

	public String getDefaultValue() {
		return defaultField.getText();
	}
	
	public void setDefaultValue(String newText) {
		defaultField.setText(newText);
	}
}
