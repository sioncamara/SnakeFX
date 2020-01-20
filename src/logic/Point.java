package logic;

/**
 * Allows for the representation of a single point on a 2D plane
 */
public class Point {
    private int x; // x coordinate of point
    private int y; // y coordinate of point

    /**
     * point constructor
     * @param x x coordinate
     * @param y y coordinate
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int newX){
       this.x = newX;
    }

    public void setY(int newY){
        this.y = newY;
    }

    public void addX(int x){
        this.x += x;
    }

    public void addY(int y){
        this.y += y;
    }
}
