
import javazoom.jl.player.Player

/**
 * Created by , 24.05.2015.
 */

public class Notifier implements Runnable {

    private Thread t;
    private String url;
    private FileInputStream fis;

    public Notifier(String url) {
        t = new Thread(this);
        this.url = url;
        fis= null;
        t.start();
    }

    public void run() {
        synchronized (this) {
            try {
                try {
                    fis = new FileInputStream("${url}/notification.mp3");
                    Player playMP3 = new Player(fis);

                    playMP3.play();
                }  finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException ignored) {

                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public Thread getThread() {
        return t;
    }
}
