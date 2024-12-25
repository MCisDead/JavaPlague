import java.awt.Rectangle;

public class PlayerEntity extends Entity {

	private Game game; 
	private String image;
	private int currentColumn;
	private int currentRow;

	// player constructor
	public PlayerEntity(Game g, String r, int newX, int newY) {
	    super(r, newX, newY);  // calls the constructor in Entity
	    game = g;
	    solidArea = new Rectangle(13 , 56 , 54, 32);
	    spriteNum = 1;
	    image = "player/down1.png";
	    keyTriGet = false;
	    keyStarGet = false;
	    keyCirGet = false;
	    currentColumn = worldX / game.tileSize;
	    currentRow = worldY / game.tileSize;
	} // PlayerEntity
  
	public int getPlayerColumn(){  
		return currentColumn;
	} // getPlayerColumn
  
	public int getPlayerRow(){	  
		return currentRow;
	} // getPlayerRow
  
	public void trackRowCol() {
		currentRow = (worldY + solidArea.y)/game.tileSize;
		currentColumn = (worldX + solidArea.x)/game.tileSize;
	} // trackRowCol
  
	// get current player movement image
	public String getPlayerImage() {	  
		up1 = "player/up1.png";
		down1 = "player/down1.png";
		left1 = "player/left1.png";
		right1 = "player/right1.png";
		up2 = "player/up2.png";
		down2 = "player/down2.png";
		left2 = "player/left2.png";
		right2 = "player/right2.png";
		up3 = "player/up3.png";
		down3 = "player/down3.png";
		left3 = "player/left3.png";
		right3 = "player/right3.png";
		down4 = "player/down4.png";
		left4 = "player/left4.png";
		right4 = "player/right4.png";
		up4 = "player/up4.png";
		  
		if (movingUp) {
			assignImage(up1, up2, up3, up4);
		} // if	
		
		else if (movingDown) {
			assignImage(down1, down2, down3, down4);	  
		}// else if
		
		else if (movingLeft) {
			assignImage(left1, left2, left3, left4);
		} // else if
		
		else if (movingRight) {
			assignImage(right1, right2, right3, right4);
		} // else if
		
		return image;
	} // getPlayerImage
  
	// sets player image based on movement and changes image if player has stun hammer
	private void assignImage(String imagePath1, String imagePath2, String imagePath3, String imagePath4) {
		if (hasHammer == false) {
			if(spriteNum == 1) {
				image = imagePath1;
			}
			else if(spriteNum == 2) {
				image = imagePath2;
			}	
		} // if
		else if (hasHammer) {
			if(spriteNum == 1) {
				image = imagePath3;
			} // if
			else if(spriteNum == 2) {
				image = imagePath4;
			} // else if
		} // else if  
	} // assignImage
  
	// player's movement
	public void move (long delta){
	  
		// if tile has collision, don't move
		if (collisionOn == false) {
	    	super.move(delta);
	    } // if
	    else if (collisionOn == true){
	    	return;
	    } // else if
	} // move
	
   @Override
   public void setHorizontalMovement(double newDX) {
	   dx = newDX;
   } // setHorizontalMovement
   
   // stuns monsters upon collision with player possessing stun hammer
   public void collidedWith(Entity other) {
	   if (other instanceof Monster) {
		   if (other.getStunBool() == true) {
			   other.stunnedTouch = true;
		   } // if
		   else {
			   game.notifyDeath();
		   } // else    	     
     } // if
   } // collidedWith    

} // ShipEntity class