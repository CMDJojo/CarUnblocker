import java.util.HashSet;
import java.util.Set;

public record Car(Point start, int length, boolean horizontal, boolean marked) {
    
    public Car(int x, int y, int length, boolean horizontal) {
        this(x, y, length, horizontal, false);
    }
    
    public Car(int x, int y, int length, boolean horizontal, boolean marked) {
        this(new Point(x, y), length, horizontal, marked);
    }
    
    /**
     * Returns a Point[3] where the first element is the new tile occupied, the 2nd element is the tile no longer
     * occupied, and the 3rd element is the new start of the Car (if you decide to move it) (which for
     * backwards-moving cars is the 1st element)
     *
     * @param forward true if you want to move the car forward (either
     * @return a Point[3] specified above
     */
    public Point[] move(boolean forward) {
        Point[] ret = new Point[3];
        int dx = horizontal ? (forward ? 1 : -1) : 0;
        int dy = horizontal ? 0 : (forward ? 1 : -1);
        
        ret[0] = forward ? new Point(end().x() + dx, end().y() + dy) :
                new Point(start.x() + dx, start.y() + dy);
        ret[1] = forward ? start : end();
        ret[2] = forward ? new Point(start.x() + dx, start().y() + dy) : ret[0];
        return ret;
    }
    
    public Point end() {
        return horizontal ? new Point(start.x() + length - 1, start.y()) :
                new Point(start.x(), start.y() + length - 1);
    }
    
    public Set<Point> pointSet() {
        Set<Point> points = new HashSet<>(length);
        for (int i = 0; i < length; i++) {
            points.add(new Point(start.x() + (horizontal ? 1 : 0) * i,
                    start().y() + (horizontal ? 0 : 1) * i));
        }
        return points;
    }
}
