import java.util.Random;

public class Road {
    static final int MAX_X_COORDINATE = 4; //X Limit of the map. Can be changed.
    static final int MAX_Y_COORDINATE = 4; //Y Limit of the map. Can be changed.
    static Random rand; //Random number generator
    int goals_num; //Variable needed if pot-holes covered all the possible goal nodes.
    int start_num; //Variable needed if pot-holes covered all the possible start nodes.
    char potholes[][]; //Location of pot-holes. 1 - Pot-hole, 0-No pot-hole.
    
    //Default constructor used to setup the grid.
    public Road() {
        setupRoad();
    }
   
   /**
    * Helper function used to calculate the number of starting positions/goal in the particular column m
    * @param m indicates which column to check
    */
    private int calculateBoundNodes(char potholes[][],int m) { 
        int count=0;
        for(int i=0;i<MAX_Y_COORDINATE;i++) {
            if(potholes[i][m]=='O')
                count++;
        }
        return count;
    }
    
    public char[][] getPotholes(){
        return potholes;
    }
    
    /**
     * Prints the grid.
     */
    public void printRoad() {
        for(int i=0;i<MAX_Y_COORDINATE;i++) {
            for(int j=0;j<MAX_X_COORDINATE;j++) {
                System.out.print(potholes[i][j] + " ");
            }
            System.out.println();
        }
    }
        
    /**
     * Randomly selects a start coordinate
     * @return the randomly generated coordinate
     */
    public int getStartCoordinate() {
        int start;
        for(;;) { //Runs infinitely until we don't get a valid start.
            start=rand.nextInt(MAX_Y_COORDINATE);
            if(potholes[start][0]=='O')
                break;
        }
        
        return start;
    }
    
    //Checks if coordinates are in bounds.
    public static boolean checkBounds(int x,int y) {
        return (x>=0 && x<MAX_X_COORDINATE && y>=0 && y<MAX_Y_COORDINATE)?true:false;
    }
    
    /**
     * Implements up the grid as per the specifications provided at 
     * http://www.ign.com/code-foo/2018/
     * Any coordinate can contain a pot-hole
     * At least one randomly generated pot-hole
     * At least one randomly generated goal node
     * At least one randomly generated start node
     * X depicts pot-hole, O depicts road
     */
    public void setupRoad() {
        rand= new Random();
        int potholes_num; //How many number of pot-holes (Randomly Generated)
        potholes=new char[MAX_Y_COORDINATE][MAX_X_COORDINATE]; //2D array is mapped in YX Form
        potholes_num= 1 + rand.nextInt(MAX_X_COORDINATE*MAX_Y_COORDINATE); //1 to X*Y possible pot-holes.
        for(int i=0;i<MAX_Y_COORDINATE;i++) {
            for(int j=0;j<MAX_X_COORDINATE;j++) {
                int temp= rand.nextInt(2); //50% probability of this coordinate being a pot-hole
                if(temp==1 &&potholes_num!=0) {
                    potholes[i][j]='X';
                    potholes_num--;
                }
                else
                    potholes[i][j]='O';
            }
        }
        
        goals_num=calculateBoundNodes(potholes,MAX_X_COORDINATE-1);
        start_num=calculateBoundNodes(potholes,0);
        if(goals_num==0) { //Pot-holes covered all the possible goal nodes
            potholes[MAX_X_COORDINATE-1][rand.nextInt(MAX_X_COORDINATE)]='O'; //Randomly re-assign a goal node
            goals_num++;
        }
        
        if(start_num==0) { //Pot-holes covered all the possible start nodes
            potholes[rand.nextInt(MAX_X_COORDINATE)][0]='O'; //Randomly re-assign a goal node
            start_num++;
        }
          
    }
}
