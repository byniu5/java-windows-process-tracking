package taskTracker;

import taskTracker.Windows.TaskTrackerFrame;

/**
 * Entry point for the application.
 */
public class App {

    public static void main(String[] args) {

    if (args != null && args.length > 0 && "--gui".equalsIgnoreCase(args[0])) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            TaskTrackerFrame frame = new TaskTrackerFrame();
            frame.setVisible(true);
        });
    }
    // Possibility to run as a console application.
//     else {
//         TaskTracker taskTracker = new TaskTracker();
//         taskTracker.trackActiveWindow();
//     }

    }
}