package pt.up.fcup.taa.project1;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	private GraphicGrid content = new GraphicGrid();
	
	public Window() {
		this.setTitle("Polygon generation by triangles");
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setContentPane(content);
		this.setVisible(true);
	}
	
	public void print(List<Point> triangle, int x, int y) {
		content.setGrid(triangle, x, y);
		content.repaint();
	}
	
}
