/* Entity.java
 * An entity is any object that appears in the game.
 * It is responsible for resolving collisions and movement.
 */
 
 import java.awt.*;
import java.awt.image.BufferedImage;
 
 public abstract class Entity {
	 
	// variables for sprite location
    protected int worldX, worldY;
    private int speedX;
    private int speedY;
    private String r;
    protected Sprite sprite; // this entity's sprite
    protected double dx; // horizontal speed (px/s)  + -> right
    protected double dy; // vertical speed (px/s) + -> down
    
    // booleans to change entity status
    protected boolean movingUp = false;
    protected boolean movingDown = false;
    protected boolean movingLeft = false;
    protected boolean movingRight = false;
    protected boolean stunned = false;
    protected boolean hasHammer = false;
    public boolean keyTriGet;
	public boolean keyCirGet;
	public boolean keyStarGet;
	public boolean speedingUp;
	protected boolean touchingGoal = false;
	protected boolean triangleDone;
	protected boolean circleDone;
	protected boolean starDone;
	protected boolean stunnedTouch;
	
	
	public int nextX;
	public int nextY;
    protected int spriteNum = 1;
    public String up1, up2, up3, up4, down1, down2, down3, down4, right1, right2, right3, right4, left1, left2, left3, left4;
    protected int moveSpeed = 90;
    
    // pathfinding
    public boolean onPath = true;

    // for collision detection
    public Rectangle solidArea;
    public boolean collisionOn = false;
    
    // bounding rectangle of this entity
    private Rectangle me = new Rectangle();
    
    // bounding rectangle of other entities
    private Rectangle him = new Rectangle();
                                             
    /* Constructor
     * input: reference to the image for this entity,
     *        initial x and y location to be drawn at
     */
     public Entity(String r, int newX, int newY) {
       worldX = newX;
       worldY = newY;
       this.r = r;
       sprite = (SpriteStore.get()).getSprite(this.r);
     } // constructor
     
     public void setSprite(String s) {
    	 this.r = s;
    	 sprite = (SpriteStore.get()).getSprite(this.r);
     } // setSprite
     
     public void setPosition(int x, int y) {
    	 this.worldX = x;
    	 this.worldY = y;
     }

     /* move
      * input: delta - the amount of time passed in ms
      * output: none
      * purpose: after a certain amout of time has passed,
      *          update the location
      */
     public void move(long delta) {
    	 
       // Sync world position with tile boundaries
    	speedX =  (int)(delta * dx / 1000);
    	speedY = (int)(delta * dy / 1000);
    	worldX += speedX; 
    	worldY += speedY; 
     } // move

     // get and set velocities
     public void setHorizontalMovement(double newDX) {
       dx = newDX;
     } // setHorizontalMovement

     public void setVerticalMovement(double newDY) {
       dy = newDY;
     } // setVerticalMovement

     public double getHorizontalMovement() {
       return dx;
     } // getHorizontalMovement

     public double getVerticalMovement() {
       return dy;
     } // getVerticalMovement
     
    public void setMoveSpeed(int amount){
    	moveSpeed = amount;
    } // setMoveSpeed

     // get position
     public int getX() {
       return worldX;
     } // getX

     public int getY() {
       return worldY;
     } // getY

    /*
     * Draw this entity to the graphics object provided at (x,y)
     */
     public void draw (Graphics g) {
       //sprite.draw(g,(int)x,(int)y);
       sprite.draw(g,worldX, worldY);
     }  // draw
     
    /* Do the logic associated with this entity.  This method
     * will be called periodically based on game events.
     */
     public void doLogic() {}
     
     /* collidesWith
      * input: the other entity to check collision against
      * output: true if entities collide
      * purpose: check if this entity collides with the other.
      */
     public boolean collidesWith(Entity other) {
       me.setBounds(worldX, worldY, sprite.getWidth(), sprite.getHeight());
       him.setBounds(other.getX(), other.getY(), 
                     other.sprite.getWidth(), other.sprite.getHeight());
       return me.intersects(him);
     } // collidesWith
     
     /* collidedWith
      * input: the entity with which this has collided
      * purpose: notification that this entity collided with another
      * Note: abstract methods must be implemented by any class
      *       that extends this class
      */
    public abstract void collidedWith(Entity other);

	public String getPlayerImage() {
		return null;
	} // getPlayerImage

	public void setImage(String s) {	
	} // setImage

	public boolean getSpeedBool() {
		return speedingUp;
	} // getSpeedBool
	  
	public void setSpeedBool(boolean set) {
		speedingUp = set;
	} // setSpeedBool
	
	public boolean getStunBool() {
		return stunned;
	} // getStunBool
	
	public void setStunBool(boolean set) {
		stunned = set;
	} // setStunBool
	
	public char[] getStunned() {
		return null;
	} // getStunned
	
	public void setStunTouch(boolean b) {
		stunnedTouch = b;
	} // setStunTouch
	
	public boolean getStunTouch() {
		return stunnedTouch;
	} // getStunTouch

	public String changeColorTri(Entity player) {
		return null;
	} // changeColorTri
	
	public void checkCollision() {
    } // checkCollision
	
	// method for pathfinding of all monster types to locate player
	public void searchPath(int goalCol, int goalRow, Game gp, Entity monster, boolean isGhost, Entity player) {
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;
        
        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, isGhost, this);

        if (gp.pFinder.search() == true) {

            // Next worldX & worldY
            nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;        

            // Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                monster.setVerticalMovement(-moveSpeed);
                monster.setHorizontalMovement(0);
            
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                monster.setVerticalMovement(moveSpeed);
                monster.setHorizontalMovement(0);
                
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
            	
                // left or right
                if (enLeftX > nextX) {
                    monster.setHorizontalMovement(-moveSpeed);
                    monster.setVerticalMovement(0);
                } // if
                if (enLeftX < nextX) {
                    monster.setHorizontalMovement(moveSpeed);
                    monster.setVerticalMovement(0);
                } // if
            } else if (enTopY > nextY && enLeftX > nextX) {
            	
                // up or left
                monster.setVerticalMovement(-moveSpeed);
                checkCollision();
                monster.setHorizontalMovement(0);
                
                if (player.collisionOn == true) {                  
                    monster.setHorizontalMovement(-moveSpeed);
                    monster.setVerticalMovement(0);
                } // if
                
            } else if (enTopY > nextY && enLeftX < nextX) {
                // up or right
                monster.setVerticalMovement(-moveSpeed);
                monster.setHorizontalMovement(0);
                
                if (player.collisionOn == true) {
                    monster.setHorizontalMovement(moveSpeed);
                    monster.setVerticalMovement(0);
                } // if
                
            } else if (enTopY < nextY && enLeftX > nextX) {
            	
                // down or left
                monster.setVerticalMovement(moveSpeed);
                monster.setHorizontalMovement(0);

                if (player.collisionOn == true) {
                    monster.setHorizontalMovement(-moveSpeed);
                    monster.setVerticalMovement(0);
                } // if
                
            } else if (enTopY < nextY && enLeftX < nextX) {
                // down or right
                monster.setVerticalMovement(moveSpeed);
                if (player.collisionOn == true) {
                    monster.setHorizontalMovement(moveSpeed);
                    monster.setVerticalMovement(0);
                } // if
            } // else if
        } // outer if
    } // searchPath

	public void setAction(Game game, Entity player) {
		// handled in monster class		
	} // setAction

	public int getPlayerColumn() {
		// handled in player class
		return 0;
	} // getPlayerColumn

	public int getPlayerRow() {
		// handled in player class
		return 0;
	} // getPlayerRow

	public void trackRowCol() {
		// handled in player class		
	} // trackRowCol

	public boolean getDrawMessage() {
		// for letter object
		return false;
	} // getDrawMessage

	public void setDrawMessage(boolean b) {
		// for letter object
	} // setDrawMessage
	
	public boolean getEnterCode() {
		// for Objects class
		return false;
	} // getEnterCode
	
	public void setEnterCode(boolean set) {
		// for Objects class
	} // setEnterCode
	
 } // Entity class