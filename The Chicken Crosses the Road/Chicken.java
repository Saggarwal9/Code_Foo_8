import java.util.ArrayList;

/**
 * Imitates the chicken agent crossing the road.
 * Randomly instantiates a starting position for chicken
 * Finds all possible path to cross the road while avoiding pot-holes.
 * If the chicken is lucky enough to not get completely blocked by pot-holes :(.
 * @author Shubham Aggarwal
 *
 */
public class Chicken {
    static char[][] potholes;
    static int count=0;


    /**
     * Did chicken cross the road?
     * @param y Y coordinate
     * @param x X Coordinate
     * @return true if he indeed crossed the road.
     */
    public static boolean isGoal(int y,int x) {
        if(x==Road.MAX_X_COORDINATE-1 && potholes[y][x]=='O')
            return true;
        else
            return false;
    }

    /**
     * Find all the paths the chicken can use to cross the path
     * @param y Y coordinate
     * @param x X coordinate
     * @param list stores all the iteration of coordinates accessed
     * @param visited stores whether a node has been visited.
     */
    public static void pathFind(int y,int x,ArrayList<Location> list,boolean[][] visited) {
        visited[y][x]=true;
        Location location=new Location(x,y);
        list.add(location);
        if(isGoal(y,x)) {
            count++;
            System.out.print(count + ". ");
            for(int i=0;i<list.size();i++) {
                if(i<list.size()-1)
                    System.out.format("(%d, %d)->", list.get(i).getX(),list.get(i).getY());
                else //Takes care of the last '->' and newline.
                    System.out.format("(%d, %d).\n", list.get(i).getX(),list.get(i).getY()); 
            }
            visited[y][x]=false;
            list.remove(list.size()-1);
            return;
        }
        if(Road.checkBounds(x+1, y) && !visited[y][x+1] && potholes[y][x+1]!='X'){ //Go Right
            pathFind(y,x+1,list,visited);
        }
        if(Road.checkBounds(x, y-1) && !visited[y-1][x] && potholes[y-1][x]!='X' ){ //Go Up
            pathFind(y-1,x,list,visited);
        }
        if(Road.checkBounds(x, y+1) && !visited[y+1][x] && potholes[y+1][x]!='X'){ //Go Down
            pathFind(y+1,x,list,visited);
        }

        list.remove(list.size()-1); //Removes the current location node from linked-list for backtracking.
        visited[y][x]=false; //Makes current location false, so chicken can access this path  
                            //from a different coordinate
        return;   
    }

    public static void main(String[] args) {
        Road road=new Road(); //Instantiate the road.
        potholes=road.getPotholes(); //Get the pot-holes location
        ArrayList<Location> list=new ArrayList<Location>();
        boolean[][] visited=new boolean[Road.MAX_Y_COORDINATE][Road.MAX_X_COORDINATE];
        int start= road.getStartCoordinate(); // Gets the starting position for the chicken.
        //TODO: EDGE CASE - Chicken might get instantiated at a coordinate from which 
        //goal is unreachable, however, there might be other starting positions that
        //chicken can take to reach the goal.
        road.printRoad();
        pathFind(start,0,list,visited);
        System.out.format("Answer: Total valid paths from starting point (%d,%d) is %d.",0,start,count);
    }

}
