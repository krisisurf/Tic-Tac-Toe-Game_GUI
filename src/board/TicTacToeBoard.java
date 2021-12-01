package board;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TicTacToeBoard extends JPanel {

    private final Boolean[] cells; // NULL (no one), TRUE (player1 'O'), FALSE(player2 'X')
    private short cellsFilled = 0;
    private Boolean winner;        // TRUE (player1 'O'), FALSE(player2 'X')

    private int winnerLineStartIndex;
    private Boolean lineDirection; // NULL diagonal, TRUE row, FALSE column

    // Rendering properties
    public final Color player1Color = Color.RED;
    public final Color player2Color = Color.blue;
    public Color gridColor = player1Color;

    public final Color gameOver_Winner1_background = new Color(255, 0, 0, 60);
    public final Color gameOver_Winner2_background = new Color(0, 0, 255, 60);
    public final Color gameOver_Equals_background = new Color(60, 60, 60, 60);

    private final Stroke gridStroke = new BasicStroke(3);
    private final Stroke endLineStroke = new BasicStroke(7);
    private final Font endGameFont= new Font("Calibre", Font.BOLD, 20);


    public TicTacToeBoard(){
        cells = new Boolean[9];
    }

    public void clearBoard(){
        Arrays.fill(cells, null);
        winner = null;
        cellsFilled = 0;

        repaint();
    }

    /**
     *
     * @param index Index of selected cell inside 'cells' array
     * @param value represents player turn -> TRUE (player1 'O'), FALSE(player2 'X')
     * @return returns false if the cell should not be changed (according to the game rules)
     */
    public boolean setCell(int index, boolean value){
        if(cells[index] == null) {
            cells[index] = value;
            cellsFilled++;
            winner = getWinner(index);

            repaint();

            return true;
        }

        return false;
    }

    public boolean cellIsEmpty(int index){
        return cells[index] == null;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int size = Math.min(getWidth(), getHeight());
        int cellSize = getWidth() / 3;
        Font resizedFont = endGameFont.deriveFont((float) ((size * endGameFont.getSize()) / 200));
        g.setFont(resizedFont);
        setSize(size, size);

        //getWidth  endGameFontSize
        //size      neFontSize
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(gridColor);
        drawGrid(g2d, cellSize);
        drawShapes(g, cellSize);

        if(winner != null) {
            drawWinningLine(g2d, cellSize);
            drawGameOver(g);
        } else if(cellsFilled == 9)
            drawGameOver(g);
    }

    private void drawGameOver(Graphics g){

        if(winner == null)
            g.setColor(gameOver_Equals_background);
        else if(winner)
            g.setColor(gameOver_Winner1_background);
        else
            g.setColor(gameOver_Winner2_background);

        g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        String message;
        if(cellsFilled == 9 && winner == null)
            message = "No one wins!";
        else
            message = (winner) ? "Circle player wins!" : "Cross player wins!";

        g.setColor(Color.WHITE);
        drawCenteredString(g, message, new Rectangle(this.getVisibleRect()));
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    private void drawCenteredString(Graphics g, String text, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics();

        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, x, y);
    }

    private void drawWinningLine(Graphics2D g2d, int cellSize){
        g2d.setStroke(endLineStroke);
        g2d.setColor(winner ? player1Color : player2Color);

        int pointBeginning = cellSize / 2 - 10;
        int pointEnding = (cellSize * 3) - cellSize / 2 + 10;

        // Diagonal
        if(lineDirection == null){
            if(winnerLineStartIndex == 0){
                g2d.drawLine(pointBeginning, pointBeginning, pointEnding, pointEnding);
                return;
            }

            g2d.drawLine(pointEnding, pointBeginning, pointBeginning, pointEnding);
            return;
        }

        // Horizontal
        if(lineDirection){
            int row = winnerLineStartIndex / 3;
            int rendY = cellSize * row + cellSize / 2;
            g2d.drawLine(pointBeginning, rendY, pointEnding, rendY);
            return;
        }

        // Vertical
        int rendX = cellSize * (winnerLineStartIndex % 3) + cellSize / 2;
        g2d.drawLine(rendX, pointBeginning, rendX, pointEnding);
    }

    private void drawGrid(Graphics2D g2d, int cellSize){
        g2d.setStroke(gridStroke);

        // Horizontal
        g2d.drawLine(0, cellSize, getWidth(), cellSize);
        g2d.drawLine(0, cellSize * 2, getWidth(), cellSize * 2);

        // Vertical
        g2d.drawLine(cellSize, 0, cellSize, getHeight());
        g2d.drawLine(cellSize * 2, 0, cellSize * 2, getHeight());
    }

    private void drawShapes(Graphics g, int cellSize){
        for(int y = 0, index = 0; y < 3; y++){
            for(int x = 0; x < 3; x++, index++){

                if(cells[index] == null)
                    continue;

                g.setColor(cells[index] ? player1Color : player2Color);

                if(cells[index])
                    g.drawOval(x * cellSize + 10, y * cellSize + 10, cellSize - 20, cellSize - 20);
                else {
                    g.drawLine(x * cellSize + 10, y * cellSize + 10, (x + 1) * cellSize - 10, (y + 1) * cellSize - 10);
                    g.drawLine((x + 1) * cellSize - 10, y * cellSize + 10, x * cellSize + 10, (y + 1) * cellSize - 10);
                }
            }
        }
    }

    public Boolean getWinnerNow(){
        return winner;
    }

    public short getCellsFilled(){
        return cellsFilled;
    }

    private Boolean getWinner(int lastPressedIndex){
        if(lastPressedIndex % 2 == 0){
            lineDirection = null;
            winnerLineStartIndex = 0;
            if(cells[0] != null && cells[0] == cells[4] && cells[0] == cells[8])
                return cells[lastPressedIndex];

            winnerLineStartIndex = 2;
            if(cells[2] != null && cells[2] == cells[4] && cells[2] == cells[6])
                return cells[lastPressedIndex];
        }

        int row = lastPressedIndex / 3;

        if(isWinningRow(row)) {
            return cells[lastPressedIndex];
        }

        int column = lastPressedIndex % 3;
        if(isWinningColumn(column))
            return cells[lastPressedIndex];

        return null;
    }

    private boolean isWinningRow(int row){
        int index = row * 3;
        Boolean forComparison = cells[index];

        for(int i = 1; i < 3; i++)
            if(forComparison != cells[index + i])
                return false;

        winnerLineStartIndex = index;
        lineDirection = true;
        return true;
    }

    private boolean isWinningColumn(int column){
        Boolean forComparison = cells[column];

        for(int i = 1; i < 3; i++)
            if(forComparison != cells[column + i * 3])
                return false;

        winnerLineStartIndex = column;
        lineDirection = false;
        return true;
    }

}
