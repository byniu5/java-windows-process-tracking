// TODO Consider moving Timer into TaskTracker
//      Because I think it's not a good idea to have it in the frame
//      Code will be more complicated and not so clean but it I'm not sure
//      if it's good practise to have it in the frame
// TODO Previously tracked apps should be saved with the counted time
// TODO If we return to the app that we counted before, we should continue counting
// TODO counters storage
// TODO Table with tracked apps

package taskTracker;

import javax.swing.*;
import java.awt.*;

public class TaskTrackerFrame extends JFrame {

    private final TaskTracker TaskTracker = new TaskTracker();

    public int timeCounter = 0;

    private final JLabel label = new JLabel();
    private final JLabel label2 = new JLabel();


    public TaskTrackerFrame() {
        super("Task Tracker");

        initializeUI();

        Timer timer = new Timer(1000, e -> {
            TaskTracker.trackActiveWindow();
            updateLabel();
            timeCounter++;
        });
        timer.start();
//            TaskTracker.refresher();
//            updateLabel();

    }

    private void initializeUI() {

        //Layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(panel);

        label.setText(TaskTracker.activeWindowTitle);
        panel.add(label, BorderLayout.CENTER);
        panel.add(label2, BorderLayout.SOUTH);

    }

    private void updateLabel() {
        label.setText(TaskTracker.activeWindowTitle);
        label2.setText("Time: " + timeCounter);
    }

}
