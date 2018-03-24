/**
 * Location class used to store the coordinates of the chicken
 * Used to trace the path the chicken took.
 * @author Shubham Aggarwal
 *
 */
class Location {
    private int x; //X Coordinate of the chicken
    private int y; //Y Coordinate of the chicken

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { //Returns the X coordinate of the chicken
        return x;
    }

    public int getY() { //Returns the Y coordinate of the chicken
        return y;
    }

}