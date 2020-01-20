package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class Snake {
    LinkedList<Point> body = new LinkedList<>();
    private int blockSPACING; // space between each piece of snake
   private int blockDemention;


    /**
     * Constructs a snake object with one point
     *
     * @param x x position of head of snake
     * @param y y position of head of snake
     */
    public Snake(int x, int y, int size, int spacing) {
        body.add(new Point(x, y));
        blockDemention = size;
        blockSPACING = spacing;
    }

    public int size() {
        return body.size();
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    /**
     * get the x coordinate of a desired point within the snakes body
     *
     * @param index desired point
     * @return x-coordinate of desired point
     */
    public int getPointX(int index) {
        int x = 0; // x-coordinate of desired point
        try {
            x = body.get(index).getX();
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return x;
    }

    /**
     * get the y coordinate of a desired point within the snakes body
     *
     * @param index desired point
     * @return y-coordinate of desired point
     */
    public int getPointY(int index) {
        int y = 0; // x-coordinate of desired point
        try {
            y = body.get(index).getY();
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return y;
    }

    /**
     * get spacing between snake points (Blocks)
     *
     * @return spacing between snake points
     */
    public int getSpaceBtwBlocks() {
        return blockSPACING;
    }

    /**
     * draws snake on provided Graphics Context
     *
     * @param gc
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        body.forEach(point -> gc.fillRect(point.getX(), point.getY(), blockDemention, blockDemention));
    }

    /**
     * moves the snake in the given direction
     *
     * @param direction
     * @return true if snake was able to move. False if not able
     */
    public boolean move(Direction direction, int gameAreaStartX, int gameAreaStartY, int gameAreaEndX, int gameAreaEndY) {

        if (!hitBoundary(gameAreaStartX, gameAreaStartY, gameAreaEndX, gameAreaEndY) && !hitBody()) {
            if (direction == null) return true;

            int headX = body.getFirst().getX(); // x-coordinate of snake head
            int headY = body.getFirst().getY(); // y-coordinate of snake head

            body.addFirst(new Point(headX + valueIfLtOrRt(direction), headY + valueIfUpOrDn(direction)));
            body.removeLast();


            return true;
        }
        return false;
    }


    public boolean foodEaton(Food food) {
        return body.getFirst().getX() == food.getX() && body.getFirst().getY() == food.getY();
    }


    /**
     * grows snake by two blocks
     */
    public void growByTwo(Direction direction) {
        int addToX = -valueIfLtOrRt(direction);
        int addToY = -valueIfUpOrDn(direction);

        // case where snake hasn't grown yet
        if (body.size() == 1) {
            for (int count = 0; count < 2; count++) {
                body.add(1, new Point(body.get(0).getX() + addToX,
                        body.get(0).getY() + addToY));
            }
        } else {

            int index = (body.size() - 1) / 2; // position in snake to add new block

            // add five blocks to snake of length > 1
            for (int count = 0; count < 2; count++) {

                body.add(index, new Point(body.get(index).getX(),
                        body.get(index).getY()));

                int addX = addToX; // stores amount added for previous block(changes if new direction)
                int addY = addToY; // stores amount added for previous block(changes if new direction)
                //  int spacing = Block_SPACING * 5;

                for (int curr = index + 1; curr < body.size(); curr++) {

                    if (curr == body.size() - 1) { // last block in snake
                        // don't change add values
                    } else {
                        if (body.get(curr).getX() - blockSPACING == body.get(curr + 1).getX()) { // moving right
                            addX = -blockSPACING;
                            addY = 0;
                        } else if (body.get(curr).getX() + blockSPACING == body.get(curr + 1).getX()) { // moving left
                            addX = blockSPACING;
                            addY = 0;
                        } else if (body.get(curr).getY() + blockSPACING == body.get(curr + 1).getY()) { // moving up
                            addX = 0;
                            addY = blockSPACING;
                        } else { // moving down
                            addX = 0;
                            addY = -blockSPACING;
                        }
                    }
                    // move block
                    body.get(curr).addX(addX);
                    body.get(curr).addY(addY);


                }
            }
        }
    }


    /**
     * check if the snake has hit a boundary
     *
     * @return true if snake head is in a boundary
     */
    private boolean hitBoundary(int gameAreaStartX, int gameAreaStartY, int gameAreaEndX, int gameAreaEndY) {
        int x = body.getFirst().getX();
        int y = body.getFirst().getY();
        return y > gameAreaEndY || y < gameAreaStartY || x > gameAreaEndX || x < gameAreaStartX;
    }

    /**
     * checks if the snake ran into itself
     *
     * @return true if snake head is at location of any part of body
     */
    private boolean hitBody() {
        int headX = body.getFirst().getX(); // x coordinate of head block
        int headY = body.getFirst().getY(); // y coordinate of head block
        return IntStream.range(1, body.size()).anyMatch(i -> headX == body.get(i).getX() && headY == body.get(i).getY());
    }

    /**
     * gets value to add to new point if snake is moving left or right
     *
     * @return positive block spacing if right, negative if left and 0 if neither
     */
    private int valueIfLtOrRt(Direction direction) {
        switch (direction) {
            case RIGHT:
                return blockSPACING;
            case LEFT:
                return -blockSPACING;
        }
        return 0;
    }

    /**
     * gets value to add to new point if snake is moving up or down
     *
     * @return positive block spacing if Up, negative if Down and 0 if neither
     */
    private int valueIfUpOrDn(Direction direction) {
        switch (direction) {
            case UP:
                return -blockSPACING;
            case DOWN:
                return blockSPACING;
        }
        return 0;
    }


}


