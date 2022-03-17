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
		mines = Play.mines;
		Play.setTimer(timer + 1);

		ToolBar scoreBar = new ToolBar();
		scoreBar.setStyle("-fx-background-color: hsb(0,0%,85%);");
		scoreBar.setStyle("-fx-background-color: (to bottom, hsb(0, 0%, 100%) 0%, hsb(0, 0%, 80%) 50%, hsb(0, 0%, 60%) 50%, hsb(0, 0%, 40%) 100%);");
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
			Text text = new Text(Integer.toString(minesDigit));
			text.setStyle("@font-face: {-font-family: 'Digital-7 Mono'; src(url(“res/fonts/digital-7.ttf”))};");
			text.setStyle("-fx-font-family: 'Digital-7 Mono'; -fx-font-size: 60px;");
			text.setEffect(ds);
			text.setCache(true);
			ImageView counter = new ImageView(new Image("file:res/digits/" + text + ".png"));
			counter.setFitHeight(40);
			counter.setFitWidth(20);
			scoreBar.getItems().add(text);
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
			ImageView counter = new ImageView(new Image("file:res/digits/" + timeDigit + ".png"));
			counter.setFitHeight(40);
			counter.setFitWidth(20);
			Text text = new Text(Integer.toString(timeDigit));
			text.setStyle("@font-face: {-font-family: 'Digital-7 Mono'; src(url(“res/fonts/digital-7.ttf”))};");
			text.setStyle("-fx-font-family: 'Digital-7 Mono'; -fx-font-size: 60px;");
			text.setEffect(ds);
			text.setCache(true);
			scoreBar.getItems().add(text);
		}
		scoreBar.getItems().add(rightSpacer);

		return scoreBar;

	}

}
