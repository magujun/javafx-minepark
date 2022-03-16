import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends Button {
	int state, type, row, col;
	ImageView imageTile, imageMine, imageFlag;
	double tileSize = Grid.tileSize;

	public Tile() {
		state = 0;

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
}
