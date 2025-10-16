import codeGenerator.CodeGenerator;
import codeGenerator.CodeSerializer;
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

            
            CodeSerializer serializer= new CodeSerializer();
            serializer.loadFromXML("./autosave.xml", frame.getCanvas());

            frame.setVisible(true);
        });
    }
}
