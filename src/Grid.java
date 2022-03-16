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
				tile.state = -1;
				tile.type = 0;
				tile.row = row;
				tile.col = col;
				// CLICK HANDLER
				tile.setOnMouseClicked(e -> {
					MouseButton mouseButton = e.getButton();
					if (clicks[0] == 0) {
						setTiles(numRows, numCols, mines, tile);
						clearEmpty(tile.row, tile.col);
						clicks[0]++;
						//						System.out.println(clicks[0]);
					} else {			
						int state = (tile.state == 10 ? 11 : tile.state < 0 ? tile.type : 11);
						if (mouseButton == MouseButton.SECONDARY) {
							state = (tile.state == 10 ? 12 : mines > 0 && tile.state < 0 ? 10 : 11);
						}
						switch(state) {

						default: // primary click, no flag, no mine
							if (tile.type == 0 && tile.state == -1) {
								clearEmpty(tile.row, tile.col);
							} else {
								tile.setGraphic(null);
								tile.setText(Integer.toString(tile.type));
								tile.setStyle("-fx-font-size: " + tileSize/2 + "px; -fx-text-fill: hsb(" + (int)120/(1.5*tile.type) + ", 100%, 100%); ");
							}
							tile.state = tile.type;
							Play.setCleared(Play.getCleared() - 1);
							if (Play.getCleared() < 1) {
								Play.gameWin(grid);
							}
							break;

						case 9: // primary click, mine
							tile.setGraphic(tile.imageMine);
							tile.setStyle("-fx-background-color: 'RED'; ");
							Play.gameOver(tiles, tile, grid);
							break;

						case 10: // secondary click, set flag
							tile.setGraphic(tile.imageFlag);
							if (tile.type == 9) tiles[tile.row][tile.col] = 10;
							tile.state = 10;
							Play.setMines(Play.getMines() - 1);
							Play.getBase().setTop(ScoreBar.scoreBar());
							break;

						case 11: // any click, if is safe or has flag, do nothing
							break;

						case 12: // secondary click, remove flag
							tile.setGraphic(tile.imageTile);
							if (tile.type == 10) tile.type = 9;
							tile.state = -1;
							Play.setMines(Play.getMines() + 1);
							Play.getBase().setTop(ScoreBar.scoreBar());
							break;
						}
					}
				});
				grid.add(tile, col, row);
			}
		}
		return grid;
	}

	public static int[][] setTiles(int numRows, int numCols, int mines, Tile tile) {

		tiles = new int[numRows][numCols];
		int num = numCols * numRows;
		tiles[tile.row][tile.col] = 0;
		System.out.println("Clicked tile is: " + tile.row + "," + tile.col);

		// MINES POSITIONING
		// Create an array of random positions for mines, no repetitions allowed
		int[] positions = new int[num];
		for (int i = 0; i < num; i++) positions[i] = i;

		for (int i = 0; i < mines; i++) {
			boolean isValid = false;
			int index = positions.length - 1 - i;
			int position = (int)(Math.random() * index);
			int mine = positions[position];
			int row = (int)Math.floor((1.0*mine)/numCols);
			int col = mine%numCols;
			System.out.print("Trying mine at: " + row + "," + col);

			//						System.out.print("Mine " + mine + " taken from position " + position + " in range 0 to " + index);
			//						System.out.println(" placed at (" + row + "," + col + ")");

			// Avoid placing mines close range of first tile clicked
			if (Math.abs(row - tile.row) > 1 || Math.abs(col - tile.col) > 1) {
				tiles[row][col] = 9;
				isValid = true;
			}
			System.out.println(isValid);
			if (!isValid) {
				i--;
				continue;
			}

			// Surrounding tiles numbering
			if (row < numRows - 1) {
				if (col > 0 && tiles[row+1][col-1] != 9) tiles[row+1][col-1]+= 1;
				if (tiles[row+1][col] !=9) tiles[row+1][col] += 1;
				if (col < numCols - 1 && tiles[row+1][col+1] != 9) tiles[row+1][col+1] += 1;
			}

			if (col < numCols - 1 && tiles[row][col+1] != 9) tiles[row][col+1] += 1;
			if (col > 0 && tiles[row][col-1] != 9) tiles[row][col-1] += 1;
			if (row > 0) {
				if (col > 0 && tiles[row-1][col-1] != 9) tiles[row-1][col-1] += 1;
				if (tiles[row-1][col] != 9) tiles[row-1][col] += 1;
				if (col < numCols - 1 && tiles[row-1][col+1] != 9) tiles[row-1][col+1] += 1;
			}

			positions[position] = positions[index];

			// Check for random positioning, no repetitions allowed
			//
			//			System.out.println("Mine " + positions[index] + " moved from position " + index + " to position " + position);
			//
			//			int count = 0;
			//			for (int[] row1: tiles) {
			//				for (int col1: row1) {
			//					System.out.print(col1);
			//					count += (col1 == 9 ? 1 : 0);
			//				}
			//				System.out.println();
			//			}
			//			System.out.println(count + " mines placed.");
		}

		// TILES POSITIONING
		// After first click and mines positioning, update grid layout
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				int item = row * numCols + col;
				System.out.print(tiles[row][col] + " ");
				tile = (Tile) grid.getChildren().get(item);
				tile.type = tiles[row][col];
				tile.state = -1;
			}
			System.out.println();
		}
		System.out.println();
		return tiles;
	}

	static void clearEmpty(int row, int col) {
		int rows = tiles.length;
		int cols = tiles[0].length;
		int item = row * cols + col;
		Tile tile = (Tile) grid.getChildren().get(item);
		if (tiles[row][col] < 9 && tile.state < 0) {
			//			System.out.println(tile.type + " ");
			if(tile.type == 0) {
				tile.setGraphic(null);
				tile.state = 0;
				Play.setCleared(Play.getCleared() - 1);
				if (Play.getCleared() < 1) {
					Play.gameWin(grid);
				}
			}
			if (tile.type < 9 && tile.type > 0) {
				tile.setGraphic(null);
				tile.setText(Integer.toString(tile.type));
				tile.setStyle("-fx-font-size: " + tileSize/2 + "px; -fx-text-fill: hsb(" + (int)120/(1.5*tile.type) + ", 100%, 100%); ");
				tile.state = tile.type;
				Play.setCleared(Play.getCleared() - 1);
				if (Play.getCleared() < 1) {
					Play.gameWin(grid);
				}
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
}

