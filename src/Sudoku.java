import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku extends JFrame {
	public static void main(String[] args) {
		new Sudoku("Sudoku");
	}

	public Sudoku(String name) {
		super(name);
		initUI();
	}

	void initUI() {
		// Panels
		JPanel mainPanel = new JPanel();
		JPanel gamePanel = new GamePanel();
		// Here optional UI goes
		mainPanel.add(gamePanel);
		add(mainPanel);

		 //setPreferredSize(new Dimension(550,550));
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
	int debugCounter = 0;
	//JPanel buttonPanel = new JPanel();
	Color backgroundColor = new Color(240, 220, 240);
	Color rowColor = new Color(100, 50, 50, 50);
	Color columnColor = rowColor;// new Color(50, 50, 100, 40);
	Color defaultColor = new Color(230, 255, 255);

	int[][] fields = new int[ROWS][COLUMNS];
	Point selection = new Point();

	public GamePanel() {
		clearField();
		setPreferredSize(new Dimension(XSIZE + BORDER_THICKNESS, YSIZE
				+ BORDER_THICKNESS +ROW_WIDTH/2));
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				p = coordToField(p);
				System.out.println(e);
				selection = p;
				System.out.println("Field: (" + p.x + ", " + p.y + ")");
				System.out.println("Selection: (" + selection.x + ", "
						+ selection.y + ")");

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
					;
				}
				if (code == SOLVE) {
					initRowTest();
					long startTime = System.currentTimeMillis();
						solve();
					long endTime   = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					System.out.println(totalTime);
					
					repaint();
				}
				if (code == CLEAR) {
					clearField();
					repaint();
				}
			}

			private void initRowTest() {
				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < ROWS; j++) {
						if(fields[i][j] != 0){
							//rowTest[i][fields[i][j]-1] = true;
							System.out.println("Row "+ i + " is set as true for number " + fields[i][j]);
						}
					}

				}
				
			}
		});
		
	}
	
	void clearField(){
		//System.out.println("clearfield");
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				fields[i][j] = 0;
				rowTest[i][j] = false;
			}

		}
	}

	Boolean solve() {
		//System.out.println("running solve");
		Point emptyField = findEmptyField();

		for (int i = 1; i <= ROWS; i++) {
			fields[emptyField.x][emptyField.y] = i;
			Boolean isValidMove = checkValidMove(emptyField, i);
			//rowTest[emptyField.x][i-1] = true;
			if(isValidMove){
				if(isFilled())		return true;
				else if(solve()) 	return true;
			}
			else {
				//rowTest[emptyField.x][i-1] = false;
			}


		}
		//rowTest[emptyField.x][fields[emptyField.x][emptyField.y]-1] = false;
		fields[emptyField.x][emptyField.y] = 0;

		return false;
	}
	Boolean isFilled() {
		//System.out.println("running isfilled");
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (fields[i][j] == 0)
					return false;
			}
		}
		return true;
	}

	Point findEmptyField() {
		//System.out.println("running find emptyfield");
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (fields[i][j] == 0)
					return new Point(i, j);
			}
		}
		System.out.println("No empty field");
		return new Point();
	}

	Boolean checkValidMove(Point emptyField, int num) {
		debugCounter++;
		if(debugCounter == 100000000){
			System.out.println("running checkValidMove");
			debugCounter = 0;
		}
		
		// Check COLUMN for similar values		
		for (int i = 0; i < ROWS; i++) {
			if (fields[i][emptyField.y] == num && i != emptyField.x) {
				//System.out.println("ROW ERROR");
				return false;
			}
		} 
		
		//System.out.println("rowTest for row: " + emptyField.x + " and number " + num + ". Which means rowTest[emptyField.x][num-1] equals " + (rowTest[emptyField.x][num-1] ));
		//if(rowTest[emptyField.x][num-1]){
			//System.out.println("ROW ERROR");
			//return false;
		//}
		 //Check ROW for similar values
		for (int i = 0; i < ROWS; i++) {
			if (fields[emptyField.x][i] == num && i != emptyField.y) {
				//System.out.println("COLUMN ERROR");
				return false;
			}
		}

		int startX = emptyField.x - emptyField.x % 3;
		int startY = emptyField.y - emptyField.y % 3;
		for (int i = startX; i < startX + 3; i++) {
			for (int j = startY; j < startY + 3; j++) {
				if (fields[i][j] == num && !( i == emptyField.x && j == emptyField.y)) {
					//System.out.println("BOXERROR");
					return false;
				}
			}
		}
		return true;
	}

	void select(Point p) {
		selection = coordToField(p);
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