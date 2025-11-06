// TODO Consider moving Timer into TaskTracker                                              Not needed
//      Code will be more complicated and not so clean but it I'm not sure
//      if it's good practise to have it in the frame
// TODO Previously tracked apps should be saved with the counted time                           OK
// TODO If we return to the app that we counted before, we should continue counting             OK
// TODO counters storage                                                                        OK
// TODO Table with tracked apps                                                                 OK
// TODO Current time from system support                                                    Not needed
// TODO Calculation based on current time                                                   Not needed
// TODO Set the default icon for apps that doesn't have icon
// TODO BIG  For now I collect only counted time in array
//      I should store all values of the table row, to save them into database or file
//      and reuse later
// TODO Dark theme                                                                              OK
// TODO Apps should be sorted by counted time. From biggest to smallest.                        OK
// TODO Apps in table and textField should be unavailable for editing.                          OK
// TODO At first iteration, the first tracked app should be added to the table automatically    OK
// TODO Understand ThemeUtils, TimeUtils, AppData class
// TODO Consider better structure of the files
// TODO BIG web pages are counted differently that is the problem                               OK
// TODO When I close the system, the app is not saved, I need to manually close it,
//      then data is saved.
// TODO DEAMON Startup
// TODO App should always be visible on top
// TODO delete path from table and change the size of window

package taskTracker.Windows;

