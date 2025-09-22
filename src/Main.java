import javax.swing.*;

public class Main {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blueprint-like UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            Canvas canvas = new Canvas();

            Block block1 = new Block("Addition", 2, 1);
            Block block2 = new Block("Multiply", 2, 1);

            canvas.addBlock(block1, 50, 100, 200, 150);
            canvas.addBlock(block2, 400, 200, 200, 150);

            frame.setContentPane(canvas);
            frame.setVisible(true);
        });
    }
}
