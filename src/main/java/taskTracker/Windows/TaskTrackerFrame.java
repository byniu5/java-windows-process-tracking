// TODO Consider moving Timer into TaskTracker                                              Not necessary
//      Because I think it's not a good idea to have it in the frame
//      Code will be more complicated and not so clean but it I'm not sure
//      if it's good practise to have it in the frame
// TODO Previously tracked apps should be saved with the counted time
// TODO If we return to the app that we counted before, we should continue counting
// TODO counters storage
// TODO Table with tracked apps
// TODO Current time from system support
// TODO Calculation based on current time
// TODO Set the default icon for apps that don't have icon

package taskTracker.Windows;

import taskTracker.TaskTracker;
import javax.swing.*;
import java.awt.*;
import static taskTracker.IconUtils.resizeIcon;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * The TaskTrackerFrame class provides a graphical user interface (GUI) for tracking the active window on a system.
 * It displays details such as the active window title, an icon representing the window, and the time duration the window has been active.
 * The class extends the JFrame class and periodically updates the displayed information.
 */
public class TaskTrackerFrame extends JFrame {

    private final TaskTracker TaskTracker = new TaskTracker();

    // Constants

    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_HEIGHT = 250;
    private static final int ICON_WIDTH = 32;
    private static final int ICON_HEIGHT = 32;

    // Variables

    public int timeCounter = 0;
    public int processIdActual;
    public int processIdPrevious;

    // Components

    private final JLabel iconLabel = new JLabel();
    private final JTextField textField = new JTextField();
    private final JLabel timeLabel = new JLabel();
    private final JPanel topPanel = new JPanel();
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    /**
     * The TaskTrackerFrame class is a GUI-based component for tracking and displaying
     * information about active system windows, including the associated process, time
     * tracking, and UI updates. This class extends JFrame and provides a structured
     * user interface to interact with the window tracking functionality.
     */
    public TaskTrackerFrame() {
        super("Task Tracker");

        initializeUI();

        Timer timer = new Timer(3000, e -> {
            TaskTracker.trackActiveWindow();
            updateLabel();
            timeCounter+=3;

            if (processIdActual != TaskTracker.getProcessId()) {
                processIdPrevious = processIdActual;
                processIdActual = TaskTracker.getProcessId();
                System.out.println("Process ID changed from " + processIdPrevious + " to " + processIdActual);
               // TODO (processIdPrevious == TaskTracker.getProcessIdStr()) { don't put into table active process, only those finished
                updateTable();
                // TODO }
            }
        });
        timer.start();

        // Initialization of the actual process ID. Run only once.
        TaskTracker.trackActiveWindow();
        if (processIdActual != TaskTracker.getProcessId()) {
            processIdActual = TaskTracker.getProcessId();
            processIdPrevious = processIdActual;
            System.out.println("Process ID changed");
        }

    }
//adseffdgs
    /**
     * Initializes the user interface (UI) components for the TaskTrackerFrame class.
     * This method creates a layout and adds components to the frame.
     */
    private void initializeUI() {

        //Layout

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(panel);

        topPanel.setLayout(new BorderLayout(8, 8));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(timeLabel, BorderLayout.SOUTH);

        textField.setText(TaskTracker.activeWindowTitle);
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(textField, BorderLayout.CENTER);

        // Table layout

        tableModel.addColumn("Icon");
        tableModel.addColumn("Title");
        tableModel.addColumn("Time");
        table.setRowHeight(ICON_HEIGHT);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMaxWidth(ICON_WIDTH);
        table.getColumnModel().getColumn(1).setMaxWidth(WINDOW_WIDTH - ICON_WIDTH*2);
        table.getColumnModel().getColumn(2).setMaxWidth(ICON_WIDTH);
        table.setShowGrid(false);

        /**
         * Custom table cell renderer for displaying icons in the first column.
         * Renders ImageIcon objects as JLabel components with the icon set.
         */        
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                }
                return label;
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * Updates the main label in the top panel.
     * This method updates the icon and title labels with the current active window information.
     * It also updates the time label with the current time duration.
     */
    private void updateLabel() {
        ImageIcon icon = TaskTracker.getCurrentIcon();
        if (icon != null) {
            ImageIcon resizedIcon = resizeIcon(icon, ICON_WIDTH, ICON_HEIGHT);
            iconLabel.setIcon(resizedIcon);
        } else {
            iconLabel.setIcon(null);
        }

        textField.setText(TaskTracker.activeWindowTitle);
        timeLabel.setText("Time: " + timeCounter);
    }

    /**
     * Updates the table with the previous active window information.
     * This method inserts a new row into the table with the current window information.
     * It also updates the icon, title and time labels with the current active window information.
     */
    private void updateTable() {

            ImageIcon icon = TaskTracker.getCurrentIcon();
            ImageIcon resizedIcon = null;
            if (icon != null) {
                resizedIcon = resizeIcon(icon, ICON_WIDTH, ICON_HEIGHT);
                iconLabel.setIcon(resizedIcon);
            } else {
                iconLabel.setIcon(null);
            }

            tableModel.insertRow(0, new Object[]{
                    resizedIcon,
                    TaskTracker.activeWindowTitle,
                    timeCounter
            });
    }
}
