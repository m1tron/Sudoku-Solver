# Sudoku Brute Force Solver

This Sudoku solver lets the user enter a sudoku puzzle into a GUI that can then be solved by the press of a button.
The program utilizes a brute force approach where every number is tried until a solution is reached. It was developed back in 2013 to 
try out some recursive coding. 

What makes this project interesting is what is provided in terms of functionality in comparison to the amount of code. It was written before
any best practices was learned, and thus illuminates what can be achieved by a barebones implementation. Some highlights are:

- Works on any puzzle that has a solution
- GUI representation with key-listeners for input
- Mouse click listener using click location arithmetic to trim away much logic
- GUI rendering using overlapping rectangles. Board effectively painted using 18 rectangles.
- Everything fitted into < 200 lines of code in a single file