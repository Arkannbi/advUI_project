import javax.swing.*;

public class Connection {
	private Port from;
	private Port to;
	
	public Connection(Port from, Port to) {
        this.from = from;
        this.to = to;
    }

    public Port getFrom() {
    	return from;
    }
    
    public Port getTo() {
    	return to;
    }
}
