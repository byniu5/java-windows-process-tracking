package taskTracker;
// TODO
//  Namespace for Interfaces package
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.ptr.IntByReference;
import taskTracker.Interfaces.Kernel32;
import taskTracker.Interfaces.Shell32;
import taskTracker.Interfaces.User32;

public class TaskTracker {

    public String activeWindowTitle;
    private WinDef.HICON currentIcon;
    User32 user32 = User32.INSTANCE;
    Kernel32 kernel32 = Kernel32.INSTANCE;
    Shell32 shell32 = Shell32.INSTANCE;

    public WinDef.HICON getCurrentIcon() {
        return currentIcon;
    }

    public void trackActiveWindow() {

        WinDef.HWND hwnd = user32.GetForegroundWindow();

        byte[] windowText = new byte[512];
        byte[] className = new byte[512];
        IntByReference lpdwProcessId = new IntByReference();

        user32.GetWindowTextA(hwnd, windowText, 512);
        user32.GetClassNameA(hwnd, className, 512);
        user32.GetWindowThreadProcessId(hwnd, lpdwProcessId);
        WinDef.HWND parent = user32.GetParent(hwnd);

        WinNT.HANDLE processHandle = kernel32.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION, false, lpdwProcessId.getValue());

        // Convert to string.

        String windowTitleStr = Native.toString(windowText);
        String classNameStr = Native.toString(className);
        String processIdStr = lpdwProcessId.getValue() + "";

        String parentTitleStr = "";
        if (parent != null) {
            byte[] parentWindowText = new byte[512];
            user32.GetWindowTextA(parent, parentWindowText, 512);
            parentTitleStr = Native.toString(parentWindowText);
        }

        String processPath = "";
        if (processHandle != null) {
            byte[] exePathBuffer = new byte[1024];
            // Converting process ID to a executable file path, then saving it to a string.
            int result = Psapi.INSTANCE.GetModuleFileNameExA(processHandle, null, exePathBuffer, exePathBuffer.length);
            if (result > 0) {
                processPath = Native.toString(exePathBuffer);
            }
            kernel32.CloseHandle(processHandle);
        }

        // Icons

        if (!processPath.isEmpty()) {
            WinDef.HICON[] largeIcons = new WinDef.HICON[1];
            // Extracting only large icon from file path.
            int result = shell32.ExtractIconExA(processPath, 0, largeIcons, null, 1);
            if (result > 0 && largeIcons[0] != null) {
                if (currentIcon != null) {
                    user32.DestroyIcon(currentIcon);
                }
                currentIcon = largeIcons[0];
            }
        }

        // Output

        activeWindowTitle = windowTitleStr + " - " + classNameStr + " - " + processIdStr + " - " + parentTitleStr + " - " + processPath;

    }
}
