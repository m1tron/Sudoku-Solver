import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku extends JPanel {
	final int ROWS = 9, COLUMNS = 9, SIZE = 495, ROW_WIDTH = SIZE / ROWS, SOLVE = KeyEvent.VK_S, CLEAR = KeyEvent.VK_C;
	int[][] fields = new int[COLUMNS][ROWS];
	Point selection = new Point();

	public static void main(String[] args) {
		JFrame frame = new JFrame("Sudoku Solver");
		frame.add(new Sudoku());
		frame.pack();
		frame.setVisible(true);
	}

	public Sudoku() {
		setPreferredSize(new Dimension(SIZE + 1, SIZE + ROW_WIDTH));
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selection =  new Point(e.getPoint().x * COLUMNS / SIZE, e.getPoint().y * ROWS / SIZE);
				requestFocusInWindow(); // Needed to activate key-listener
				repaint();
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (code >= KeyEvent.VK_NUMPAD0 && code <= KeyEvent.VK_NUMPAD9) {
					code -= KeyEvent.VK_NUMPAD0 - KeyEvent.VK_0; // Translate numpad input into normal numbers
				}
				if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9) {
					code -= KeyEvent.VK_0;
					if(checkValidMove(selection, code)) fields[selection.x][selection.y] = code;
				}
				if (code == SOLVE && findEmptyField() != null) solve();
				if (code == CLEAR) fields = new int[COLUMNS][ROWS];
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
		for (int i = 0; i < COLUMNS*ROWS; i++)
			if (fields[i%ROWS][i/ROWS] == 0) return new Point(i%ROWS, i/ROWS);
		return null;
	}

	Boolean checkValidMove(Point p, int num) {
		int startX = p.x - p.x % 3, startY = p.y - p.y % 3;
		for (int i = 0; i < COLUMNS; i++) {// Check COLUMN and ROWS and 3x3 boxes for similar values
			if ((fields[i][p.y] == num && i != p.x) || (fields[p.x][i] == num && i != p.y)) return false; // ROW/COLUMN
			if (fields[startX+i%3][startY+i/3] == num && !( startX + i%3 == p.x && startY + i/3 == p.y)) return false;
		}
		return true;
	}

	Point fieldToCoord(Point p) {
		return new Point(p.x * SIZE / COLUMNS, p.y * SIZE / COLUMNS);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(100, 50, 50, 50));
		for (int line = 0; line < ROWS; line += 2) {
			g.fillRect(0, line * ROW_WIDTH, SIZE, ROW_WIDTH); // Rows
			g.fillRect(line * ROW_WIDTH, 0, ROW_WIDTH, SIZE); // Columns
		}
		g.setColor(Color.BLACK);
		g.drawString("Solve = '" + (char)SOLVE + "'", ROW_WIDTH, SIZE + ROW_WIDTH/3 -2);
		g.drawString("Clear = '" + (char)CLEAR + "'", ROW_WIDTH, SIZE + ROW_WIDTH/3 + 10);
		for (int line = 0; line < ROWS; line += 3) {
			g.drawRect(0, line * ROW_WIDTH, SIZE, ROW_WIDTH * 3); // border for boxes rows
			g.drawRect(line * ROW_WIDTH, 0, ROW_WIDTH * 3, SIZE); // border for boxes columns
		}
		for (int i = 0; i < ROWS*COLUMNS; i++) //
				if (fields[i%COLUMNS][i/ROWS] != 0) {
					Point temp = fieldToCoord(new Point(i%COLUMNS, i/ROWS));
					g.drawString(String.valueOf(fields[i%COLUMNS][i/ROWS]), temp.x+ROW_WIDTH/2, temp.y+ROW_WIDTH/2+5);
				}
		g.setColor(new Color(50, 200, 50, 50));
		g.fillRect(fieldToCoord(selection).x, fieldToCoord(selection).y, ROW_WIDTH, ROW_WIDTH);
	}
}