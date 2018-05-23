package pertemuan9;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author naufalprakoso
 */
public class Pertemuan9 extends JFrame{

    AudioFormat audioFormat;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    boolean stopPlayback = false;
    
    final JButton stopBtn = new JButton("Stop");
    final JButton playBtn = new JButton("Play");
    final JTextField textField = new JTextField("");

    public static void main(String args[]){
        new Pertemuan9();
    }

    public Pertemuan9(){
        stopBtn.setEnabled(false);
        playBtn.setEnabled(true);

        playBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                stopBtn.setEnabled(true);
                playBtn.setEnabled(false);
                playAudio();
            }
        });
        
        stopBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
              stopPlayback = true;
            }
        });

        getContentPane().add(playBtn,"West");
        getContentPane().add(stopBtn,"East");
        getContentPane().add(textField,"North");

        setTitle("Music Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(250,70);
        setVisible(true);
    }
    
    private void playAudio() {
        try{
            File soundFile = new File(textField.getText());
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audioFormat = audioInputStream.getFormat();
            //System.out.println(audioFormat);

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
            
            new PlayThread().start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    class PlayThread extends Thread{
        byte tempBuffer[] = new byte[10000];

        public void run(){
            try{
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();

                int cnt;
                while((cnt = audioInputStream.read(tempBuffer,0,tempBuffer.length)) != -1
                             && stopPlayback == false){
                    if(cnt > 0){
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }
                sourceDataLine.drain();
                sourceDataLine.close();

                stopBtn.setEnabled(false);
                playBtn.setEnabled(true);
                stopPlayback = false;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}