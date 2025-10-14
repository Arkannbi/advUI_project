package codeGenerator;
import blocks.Block;
import blocks.BlockType;
import java.util.*;
import javax.swing.JLabel;
import ui.MainFrame;

public class CodeGenerator {

    private final MainFrame mainFrame;
    private final List<String> eventCodeFragments = new ArrayList<>(List.of("", "", ""));
    private final CodeTemplate codeTemplate = new CodeTemplate();
    private final CodeExecutor executor = new CodeExecutor();
    private List<Map<String, String>> variables;

    public CodeGenerator(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setVariables(List<Map<String, String>> variables) {
        this.variables = variables;
    }

    /** Entry point — generates, compiles, and runs code */
    public void runCode() {
        mainFrame.showLog("\n--- CODE GENERATION STARTED ---");

        String generatedCode = generateCode();
        String dirPath = "./generated/";
        String className = "GameRunner";

        if (!executor.writeFile(dirPath, className + ".java", generatedCode)) {
            mainFrame.showLog("Failed to write source file.");
            return;
        }

        if (!executor.compile(dirPath, className)) {
            mainFrame.showLog("Compilation failed. Check generated code.");
            return;
        }

        executor.runJavaClass(dirPath, className, mainFrame);
    }

    /** Builds the full code from template, variables, and events */
    private String generateCode() {
        mainFrame.showLog("Generating code...");

        String variablesCode = generateVariableDeclarations();

        // Reset fragments
        Collections.fill(eventCodeFragments, "");

        // Walk through all event blocks
        for (Block block : mainFrame.getCanvasBlocks()) {
            if (block.getType() != BlockType.Event) continue;

            int fragmentIndex = getFragmentIndex(block);
            if (fragmentIndex == -1) continue;

            String eventCode = block.getCode();
            String chainCode = "";

            var nextBlocks = block.getNextBlocks();
            if (!nextBlocks.isEmpty()) {
                chainCode = processBlockChain(nextBlocks.get(0), "");
            }

            String fullChain = String.format(eventCode, chainCode);
            addCodeToFragment(fullChain, fragmentIndex);
        }

        return codeTemplate.build(variablesCode, eventCodeFragments);
    }

    /** Converts variable maps to Java declarations */
    private String generateVariableDeclarations() {
        if (variables == null) return "";
        StringBuilder sb = new StringBuilder();
        for (var variable : variables) {
            sb.append("\n")
              .append(variable.get("type")).append(" ")
              .append(variable.get("name")).append(" = ")
              .append(variable.get("value")).append(";");
        }
        return sb.toString();
    }

    /** Recursive traversal through connected logic blocks */
    private String processBlockChain(Block currentBlock, String chainCode) {
        chainCode = backpropagateInputs(currentBlock, chainCode);

        chainCode += "\n" + currentBlock.getCode();
        List<Block> nextBlocks = currentBlock.getNextBlocks();

        if (currentBlock.getType() == BlockType.Logic) {
            List<String> blocksCode = new ArrayList<>();
            for (Block b : nextBlocks) {
                blocksCode.add(b == null ? "" : processBlockChain(b, ""));
            }
            return String.format(chainCode, blocksCode.toArray());
        }

        if (nextBlocks.isEmpty() || nextBlocks.get(0) == null)
            return chainCode;

        return processBlockChain(nextBlocks.get(0), chainCode);
    }

    /** Recursively add input-connected blocks before the current one */
    private String backpropagateInputs(Block currentBlock, String chainCode) {
        for (var port : currentBlock.getInputs()) {
            if (!port.isActivationPort && port.isConnected()) {
                var prev = port.getConnectedBlock();
                chainCode = backpropagateInputs(prev, chainCode);
                chainCode += prev.getCode();
            }
        }
        return chainCode;
    }

    private int getFragmentIndex(Block block) {
        return switch (((JLabel) block.getComponent(0)).getText()) {
            case "On KeyPressed (Space)", "On KeyPressed (Left)", "On KeyPressed (Right)",
                 "On KeyPressed (Up)", "On KeyPressed (Down)" -> 0;
            case "On Frame" -> 1;
            case "On Start" -> 2;
            default -> -1;
        };
    }

    private void addCodeToFragment(String code, int index) {
        String indented = code.replaceAll("(?m)^", "        ");
        eventCodeFragments.set(index, eventCodeFragments.get(index) + indented + "\n");
    }
}
