
/*************************************************
 * Marcelo Guimaraes Junior
 * Minepark (part two)
 * 
 * https://linkedin.com/in/marcelo-guimaraes-junior
 * https://github.com/magujun
 * Please don't copy my code =)
 *************************************************/

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.media.MediaException;
import javafx.stage.Stage;

public class Minepark extends Application {


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		try {
			primaryStage.getIcons().add(new Image("file:res/minepark.png"));
		} catch (MediaException e) {
			System.out.println("Missing media files!");
		}

		// Start game
		new Play(primaryStage);
	}
}