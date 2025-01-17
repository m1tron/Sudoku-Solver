# Sudoku Brute Force Solver

This Sudoku solver lets the user enter a sudoku puzzle into a GUI that can then be solved by the press of a button.
The program utilizes a brute force approach where every number is tried until a solution is reached. It was developed back in 2013 to 
try out some recursive coding. 

What makes this project interesting is what is provided in terms of functionality in comparison to the amount of code. It was written before
any best practices was learned, and thus illuminates what can be achieved by a barebone implementation. Some highlights are:

- Works on any puzzle that has a solution
- GUI representation with key-listeners for input
- Mouse click listener using click location arithmetic to trim away much logic
- GUI rendering using overlapping rectangles. Board effectively painted using 18 rectangles.
- Everything fitted into < 200 lines of code

How it solves using method _Boolean solve()_:
1. Finds first empty field.
2. Try every number in order iteratively (loop)
   3. When valid move found:
      1. If board filled, return true
      2. Else Recursively call, and return its value (1)
   3. If no valid move:
      4. Clear last move and return false (i.e. backtrack)

This recursive function naively tries the first empty square with the first valid number, then recursively calls itself. This is equivalent to just
solving a Sudoku puzzle by entering numbers into all boxes with the only precondition that any entered number does not break row, column or box uniqueness. 
Whenever it encounters a situation where no valid moves are present, it will backtrack, and just continue. This naive approach usually leads to millions of
tries before finding a solution, and might for some very specific low-information and single solution puzzles lead to very long solve times. However,
the usual case is <1ms, and solving an empty board, it turns out, is actually trivial.

### Future work
- Condense program even more
