package taskTracker.Interfaces;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
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

public interface Kernel32 extends StdCallLibrary {
    Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

    WinNT.HANDLE OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);

    int GetModuleFileNameExA(WinNT.HANDLE hProcess, WinNT.HANDLE hModule, byte[] lpFilename, int nSize);

    void CloseHandle(WinNT.HANDLE hObject);

}