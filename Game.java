/* Game.java
 * Plague Main Program
 * 
 * Program Name: PLAGUE
 * Authors: Mamon, Clarise, Karan
 * Based on Ms. Wear's Space Invaders Game
 * Date: 14/11/24
 * Description: Horror game where player must find keys to escape, avoiding monsters.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.util.ArrayList;

public class Game extends Canvas {
	
		// take advantage of accelerated graphics
      	private BufferStrategy strategy;   // take advantage of accelerated graphics
        private boolean waitingForKeyPress = true;  
        
        // see if a specific key is pressed
        private boolean leftPressed = false;  
        private boolean rightPressed = false; 
        private boolean upPressed = false;
        private boolean downPressed = false;
        private boolean xPressed = false;
        private boolean pressed0 = false;
        private boolean pressed1 = false;
        private boolean pressed2 = false;
        private boolean pressed3 = false;
        private boolean pressed4 = false;
        private boolean pressed5 = false;
        private boolean pressed6 = false;
        private boolean pressed7 = false;
        private boolean pressed8 = false;
        private boolean pressed9 = false;
        private boolean pressedNum = false;

        // booleans
        private boolean drawJumpscare = false;
        private boolean menuPressed = false;
        private boolean won = false;
        private boolean gameRunning = true;
        private boolean gotTriKey = false;
        private boolean startCount = false;
        private boolean startCountType = false;
        public boolean gamePage = true;
        private boolean showNotis = false;
        
        // what sprite player should be
        private int spriteCount = 0;

        // ArrayLists 
        private ArrayList entitiesMap1 = new ArrayList(); // list of entities in map1
        private ArrayList entitiesMap2 = new ArrayList(); // list of entities in map2
        private ArrayList currentEntityMap = new ArrayList(); // which one
        private ArrayList removeEntities = new ArrayList(); // list of entities
                                                            // to remove this loop
        private Entity player;  // the ship
        private Entity monster;
        private Entity ghost;
        private Entity ghost2;
        private Entity cosmos;
        private Entity objectGet;
        private Entity triangleIndicator;
        private Entity circleIndicator;
        private Entity starIndicator;
        private Entity letter;
        private Entity safebox;
        private Entity hammer;
        private double moveSpeed = 200; // hor. vel. of ship (px/s)
        
        // counts/durations
        private int speedDuration = 500;
        private int stunDuration = 0;
        private int countBox = 0;
        private int countType = 0;
        private int notisCounter = 0;
        
        // for key code lock puzzle
        private String num1 = "";
        private String num2 = "";
        private String num3 = "";
        private String num4 = "";
        private int numCount = 1;
        		
        // music variable declarations
        String filepath = "sounds/Soundtrack.wav";
        Music musicObject = new Music();
        
        //SCREEN SETTINGS
        final int WIDTH = 1200;
        final int HEIGHT = 800;
        
        final int originalTileSize = 16; //16x16 tile (default size of character/npc)
		final int scale = 3; //16x3 is 48 make it look larger in higher resolution
        public final int tileSize = originalTileSize * scale; //48x48 tile
        
        //change to screenWidth and screenHeight later
        public final int screenX = WIDTH/2;
        public final int screenY = HEIGHT/2;
        
        // world settings
      	public final int maxWorldCol = 70;
      	public final int maxWorldRow = 50;
      	public final int worldWidth = tileSize * maxWorldCol;
      	public final int worldHeight = tileSize * maxWorldRow;
      	public final int maxMap = 5;
      	public int currentMap = 0;
      	public int cameraX = 0;
      	public int cameraY = 0;    	
      	public PathFinder pFinder = new PathFinder(this);
      	
      	// position relative to player
      	int playerX;
      	int playerY;
      	
      	// scene images
      	BufferedImage jumpscarePic;
      	BufferedImage winPic;
      	BufferedImage lighting;
      	BufferedImage titlemenu;
      	BufferedImage letterPic;
      	BufferedImage lockedPic;
      	BufferedImage unlockedPic;
      	BufferedImage poolPic;
      	
      	// changed for my sanity - change back later
      	String jumpscarePath = "scenes/jumpscare.png";
      	String winImgPath = "scenes/escaped.png";
      	String lightingPath = "scenes/light.png";
      	String titlemenuPath = "scenes/titlescreen.png";
      	String letterImagePath = "scenes/lettercontents.png";
      	String lockedImagePath = "scenes/locked.PNG";
      	String unlockedImagePath = "scenes/unlocked.PNG";
      	String poolPath = "scenes/poolarrow.PNG";
      	
      	// instantiate other thing
      	CollisionChecker cChecker;
      	String spriteType = "player/down1.png";
      	String keyCodeMessage = "Enter Key Code";
      	
      	// system
      	TileManager tileM = new TileManager(this);

    	/*
    	 * Construct our game and set it running.
    	 */
    	public Game() {
    		// create a frame to contain game
    		JFrame container = new JFrame("PLAGUE");
    
    		// get hold the content of the frame
    		JPanel panel = (JPanel) container.getContentPane();
    
    		// set up the resolution of the game
    		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    		panel.setLayout(null);
    
    		// set up canvas size (this) and add to frame
    		setBounds(0, 0, WIDTH, HEIGHT);
    		panel.add(this);
    
    		// Tell AWT not to bother repainting canvas since that will
            // be done using graphics acceleration
    		setIgnoreRepaint(true);
    		
    		try {
          		jumpscarePic = ImageIO.read(new File(jumpscarePath));
          		winPic  = ImageIO.read(new File(winImgPath));
          		lighting  = ImageIO.read(new File(lightingPath));
          		titlemenu = ImageIO.read(new File(titlemenuPath));
          		letterPic = ImageIO.read(new File(letterImagePath));
          		lockedPic = ImageIO.read(new File(lockedImagePath));
          		unlockedPic = ImageIO.read(new File(unlockedImagePath)); 
          		poolPic = ImageIO.read(new File(poolPath)); 

          	}catch(Exception e) {
          		System.out.println("image not here");
          	} // catch
    	    
    		// make the window visible
    		container.pack();
    		container.setResizable(false);
    		container.setVisible(true);

            // if user closes window, shutdown game and jre
    		container.addWindowListener(new WindowAdapter() {
    			public void windowClosing(WindowEvent e) {
    				System.exit(0);
    			} // windowClosing
    		});
    
    		// add key listener to this canvas
    		addKeyListener(new KeyInputHandler());
    
    		// request focus so key events are handled by this canvas
    		requestFocus();

    		// create buffer strategy to take advantage of accelerated graphics
    		createBufferStrategy(2);
    		strategy = getBufferStrategy();
    
    		// initialize entities
    		initEntities();
    
    		// start the game
    		gameLoop();
        } // constructor
    
    	// Initialize the starting state of entities.
    	private void initEntities() {
    		
    		// main map entities
    		monster = new Monster(this, "sprites/creepytest.png", 2000, 1050, player, false, 265);
    		entitiesMap1.add(monster);
    		
    		ghost = new Monster(this, "sprites/ghost.png", 160, 650, player, true, 160);
    		entitiesMap1.add(ghost);
    		
    		ghost2 = new Monster(this, "sprites/ghost.png", 2924, 623, player, true, 150);
    		entitiesMap1.add(ghost2);
    		
    		objectGet = new Objects(this, "objs/cirKey.png", 180, 760, "circle");
            entitiesMap1.add(objectGet);
    
            objectGet = new Objects(this, "objs/speedBoost.png", 1801, 240, "speedBoost");
            entitiesMap1.add(objectGet);
            
            objectGet = new Objects(this, "objs/speedBoost.png", 1669, 1488, "speedBoost");
            entitiesMap1.add(objectGet);
            
            objectGet = new Objects(this, "objs/speedBoost.png", 233, 1443, "speedBoost");
            entitiesMap1.add(objectGet);
            
            objectGet = new Objects(this, "objs/starKey.png", 2330, 1450, "star");
            entitiesMap1.add(objectGet);
            
            hammer = new Objects(this, "objs/hammer.png", 215, 620, "hammer");           
            entitiesMap1.add(hammer);
            
            hammer = new Objects(this, "objs/hammer.png", 1158, 226, "hammer");           
            entitiesMap1.add(hammer);
            
            hammer = new Objects(this, "objs/hammer.png", 2615, 1978, "hammer");           
            entitiesMap1.add(hammer);
            
            hammer = new Objects(this, "objs/hammer.png", 942, 609, "hammer");           
            entitiesMap1.add(hammer);
            
            hammer = new Objects(this, "objs/hammer.png", 1116, 1680, "hammer");           
            entitiesMap1.add(hammer);
            
            letter = new Objects(this, "objs/letter.png", 670, 2040, "letter");
            entitiesMap1.add(letter);
            
            triangleIndicator = new KeyIndicator("objs/unlitTriangle.png", 1420, 200, "triangle");
            entitiesMap1.add(triangleIndicator);
            
            circleIndicator = new KeyIndicator("objs/unlitCircle.png", 1490, 200, "circle");
            entitiesMap1.add(circleIndicator);
            
            starIndicator = new KeyIndicator("objs/unlitStar.png", 1560, 200, "star");
            entitiesMap1.add(starIndicator);
            
            // swimming pool map entities     
            hammer = new Objects(this, "objs/hammer.png", 1263, 960, "hammer");
            entitiesMap2.add(hammer);
            
            hammer = new Objects(this, "objs/hammer.png", 3247, 258, "hammer");
            entitiesMap2.add(hammer);
            
            hammer = new Objects(this, "objs/hammer.png", 1675, 653, "hammer");
            entitiesMap2.add(hammer);
            
            hammer = new Objects(this, "objs/hammer.png", 3236, 1136, "hammer");
            entitiesMap2.add(hammer);
            
            objectGet = new Objects(this, "objs/speedBoost.png", 941, 299, "speedBoost");
            entitiesMap2.add(objectGet);
            
            objectGet = new Objects(this, "objs/speedBoost.png", 1047, 1544, "speedBoost");
            entitiesMap2.add(objectGet);
            
            safebox = new Objects(this, "objs/safelock.PNG", 1074, 590, "keycode");
            entitiesMap2.add(safebox);
            
    		cosmos = new Monster(this, "sprites/cosmos.png", 1490, 240, player, false, 300);
    		entitiesMap2.add(cosmos);
            
            // player entity
            player = new PlayerEntity(this, "player/down1.png", 650, 1800);
            entitiesMap1.add(player);
            entitiesMap2.add(player);
            
            // music entity
            musicObject.playMusic(filepath);         
            
            // collision checker
            cChecker = new CollisionChecker(this);
    	} // initEntities

        // Remove an entity from the game
    	// called in Objects.java
        public void removeEntity(Entity entity) {
          removeEntities.add(entity);
        } // removeEntity
        
        // indicate that player has lost
        public void notifyDeath() {
          waitingForKeyPress = false;
          drawJumpscare = true;        
        } // notifyDeath

        // indicate that player has won
        public void notifyWin(){
          waitingForKeyPress = false;
          won = true;
        } // notifyWin
        
        public void showNotis(){
            showNotis = true;
            notisCounter = 0;
        } // showNotis
        
        // for key code, see which number player typed
        public String numEntered (String numPosition) {
        	String s = numPosition; 
        	if (pressed0) {
        		s = "0";
    		} // if
        	else if (pressed1) {
    			s = "1";
    		} // else if
        	else if (pressed2) {
    			s = "2";
    		} // else if
        	else if (pressed3) {
    			s = "3";
    		} // else if
        	else if (pressed4) {
    			s = "4";
    		} // else if
        	else if (pressed5) {
    			s = "5";
    		} // else if
        	else if (pressed6) {
    			s = "6";
    		} // else if
        	else if (pressed7) {
    			s = "7";
    		} // else if
        	else if (pressed8) {
    			s = "8";
    		} // else if
        	else if (pressed9) {
    			s = "9";
    		} // else if
	 
        	return s;
         } // numEntered
         
        // collision detection
        public void checkCollisions(ArrayList entityMapNum) {
        	for (int i = 0; i < entityMapNum.size(); i++) {
                for (int j = i + 1; j < entityMapNum.size(); j++) {
                   Entity me = (Entity)entityMapNum.get(i);
                   Entity him = (Entity)entityMapNum.get(j);
                   
                   if (me.collidesWith(him)) {  	   
                     me.collidedWith(him);
                     him.collidedWith(me);
                   } // if
                } // inner for
              } // outer for
        } // checkCollisions

    // main game loop that runs throughout game play
	public void gameLoop() {
          long lastLoopTime = System.currentTimeMillis();

          // keep loop running until game ends
          while (gameRunning) {
        	// calc. time since last update, will be used to calculate
              // entities    movement
              long delta = System.currentTimeMillis() - lastLoopTime;
              
              lastLoopTime = System.currentTimeMillis();

              // get graphics context for the accelerated surface and make it black
              Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
              
              // while the game is being played
        	  if (gamePage == true) {
                  
        		  // load the correct tile map
        		  if (currentMap == 0) {
        			  currentEntityMap = entitiesMap1;
        		  } // if
        		  else if (currentMap == 1) {
        			  currentEntityMap = entitiesMap2;
        		  } // else if
        		  else {
        			  currentEntityMap = entitiesMap1;
        		  } // else
        		  
                  // track player's row and column
                  player.trackRowCol();
                  
                  // player position relative to screen
                  playerX = (int) player.getX() - screenX + (tileSize / 2);
                  playerY = (int) player.getY() - screenY + (tileSize / 2);
                  
                  // Calculate camera position based on player's position
                  cameraX = playerX;
                  cameraY = playerY;
                  
                  // player is not touching door all the time
                  player.touchingGoal = true;
                  
                  // Translate graphics context for camera
                  g.translate(-cameraX, -cameraY);
                  tileM.draw(g);

                  // move each entity
                  if (!waitingForKeyPress) {
                    for (int i = 0; i < currentEntityMap.size(); i++) {
                      Entity entity = (Entity) currentEntityMap.get(i);
                      entity.move(delta);
                    } // for
                  } // if           
                  
                  for (int i = 0; i < currentEntityMap.size(); i++) {
                     Entity entity = (Entity) currentEntityMap.get(i);                     
                     entity.draw(g);
                  } // for
                 
                 // check each entity's collision with other
                 checkCollisions(currentEntityMap);            

                 // remove dead entities
                 currentEntityMap.removeAll(removeEntities);
                 removeEntities.clear();
                 player.collisionOn = true;
                 
                 // draws pool hint on first map
                 if (currentMap == 0) {
                	 g.drawImage(poolPic, 2300, 390, null);
                 } // if
                 
                 // draws lighting effect
                 g.drawImage(lighting, playerX, playerY, null);               
                 
                 // shows jumpscare if player has died
                 if(drawJumpscare == true) {
                 		g.drawImage(jumpscarePic, playerX, playerY, null);
                 } // if
                 
                 // sends player back to game menu after death
                 if (drawJumpscare && menuPressed) {
                	 waitingForKeyPress = true;
                 } // if
                 
                 if(notisCounter < 60 * 4 && showNotis){
                	 g.drawString("You collected a key!", playerX + (WIDTH/2 - 30), playerY + 292);
                	} else if (notisCounter >= 60 * 4 && showNotis) {
                	 showNotis = false;
                	} // else if
                 notisCounter++;
                
                 // shows win screen if player has won
                 if(won == true) {
              	   g.drawImage(winPic, playerX, playerY, null);
                 } // if
                 
                 // sends player back to game menu after win
                 if (won && menuPressed) {
                	 waitingForKeyPress = true;
                 } // if
                 
                 // the letter at the start of game
                 if (letter.getDrawMessage() == true) {
                	 g.drawImage(letterPic, playerX, playerY, null);
                	 monster.setStunBool(true);
                	 monster.setStunTouch(true);
                	 ghost.setStunBool(true);
                	 ghost.setStunTouch(true);
                	 ghost2.setStunBool(true);
                	 ghost2.setStunTouch(true);
                	 
                 } // if
                 else if (letter.getDrawMessage() == false && player.hasHammer == false) {
                	 monster.setStunBool(false);
                	 monster.setStunTouch(false);
                	 ghost.setStunBool(false);
                	 ghost.setStunTouch(false);
                	 ghost2.setStunBool(false);
                	 ghost2.setStunTouch(false);
                 } // else if

                 letter.setDrawMessage(false);

                 // key code box in swimming pool
                 if (startCount == true) {
                	 countBox++;
                	 safebox.setEnterCode(false);
                 } // if
                 if (countBox >= 30) {
                	 startCount = false;
                	 countBox = 0; 
                 } // if
                 
                 // detects number input
                 if (safebox.getEnterCode() == true && startCount == false) {
                	 if (gotTriKey == false) {
                		 g.drawImage(lockedPic, playerX, playerY, null);
                		 
                		 if (startCountType == true) {
            				 countType++;
                		 } // if
                		 if (countType > 10) {
        					 countType = 0;
        					 startCountType = false;
        				 } // if
                		 
                		 if (numCount == 1 && countType == 0) {
                			 num1 = numEntered(num1);
                			 if (pressedNum == true) {
                				 numCount++;
                				 startCountType = true;
                			 } // inner if	 
                		 } // outer if  		 
                		 else if (numCount == 2 && countType == 0) {
                			 num2 = numEntered(num2);
                			 if (pressedNum == true) {
                				 numCount++;
                				 startCountType = true;
                				 
                			 } // inner if	
                		 } // else if	
                		 else if (numCount == 3  && countType <= 0) {
                			 num3 = numEntered(num3);
                			 if (pressedNum == true) {
                				 numCount++;
                				 startCountType = true;
                			 } // inner if	
                		 } // else if	
                		 else if (numCount == 4 && countType <= 0) {
                			 num4 = numEntered(num4);
                			 if (pressedNum == true) {
                				 numCount++;
                				 startCountType = true;
                			 } // inner if	
                		 } // else if	
                		 else if (numCount == 5 && countType <= 0) {
                				 if(num1.equals("0") && num2.equals("6") && num3.equals("3") && num4.equals("4")) {
                					 gotTriKey = true;
                					 player.triangleDone = true;
                				 } // if	
                				 else {
                					 num1 = "";
                					 num2 = "";
                					 num3 = "";
                					 num4 = "";
                					 numCount = 1;
                					 startCountType = true;
                				 } // else	
                			 
                		 } // else if	
                		 
                	 } // got TriKey if
                	 else {
                		 g.drawImage(unlockedPic, playerX, playerY, null);
                	 } // else
                	 g.setColor(Color.white);
                     g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
                	 g.drawString(num1 + "   " + num2 +  "   " + num3 +  "   " + num4, playerX + 370, playerY + 292);

                	 // enters code after x pressed
                	 if (xPressed == true) {
                		 safebox.setEnterCode(false);
                		 countBox = 0;
                		 player.setPosition(1074, 695);
                		 startCount = true;
                	 } // if
                	 
                	 // changes game background as player uses key code box
                	 g.setColor(Color.black);
                     g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
                	 g.drawString("[X] to exit", playerX, playerY + 50);
                	 cosmos.setStunBool(true);
                	 cosmos.setStunTouch(true);
                 } // safe box code if container
                 
                 else if (safebox.getEnterCode() == false && player.hasHammer == false) {
                	 cosmos.setStunBool(false);
                	 cosmos.setStunTouch(false);
                 } // else if
                 
                 // if waiting for "any key press", draw menu screen
                 if (waitingForKeyPress) {
                   g.drawImage(titlemenu, playerX, playerY, null);
                 }  // if

                 // clear graphics and flip buffer
                 g.dispose();
                 strategy.show();

                 // player should not move without user input
                 player.setHorizontalMovement(0);
                 player.setVerticalMovement(0);
                  
                 // change move speed
                 if (player.getSpeedBool() == true) {
                	 moveSpeed = 500;
                	 speedDuration--;
                	 if (speedDuration < 0) {
                		 player.setSpeedBool(false);
                	 } // inner if
                 } // outer if
                 else if (player.getSpeedBool() == false) {
              		 speedDuration = 500;           		
              		 moveSpeed = 300; 
                 } // else if
                               
                 // stun monsters
                 if (monster.getStunBool() == false) {
                	 monster.setStunTouch(false);
                 } // if
                  
                 if (ghost.getStunBool() == false) {
                	 ghost.setStunTouch(false);
                 } // if
                  
                 if (ghost2.getStunBool() == false) {
                	 ghost2.setStunTouch(false);
                 } // if
                  
                 if (cosmos.getStunBool() == false) {
                	 cosmos.setStunTouch(false);
                 } // if
                  
                 // if hammer grabbed, monster can be stunned             
                 if (player.hasHammer == true) {
                	 	monster.setStunBool(true);
                    	ghost.setStunBool(true);
                    	ghost2.setStunBool(true);
                    	cosmos.setStunBool(true);
                    	stunDuration++;
                    	
                    	// reactivate monsters after stun period
                    	if (stunDuration > 200) {
                    		player.hasHammer = false;
                    		monster.setStunBool(false);
                    		ghost.setStunBool(false);
                    		ghost2.setStunBool(false);
                    		cosmos.setSpeedBool(false);
                    		stunDuration = 0;
                    	} // inner if
                  } // outer if
                                  
                  // pathfinding
                  cChecker.checkTile(player);
                  monster.setAction(this, player);
                  ghost.setAction(this, player);
                  ghost2.setAction(this, player);
                  cosmos.setAction(this, player);
                  
                  // reset player movement
                  player.movingDown = false;
                  player.movingUp = false;
                  player.movingLeft = false;
                  player.movingRight = false;
                  
                  // respond to user moving player, preventing player from moving diagonally             
                  if (leftPressed || upPressed || downPressed || rightPressed) {
                      if ((leftPressed) && (!upPressed) && (!downPressed)) {
                      	player.setHorizontalMovement(-moveSpeed);
                      	player.movingLeft = true;
                      } //if
                      else if ((rightPressed) && (!upPressed) && (!downPressed)) {
                      	player.setHorizontalMovement(moveSpeed);
                      	player.movingRight = true;
                      } // else if
                      
                      if ((upPressed) && (!leftPressed) && (!rightPressed)) {
                      	player.setVerticalMovement(-moveSpeed);
                      	player.movingUp = true;
                      } // if
                      else if ((downPressed) && (!leftPressed) && (!rightPressed)) {
                      	player.setVerticalMovement(moveSpeed);
                      	player.movingDown = true;              	
                      } // else if
                      
                      spriteCount++;
                      if (spriteCount > 10) {
                      	if (player.spriteNum == 1) {
                      		player.spriteNum = 2;
                      	} // if
                      	else if (player.spriteNum == 2) {
                      		player.spriteNum = 1;
                      	} // else if
                      	spriteCount = 0;
                      } // if
                                          
                  }// player movement if statement
                
                  // set the keys and player sprites
                  spriteType = player.getPlayerImage();
                  player.setSprite(spriteType);                
                  triangleIndicator.setSprite(triangleIndicator.changeColorTri(player));
                  circleIndicator.setSprite(circleIndicator.changeColorTri(player));
                  starIndicator.setSprite(starIndicator.changeColorTri(player));   
                  
                  // change the safe sprite if decoded
                  if (gotTriKey == true) {
                	  safebox.setSprite("objs/safegone.PNG");   
                  } // if
                  
        	  }
        	  
            // smoothness here
            try { Thread.sleep(10); } catch (Exception e) {}

          } // while game loop

	} // gameLoop

    // start a fresh game, clear old data
	private void startGame() {
		// clear out any existing entities and initalize a new set
		currentEntityMap.clear();
		entitiesMap1.clear();
		entitiesMap2.clear();
            
		initEntities();
            
		// blank out any keyboard settings that might exist
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        menuPressed = false;
        currentMap = 0;
    } // startGame

	// class handles keyboard input from user
	private class KeyInputHandler extends KeyAdapter {          
		
        private int pressCount = 1;  // # of key presses since waiting for 'any' key press
        
        // method detects key presses
		public void keyPressed(KeyEvent e) {

			// if waiting for key press to start game, do nothing
			if (waitingForKeyPress) {
				return;
			} // if
                  
			// respond to move left, right, up, down, m (menu), x (exit safe) and numbers, 
			if (gamePage) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                 	  leftPressed = true;
				} // if

				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightPressed = true;
				} // if
                        
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					upPressed = true;
				} // if
                        
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    downPressed = true;
				} // if
                        
				if (e.getKeyCode() == KeyEvent.VK_M) {
					menuPressed = true;
				} // if    
				
				if (e.getKeyCode() == KeyEvent.VK_X) {
					xPressed = true;
				} // if   
				
				if (e.getKeyCode() == KeyEvent.VK_0) {
					pressedNum = true;
					pressed0 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_1) {
					pressedNum = true;
					pressed1 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_2) {
					pressedNum = true;
					pressed2 = true;
				} // if

				if (e.getKeyCode() == KeyEvent.VK_3) {
					pressedNum = true;
					pressed3 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_4) {
					pressedNum = true;
					pressed4 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_5) {
					pressedNum = true;
					pressed5 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_6) {
					pressedNum = true;
					pressed6 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_7) {
					pressedNum = true;
					pressed7 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_8) {
					pressedNum = true;
					pressed8 = true;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_9) {
					pressedNum = true;
					pressed9 = true;
				} // if
			
			} // outer if

		} // keyPressed

		public void keyReleased(KeyEvent e) {
                 // if waiting for key press to start game, do nothing
                  if (waitingForKeyPress) {
                    return;
                  } // if
                  
                  // set key press booleans to false
                  if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = false;
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = false;
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_UP) {
                      upPressed = false;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                      downPressed = false;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_M) {
                	  menuPressed = false;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_X) {
                	  xPressed = false;
                  } // if
                  if (e.getKeyCode() == KeyEvent.VK_0) {
  					pressed0 = false;
  					pressedNum = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_1) {
  					pressedNum = false;
  					pressed1 = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_2) {
  					pressed2 = false;
  					pressedNum = false;
  				} // if

  				if (e.getKeyCode() == KeyEvent.VK_3) {
  					pressed3 = false;
  					pressedNum = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_4) {
  					pressed4 = false;
  					pressedNum = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_5) {
  					pressed5 = false;
  					pressedNum = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_6) {
  					pressed6 = false;
  					pressedNum = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_7) {
  					pressed7 = false;
  					pressedNum = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_8) {
  					pressed8 = false;
  					pressedNum = false;
  				} // if
  				
  				if (e.getKeyCode() == KeyEvent.VK_9) {
  					pressed9 = false;
  					pressedNum = false;
  				} // if

		} // keyReleased

 	        public void keyTyped(KeyEvent e) {

               // if waiting for key press to start game
 	           if (waitingForKeyPress) {
 	        	   if(gamePage) {
 	                    if (pressCount == 1) {                    	 
 	                         waitingForKeyPress = false;
 	                         startGame();
 	                         won = false;
 	                         drawJumpscare = false;
 	                         pressCount = 0;    
 	                     } else {
 	                       pressCount++;
 	                     } // else
 	        	   } // if

                   } // if waitingForKeyPress

                   // if escape is pressed, end game
                   if (e.getKeyChar() == 27) {
                     System.exit(0);
                   } // if escape pressed
		} // keyTyped

	} // class KeyInputHandler

	// main program
	public static void main(String [] args) {
        // instantiate this object
		new Game();
	} // main
} // Game