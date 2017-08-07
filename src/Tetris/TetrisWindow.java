package Tetris;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jpbere on 02/12/2015.
 */
public class TetrisWindow extends JComponent{
    /*
    The TetrisWindow is a class created to be a multipurpose piece of User Interface for the application. They display a title and either control instructions or a tetrisTetromino
    Their Variables determine which piece is held (heldPiece) and if it is holding a piece (full)
    xSize and ySize determine the size of the window
    winTitle gives the text to be rendered on the top of the box
    theApplet and theGrid simply store the instance of each class that created it
     */
    TetrisTetromino heldPiece;
    int xSize, ySize;
    String winTitle;
    boolean full;
    TetrisApplet theApplet;
    TetrisGrid theGrid;

    public TetrisWindow(int x, int y, String name, TetrisApplet apple, TetrisGrid grid){
        /*
        Constructor initialises variables
         */
        xSize = x;
        ySize = y;
        winTitle = name;
        full = false;
        theApplet = apple;
        theGrid = grid;
    }
    public void setHeldPiece(TetrisTetromino piece){
        /*
        Changes the heldPiece by the parameter piece, and sets full to true
         */
        heldPiece = piece;
        full = true;
        repaint();
    }
    public TetrisTetromino getHeldPiece(){
        /*
        Returns the heldPiece
         */
        repaint();
        return heldPiece;
    }
    public void paintComponent(Graphics g) {
        /*
        The paint method for this class, not as adaptable as the TetrisGrid method
        First draws a filled rectangle of Light Grey
        The draws 2 non-filled rectangles around the box to give it a thick outline
        If it has a piece it then draws the piece within the box
        It then draws a dark grey title box
        The fills it with the title in white
        If the title was "CTRL" (control) it then renders the game controls
         */
        g.setColor(new Color(192, 192, 192));
        g.fillRect(0, 0, xSize, ySize);
        g.setColor(new Color(64, 64, 64));
        g.drawRect(0, 0, xSize - 2, ySize);
        g.drawRect(1, 1, xSize - 3, ySize - 1);
        if (full) {
            for (TetrisBlock e : heldPiece.blockList) {
                g.setColor(e.blockColour);
                if (e.blockX >= 0 && e.blockY >= 0) {
                    g.fillRect(convertX((e.blockX-e.blockOwner.pieceX) + 3), convertY((e.blockY-e.blockOwner.pieceY) + 3), e.blockSize, e.blockSize);
                    g.setColor(e.blockColour.brighter());
                    g.fillRect(convertX((e.blockX - e.blockOwner.pieceX) + 3), convertY((e.blockY - e.blockOwner.pieceY) + 3), 1, e.blockSize);
                    g.fillRect(convertX((e.blockX - e.blockOwner.pieceX) + 3), convertY((e.blockY - e.blockOwner.pieceY) + 3), e.blockSize, 1);
                    g.setColor(e.blockColour.darker());
                    g.fillRect(convertX((e.blockX - e.blockOwner.pieceX) + 3) + e.blockSize - 1, convertY((e.blockY - e.blockOwner.pieceY) + 3), 1, e.blockSize);
                    g.fillRect(convertX((e.blockX - e.blockOwner.pieceX) + 3), convertY((e.blockY-e.blockOwner.pieceY) + 3) + e.blockSize - 1, e.blockSize, 1);
                }
            }
        }
        g.setColor(new Color(64, 64, 64));
        g.fillRect(xSize / 4, 0, xSize / 2, ySize / 4);
        g.setColor(new Color(255, 255, 255));
        g.drawString(winTitle, (xSize * 5) / 16, (ySize * 3) / 16);
        if (winTitle == "CTRL"){
                g.setColor(new Color(255, 255, 255));
                g.drawString("LMB - Left", (xSize * 2) / 16, (ySize * 6) / 16);
                g.drawString("MMB - Rotate", (xSize * 2) / 16, (ySize * 8) / 16);
                g.drawString("RMB - Right", (xSize * 2) / 16, (ySize * 10) / 16);
        }
    }
    public int convertX(int X){
        /*
        Similar to the convertX function of TetrisGrid, this version has an offset to make it properly appear within the TetrisWindow
         */

        if (X < 0){
            return (-X*20-30);
        }
        else
            return (X*20-30);
    }
    public int convertY(int Y){
        /*
        Similar to the convertY function of TetrisGrid, this version has an offset to make it properly appear within the TetrisWindow
         */

        if (Y < 0){
            return (-Y*20-30);
        }
        else
            return (Y*20-30);
    }
}
