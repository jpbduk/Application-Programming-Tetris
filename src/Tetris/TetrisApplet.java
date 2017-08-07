package Tetris;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jpbere on 19/11/2015.
 */

public class TetrisApplet extends JApplet {
    /*
    This class simply sets up the JApplet, and it's components;
    -theGrid, an object of TetrisGrid which is where the game itself is played.
    -4 Instances of TetrisWindow, a multipurpose piece of the UI designed to show upcoming pieces and the controls.

    The global variables are things predefined here to meet specifications
    -gridX is how many boxes across the Tetris Space is
    -gridY is how many boxes tall the Tetris Space is
    -gridLines defines whether or not to draw lines in the tetris space to show the grid.
    -backgroundColour shows the background colour of the grid
    -blockSize is how big each Tetris block should be in pixels
    -waiting is an ArrayList that holds 3 of the TetrisWindows so they can be called upon later
     */
    int gridX = 10;
    int gridY = 20;
    Boolean gridLines = true;
    Color backgroundColour = new Color(128, 128, 128);
    int blockSize = 20;
    ArrayList<TetrisWindow> waiting = new ArrayList<TetrisWindow>();

    public void init() {
        /*
        On Applet start this runs the basic set up.
         */
        super.setName("Joshua Beresford-Davis 1402731");
        TetrisGrid theGrid = new TetrisGrid(gridX, gridY, backgroundColour, gridLines, this, blockSize);
        GridLayout sidebarLayout = new GridLayout(4,1);
        JPanel sidebar = new JPanel(sidebarLayout);

        //Essential, a weird bug means that when run as a resizeable application the game is unplayable
        this.setSize(400, 400);


        TetrisWindow score = new TetrisWindow(5* blockSize,5 *blockSize, "CTRL", this, theGrid);
        TetrisWindow n1Win = new TetrisWindow(5* blockSize,5* blockSize, "NEXT", this, theGrid);
        TetrisWindow n2Win = new TetrisWindow(5* blockSize,5* blockSize, "LATE", this, theGrid);
        TetrisWindow n3Win = new TetrisWindow(5* blockSize,5* blockSize, "END", this, theGrid);

        waiting.add(n3Win);
        waiting.add(n2Win);
        waiting.add(n1Win);

        sidebar.add(score);
        sidebar.add(n1Win);
        sidebar.add(n2Win);
        sidebar.add(n3Win);

        this.setLayout(new GridLayout(1,2,0,0));
        this.add(sidebar);
        this.add(theGrid);
    }

    public ArrayList<TetrisWindow> getWaiting(){
        //Returns the waiting Arraylist
        return waiting;
    }
}