// Code for displaying icon associated with an executable
// https://www.rgagnon.com/javadetails/java-0439.html
package taskTracker;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.ptr.IntByReference;
import taskTracker.Interfaces.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This class interacts with native Windows APIs using JNI to extract the
 * relevant information about the active application. Captured data includes
 * the process name, process ID, window title, and other metadata such as
 * the system icon and display name of the associated application.
 */
public class TaskTracker {

    User32 user32 = User32.INSTANCE;
    Kernel32 kernel32 = Kernel32.INSTANCE;

    // Variables

    public String activeWindowTitle;
    public String activeWindowProcessPath;
    public String activeWindowTitle1;
    private ImageIcon currentIcon;
    private int processId;

    // Methods

    public ImageIcon getCurrentIcon() {
        return currentIcon;
    }
    
    public int getProcessId() {
        return processId;
    }

    /**
     * Tracks information about the currently active window on the system.
     * This method retrieves various details of the active window, including
     * - Window title and class name.
     * - Parent window title (if applicable).
     * - Associated process ID and executable path.
     * - Icon and display name of the process associated with the active window.
     */
    public void trackActiveWindow() {

        // Retrieve window information.

        WinDef.HWND hwnd = user32.GetForegroundWindow();

        byte[] windowText = new byte[512];
        byte[] className = new byte[512];
        IntByReference lpdwProcessId = new IntByReference();

        user32.GetWindowTextA(hwnd, windowText, 512);
        user32.GetClassNameA(hwnd, className, 512);
        user32.GetWindowThreadProcessId(hwnd, lpdwProcessId);
        WinDef.HWND parent = user32.GetParent(hwnd);

        // Use of Kernel32 Instance to get process ID.

        WinNT.HANDLE processHandle = kernel32.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION, false, lpdwProcessId.getValue());
        processId = lpdwProcessId.getValue();

        // Convert retrieved information to easily accessible strings.

        String windowTitleStr = Native.toString(windowText);
        String classNameStr = Native.toString(className);

        String parentTitleStr = "";
        if (parent != null) {
            byte[] parentWindowText = new byte[512];
            user32.GetWindowTextA(parent, parentWindowText, 512);
            parentTitleStr = Native.toString(parentWindowText);
        }

        String processPath = "";
        if (processHandle != null) {
            byte[] exePathBuffer = new byte[1024];
            // Based on process ID, find the path of an executable file, then save it to a string.
            int result = Psapi.INSTANCE.GetModuleFileNameExA(processHandle, null, exePathBuffer, exePathBuffer.length);
            if (result > 0) {
                processPath = Native.toString(exePathBuffer);
            }
            kernel32.CloseHandle(processHandle);
        }

        // Get icon and display name.

        ImageIcon icon = null;
        String displayName = "";
        try {
            if (!processPath.isEmpty()) {
                File file = new File(processPath);
                if (file.exists()) {
                    // Create an instance of FileSystemView to get a file from the provided processPath.
                    javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
                    
                    // Get the name of the executable file from the provided file.
                    displayName = fsv.getSystemDisplayName(file);

                    // Get the icon of the executable file from the provided file.
                    Icon fileIcon = fsv.getSystemIcon(file);
                    if (fileIcon instanceof ImageIcon) {
                        icon = (ImageIcon) fileIcon;
                    } else {
                        // Convert Icon to ImageIcon if needed.
                        icon = new ImageIcon(IconUtils.iconToImage(fileIcon));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to extract icon: " + e.getMessage());
            // Optionally set a default icon here
            icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        }

        // Output.

        displayName = displayName.replaceAll(".exe", "");
        activeWindowTitle = displayName;
        activeWindowProcessPath = processPath;
        //FOR TESTING activeWindowTitle1 = windowTitleStr + " - " + classNameStr + " - " + processId + " - " + parentTitleStr + " - " + processPath + " - " + displayName;
        currentIcon = icon;

    }
}
