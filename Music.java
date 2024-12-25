import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

public class Music {
	
	// plays background track
	void playMusic(String musicLocation) {
		
		try {
			File musicPath = new File(musicLocation);
			
			// runs if .wav is located
			if(musicPath.exists()) {
				// initializes music
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				
				// plays music
				clip.open(audioInput);
				clip.start();
				
				// loops music forever
				clip.loop(clip.LOOP_CONTINUOUSLY);
				
			} //if
			else {
				System.out.println("cannot find file");
			} // else
			
		} // try
		catch(Exception ex) {
			ex.printStackTrace();
		} // catch
		
		
	} // playMusic

} // Music class