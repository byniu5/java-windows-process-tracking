package taskTracker.Interfaces;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public interface Shell32 extends StdCallLibrary {
    Shell32 INSTANCE = Native.load("shell32", Shell32.class);

    /**
     * Extracts icons from an executable file, DLL, or icon file.
     *
     * @param lpszFile Path to the file containing icons
     * @param nIconIndex Index of the first icon to extract (-1 to get icon count)
     * @param phiconLarge Array to store large icon handles
     * @param phiconSmall Array to store small icon handles
     * @param nIcons Number of icons to extract
     * @return Number of icons extracted, or total icon count if nIconIndex is -1
     */
    int ExtractIconExA(String lpszFile, int nIconIndex, WinDef.HICON[] phiconLarge,
                       WinDef.HICON[] phiconSmall, int nIcons);
}
