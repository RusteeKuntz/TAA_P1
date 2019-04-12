package pt.up.fcup.taa.project1;

/**
 * Point class
 *
 * @author Dusan Hetlerovic, Vincent Lopes
 */
public class Point {

    private int x, y;
    private Point before, after;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.x;
        hash = 59 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    public Point getBefore() {
        return before;
    }

    public void setBefore(Point before) {
        this.before = before;
    }

    public Point getAfter() {
        return after;
    }

    public void setAfter(Point after) {
        this.after = after;
    }

}
