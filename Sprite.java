 import java.awt.Graphics;
 import java.awt.Image;
 
 public class Sprite {

   public Image image;  // the image to be drawn for this sprite
   private Game gp; // the game in which the ship exists
   // constructor
   public Sprite (Image i) {
     image = i;
   } // constructor

   // return width of image in pixels
   public int getWidth() {
      return image.getWidth(null);
   } // getWidth

   // return height of image in pixels
   public int getHeight() {
      return image.getHeight(null);
   } // getHeight

   // draw the sprite in the graphics object provided at location (x,y)
   public void draw(Graphics g, int x, int y) {
	   //Resizer uTool = new Resizer();
	   //uTool.scaleImage(image, gp.tileSize, gp.tileSize);
	  //Image newImage = image.getScaledInstance(72, 72, Image.SCALE_DEFAULT);
      g.drawImage(image, x, y, null);
   } // draw

 } // Sprite