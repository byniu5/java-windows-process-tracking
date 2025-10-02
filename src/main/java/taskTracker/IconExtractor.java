package taskTracker;

import com.sun.jna.platform.win32.WinDef;
import taskTracker.Interfaces.Shell32;
import taskTracker.Interfaces.User32;

public class IconExtractor {

    private final Shell32 shell32 = Shell32.INSTANCE;
    private final User32 user32 = User32.INSTANCE;

    /**
     * Extract just the large icon (typically 32x32)
     */
    public WinDef.HICON extractIcon(String exePath) {
        WinDef.HICON[] largeIcons = new WinDef.HICON[1];

        // Pass null for smallIcons since we don't need them
        int result = shell32.ExtractIconExA(exePath, 0, largeIcons, null, 1);

        if (result > 0 && largeIcons[0] != null) {
            return largeIcons[0];
        }
        return null;
    }
}
