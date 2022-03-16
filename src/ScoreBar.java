import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class ScoreBar extends ToolBar {
	private static int timer;
	private static int mines;

	public static ToolBar scoreBar() {
		timer = Play.getTimer();
		mines = Play.getMines();
		Play.setTimer(timer + 1);

		ToolBar scoreBar = new ToolBar();

		Pane leftSpacer = new Pane();
		HBox.setHgrow(
				leftSpacer,
				Priority.SOMETIMES
				);

		Pane rightSpacer = new Pane();
		HBox.setHgrow(
				rightSpacer,
				Priority.SOMETIMES
				);

		Pane spacerLeft = new Pane();
		Pane spacerRight = new Pane();
		HBox.setMargin(spacerLeft, new Insets(0.0,20.0,0.0,20.0));
		HBox.setMargin(spacerRight, new Insets(0.0,20.0,0.0,20.0));

		scoreBar.getItems().add(leftSpacer);

		for (int i = 1; i < 4; i++) {
			int minesDigit = (int)(i == 1 ? Math.floor(mines/100): i == 2 ? Math.floor(mines/10) : mines%10) ;
			ImageView counter = new ImageView(new Image("file:res/digits/" + minesDigit + ".png"));
			counter.setFitHeight(40);
			counter.setFitWidth(20);
			scoreBar.getItems().add(counter);
		}

		String playerImg = (Play.isSafe() ? "file:res/win" + Play.getDifficulty() + ".png" : Play.isDead() ? "file:res/gameover" + Play.getDifficulty() + ".png" : Play.getPlayerImg());

		ImageView playerImage = new ImageView(new Image(playerImg));
		playerImage.setFitWidth(100);
		playerImage.setFitHeight(100);
		Button player = new Button();
		player.setGraphic(playerImage);
		player.autosize();

		scoreBar.getItems().add(spacerLeft);
		scoreBar.getItems().add(player);
		scoreBar.getItems().add(spacerRight);

		player.setOnMouseClicked(e -> {
			Play.getTimeline().stop();
			new Play(Play.getDifficulty());
		});

		for (int i = 1; i < 4; i++) {
			int timeDigit = (int)(i == 1 ? Math.floor(timer/100%100) : i == 2 ? Math.floor(timer/10%10) : timer%10);
			ImageView counter = new ImageView(new Image("file:res/digits/" + timeDigit + ".png"));
			counter.setFitHeight(40);
			counter.setFitWidth(20);
			scoreBar.getItems().add(counter);
		}

		scoreBar.getItems().add(rightSpacer);

		return scoreBar;

	}

}
