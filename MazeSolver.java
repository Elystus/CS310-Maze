/*
 * Author: Andrew Black
 * Course: CS310 Data Structures
 */
import data_structures.*;

public class MazeSolver {
    private int mazeDimension;
    private MazeGrid mazeGUI;
    private Queue<GridCell> mazeQueue;
    private Stack<GridCell> pathStack;

    public MazeSolver(int dimension) {
        mazeDimension = dimension;
        pathStack = new Stack<GridCell>();
        mazeQueue = new Queue<GridCell>();

        mazeGUI = new MazeGrid(this, dimension);
    }

    public void mark()  {
    	GridCell c = mazeGUI.getCell(new Integer(0), new Integer(0));
    	c.setDistance(0);
    	mazeGUI.markDistance(c);
        mazeQueue.enqueue(c);
        
        int adjMoves[][] = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        while(!mazeQueue.isEmpty()) {
            GridCell currCell = mazeQueue.dequeue();
            GridCell tmpCell;

            for(int i[] : adjMoves) {
                tmpCell = mazeGUI.getCell(currCell.getX() + i[0], currCell.getY() + i[1]);

                if(mazeGUI.isValidMove(tmpCell) && !tmpCell.wasVisited()) {
                        tmpCell.setDistance(currCell.getDistance() + 1);
                        mazeGUI.markDistance(tmpCell);
                        mazeQueue.enqueue(tmpCell);
                }
            }
        }
    }


    public boolean move() {
        GridCell endCell = mazeGUI.getCell(mazeDimension - 1, mazeDimension - 1);
        if(endCell.getDistance() == -1)
            return false;

        pathStack.push(endCell);
        int adjMoves[][] = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        while(pathStack.peek().getDistance() != 0) {
            GridCell tmpCell = pathStack.peek();
            for(int i[] : adjMoves) {
                GridCell c = mazeGUI.getCell(pathStack.peek().getX() + i[0], pathStack.peek().getY() + i[1]);
                
                if(mazeGUI.isValidMove(c) && c.getDistance() < tmpCell.getDistance()) 
                    tmpCell = mazeGUI.getCell(pathStack.peek().getX() + i[0], pathStack.peek().getY() + i[1]);
            }
            
            pathStack.push(tmpCell);
        }

        while(!pathStack.isEmpty()) 
            mazeGUI.markMove(pathStack.pop());

        return true;
    }

    public void reset() {
        mazeQueue.makeEmpty();
        pathStack.makeEmpty();
    }

    public static void main(String [] args) {
        new MazeSolver(new Integer(30));
    }
}