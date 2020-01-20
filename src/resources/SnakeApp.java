package resources;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.Direction;
import logic.Food;
import logic.Snake;

/**
 * A public class that contains all the components for a snake game
 *
 * @author sion wilks
 */
public class SnakeApp extends Application {
    private Direction direction = null; // enum that keeps track of the direction the snake is moving
    private int blockDimension = 35;
    private int blockSpacing = blockDimension + 5; // doubles as start coordinates for game area
    private Snake snake = new Snake(120, 120, blockDimension, blockSpacing);
    private double speed;
    private final int PRE_WIDTH = blockSpacing * 45; // width of stage
    private final int PRE_HEIGHT = blockSpacing * 25; // height of stage
    private int gameAreaEndX = PRE_WIDTH - blockSpacing * 2;
    private int gameAreaEndY = PRE_HEIGHT - blockSpacing * 2;
    private Food food = new Food(blockSpacing, blockSpacing, gameAreaEndX, gameAreaEndY, blockDimension, snake);

    private StackPane root = new StackPane();
    private Canvas canvas = new Canvas(PRE_WIDTH, PRE_HEIGHT);
    private VBox postDeathButtons = new VBox();
    MenuButton startBut;
    private Button restartBut = new Button("play Again");
    Button changeDiff = new Button("Change Difficulty"); // allows player to change difficulty after death
    Label sizeLabel;

    Timeline gameLoop;

    @Override
    /**
     * Starts the application
     */
    public void start(Stage primaryStage) {

        // create game background
        root.setPrefSize(PRE_WIDTH, PRE_HEIGHT); // set pre size on stage(window)
        root.setStyle("-fx-background-color: Black"); // set color red
        Rectangle snakeArea = new Rectangle(blockSpacing, blockSpacing, gameAreaEndX, gameAreaEndY);
        snakeArea.setFill(Color.rgb(0, 102, 225)); // set color blue
        canvas.setFocusTraversable(true);

        // create label that tracks snake length
        sizeLabel = new Label("Length" + snake.size());
        sizeLabel.setStyle("-fx-background-color: White");
        sizeLabel.setStyle("-fx-padding: 10px 10px");
        sizeLabel.setStyle("-fx-text-color:White");
        sizeLabel.setTextFill(Color.WHITE);
        sizeLabel.setStyle("-fx-font-size: 24px");

        // buttons for when user dies
        playAgain(restartBut); // create Event Handler
        postDeathButtons.getChildren().addAll(changeDiff, restartBut);
        postDeathButtons.setAlignment(Pos.TOP_CENTER);
        postDeathButtons.setSpacing(200);
        postDeathButtons.setVisible(false);
        restartBut.setPrefSize(100, 50);

        changeDiff.setOnMouseClicked(mouseEvent -> {
            startBut.setVisible(true);
            postDeathButtons.setVisible(false);
            snake = new Snake(120, 120, blockDimension, blockSpacing);
            direction = null;
        });

        // game menu button
        MenuItem easy = new MenuItem("easy");
        MenuItem medium = new MenuItem("medium");
        MenuItem hard = new MenuItem("hard");
        startBut = new MenuButton("Difficulty", null, easy, medium, hard);
        startBut.setStyle("-fx-color:#F0F8FF");

        //Add events for each option
        easy.setOnAction(event -> {
            speed = 0.12;
            gameLoop = loop(); // create game loop
            startBut.setVisible(false);
        });
        medium.setOnAction(event -> {
            speed = 0.09;
            gameLoop = loop(); // create game loop
            startBut.setVisible(false);
        });
        hard.setOnAction(event -> {
            speed = 0.07;
            gameLoop = loop(); // create game loop
            startBut.setVisible(false);
        });


        // set node locations
        StackPane.setAlignment(sizeLabel, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(postDeathButtons, Pos.TOP_CENTER);
        StackPane.setMargin(postDeathButtons, new Insets(100, 0, 0, 0));
        StackPane.setAlignment(startBut, Pos.TOP_CENTER);
        StackPane.setMargin(startBut, new Insets(100, 0, 0, 0));


        root.getChildren().addAll(snakeArea, canvas, postDeathButtons, startBut, sizeLabel);
        Scene snakeScene = new Scene(root);
        primaryStage.setScene(snakeScene);
        primaryStage.show();

        addKeyListener(snakeScene);

    }

    /**
     * sets up event handler for button that asked user to play again
     *
     * @param restartBut button to set handler for
     */
    private void playAgain(Button restartBut) {
        restartBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                restartBut.setPrefSize(100, 50);
                 snake = new Snake(120, 120, blockDimension, blockSpacing);
                postDeathButtons.setVisible(false);
                gameLoop = loop();
                direction = null;
            }
        });
    }

    private void addMenuItems(MenuButton menu) {

    }


    /**
     * creates game loop that draws the snake and food along with moving the snake
     */
    private Timeline loop() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        final long timeStart = System.currentTimeMillis();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(speed),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        gc.clearRect(0, 0, PRE_WIDTH, PRE_HEIGHT); // Clear the canvas
                        food.draw(gc);
                        snake.draw(gc);
                        sizeLabel.setText("Length: " + snake.size());

                        // check if snake hit body or boundary
                        if (!snake.move(direction, blockSpacing, blockSpacing, gameAreaEndX, gameAreaEndY)) {
                            postDeathButtons.setVisible(true);
                            restartBut.setStyle("-fx-background-color: #555555");
                            changeDiff.setStyle("-fx-background-color: #555555");
                            gameLoop.stop();

                        }

                        if (snake.foodEaton(food)) {
                            snake.growByTwo(direction);
                            food.getMoreFood(snake);
                        }
                    }
                });

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
        return gameLoop;
    }

    /**
     * adds a key listener to the provided scene that listens for WASD kays as well as arrow keys
     * checks if the snake is not going in the opposite direction as well as the snakes head current position is not
     * the first block in the snake body(this can happen if keys are pressed too fast)
     *
     * @param scene scene to add listener to
     */
    private void addKeyListener(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if ((keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D)) {
                    if (snake.size() == 1 || (snake.getPointX(0) + blockSpacing != snake.getPointX(1))) {
                        direction = Direction.RIGHT;
                    }

                }

                // change direction to left if left arrow key or A key is pressed. Do not change if curr direction is right
                else if ((keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A)) {
                    if (snake.size() == 1 || (snake.getPointX(0) - blockSpacing != snake.getPointX(1))) {
                        direction = Direction.LEFT;
                    }

                }
                // change direction to up if up arrow key or W key is pressed. Do not change if curr direction is down
                else if ((keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W)) {
                    if (snake.size() == 1 || (snake.getPointY(0) - blockSpacing  != snake.getPointY(1))) {
                        direction = Direction.UP;
                    }


                }
                // change direction to down if left arrow key or s key is pressed. Do not change if curr direction is up
                else if ((keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S)) {
                    if (snake.size() == 1 || (snake.getPointY(0) + blockSpacing  != snake.getPointY(1))) {
                        direction = Direction.DOWN;
                    }


                }
            }
        });
    }

    public static void main(String[] args){
        Application.launch(SnakeApp.class);
    }
}
