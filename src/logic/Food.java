package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class Food {
    private Point foodLoc; // location of food
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int blockDimension;


    public Food(int startX, int startY, int endX, int endY, int size, Snake snake) {
        // creates new food within provided bounds
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        blockDimension = size;
        foodLoc = new Point(getNewXPt(startX, endX), getNewYPt(startY, endY));
        getMoreFood(snake);
    }

    public void getMoreFood(Snake snake){
        boolean foodOnSnake;
        do {
            foodLoc.setX(getNewXPt(startX, endX));
            foodLoc.setY(getNewYPt(startY, endY));
            foodOnSnake = false;
            for(Point point : snake.getBody()){
                if (foodLoc.getX() == point.getX() && foodLoc.getY() == point.getY()) {
                    foodOnSnake = true;
                    break;
                }
            }
        } while(foodOnSnake);
    }

    public void draw(GraphicsContext gc){
        gc.setFill(Color.RED);
        gc.fillRect(foodLoc.getX(),foodLoc.getY(),blockDimension, blockDimension);
    }

    /**
     * generates new random x-coordinate with given area
     * @param startX start of x plane
     * @param endX end of x plane
     * @return new random x position
     */
    private int getNewXPt(int startX, int endX){
        int x = ThreadLocalRandom.current().nextInt(startX, endX);
        while (x % 40 != 0) {
            x = ThreadLocalRandom.current().nextInt(startX, endX);
        }

        return x;
    }

    /**
     * generates new random x-coordinate with given area
     * @param startY start of y plane
     * @param endY end of x plane
     * @return new random y position
     */
    private int getNewYPt(int startY, int endY){
        int y = ThreadLocalRandom.current().nextInt(startY, endY);
        while (y % 40 != 0) {
            y = ThreadLocalRandom.current().nextInt(startY, endY);
        }

        return y;
    }

    public int getX(){
        return foodLoc.getX();
    }

    public int getY(){
        return foodLoc.getY();
    }



}
