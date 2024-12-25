import java.awt.image.BufferedImage;

public class Objects extends Entity {
	public BufferedImage image;
	public String name;
	public String objType;
	private Game gp;
	private boolean drawMessage;
	private boolean enterCodeScreen;
	
	// Objects constructor
	public Objects(Game g, String r, int newX, int newY, String objType) {
		super(r, newX, newY);
		this.gp = g;
		this.objType = objType;
		speedingUp = false;
		drawMessage = false;
		enterCodeScreen = false;
	} // Objects
	
	// message object getter + setter
	public void setDrawMessage(boolean set) {
		drawMessage = set;
	} // setDrawMessage
	public boolean getDrawMessage() {
		return drawMessage;
	} // getDrawMessage
	
	// code safe screen object getter + setter
	public void setEnterCode(boolean set) {
		enterCodeScreen = set;
	} // setEnterCode
	public boolean getEnterCode() {
		return enterCodeScreen;
	} // getEnterCode
	 
	// collidedWith manages collisions with player and game items
	@Override
	public void collidedWith(Entity other) {
		if (other instanceof PlayerEntity) {
            if (this.objType != "letter" && this.objType != "keycode") {
            	gp.removeEntity(this);
            } // if    
            if (this.objType == "triangle") {
            	other.keyTriGet = true;
            	gp.showNotis();
            } // if
            else if (this.objType == "circle") {
                other.keyCirGet = true;
                gp.showNotis();
            } // else if
            else if (this.objType == "star") {
                other.keyStarGet = true;
                gp.showNotis();
            } // else if
            else if (this.objType == "speedBoost") {
            	other.speedingUp = true;
            } // else if
            else if (this.objType == "hammer") {
            	other.hasHammer = true;
            } // else if
            else if (this.objType == "letter") {
            	drawMessage = true;
            } // else if
            else if (this.objType == "keycode") {
            	enterCodeScreen = true;
            } // else if
        } // outer if
	} // collidedWith
} // Objects class 
