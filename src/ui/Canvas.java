package ui;
import blocks.Block;
import blocks.BlockTransferHandler;
import blocks.Connection;
import blocks.Port;
import blocks.RoundedBorder;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.geom.CubicCurve2D;
import java.util.*;
import javax.swing.*;
import settings.Settings;

public class Canvas extends JLayeredPane {
	private final List<Block> blocks;
	private final List<Connection> connections;
	
	private Port tempFrom;
	private Point mousePos;
	private Block currentSelectedBlock;
	private Block copiedBlock;
	
	// Arrows to show where blocks are on the canvas
	private boolean hasBlockLeft;
	private boolean hasBlockRight;
	private boolean hasBlockUp;
	private boolean hasBlockDown;
	
	public Canvas() {
		this.blocks = new ArrayList<>();
		this.connections = new ArrayList<>();
		this.tempFrom = null;
		this.mousePos = null;
		this.currentSelectedBlock = null;
		this.copiedBlock = null;
		
		this.hasBlockLeft = false;
		this.hasBlockRight = false;
		this.hasBlockUp = false;
		this.hasBlockDown = false;

		setFocusable(true);
		setLayout(null);
		requestFocusInWindow();
		
		setupListeners();
        setTransferHandler(new BlockTransferHandler());

		setBackground(Settings.getInstance().secondaryColor);
		setOpaque(true);
	}
	
