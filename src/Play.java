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
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Play extends BorderPane {
	private static int numCols, numRows, cleared, timer;
	private static boolean isSafe, isDead;
	private static String playerImg;
	private static Timeline timeline;
	private static BorderPane base;
	private static Music music;
	private static Stage stage;
	static String difficulty;
	static Tile mine;
	static int mines;

	// First run constructor
	public Play(Stage stage) {
		// Start game with default "Beginner" difficulty
		Play.stage = stage;
		Play.difficulty = "Beginner";
		loadSplashScreen();
	}

	// New game constructor
	public Play(String difficulty) {
		// Start nem game with current or newly selected difficulty
		Play.difficulty = difficulty;
		music.stop();
		newGame(difficulty);
	}

	// Start new game
	public void newGame(String difficulty) {

		timer = 0;
		music = new Music("newGame", difficulty);
		music.start();
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
		default: // "Beginner"
			numCols = 8;
			numRows = 8;
			mines = 10;
			playerImg += "butters.png";
			break;
		}

		// Define number of tiles that must be cleared to win the game
		cleared = (numCols * numRows) - mines;

		Canvas canvas = new Canvas(0,0);
		canvas.autosize();

		// SET BORDER PANE LAYOUT
		// Top pane scorebar, contains flagged mines counter, game button and a timer
		// Center pane grid, contains the tiles
		// Bottom pane menu, contains game options menu
		base = new BorderPane(canvas);
		base.setStyle("-fx-border-width: 10px; -fx-border-color: #444; -fx-background-color: #444;");
		base.setTop(ScoreBar.scoreBar());
		base.setCenter(Grid.gridPane(numRows, numCols, mines));
		base.setBottom(MainMenu.menu());

		// Update the BorderPane top region with a new scoreBar, every second
		// Check the number of cleared non mined tiles to process a game win
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			base.setTop(ScoreBar.scoreBar());
			if (Play.getCleared() < 1) {
				Play.gameWin();
			}
			if (isDead == true) {
				Play.gameOver(mine);
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		// SET SCENE AND OPTIONS
		Scene scene = new Scene(base);
		stage.getIcons().add(new Image("file:res/minepark.png"));
		stage.setScene(scene);
		stage.setTitle("Minepark");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.show();
	}

	// End game for clicking on a mine tile
	static void gameOver(Tile mine) {
		isDead = true;
		GridPane grid = Grid.getGrid();
		base.setTop(ScoreBar.scoreBar());
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (row == mine.row && col == mine.col) continue;
				int item = row * numCols + col;
				Tile tile = (Tile) grid.getChildren().get(item);
				if (tile.covered) tile.clear();
				tile.setDisable(true);
				tile.opacityProperty().set(90.0);
			}
		}
		// Stop the timer and load new music
		timeline.stop();
		music.stop();
		music = new Music("gameover", difficulty);
		music.start();
	}

	// Process game win for clearing all safe tiles
	static void gameWin() {
		isSafe = true;
		GridPane grid = Grid.getGrid();
		base.setTop(ScoreBar.scoreBar());
		for (Node tile: grid.getChildren()) {
			tile.setDisable(true);
			tile.opacityProperty().set(90.0);
		}
		HighScores.score();
		timeline.stop();
		music.stop();
		music = new Music("win", difficulty);
		music.start();
	}

	// Getters and setters
	public Stage getStage() {
		return stage;
	}

	public static BorderPane getBase() {
		return base;
	}

	public static Timeline getTimeline() {
		return timeline;
	}

	public static String getPlayerImg() {
		return playerImg;
	}

	public static int getNumCols() {
		return numCols;
	}

	public static int getNumRows() {
		return numRows;
	}

	public static int getCleared() {
		return cleared;
	}

	public static boolean isSafe() {
		return isSafe;
	}

	public static boolean isDead() {
		return isDead;
	}

	public static void setSafe(boolean safe) {
		isSafe = safe;
	}

	public static void setDead(boolean dead) {
		isDead = dead;
	}

	public static void setPlayerImg(String playerImg) {
		Play.playerImg = playerImg;
	}

	public static void setCleared(int cleared) {
		Play.cleared = cleared;
	}

	public static int getTimer() {
		return timer;
	}

	public static void setTimer(int timer) {
		Play.timer = timer;
	}

	// Splash screen =)
	void loadSplashScreen() {

		Stage stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		base = new BorderPane();
		base.setStyle("-fx-background-color: #BBB;");
		base.setMinSize(720,345);
		String splashImg = ("file:res/splash.png");
		ImageView splash = new ImageView(new Image(splashImg));
		splash.minHeight(base.getHeight());
		splash.minWidth(base.getWidth());
		splash.maxHeight(base.getHeight());
		splash.maxWidth(base.getWidth());
		base.getChildren().setAll(splash);

		// SET SCENE AND OPTIONS
		Scene scene = new Scene(base);
		stage.getIcons().add(new Image("file:res/minepark.png"));
		stage.setScene(scene);
		stage.setTitle("Minepark");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.show();

		// Set fade in effect
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), base);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setCycleCount(1);
		fadeIn.play();

		// Set fade out effect
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), base);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setCycleCount(1);
		fadeOut.setDelay(Duration.seconds(1));

		//After fade in, start fade out
		fadeIn.setOnFinished((e) -> {
			fadeOut.play();
		});

		//After fade out, start game
		fadeOut.setOnFinished((e) -> {
			stage.close();
			newGame(difficulty);
		});
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
