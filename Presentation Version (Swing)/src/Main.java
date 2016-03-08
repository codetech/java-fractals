import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JFrame
{

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			Main main = new Main();
			main.setVisible(true);
		});
	}

	public Main()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		PythagorasTree panel = new PythagorasTree();
		add(panel);
		panel.repaint();
	}

}

class PythagorasTree extends JPanel
{

	public int x, y;
	public double halfAngle;
	public double size;
	public double sizeModifier;

	public PythagorasTree()
	{
		setSize(800, 600);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawTree(g, 15, getWidth() / 2, getHeight() - 100, toRadians(90), toRadians(20), 100, 0.75);
	}

	public void drawTree(Graphics g, int iterations, int x, int y, double angle, double halfAngle, double size,
			double sizeModifier)
	{
		if (iterations > 0)
		{
			int xPrime = (int) (x + cos(angle) * size);
			int yPrime = (int) (y - sin(angle) * size);
			g.setColor(Color.BLUE);

			g.drawLine(x, y, xPrime, yPrime);
			drawTree(g, iterations - 1, xPrime, yPrime, angle + halfAngle, halfAngle, size * sizeModifier,
					sizeModifier);
			drawTree(g, iterations - 1, xPrime, yPrime, angle - halfAngle, halfAngle, size * sizeModifier,
					sizeModifier);
		}
	}

}