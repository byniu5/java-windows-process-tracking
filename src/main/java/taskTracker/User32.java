package taskTracker;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;


public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    // Example function: get a handle to a foreground window
    WinDef.HWND GetForegroundWindow();

    // Example function: get window title text
    void GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);

}
