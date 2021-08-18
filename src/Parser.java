import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Parser {
    public static void main(String... args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter all piece, using the same character for each part of the same piece");
        System.out.println("Adjacent piece can't use the same character");
        System.out.println("Use 'M' for the marked piece");
        System.out.println("Enter 'end' when finished");
        List<String> input = new ArrayList<>();
        int w = 0;
        String line;
        while (!(line = s.nextLine()).equalsIgnoreCase("end")) {
            w = Math.max(line.length(), w);
            input.add(line);
        }
        int h = input.size();
        
        System.out.print("");
        for (int i = 0; i < w; i++) {
            if (i < 10) System.out.print(' ');
            System.out.print(i);
        }
        System.out.println();
        for (int i = 0; i < h; i++) {
            System.out.println(i);
        }
        
        System.out.print("Enter column for goal: ");
        int goalX = s.nextInt();
        System.out.print("Enter row for goal: ");
        int goalY = s.nextInt();
        
        Point goal = new Point(goalX, goalY);
        Set<Point> occupiedPoints = new HashSet<>();
        Set<Car> cars = new HashSet<>();
        
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                if (input.get(y).charAt(x) == ' ' || occupiedPoints.contains(new Point(x, y))) continue;
                int hz = getHZLen(input.get(y), x);
                int vc = getVCLen(input, x, y);
                if (hz == 1 && vc == 1) throw new IllegalArgumentException("1x1 car at (" + x + "," + y + ")");
                else if (hz > 1 && vc > 1) throw new IllegalArgumentException("wide car at (" + x + "," + y + ")");
                else {
                    Car c = new Car(x, y, Math.max(hz, vc), hz > 1,
                            input.get(y).charAt(x) == 'm' ||
                                    input.get(y).charAt(x) == 'M');
                    cars.add(c);
                    occupiedPoints.addAll(c.pointSet());
                }
            }
        }
        
        Board b = new Board(w, h, cars, goal, false);
        System.out.println("Solving...");
        if(b.hasWon()){
            System.out.println("Puzzle already solved; no moves needed");
            return;
        }
        
        long start = System.nanoTime();
        Optional<Board> sol = Solver.solveNonParallel(b);
        long end = System.nanoTime();
        if (sol.isPresent()) {
            System.out.println("Solution found!");
            System.out.printf("Time taken: %,dns%n", end - start);
            for (Move move : sol.get().getMoves()) {
                System.out.printf("(%d,%d) -> (%d,%d) [%s]%n",
                        move.a().x(), move.a().y(), move.b().x(), move.b().y(), move.direction().name());
            }
            printMoves(sol.get().getMoves(), w, h);
        } else {
            System.err.println("No solution found");
            System.out.printf("Time taken: %,dns%n", end - start);
        }
    }
    
    static void printMoves(Move[] moves, int w, int h) {
        for (int i = 0; i < moves.length; i++) {
            System.out.println("Move #" + (i + 1));
            System.out.println(moveToString(moves[i], w, h));
            System.out.println();
        }
    }
    
    static String moveToString(Move move, int w, int h) {
        StringBuilder sb = new StringBuilder((w + 3) * (h + 2));
        String row = " ".repeat(w);
        sb.append("#".repeat(w + 2));
        sb.append('\n');
        for (int y = 0; y < h; y++) {
            sb.append('#');
            if (y == move.a().y()) {
                if (y == move.b().y()) {
                    int first = Math.min(move.a().x(), move.b().x());
                    int last = Math.max(move.a().x(), move.b().x());
                    char firstc = move.a().x() < move.b().x() ? 'O' : 'X';
                    char lastc = firstc == 'O' ? 'X' : 'O';
                    sb.append(" ".repeat(first));
                    sb.append(firstc);
                    sb.append(" ".repeat(last - first - 1));
                    sb.append(lastc);
                    sb.append(" ".repeat(w - last - 1));
                } else {
                    sb.append(" ".repeat(move.a().x()));
                    sb.append('O');
                    sb.append(" ".repeat(w - move.a().x() - 1));
                }
            } else if (y == move.b().y()) {
                sb.append(" ".repeat(move.a().x()));
                sb.append('X');
                sb.append(" ".repeat(w - move.a().x() - 1));
            } else sb.append(row);
            sb.append("#\n");
        }
        sb.append("#".repeat(w + 2));
        return sb.toString();
    }
    
    static int getHZLen(String s, int x) {
        int c = 1;
        int cx = x;
        while (++cx < s.length() && s.charAt(cx) == s.charAt(x)) c++;
        return c;
    }
    
    static int getVCLen(List<String> ss, int x, int y) {
        int c = 1;
        int cy = y;
        while (++cy < ss.size() && x < ss.get(cy).length() && ss.get(cy).charAt(x) == ss.get(y).charAt(x)) c++;
        return c;
    }
}
