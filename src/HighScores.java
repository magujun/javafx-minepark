import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class HighScores {
	private static final String HIGHSCORES = "res/highscores_";
	private static int finalscore, highscores, leaderscore;
	private static String difficulty;
	private static boolean isEmpty;
	private static String name;
	private static ArrayList<String> leaders = new ArrayList<>();
	private static Stage stage = new Stage();
	private static VBox messageBox = new VBox();
	private static Scanner input;
	private static FadeTransition fadeIn, fadeOut;
	private static File file;

	public static void highscore() {

		BorderPane pane = new BorderPane();
		pane.setMinSize(250,250);
		pane.setMaxSize(250,250);
		messageBox.setSpacing(10);
		messageBox.setAlignment(Pos.CENTER);

		// Set fade in effect
		fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setCycleCount(1);

		// Set fade out effect
		fadeOut = new FadeTransition(Duration.seconds(1), pane);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setCycleCount(1);
		fadeOut.setDelay(Duration.seconds(3));

		pane.setTop(null);
		pane.setCenter(messageBox);
		pane.setBottom(null);

		fadeIn.play();

		// SET SCENE AND OPTIONS
		Scene scene = new Scene(pane);
		stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.getIcons().add(new Image("file:res/minepark.png"));
		stage.setScene(scene);
		stage.setTitle("Minepark High Scores");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.requestFocus();
		stage.show();

	}

	public static void score() {

		finalscore = Play.getTimer();
		difficulty = Play.difficulty;

		try {

			// Load high score records from local file
			// One file for each difficulty level
			isEmpty = false;

			file = new File(HIGHSCORES + difficulty + ".txt");
			if (!file.exists()) {
				file.createNewFile();
				isEmpty = true;
			}

			input = new Scanner(file);
			String line, leadername;
			int leaderscore;
			boolean highscore = false;

			// Iterate through current records
			// Add records to leaderboard array
			// Check if this is a highscore
			if (input.hasNextLine()) {
				do {
					line = input.nextLine();
					leadername = line.substring(0, line.indexOf(":"));
					leaderscore = Integer.parseInt(line.substring(line.indexOf(":") + 2, line.length()));
					leaders.add(leadername + ": " + leaderscore);
					if (finalscore < leaderscore) {
						highscore = true;
					}
				} while (input.hasNextLine());
			} 
			else {
				isEmpty = true;
			}

			// If leaderboard is empty or this is a high score
			// Process new highscore
			int highscores = leaders.size();
			if (highscore || highscores < 5 || isEmpty) {

				String text = "Congratulations!\n\n";
				text += "You survived Minepark and you are\n";
				text += "one of the top 5 fastest players\n\n";
				text += "Level: " + difficulty.toUpperCase();
				Text message = new Text(text);
				message.setTextAlignment(TextAlignment.CENTER);

				TextField nameField = new TextField();
				nameField.setPromptText("Enter your nickname:");
				nameField.setMaxWidth(100);
				nameField.setAlignment(Pos.CENTER);
				Button button = new Button("Submit");

				Label result = new Label();
				result.setTextAlignment(TextAlignment.CENTER);
				result.setText("\n\n");

				messageBox.getChildren().clear();
				messageBox.getChildren().addAll(message, nameField, button, result);

				// Create a new window (stage)
				// Load and display message and form
				highscore();

				// Submit form and call leaderboard to process record
				button.setOnAction(e -> {
					if ((nameField.getText() != null && !nameField.getText().isEmpty())) {
						name = nameField.getText();
						result.setText(name + ", your time was " + (finalscore - 1) + " seconds.\n"
								+ "Now you are on the Hall of Heroes!");
						button.setDisable(true);

						fadeOut.play();

						// After message fade out, process record
						fadeOut.setOnFinished(e1 -> {
							highscores();
						});

					} else {
						result.setText("Please enter your nickname\n\n!");
					}
				});

			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + HIGHSCORES + " not found!");
		} catch (IOException e) {
			System.out.println("Input / output error!");
		}
	}

	public static void highscores() {

		try {

			difficulty = Play.difficulty;

			// Check for number of highscores
			// Remove last place, if needed
			// Add new highscore to the leaderboard array
			int i = 0;
			if (isEmpty) {
				leaders.add(name + ": " + finalscore);
			}
			else {
				for (String s: leaders) {
					leaderscore = Integer.parseInt(s.substring(s.indexOf(":") + 2, s.length()));
					if (finalscore < leaderscore || i == highscores - 1) {
						leaders.add(i, name + ": " + finalscore);
						break;
					}
					i++;
				}
			}

			highscores = leaders.size();
			if (highscores > 5) leaders.remove(highscores - 1);

			// Display leaderboard
			// Write record to the highscores file
			PrintWriter output = new PrintWriter(file);
			String text = "Hall of Heroes\n";
			text += "-" + difficulty.toUpperCase() + "-\n\n";
			for (String s: leaders) {
				text += s + "\n";
				output.println(s);
			}
			Text message = new Text(text);
			message.setTextAlignment(TextAlignment.CENTER);
			messageBox.getChildren().clear();
			messageBox.getChildren().add(message);

			fadeIn.play();
			fadeOut.setDelay(Duration.seconds(5));
			fadeOut.play();

			// After leaderboard fade out, close window
			fadeOut.setOnFinished((e) -> {
				output.close();
				stage.close();
			});

		} catch (FileNotFoundException e) {
			System.out.println("\nFile " + HIGHSCORES + " not found!");
		}
	}

	public static void leaderboard() {

		try {

			difficulty = Play.difficulty;

			// Check file for highscores			
			file = new File(HIGHSCORES + difficulty + ".txt");
			if (!file.exists()) {
				file.createNewFile();
				isEmpty = true;
			}
			input = new Scanner(file);
			leaders.clear();
			String line, leadername;
			int leaderscore;
			if (input.hasNextLine()) {
				do {
					line = input.nextLine();
					leadername = line.substring(0, line.indexOf(":"));
					leaderscore = Integer.parseInt(line.substring(line.indexOf(":") + 2, line.length()));
					leaders.add(leadername + ": " + leaderscore);
				} while (input.hasNextLine());
			} 
			else {
				isEmpty = true;
			}
			input.close();

			// Display leaderboard
			String text = "Hall of Heroes\n";
			text += "-" + difficulty.toUpperCase() + "-\n\n";

			if (isEmpty) {
				text += "NO HIGH SCORES\n";
			}
			else {
				for (String s: leaders) {
					text += s + "\n";
				}
			}
			Text message = new Text(text);
			message.setTextAlignment(TextAlignment.CENTER);
			messageBox.getChildren().clear();
			messageBox.getChildren().add(message);

			highscore();
			fadeOut.setDelay(Duration.seconds(5));
			fadeOut.play();

			// After leaderboard fade out, close window
			fadeOut.setOnFinished((e) -> {
				stage.close();
			});

		} catch (FileNotFoundException e) {
			System.out.println("\nFile " + HIGHSCORES + difficulty + " not found!");
		} catch (IOException e) {
			System.out.println("Input/output error!");
		}
	}

}
