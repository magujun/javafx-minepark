import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenu extends MenuBar {

	public static MenuBar menu() {

		MenuItem newGameItem, difficultyItem, quitItem;
		Menu menu, difficultyMenu;

		MenuBar menuBar = new MenuBar();
		menu = new Menu("Menu");
		menuBar.setStyle("-fx-background-radius: 0 0 10 10;");
		menuBar.getMenus().add(menu);

		newGameItem = new MenuItem("New Game");
		menu.getItems().add(newGameItem);
		newGameItem.setOnAction( e -> {
			Play.getTimeline().stop();
			new Play(Play.difficulty);
		});

		quitItem = new MenuItem("Quit");
		menu.getItems().add(quitItem);
		quitItem.setOnAction( e -> System.exit(0));

		difficultyMenu = new Menu("Difficulty");
		menuBar.getMenus().add(difficultyMenu);

		difficultyItem = new MenuItem("Beginner");
		difficultyMenu.getItems().add(difficultyItem);
		difficultyItem.setOnAction( e -> {
			Play.getTimeline().stop();
			new Play("Beginner");
		});

		difficultyItem = new MenuItem("Intermediate");
		difficultyMenu.getItems().add(difficultyItem);
		difficultyItem.setOnAction( e -> {
			Play.getTimeline().stop();
			new Play("Intermediate");
		});

		difficultyItem = new MenuItem("Expert");
		difficultyMenu.getItems().add(difficultyItem);
		difficultyItem.setOnAction( e -> {
			Play.getTimeline().stop();
			new Play("Expert");
		});

		return menuBar;
	}
}
