import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends Button {
	int type; // type is a number meaning (0 to 8) for points of contact with mines and (9) for mines
	int row, col;
	String state;
	boolean flagged;
	ImageView imageTile, imageMine, imageFlag;
	double tileSize = Grid.tileSize;

	public Tile() {
		this.state = "covered";

		setMinWidth(tileSize);
		setMinHeight(tileSize);
		setMaxWidth(tileSize);
		setMaxHeight(tileSize);

		int tile = (int)(Math.random() * 4);
		imageTile = new ImageView(new Image(tile == 1 ? "file:res/tiles/block1.png" : tile == 2 ? "file:res/tiles/block2.png" : "file:res/tiles/block3.png"));
		imageTile.setFitHeight(tileSize);
		imageTile.setFitWidth(tileSize);

		imageMine = new ImageView(new Image("file:res/tiles/mine.png"));
		imageMine.setFitHeight(tileSize);
		imageMine.setFitWidth(tileSize);

		imageFlag = new ImageView(new Image("file:res/tiles/flag.png"));
		imageFlag.setFitHeight(tileSize);
		imageFlag.setFitWidth(tileSize);

		setGraphic(imageTile);
	}

	public void click() {
		// Handle primary click, if is safe or has flag, do nothing
		if (state.equals("covered")) {
			if (type == 9) {
				// Hit a mine
				mine();
			} else if (this.type == 0) {
				// Hit an empty tile
				Grid.clearEmpty(this.row, this.col);
			} else {
				// Hit a tile that is in contact with a mine
				// Clear it and display the number of mines
				setGraphic(null);
				setText(Integer.toString(type));
				setStyle("-fx-font-size: " + tileSize/2 + "px; -fx-text-fill: hsb(" + (int)120/(1.5*this.type) + ", 100%, 100%); ");
				Play.setCleared(Play.getCleared() - 1);
			}						
			this.state = "cleared";
		}
	}
	public void flag() {
		if (flagged == true) {
			unflag();
		} else {
			setGraphic(this.imageFlag);
			flagged = true;
			state = "flagged";
			Play.mines -= 1;
			Play.getBase().setTop(ScoreBar.scoreBar());
		}
	}

	public void unflag() {
		setGraphic(this.imageTile);
		state = "covered";
		flagged = false;
		Play.mines += 1;
		Play.getBase().setTop(ScoreBar.scoreBar());
	}

	public void mine() {
		setGraphic(this.imageMine);
		setStyle("-fx-background-color: 'RED'; ");
		Play.gameOver(this);
	}

	public void clear() {
		if (type == 0) {
			// Hit an empty tile
			Grid.clearEmpty(this.row, this.col);
		} else if (type == 9) {
			setGraphic(this.imageMine);
			// Hit a tile that is in contact with a mine
			// Clear it and display the number of mines
		} else { 
			setGraphic(null);
			setText(Integer.toString(type));
			setStyle("-fx-font-size: " + tileSize/2 + "px; -fx-text-fill: hsb(" + (int)120/(1.5*this.type) + ", 100%, 100%); ");
		}	
		setDisable(true);
		opacityProperty().set(90.0);
	}
}