import java.awt.*;
import javax.swing.*;

public class Port extends JPanel {
	public final boolean isActivationPort;
	
	private final boolean isInput;
	private final String name;
	private boolean isConnected;
	private final JTextField defaultField;
	private final JComponent clickablePart;

	private Port connectedPort;
	private final Block parent;

	private String outputValue;
	
	public Port(Block parent, boolean isInput, String name, boolean isActivationPort) {
		this.isInput = isInput;
		this.isActivationPort = isActivationPort;
		this.isConnected = false;
		this.defaultField = new JTextField();
		this.name = name;
		this.parent = parent;
		
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
				if (isActivationPort)
                	g.setColor(isInput ? Color.CYAN : Color.MAGENTA);
				else
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
        	if (!isActivationPort) this.add(defaultField, BorderLayout.EAST);
        }
        else {
        	title.setHorizontalAlignment(SwingConstants.RIGHT);
        	this.add(title, BorderLayout.CENTER);
        	this.add(clickablePart, BorderLayout.EAST);
        }
	}

	public boolean isInput() {
		return isInput;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void connect(Port port) {
		isConnected = true;
		connectedPort = port;
		
		if (isInput) {
			this.removeAll();
	        JLabel title = new JLabel(name);
			this.add(clickablePart, BorderLayout.WEST);
			this.add(title, BorderLayout.CENTER);
			revalidate();
		}
	}
	
	public void disconnect() {
		isConnected = false;
		connectedPort = null;
		
		if (isInput) {
			this.removeAll();
	        JLabel title = new JLabel(name);
			this.add(clickablePart, BorderLayout.WEST);
			this.add(title, BorderLayout.CENTER);
			if (!isActivationPort) this.add(defaultField, BorderLayout.EAST);
			revalidate();
		}
	}
	
	public Port getConnectedPort() {
		return connectedPort;
	}

	public String getDefaultValue() {
		if (!isConnected) return defaultField.getText();
		else return connectedPort.getOutputValue();
	}
	
	public void setDefaultValue(String newText) {
		defaultField.setText(newText);
	}

	public Block getConnectedBlock() {
		if (isConnected) {
			return connectedPort.getBlock();
		}
		else return null;
	}

	public Block getBlock() {
		return parent;
	}

	public String getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(String outputValue) {
		this.outputValue = outputValue;
	}
}
