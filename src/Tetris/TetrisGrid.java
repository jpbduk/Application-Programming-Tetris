package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by Jpbere on 19/11/2015.
 */
public class TetrisGrid extends JComponent {
    /*
    This is the main game itself, it uses TetrisTetromino and TetrisBlock primarily as datatypes
    It's variables do the following:
    -gridX, gridY, gridSize are 3 variables used in the same way as the ones in TetrisApplet are, to determine the grid's size in terms of spaces and actual size of each space in pixels.
    -gridColourBG is also from TetrisApplet, it stores the background Color for the grid
    -gridLine likewise is from TetrisApplet
    -blocks is an ArrayList that stores each and every TetrisTetromino Object that isn't the Active Piece, the Ghost Piece or the 3 waiting pieces stored in TetrisWindows
    -fallingSpeed is the time in ms between each timer tick allowing pieces to fall. The smaller the faster. Between 250-500 is optimal for a playable fun experience.
    -delay is how long in ms before the game timer starts.
    -pieceActive is a boolean representation of if there is an Active Piece, such that a new one can be pull fro the waiting if it's needed
    -activePiece and ghostPiece keep track of 1 instance of a piece each which is the manipulatable piece and it's shadow upon the playstate
    -gameStarted and gameOver are 2 flags used to properly play the game and draw it upon the screen
    -upcomingPieces is an arraylist of TetrisWindows called from the TetrisApplet such that they can be used to correctly store pieces
     */
    int gridX,gridY,gridSize;
    Color gridColourBG;
    Boolean gridLine;
    TetrisApplet theApplet;
    ArrayList<TetrisTetromino> blocks = new ArrayList<TetrisTetromino>();
    int fallingSpeed = 333;
    int delay = 1000;
    boolean pieceActive = false;
    TetrisTetromino activePiece;
    TetrisTetromino ghostPiece;
    boolean gameStarted,gameOver = false;
    ArrayList<TetrisWindow> upcomingPieces;
    int intScore = 0;

    TetrisGrid(int x, int y, Color Colour, Boolean hasLine, TetrisApplet apple, int BlockSize)
    {
        /*
        The constructor sets it's parameters to the correct variables for this class
        It then starts a timer called imaginatively, "timer".
        Also it starts a mouseListener called "squeek" since it's shorter than "mouseListener" and less likely to be confused with "MouseListener"
        The upcoming pieces are also pulled from the Tetris Applet since this was a later addition and wasn't in the original parameters
         */
        gridX = x;
        gridY = y;
        gridColourBG = Colour;
        gridLine = hasLine;
        theApplet = apple;
        gridSize = BlockSize;
        Timer timer = new Timer(fallingSpeed, new TimerEvents(this));
        timer.setInitialDelay(delay);
        timer.start();
        MouseEvents squeek = new MouseEvents(this);
        this.addMouseListener(squeek);
        upcomingPieces = theApplet.getWaiting();
        repaint();
    }

