import javax.swing.*;

public class Main {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            MainController controller = new MainController( frame);

            frame.setController(controller);

            frame.setVisible(true);
        });
    }
}
