
public class Connection {
	private final Port from;
	private final Port to;
	
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
