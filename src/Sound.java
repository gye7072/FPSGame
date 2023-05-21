import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[6];

    public Sound(){
        soundURL[0] = getClass().getResource("/sound/shoot.wav");
        soundURL[1] = getClass().getResource("/sound/invaderkilled.wav");
        soundURL[2] = getClass().getResource("/sound/playerkilled.wav");
        soundURL[3] = getClass().getResource("/sound/lifeUp.wav");
        soundURL[4] = getClass().getResource("/sound/waveCompleted.wav");
        soundURL[5] = getClass().getResource("/sound/gameOver.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream a = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(a);
        } catch (Exception e){

        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}
