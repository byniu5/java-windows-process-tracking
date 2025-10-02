package taskTracker;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;


public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    // Get a handle to a foreground window.
    // Starting point for all the above functions.
    WinDef.HWND GetForegroundWindow();

    // Get window title text.
    void GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);

    void GetClassNameA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);
    void GetWindowThreadProcessId(WinDef.HWND hWnd, IntByReference lpdwProcessId);
    WinDef.HWND GetParent(WinDef.HWND hWnd);
}


