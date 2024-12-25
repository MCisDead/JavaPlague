import java.awt.Rectangle;

public class Monster extends Entity {
    public boolean isGhost; // for monsters of ghost type (can transcend walls)

    // constructor for monster
    public Monster(Game g, String r, int newX, int newY, Entity player, boolean isGhost, int moveSpeed) {
        super(r, newX, newY);
        this.solidArea = new Rectangle(8, 8, 12, 12);
        stunnedTouch = false;
        this.isGhost = isGhost;
        this.moveSpeed = moveSpeed;
    } // Monster

    // checks if monster is stunned before allowing movement
    public void move(long delta) {
        if (!this.collisionOn && stunnedTouch == false) {
            super.move(delta);
        } // if    
    } // move

    public void collidedWith(Entity other) {
    	// this is detected in playerEntity
    } // collidedWith
    
    // pathfinding initiation
    public void setAction(Game gp, Entity player) {
        if(onPath == true){
            int goalRow = player.getPlayerRow();
            int goalCol = player.getPlayerColumn();
            searchPath(goalCol, goalRow, gp, this, this.isGhost, player);
        } // if
    } // setAction
    
} // Monster