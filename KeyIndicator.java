public class KeyIndicator extends Entity{
	private String shape;
	

	// key constructor
	public KeyIndicator(String r, int newX, int newY, String shape) {
		super(r, newX, newY);
		this.shape = shape;
	} // KeyIndicator
	
	// changes the color of shape cut out in wall when key is obtained
	public String changeColorTri(Entity player) {
		
		// changes color of triangle
		if (shape == "triangle") {
			if (player.keyTriGet) {
				player.triangleDone = true;						
			} // if
			if (!player.triangleDone) {
				return "objs/unlitTriangle.png";
			} // if
			else {
				return "objs/litTriangle.png";
			} // else
			
		} // triangle if
		
		// changes color of circle
		else if (shape == "circle") {
			if (player.keyCirGet) {
				player.circleDone = true;						
			} // if
			if (!player.circleDone) {
				return "objs/unlitCircle.png";
			} // if
			else {
				return "objs/litCircle.png";
			} // else
		} // circle else if
		
		// changes color of star
		else if (shape == "star") {
			if (player.keyStarGet) {
				player.starDone = true;						
			} // if
			if (!player.starDone) {
				return "objs/unlitStar.png";
			} // if
			else {
				return "objs/litStar.png";
			} // else
		} // star else if
		
		return " ";
	} // changeColorTri

	@Override
	public void collidedWith(Entity other) {
	} // collidedWith

} // class Key Indicator
