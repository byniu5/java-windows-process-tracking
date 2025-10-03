// Code for displaying icon associated with an executable
// https://www.rgagnon.com/javadetails/java-0439.html
package taskTracker;
// TODO
//  Namespace for Interfaces package
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.ptr.IntByReference;
import taskTracker.Interfaces.Kernel32;
import taskTracker.Interfaces.User32;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TaskTracker {

    public String activeWindowTitle;
    private ImageIcon currentIcon;
    private int processIdStr;
    User32 user32 = User32.INSTANCE;
    Kernel32 kernel32 = Kernel32.INSTANCE;

    public ImageIcon getCurrentIcon() {
        return currentIcon;
    }
    
    public int getProcessIdStr() {
        return processIdStr;
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
        processIdStr = lpdwProcessId.getValue();

        String parentTitleStr = "";
        if (parent != null) {
            byte[] parentWindowText = new byte[512];
            user32.GetWindowTextA(parent, parentWindowText, 512);
            parentTitleStr = Native.toString(parentWindowText);
        }

        String processPath = "";
        if (processHandle != null) {
            byte[] exePathBuffer = new byte[1024];
            // Converting process ID to an executable file path, then saving it to a string.
            int result = Psapi.INSTANCE.GetModuleFileNameExA(processHandle, null, exePathBuffer, exePathBuffer.length);
            if (result > 0) {
                processPath = Native.toString(exePathBuffer);
            }
            kernel32.CloseHandle(processHandle);
        }

        // Icons and display names

        ImageIcon icon = null;
        String displayName = "";
        try {
            if (!processPath.isEmpty()) {
                File file = new File(processPath);
                if (file.exists()) {
                    // Use FileSystemView (public API) instead
                    javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
                    
                    // Get system, display name
                    displayName = fsv.getSystemDisplayName(file);
                    
                    Icon fileIcon = fsv.getSystemIcon(file);
                    if (fileIcon instanceof ImageIcon) {
                        icon = (ImageIcon) fileIcon;
                    } else {
                        // Convert Icon to ImageIcon if needed
                        icon = new ImageIcon(IconUtils.iconToImage(fileIcon));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to extract icon: " + e.getMessage());
            // Optionally set a default icon here
            icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        }

        // Output

        activeWindowTitle = windowTitleStr + " - " + classNameStr + " - " + processIdStr + " - " + parentTitleStr + " - " + processPath + " - " + displayName;
        currentIcon = icon;

    }
}
