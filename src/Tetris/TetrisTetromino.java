package Tetris;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jpbere on 19/11/2015.
 */
public class TetrisTetromino extends JComponent {
    /*
    The TetrisTetromino class stores primarily it's co-ordinates, and a list of block that it has generated and their shape.
     */
    ArrayList<TetrisBlock> blockList = new ArrayList<TetrisBlock>();
    Color tetrominoColour;
    TetrisGrid tGrid;
    int pieceX;
    int pieceY;
    int pieceType;
    int pieceRotation = 0;

    TetrisTetromino(int x, int y, int type, TetrisGrid Grid){
        /*
        The constructor initialises the variables and creates a set of TetrisBlocks depending on the TetrisTetromino shape
        It then prints the shape to the console for debugging purposes
         */
        tGrid = Grid;
        pieceX = x;
        pieceY = y;
        pieceType = type;
        switch (type) {
            case 1: //Generate a Box Piece
                tetrominoColour = scoreColor(new Color(192,0,0));
                blockList.add(new TetrisBlock(x,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y+1, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y+1, tetrominoColour,this));
                System.out.println("[][]");
                System.out.println("[][]");
                break;
            case 2: //Straight Piece
                tetrominoColour = scoreColor(new Color(192,192,0));
                blockList.add(new TetrisBlock(x,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+2,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x-1,y, tetrominoColour,this));
                System.out.println("[][][][]");
                break;
            case 3: //S shaped Curve
                tetrominoColour = scoreColor(new Color(192,0,192));
                blockList.add(new TetrisBlock(x-1,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y+1, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y+1, tetrominoColour,this));
                System.out.println("[][]");
                System.out.println("  [][]");
                break;
            case 4: //Z shaped Curve
                tetrominoColour = scoreColor(new Color(0,192,0));
                blockList.add(new TetrisBlock(x-1,y+1, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y+1, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y, tetrominoColour,this));
                System.out.println("  [][]");
                System.out.println("[][]");
                break;
            case 5: //L shaped Curve
                tetrominoColour = scoreColor(new Color(0,192,192));
                blockList.add(new TetrisBlock(x-1,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y+1, tetrominoColour,this));
               System.out.println("[][][]");
                System.out.println("    []");
                break;
            case 6: //J shaped Curve
                tetrominoColour = scoreColor(new Color(0,0,192));
                blockList.add(new TetrisBlock(x-1,y+1, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y+1, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y+1, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y, tetrominoColour,this));
                System.out.println("    []");
                System.out.println("[][][]");
                break;
            case 7: //T shaped Piece
                tetrominoColour = scoreColor(new Color(128,64,0));
                blockList.add(new TetrisBlock(x-1,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x+1,y, tetrominoColour,this));
                blockList.add(new TetrisBlock(x,y+1, tetrominoColour,this));
                System.out.println("[][][]");
                System.out.println("  []");
                break;
        }
    }

    public void movePiece(int x, int y){
        /*
        Function attempts to move the piece by the given co-ordinates first checking to see if the location is clear
         */
        boolean valid = true;
        for (TetrisBlock e: blockList) {
            if (!tGrid.checkClear(e.blockX + x, e.blockY + y)) {
                valid = false;
            }
        }
        if (valid)
        {
            for (TetrisBlock e: blockList) {
            e.blockX += x;
            e.blockY += y;
            }
            pieceX += x;
            pieceY += y;
        }
    }
    public void setPieceLoc(int x, int y){
        /*
        Function changes the piece co-ordinates to the new co-ordinates
         */
        for (TetrisBlock e: blockList){
            e.blockX = (e.blockX - pieceX)+x;
            e.blockY = (e.blockY - pieceY)+y;
        }
        pieceX = x;
        pieceY = y;
    }
    public void blockDelete(int x, int y){
        /*
        For co-ordinates (x,y) check to see if that is one of this Tetrominos blocks
        If it is, change it's owner to null, and set it's co-ordinates to negative
        This removes all pointers to the instance and should allow the garbage collector to delete it
         */
        for (TetrisBlock i : blockList) {
            if (i.blockX == x && i.blockY == y){
                i.blockOwner = null;
                i.blockX = -1;
                i.blockY = -1;
                System.out.println("Block (" + x + "," + y + ") deleted.");
            }
        }
    }
    public Color scoreColor(Color c){
        /*
        This function changes the given colour slightly, depending on the current score. This was done to show the "age" of some blocks on the board better
         */
        int score = tGrid.intScore;
        int scorered = score%60;
        int scoregreen = (score+20)%60;
        int scoreblue = (score+40)%60;
        int red = c.getRed()+scorered;
        int green = c.getGreen()+scoregreen;
        int blue = c.getBlue()+scoreblue;
        Color newColor = new Color(red,green,blue);
        return newColor;
    }
    public void makeGhost(boolean undead){
        /*
        Makes every block a ghost by calling their methods
         */
        for (TetrisBlock i : blockList) {
            i.makeGhost(undead);
        }
    }
}
