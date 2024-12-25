import java.awt.image.BufferedImage;

public class Tile {
	public BufferedImage image;
	
	// tile attributes
	public boolean collision; // true if player can collide with tile 
	public boolean warp;
	public boolean canPass;
	public boolean isGoal;
	
	// tile constructor
	public Tile() {
		collision = false;
		warp = false;
		canPass = false;
		isGoal = false;
	} // Tile
	
	// 
	public boolean getWinPass() {
		return canPass;
	} // getWinPass
	
	public void setWinPass(boolean winPass) {
		canPass = winPass;
	} // setWinPass
	
} // Tile