    public void paintComponent(Graphics g) {
        /*
        called via repaint()
        This paints in order;
        -The background to the grid
        -The Gridlines if they are wanted to be drawn
        -The Active Piece
        -The Ghost Piece (Which is both Transparent and doesn't have the shading lines of the other pieces)
        -The rest of the dropped blocks
         */
        super.paintComponent(g);
        g.setColor(gridColourBG);
        g.fillRect(0, 0, (gridX * gridSize), (gridY * gridSize));
        Color LineColour = gridColourBG.darker();
        //If wanted, draw the gridLines
        if (gridLine) {
            for (int i = 0; i < gridX; i++) {
                g.setColor(LineColour);
                g.drawLine(i * gridSize, 0, i * gridSize, (gridY * gridSize));
            }
            for (int i = 0; i < gridY; i++) {
                g.setColor(LineColour);
                g.drawLine(0, i * gridSize, (gridX * gridSize), i * gridSize);
            }
        }
        if (gameStarted == true && pieceActive && gameOver == false) {
            for (TetrisBlock e : activePiece.blockList) {
                g.setColor(e.blockColour);
                if (e.blockX >= 0 && e.blockY >= 0) {
                    g.fillRect(e.blockOwner.tGrid.convertX(e.blockX), e.blockOwner.tGrid.convertY(e.blockY), e.blockSize, e.blockSize);
                    g.setColor(e.blockColour.brighter());
                    g.fillRect(e.blockOwner.tGrid.convertX(e.blockX), e.blockOwner.tGrid.convertY(e.blockY), 1, e.blockSize);
                    g.fillRect(e.blockOwner.tGrid.convertX(e.blockX), e.blockOwner.tGrid.convertY(e.blockY), e.blockSize, 1);
                    g.setColor(e.blockColour.darker());
                    g.fillRect(e.blockOwner.tGrid.convertX(e.blockX) + e.blockSize - 1, e.blockOwner.tGrid.convertY(e.blockY), 1, e.blockSize);
                    g.fillRect(e.blockOwner.tGrid.convertX(e.blockX), e.blockOwner.tGrid.convertY(e.blockY) + e.blockSize - 1, e.blockSize, 1);
                }
            }
            for (TetrisBlock e: ghostPiece.blockList){
                Color GhostColour = new Color(e.blockColour.getRed(),e.blockColour.getGreen(),e.blockColour.getBlue(),128);
                g.setColor(GhostColour);
                g.fillRect(e.blockOwner.tGrid.convertX(e.blockX), e.blockOwner.tGrid.convertY(e.blockY), e.blockSize, e.blockSize);
            }
        }
        if (gameStarted == true && gameOver == false) {
            for (TetrisTetromino f: blocks){
                for (TetrisBlock e:f.blockList){
                    g.setColor(e.blockColour);
                    if (e.blockX >= 0 && e.blockY >= 0) {
                        g.fillRect(convertX(e.blockX), convertY(e.blockY), e.blockSize, e.blockSize);
                        g.setColor(e.blockColour.brighter());
                        g.fillRect(convertX(e.blockX), convertY(e.blockY), 1, e.blockSize);
                        g.fillRect(convertX(e.blockX), convertY(e.blockY), e.blockSize, 1);
                        g.setColor(e.blockColour.darker());
                        g.fillRect(convertX(e.blockX) + e.blockSize - 1, convertY(e.blockY), 1, e.blockSize);
                        g.fillRect(convertX(e.blockX), convertY(e.blockY) + e.blockSize - 1, e.blockSize, 1);
                        //Debugging feature that writes on the block co-ordinates on the block, designed when I thought blocks were forgetting where they are
//                        g.setColor(Color.WHITE);
//                        String blockstring = "("+ e.blockX +", "+ e.blockY+ ")";
//                        g.drawString(blockstring, e.blockOwner.tGrid.convertX(e.blockX)+gridSize,e.blockOwner.tGrid.convertY(e.blockY)+gridSize);
                    }
                }
            }
        }
        repaint();
    }

