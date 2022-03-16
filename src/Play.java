import java.io.File;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Play extends BorderPane {
	private static String playerImg;
	private static String difficulty;
	private static boolean isSafe;
	private static boolean isDead;
	private static int mines;
	private static int numCols;
	private static int numRows;
	private static int cleared;
	private static int timer;
	private static Timeline timeline;
	private static BorderPane base;
	private static Stage stage;
	private static Music music;

	// First run constructor
	public Play(Stage stage) {
		// Start game with default difficulty
		Play.stage = stage;
		difficulty = "Beginner";
		newGame(difficulty);
	}

	// New game constructor
	public Play(String difficulty) {
		// Start nem game with current or selected difficulty
		Play.difficulty = difficulty;
		music.stop();
		newGame(difficulty);
	}

	public static void newGame(String difficulty) {

		music = new Music("newGame", difficulty);
		music.start();
		setTimer(0);
		playerImg = "file:res/";
		isSafe = false;
		isDead = false;

		switch(difficulty) {
		case "Intermediate":
			numCols = 16;
			numRows = 16;
			mines = 40;
			playerImg += "kenny.png";
			break;
		case "Expert":
			numCols = 32;
			numRows = 16;
			mines = 99;
			playerImg += "mysterion.png";
			break;
		default:
			numCols = 8;
			numRows = 8;
			mines = 10;
			playerImg += "butters.png";
			break;
		}

		// Define number of tiles that must be cleared to win the game
		setCleared((numCols * numRows) - mines);

		Canvas canvas = new Canvas(0,0);
		canvas.autosize();

		// Set BorderPane layout:
		// Top pane scorebar, updated every second
		// Center pane grid, contains the tiles
		// Bottom pane menu, contains game option
		base = new BorderPane(canvas);
		getBase().setStyle("-fx-border-width: 10px; -fx-border-color: #444");
		getBase().setTop(ScoreBar.scoreBar());

		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			getBase().setTop(ScoreBar.scoreBar());
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		getBase().setCenter(Grid.gridPane(numRows, numCols, mines));
		getBase().setBottom(MainMenu.menu());
		timeline.play();

		// set scene and options
		Scene scene = new Scene(getBase());
		stage.getIcons().add(new Image("file:res/minepark.png"));
		stage.setScene(scene);
		stage.setTitle("Minepark");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.show();
	}

	// Game ends by selecting a mine tile
	public static void gameOver(int[][] tiles, Tile t, GridPane grid) {
		setDead(true);
		getBase().setTop(ScoreBar.scoreBar());
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				int type = tiles[row][col];
				if (row == t.row && col == t.col) continue;
				if (type == 9) {
					Tile tile = new Tile();
					tile.setGraphic(tile.imageMine);
					grid.add(tile,col,row);	
				}
			}
		}
		for (Node tile: grid.getChildren()) { 
			tile.setDisable(true);
			tile.opacityProperty().set(90.0);
		}
		timeline.stop();
		music.stop();
		music = new Music("gameover", difficulty);
		music.start();
	}

	// Game ends by clearing all safe tiles
	public static void gameWin(GridPane grid) {
		setSafe(true);
		getBase().setTop(ScoreBar.scoreBar());
		for (Node tile: grid.getChildren()) {
			tile.setDisable(true);
			tile.opacityProperty().set(90.0);;
		}
		timeline.stop();
		music.stop();
		music = new Music("win", difficulty);
		music.start();
	}

	public static void loadSplashScreen() {
		//Load splash screen
		String splashImg = ("file:res/splash.png");

		ImageView splash = new ImageView(new Image(splashImg));
		StackPane pane = new StackPane(splash);
		base.getChildren().setAll(pane);

		//Load splash screen with fade in effect
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), pane);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setCycleCount(1);

		//Finish splash with fade out effect
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), pane);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setCycleCount(1);

		fadeIn.play();

		//After fade in, start fade out
		fadeIn.setOnFinished((e) -> {
			fadeOut.play();
		});
	}

	// Getters and setters
	public static String getPlayerImg() {
		return playerImg;
	}

	public static String getDifficulty() {
		return difficulty;
	}

	public static int getMines() {
		return mines;
	}

	public static int getNumCols() {
		return numCols;
	}

	public static int getNumRows() {
		return numRows;
	}

	public static boolean isSafe() {
		return isSafe;
	}

	public static boolean isDead() {
		return isDead;
	}

	public static int getCleared() {
		return cleared;
	}

	public static int getTimer() {
		return timer;
	}

	public static void setTimer(int timer) {
		Play.timer = timer;
	}

	public static void setPlayerImg(String playerImg) {
		Play.playerImg = playerImg;
	}

	public static void setDifficulty(String difficulty) {
		Play.difficulty = difficulty;
	}

	public static void setMines(int mines) {
		Play.mines = mines;
	}

	public static void setSafe(boolean isSafe) {
		Play.isSafe = isSafe;
	}

	public static void setDead(boolean isDead) {
		Play.isDead = isDead;
	}

	public static void setCleared(int cleared) {
		Play.cleared = cleared;
	}

	public static Timeline getTimeline() {
		return timeline;
	}

	public static BorderPane getBase() {
		return base;
	}
}

class Music {
	private MediaPlayer mediaPlayer;
	private String theme, level;

	public Music (String theme, String level) {
		this.theme = theme;
		this.level = level;
	}

	public void start() {
		try {
			String file = "res/" + theme + level + ".mp3";
			Media music = new Media(new File(file).toURI().toString());
			mediaPlayer = new MediaPlayer(music);
		} catch (MediaException e) {
			System.out.println("Missing media files!");
		}
		mediaPlayer.setAutoPlay(true);
	}

	void stop () {
		mediaPlayer.stop();
		mediaPlayer.dispose();
	}
}
