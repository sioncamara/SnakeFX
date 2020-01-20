package controllers;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import logic.Direction;
import logic.Snake;


public class SnakeController {
    public Rectangle r1;
    public Rectangle r2;
    public Group group;
    public Canvas canvas;



    private Direction direction = null;
    private Snake snake = new Snake(100, 100, 35, 40);
    private int speed = 4;

    @FXML
    public void initialize() {
        // gameLoop
//        final long startNanoTime = System.nanoTime();
//        final double startNanoTime2 = 1000;



        new AnimationTimer() { // hidden class
            long prevTime = 0;

            public void handle(long now) {
                GraphicsContext gc = canvas.getGraphicsContext2D();



                if(prevTime == 0 ||(now - prevTime > 1000000000 / speed)) {
                    prevTime = now;
                    gc.clearRect(60,50,480,300);
                    snake.draw(gc);

                }

            }
        }.start();


    }

    public void keyPressed(KeyEvent keyEvent) {


    }

    public void buttonClck(MouseEvent mouseEvent) {
    }
    //    /**
//     * creates a game loop that represents the current points in the snake obj as yellow rectangles
//     */
//    public void loop2(){
//        new AnimationTimer() { // hidden class
//            long prevTime = 0;
//
//            public void handle(long now) {
//                GraphicsContext gc = canvas.getGraphicsContext2D();
//
//
//
//                if(prevTime == 0 || (now - prevTime > 1000000000 / speed)) {
//                    prevTime = now;
//                    //gc.clearRect(0, 0, PRE_WIDTH, PRE_HEIGHT);
//                    gc.setFill(Color.BLUE);
//                    gc.fillRect(SPACE,SPACE,PRE_WIDTH - SPACE * 2,PRE_HEIGHT - SPACE * 2);
//                    food.draw(gc);
//                    snake.draw(gc);
//                    if(!snake.move(direction, SPACE,SPACE,PRE_WIDTH - SPACE * 2,PRE_HEIGHT - SPACE * 2)){
//
//                    }
//                    if(snake.foodEaton(food)){
//                        snake.grow(direction);
//                        food.getMoreFood(snake);
//                    }
//
//
//                }
//
//            }
//        }.start();
//    }
}
