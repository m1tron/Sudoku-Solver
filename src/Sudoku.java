import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku extends JPanel {
	final int ROWS = 9, COLS = 9, SIZE = 495, ROW_W = SIZE / ROWS, SOLVE = KeyEvent.VK_S, CLEAR = KeyEvent.VK_C;
	int[][] fields = new int[COLS][ROWS];
	Point selection = new Point();

	public static void main(String[] args) {
		JFrame frame = new JFrame("Sudoku Solver");
		frame.add(new Sudoku());
		frame.pack();
		frame.setVisible(true);
	}

	public Sudoku() {
		setPreferredSize(new Dimension(SIZE + 1, SIZE + ROW_W));
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selection =  new Point(e.getPoint().x * COLS / SIZE, e.getPoint().y * ROWS / SIZE);
				requestFocusInWindow(); // Needed to activate key-listener
				repaint();
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (code >= KeyEvent.VK_NUMPAD0 && code <= KeyEvent.VK_NUMPAD9)
					code -= KeyEvent.VK_NUMPAD0 - KeyEvent.VK_0; // Translate numpad input into normal numbers
				if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9 && checkValidMove(selection, code-KeyEvent.VK_0))
                    fields[selection.x][selection.y] = code - KeyEvent.VK_0;
				if (code == SOLVE && findEmptyField() != null) solve();
				if (code == CLEAR) fields = new int[COLS][ROWS];
				repaint();
			}
		});
	}

	Boolean solve() {
		Point field = findEmptyField();
		for (int i = 1; i <= ROWS; i++) {
			fields[field.x][field.y] = i;
			if(checkValidMove(field, i) && ((findEmptyField() == null) || solve())) return true;
		}
		fields[field.x][field.y] = 0; // restore value if backtracking
		return false;
	}

	Point findEmptyField() {
		for (int i = 0; i < COLS*ROWS; i++) if (fields[i%ROWS][i/ROWS] == 0) return new Point(i%ROWS, i/ROWS);
		return null;
	}

	Boolean checkValidMove(Point p, int num) {
		for (int i = 0, x0 = p.x - p.x % 3, y0 = p.y - p.y % 3; i < COLS; i++) {
			if ((fields[i][p.y] == num && i != p.x) || (fields[p.x][i] == num && i != p.y)) return false; // ROW/COLUMN
			if (fields[x0+i%3][y0+i/3] == num && !( x0 + i%3 == p.x && y0 + i/3 == p.y)) return false;
		}
		return true;
	}

    Point ftc(int x, int y) { return new Point(x * SIZE / COLS, y * SIZE / COLS); } // Field To Coordinate

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(100, 50, 50, 50));
		for (int line = 0; line < ROWS; line += 2) {
			g.fillRect(0, line * ROW_W, SIZE, ROW_W); // Rows
			g.fillRect(line * ROW_W, 0, ROW_W, SIZE); // Columns
		}
		g.setColor(Color.BLACK);
		g.drawString("Solve = '" + (char)SOLVE + "'", ROW_W, SIZE + ROW_W /3 - 2);
		g.drawString("Clear = '" + (char)CLEAR + "'", ROW_W, SIZE + ROW_W /3 + 10);
		for (int line = 0; line < ROWS; line += 3) {
			g.drawRect(0, line * ROW_W, SIZE, ROW_W * 3); // border for boxes rows
			g.drawRect(line * ROW_W, 0, ROW_W * 3, SIZE); // border for boxes columns
		}
        for (int k = 0, i = 0, j = 0; k < ROWS* COLS; k++, i = k%COLS, j = k/COLS)
            if (fields[i][j] != 0) g.drawString(""+fields[i][j], ftc(i, j).x+ ROW_W/2, ftc(i, j).y+ ROW_W/2+5);
		g.setColor(new Color(50, 200, 50, 50));
		g.fillRect(ftc(selection.x, selection.y).x, ftc(selection.x, selection.y).y, ROW_W, ROW_W);
	}
}