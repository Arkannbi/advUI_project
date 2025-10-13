import codeGenerator.CodeGenerator;
import javax.swing.*;
import ui.MainFrame;

public class Main {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            CodeGenerator codeGenerator = new CodeGenerator(frame);

            frame.setCodeGenerator(codeGenerator);

            frame.setVisible(true);
        });
    }
}
