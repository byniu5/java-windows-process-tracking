package taskTracker;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;

public class TaskTracker {

    public String activeWindowTitle;
    User32 user32 = User32.INSTANCE;

    public void trackActiveWindow() {

    WinDef.HWND hwnd = user32.GetForegroundWindow();
    byte[] windowText = new byte[512];
    user32.GetWindowTextA(hwnd, windowText, 512);
    activeWindowTitle = Native.toString(windowText);

    }
}
