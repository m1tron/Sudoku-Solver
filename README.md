# A Java Sudoku Brute Force Solver

### How to run
Either compile with 'javac' or run in your favorite IDE using JDK1.8 or newer.

### How it works
1. Select a field with your mouse.
2. Enter a number using keyboard.
3. Repeat step 1-2 to insert a Sudoku puzzle.
4. Press 'S' to Solve, or 'C'  to clear the puzzle.
5. ...
6. Profit!

### Features
- Works on any puzzle that has a solution.
- Implemented in less than 100 lines (84 currently).
- GUI rendered using only Graphics2D functions .drawString, .drawRect, .fillRect
- Mouse click listener using click location arithmetic
- Everything fitted into 100 lines of code

### Under the hood

The program utilizes a brute force approach where every number is tried until a solution is reached. It was developed to 
try out some recursive coding. In more modern terms, the approach could be called a recursive backtracking constraint
search. The project has recently changed focus from a recursive PoC to a Sudoku solver in the least amount of lines.

#### Rules when compacting the code
- Only one statement (;) per line.
- Brackets can be omitted for single line FOR and IF statements.
- One empty line between methods.

#### Recursive function
How it solves using method _Boolean solve()_:
1. Finds first empty field.
2. Insert a number 1-9 in order (for-loop)
   1. When valid move found:
      1. If board solved, return true
      2. Else Recursively call solve(), and return its value (1)
   2. If no valid move:
      4. Clear last move and return false (i.e. backtrack)

This recursive function naively tries the first empty square with the first valid number, then recursively calls itself. This is equivalent to just
solving a Sudoku puzzle by entering numbers into all boxes with the only precondition that any entered number does not break row, column or box uniqueness. 
Whenever it encounters a situation where no valid moves are present, it will backtrack one step, and just continue. This naive approach usually leads to millions of
tries before finding a solution, and might for some very specific low-information and single solution puzzles lead to very long solve times. However,
the usual case is <1ms, and solving an empty board, it turns out, is actually trivial.

### Changes 2025-01-17
- Further condensed IF and FOR statements. 100 -> 84 lines.

### Changes 2025-01-17
- Pruned down redundant print logic
- Clarified logic by using COLUMNS variable in many places (it still equals ROWS)
- Refactored into a single class, saving some constructor logic, and making it a bit more tidy
- Now checks for invalid input, hindering the user from triggering an exhaustive search taking forever
- Removed unused variables
- Condensed key listener logic
- Condensed if statements with simple actions, moving action to same line
- Condensed simple double for-loops by omitting brackets
- Flattened some double for-loops
- Reusing findEmptyField to check for completed condition
- Arguably went to far in condensing logic...

### Future ideas
- Try out a one-dimensional array for board storage (can in theory shorten some logic i.e. double looping)
- Consider maintaining a pointer for current position (avoids expensive lookups with findEmptyField())
