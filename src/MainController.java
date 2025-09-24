import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class MainController {

    private final StringBuilder codeModel;
    private final MainFrame mainFrame;
    private List blocks;
    
    private String codeToBeGenerateded;

    public MainController(StringBuilder codeModel, MainFrame mainFrame) {
        this.codeModel = codeModel;
        this.mainFrame = mainFrame;
        initCode();
    }

    public void addDebugBlock() {
        codeModel.append("System.out.println(\"Hello from the debug block!\");\n");
        mainFrame.updateCodeDisplay(codeModel.toString());
    }

    public void runCode() {
        String generatedCode = generateCode();

        // The generated directory is now a constant, making it easier to manage.
        String dirPath = "./generated/";
        String className = "GameRunner";
        String fileName = className + ".java";

        // --- WRITE FILE ---
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(dirPath + fileName)) {
            fileWriter.write(generatedCode);
        } catch (IOException e) {
            mainFrame.showLog("Error writing code to file: " + e.getMessage());
            return;
        }

        // --- FIND COMPILER ---
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            mainFrame.showLog("Java compiler not found. Make sure you are using a JDK, not a JRE.");
            return;
        }

        int compilationResult = compiler.run(null, null, null, "-d", dirPath, dirPath + fileName);

        if (compilationResult == 0) {
            // --- RUN THE GENERATED CLASS ---
            try {
                mainFrame.showLog("Code compiled successfully!");
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "java", "-cp", dirPath, className);
                processBuilder.redirectErrorStream(true); // merge stdout + stderr

                Process process = processBuilder.start();

                // Read output asynchronously
                new Thread(() -> {
                    try (java.io.BufferedReader reader =
                                new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            mainFrame.showLog(line); // Send to your custom logger
                        }
                    } catch (IOException e) {
                        mainFrame.showLog("Error reading process output: " + e.getMessage());
                    }
                }).start();

            } catch (IOException e) {
                mainFrame.showLog("Error running generated code: " + e.getMessage());
            }
        } else {
            mainFrame.showLog("Code compilation failed. Check the generated code for errors.");
        }
    }

    private String generateCode() {
        for (Block block : mainFrame.getCanvasBlocks()) {
            addCode(block.getCode());
        }
        addCode("");
        return codeToBeGenerateded.formatted(codeModel.toString());
    }

    private void initCode() {
        codeToBeGenerateded = """
            import javax.swing.*;

            public class GameRunner {
                public static void main(String[] args) {
                    System.out.println("Starting your game...");
                    JFrame frame = new JFrame("Platformer Game");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(400, 300);
                    frame.setLocationRelativeTo(null);
                    JLabel label = new JLabel("Game running... Check console for debug output!", SwingConstants.CENTER);
                    frame.add(label);
                    frame.setVisible(true);

                    // Generated pseudo-code from blocks starts here:
                    %s


                }
            }
            """;
    }    
    
    private void addCode(String codeToBeAdded) {
        codeToBeGenerateded = String.format(codeToBeGenerateded, codeToBeAdded);
    }

}
