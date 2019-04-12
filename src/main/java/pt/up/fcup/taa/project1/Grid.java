package pt.up.fcup.taa.project1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

/**
 * The Grid class with all the needed backend operations
 *
 * @author Dusan Hetlerovic
 */
public final class Grid {

    private PointType[][] grid;         //grid representation
    private List<Point> vertices;       //ordered list of current vertices
    private List<Point> freePoints;     //list of points outside of the polygon
    private final int MAX_SIZE = 20;    //max height/width of the grid
    private final Random rand = new Random(123456);
    private final Window window = new Window();

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

    /**
     * Creates a default 4x4 grid with a triangle inside
     */
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
        vertices = new ArrayList<>();
        Point a = new Point(1, 1);
        Point b = new Point(1, 2);
        Point c = new Point(2, 2);
        a.setBefore(c);
        a.setAfter(b);
        b.setBefore(a);
        b.setAfter(c);
        c.setBefore(b);
        c.setAfter(a);
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        updateFreePoints();
    }

    /**
     * Find all of the points that could currently be used as a new vertex
     */
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

    /**
     * If a border of the grid is reached, it is expanded in the relevant
     * direction
     *
     * @param b which border to expand
     * @param n how much the border should be expanded
     */
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

    /**
     * Checks whether a point is on any border of the grid
     *
     * @param p point
     * @return border which p lies on or @null if none
     */
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

    /**
     * Sets all points between a, b and c to be considered inside the polygon,
     * hence they can not be chosen as a new vertex. The function identifies the
     * points as the ones which are on the left of all three line segments. Only
     * checks the points inside the triangle's bounding box.
     *
     * @param a point
     * @param b point
     * @param c point
     */
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

    /**
     * Checks whether the angle ABP is positive, negative or zero
     *
     * @param p point
     * @param a point
     * @param b point
     * @return 0 if ABP is a line, 1 if it's a left turn, -1 if right
     */
    private int orientation(Point p, Point a, Point b) {
        return (int) -Math.signum((b.getX() - a.getX()) * (p.getY() - a.getY())
                - (b.getY() - a.getY()) * (p.getX() - a.getX()));
    }

    /**
     * Checks if you can draw a line from a to b without intersecting the
     * existing polygon
     *
     * @param a point
     * @param b point
     * @return true if b is visible from a (and vice versa)
     */
    private boolean isVisible(Point a, Point b) {
        for (int i = 0; i < vertices.size(); ++i) {
            Point c = vertices.get(i);
            Point d = vertices.get((i + 1) % vertices.size());
            if (intersects(a, b, c, d)) {
                return false;
            }
            if (orientation(a, b, c) == 0) {
                if (!b.equals(c)) {
                    return false;
                }
            }
            if (orientation(a, b, d) == 0) {
                if (!b.equals(d)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether AB and CD intersect using orientation cases
     *
     * @param a point
     * @param b point
     * @param c point
     * @param d point
     * @return
     */
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

    /**
     * Adds a vertex to the current polygon.
     *
     * The function first picks a free point P at random, then finds all of the
     * edges visible from this vertex. If there are none, it removes P from list
     * of candidates and picks again. If the list of edges is not empty, it
     * chooses a random edge AB and divides it into AP and PB, keeping the CCW
     * order.
     */
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

            int n = (int) Math.floor(rand.nextDouble() * edges.size());
            Pair<Point, Point> edge = edges.get(n);
            Point a = edge.getKey();
            Point b = edge.getValue();
            List<Point> newVertices = new ArrayList<>();
            if (vertices.indexOf(b) != 0) {
                newVertices.addAll(vertices.subList(0, vertices.indexOf(a) + 1));
                newVertices.add(p);
                newVertices.addAll(vertices.subList(vertices.indexOf(b), vertices.size()));
                vertices = newVertices;
            } else {
                vertices.add(p);
            }

            fillTriangle(a, p, b);
            p.setBefore(a);
            p.setAfter(b);
            expand(isBorder(p), 1);
            updateFreePoints();
            return;
        }
    }

    /**
     * Prints the current grid to the console
     */
    public void printGrid() {
        window.print(vertices, grid.length, grid[0].length);
        for (int i = 0; i < vertices.size(); ++i) {
            grid[vertices.get(i).getX()][vertices.get(i).getY()] = PointType.VERTEX;
        }
        for (int j = 0; j < grid[0].length; ++j) {
            for (int i = 0; i < grid.length; ++i) {
                if (grid[i][j] == PointType.VERTEX) {
                    System.out.print("O");
                } else {
                    System.out.print((grid[i][j] == PointType.OUT) ? "." : "x");
                }
            }
            System.out.println();
        }
        for (int i = 0; i < grid.length + 3; ++i) {
            System.out.print("-");
        }
        System.out.println();
    }

}
