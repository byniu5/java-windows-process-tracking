package taskTracker.Interfaces;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32 extends StdCallLibrary {
    Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

    WinNT.HANDLE OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);

    int GetModuleFileNameExA(WinNT.HANDLE hProcess, WinNT.HANDLE hModule, byte[] lpFilename, int nSize);

    void CloseHandle(WinNT.HANDLE hObject);

}