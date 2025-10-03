//package taskTracker.Windows;
//
//import taskTracker.TaskTracker;
//
//import javax.swing.*;
//import java.awt.*;
//
//import static taskTracker.IconUtils.resizeIcon;
//
//public class TableFrame extends JFrame {
//
//    private void updateLabel(int processIdActual) {
//        ImageIcon icon = TaskTracker.getCurrentIcon();
//        if (icon != null) {
//            ImageIcon resizedIcon = resizeIcon(icon, 64, 64);
//            iconLabel.setIcon(resizedIcon);
//        } else {
//            iconLabel.setIcon(null);
//        }
//
//        textField.setText(TaskTracker.activeWindowTitle);
//        label2.setText("Time: " + timeCounter);
//
//    }
//
//}
