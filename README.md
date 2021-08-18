# CarUnblocker

This program solves Unblock Car-kinds of puzzles, where a board consists of pieces that can be moved either horizontally
or vertically, and where the goal is to move once specific piece out the exit. The solver always finds the solution
requiring the least amount of moves (where multiple consecutive moves of the same piece in the same direction counts as
one). If more than one solution exists, one of them will be chosen arbitrarily. If the program reports that no solution
is found, it means that is has tried all possible moves and couldn't find a solution.

## Running

The program is written in Java and runs on Java 16 (or Java 15 with preview features enabled). Compile all sources
using `javac -d build src/*`, enter the directory with `cd build` and run with `java Parser`.

## Usage

Enter the puzzle, row by row, and use the same character for each part of the same piece. Use uppercase or lowercase "M"
for the marked piece (the one that needs to leave the exit). When you have entered the last row, type "end". Then, enter
the coordinates of where the exit is.

### Example

![Example](example.png)
This puzzle can be entered as

```
  ab
  abcc
mma  b
 ddd b
     b
 aaa
```

There are multiple pieces written as "a" and "b", but that is fine since they are not adjacent. The input is ended
with `end`. Afterwards, a grid appears in the console as

```
  0 1 2 3 4 5
0
1
2
3
4
5
```

The program prompts you for the column and row of the exit, and as seen in the screenshot, the exit is at col 5 row 2.
Then, the program finds a solution in 20 moves, prints the solution, first in coordinate pairs, then in pictures as

```
Move #19
########
#     O#
#      #
#      #
#     X#
#      #
#      #
########
```

where you are supposed to move the piece containing the "O" such as the new position of that "O" is at "X".

## Licenses

The code is released under an MIT license. The screenshot is from a minigame in the [Fancade](https://fancade.com) app
developed by Martin Magni, and the minigame [Blocked](https://fancade.page.link/ajH3) is made by him as well.
