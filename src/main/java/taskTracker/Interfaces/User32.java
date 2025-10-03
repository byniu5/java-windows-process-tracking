package taskTracker.Interfaces;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

/**
 * <p>Key interfaces include:</p>
 * <ul>
 *   <li>{@link taskTracker.Interfaces.User32} - User interface functions from user32.dll
 *       (window management, foreground window detection)</li>
 *   <li>{@link taskTracker.Interfaces.Kernel32} - Core Windows functions from kernel32.dll
 *       (process management, file path retrieval)</li>
 *   <li>{@link taskTracker.Interfaces.Shell32} - Shell functions from shell32.dll
 *       (icon extraction from executables)</li>
 * </ul>
 *
 * <p>All interfaces extend {@link com.sun.jna.win32.StdCallLibrary} and follow the
 * Windows API calling conventions. Each interface provides a singleton INSTANCE
 * for accessing the native library functions.</p>
 *
 */

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

    boolean DestroyIcon(WinDef.HICON hIcon);
}