    public boolean checkClear (int x, int y)
            /*
            A function that returns True if the given co-ordinates are both clear of blocks (excluding the active and ghost pieces) and are within the gamespace
             */
    {
        if (!(y < gridY && y >= 0 && x < gridX && x >= 0)){
            return false;
        }
        for (TetrisTetromino i : blocks) {
            for (TetrisBlock j : i.blockList){
                if (j.blockX == x && j.blockY == y)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public int dropPiece (TetrisTetromino piece)
            /*
            A function that returns how many y levels a piece can be dropped before it would collide. This is used in the ghost piece but could have been added to a drop function with ease.
             */
    {
        for (int i = 0;i< gridY;i++)
        {
            for (TetrisBlock e: piece.blockList){
                if (!checkClear(e.blockX, e.blockY +i)){
                    return i;
                }
            }
        }
        return 0;
    }

    public boolean collisionBelow(TetrisTetromino piece){
        /*
        returns true if the space below a piece is NOT clear
         */
        boolean collision = false;
        for (TetrisBlock e: piece.blockList){
            if (!checkClear(e.blockX, e.blockY +1)){
                collision = true;
            }
        }
        return collision;
    }
    public int convertX(int X){
        /*
        Converts grid co-ordinates to pixel/screen co-ordinates used heavily in the painting process
         */

        if (X < 0){
            return (-X* gridSize);
        }
        else
            return (X* gridSize);
    }
    public int convertY(int Y){
        /*
        Converts grid co-ordinates to pixel/screen co-ordinates used heavily in the painting process
         */
        if (Y < 0){
            return (-Y* gridSize);
        }
        else
            return (Y* gridSize);
    }
    public void movePiece(int x, int y){
        /*
        Moves the active piece by the given co-ordinates by calling the active pieces movePiece function
         */
        activePiece.movePiece(x, y);
    }
    public void deleteLine(int y) {
        /*
        When given a Y-layer/row this function will run through the entire row and delete every block within that row by calling the blocks blockDelete function
        Once the line has been cleared, it will look for blocks in rows above/below (Above visually, below numerically) and move their co-ordinates to fill the now emptied row
        It prints it's movements to the console for debugging purposes
         */
        for (TetrisTetromino i : blocks) {
            for (TetrisBlock j : i.blockList) {
                for (int k = 0; k <= gridX; k++) {
                    if (j.blockY == y && j.blockX == k && j.ghostState == false) {
                        intScore += 1;
                        i.blockDelete(k, y);
                    }
                }
            }
        }
        for (TetrisTetromino i : blocks) {
            for (TetrisBlock j : i.blockList) {
                for (int k = 0; k <= gridX; k++) {
                    if (j.blockY <= y && j.blockX == k) {
                        System.out.print("Block (" + j.blockX + "," + j.blockY + ") moved to (");
                        j.blockY += 1;
                        System.out.println(j.blockX + "," + j.blockY + ")");
                    }
                }
            }
        }
    }
    public boolean checkLine(int y){
        /*
        Checks a given row to see if it's full
        returns true if that is true
        false otherwise
         */
        int count = 0;
        for (TetrisTetromino i : blocks)
        {
            for (TetrisBlock j : i.blockList)
            {
                for (int k = 0; k <= gridX; k++)
                {
                    if (j.blockY == y && j.blockX == k)
                    {
                        count++;
                    }
                }
                if (count == gridX){
                    return true;
                }
            }
        }
        return false;
    }
    public void rotatePiece(TetrisTetromino piece){
        /*
        Rotates the blocks around the central block of a piece.
        This is done by taking the piece co-ordinates away from the block co-ordinates to get their relative co-ordinates.
        These relative co-ordinates are between -2 and +2.
        The Relative X co-ordinate then becomes equal to the negative version of the value of the relative Y co-ordinate
        The Relative Y co-ordinate then becomes equal to the original verison of the value of the relative X co-ordinate
        Then the location which the blocks should be moved to is checked for clearness
        If they are ALL clear, checked using a boolean flag, the blocks are moved to their new co-ordinates
         */
        int tBlockX, tBlockY;
        boolean valid = true;
        for (TetrisBlock i: piece.blockList) {
            tBlockX = -(i.blockY - piece.pieceY);
            tBlockY = i.blockX - piece.pieceX;
            if (!checkClear(tBlockX + piece.pieceX, tBlockY + piece.pieceY)) {
                valid = false;
            }
        }
        for (TetrisBlock i: piece.blockList){
            if (valid)
            {
                piece.pieceRotation++;
                tBlockX = -(i.blockY - piece.pieceY);
                tBlockY = i.blockX - piece.pieceX;
                i.blockY = tBlockY+piece.pieceY;
                i.blockX = tBlockX+piece.pieceX;
            }
        }
    }
    public void updateGhost(){
        /*
        Sets the ghostPieces's location to the activePiece
        Re-rotates the ghostPiece to match the activePiece
        Runs drop piece to know how far the ghostPiece can drop
        Moves the ghostPiece down that many times to find it's new position (This runs collision checking again, making sure errors in the dropPiece are caught)
         */
        ghostPiece.setPieceLoc(activePiece.pieceX, activePiece.pieceY);

        while (ghostPiece.pieceRotation < activePiece.pieceRotation){
            rotatePiece(ghostPiece);
        }
        int temp = dropPiece(ghostPiece);
        for (int i = 0; i < temp; i++){
            ghostPiece.movePiece(0, 1);
        }
    }
    public void endGame(){
        /*
        ends the game with a cheery score dialogue
         */
        System.out.println("End game");
        gameStarted = false;
        gameOver = true;
        repaint();
        System.out.println("You scored " + intScore + " points.");
        JOptionPane.showMessageDialog(new JFrame(), "You scored " + intScore + " points.");
    }
}

class MouseEvents implements MouseListener {
    /*
    Inner class of TetrisGrid, this manages all usage of the mouse
    All it get from it's constructor is the TetrisGrid's name such that it can invoke it's functions upon it's variables
    Also prints a message to the console for debugging
     */
    TetrisGrid theGrid;
    MouseEvents(TetrisGrid Grid) {
        theGrid = Grid;
        System.out.println("Mouse listening");
    }
    public void mouseClicked (MouseEvent e) {
        /*When a mouse button is clicked (Pressed and released) this runs various functions dependent on the button pressed;
         -Left Mouse Button (LMB) prints it's press to the log, moves the activepiece left 1 and updates the ghost
         -Middle Mouse Button (MMB) prints it's press to the log, rotates the activepiece and updates the ghost
         -Right Mouse Button (RMB) prints it's press to the log, moves the activepiece right 1 and updates the ghost
         These buttons latter functions only apply if there IS an active piece and the game has started
         */
        if (e.getButton() == MouseEvent.BUTTON1){
            if (theGrid.gameStarted && theGrid.pieceActive)
            {
                theGrid.movePiece(-1, 0);
                theGrid.updateGhost();
            }
            System.out.println("LMB");
        }
        if (e.getButton() == MouseEvent.BUTTON2){
            if (theGrid.gameStarted && theGrid.pieceActive)
            {
                theGrid.rotatePiece(theGrid.activePiece);
                theGrid.updateGhost();
            }
            System.out.println("MMB");
        }
        if (e.getButton() == MouseEvent.BUTTON3){
            if (theGrid.gameStarted && theGrid.pieceActive)
            {
                theGrid.movePiece(1, 0);
                theGrid.updateGhost();
            }
            System.out.println("RMB");
        }
    }
    /*
    Below are 4 necessary overrides to implement MouseListener
    They are just dummy functions
     */
    public void mouseExited (MouseEvent e){
    }
    public void mouseEntered (MouseEvent e){
    }
    public void mouseReleased (MouseEvent e){
    }
    public void mousePressed (MouseEvent e){
    }
}

class TimerEvents implements ActionListener {
    /*
    Inner class of TetrisGrid, this manages all usage of the Timer
    All it get from it's constructor is the TetrisGrid's name such that it can invoke it's functions upon it's variables
     */
    TetrisGrid theGrid;
    TimerEvents(TetrisGrid Grid) {
        theGrid = Grid;
    }

    public void actionPerformed(ActionEvent e) {
        /*
        When the timer action occurs, this function runs
        It checks if the game is not over and then
        Fills the upcomingPieces TetrisWindows, if they are empty
        if there isn't an upcomingPiece it will take the piece from the "Next" window
        It then moves the pieces from each Window to the next, such that the "END" window is empty
        The END window then fills with a random piece
        The game then attempts to place the piece upon the board. If it cannot do so it tries again changing the co-ordinates 30 times.
        If it fails all 30 times, it game overs.
        If it passes it continues
        This sets pieceActive to true to show there is a piece, and creates the ghost of the active piece
         */
        if (!theGrid.gameOver) {
            if (!theGrid.pieceActive) {
                if (!theGrid.gameStarted) {
                    for (TetrisWindow f : theGrid.upcomingPieces) {
                        if (!f.full) {
                            f.setHeldPiece(new TetrisTetromino(randomRange(1, theGrid.gridX - 3), 1, randomRange(1, 7), theGrid));
                        }
                        f.repaint();
                    }
                } else {
                    theGrid.activePiece = theGrid.upcomingPieces.get(2).getHeldPiece();
                    theGrid.upcomingPieces.get(2).setHeldPiece(theGrid.upcomingPieces.get(1).getHeldPiece());
                    theGrid.upcomingPieces.get(1).setHeldPiece(theGrid.upcomingPieces.get(0).getHeldPiece());
                    theGrid.upcomingPieces.get(0).setHeldPiece(new TetrisTetromino(randomRange(1, theGrid.gridX - 3), 1, randomRange(1, 7), theGrid));
                    boolean checked = false;
                    int failCount = 0;
                    while (!checked) {
                        boolean valid = true;
                        for (TetrisBlock f : theGrid.activePiece.blockList) {
                            if (!theGrid.activePiece.tGrid.checkClear(f.blockX, f.blockY)) {
                                valid = false;
                            }
                        }
                        if (!valid) {
                            theGrid.activePiece.setPieceLoc(randomRange(1, theGrid.gridX - 3), 0);
                            failCount++;
                        }
                        if (valid) {
                            checked = true;
                        }
                        if (failCount > 30) {
                            theGrid.endGame();
                            break;
                        }
                    }
                    theGrid.pieceActive = true;
                    theGrid.ghostPiece = new TetrisTetromino(theGrid.activePiece.pieceX, theGrid.dropPiece(theGrid.activePiece), theGrid.activePiece.pieceType, theGrid);
                    theGrid.ghostPiece.makeGhost(true);
                    int rotations = randomRange(0, 3);
                    for (int i = 0; i < rotations; i++) {
                        theGrid.rotatePiece(theGrid.activePiece);
                    }
                    theGrid.updateGhost();
                    theGrid.add(theGrid.ghostPiece);
                    theGrid.add(theGrid.activePiece);
                }
            } else {
                if (!theGrid.collisionBelow(theGrid.activePiece)) {
                    theGrid.activePiece.movePiece(0, 1);
                    theGrid.updateGhost();
                } else {
                    theGrid.blocks.add(theGrid.activePiece);
                    theGrid.pieceActive = false;
                }
            }
            theGrid.gameStarted = true;
            for (int i = 0; i < theGrid.gridY; i++) {
                if (theGrid.checkLine(i)){
                    theGrid.deleteLine(i);
                }
            }
            theGrid.repaint();
        }
    }
    public int randomRange(int min, int max) {
        /*
        Generates a random Number between the min and Max values
         */
        int range = Math.abs(max - min) + 1;
        int returnValue = (int) (Math.random() * range);
        if (min <= max) {
            returnValue += min;
        } else {
            returnValue += max;
        }
        return returnValue;
    }
}