	private void setupListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
            	tempFrom = null;
            	currentSelectedBlock = null;
            	mousePos = e.getPoint();
            	repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				requestFocusInWindow();
				mousePos = e.getPoint();
				if (tempFrom != null) {
					repaint();
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				requestFocusInWindow();
				for (Block block : blocks) {
					int deltaX = mousePos.x - e.getPoint().x;
					int deltaY = mousePos.y - e.getPoint().y;
					Rectangle rect = block.getBounds();
					block.setBounds(rect.x - deltaX, rect.y - deltaY, rect.width, rect.height);
				}
				mousePos = e.getPoint();
				repaint();
			}
		});
		
		addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            		if (currentSelectedBlock != null) {
            			removeBlock(currentSelectedBlock);
            			currentSelectedBlock = null;
            		}
            	}
            	else if (e.isControlDown()) {
            		if (e.getKeyCode() == KeyEvent.VK_C && currentSelectedBlock != null) {
            			if (currentSelectedBlock != null) {
                			copiedBlock = currentSelectedBlock.copy();
                		}
            		}
            		else if (e.getKeyCode() == KeyEvent.VK_V && copiedBlock != null) {
            			addBlock(copiedBlock, mousePos.x, mousePos.y);
            			copiedBlock = currentSelectedBlock.copy();
            		}
            	}
            	repaint();
            }
        });
	}
	
	public void addBlock(Block b, int x, int y) {
		// A bit ugly but to make it simple we're using hardcoded port sizes and their number to create the good block size
        // And we have to do it here because blocks aren't instantiated with x and y coordinates, as they depend on the canvas
		Dimension d = b.getPreferredSize();
		b.setBounds(x, y, 200, d.height);

        for (Port p : b.getOutputs()) {
            MouseAdapter adapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                	if (!p.isConnected()) {
                        tempFrom = p;
                	}
                	else {
                		List<Connection> toRemove = new ArrayList<>();
                		for (Connection connection : connections)  {
                			if (connection.getFrom().equals(p)) {
                				toRemove.add(connection);
                				Port to = connection.getTo();
                				to.disconnect();
                			}
                		}
                		connections.removeAll(toRemove);
                		p.disconnect();
                		repaint();
                	}
                }
            };
            p.setClickablePartAdapter(adapter);
        }
        
        for (Port p : b.getInputs()) {
            MouseAdapter adapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                	if (p.isConnected()) {
                		List<Connection> toRemove = new ArrayList<>();
                		for (Connection connection : connections)  {
                			if (connection.getTo().equals(p)) {
                				toRemove.add(connection);
                				Port from = connection.getFrom();
                				from.disconnect();
                			}
                		}
                		connections.removeAll(toRemove);
                		p.disconnect();
                	}
                    if (tempFrom != null) {
                        connections.add(new Connection(tempFrom, p));
                        tempFrom.connect(p);
                        p.connect(tempFrom);
                        tempFrom = null;
                    }
                    repaint();
                }
            };
            p.setClickablePartAdapter(adapter);
        }

        b.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		mousePos = e.getPoint();
        		tempFrom = null;
        		currentSelectedBlock = b;
        		b.getParent().repaint();
        	}
        });
        
        b.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
            public void mouseMoved(MouseEvent e) {
                if (mousePos != null) {
                    mousePos.x = b.getX() + e.getX();
                    mousePos.y = b.getY() + e.getY();

                    b.getParent().repaint();
                }
            }
        	
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mousePos != null) {
                    int newX = b.getX() + e.getX() - mousePos.x;
                    int newY = b.getY() + e.getY() - mousePos.y;

                    b.setLocation(newX, newY);

                    b.getParent().repaint();
                }
            }
        });
        
        blocks.add(b);
        add(b, Integer.valueOf(1));
        currentSelectedBlock = b;
        repaint();
    }
	
	public void removeBlock(Block b) {
		List<Connection> toRemove = new ArrayList<>();
		for (Connection c : connections) {
			if (b.getOutputs().contains(c.getFrom()) || b.getInputs().contains(c.getTo())) {
				toRemove.add(c);
			}
		}
		for (Connection c : toRemove) {
			removeConnection(c);
		}
		blocks.remove(b);
		remove(b);
	}
	
	public void removeConnection(Connection c) {
		c.getFrom().disconnect();
		c.getTo().disconnect();
		connections.remove(c);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        hasBlockLeft = false;
        hasBlockRight = false;
        hasBlockUp = false;
        hasBlockDown = false;
        
        int width = getWidth();
		int height = getHeight();

        Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Settings.getInstance().textColor);
        g2.setStroke(new BasicStroke(2));

        for (Connection c : connections) {
            Point a = SwingUtilities.convertPoint(c.getFrom(), c.getFrom().getWidth(), c.getFrom().getHeight()/2, this);
            Point b = SwingUtilities.convertPoint(c.getTo(), 0, c.getFrom().getHeight()/2, this);

            drawCurve(g2, a, b);
        }

        if (tempFrom != null && mousePos != null) {
            Point a = SwingUtilities.convertPoint(tempFrom, tempFrom.getWidth()/2, tempFrom.getHeight()/2, this);
            drawCurve(g2, a, mousePos);
        }
        for (Block b : blocks) {
        	if (b != null) {
        		if (b != currentSelectedBlock) {
        			b.setBorder(new RoundedBorder(b.getBorderColor(b.getType()), 2, 15));
        		}
            	else if (b == currentSelectedBlock) {
            		b.setBorder(new RoundedBorder(Settings.getInstance().textColor, 2, 15));
            	}
        		Rectangle rect = b.getBounds();
        		if (rect.x + rect.width < 0) {
        			hasBlockLeft = true;
        		}
        		if (rect.x > width) {
        			hasBlockRight = true;
        		}
        		if (rect.y + rect.height < 0) {
        			hasBlockUp = true;
        		}
        		if (rect.y > height) {
        			hasBlockDown = true;
        		}
        		
        	}
        }
        
        g2.setColor(Settings.getInstance().textColor);
        g2.setStroke(new BasicStroke(3));

        if (hasBlockUp) {
            drawTriangle(g2, width / 2, 20, 20, 10, "Up");
        }
        if (hasBlockDown) {
            drawTriangle(g2, width / 2, height - 20, 20, 10, "Down");
        }
        if (hasBlockLeft) {
            drawTriangle(g2, 20, height / 2, 20, 10, "Left");
        }
        if (hasBlockRight) {
            drawTriangle(g2, width - 20, height / 2, 20, 10, "Right");
        }
    }

    private void drawCurve(Graphics2D g2, Point a, Point b) {
        int ctrl1x = a.x + 80;
        int ctrl1y = a.y;
        int ctrl2x = b.x - 80;
        int ctrl2y = b.y;

        CubicCurve2D curve = new java.awt.geom.CubicCurve2D.Float(
                a.x, a.y, ctrl1x, ctrl1y, ctrl2x, ctrl2y, b.x, b.y
        );
        g2.draw(curve);
    }
    
    private void drawTriangle(Graphics2D g2, int xTip, int yTip, int baseWidth, int height, String direction) {
    	if ("Up".equals(direction)) {
    		g2.drawLine(xTip, yTip, xTip - baseWidth / 2, yTip + height);
    		g2.drawLine(xTip - baseWidth / 2, yTip + height, xTip + baseWidth / 2, yTip + height);
    		g2.drawLine(xTip + baseWidth / 2, yTip + height, xTip, yTip);
    	}
    	if ("Down".equals(direction)) {
    		g2.drawLine(xTip, yTip, xTip + baseWidth / 2, yTip - height);
    		g2.drawLine(xTip + baseWidth / 2, yTip - height, xTip - baseWidth / 2, yTip - height);
    		g2.drawLine(xTip - baseWidth / 2, yTip - height, xTip, yTip);
    	}
    	if ("Left".equals(direction)) {
    		g2.drawLine(xTip, yTip, xTip + height, yTip - baseWidth / 2);
    		g2.drawLine(xTip + height, yTip - baseWidth / 2, xTip + height, yTip + baseWidth / 2);
    		g2.drawLine(xTip + height, yTip + baseWidth / 2, xTip, yTip);
    	}
    	if ("Right".equals(direction)) {
    		g2.drawLine(xTip, yTip, xTip - height, yTip - baseWidth / 2);
    		g2.drawLine(xTip - height, yTip - baseWidth / 2, xTip - height, yTip + baseWidth / 2);
    		g2.drawLine(xTip - height, yTip + baseWidth / 2, xTip, yTip);
    	}
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}