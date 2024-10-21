import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku extends JFrame {
	public static void main(String[] args) {
		new Sudoku("Sudoku");
	}

	public Sudoku(String name) {
		super(name);
		add(new GamePanel());
		setVisible(true);
		pack();
	}
}

class GamePanel extends JPanel {
	static final int ROWS = 9;
	static final int COLUMNS = ROWS;
	static final int XSIZE = 495;
	static final int YSIZE = XSIZE;
	static final int ROW_WIDTH = XSIZE / ROWS;
	static final int HIGHT_PADDING = 5;
	static final int BORDER_THICKNESS = 1;
	static final int SOLVE = KeyEvent.VK_S;
	static final int CLEAR = KeyEvent.VK_C;
	boolean rowTest[][] = new boolean[ROWS][ROWS]; 

	Color rowColor = new Color(100, 50, 50, 50);
	Color columnColor = rowColor;// new Color(50, 50, 100, 40);
	Color defaultColor = new Color(230, 255, 255);

	int[][] fields = new int[ROWS][COLUMNS];
	Point selection = new Point();

	public GamePanel() {
		clearField();
		setPreferredSize(new Dimension(XSIZE + BORDER_THICKNESS, YSIZE
				+ BORDER_THICKNESS +ROW_WIDTH/2 + HIGHT_PADDING));
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				p = coordToField(p);
				selection = p;
				requestFocusInWindow();
				repaint();
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (code >= KeyEvent.VK_NUMPAD0 && code <= KeyEvent.VK_NUMPAD9) {
					code -= KeyEvent.VK_NUMPAD0 - KeyEvent.VK_0;
				}
				if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9) {
					code -= KeyEvent.VK_0;
					fields[selection.x][selection.y] = code;
					repaint();
				}
				if (code == SOLVE) {
					solve();
					repaint();
				}
				if (code == CLEAR) {
					clearField();
					repaint();
				}
			}
		});
	}
	
	void clearField(){
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				fields[i][j] = 0;
				rowTest[i][j] = false;
			}
		}
	}

	Boolean solve() {
		Point emptyField = findEmptyField();
		for (int i = 1; i <= ROWS; i++) {
			fields[emptyField.x][emptyField.y] = i;
			Boolean isValidMove = checkValidMove(emptyField, i);
			if(isValidMove){
				if(isFilled())		return true;
				else if(solve()) 	return true;
			}
		}
		fields[emptyField.x][emptyField.y] = 0;
		return false;
	}

	Boolean isFilled() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (fields[i][j] == 0)
					return false;
			}
		}
		return true;
	}

	Point findEmptyField() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (fields[i][j] == 0)
					return new Point(i, j);
			}
		}
		return new Point();
	}

	Boolean checkValidMove(Point emptyField, int num) {
		// Check COLUMN for similar values		
		for (int i = 0; i < ROWS; i++) {
			if (fields[i][emptyField.y] == num && i != emptyField.x) {
				return false;
			}
		} 

		 //Check ROW for similar values
		for (int i = 0; i < ROWS; i++) {
			if (fields[emptyField.x][i] == num && i != emptyField.y) {
				return false;
			}
		}

		int startX = emptyField.x - emptyField.x % 3;
		int startY = emptyField.y - emptyField.y % 3;
		for (int i = startX; i < startX + 3; i++) {
			for (int j = startY; j < startY + 3; j++) {
				if (fields[i][j] == num && !( i == emptyField.x && j == emptyField.y)) {
					return false;
				}
			}
		}
		return true;
	}

	Point coordToField(Point p) {
		return new Point(p.y * COLUMNS / XSIZE, p.x * COLUMNS / XSIZE);
	}

	Point fieldToCoord(Point p) {
		return new Point(p.y * XSIZE / COLUMNS, p.x * XSIZE / COLUMNS);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(defaultColor);
		g2d.fillRect(0, 0, XSIZE, YSIZE);
		g2d.setColor(rowColor);
		for (int line = 0; line < ROWS; line += 2) {
			g2d.fillRect(0, line * ROW_WIDTH, XSIZE, ROW_WIDTH);
		}

		g2d.setColor(columnColor);
		for (int line = 0; line < COLUMNS; line += 2) {
			g2d.fillRect(line * ROW_WIDTH, 0, ROW_WIDTH, YSIZE);
		}

		g2d.setColor(Color.BLACK);
		g2d.drawString("Solve = '" + (char)SOLVE + "'", ROW_WIDTH, YSIZE + ROW_WIDTH/3 -2);
		g2d.drawString("Clear = '" + (char)CLEAR + "'", ROW_WIDTH, YSIZE + ROW_WIDTH/3 + 10);
		for (int line = 0; line < ROWS; line += 3) {
			g2d.drawRect(0, line * ROW_WIDTH, XSIZE, ROW_WIDTH * 3);
		}

		g2d.setColor(Color.BLACK);
		for (int line = 0; line < ROWS; line += 3) {
			g2d.drawRect(line * ROW_WIDTH, 0, ROW_WIDTH * 3, YSIZE);
		}

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (fields[i][j] != 0) {
					Point temp = fieldToCoord(new Point(i, j));
					g2d.drawString(String.valueOf(fields[i][j]), temp.x
							+ ROW_WIDTH / 2, temp.y + ROW_WIDTH / 2
							+ HIGHT_PADDING);
				}
			}
		}
		g2d.setColor(new Color(50, 200, 50, 50));
		Point temp = fieldToCoord(selection);
		g2d.fillRect(temp.x, temp.y, ROW_WIDTH, ROW_WIDTH);
	}
}