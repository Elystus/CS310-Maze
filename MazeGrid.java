/*	MazeGrid.java
    The GUI for the MazeSolver class
	Alan Riggins
	CS310 Fall 2015
*/    

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import data_structures.*;

public class MazeGrid extends JFrame {
    private final int DIMENSION;    
    private JPanel gridPanel, buttonPanel;
    private JButton resetButton, slowButton, markButton, pathButton, stopButton;
    private JLabel [][] gridLabels;
    private GridCell [][] cells;
    private MazeSolver solver;
    private String action;
    private Color pathColor;
    private volatile boolean slow;
    private volatile boolean stopped;
    
    public MazeGrid(MazeSolver solver, int dimension) {
        pathColor = new Color(200,225,250);
        DIMENSION = dimension;
        this.solver = solver;
        setWindowAttributes();
        setLookAndFeel();
        JPanel contentPanel = getContentPanel();
        addComponents(contentPanel);                
        setVisible(true);          
        initPuzzle();
       }
        
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
            "javax.swing.plaf.metal.MetalLookAndFeel");
            }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, 
            "Required Metal Look and Feel resources unavailable.  " +
            "The program will run, but may not look right");                    
            }
        }
                
        
    private void setWindowAttributes() {
        setTitle("Maze Puzzle");
        setSize(800,850);
        setLocation(0,0);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
                
    private JPanel getContentPanel() {
        JPanel panel = (JPanel) getContentPane();
        panel.setBackground(Color.white);                  
        return panel;
        }
        
    private void addComponents(JPanel panel) {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(DIMENSION,DIMENSION)); 
        gridPanel.setBorder(new EmptyBorder(0,10,0,10)); 
        gridLabels = new JLabel[DIMENSION][DIMENSION];
        cells = new GridCell[DIMENSION][DIMENSION];
        for(int i=0; i < DIMENSION; i++)
            for(int j=0; j < DIMENSION; j++) {
                gridLabels[i][j] = new JLabel();
                gridLabels[i][j].setOpaque(true);
                gridLabels[i][j].setBorder(LineBorder.createBlackLineBorder());
                gridLabels[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 8));
                gridLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                gridPanel.add(gridLabels[i][j]);
                cells[i][j] = new GridCell(i,j);
            }           
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ButtonHandler());
        slowButton = new JButton("Mark Slowly");
        slowButton.addActionListener(new ButtonHandler());        
        markButton = new JButton("Mark");
        markButton.addActionListener(new ButtonHandler());
        pathButton = new JButton("Show Shortest Path");
        pathButton.addActionListener(new ButtonHandler());   
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ButtonHandler());             
        buttonPanel = new JPanel();
        buttonPanel.add(resetButton);
        buttonPanel.add(slowButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(markButton);
        buttonPanel.add(pathButton);
        buttonPanel.setBorder(new EmptyBorder(0,0,15,0));        
        panel.add(gridPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);        
        }
           
    private class ButtonHandler implements ActionListener {        
        public void actionPerformed(ActionEvent e) {                                 
            String doWhat = e.getActionCommand();
            action = "nothing";
            if(doWhat.equals("Reset")) {
                initPuzzle();
                solver.reset();
                stopped = false;
                }
            else if(doWhat.equals("Mark")) {
                slow = false;
                new Thread(new Runnable() {
                    public void run() {
                        solver.mark(); 
                        }
                    }).start(); 
                } 
            else if(doWhat.equals("Mark Slowly")) {
                slow = true;
                new Thread(new Runnable() {
                    public void run() {
                        solver.mark(); 
                        }
                    }).start(); 
                }
            else if(doWhat.equals("Stop")) {
                stopped = true;
                //initPuzzle();
                solver.reset();
                }                                                     
            else {
                if(!stopped) {
                    if(!solver.move())                                                
                        printFailureMessage();                    
                    else 
                	;    
                }                         
                }            
            } // end actionPerformed
        } // end ButtonHandler
                   
////////////////////////////////////////////////////////////////////////
//          Maze Manipulation Methods
////////////////////////////////////////////////////////////////////////

    private void initPuzzle() {
        for(int i=0; i < DIMENSION; i++)
            for(int j=0; j < DIMENSION; j++) {
                gridLabels[i][j].setBackground(Color.white);
                gridLabels[i][j].setText("");
                cells[i][j].setDistance(-1);
                }
            
        for(int i = 1; i < DIMENSION; i+=2 )
            for(int j=0; j < DIMENSION; j++)  {
                int x = (int) (DIMENSION/4 * Math.random());
                if(x > 1)
                    gridLabels[j][i].setBackground(Color.black);
            }   
        
        for(int i = 0; i < DIMENSION; i+=2 )
            for(int j=0; j < DIMENSION; j++)  {
                int x = (int) (DIMENSION/2 * Math.random());
                if(x == 1)
                    gridLabels[j][i].setBackground(Color.black);
            }                    
                
        gridLabels[0][0].setBackground(Color.yellow);    
        gridLabels[0][1].setBackground(Color.white);
        gridLabels[1][1].setBackground(Color.white);
        gridLabels[1][0].setBackground(Color.white);            
        gridLabels[DIMENSION-1][DIMENSION-1].setBackground(Color.green);    
        gridLabels[DIMENSION-1][DIMENSION-2].setBackground(Color.white);
        gridLabels[DIMENSION-2][DIMENSION-2].setBackground(Color.white);
        gridLabels[DIMENSION-2][DIMENSION-1].setBackground(Color.white);            
        repaint();
        }
       
    // Returns the GridCell object at the XY location specified, or null if the
    // ccordinates are invalid.
    public GridCell getCell(int x, int y) {
        try {
            GridCell c = cells[x][y];
            }
        catch(IndexOutOfBoundsException e) { 
            return null;
            }
        return cells[x][y];
        }
    
    // Returns true if the move is valid.  Valid means the XY coordinate are 
    // within the grid, and the background is not black.
    // Does NOT detect if the move is legal since it does not maintain history.
    // A move such as to a diagonal cell, or a move greater than 1 cell away
    // may be valid if the cell is within the grid, and does not have a black background.
    // However, such moves are illegal.  This method does **NOT** detect illegal moves,
    // only invalid ones.
    public boolean isValidMove(GridCell cell) {
        if (cell == null) return false;
        int x = cell.getX();
        int y = cell.getY();
	    if(x < 0 || x >= DIMENSION) return false;
    	if(y < 0 || y >= DIMENSION) return false;        
        if(gridLabels[x][y].getBackground().equals(Color.black))                       
            return false;            
        return true;
        }
    
    // Sets the background color of the cell to indicate that it is in the shortest path.
    public void markMove(GridCell cell) {
        int a = cell.getX();
        int b = cell.getY();
        int d = cell.getDistance();
        gridLabels[a][b].setBackground(pathColor);
        }    
    
    // Sets the distance in the given GridCell instance, and displays that distance on the grid.
    public void markDistance(GridCell cell) { 
        if(stopped) return;       
        int a = cell.getX();
        int b = cell.getY();
        int d = cell.getDistance();
        gridLabels[a][b].setText(""+d);
        if(slow) {
            gridLabels[a][b].setBackground(Color.red);
            try {
                Thread.sleep(50);
                }
            catch(Exception e) {}
            gridLabels[a][b].setBackground(Color.white);        
            repaint();        
            } // end if slow
        }        
     
    
    // Pops up a dialog box to indicate that this maze has no solution.
    private void printFailureMessage() {
        JOptionPane.showMessageDialog(this, "Sorry, this maze has no solution!!");
    }
            
}
