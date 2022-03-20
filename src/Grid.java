import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class Grid extends GridPane {
	public static int tileSize;
	private static int[][] tiles;
	private static GridPane grid;

	public static GridPane gridPane(int numRows, int numCols, int mines) {

		// Create an array of clicks for the counter inside the event handler
		int[] clicks = new int[1];

		// Instantiate a fixed grid with calculated tile size
		double gridSize = 500.0;
		tileSize = (int)(gridSize/numRows);
		grid = new GridPane();
		grid.setPrefSize(tileSize*numCols, tileSize*numRows);

		tiles = new int[numRows][numCols];

		// INITIAL GRID LAYOUT
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				Tile tile = new Tile();
				tile.row = row;
				tile.col = col;

				// Set new tile properties, according to predetermined click events for the tile

				// Handle tease click {
				tile.setOnMousePressed(e -> {
					MouseButton mouseButton = e.getButton();
					if (mouseButton == MouseButton.PRIMARY) tile.tease();
				});

				// Handle proper mouse click
				tile.setOnMouseClicked(e -> {
					MouseButton mouseButton = e.getButton();

					// Handle first click on the grid
					// First click is always on an empty tile
					// Position the mines to set the grid layout and increment click count
					if (clicks[0] == 0) {
						if (mouseButton == MouseButton.PRIMARY) {
							tiles = TileSet.setTiles(numRows, numCols, mines, tile, tiles, grid);
							tile.click();
							clicks[0]++;
						}
					}

					// Handle secondary mouse button click, which sets flags on and off
					// The number of flagged mines has to be less than the total number of mines
					// The tile state has to be "covered"
					// Also Check for flagged mines around a cleared tile
					// If all surrounding mines are correctly flagged, clear surrounding tiles
					else if (mouseButton == MouseButton.SECONDARY) {
						if (mines > 0) tile.flag();
						tile.check();
					}

					// Handle primary single click on a tile, checks for state, flags and mines
					else {
						tile.click();				
					}
				});

				// Add tiles to grid pane
				grid.add(tile, col, row);
			}
		}
		return grid;
	}

	// Getters and Setters
	public static GridPane getGrid() {
		return grid;
	}

	public static int[][] getTiles() {
		return tiles;
	}
}