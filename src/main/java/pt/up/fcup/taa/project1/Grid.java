package pt.up.fcup.taa.project1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ayy lmao
 */
public class Grid {

    private PointType[][] grid;
    private List<Point> vertices = new ArrayList<>();

    public Grid(int n, int m) {
        grid = new PointType[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                grid[i][j] = PointType.OUT;
            }
        }
    }

    public void expand(Border b, int n) {
        PointType[][] newGrid;
        switch (b) {

            case TOP:
                newGrid = new PointType[grid.length][grid[0].length + n];
                for (int i = 0; i < grid.length; ++i) {
                    for (int j = 0; j < n; ++j) {
                        newGrid[i][j] = PointType.OUT;
                    }
                    for (int j = 0; j < grid[0].length; ++j) {
                        newGrid[i][j + n] = grid[i][j];
                    }
                }
                grid = newGrid;
                updateVertices(b, n);
                break;

            case BOTTOM:
                newGrid = new PointType[grid.length][grid[0].length + n];
                for (int i = 0; i < grid.length; ++i) {
                    for (int j = 0; j < grid[0].length; ++j) {
                        newGrid[i][j] = grid[i][j];
                    }
                    for (int j = 0; j < n; ++j) {
                        newGrid[i][j + grid[0].length] = PointType.OUT;
                    }
                }
                grid = newGrid;
                break;

            case LEFT:
                newGrid = new PointType[grid.length + n][grid[0].length];
                for (int i = 0; i < n; ++i) {
                    for (int j = 0; j < grid[0].length; ++j) {
                        newGrid[i][j] = PointType.OUT;
                    }
                }
                for (int i = 0; i < grid.length; ++i) {
                    for (int j = 0; j < grid[0].length; ++j) {
                        newGrid[i + n][j] = grid[i][j];
                    }
                }
                grid = newGrid;
                updateVertices(b, n);
                break;

            case RIGHT:
                newGrid = new PointType[grid.length + n][grid[0].length];
                for (int i = 0; i < grid.length; ++i) {
                    for (int j = 0; j < grid[0].length; ++j) {
                        newGrid[i][j] = grid[i][j];
                    }
                }
                for (int i = 0; i < n; ++i) {
                    for (int j = 0; j < grid[0].length; ++j) {
                        newGrid[i + grid.length][j] = PointType.OUT;
                    }
                }
                grid = newGrid;
        }
    }

    public void updateVertices(Border b, int n) {
        switch (b) {
            case TOP:
                for (Point p : vertices) {
                    p.setY(p.getY() + n);
                }
                break;
            case LEFT:
                for (Point p : vertices) {
                    p.setX(p.getX() + n);
                }
                break;
        }
    }

    public Border isBorder(Point p) {
        if (p.getX() == 0) {
            return Border.LEFT;
        } else if (p.getX() == grid.length - 1) {
            return Border.RIGHT;
        } else if (p.getY() == 0) {
            return Border.TOP;
        } else if (p.getY() == grid[0].length - 1) {
            return Border.BOTTOM;
        } else {
            return null;
        }
    }

}
