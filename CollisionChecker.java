public class CollisionChecker {
	Game gp;
	
	public CollisionChecker(Game gp) {
		this.gp = gp;
	} // CollisionChecker
	
	public void checkTile(Entity entity) {
		int entityLeftWorldX = (int) (entity.worldX + entity.solidArea.x -5);
		int entityRightWorldX = (int) (entity.worldX + entity.solidArea.x + entity.solidArea.width);
		int entityTopWorldY = (int) (entity.worldY + entity.solidArea.y - 10);
		int entityBottomWorldY = (int) (entity.worldY + entity.solidArea.y + entity.solidArea.height);
		
		int entityLeftCol = entityLeftWorldX/gp.tileSize;
		int entityRightCol = entityRightWorldX/gp.tileSize;
		int entityTopRow = entityTopWorldY/gp.tileSize;
		int entityBottomRow = entityBottomWorldY/gp.tileSize;
	
		int tileNum1, tileNum2;
		
		// collisions while entity moves up
		if (entity.movingUp == true) {
			entityLeftCol = (entityLeftWorldX + 20)/gp.tileSize;
			entityRightCol = (entityRightWorldX - 10)/gp.tileSize;
			
			tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
			
			// change maps
			if (gp.tileM.tile[tileNum1].warp == true || gp.tileM.tile[tileNum2].warp == true) {
				if (gp.currentMap == 0) {
					gp.currentMap = 1;
				} // inner if
				else if (gp.currentMap > 0) {
					gp.currentMap = 0;
				} // else if
			} // outer if

			// get keys
			if (gp.tileM.tile[tileNum1].isGoal == true) {
				entity.touchingGoal = true;
				System.out.println(entity.triangleDone);
				System.out.println(entity.circleDone);
				System.out.println(entity.starDone);
				if (entity.triangleDone == true && entity.circleDone == true && entity.starDone == true) {
					gp.notifyWin();
				} // inner if
				else {
					System.out.println("go get Triangle key");
				} // else
			} // outer if
			
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			} // if
			if (gp.tileM.tile[tileNum1].collision == false && gp.tileM.tile[tileNum2].collision == false) {
				entity.collisionOn = false;
			} // if		
		} // if entity moving up
		
		else if (entity.movingLeft == true) {
			entity.movingUp = false;
			entityTopRow = (entityTopWorldY + 10)/gp.tileSize;
			entityBottomRow = (entityBottomWorldY - 10)/gp.tileSize;
			
			tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
			
			// change maps
			if (gp.tileM.tile[tileNum1].warp == true || gp.tileM.tile[tileNum2].warp == true) {
				if (gp.currentMap == 0) {
					gp.currentMap = 1;
				} // inner if
				else if (gp.currentMap > 0) {
					gp.currentMap = 0;
				} // else if
			} // outer if
	
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			} // if
			if (gp.tileM.tile[tileNum1].collision == false && gp.tileM.tile[tileNum2].collision == false) {
				entity.collisionOn = false;
			} // if
		} // entity moving left if
		
		else if (entity.movingRight == true) {
			entityTopRow = (entityTopWorldY + 10)/gp.tileSize;
			entityBottomRow = (entityBottomWorldY - 10)/gp.tileSize;

			tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
			
			// change maps
			if (gp.tileM.tile[tileNum1].warp == true || gp.tileM.tile[tileNum2].warp == true) {
				if (gp.currentMap == 0) {
					gp.currentMap = 1;
				} // inner if
				else if (gp.currentMap > 0) {
					gp.currentMap = 0;
				} // else
			} // outer if
	
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			} // if
			if (gp.tileM.tile[tileNum1].collision == false && gp.tileM.tile[tileNum2].collision == false) {
				entity.collisionOn = false;
			} // if
		
		} // entity moving right if
		
		else if (entity.movingDown == true) {
			
			entityLeftCol = (entityLeftWorldX + 10)/gp.tileSize;
			entityRightCol = (entityRightWorldX - 10)/gp.tileSize;

			tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
			
			// change maps
			if (gp.tileM.tile[tileNum1].warp == true || gp.tileM.tile[tileNum2].warp == true) {
				if (gp.currentMap == 0) {
					gp.currentMap = 1;
				} // inner if
				else if (gp.currentMap > 0) {
					gp.currentMap = 0;
				} // else if
			} // outer if
	
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			} // if
			if (gp.tileM.tile[tileNum1].collision == false && gp.tileM.tile[tileNum2].collision == false) {
				entity.collisionOn = false;
			}	 // if	
		} // entity moving down if
		
		
	} // checktile
} // Collision Checker