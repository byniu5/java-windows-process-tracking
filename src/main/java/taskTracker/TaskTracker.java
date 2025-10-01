package taskTracker;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import javax.swing.*;

public class TaskTracker {

    public String activeWindowTitle;
    //private Timer timer;
    User32 user32 = User32.INSTANCE;
    //int timeCounter = 0;

    public void trackActiveWindow() {

        WinDef.HWND hwnd = user32.GetForegroundWindow();
        byte[] windowText = new byte[512];
        user32.GetWindowTextA(hwnd, windowText, 512);
        activeWindowTitle = Native.toString(windowText);

    }

//    public void refresher() {
//        timer = new Timer(1000, e -> {
//            trackActiveWindow();
//            timeCounter++;
//        });
//        timer.start();
//    }

}
