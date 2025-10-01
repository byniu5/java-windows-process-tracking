package taskTracker;

import javax.swing.*;
import java.awt.*;

public class TaskTrackerFrame extends JFrame {

    private final TaskTracker TaskTracker = new TaskTracker();
    private final JLabel label = new JLabel();

    public TaskTrackerFrame() {
        super("Task Tracker");

        TaskTracker.trackActiveWindow();
        initializeUI();

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

    }

}
