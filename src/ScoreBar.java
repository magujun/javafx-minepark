import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ScoreBar extends ToolBar {
	private static int timer;
	private static int mines;

	public static ToolBar scoreBar() {
		timer = Play.getTimer();
		Play.setTimer(timer + 1);
		mines = Play.mines;

		ToolBar scoreBar = new ToolBar();
//		scoreBar.setStyle("-fx-background-color: hsb(0,0%,85%);");
		scoreBar.setStyle("-fx-background-color: linear-gradient(to bottom, #ff7f50, #6a5acd);");
		scoreBar.setStyle("-fx-background-radius: 10 10 0 0;");
		DropShadow ds = new DropShadow();
		ds.setOffsetY(5.0f);
		ds.setOffsetX(5.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
		ds.setRadius(5.0f);
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
			int minesDigit = (int)(i == 1 ? Math.floor(mines/100): i == 2 ? Math.floor(mines/10) : mines%10);
			Text counter = new Text(Integer.toString(minesDigit));
			counter.setStyle("@font-face: {-font-family: 'Digital-7 Mono'; src(url(“res/fonts/digital-7.ttf”))};");
			counter.setStyle("-fx-font-family: 'Digital-7 Mono'; -fx-font-size: 60px;");
			counter.setEffect(ds);
			counter.setCache(true);
			scoreBar.getItems().add(counter);
		}

		String playerImg = (Play.isSafe() ? "file:res/win" + Play.difficulty + ".png" : Play.isDead() ? "file:res/gameover" + Play.difficulty + ".png" : Play.getPlayerImg());

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
			new Play(Play.difficulty);
		});

		for (int i = 1; i < 4; i++) {
			int timeDigit = (int)(i == 1 ? Math.floor(timer/100%100) : i == 2 ? Math.floor(timer/10%10) : timer%10);
			Text timer = new Text(Integer.toString(timeDigit));
			timer.setStyle("@font-face: {-font-family: 'Digital-7 Mono'; src(url(“res/fonts/digital-7.ttf”))};");
			timer.setStyle("-fx-font-family: 'Digital-7 Mono'; -fx-font-color: 'RED'; -fx-font-size: 60px;");
			timer.setEffect(ds);
			timer.setCache(true);
			scoreBar.getItems().add(timer);
		}
		scoreBar.getItems().add(rightSpacer);

		return scoreBar;

	}

}
