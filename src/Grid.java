import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class Grid extends GridPane {
	public static int tileSize;
	private static int[][] tiles;
	private static GridPane grid;

	public static GridPane gridPane(int numRows, int numCols, int mines) {
		int[] clicks = new int[1];
		double gridSize = 500.0;
		tileSize = (int)(gridSize/numRows);
		tiles = new int[numRows][numCols];
		grid = new GridPane();
		grid.setPrefSize(tileSize*numCols, tileSize*numRows);

		// INITIAL GRID LAYOUT
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				Tile tile = new Tile();
				tile.type = 0;
				tile.row = row;
				tile.col = col;
				tile.setOnMousePressed(e -> {
					MouseButton mouseButton = e.getButton();
					if (mouseButton == MouseButton.PRIMARY)
						if (mines > 0 && !tile.state.equals("cleared")) tile.sure();
				});
				tile.setOnMouseReleased(e -> tile.covered());
				// Set new properties for the tiles, according to the clicked tile.
				tile.setOnMouseClicked(e -> {
					MouseButton mouseButton = e.getButton();

					// Handle secondary mouse button click, which sets flags on and off
					// The number of mines flagged has to be less than the total number of mines
					// The tile state has to be "covered"
					if (mouseButton == MouseButton.SECONDARY) {
						if (mines > 0 && !tile.state.equals("cleared")) tile.flag();
						if (tile.state.equals("cleared")) clearEmpty(tile.row, tile.col);
					} else if (clicks[0] == 0 && !tile.state.equals("flagged")) {
						// If it's the first click on the grid, position the mines to set the grid layout.
						// First click is always on an empty tile
						tiles = TileSet.setTiles(numRows, numCols, mines, tile, tiles, grid);
						clearEmpty(tile.row, tile.col);
						clicks[0]++;
					} else {
						// Handle primary click on a tile, checks for state, flags and mines
						tile.click();
					}
				});
				// Add tiles to grid pane
				grid.add(tile, col, row);
			}
		}
		return grid;
	}

	// Clear empty tiles around a clicked tile, search using recursion
	static void clearEmpty(int row, int col) {
		int rows = tiles.length;
		int cols = tiles[0].length;
		int item = row * cols + col;
		Tile tile = (Tile) grid.getChildren().get(item);
		if (tiles[row][col] < 9 && tile.state.equals("covered")) {
			//			System.out.println(tile.type + " ");
			if (tile.type == 0) {
				tile.setGraphic(null);
				tile.state = "cleared";
				Play.setCleared(Play.getCleared() - 1);
			}
			if (tile.type < 9 && tile.type > 0) {
				tile.setGraphic(null);
				tile.setText(Integer.toString(tile.type));
				tile.setStyle("-fx-font-size: " + tileSize/2 + "px; -fx-text-fill: hsb(" + (int)120/(1.5*tile.type) + ", 100%, 100%); ");
				tile.state = "cleared";
				Play.setCleared(Play.getCleared() - 1);
			} else {	
				if (col > 0) {
					clearEmpty(row, col-1);
				}
				if (col < cols-1) {
					clearEmpty(row, col+1);
				}
				if (row > 0) {
					clearEmpty(row-1, col);
					if (col > 0) {
						clearEmpty(row-1, col-1);
					}
					if (col < cols-1) {
						clearEmpty(row-1, col+1);
					}
				}
				if (row < rows-1) {
					clearEmpty(row+1, col);
					if (col > 0) {
						clearEmpty(row+1, col-1);
					}
					if (col < cols-1) {
						clearEmpty(row+1, col+1);
					}

				}
			}
		}
	}

	// Getters and Setters
	public static GridPane getGrid() {
		return grid;
	}
}