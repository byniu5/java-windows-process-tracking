package taskTracker;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;

import javax.swing.*;

public class TaskTracker {

    public String activeWindowTitle;
    //private Timer timer;
    User32 user32 = User32.INSTANCE;
    //int timeCounter = 0;

    public void trackActiveWindow() {

        WinDef.HWND hwnd = user32.GetForegroundWindow();

        byte[] windowText = new byte[512];
        byte[] className = new byte[512];
        IntByReference lpdwProcessId = new IntByReference();

        user32.GetWindowTextA(hwnd, windowText, 512);
        user32.GetClassNameA(hwnd, className, 512);
        user32.GetWindowThreadProcessId(hwnd, lpdwProcessId);
        WinDef.HWND parent = user32.GetParent(hwnd);

        String windowTitleStr = Native.toString(windowText);
        String classNameStr = Native.toString(className);
        String processIdStr = lpdwProcessId.getValue() + "";
        String parentTitleStr = "";
        if (parent != null) {
            byte[] parentWindowText = new byte[512];
            user32.GetWindowTextA(parent, parentWindowText, 512);
            parentTitleStr = Native.toString(parentWindowText);
        }
        activeWindowTitle = windowTitleStr + " - " + classNameStr + " - " + processIdStr + " - " + parentTitleStr;

    }

//    public void refresher() {
//        timer = new Timer(1000, e -> {
//            trackActiveWindow();
//            timeCounter++;
//        });
//        timer.start();
//    }

}
