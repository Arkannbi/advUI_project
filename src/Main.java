import javax.swing.*;

public class Main {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            CodeGenerator controller = new CodeGenerator( frame);

            frame.setController(controller);

            frame.setVisible(true);
        });
    }
}
