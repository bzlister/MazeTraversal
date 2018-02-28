//Ben Lister
import java.lang.StringBuilder;
import java.util.*;
import java.awt.Point;
/*
 * Class for determining if a maze is navigable for some path from (0, 0) to (width, height). Specified circular areas are
 * blocked off.
 */
public class Maze {
  
  /*
   * Data structure used to represent the input state internally. Allows for constant-time checking of whether a
   * a given point is passable
   */   
  private int[][] table;
  private int width;
  private int height;
  private int smallestX;
  private int smallestY;
  private static Point goal;//used for comparator

  
  /*
   * Constructor. xCoor and yCoor are equal-length arrays of circle center coordinates
   */
  public Maze(int w, int h, int radius, int[] xCoor, int[] yCoor) {
    if ((w <= 0) || (h <= 0) || (radius < 0) || (xCoor.length != yCoor.length))
      throw new IllegalArgumentException("Error. Illegal input.");
    width = w;
    height = h;
    Maze.goal = new Point(w, h);
    table = new int[w+1][h+1];
    /*
     * Valid path determination begins at the first occurance of an impass, rather than (0, 0), to speed up
     * performance for large, mostly empty mazes
     */
    smallestX = w;
    smallestY = h;
    for (int q = 0; q < xCoor.length; q++){
      for (int x = xCoor[q]-radius; x <= xCoor[q]+radius; x++){
        for (int y = yCoor[q]-radius; y <= yCoor[q]+radius; y++){
          if ((x >= 0) && (y >= 0) && (x <= w) && (y <= h) && (Math.sqrt(Math.pow(x-xCoor[q], 2)+Math.pow(y-yCoor[q], 2)) <= radius)){
            table[x][y] = -1;//impassable
            if (x < smallestX){
              smallestX = x;
            }
            if (y < smallestY){
              smallestY = y;
            }
          }
        }
      }
    }
    if (smallestX > 0){
      smallestX--;
    }
    if (smallestY > 0){
      smallestY--;
    }
  }
  
  /*
   * Traverses maze and returns 'true' if there is a path, and false if otherwise
   */
  public boolean findPath(){        
    
    boolean exit = false;
    //Stack for performing DFS
    ArrayList<Point> myStack = new ArrayList<Point>();
    myStack.add(new Point(smallestX, smallestY));
    
    //DFS
    while ((!exit) && (!myStack.isEmpty())){
      Point pos = myStack.remove(myStack.size()-1);
      
      //Base case; have successfully navigated maze
      if (((int)pos.getX() == width) && ((int)pos.getY() == height)){
        exit = true;
      }
      //Not there yet; calculate next moves and add to stack
      else
        myStack.addAll(nextMoves((int)pos.getX(), (int)pos.getY()));
    }
    
    if (exit){
      return true;
    }
    else{
      return false;
    }
  }
  
  /*
   * Calculates the next coordinates to go to and returns them as an ArrayList in distance order
   */
  public ArrayList<Point> nextMoves(int x, int y){
    ArrayList<Point> moves = new ArrayList<Point>();      
    //Need to backtrack if path ahead is blocked and haven't already visited position before
    if (table[x][y] == 0){
      if ((x > 0) && (x < width) && (table[x+1][y] == -1)){
        moves.add(new Point(x-1, y));
        if (y > 0)
          moves.add(new Point(x-1, y-1));
      }
      if ((y > 0) && (y < height) && (table[x][y+1] == -1)){
        moves.add(new Point(x, y-1));
        if (x < width)
          moves.add(new Point(x+1, y-1));
      }
            
    }
    
    //mark as visited
    table[x][y] = 1;
    
    //traverse forward
    if (x < width){
      if (table[x+1][y] == 0)
        moves.add(new Point(x+1, y));
    }
    if (y < height){
      if (table[x][y+1] == 0)
        moves.add(new Point(x, y+1));
    }
    if ((x < width) && (y < height)){
      if (table[x+1][y+1] == 0)
        moves.add(new Point(x+1, y+1));
    }
    if ((x > 0) && (y < height)){
      if (table[x-1][y+1] == 0)
        moves.add(new Point(x-1, y+1));
    }
    //arrange next move by proximity to exit
    moves.sort(Maze.PointCompare);
    return moves;
  }
  
  public static Comparator<Point> PointCompare = new Comparator<Point>() {
    public int compare(Point p1, Point p2){
    return new Double(p2.distance(Maze.goal.getX(), Maze.goal.getY())).compareTo(p1.distance(Maze.goal.getX(), Maze.goal.getY()));
  }};
  
  /*
   * Returns a String visualization of the maze
   */
  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder("\n");
    for (int i = 0; i <= width; i++){
      for (int j = 0; j <= height; j++){
        if ((i == smallestX) && (j == smallestY)){
          sb.append("*");//starting position
          sb.append(" ");
        }            
        else if ((i == width) && (j == height)){
          sb.append("O");//exit of maze
        }
        else{
          if (table[i][j] == -1){
            sb.append("x");//occupied by circle
          }
          if (table[i][j] == 0){
            sb.append(".");//passable, untraversed coordinate
          }
          if (table[i][j] == 1){
            sb.append("~");//passable, traversed coordinate
          }
          sb.append(" ");
        }
      }
      sb.append("\n");          
    }
    return sb.toString();
  }
  
  public int[][] getTable(){
    return table;
  }
}
