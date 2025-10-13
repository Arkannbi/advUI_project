package codeGenerator;
import java.io.*;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import ui.MainFrame;

public class CodeExecutor {

    public boolean writeFile(String dirPath, String fileName, String code) {
        File directory = new File(dirPath);
        if (!directory.exists() && !directory.mkdirs()) return false;

        try (FileWriter writer = new FileWriter(new File(directory, fileName))) {
            writer.write(code);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean compile(String dirPath, String className) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("JDK required: Java compiler not found.");
            return false;
        }

        int result = compiler.run(null, System.err, System.err, "-d", dirPath, dirPath + className + ".java");
        return result == 0;
    }

    public void runJavaClass(String dirPath, String className, MainFrame mainFrame) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", dirPath, className);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            new Thread(() -> {
                try (BufferedReader reader =
                             new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null)
                        mainFrame.showLog(line);
                } catch (IOException e) {
                    mainFrame.showLog("Error reading process output: " + e.getMessage());
                }
            }).start();

            mainFrame.showLog("Code compiled and running...");
        } catch (IOException e) {
            mainFrame.showLog("Error running code: " + e.getMessage());
        }
    }
}
