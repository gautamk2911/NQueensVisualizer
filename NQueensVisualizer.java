import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NQueensVisualizer extends JPanel {

    private int size;
    int[][] board;
    private static final int CELL_SIZE = 60; 
    private static final int DELAY = 500; 
    private Image queenImage;

    public NQueensVisualizer(int size) {
        this.size = size;
        this.board = new int[size][size];
        try {
            queenImage = ImageIO.read(new File("crownImg.png")); // Load your image file here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(new Color(173, 216, 230)); // Light Blue
                } else {
                    g.setColor(new Color(240, 248, 255)); // Alice Blue
                }
                g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                if (board[row][col] == 1) {
                    g.drawImage(queenImage, col * CELL_SIZE + 10, row * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20, this);
                }
            }
        }
    }

    public void placeQueens(int queens) {
        new Thread(() -> {
            if (solveNQueens(0, queens)) {
                JOptionPane.showMessageDialog(this, "All solutions displayed");
            } else {
                JOptionPane.showMessageDialog(this, "No solutions found");
            }
        }).start();
    }

    private boolean solveNQueens(int row, int queens) {
        if (queens == 0) {
            int[][] solution = new int[size][size];
            for (int i = 0; i < size; i++) {
                solution[i] = board[i].clone();
            }
            displaySolution(solution);
            return true;
        }

        if (row >= size) {
            return false;
        }

        boolean foundSolution = false;

        for (int col = 0; col < size; col++) {
            if (isSafe(row, col)) {
                board[row][col] = 1;
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                foundSolution |= solveNQueens(row + 1, queens - 1);

                board[row][col] = 0;
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return foundSolution;
    }

    private boolean isSafe(int row, int col) {
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 1) {
                return false;
            }
        }

        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        for (int i = row, j = col; i >= 0 && j < size; i--, j++) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    private void displaySolution(int[][] solution) {
        JFrame frame = new JFrame("N-Queens Solution");
        NQueensVisualizer visualizer = new NQueensVisualizer(size);
        visualizer.board = solution;
        frame.add(visualizer);
        frame.setSize(size * CELL_SIZE, size * CELL_SIZE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            Thread.sleep(DELAY * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int size = Integer.parseInt(JOptionPane.showInputDialog("Enter board size:"));
            int queens = Integer.parseInt(JOptionPane.showInputDialog("Enter number of queens:"));

            JFrame frame = new JFrame("N-Queens Visualizer");
            NQueensVisualizer visualizer = new NQueensVisualizer(size);
            frame.add(visualizer);
            frame.setSize(size * CELL_SIZE, size * CELL_SIZE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            visualizer.placeQueens(queens);
        });
    }
}
