import javax.swing.*;

public class Port extends JPanel {
	private final boolean isInput;
	private boolean isConnected;
	private String defaultContent;
	
	public Port(boolean isInput) {
		this.isInput = isInput;
		this.isConnected = false;
		this.defaultContent = "";
	}

	public boolean isInput() {
		return isInput;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void connect() {
		isConnected = !isConnected;
	}

	public String getDefaultContent() {
		return defaultContent;
	}

	public void setDefaultContent(String defaultContent) {
		this.defaultContent = defaultContent;
	}
}
