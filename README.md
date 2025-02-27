# Sudoku Brute Force Solver

This Sudoku solver lets the user enter a sudoku puzzle into a GUI that can then be solved by the press of a button.
The program utilizes a brute force approach where every number is tried until a solution is reached. It was developed back in 2013 to 
try out some recursive coding. In more modern terms, the program could be called a recursive backtracking constraint
search.

What makes this project interesting is what is provided in terms of functionality in comparison to the amount of code. It was written before
any best practices were learned, and illustrates what can be achieved by a barebone implementation. Some highlights are:

- Works on any puzzle that has a solution
- Easily portable to any language or frameworks that exposes a canvas to draw (basically any language)
- Compatible all the way back to at least java 8
- GUI rendered using only Graphics2D functions .drawString, .drawRect, .fillrect
- Mouse click listener using click location arithmetic
- Everything fitted into 100 lines of code

How it solves using method _Boolean solve()_:
1. Finds first empty field.
2. Try every number in order iteratively (loop)
   3. When valid move found:
      1. If board complete, return true
      2. Else Recursively call solve(), and return its value (1)
   3. If no valid move:
      4. Clear last move and return false (i.e. backtrack)

This recursive function naively tries the first empty square with the first valid number, then recursively calls itself. This is equivalent to just
solving a Sudoku puzzle by entering numbers into all boxes with the only precondition that any entered number does not break row, column or box uniqueness. 
Whenever it encounters a situation where no valid moves are present, it will backtrack, and just continue. This naive approach usually leads to millions of
tries before finding a solution, and might for some very specific low-information and single solution puzzles lead to very long solve times. However,
the usual case is <1ms, and solving an empty board, it turns out, is actually trivial.

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
