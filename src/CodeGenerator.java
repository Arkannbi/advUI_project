import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.tools.JavaCompiler; // Import ArrayList
import javax.tools.ToolProvider;

public class CodeGenerator {

    private final MainFrame mainFrame;
    
    private final List<String> eventCodeFragments = new ArrayList<>(3); // 0: KeyPressed, 1: OnFrame, 2: OnStart

    private String codeTemplate; // Stores the template with %s placeholders
    List<Map<String, String>> variables;
    
    public CodeGenerator(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void runCode() {
        initCode();
        System.out.println("\n\n\n\n--- CODE RUNNING ---\n\n\n");
        String generatedCode = generateCode();

        // ... (rest of runCode remains the same) ...
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

        // Using System.err for compiler output for better logging, though null is fine
        int compilationResult = compiler.run(null, System.err, System.err, "-d", dirPath, dirPath + fileName);

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
        System.out.println("Generating code...");

        var currentCode = codeTemplate;

        currentCode = generateVariables(currentCode);

        
        // Reset code fragments
        eventCodeFragments.clear();
        eventCodeFragments.add(""); // KeyPressed (index 0)
        eventCodeFragments.add(""); // OnFrame (index 1)
        eventCodeFragments.add(""); // OnStart (index 2)

        // Find and process event chains
        for (Block eventBlock : mainFrame.getCanvasBlocks()) {
            // Find the event blocks to start the chain
            if (eventBlock.getType() == BlockType.Event) {
                // Determine where to put the code depending on the event type
                int fragmentIndex = getFragmentIndex(eventBlock);
                
                // Check if the event is recognized
                if (fragmentIndex != -1) {
                    // Generate the code for the entire chain starting from the event block
                    String eventCode = eventBlock.getCode();
                    String generatedChainCode = "";
                    var nextBlocks = eventBlock.getNextBlocks();
                    System.out.println("nouveau block : " + eventBlock.getTitle() + " !");
                    if (!nextBlocks.isEmpty()) {
                        generatedChainCode = processBlockChain(nextBlocks.get(0),""); // Start processing from the block *after* the event block
                    }                    
                    String fullChainCode = String.format(eventCode, generatedChainCode);
                    // Write the code on the corresponding code fragment
                    addCodeToFragment(fullChainCode, fragmentIndex);
                }
            }
        }

        // Now all three fragments are complete, fill the template.
        return String.format(currentCode, 
            eventCodeFragments.get(0), // KeyPressed
            eventCodeFragments.get(1), // OnFrame
            eventCodeFragments.get(2)  // OnStart
        );
    }

    // Initialize the variables
    private String generateVariables(String currentCode) {
        String variablesCode = "";
        for (var variable : variables) {
            String varName = variable.get("name");
            String varType = variable.get("type");
            String varValue = variable.get("value");
            variablesCode = variablesCode + "\n" + varType + " " + varName + " = " + varValue + ";";
        }
        return String.format(currentCode, variablesCode, "%s", "%s", "%s"); // Keep the "%s" to replace them later with the actual code
    }
    
    // Traverse the chain of blocks
    private String processBlockChain(Block currentBlock, String currentChainCode) {


        currentChainCode = BackpropagateBlockChain(currentBlock, currentChainCode);
        String currentBlockCode = currentBlock.getCode();
        currentChainCode = currentChainCode + "\n" + currentBlockCode;
        
        List<Block> nextBlocks = currentBlock.getNextBlocks();
        
        if (currentBlock.getType() == BlockType.Logic) {
            System.out.println("logic !");
            List<String> blocksCode = new ArrayList<>();
            for (Block b : nextBlocks) {
                System.out.println("block !");
                System.out.println(b == null);
                if (b == null) blocksCode.add("");
                else blocksCode.add(processBlockChain(b, ""));
            }
            System.out.println(currentChainCode);
            System.out.println(blocksCode.size());
            return String.format(currentChainCode, blocksCode.toArray());
            } else if (nextBlocks.isEmpty() || nextBlocks.getFirst() == null) {
            System.out.println("chain : " + currentBlock.getTitle() + " -> ???");
            return currentChainCode;
            } else {
                Block nextBlock = nextBlocks.get(0); 
                System.out.println("chain : " + currentBlock.getTitle() + " -> " + nextBlock.getTitle());
                return processBlockChain(nextBlock, currentChainCode);
            }
        }

    private String BackpropagateBlockChain(Block currentBlock, String currentChainCode) {

        for (var port : currentBlock.getInputs()) {
            if (!port.isActivationPort && port.isConnected()) {
                var previousBlock = port.getConnectedBlock();
                currentChainCode = BackpropagateBlockChain(previousBlock, currentChainCode);
                currentChainCode = currentChainCode + previousBlock.getCode();
            }
        }
        return currentChainCode;
    }
    
    // Get the event index to know where to insert the code
    private int getFragmentIndex(Block block) {
        return switch (((JLabel) block.getComponent(0)).getText()) {
            case "On KeyPressed (Space)" -> 0;
            case "On KeyPressed (Left)" -> 0;
            case "On KeyPressed (Right)" -> 0;
            case "On KeyPressed (Up)" -> 0;
            case "On KeyPressed (Down)" -> 0;
            case "On Frame" -> 1;
            case "On Start" -> 2;
            default -> -1;
        };
    }
    
    // Add the code of the block to the fragment
    private void addCodeToFragment(String generatedChainCode, int fragmentIndex) {
        String currentCode = eventCodeFragments.get(fragmentIndex);

        // Add a bit of indentation (e.g., 8 spaces) to align with the template
        // This must be done on the *final* generated code for the whole chain.
        String indentedCode = generatedChainCode.replaceAll("(?m)^", "        "); 
        
        eventCodeFragments.set(fragmentIndex, currentCode + indentedCode + "\n");
    }
    
    private void initCode() {
        // Store the template here, the placeholders are now for the event code fragments
        codeTemplate = """
            import java.awt.*;
            import java.awt.event.KeyAdapter;
            import java.awt.event.KeyEvent;
            import java.util.ArrayList;
        	import java.util.List;
            
            import javax.swing.*;

            public class GameRunner extends JPanel implements Runnable {
                private Thread gameThread;
                private List<GameObject> objects = new ArrayList<>();

                // Variables
                %s

                public GameRunner() {
                    setFocusable(true);
                    addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            // KeyPressed event
                            %s 
                        }
                    });
                }

                @Override
                public void run() {
                    startGame();
                    long lastTime = System.nanoTime();
                    while (true) {
                        long currentTime = System.nanoTime();
                        deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
                        lastTime = currentTime;
                        update();
                        repaint();
                        try {
                            Thread.sleep(16); // ~60 FPS
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                private void update() {
                    // OnFrameEvent
                    %s 
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    for (GameObject obj : objects) {
				        g.setColor(obj.color);
				        g.fillRect(obj.x, obj.y, obj.width, obj.height);
				    }
				    
                    g.setColor(Color.BLUE);
                    g.fillRect(playerX, playerY, playerWidth, playerHeight);
                }
                
                private void startGame() {
                    // OnStart Event
                    %s 
                }

                public static void main(String[] args) {
                    System.out.println("Starting your game...");
                    JFrame frame = new JFrame("Platformer Game");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(400, 300);
                    frame.setLocationRelativeTo(null);

                    GameRunner game = new GameRunner();
                    frame.add(game);
                    frame.setVisible(true);

                    game.gameThread = new Thread(game);
                    game.gameThread.start();
                }
            }
            
            class GameObject {
				public int x;
				public int y;
				public int width;
				public int height;
				
				public Color color;
				public boolean collides;
				
				 public GameObject(int x, int y, int width, int height, Color color, boolean collides) {
			        this.x = x;
			        this.y = y;
			        this.width = width;
			        this.height = height;
			        this.color = color;
			        this.collides = collides;
			    }
				 
				 public Rectangle getBounds() {
			        return new Rectangle(x, y, width, height);
			    }
			}

            """;
    }

    public void setVariables(List<Map<String, String>> variables) {
        this.variables = variables;
    }
}