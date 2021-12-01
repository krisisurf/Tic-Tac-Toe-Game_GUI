package game;

import board.TicTacToeBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TicTacToeGameController {
    // Display
    private JFrame frame;
    private Color backgroundColor = Color.WHITE;

    // Game
    private TicTacToeBoard board;

    public TicTacToeGameController(int width, int height) {
        initDisplay("Sea Chess by Kristian Dimitrov, 11v, No.15", width, height);

        gameLogic();
    }

    private void gameLogic() {
        board.addMouseListener(new MouseListener() {
            boolean isPlayer1Turn = true;
            
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3 || board.getCellsFilled() == 9 || board.getWinnerNow() != null) {
                    board.clearBoard();
                    return;
                }
                
                // Calculating which cell from the grid is pressed
                int xPressed = e.getX();
                int yPressed = e.getY();

                int cellSize = board.getWidth() / 3;

                int cellX = xPressed / cellSize;
                int cellY = yPressed / cellSize;

                int cellPressed = cellY * 3 + cellX;

                
                // Setting the pressed cell
                if (!board.cellIsEmpty(cellPressed))
                    return;

                if(!board.setCell(cellPressed, isPlayer1Turn)){
                    return;
                }

                isPlayer1Turn = !isPlayer1Turn;
                board.gridColor = (isPlayer1Turn) ? board.player1Color : board.player2Color;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void initDisplay(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setMinimumSize(new Dimension(200, 200));
        frame.setMaximumSize(new Dimension(800, 800));
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new TicTacToeBoard();
        frame.add(board);
        board.setLocation(0, 0);

        frame.setVisible(true);
    }
}
