package DomumViri.Managers;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public final class AudioManager {

    private static MediaPlayer mediaPlayer;
    private static MediaPlayer mediaPlayerJump;

    private AudioManager(){
    }
    
    /**
     * Speelt een muziek bestand af op de muziekspeler
     * @param volume Het volume waarop je het bestand wilt afspelen.
     * @return Of het afspelen is gelukt of niet.
     */
    public static boolean play(double volume) {
        try {
            File file = new File("DomusMusic.mp3");
            Media hit = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
            mediaPlayer.play();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Speelt het spring geluid
     * @param volume Hoe hard je het geluid wilt hebben.
     */
    public static void playJump(double volume) {
        try {
            File file = new File("Jump.wav");
            Media hit = new Media(file.toURI().toString());
            mediaPlayerJump = new MediaPlayer(hit);
            mediaPlayerJump.setVolume(volume);
            mediaPlayerJump.play();
        } catch (Exception ex) {
            Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Zet het volume van de speler die muziek afspeelt.
     * @param volume 
     */
    public static void setVolume(double volume){
        mediaPlayer.setVolume(volume);
    }
}
