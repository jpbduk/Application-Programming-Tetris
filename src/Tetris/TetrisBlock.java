package Tetris;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jpbere on 19/11/2015.
 */
public class TetrisBlock extends JComponent{
    /*
    TetrisBlock is an object that exists just to hold several different types of data;
    blockX the blocks x co-ordinate on the grid
    blockY the blocks y co-ordinate on the grid
    blockColour the blocks colour (Defaults to Red, incase I ever needed it)
    blockOwner is which Tetromino contains the Block
    blockSize is the size of the block in pixels (In hindsight this could be removed)
     */
    int blockX = 1;
    int blockY = 1;
    Color blockColour = new Color(255,0,0);
    TetrisTetromino blockOwner;
    boolean ghostState;
    int blockSize;
    boolean blockFalling;

    TetrisBlock(int x, int y, Color Colour, TetrisTetromino Owner)
            /*
            This constructor just sets the values for the Blocks Variables
             */
    {
        blockX = x;
        blockY = y;
        blockColour = Colour;
        blockOwner = Owner;
        blockSize = blockOwner.tGrid.gridSize;
        blockFalling = true;
        blockOwner.tGrid.theApplet.add(this);
    }
    public void makeGhost(boolean undead)
            /*
            Makes the Block a "Ghost" which means it won't be removed with line deletions
             */
    {
        ghostState = undead;
    }

}
