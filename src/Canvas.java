import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.CubicCurve2D;
import java.util.*;
import javax.swing.*;

public class Canvas extends JLayeredPane {
	private List<Block> blocks;
	private List<Connection> connections;
	
	private Port tempFrom;
	private Point mousePos;
	private Block currentSelectedBlock;
	
	public Canvas() {
		this.blocks = new ArrayList<>();
		this.connections = new ArrayList<>();
		this.tempFrom = null;
		this.mousePos = null;
		this.currentSelectedBlock = null;

		setFocusable(true);
		requestFocusInWindow();
		
		setupListeners();
        setTransferHandler(new BlockTransferHandler());
	}
	
	private void setupListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
            	tempFrom = null;
            	currentSelectedBlock = null;
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
		});
		
		addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
            		if (currentSelectedBlock != null) {
            			removeBlock(currentSelectedBlock);
            			currentSelectedBlock = null;
            		}
            	}
            	repaint();
            }
        });
	}
	
	public void addBlock(Block b, int x, int y) {
        b.setBounds(x, y, 200, Math.max(b.getInputs().size(), b.getOutputs().size()) * 25 + 20);

        for (Port p : b.getOutputs()) {
            p.addMouseListener(new MouseAdapter() {
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
            });
        }
        
        for (Port p : b.getInputs()) {
            p.addMouseListener(new MouseAdapter() {
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
                        tempFrom.connect();
                        p.connect();
                        tempFrom = null;
                    }
                    repaint();
                }
            });
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
        add(b, JLayeredPane.DEFAULT_LAYER);
        currentSelectedBlock = b;
        repaint();
    }
	
	public void removeBlock(Block b) {
		blocks.remove(b);
		remove(b);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
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
        	if (b != null && b != currentSelectedBlock) {
    			b.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    		}
        	else if (b != null && b == currentSelectedBlock) {
        		b.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        	}
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

    public List<Block> getBlocks() {
        return blocks;
    }
}
