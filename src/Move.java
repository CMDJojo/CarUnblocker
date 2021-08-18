public record Move(Point a, Point b) {
    public Direction direction() {
        int dx = b.x() - a.x();
        int dy = b.y() - a.y();
        if (dx == 0 && dy == 0) throw new IllegalStateException("Point didn't move");
        if (dx != 0 && dy != 0) throw new IllegalStateException("Diagonal move");
        if (dx < 0) return Direction.LEFT;
        if (dx > 0) return Direction.RIGHT;
        if (dy < 0) return Direction.UP;
        if (dy > 0) return Direction.DOWN;
        
        throw new IllegalStateException("Could not lock direction");
    }
    
    public int distance() {
        return a.x() - b.x() + a.y() - b.y();
    }
}
