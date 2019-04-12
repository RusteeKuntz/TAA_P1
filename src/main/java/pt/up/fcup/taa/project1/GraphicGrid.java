package pt.up.fcup.taa.project1;

import java.awt.Graphics;
import java.util.List;
import java.awt.Color;
import javax.swing.JPanel;

public class GraphicGrid extends JPanel {

    List<Point> grid = null;
    int lenx, leny;

    public GraphicGrid() {
    }

    @Override
    public void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        for (Point p : grid) {
            g.drawString(String.valueOf(grid.indexOf(p) + 1), w * p.getX() / lenx + 3, h * p.getY() / leny + 3);
            g.drawLine(w * p.getBefore().getX() / lenx, h * p.getBefore().getY() / leny, w * p.getX() / lenx, h * p.getY() / leny);
            g.drawLine(w * p.getX() / lenx, h * p.getY() / leny, w * p.getAfter().getX() / lenx, h * p.getAfter().getY() / leny);
            g.drawLine(w*p.getAfter().getX()/lenx, h*p.getAfter().getY()/leny, w*p.getBefore().getX()/lenx, h*p.getBefore().getY()/leny);
        }
    }

    public void setGrid(List<Point> grid, int x, int y) {
        this.grid = grid;
        lenx = x;
        leny = y;
    }
}