import taskTracker.TaskTracker;
import taskTracker.Utils.themeUtils;
import taskTracker.AppData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static taskTracker.Utils.IconUtils.resizeIcon;
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

    private static final int ICON_WIDTH = 28;
    private static final int ICON_HEIGHT = 28;
    private static final int WINDOW_WIDTH = ICON_WIDTH*16;
    private static final int WINDOW_HEIGHT = 250;

    // Variables

    public int timeOfWork = 0;
    public int timeCounter = 0;
    public int processIdActual;
    public int processIdPrevious;
    private ArrayList<Integer> timeValuesArray = new ArrayList<>();

    // Components

    private final JLabel iconLabel = new JLabel();
    private final JTextField textField = new JTextField();
    private final JLabel timeLabel = new JLabel();
    private final JPanel topPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final DefaultTableModel tableModel = new DefaultTableModel() {
        // Disable editing table rows.
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel dateLabel = new JLabel();

    /**
     * The TaskTrackerFrame class is a GUI-based component for tracking and displaying
     * information about active system windows, including the associated process, time
     * tracking, and UI updates. This class extends JFrame and provides a structured
     * user interface to interact with the window tracking functionality.
     */
    public TaskTrackerFrame() {
        super("Task Tracker");

        timeValuesArray = new ArrayList<>();

        themeUtils.applyGlobalTheme();
        initializeUI();

        // Load persisted app data and populate table
        AppData.getInstance().loadFromFile();
        java.util.List<AppData.TrackedApp> savedApps = AppData.getInstance().getAllApps();
        for (AppData.TrackedApp app : savedApps) {
            ImageIcon savedIcon = AppData.getInstance().loadIcon(app.getIconFileName());
            ImageIcon resizedSavedIcon = savedIcon != null ? resizeIcon(savedIcon, ICON_WIDTH, ICON_HEIGHT) : null;
            tableModel.insertRow(0, new Object[]{
                    resizedSavedIcon,
                    app.getTitle(),
                    app.getPath(),
                    formatCountedTime(app.getTimeInSeconds())
            });
            timeValuesArray.add(0, app.getTimeInSeconds());
        }
        sortTableByTime();

        // Save data on window close
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Before saving, ensure the current active app time is persisted
                try {
                    // Find current app in table and persist its time
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        Object pathObj = tableModel.getValueAt(i, 2);
                        if (pathObj != null) {
                            String path = pathObj.toString();
                            int seconds = timeValuesArray.get(i);
                            String title = String.valueOf(tableModel.getValueAt(i, 1));
                            ImageIcon icon = (ImageIcon) tableModel.getValueAt(i, 0);
                            AppData.getInstance().addOrUpdateApp(title, path, seconds, icon);
                        }
                    }
                } catch (Exception ignored) {}
                AppData.getInstance().saveToFile();
            }
        });

        Timer timer = new Timer(1000, e -> {
            TaskTracker.trackActiveWindow();
            updateLabel();

            if (processIdActual != TaskTracker.getProcessId()) {
                processIdPrevious = processIdActual;
                processIdActual = TaskTracker.getProcessId();
                System.out.println("Process ID changed from " + processIdPrevious + " to " + processIdActual);
                createTable();
            }
            timeCounter++; timeOfWork++;
            updateTable();
        });
        timer.start();

        // Initialization of the actual process ID. Run only once.
        TaskTracker.trackActiveWindow();
        if (processIdActual != TaskTracker.getProcessId()) {
            processIdActual = TaskTracker.getProcessId();
            processIdPrevious = processIdActual;
            createTable();
            System.out.println("Process ID changed");
        }

    }

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
        bottomPanel.setLayout(new BorderLayout(8, 8));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        textField.setText(TaskTracker.activeWindowTitle);
        textField.setEditable(false);
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(timeLabel, BorderLayout.WEST);
        bottomPanel.add(dateLabel, BorderLayout.EAST);

        // Table layout

        tableModel.addColumn("Icon");
        tableModel.addColumn("Title");
        tableModel.addColumn("Path");
        tableModel.addColumn("Time");
        table.setRowHeight(ICON_HEIGHT);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMaxWidth(ICON_WIDTH);
        table.getColumnModel().getColumn(1).setMaxWidth(ICON_WIDTH*3);
        table.getColumnModel().getColumn(2).setMaxWidth(ICON_WIDTH*10);
        table.getColumnModel().getColumn(3).setMaxWidth(ICON_WIDTH*2);
        table.setShowGrid(false);

        /*
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

        // Theme

        themeUtils.stylePanel(panel);
        themeUtils.stylePanel(topPanel);
        themeUtils.stylePanel(bottomPanel);
        themeUtils.styleLabel(timeLabel);
        themeUtils.styleLabel(dateLabel);
        themeUtils.styleTextField(textField);
        themeUtils.styleScrollPane(scrollPane);
        themeUtils.styleTable(table);
        setVisible(true);
        themeUtils.applyDarkTitleBar(this);

    }

    /**
     * Updates the main label in the top panel.
     * This method updates the icon and title labels with the current active window information.
     * It also updates the time label with the current time duration.
     */
    private void updateLabel() {
        ImageIcon icon = TaskTracker.getCurrentIcon();
        ImageIcon resizedIcon = null;
        if (icon != null) {
            resizedIcon = resizeIcon(icon, ICON_WIDTH, ICON_HEIGHT);
            iconLabel.setIcon(resizedIcon);
        } else {
            iconLabel.setIcon(null);
        }

        textField.setText(TaskTracker.activeWindowProcessPath);
        timeLabel.setText("Time: " + formatCountedTime(timeOfWork));
        dateLabel.setText(String.valueOf(TaskTracker.currentDate));
    }

    /**
     * Creates a new row in the table with the current active window information.
     */
    private void createTable() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 2).equals(TaskTracker.activeWindowProcessPath)) {
                return;
            }
        }

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
                TaskTracker.activeWindowProcessPath,
                0
        });
        timeValuesArray.add(0, 0);
        // Persist new app with initial time
        AppData.getInstance().addOrUpdateApp(TaskTracker.activeWindowTitle, TaskTracker.activeWindowProcessPath, 0, TaskTracker.getCurrentIcon());
    }

    /**
     * Updates the table with the previous active window information.
     * It updates the icon, title and time labels with the current active window information.
     */
    private void updateTable() {

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 2).equals(TaskTracker.activeWindowProcessPath)) {
                // Calculate time actually counted with the time stored in the array.
                int timeCounted = timeCounter + timeValuesArray.get(i);
                // Set the new time value in the array and table.
                timeValuesArray.set(i, timeCounted);
                tableModel.setValueAt(formatCountedTime(timeCounted), i, 3);
                timeCounter = 0;
                // Persist updated time for the active app
                AppData.getInstance().addOrUpdateApp(TaskTracker.activeWindowTitle, TaskTracker.activeWindowProcessPath, timeCounted, TaskTracker.getCurrentIcon());
                sortTableByTime();
                return;
                }
            }
    }

    /**
     * Formats the time duration in a human-readable format.
     */
    private String formatCountedTime(int time) {
        String timeStr = "";
        if (time >= 3600) {
            timeStr += time / 3600 + "h ";
            time %= 3600;
        }
        if (time >= 60) {
            timeStr += time / 60 + "m ";
            time %= 60;
        }
        timeStr += time + "s";
        return timeStr;
    }

    /**
     * Sorts the rows of the table in descending order based on their corresponding time values.
     * The method uses an external list of time values and matches them with the rows in
     * the table model to rearrange data accordingly.
     */
    private void sortTableByTime() {
        // Create a list of indices.
        java.util.List<Integer> indices = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            indices.add(i);
        }

        // Sort indices based on time values (descending order).
        indices.sort((i1, i2) -> Integer.compare(timeValuesArray.get(i2), timeValuesArray.get(i1)));

        // Create temporary storage for sorted data
        java.util.List<Object[]> sortedRows = new java.util.ArrayList<>();
        java.util.List<Integer> sortedTimes = new java.util.ArrayList<>();

        for (int idx : indices) {
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                row[col] = tableModel.getValueAt(idx, col);
            }
            sortedRows.add(row);
            sortedTimes.add(timeValuesArray.get(idx));
        }

        // Update table model and time array with sorted data
        for (int i = 0; i < sortedRows.size(); i++) {
            Object[] row = sortedRows.get(i);
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                tableModel.setValueAt(row[col], i, col);
            }
            timeValuesArray.set(i, sortedTimes.get(i));
        }

        tableModel.fireTableDataChanged();

    }

}


