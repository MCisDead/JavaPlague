import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class TileManager {
	Game gp;
	public Tile[] tile; 
	public int mapTileNum[][][];
	
	// tile manager default constructor
	public TileManager() {
		tile = new Tile[1];
	} // TileManager
	
	// tile manager constructor
	public TileManager(Game gp) {
		this.gp = gp;		
		tile = new Tile[50]; // how many types of tiles there are (images)
		mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();

		// assign maps
		loadMap("/maps/mainHall.txt", 0);
		loadMap("/maps/hydrotherapy.txt", 1);
	} // TileManager
	
	// set up the tiles and images
	public void getTileImage() {
		 
		setup(1, "lightwall", true, false);
		setup(2, "void", true, false);
		setup(3, "wallpaper", true, false);
		setup(4, "panelwallpaper", true, false);
		setup(5, "panel", true, false);
		setup(6, "floor", false, false);
		setup(7, "darkwall", true, false);
		setup(8, "frametop", true, false);
		setup(9, "frameleft", true, false);
		setup(10, "doortopleft", true, false);
		setup(11, "doortopright", true, false);
		setup(12, "frameright", true, false);
		setup(13, "doorleft", true, false);
		setup(14, "doorright", true, false);
		setup(15, "doorbottomleft", true, false); 
		setup(16, "doorbottomright", true, false); 
		
		// pool map exclusive
		setup(17, "panelwallpaperleft", true, false);
		setup(18, "panelwallpaperright", true, false);
		setup(19, "panelleft", true, false);
		setup(20, "panelright", true, false);
		setup(21, "pooldark", true, false);
		setup(22, "poollight", false, false);
		setup(23, "water", true, false);
		setup(24, "tunnel", true, false);
		setup(25, "darkwater", true, false);
		
		// warp to pool and back
		setup(26, "void", true, true);
		
	} // getTileImage
	
	
	// assign values to booleans of each tile index to check for property
	public void setup(int index, String imageName, boolean collision, boolean warp) {
		Resizer uTool = new Resizer();
		
		try {
			tile[index]= new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
			tile[index].warp = warp;
			if (index == 13 || index == 15 || index == 16) {
				tile[index].isGoal = true; 
			} // if			
		} catch (IOException e) {
			e.printStackTrace();
		} // catch
		
	} // setup
	
	// read contents of map txt file
	public void loadMap(String filepath, int map) {
		try {
			
			// read the txt file
			InputStream is = getClass().getResourceAsStream(filepath);
			
			// BufferedReader reads the content of the text file
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				// reads a single line
				String line = br.readLine();
				
				while(col < gp.maxWorldCol) {
					
					// split the line and get tile numbers one by one
					String numbers[] = line.split(" ");
					
					// change the string to integer
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[map][col][row] = num;
					col++;
				} // while
				
					if(col == gp.maxWorldCol) {
						col=0;
						row++;
					} // if
				
			} // while
			
			br.close();
		}catch(Exception e) {
			 e.printStackTrace();
		} // catch
	} // loadMap
	
	// called in game draw the tiles on the screen
	public void draw(Graphics2D g2) {
		int worldCol = 0;
		int worldRow = 0;
		int worldX = 0;
		int worldY = 0;
		
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];		
			g2.drawImage(tile[tileNum].image, worldX, worldY, gp.tileSize, gp.tileSize, null);			
			worldCol++;
			worldX += gp.tileSize;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
				worldX = 0;
				worldY += gp.tileSize;
			} // if

		} // while
	} // draw
} // TileManager