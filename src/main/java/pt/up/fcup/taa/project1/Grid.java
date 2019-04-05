package pt.up.fcup.taa.project1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * TODO choose point, check line visibility
 * 
 * @author Ayy lmao
 */
public final class Grid {

    private PointType[][] grid;
    private List<Point> vertices;
    private List<Point> freePoints;

    //deprecated
    public Grid(int n, int m) {
        grid = new PointType[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                grid[i][j] = PointType.OUT;
            }
        }
        vertices = new ArrayList();
    }

    //default starting grid
    public Grid() {
        grid = new PointType[4][4];
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                grid[i][j] = PointType.OUT;
            }
        }
        grid[1][1] = PointType.IN;
        grid[1][2] = PointType.IN;
        grid[2][2] = PointType.IN;
        vertices.add(new Point(1, 1));
        vertices.add(new Point(1, 2));
        vertices.add(new Point(2, 2));
        updateFreePoints();
    }

    public void updateFreePoints() {
        freePoints = new ArrayList<>();
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                if (grid[i][j] == PointType.OUT) {
                    freePoints.add(new Point(i, j));
                }
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
                break;

            default:
                newGrid = grid;
        }
        grid = newGrid;
        updateFreePoints();
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

    private void fillTriangle(Point a, Point b, Point c) {
        int xMin = Math.min(a.getX(), Math.min(b.getX(), c.getX()));
        int xMax = Math.max(a.getX(), Math.max(b.getX(), c.getX()));
        int yMin = Math.min(a.getY(), Math.min(b.getY(), c.getY()));
        int yMax = Math.max(a.getY(), Math.max(b.getY(), c.getY()));
        for (int x = xMin; x <= xMax; ++x) {
            for (int y = yMin; y <= yMax; ++y) {
                Point p = new Point(x, y);
                if (isLeftOf(p, a, b) && isLeftOf(p, b, c) && isLeftOf(p, c, a)) {
                    grid[x][y] = PointType.IN;
                }
            }
        }
    }

    private boolean isLeftOf(Point p, Point a, Point b) {
        return 0 <= Math.signum((b.getX() - a.getX()) * (p.getY() - a.getY())
                - (b.getY() - a.getY()) * (p.getX() - a.getX()));
    }

}
