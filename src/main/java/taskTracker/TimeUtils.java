package taskTracker;

import javax.swing.Timer;
import java.time.Duration;

public class TimeUtils {

    private Duration elapsedTime = Duration.ZERO;
    private Timer timer;

    public void startTracking() {
        timer = new Timer(1000, e -> {
            elapsedTime = elapsedTime.plusSeconds(1);
            // Update UI here
            System.out.println("Elapsed: " + formatDuration(elapsedTime));
        });
        timer.start();
    }

    public void stopTracking() {
        if (timer != null) {
            timer.stop();
        }
    }

    private String formatDuration(Duration d) {
        return String.format("%02d:%02d:%02d",
                d.toHours(),
                d.toMinutesPart(),
                d.toSecondsPart());
    }


}
