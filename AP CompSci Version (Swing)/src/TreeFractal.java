import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;

import static java.lang.Math.*;

public class TreeFractal {

	public static DrawingPanel panel = new DrawingPanel(800, 600);
	public static Graphics2D g = (Graphics2D) panel.getGraphics();

	public static void main(String[] args) {
		fixFrame();
		panel.setBackground(Color.BLACK);
		g.setColor(Color.GREEN);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		drawKochTriangle(2, panel.getWidth() / 3, (int) (panel.getHeight() / 1.5), new ArrayList<Double>(), new ArrayList<Double>(), 250.0, g);
		for(int i = 0; i < 20; i++){
			drawTree(i, panel.getWidth() / 2, panel.getHeight() - 100, PI / 2, toRadians(20), 50, .9, 0, false, false, true, panel, g);
			panel.sleep(250);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);
		}
		for(int i = 0; i < 20; i++){
			drawTree(i, panel.getWidth() / 2, panel.getHeight() - 100, PI / 2, toRadians(20), 50, .9, 0, false, true, false, panel, g);
			panel.sleep(250 * 2);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);
		}
		for(int i = 0; i < 19; i++){
			drawTree(i, panel.getWidth() / 2, panel.getHeight() - 100, PI / 2, toRadians(20), 50, .9, 0, true, false, true, panel, g);
			panel.sleep(250 * 2);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);
		}
		long startTime = System.nanoTime();
		drawTree(15, panel.getWidth() / 2, panel.getHeight() - 100, PI / 2, toRadians(20), 50, .9, 1, true, false, true, panel, g);
		System.out.println((System.nanoTime() - startTime) / 1000000.0 + " branches");
		panel.sleep(250 * 2);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);

		for(int i = 0; i < 19; i++){
			drawTree(i, panel.getWidth() / 2, panel.getHeight() - 100, PI / 2, toRadians(20), 50, .9, 0, true, true, false, panel, g);
			panel.sleep(250 * 2);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);
		}
		drawTree(15, panel.getWidth() / 2, panel.getHeight() - 100, PI / 2, toRadians(20), 50, .9, 1, true, true, false, panel, g);
	}

	public static void drawTree(int iterations, int x, int y, double initAngle, double halfAngle, double initSize, double sizeMultiplier, int timeDelayInMillis, boolean drawBackwards,boolean randomizeColors, boolean modColors,  DrawingPanel panel, Graphics g) {
		if(iterations == 0) {
			return;
		}
		if(!drawBackwards){
			if(randomizeColors && (randomizeColors ^ modColors)){
				g.setColor(randomColorMinusBlack());
			}
			else if(modColors && (randomizeColors ^ modColors)){
				g.setColor(numToColor((iterations - 1)% 12));
			}
			else{
				g.setColor(Color.GREEN);
			}
			g.drawLine(x, y, (int) (x + cos(initAngle) * initSize), (int) (y - sin(initAngle) * initSize));
		}

		panel.sleep(timeDelayInMillis);
		drawTree(iterations - 1, (int) (x + cos(initAngle) * initSize), (int) (y - sin(initAngle) * initSize), initAngle + halfAngle, halfAngle, initSize * sizeMultiplier, sizeMultiplier, timeDelayInMillis, drawBackwards, randomizeColors, modColors, panel, g);
		drawTree(iterations - 1, (int) (x + cos(initAngle) * initSize), (int) (y - sin(initAngle) * initSize), initAngle - halfAngle, halfAngle, initSize * sizeMultiplier, sizeMultiplier, timeDelayInMillis, drawBackwards, randomizeColors, modColors, panel, g);
		if(drawBackwards){
			if(randomizeColors && (randomizeColors ^ modColors)){
				g.setColor(randomColorMinusBlack());
			}
			else if(modColors && (randomizeColors ^ modColors)){
				g.setColor(numToColor((iterations - 1) % 12));
			}
			else{
				g.setColor(Color.GREEN);
			}
			g.drawLine(x, y, (int) (x + cos(initAngle) * initSize), (int) (y - sin(initAngle) * initSize));
		}
	}

	public static void drawKochTriangle(int i, int x, int y, ArrayList<Double> xAry, ArrayList<Double> yAry,  double size, Graphics g) {
		xAry.add(x + cos(PI / 3) * size);
		yAry.add(y - sin(PI / 3) * size);
		xAry.add(x + size);
		yAry.add((double) y);
		xAry.add((double) x);
		yAry.add((double) y);
		genKochTriangle(i - 1, 1, x, y, xAry, yAry, size);
		int[] polyX = new int[xAry.size()];
		int[] polyY = new int[yAry.size()];
		for(int forLoopIndex = 0; forLoopIndex < xAry.size(); forLoopIndex++) {
			polyX[forLoopIndex] = (int) round(xAry.get(forLoopIndex));
			polyY[forLoopIndex] = (int) round(yAry.get(forLoopIndex));
		}
		g.drawPolygon(new Polygon(polyX, polyY, polyX.length));
	}
	
	public static void genKochTriangle(int i, int index, int x, int y, ArrayList<Double> xAry, ArrayList<Double> yAry, double size) {
		if(i < 0) {
			return;
		}
//		xAry.add(index, size);
	}

	public static Color randomColorMinusBlack() {
		int randomNum = (int) (random() * 12);
		return numToColor(randomNum);
	}

	public static Color numToColor(int num) {
		switch (num) {
		case (0):
			return Color.WHITE;
		case (1):
			return Color.BLUE;
		case (2):
			return Color.RED;
		case (3):
			return Color.DARK_GRAY;
		case (4):
			return Color.GREEN;
		case (5):
			return Color.MAGENTA;
		case (6):
			return Color.ORANGE;
		case (7):
			return Color.YELLOW;
		case (8):
			return Color.GRAY;
		case (9):
			return Color.PINK;
		case (10):
			return Color.CYAN;
		case (11):
			return Color.LIGHT_GRAY;
		case (12):
			return Color.BLACK;
		default:
			return null;
		}
	}

	public static void fixFrame() {
		try {
			java.lang.reflect.Field fieldFrame = panel.getClass().getDeclaredField("frame");
			fieldFrame.setAccessible(true);
			JFrame frame = (JFrame) fieldFrame.get(panel);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
