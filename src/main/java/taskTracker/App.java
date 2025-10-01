package taskTracker;

public class App {

    public static void main(String[] args) {

    if (args != null && args.length > 0 && "--gui".equalsIgnoreCase(args[0])) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            TaskTrackerFrame frame = new TaskTrackerFrame();
            frame.setVisible(true);
        });
    }
    else {
        TaskTracker taskTracker = new TaskTracker();
        taskTracker.trackActiveWindow();
    }
    }
}