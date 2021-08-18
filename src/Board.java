import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Board {
    private final int w, h;
    private final Set<Car> cars;
    private final Set<Point> occupied;
    private final Set<Board> explored;
    private final Point goal;
    private final TraceLink<Move> lastMove;
    private final Car markedCar;
    private int hash;
    private boolean hashIsSet;
    private Move[] moves;
    
    public Board(int w, int h, Set<Car> cars, Point goal, boolean threadSafe) {
        this.w = w;
        this.h = h;
        this.cars = cars;
        occupied = new HashSet<>();
        Car markedCar = null;
        for (Car car : cars) {
            for (Point point : car.pointSet()) {
                if (!occupied.add(point))
                    throw new IllegalArgumentException("Colliding point: " + point + " as consequence of " + car);
            }
            if (car.marked()) {
                if (markedCar != null) throw new IllegalArgumentException("More than 1 marked car");
                markedCar = car;
            }
        }
        if (markedCar == null) throw new IllegalArgumentException("No marked car");
        this.markedCar = markedCar;
        this.goal = goal;
        explored = threadSafe ? Collections.synchronizedSet(new HashSet<>()) : new HashSet<>();
        lastMove = null;
    }
    
    public Board(int w, int h, Set<Car> cars, Set<Point> occupied, Set<Board> explored, Car markedCar, Point goal,
                 TraceLink<Move> move) {
        this.w = w;
        this.h = h;
        this.cars = cars;
        this.occupied = occupied;
        this.explored = explored;
        this.markedCar = markedCar;
        this.goal = goal;
        this.lastMove = move;
    }
    
public List<Board> step() {
    List<Board> boards = new ArrayList<>(cars.size() * 2);
    for (Car car : cars) {
        {
            Set<Car> nCars = null;
            Set<Point> nOcc = null;
            Car fwCar = car;
            while (true) {
                Point[] mvs = fwCar.move(true);
                if (!occupied.contains(mvs[0]) &&
                        mvs[0].x() >= 0 && mvs[0].x() < w &&
                        mvs[0].y() >= 0 && mvs[0].y() < w) {
                    nCars = new HashSet<>(Objects.requireNonNullElse(nCars, cars));
                    nOcc = new HashSet<>(Objects.requireNonNullElse(nOcc, occupied));
                    Car nMarked = markedCar;
                    
                    nCars.remove(fwCar);
                    fwCar = new Car(mvs[2], car.length(), car.horizontal(), car.marked());
                    Move move = new Move(car.start(), fwCar.start());
                    nCars.add(fwCar);
                    nOcc.add(mvs[0]);
                    nOcc.remove(mvs[1]);
                    if (car.marked()) nMarked = fwCar;
                    Board nB = new Board(w, h, nCars, nOcc, explored, nMarked, goal,
                            new TraceLink<>(lastMove, move));
                    if (explored.add(nB)) {
                        boards.add(nB);
                    }
                } else break;
            }
        }
        
        {
            Set<Car> nCars = null;
            Set<Point> nOcc = null;
            Car bwCar = car;
            while (true) {
                Point[] mvs = bwCar.move(false);
                if (!occupied.contains(mvs[0]) &&
                        mvs[0].x() >= 0 && mvs[0].x() < w &&
                        mvs[0].y() >= 0 && mvs[0].y() < w) {
                    nCars = new HashSet<>(Objects.requireNonNullElse(nCars, cars));
                    nOcc = new HashSet<>(Objects.requireNonNullElse(nOcc, occupied));
                    Car nMarked = markedCar;
                    
                    nCars.remove(bwCar);
                    bwCar = new Car(mvs[2], car.length(), car.horizontal(), car.marked());
                    Move move = new Move(car.start(), bwCar.start());
                    nCars.add(bwCar);
                    nOcc.add(mvs[0]);
                    nOcc.remove(mvs[1]);
                    if (car.marked()) nMarked = bwCar;
                    Board nB = new Board(w, h, nCars, nOcc, explored, nMarked, goal,
                            new TraceLink<>(lastMove, move));
                    if (explored.add(nB)) {
                        boards.add(nB);
                    }
                } else break;
            }
        }
    }
    return boards;
}
    
    public List<Board> stepOld() {
        List<Board> boards = new ArrayList<>(cars.size() * 2);
        for (Car car : cars) {
            {
                Point[] mvs = car.move(true);
                if (!occupied.contains(mvs[0]) &&
                        mvs[0].x() >= 0 && mvs[0].x() < w &&
                        mvs[0].y() >= 0 && mvs[0].y() < w) {
                    Set<Car> nCars = new HashSet<>(cars);
                    Set<Point> nOcc = new HashSet<>(occupied);
                    Car nMarked = markedCar;
                    
                    Car nCar = new Car(mvs[2], car.length(), car.horizontal(), car.marked());
                    Move move = new Move(car.start(), nCar.start());
                    nCars.remove(car);
                    nCars.add(nCar);
                    nOcc.add(mvs[0]);
                    nOcc.remove(mvs[1]);
                    if (car.marked()) nMarked = nCar;
                    Board nB = new Board(w, h, nCars, nOcc, explored, nMarked, goal,
                            new TraceLink<>(lastMove, move));
                    if (explored.add(nB)) {
                        boards.add(nB);
                    }
                }
            }
            
            {
                Point[] mvs = car.move(false);
                if (!occupied.contains(mvs[0]) &&
                        mvs[0].x() >= 0 && mvs[0].x() < w &&
                        mvs[0].y() >= 0 && mvs[0].y() < w) {
                    Set<Car> nCars = new HashSet<>(cars);
                    Set<Point> nOcc = new HashSet<>(occupied);
                    Car nMarked = markedCar;
                    
                    Car nCar = new Car(mvs[2], car.length(), car.horizontal(), car.marked());
                    Move move = new Move(car.start(), nCar.start());
                    nCars.remove(car);
                    nCars.add(nCar);
                    nOcc.add(mvs[0]);
                    nOcc.remove(mvs[1]);
                    if (car.marked()) nMarked = nCar;
                    Board nB = new Board(w, h, nCars, nOcc, explored, nMarked, goal,
                            new TraceLink<>(lastMove, move));
                    if (explored.add(nB)) {
                        boards.add(nB);
                    }
                }
            }
        }
        return boards;
    }
    
    public Move[] getMoves() {
        if(moves == null) moves = lastMove.toArray();
        return moves;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Board board = (Board) o;
        
        if (w != board.w) return false;
        if (h != board.h) return false;
        return cars.equals(board.cars);
    }
    
    @Override
    public int hashCode() {
        //since a board is immutable, cache the hash (haha that rhymes)
        if(hashIsSet) return hash;
        hash = cars.hashCode();
        hashIsSet = true;
        return hash;
    }
    
    public boolean hasWon() {
        return markedCar.pointSet().contains(goal);
    }
}
