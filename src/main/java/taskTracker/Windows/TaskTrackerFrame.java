// TODO Consider moving Timer into TaskTracker                                              Not necessary
//      Because I think it's not a good idea to have it in the frame
//      Code will be more complicated and not so clean but it I'm not sure
//      if it's good practise to have it in the frame
// TODO Previously tracked apps should be saved with the counted time
// TODO If we return to the app that we counted before, we should continue counting
// TODO counters storage
// TODO Table with tracked apps

package taskTracker.Windows;

import taskTracker.TaskTracker;
import javax.swing.*;
import java.awt.*;
import static taskTracker.IconUtils.resizeIcon;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TaskTrackerFrame extends JFrame {

    private final TaskTracker TaskTracker = new TaskTracker();

    public int timeCounter = 0;
    public int processIdActual;
    public int processIdPrevious;

    private final JLabel label2 = new JLabel();
    private final JLabel iconLabel = new JLabel();
    private final JTextField textField = new JTextField();

    private final JPanel topPanel = new JPanel();
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    public TaskTrackerFrame() {
        super("Task Tracker");

        initializeUI();

        Timer timer = new Timer(3000, e -> {
            TaskTracker.trackActiveWindow();
            updateLabel(processIdActual);
            timeCounter+=3;

            if (processIdActual != TaskTracker.getProcessIdStr()) {
                processIdPrevious = processIdActual;
                processIdActual = TaskTracker.getProcessIdStr();
                System.out.println("Process ID changed from " + processIdPrevious + " to " + processIdActual);


               //if (processIdPrevious == TaskTracker.getProcessIdStr()) {
                updateTable();
                //}
            }

        });
        timer.start();

        // Initialization of the actual process ID. Run only once.
        TaskTracker.trackActiveWindow();
        if (processIdActual != TaskTracker.getProcessIdStr()) {
            processIdActual = TaskTracker.getProcessIdStr();
            processIdPrevious = processIdActual;
            System.out.println("Process ID changed");
        }
    }

    private void initializeUI() {

        //Layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(panel);

        topPanel.setLayout(new BorderLayout(8, 8));
        panel.add(topPanel, BorderLayout.NORTH);

        textField.setText(TaskTracker.activeWindowTitle);
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(textField, BorderLayout.CENTER);
        panel.add(label2, BorderLayout.SOUTH);

        tableModel.addColumn("Icon");
        tableModel.addColumn("Window Title");
        tableModel.addColumn("Time (seconds)");
        table.setRowHeight(64);
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

    private void updateLabel(int processIdActual) {
        ImageIcon icon = TaskTracker.getCurrentIcon();
        if (icon != null) {
            ImageIcon resizedIcon = resizeIcon(icon, 64, 64);
            iconLabel.setIcon(resizedIcon);
        } else {
            iconLabel.setIcon(null);
        }

        textField.setText(TaskTracker.activeWindowTitle);
        label2.setText("Time: " + timeCounter);

        }

    private void updateTable() {

            ImageIcon icon = TaskTracker.getCurrentIcon();
            ImageIcon resizedIcon = null;
            if (icon != null) {
                resizedIcon = resizeIcon(icon, 64, 64);
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
