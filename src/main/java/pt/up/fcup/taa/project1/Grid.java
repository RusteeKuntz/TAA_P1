package pt.up.fcup.taa.project1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

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
    private final int MAX_SIZE = 20;
    private final Random rand = new Random(12345);

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
        if (b == null) {
            return;
        }
        if (b == Border.LEFT || b == Border.RIGHT) {
            if (grid.length + n > MAX_SIZE) {
                return;
            }
        } else if (grid[0].length + n > MAX_SIZE) {
            return;
        }
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
                for (Point p : vertices) {
                    p.setY(p.getY() + n);
                }
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
                for (Point p : vertices) {
                    p.setX(p.getX() + n);
                }
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
        }
        return null;
    }

    private void fillTriangle(Point a, Point b, Point c) {
        int xMin = Math.min(a.getX(), Math.min(b.getX(), c.getX()));
        int xMax = Math.max(a.getX(), Math.max(b.getX(), c.getX()));
        int yMin = Math.min(a.getY(), Math.min(b.getY(), c.getY()));
        int yMax = Math.max(a.getY(), Math.max(b.getY(), c.getY()));
        for (int x = xMin; x <= xMax; ++x) {
            for (int y = yMin; y <= yMax; ++y) {
                Point p = new Point(x, y);
                if (orientation(p, a, b) >= 0
                        && orientation(p, b, c) >= 0
                        && orientation(p, c, a) >= 0) {
                    grid[x][y] = PointType.IN;
                }
            }
        }
    }

    private float orientation(Point p, Point a, Point b) {
        return Math.signum((b.getX() - a.getX()) * (p.getY() - a.getY())
                - (b.getY() - a.getY()) * (p.getX() - a.getX()));
    }

    private boolean isVisible(Point a, Point b) {
        for (int i = 0; i < vertices.size(); ++i) {
            Point c = vertices.get(i);
            Point d = vertices.get((i + 1) % vertices.size());
            if (intersects(a, b, c, d)) {
                return false;
            }
        }
        return true;
    }

    private boolean intersects(Point a, Point b, Point c, Point d) {
        float o1 = orientation(c, a, b);
        float o2 = orientation(d, a, b);
        float o3 = orientation(a, c, d);
        float o4 = orientation(b, c, d);

        if (o1 == 0 && o2 == 0) {
            int minX1 = Math.min(a.getX(), b.getX());
            int minX2 = Math.min(c.getX(), d.getX());
            int maxX1 = Math.max(a.getX(), b.getX());
            int maxX2 = Math.max(c.getX(), d.getX());
            if (minX1 < minX2) {
                return maxX1 >= minX2;
            } else {
                return maxX2 >= minX1;
            }
        }

        return (o1 == -o2) && (o3 == -o4);
    }

    public void addVertex() {
        Point p;
        List<Pair<Point, Point>> edges = new ArrayList<>();
        while (!freePoints.isEmpty()) {
            p = freePoints.get((int) Math.floor(rand.nextDouble() * freePoints.size()));
            for (int i = 0; i < vertices.size(); ++i) {
                Point a = vertices.get(i);
                Point b = vertices.get((i + 1) % vertices.size());
                if (isVisible(p, a) && isVisible(p, b)) {
                    edges.add(new Pair(a, b));
                }
            }

            if (edges.isEmpty()) {
                freePoints.remove(p);
                edges.clear();
                continue;
            }

            Pair<Point, Point> edge = edges.get((int) Math.floor(rand.nextDouble() * freePoints.size()));
            Point a = edge.getKey();
            Point b = edge.getValue();
            List<Point> newVertices = new ArrayList<>();
            newVertices.addAll(vertices.subList(0, vertices.indexOf(a)));
            newVertices.add(p);
            newVertices.addAll(vertices.subList(vertices.indexOf(b), vertices.size() - 1));
            vertices = newVertices;

            fillTriangle(a, p, b);
            expand(isBorder(p), 1);
            updateFreePoints();
            return;
        }
    }

    public void printGrid() {
        for (int j = 0; j < grid[0].length; ++j) {
            for (int i = 0; i < grid.length; ++i) {
                System.out.print((grid[i][j] == PointType.OUT) ? "." : "X");
            }
            System.out.println();
        }
        for (int i = 0; i < grid.length; ++i) {
            System.out.print("-");
        }
        System.out.println();
    }

}
