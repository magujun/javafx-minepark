import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Tile extends Button {
	int type; // type is a number meaning (0 to 8) for points of contact with mines and (9) for mines
	int row, col;
	boolean covered, flagged, checked; // !covered is cleared, flagged needs no explanation
	ImageView imageTile, imageMine, imageFlag, imageMisflag, imageSure;
	double tileSize = Grid.tileSize;
	int[][] tiles = Grid.getTiles();
	int rows = tiles.length;
	int cols = tiles[0].length;
	GridPane grid = Grid.getGrid();

	// Tile constructor
	public Tile() {

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

		imageMisflag = new ImageView(new Image("file:res/tiles/misflag.png"));
		imageMisflag.setFitHeight(tileSize);
		imageMisflag.setFitWidth(tileSize);

		imageSure = new ImageView(new Image("file:res/tiles/sure.png"));
		imageSure.setFitHeight(tileSize);
		imageSure.setFitWidth(tileSize);

		type = 0;
		flagged = false;
		covered = true;
		setGraphic(imageTile);

	}

	public void click() {

		// Handle primary click, if is cleared or has flag, do nothing
		if (covered && !flagged) {

			// Hit a mine
			if (type == 9) {
				mine();
			}

			// Hit an empty tile, clear surrounding empty tiles
			else if (type == 0) {
				clearAroundEmpty(row, col);
			}

			// Hit a tile that is in contact with a mine
			// Clear it and display the number of surrounding mines
			else {
				setGraphic(null);
				setText(Integer.toString(type));
				setStyle("-fx-font-size: " + tileSize/2 + "px; -fx-text-fill: hsb(" + (int)120/(1.5*type) + ", 100%, 100%);");
				Play.setCleared(Play.getCleared() - 1);
			}	
			covered = false;
			checked = true;
		}
	}

	public void check() {
		if (!covered) {
			searchSurrounding();
		}
	}

	public void tease() {
		if (covered && !flagged) {
			setGraphic(imageSure);
			setOnMouseReleased(e -> {
				if (covered && !flagged) setGraphic(imageTile);
			});
		}
	}

	public void flag() {
		if (flagged == true) {
			unflag();
		} 

		else if (Play.mines > 0 && covered) {
			setGraphic(imageFlag);
			flagged = true;
			Play.mines -= 1;
			Play.getBase().setTop(ScoreBar.scoreBar());
		}
	}

	public void unflag() {
		flagged = false;
		setGraphic(imageTile);
		Play.mines += 1;
		Play.getBase().setTop(ScoreBar.scoreBar());
	}

	public void mine() {
		setGraphic(imageMine);
		setStyle("-fx-background-color: 'RED'; ");
		Play.setDead(true);
		Play.gameOver(this);
	}

	public void clear() {
		// Clear an empty tile
		// Update the number of cleared tiles
		if (type == 0) {
			setGraphic(null);
			Play.setCleared(Play.getCleared() - 1);
		} 

		// Clear a tile with an unflagged mine
		// Sets mine field to point to this tile and isDead 
		// Timeline handles gameOver(mine)
		else if (type == 9 && !flagged) {
			setGraphic(imageMine);
			Play.setDead(true);
			Play.mine = this;
		} 

		// Clear a tile with an unflagged mine
		// Sets mine field to point to this tile and isDead 
		// Timeline handles gameOver(mine)
		else if (type != 9 && flagged) {
			setGraphic(imageMisflag);
		}

		// Clear a tile that is in contact with a mine
		// Update the number of cleared tiles
		else if (type > 0 && type < 9) { 
			setGraphic(null);
			setText(Integer.toString(type));
			setStyle("-fx-font-size: " + tileSize/2 + "px; -fx-text-fill: hsb(" + (int)120/(1.5*type) + ", 100%, 100%);");
			Play.setCleared(Play.getCleared() - 1);
		}
		covered = false;
		checked = true;
	}

	// Clear tiles around an empty clicked tile, using recursive search
	public void clearAroundEmpty(int row, int col) {
		int element = row * cols + col;
		Tile tile = (Tile) grid.getChildren().get(element);
		if (tile.covered) {
			if (tile.type == 0) tile.clear();
			if (tile.type > 0) tile.clear();
			else searchAround(row, col);
		}
	}	

	// Clear tiles around a cleared, non empty tile, using recursive search
	// Only searches aroung the clicked tile coordinates (row, col)
	public void clearSurrounding(int r, int c) {
		int element = r * cols + c;
		Tile checking = (Tile) grid.getChildren().get(element);
		if(!checking.checked) {
			checking.clear();
		}
	}


	// Recursively search for surrounding mines
	// Take into account grid array limits
	public void searchAround(int row, int col) {
		// Search left
		if (col > 0) {
			clearAroundEmpty(row, col-1);
		}
		// Search right
		if (col < cols-1) {
			clearAroundEmpty(row, col+1);
		}
		// Search up 
		if (row > 0) {
			clearAroundEmpty(row-1, col);
			if (col > 0) {
				clearAroundEmpty(row-1, col-1);
			}
			if (col < cols-1) {
				clearAroundEmpty(row-1, col+1);
			}
		}
		// Search down
		if (row < rows-1) {
			clearAroundEmpty(row+1, col);
			if (col > 0) {
				clearAroundEmpty(row+1, col-1);
			}
			if (col < cols-1) {
				clearAroundEmpty(row+1, col+1);
			}

		}
	}
	// Recursively search for surrounding mines
	// Take into account grid array limits
	public void searchSurrounding() {
		// Search left
		if (col > 0) {
			clearSurrounding(row, col-1);
		}
		// Search right
		if (col < cols-1) {
			clearSurrounding(row, col+1);
		}
		// Search up 
		if (row > 0) {
			clearSurrounding(row-1, col);
			if (col > 0) {
				clearSurrounding(row-1, col-1);
			}
			if (col < cols-1) {
				clearSurrounding(row-1, col+1);
			}
		}
		// Search down
		if (row < rows-1) {
			clearSurrounding(row+1, col);
			if (col > 0) {
				clearSurrounding(row+1, col-1);
			}
			if (col < cols-1) {
				clearSurrounding(row+1, col+1);
			}

		}
	}
}