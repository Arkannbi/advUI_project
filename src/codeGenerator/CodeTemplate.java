package codeGenerator;

import java.util.List;

public class CodeTemplate {

    public String build(String variablesCode, List<String> fragments) {
        return String.format(TEMPLATE,
                variablesCode,
                fragments.get(0),
                fragments.get(1),
                fragments.get(2));
    }

    private static final String TEMPLATE = """
            import java.awt.*;
            import java.awt.event.KeyAdapter;
            import java.awt.event.KeyEvent;
            import java.util.ArrayList;
        	import java.util.List;
        	import java.util.HashSet;
    		import java.util.Set;
    		import java.util.HashMap;
    		import java.util.Map;
            
            import javax.swing.*;

            public class GameRunner extends JPanel implements Runnable {
                private Thread gameThread;
                private Map<String, GameObject> objects = new HashMap<>();
                private Set<Integer> keysPressed = new HashSet<>();

                // Variables
                %s

                public GameRunner() {
                    setFocusable(true);
                    objects.put("player", new GameObject(player_initial_x, player_initial_y, player_initial_width, player_initial_height, Color.blue));
                    addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            // KeyPressed event
                            keysPressed.add(e.getKeyCode());
                        }
                        
                        @Override 
                        public void keyReleased(KeyEvent e) {
    		                // KeyReleased event
                            keysPressed.remove(e.getKeyCode());
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
                        
                        // Player actions
                        %s 
                        
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
                    
                    for (GameObject obj : objects.values()) {
				        g.setColor(obj.color);
				        g.fillRect(obj.x, obj.y, obj.width, obj.height);
				    }
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
				
				 public GameObject(int x, int y, int width, int height, Color color) {
			        this.x = x;
			        this.y = y;
			        this.width = width;
			        this.height = height;
			        this.color = color;
			    }
				 
				 public Rectangle getBounds() {
			        return new Rectangle(x, y, width, height);
			    }
			}

            """;
    }

